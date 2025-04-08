## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

SupportDesk/
│
├── controller/
│ ├── LoginController.java
│ ├── WelcomeController.java
│ ├── IssueController.java
│ ├── ResponseController.java
│ └── AdminController.java
│
├── model/
│ ├── User.java
│ ├── Issue.java
│ ├── Response.java
│ └── Admin.java
│
├── view/
│ ├── LoginView.java
│ ├── WelcomeView.java
│ ├── PostIssueView.java
│ ├── ProfileView.java
│ └── AdminDashboardView.java
│
├── service/ <== optional layer for logic between model & controller
│ └── AuthService.java
│
├── util/
│ └── DBConnection.java (if using DB later)
│ └── Session.java (holds current logged-in user)
│
└── Main.java
