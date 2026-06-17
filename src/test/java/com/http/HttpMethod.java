package com.http;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
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

        Response response = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("http://localhost:5000/api/v1/auth/register");

        long responseTime = response.getTime();

        response.then()
                .statusCode(201)
                .body("data.id", notNullValue())
                .body("data.email", equalTo(email))
                .time(lessThan(2000L))
                .log().all();

        id = response.jsonPath().getInt("data.id");

        System.out.println("Created User ID: " + id);
        System.out.println("Register Response Time: " + responseTime + " ms");

        Assert.assertTrue(responseTime < 2000, "Register response time exceeded 2 seconds");
    }

    @Test(priority = 2, dependsOnMethods = "registerUser")
    public void loginUser() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("password", "test123");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("http://localhost:5000/api/v1/auth/login");

        long responseTime = response.getTime();

        response.then()
                .statusCode(200)
                .body("data.token", notNullValue())
                .body("data.tokenType", equalTo("Bearer"))
                .time(lessThan(2000L))
                .log().all();

        token = response.jsonPath().getString("data.token");

        System.out.println("Token: " + token);
        System.out.println("Login Response Time: " + responseTime + " ms");

        Assert.assertTrue(responseTime < 2000, "Login response time exceeded 2 seconds");
    }

    @Test(priority = 3, dependsOnMethods = "registerUser")
    public void loginUserInvalid() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("password", "wrong123");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("http://localhost:5000/api/v1/auth/login");

        long responseTime = response.getTime();

        response.then()
                .statusCode(401)
                .body("statusText", equalTo("Error"))
                .body("message", equalTo("Invalid password"))
                .time(lessThan(2000L))
                .log().all();

        System.out.println("Invalid Login Response Time: " + responseTime + " ms");

        Assert.assertTrue(responseTime < 2000, "Invalid login response time exceeded 2 seconds");
    }

    @Test(priority = 4, dependsOnMethods = "loginUser")
    public void putMethod() {

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Venkatesh");
        payload.put("email", email);
        payload.put("password", "test123");
        payload.put("role", "admin");

        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .when()
                .put("http://localhost:5000/api/v1/users/" + id);

        long responseTime = response.getTime();

        response.then()
                .statusCode(200)
                .body("data.id", equalTo(id))
                .body("data.name", equalTo("Venkatesh"))
                .body("data.email", equalTo(email))
                .body("data.role", equalTo("admin"))
                .time(lessThan(2000L))
                .log().all();

        System.out.println("PUT Response Time: " + responseTime + " ms");

        Assert.assertTrue(responseTime < 2000, "PUT response time exceeded 2 seconds");
    }

    @Test(priority = 5, dependsOnMethods = "putMethod")
    public void getUserById() {

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("http://localhost:5000/api/v1/users/" + id);

        long responseTime = response.getTime();

        response.then()
                .statusCode(200)
                .body("data.id", equalTo(id))
                .body("data.name", equalTo("Venkatesh"))
                .body("data.email", equalTo(email))
                .body("data.role", equalTo("admin"))
                .time(lessThan(2000L))
                .log().all();

        System.out.println("GET By ID Response Time: " + responseTime + " ms");

        Assert.assertTrue(responseTime < 2000, "GET by ID response time exceeded 2 seconds");
    }

    @Test(priority = 6, dependsOnMethods = "loginUser")
    public void getAllUsers() {

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("page", 1)
                .queryParam("limit", 10)
                .when()
                .get("http://localhost:5000/api/v1/users");

        long responseTime = response.getTime();

        response.then()
                .statusCode(200)
                .body("data.page", equalTo(1))
                .body("data.limit", equalTo(10))
                .body("data.totalUsers", greaterThan(0))
                .body("data.users.size()", greaterThan(0))
                .body("data.users.id", hasItem(id))
                .time(lessThan(2000L))
                .log().all();

        System.out.println("GET All Users Response Time: " + responseTime + " ms");

        Assert.assertTrue(responseTime < 2000, "GET all users response time exceeded 2 seconds");
    }

    @Test(priority = 7, dependsOnMethods = "getAllUsers")
    public void getAllUsersWithSearch() {

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .queryParam("page", 1)
                .queryParam("limit", 10)
                .queryParam("search", "Venkatesh")
                .queryParam("role", "admin")
                .when()
                .get("http://localhost:5000/api/v1/users");

        long responseTime = response.getTime();

        response.then()
                .statusCode(200)
                .body("data.users.id", hasItem(id))
                .body("data.users.name", hasItem("Venkatesh"))
                .time(lessThan(2000L))
                .log().all();

        System.out.println("GET Search Users Response Time: " + responseTime + " ms");

        Assert.assertTrue(responseTime < 2000, "Search users response time exceeded 2 seconds");
    }

    @Test(priority = 8, dependsOnMethods = "getAllUsersWithSearch")
    public void deleteUser() {

        Response response = given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("http://localhost:5000/api/v1/users/" + id);

        long responseTime = response.getTime();

        response.then()
                .statusCode(200)
                .body("message", equalTo("User deleted successfully"))
                .time(lessThan(2000L))
                .log().all();

        System.out.println("DELETE Response Time: " + responseTime + " ms");

        Assert.assertTrue(responseTime < 2000, "DELETE response time exceeded 2 seconds");
    }
}