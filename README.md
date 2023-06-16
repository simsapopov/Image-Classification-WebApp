# ICS App Setup Guide

This guide will walk you through the steps to start the ICS (Image Classification System) application, which is a Spring app with an Angular frontend. Before proceeding, make sure you have the following prerequisites installed on your system:

Java Development Kit (JDK)
Node.js and npm (Node Package Manager)
PostgreSQL database

## Installation

Clone the repository:

```bash
git clone https://github.com/simsapopov/ics.git

```

Navigate to the project directory:



```bash
cd ics

```

 1. Configure the backend:

- Open the application.properties file located in src/main/resources.
- Set the following properties based on your environment:
- server.port: The port on which the Spring server will run.
- spring.datasource.url: The URL of your PostgreSQL database.
- spring.datasource.username: The username to access your PostgreSQL database.
- spring.datasource.password: The password to access your PostgreSQL database.
- spring.jpa.database-platform: The Hibernate dialect for your PostgreSQL database.
- spring.jpa.hibernate.ddl-auto: The database schema generation strategy. By default, it  is set to update.
-  imagga.api.key: Your Imagga API key.
- imagga.api.secret: Your Imagga API secret.
2. Create the database:

- Execute the SQL script src/main/resources/database.sql to create the necessary database schema.

3. Frontend setup:

```bash
cd frontend
npm install

```
4. Build the application:
```bash
./mvnw clean package
```

## Running the Application
1. Start the Spring backend:
```bash
./mvnw spring-boot:run
```
The backend server will start on the configured port (default: 8080).

2. Start the Angular frontend:

- Open a new terminal window.

- Navigate to the project directory (if you're not already there).

- Run the following command:
```bash
cd frontend
ng serve
```
The Angular development server will start on the default port (usually 4200).

3. Access the application:

Open your web browser and visit http://localhost:4200 (or the port specified by Angular if you modified it).

You should now be able to use the ICS application and perform image classification tasks.
