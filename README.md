# Image Classification Service webapp 
## Short description
This web application, built with Spring Web and Angular, provides an easy way to classify images using a given URL or by uploading a file. Before the classification process, each image is uploaded to Imgur. The application offers a paginated gallery displaying 10 images, sorted by the date of analysis. The sorting order can be customized, and users can also apply filters by entering complete or partial tags in the input text. Additionally, the top five tags for each image are displayed, allowing users to filter by clicking on any of those tags. Upon classification, users are redirected to the image view page for the analyzed image. Similarly, clicking on an image from the gallery will redirect users to the image view page for that specific image.
## Table of Contents
- [Short Description](#short-description)
- [ICS App Setup Guide](#ics-app-setup-guide)
  - [Installation](#Installation)
  - [Running the Application](#Running-the-Application)
- [API Reference](#api-reference)
  - [Classify](#classify)
  - [Get gallery page](#Get-image-page)
  - [Get all unique tags](#get-all-unique-tags)
  - [Get image information](#get-image-information)
      - [Get all information](#Get-all-information)
      - [Get image message](#Get-image-message)
      - [Get image message](#Get-the-only-the-image-tags)
  - [Replace tags](#replace-tags)
  - [Get photos with given tag](#get-photos-with-given-tag)
- [App demo with screenshots](#App-demo-Screenshots)
  - [Classify page](#Classify-page)
  - [Image view page](#Image-view-page)
   
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
#### Get image page



```http
  GET api/v2/all
```

| Key | Type     | Default value|
| :-------- | :------- | :------------------------- |
| `pageNo` | `int` | 1 |
| `pageSize` | `int` | 10 |
| `direction` | `String` | "asc" |
| `tag` | `String` | The param is not Required. It can be a whole tag or a prefix of tag |

#### Response format for api/v2/all?pageNo=1&pageSize=3&direction=asc&tag=car
The answer have info
- Total pages for this size.
- Current page.
- Number of elements.
- The sorting of the elements is based on the analyzed date.
- Is result empty.
- Tag filter.


```json
{
    "content": [
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
    {
    ...
    // Rest of the response example
    }
    
    ]
     "pageable": {
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 3,
        "pageNumber": 1,
        "pageSize": 3,
        "paged": true,
        "unpaged": false
    },
    "totalPages": 3,
    "totalElements": 9,
    "last": false,
    "size": 3,
    "number": 1,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 3,
    "first": false,
    "empty": false
}

```

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
 ### Get the only the image tags 
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


 ```
## App demo Screenshot
### Classify page
Welcome to our main page!

Here, you will find a user-friendly interface designed to facilitate image classification tasks. Let's explore the available options:

- In the first dropdown menu, we provide two choices for the third-party image classification service. You can select either Imagga or Clarifai, depending on your preference and requirements. These services offer powerful image analysis capabilities to accurately classify and annotate images.

- Moving on to the second dropdown menu, it allows you to specify the input format for the image you wish to classify. You have two options: either provide the image URL directly or upload a file from your local machine. This flexibility ensures that you can conveniently use images from various sources for classification.

Before proceeding with the classification process, our system performs a quick search to determine if the image already exists in our database. This search serves multiple purposes, including resource management, cost optimization, and ensuring a gallery with no repeating images.

By checking if the image is already present, we can avoid unnecessary expenses associated with reclassifying identical images. This approach helps us make efficient use of resources and maintain a cost-effective image classification system.

If the image is found in the database, we retrieve the existing classification results, eliminating the need for redundant computations. This way, we can provide you with instant access to the previously classified image, saving both time and computational resources.

On the other hand, if the image is not found in the database, our system proceeds with the classification process. To ensure the long-term availability and reliability of the image, we upload it to Imgur. This step enhances both flexibility and security, allowing us to access and analyze the image without worrying about it becoming unavailable in the future.

Once the classification process is completed, you are seamlessly redirected to the image view page
- Dark Theme
![App Screenshot](https://i.imgur.com/0EtwHP8.jpeg) 
- Light Theme
![My Remote Image](https://i.imgur.com/QPmM7ld.jpg)

### Image view page

Welcome to the Image View page!
This page provides a comprehensive view of the image you selected, along with additional information and functionality to enhance your classification experience. Let's explore the various elements and features available:

- At the center of the page, the image you selected is  displayed.
- Just below the image, you will find valuable information about the analysis process. This includes the date and time when the image was analyzed, providing you with a timestamp for reference. Additionally, the name of the classification service used for the analysis is displayed, giving you insights into the technology behind the classification results.
- To further enhance your experience, we have included a convenient "Remake Tags" button. By clicking on this button, you can trigger a fresh analysis of the image using the alternative classification service. For example, if you initially used Imagga for classification, clicking the "Remake Tags" button will invoke Clarifai for a new analysis. The system will then save and display the updated tags based on the new analysis. This feature allows you to compare the results of different classification services and choose the most accurate and relevant tags for your needs.
- Continuing below the "Remake Tags" button, you will find a data grid that presents all the tags associated with the image. These tags are accompanied by their respective confidence levels, indicating the level of certainty the classification service has assigned to each tag. To ensure relevancy, only tags with a confidence level of more than 30% are displayed. This filtering helps you focus on the most significant and reliable tags, as those below this threshold may be less relevant to the image.
- To facilitate easy navigation, we have placed a "Back" button in the upper-left corner, just below the logo. Clicking this button will take you back to the previous page, preserving any filters, sorting, or specific page settings you applied in the gallery. Whether you sorted the images, applied filters, or performed any other actions, returning to the previous page will restore your previous view, ensuring a seamless browsing experience.
Dark Theme
![My Remote Image](https://i.imgur.com/2D1H82x.jpg)
Light Theme
![My Remote Image](https://i.imgur.com/nZzGKcJ.jpg)
