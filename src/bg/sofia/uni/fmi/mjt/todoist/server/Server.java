package bg.sofia.uni.fmi.mjt.todoist.server;

import bg.sofia.uni.fmi.mjt.todoist.command.CommandCreator;
import bg.sofia.uni.fmi.mjt.todoist.command.CommandExecutor;
import bg.sofia.uni.fmi.mjt.todoist.storage.CollaborationsStorage;
import bg.sofia.uni.fmi.mjt.todoist.storage.UsersStorage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Server {
    private static final String SUCCESSFUL_REGISTRATION = "Successful registration";
    private static final String SUCCESSFUL_LOGIN = "Successful login";
    private static final String DISCONNECTED = "Disconnected";
    private static final String REGEX = ": ";
    private static final int BUFFER_SIZE = 1024;
    private static final int PORT = 7777;
    private static final String HOST = "localhost";
    private final CommandExecutor commandExecutor;
    private static final String DISCONNECT_COMMAND = "disconnect";
    private static final String COLLABORATIONS_FILE_NAME = "collaborations.dat";
    private static final String USERS_FILE_NAME = "users.dat";
    private Map<SelectionKey, String> loggedUsers;

    private final int port;
    private boolean isServerWorking;

    private ByteBuffer buffer;
    private Selector selector;

    public Server(int port, CommandExecutor commandExecutor) {
        this.port = port;
        this.commandExecutor = commandExecutor;
        this.loggedUsers = new HashMap<>();
    }

    public static void main(String[] args) {
        UsersStorage usersStorage = new UsersStorage();
        CollaborationsStorage collaborationsStorage = new CollaborationsStorage();
        usersStorage.read(USERS_FILE_NAME);
        collaborationsStorage.read(COLLABORATIONS_FILE_NAME);
        CommandExecutor commandExecutor = new CommandExecutor(usersStorage, collaborationsStorage);

        Server server = new Server(PORT, commandExecutor);
        server.start();
    }

    public void start() {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            selector = Selector.open();
            configureServerSocketChannel(serverSocketChannel, selector);
            this.buffer = ByteBuffer.allocate(BUFFER_SIZE);
            isServerWorking = true;
            while (isServerWorking) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        continue;
                    }

                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();
                        if (key.isReadable()) {
                            SocketChannel clientChannel = (SocketChannel) key.channel();
                            String clientInput = getClientInput(clientChannel);
                            if (clientInput == null) {
                                continue;
                            }

                            String output = commandExecutor.execute(CommandCreator.newCommand(clientInput),
                                loggedUsers.getOrDefault(key, null));
                            addUserIfNeeded(output, key);
                            removeUserIfNeeded(output, key);

                            writeClientOutput(clientChannel, output);

                        } else if (key.isAcceptable()) {
                            accept(selector, key);
                        }

                        keyIterator.remove();
                    }
                } catch (IOException e) {
                    System.out.println("Error occurred while processing client request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("failed to start server", e);
        }
    }

    public void stop() {
        this.isServerWorking = false;
        if (selector.isOpen()) {
            selector.wakeup();
        }
    }

    private void addUserIfNeeded(String commandResponse, SelectionKey key) {
        if (commandResponse.startsWith(SUCCESSFUL_LOGIN) || commandResponse.startsWith(SUCCESSFUL_REGISTRATION)) {
            String[] tokens = commandResponse.split(REGEX);
            String username = tokens[1];
            loggedUsers.put(key, username);
        }
    }

    private void removeUserIfNeeded(String commandResponse, SelectionKey key) {
        if (commandResponse.startsWith(DISCONNECTED)) {
            loggedUsers.remove(key);
        }
    }

    private void configureServerSocketChannel(ServerSocketChannel channel, Selector selector) throws IOException {
        channel.bind(new InetSocketAddress(HOST, this.port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private String getClientInput(SocketChannel clientChannel) throws IOException {
        buffer.clear();

        int readBytes = clientChannel.read(buffer);
        if (readBytes < 0) {
            clientChannel.close();
            return null;
        }

        buffer.flip();

        byte[] clientInputBytes = new byte[buffer.remaining()];
        buffer.get(clientInputBytes);

        return new String(clientInputBytes, StandardCharsets.UTF_8);
    }

    private void writeClientOutput(SocketChannel clientChannel, String output) throws IOException {
        buffer.clear();
        buffer.put(output.getBytes());
        buffer.flip();

        clientChannel.write(buffer);
    }

    private void accept(Selector selector, SelectionKey key) throws IOException {
        ServerSocketChannel sockChannel = (ServerSocketChannel) key.channel();
        SocketChannel accept = sockChannel.accept();

        accept.configureBlocking(false);
        accept.register(selector, SelectionKey.OP_READ);
    }
}
