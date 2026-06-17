package com.http;

import com.hooks.hooks;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class getCourse extends hooks {

    @Test(priority = 4, dependsOnMethods = "ValidLogin")
    public void get_course() {

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + loginAPi.token)
                .when()
                .get("/courses-structure/getAll")
                .then()
                .statusCode(200)
                .log().all()
                .extract()
                .response();

        System.out.println(response.asPrettyString());
    }
}