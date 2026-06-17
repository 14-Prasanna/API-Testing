package com.http;

import com.hooks.hooks;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.notNullValue;

public class getInsitiution extends hooks {

    @Test(priority = 3, dependsOnMethods = {"get_base", "ValidLogin"})
    public void getAllInsititute(){

       Response response = RestAssured
                .when()
                .get("/getAll/institution")
                .then()
                .statusCode(200)
               .body(notNullValue())
                .extract().response();

       System.out.println(response.body().prettyPrint());
    }


}
