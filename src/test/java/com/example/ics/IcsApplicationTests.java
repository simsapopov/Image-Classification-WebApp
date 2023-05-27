package com.example.ics;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class IcsApplicationTests {
	public void setup() {
		RestAssured.baseURI = "https://localhost";
		RestAssured.port = 8079;
	}
	@Test
	public void whenMeasureResponseTime_thenOK() {
		RestAssured.baseURI = "http://localhost:8079";
//	RestAssured.port = 8079;
		Response response = RestAssured.get("/greetings");
		long timeInMS = response.time();
		long timeInS = response.timeIn(TimeUnit.SECONDS);

		assertEquals(response.getStatusCode(), 200);
	}
//TODO
}
