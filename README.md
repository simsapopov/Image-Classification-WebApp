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
# API Reference

## Classify

```http
  Post /api/v2/classify/imagga
  ```
  or clarifai if you want the clarifai service
  ```http
  Post /api/v2/classify/clarifai
```

#### Request Payload: Where $input is the img URL or Byte64 formatted img data
```json
  {
  "imageurl": "{input}"
  }
```
#### Response Format 
The API response will be a raw string representing an integer value. The response can be interpreted as follows:

- If the photo was not classified yet, the response will be the ID of the newly classified photo.
- If the photo was already classified, the response will be the ID of the previously classified photo.

## Get all unique tags

```http
  Get /api/v2/getUniqueTags
  ```

#### Response Format 
The API response will be a list of all unique tags like
[
    "happy",
    "church",
    "ocean"]


## Get image information
 ### Get all information

```http
  Get /api/v2/images/{id}
  ```
Replace {id} in the endpoint with the actual ID of the desired photo.
### Response Format 
The response will be in the following format:

```json
 {
  "id": 4,
  "tags": [
    {
      "id": 42,
      "tag": "car",
      "confidencePercentage": 100.0
    },
    {
      "id": 43,
      "tag": "motor vehicle",
      "confidencePercentage": 96.5658493041992
    },
    {
      "id": 44,
      "tag": "coupe",
      "confidencePercentage": 91.2214736938477
    },
    ...
    // More tags
  ],
  "name": "Imagga",
  "url": "https://i.imgur.com/kK21Uub.jpg",
  "imgurlUrl": "https://i.imgur.com/pF7okqQ.jpg",
  "analyzedAt": "2023-06-06T13:23:08.525+00:00",
  "hash": "2c3e0bb148c976e596bbe93f56030385f1e00f3b414597ccf223586f4fbfcd23"
}
  ```
 ### Get image message
```http
  Get /api/v2/message/{id}
  ```
  #### Response Format 
  ```text
 This image was processed on: 2023-06-16 16:58:03.948 by Clarifai
  ```
 ### Or get the only the image tags 
```http
  Get /api/v2/images/{id}/tags
  ```
  ```json
 {
 
  "tags": [
    {
      "id": 42,
      "tag": "car",
      "confidencePercentage": 100.0
    },
    {
      "id": 43,
      "tag": "motor vehicle",
      "confidencePercentage": 96.5658493041992
    },
    {
      "id": 44,
      "tag": "coupe",
      "confidencePercentage": 91.2214736938477
    },
    ...
    // More tags
  ],
  ```

  ## Replace tags
  ```http
  Get /api/v2/replacetags/{id}
  ```
  Where {id} is the photo id.
  It will delete the photo's tags and use the other classify service to make new tags
  #### Response Format 
If there is no photo the response will be 
  ```json
There isn't image with this id
  ```
  Otherwise 
  ```json
 {
 
  "tags": [
    {
      "id": 42,
      "tag": "car",
      "confidencePercentage": 100.0
    },
    {
      "id": 43,
      "tag": "motor vehicle",
      "confidencePercentage": 96.5658493041992
    },
    {
      "id": 44,
      "tag": "coupe",
      "confidencePercentage": 91.2214736938477
    },
    ...
    // More tags
  ],
  ```
## Get photos with given tag

```http
  Get /api/v2/{tag}
  ```
 #### Response Format 
  ```json
[
    {
        "id": 11,
        "tags": [
            {
                "id": 158,
                "tag": "patio",
                "confidencePercentage": 87.5628890991211
            },
            {
                "id": 159,
                "tag": "area",
                "confidencePercentage": 69.8299407958984
            },
            ...
            // Rest of the response example
        ],
        "name": "Imagga",
        "url": "https://i.imgur.com/hXKwYCE.jpg",
        "imgurlUrl": "https://i.imgur.com/JeAVWW0.jpg",
        "analyzedAt": "2023-06-06T13:24:15.837+00:00",
        "hash": "223bbf772c76b0fdf0cafbf7c01533b557547f2ac5eb75f92d73c8d069e06904"
    },
    {
        "id": 13,
        "tags": [
            {
                "id": 196,
                "tag": "building",
                "confidencePercentage": 100.0
            },
            ...
            // Rest of the response example
        ],
        "name": "Imagga",
        "url": "https://i.imgur.com/wkSNSkL.jpg",
        "imgurlUrl": "https://i.imgur.com/m55FusL.jpg",
        "analyzedAt": "2023-06-06T13:24:25.240+00:00",
        "hash": "fc9e6a7fb35429f52f4789dd2ad0a1b892e17b765d0f23312460c954db4cc98f"
    },
    ...
    // Rest of the response example
]

