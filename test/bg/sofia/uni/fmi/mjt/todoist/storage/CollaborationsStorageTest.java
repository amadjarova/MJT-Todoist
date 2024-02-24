package bg.sofia.uni.fmi.mjt.todoist.storage;

import bg.sofia.uni.fmi.mjt.todoist.collaboration.Collaboration;
import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNameAlreadyTakenException;
import bg.sofia.uni.fmi.mjt.todoist.exception.CollaborationNotFoundException;
import bg.sofia.uni.fmi.mjt.todoist.exception.UserNotCreatorException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollaborationsStorageTest {
    private static CollaborationsStorage collaborationsStorage = new CollaborationsStorage();
    private CollaborationsStorage empty = new CollaborationsStorage();
    @BeforeAll
    static void setUpTestCase() throws CollaborationNameAlreadyTakenException {
        collaborationsStorage.addCollaboration("collaboration1", "user1");
        collaborationsStorage.addCollaboration("collaboration2", "user2");
        collaborationsStorage.addCollaboration("collaboration3", "user3");
        collaborationsStorage.addCollaboration("collaboration4", "user4");
        collaborationsStorage.addCollaboration("collaboration5", "user5");

    }

    @Test
    void testAddCollaborationNullName() {
        assertThrows(IllegalArgumentException.class, ()->collaborationsStorage.addCollaboration(null, "az"),
            "addCollaboration should throw IllegalArgumentException when name is null");
    }

    @Test
    void testAddCollaborationCollaborationNameAlreadyTaken() {
        assertThrows(CollaborationNameAlreadyTakenException.class, ()->collaborationsStorage.addCollaboration("collaboration1", "az"),
            "addCollaboration should throw CollaborationNameAlreadyTakenException when there is collaboration with the same name");
    }

    @Test
    void testDeleteCollaborationNullName() {
        assertThrows(IllegalArgumentException.class, ()->collaborationsStorage.deleteCollaboration(null, "2"),
            "deleteCollaboration should throw IllegalArgumentException when name is null");

    }

    @Test
    void testListCollaborationsOfUserNullUser() {
        assertThrows(IllegalArgumentException.class, ()->collaborationsStorage.listCollaborationsOfUser(null),
            "listCollaborationsOfUser should throw IllegalArgumentException when user is null");
    }

    @Test
    void testListCollaborationsOfUserWithNoCollaborations() {
        assertEquals(Collections.emptyList(), collaborationsStorage.listCollaborationsOfUser("user7"),
            "listCollaborations of user should return correct list");
    }

    @Test
    void testListCollaborationsOfUser() {
        assertEquals(List.of(new Collaboration("collaboration3","user3")), collaborationsStorage.listCollaborationsOfUser("user3"),
            "listCollaborations of user should return correct list");
    }

    @Test
    void testDeleteCollaborationFound() throws CollaborationNameAlreadyTakenException, CollaborationNotFoundException, UserNotCreatorException {
        empty.addCollaboration("1", "2");
        assertEquals(List.of(new Collaboration("1", "2")), empty.listCollaborationsOfUser("2"),
            "Failed during setup");

        empty.deleteCollaboration("1", "2");
        assertEquals(Collections.emptyList(), empty.listCollaborationsOfUser("2"),
            "deleteCollaboration should delete collaboration successfully");
    }

    @Test
    void testAddUserToCollaborationNullUser() {
        assertThrows(IllegalArgumentException.class, ()->collaborationsStorage.addUserToCollaboration(null, "a"),
            "addUserToCollaboration should throw IllegalArgumentException when user is null");
    }

    @Test
    void testAddUserToCollaborationCollaborationNotFound() {
        assertThrows(CollaborationNotFoundException.class, ()->collaborationsStorage.addUserToCollaboration("2", "4"),
            "addUserToCollaboration should throw exception when the collaboration is not found");
    }

    @Test
    void testGetCollaboration() throws CollaborationNotFoundException {
        assertEquals(new Collaboration("collaboration1", "user1"), collaborationsStorage.getCollaboration("collaboration1"),
            "getCollaboration should return correct collaboration");
    }

    @Test
    void testSaveAndRead() throws CollaborationNameAlreadyTakenException, CollaborationNotFoundException {
        CollaborationsStorage originalCollaborationsStorage = new CollaborationsStorage();
        originalCollaborationsStorage.addCollaboration("collaboration1", "user1");
        originalCollaborationsStorage.addCollaboration("collaboration2", "user2");
        originalCollaborationsStorage.addCollaboration("collaboration3", "user3");


        String fileName = "testCollaborationsStorage.txt";
        originalCollaborationsStorage.save(fileName);

        CollaborationsStorage newCollaborationsStorage = new CollaborationsStorage();
        newCollaborationsStorage.read(fileName);

        assertEquals(originalCollaborationsStorage.getCollaboration("collaboration1"),
            newCollaborationsStorage.getCollaboration("collaboration1"));
        assertEquals(originalCollaborationsStorage.getCollaboration("collaboration2"),
            newCollaborationsStorage.getCollaboration("collaboration2"));
        assertEquals(originalCollaborationsStorage.getCollaboration("collaboration3"),
            newCollaborationsStorage.getCollaboration("collaboration3"));

        assertTrue(Files.exists(Path.of(fileName)), "File should exist after saving");
    }


}
