package com.http;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class HttpMethod {

    int id;
    String token;
    String email;

    @Test(priority = 1)
    public void registerUser() {

        email = "prasanna" + System.currentTimeMillis() + "@gmail.com";

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Prasanna");
        payload.put("email", email);
        payload.put("password", "test123");
        payload.put("role", "admin");

        id = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("http://localhost:5000/api/v1/auth/register")
                .then()
                .statusCode(201)
                .body("data.id", notNullValue())
                .body("data.email", equalTo(email))
                .log().all()
                .extract()
                .jsonPath()
                .getInt("data.id");

        System.out.println("Created User ID: " + id);
    }

    @Test(priority = 2, dependsOnMethods = "registerUser")
    public void loginUser() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("password", "test123");

        token = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("http://localhost:5000/api/v1/auth/login")
                .then()
                .statusCode(200)
                .body("data.token", notNullValue())
                .log().all()
                .extract()
                .jsonPath()
                .getString("data.token");

        System.out.println("Token: " + token);
    }

    @Test(priority = 3, dependsOnMethods = "loginUser")
    public void putMethod() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Venkatesh");
        payload.put("email", email);
        payload.put("password", "test123");
        payload.put("role", "admin");

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .put("http://localhost:5000/api/v1/users/" + id)
                .then()
                .statusCode(200)
                .body("data.id", equalTo(id))
                .body("data.name", equalTo("Venkatesh"))
                .body("data.email", equalTo(email))
                .log().all();
    }

    @Test(priority = 4, dependsOnMethods = "putMethod")
    public void getUserById() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("http://localhost:5000/api/v1/users/" + id)
                .then()
                .statusCode(200)
                .body("data.id", equalTo(id))
                .body("data.name", equalTo("Venkatesh"))
                .body("data.email", equalTo(email))
                .body("data.role", equalTo("admin"))
                .log().all();
    }

    @Test(priority = 5, dependsOnMethods = "loginUser")
    public void getAllUsers() {

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("http://localhost:5000/api/v1/users")
                .then()
                .statusCode(200)
                .body("data.size()", greaterThan(0))
                .body("data.id", hasItem(id))
                .log().all();
    }

    @Test(priority = 6, dependsOnMethods = "getAllUsers")
    public void deleteUser(){

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("http://localhost:5000/api/v1/users/" + id)
                .then()
                .statusCode(200)
                .body("message", equalTo("User deleted successfully"))
                .log().all();


    }
}