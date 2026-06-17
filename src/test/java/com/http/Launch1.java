package com.http;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;

public class Launch1 {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost:5000";
    }

    @Test
    public void landingPage() {

        Response response = RestAssured
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body("message",
                        equalTo("API Testing Practice Server is running"))
                .log().all()
                .extract()
                .response();

        System.out.println(
                "The application launched successfully. Status Code: "
                        + response.getStatusCode());
    }

    @Test
    public void healthCheck() {

        Response response = RestAssured
                .when()
                .get("/api/v1/health")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();

        System.out.println("Health API opens");
        System.out.println("Status code: " + response.getStatusCode());
    }

    @Test
    public void documentation() {

        Response response = RestAssured
                .when()
                .get("/api/v1/docs")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();

        System.out.println("Documentation API opens");
        System.out.println("Status code: " + response.getStatusCode());
    }
}