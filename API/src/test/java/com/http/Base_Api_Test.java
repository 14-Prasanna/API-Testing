package com.http;

import com.hooks.hooks;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class Base_Api_Test extends hooks {

    @Test(priority = 1)
    public void get_base(){
        Response response =RestAssured
                .when()
                .get("https://lms-server-3-wedg.onrender.com/")
                .then()
                .statusCode(200)
                .extract().response();

        System.out.println("The status code: " + response.statusCode());
    }
}
