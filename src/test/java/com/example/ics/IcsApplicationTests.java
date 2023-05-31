package com.example.ics;

import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IcsApplicationTests {
	public void setup() {
		RestAssured.baseURI = "https://localhost";
		RestAssured.port = 8079;
	}
//	@Test
//	public void whenMeasureResponseTime_thenOK() {
//		RestAssured.baseURI = "http://localhost:8079";
//	RestAssured.port = 8079;
//		Response response = RestAssured.get("/greetings");
//		long timeInMS = response.time();
//		long timeInS = response.timeIn(TimeUnit.SECONDS);
//
//		assertEquals(response.getStatusCode(), 200);
//	}
////TODO
}
