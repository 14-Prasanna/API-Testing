package com.http;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class DifferrentPostMethod {


    String email;


    // Using HashMap

    @Test
    public void HashMapPost(){
        HashMap payload = new HashMap();

        email = "prasanna" + System.currentTimeMillis() + "@gmail.com";

        payload.put("name", "Prasanna");
        payload.put("email", email);
        payload.put("password", "test123");
        payload.put("role", "admin");
    }
}
