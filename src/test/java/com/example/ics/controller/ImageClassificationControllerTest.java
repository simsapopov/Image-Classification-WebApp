package com.example.ics.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@TestConfiguration
class ImageClassificationControllerTest {
    private static String tagSaved;
    private static String imgIdSaved;
    private static Response responseImg;
    private static List<String> tagList;
    private static int numOfImages;
    private static String imgId;
    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8079;

    }
    @Test
    public void testClassifyImagga_ExceptionHandling() {
        given()
                .contentType("application/json")
                .body("{\"imageUr\":\"https://example.com/image.jpg\"}")
                .when()
                .post("/api/v2/classify/imagga")
                .then()
                .assertThat()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .contentType("text/plain")
                .content(is("An error occurred: No value for imageUrl"));
    }
    @Test
    public void testClassifyClarifai_ExceptionHandling() {
        given()
                .contentType("application/json")
                .body("{\"imageUr\":\"https://example.com/image.jpg\"}")
                .when()
                .post("/api/v2/classify/clarifai")
                .then()
                .assertThat()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .contentType("text/plain")
                .content(is("An error occurred: No value for imageUrl"));
    }


    @Order(1)
    @Test
    public void testGetAllImages() {
        Response response = given()
                .get("/api/v2/images")
                .then()
                .statusCode(200)
                .body("[0].id", is(notNullValue()))
                .body("[0].tags", is(notNullValue()))
                .body("[0].name", is(notNullValue()))
                .body("[0].url", is(notNullValue()))
                .body("[0].imgurlUrl", is(notNullValue()))
                .body("[0].analyzedAt", is(notNullValue()))
                .body("[0].hash", is(notNullValue()))
                .extract().response();


        numOfImages = response.jsonPath().getList("").size();
    }

    @Order(2)
    @Test
    public void testClassifyClarifai() {
        String imageUrl = "{\"imageUrl\":\"https://th.bing.com/th/id/R.a4de62f27e25796493e633172f99afb3?rik=1Kfs9c3kFWIKnw&pid=ImgRaw&r=0\"}";


        Response response = given()
                .contentType("text/plain")
                .body(imageUrl)
                .post("/api/v2/classify/clarifai")
                .then()
                .statusCode(200)
                .body(notNullValue())
                .extract().response();

        responseImg=response;
        imgId = response.getBody().asString();
    }
    @Order(3)
    @Test
    public void testClassifyClarifaiAlreadyUploaded() {
        String imageUrl = "{\"imageUrl\":\"https://th.bing.com/th/id/R.a4de62f27e25796493e633172f99afb3?rik=1Kfs9c3kFWIKnw&pid=ImgRaw&r=0\"}";


        Response response = given()
                .contentType("text/plain")
                .body(imageUrl)
                .post("/api/v2/classify/clarifai")
                .then()
                .statusCode(200)
                .body(notNullValue())
                .extract().response();


        Assertions.assertEquals(response.getBody().asString(),responseImg.getBody().asString());
    }


    @Order(4)
    @Test
    public void testGetAllImagesAgain() throws InterruptedException {
        Thread.sleep(1000);
        Response responseAgain = given()
                .get("/api/v2/images")
                .then()
                .statusCode(200)
                .body("[0].id", is(notNullValue()))
                .body("[0].tags", is(notNullValue()))
                .body("[0].name", is(notNullValue()))
                .body("[0].url", is(notNullValue()))
                .body("[0].imgurlUrl", is(notNullValue()))
                .body("[0].analyzedAt", is(notNullValue()))
                .body("[0].hash", is(notNullValue()))
                .extract().response();


        int numOfImagesAgain = responseAgain.jsonPath().getList("").size();


        assertEquals(numOfImages + 1, numOfImagesAgain);
    }

    @Order(6)
    @Test
    public void testGetAllUniqueTags() {

        Response response = given()
                .get("/api/v2/getUniqueTags")
                .then()
                .statusCode(200)
                .body(is(notNullValue()))
                .extract().response();


        tagSaved = (String) response.jsonPath().getList("").get(0);


        assertNotNull(tagSaved);
    }
    @Test
    public void testGetImagesWithTag() {
        String tag = tagSaved;

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("api/v2/{tag}", tag)
                .then()
                .statusCode(200)
                .body("size()", is(not(0)));

    }

    @Order(7)
    @Test
    public void testGetImagesByTag() {

        Response response = given()
                .get("/api/v2/getimage/{tag}", tagSaved)
                .then()
                .statusCode(200)
                .body("[0].id", is(notNullValue()))
                .body("[0].tags", is(notNullValue()))
                .body("[0].name", is(notNullValue()))
                .body("[0].url", is(notNullValue()))
                .body("[0].imgurlUrl", is(notNullValue()))
                .body("[0].analyzedAt", is(notNullValue()))
                .body("[0].hash", is(notNullValue()))
                .extract().response();


        imgIdSaved = response.jsonPath().getString("[0].id");
        System.out.println(imgIdSaved);

        assertNotNull(imgIdSaved);
    }

    @Order(8)
    @Test
    public void testGetImageById() {

        Response response = given()
                .get("/api/v2/images/{id}", imgIdSaved
                )
                .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("tags", is(notNullValue()))
                .body("name", is(notNullValue()))
                .body("url", is(notNullValue()))
                .body("imgurlUrl", is(notNullValue()))
                .body("analyzedAt", is(notNullValue()))
                .body("hash", is(notNullValue()))
                .extract().response();


        List<Map<String, ?>> tags = response.jsonPath().getList("tags");
        List<String> tagList = new ArrayList<>();
        for (Map<String, ?> tag : tags) {
            if (tag.containsKey("tag")) {
                tagList.add(tag.get("tag").toString());
            }
        }

        System.out.println(tagList.toString());
        System.out.println(tagSaved);
        assertTrue(tagList.contains(tagSaved));
    }
    @Order(9)
    @Test
    public void testGetReplaceTags() {
        Response response = given()
                .header("Accept", ContentType.JSON.toString()) // Update content-type header
                .get("/api/v2/replacetags/{id}", imgIdSaved)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("[0].id", is(notNullValue()))
                .body("[0].tag", is(notNullValue()))
                .body("[0].confidencePercentage", is(notNullValue()))
                .extract().response();
        List<String> tags = response.jsonPath().getList("tags");
        assertFalse(tags.equals(tagList));
    }




}
