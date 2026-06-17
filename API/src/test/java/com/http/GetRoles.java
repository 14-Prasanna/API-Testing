package com.http;

import com.hooks.hooks;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;


public class GetRoles extends hooks {


    @Test(priority = 5, dependsOnMethods = "ValidLogin", description = "get All roles ")
    public void getRoles(){

        System.out.println("Get all roles");

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer" + loginAPi.token)
                .when()
                .get("/roles/getAll")
                .then()
                .extract().response();

        System.out.println("Response body: " + response.body().prettyPrint());
    }
}
