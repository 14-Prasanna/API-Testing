package com.http;

import com.hooks.hooks;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class loginAPi extends hooks {

    public static String token;

    @Test(priority = 2)
    public void ValidLogin() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", "sam@gmail.com");
        payload.put("password", "123");

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post("/user/login")
                .then()
                .statusCode(201)
                .body("token", notNullValue())
                .extract()
                .response();

        token = response.jsonPath().getString("token");

        System.out.println("Token: " + token);
    }
}