# 📝 Todoist – Console Client-Server Task Manager

A **Java-based client-server application** inspired by **Todoist**, designed to help users manage daily tasks, organize projects, and collaborate with others — directly from the console.

The system supports multiple users who can register, log in, create and manage tasks, collaborate with others, and organize their work using labels.

---

## 🚀 Features

### 🧍 User Management
- `register` – Register a new user with username and password.  
- `login` – Log into the system.  
- User data is stored persistently on the server and reloaded on restart.

---

### ✅ Task Management
Each task includes the following attributes:
- **name** – task name *(required)*
- **date** – target date *(optional)*
- **due date** – deadline *(optional)*
- **description** – task description *(optional)*

Tasks without a date are stored in the **Inbox**.  
Tasks are unique by **name + date** (or just name if in Inbox).

#### Available Commands
- add-task --name=<name> --date=<date> --due-date=<due-date> --description=<description>
- update-task --name=<name> --date=<date> --due-date=<due-date> --description=<description>
- delete-task --name=<name> [--date=<date>]
- get-task --name=<name> [--date=<date>]
- list-tasks [--date=<date>] [--completed=true]
- list-dashboard
- finish-task --name=<name>

### 🤝 Collaborations
Collaborations are **shared projects** where multiple users can add, view, and assign tasks.

Each task inside a collaboration can have an **assignee** (responsible user).

#### Available Commands
- add-collaboration --name=<name>
- delete-collaboration --name=<name>
- list-collaborations
- add-user --collaboration=<name> --user=<username>
- assign-task --collaboration=<name> --user=<username> --task=<name>
- list-tasks --collaboration=<name>
- list-users --collaboration=<name>

## ⚙️ Architecture Overview

### 🖥️ Client
- Console-based Java application.  
- Reads user commands and sends them to the server over TCP sockets.  
- Displays responses in a user-friendly, human-readable format.  
- Supports a `help` command listing all available actions.

### 🌐 Server
- Multithreaded Java server that handles multiple clients simultaneously.  
- Processes commands, manages data, and returns responses.  
- Stores all users, tasks, collaborations, and labels persistently in files.  
- Automatically loads data on startup.

### 💾 Data Persistence
Server data (users, tasks, collaborations) is stored locally in files, ensuring persistence after shutdown.  
