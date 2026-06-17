//package com.http;
//
//
//import io.restassured.RestAssured;
//import io.restassured.response.Response;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import static org.hamcrest.Matchers.equalTo;
//
//
//public class ProductPage {
//
//
//    @BeforeClass
//    public void setup(){
//        RestAssured.baseURI = "http://localhost:5000";
//    }
//
//    @Test
//    public void getProducts(){
//
//        Response response = RestAssured
//                .given()
//                .when()
//                .get("/api/v1/products/")
//                .then()
//                .
//    }
//}
