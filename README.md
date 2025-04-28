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

RMI stands for remote method invocation is one of the technology being used in java to develop a sistributed application where we have a server and client and both are able to communicate over the network.
N.B inorder server and client to communicate they have to use Gateway where client side use STUB ans SKELETON on the server side

1. STUB (cliesnt side)
   a. It initiate the communacation with server
   b. It has to invoke / call / disturb methos exposed by the server
   c. It has to wait the feedback from server side.
   d. It has to communicate the feedvak to the client

2.Skeleton (Server side)

a. It accept /reject incoming request from the client side.

A.steps to configure client side
1.import required libraries
2.create pojo classess without java persistance annotation and it should implement serializable
3.create jaca interface

//method signature or definition
// return type methodName(par)

where every pojo will have it's method

//setting server controller
//configure the properties
system.setProperty
//on serverside you create a registry on client to registre
//registry registry = laca teREgistry.createREgisty
tyr and catcy
if used used bind inn the middle of the they lost connection everything is down
but rebinnd if lose connention you will reconnect
you do all for all methods
registry.rebind("address" ,addreesImplementation)
