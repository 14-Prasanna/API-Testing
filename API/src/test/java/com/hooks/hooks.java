package com.hooks;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class hooks {

    @BeforeClass
    public void setup_base() {
        RestAssured.baseURI = "https://lms-server-3-wedg.onrender.com";
        System.out.println("Base URL set");
    }

    @AfterClass
    public void close() {
        System.out.println("Closing the Testing");
        System.out.println();
    }
}