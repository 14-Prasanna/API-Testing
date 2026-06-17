package com.crud;

import com.hooks.hooks;
import com.http.loginAPi;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

public class create_api extends hooks {

    public static String noteId;

    @Test(
            priority = 6,
            dependsOnMethods = "com.http.loginAPi.ValidLogin",
            description = "Create a new note using valid bearer token"
    )
    public void Create() {

        Map<String, Object> map = new HashMap<>();
        map.put("title", "Creating_Muhi");
        map.put("content", "Muhindhar.... ");
        map.put("tags", new String[]{"qa", "demo"});
        map.put("isPinned", true);
        map.put("color", "#ffffbf");
        map.put("lastEdited", "2026-06-17T04:10:19.541Z");

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + loginAPi.token)
                .body(map)
                .when()
                .post("/create/notes")
                .then()
                .statusCode(201)
                .body("success", equalTo(true))
                .body("data._id", notNullValue())
                .log().all()
                .extract()
                .response();

        noteId = response.jsonPath().getString("data._id");

        System.out.println("Created Note ID: " + noteId);
        System.out.println(response.asPrettyString());
    }

    @Test(
            priority = 7,
            dependsOnMethods = "Create",
            description = "Get created note by note ID"
    )
    public void GetNoteById() {

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + loginAPi.token)
                .pathParam("id", noteId)
                .when()
                .get("/getById/notes/{id}")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data._id", equalTo(noteId))
                .body("data.title", notNullValue())
                .body("data.content", notNullValue())
                .log().all()
                .extract()
                .response();

        System.out.println(response.asPrettyString());
    }

    @Test(
            priority = 8,
            dependsOnMethods = "GetNoteById",
            description = "Update note details by note ID"
    )
    public void UpdateNoteById() {

        Map<String, Object> updatePayload = new HashMap<>();
        updatePayload.put("title", "API Test Note Edited");
        updatePayload.put("content", "Updated content");

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + loginAPi.token)
                .pathParam("id", noteId)
                .body(updatePayload)
                .when()
                .put("/update/notes/{id}")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("message", equalTo("Note updated successfully"))
                .body("data._id", equalTo(noteId))
                .body("data.title", equalTo("API Test Note Edited"))
                .body("data.content", equalTo("Updated content"))
                .log().all()
                .extract()
                .response();

        System.out.println(response.asPrettyString());
    }

    @Test(
            priority = 9,
            dependsOnMethods = "Create",
            description = "Get all notes with pagination query parameters"
    )
    public void GetAllNotes() {

        int page = 1;
        int limit = 50;

        Response response = RestAssured
                .given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + loginAPi.token)
                .queryParam("page", page)
                .queryParam("limit", limit)
                .when()
                .get("/getAll/notes")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("data", notNullValue())
                .body("data.size()", lessThanOrEqualTo(limit))
                .body("pagination", notNullValue())
                .body("pagination.currentPage", equalTo(page))
                .body("pagination.totalPages", greaterThanOrEqualTo(1))
                .body("pagination.totalNotes", greaterThanOrEqualTo(0))
                .body("pagination.hasNext", notNullValue())
                .body("pagination.hasPrev", notNullValue())
                .log().all()
                .extract()
                .response();

        System.out.println(response.asPrettyString());

        int currentPage = response.jsonPath().getInt("pagination.currentPage");
        int totalPages = response.jsonPath().getInt("pagination.totalPages");
        boolean hasNext = response.jsonPath().getBoolean("pagination.hasNext");
        boolean hasPrev = response.jsonPath().getBoolean("pagination.hasPrev");

        Assert.assertEquals(currentPage, page);

        if (currentPage < totalPages) {
            Assert.assertTrue(hasNext);
        } else {
            Assert.assertFalse(hasNext);
        }

        if (currentPage > 1) {
            Assert.assertTrue(hasPrev);
        } else {
            Assert.assertFalse(hasPrev);
        }
    }

    @Test(
            priority = 10,
            dependsOnMethods = "UpdateNoteById",
            description = "Toggle pin status of note by note ID"
    )
    public void TogglePinNoteById() {

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + loginAPi.token)
                .pathParam("id", noteId)
                .when()
                .put("/toggle-pin/notes/{id}")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("message", anyOf(
                        equalTo("Note pinned successfully"),
                        equalTo("Note unpinned successfully")
                ))
                .body("data._id", equalTo(noteId))
                .body("data.isPinned", notNullValue())
                .log().all()
                .extract()
                .response();

        System.out.println(response.asPrettyString());
    }

    @Test(
            priority = 11,
            dependsOnMethods = "TogglePinNoteById",
            description = "Delete note by note ID"
    )
    public void DeleteNoteById() {

        Response response = RestAssured
                .given()
                .header("Authorization", "Bearer " + loginAPi.token)
                .pathParam("id", noteId)
                .when()
                .delete("/delete/notes/ById/{id}")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .log().all()
                .extract()
                .response();

        System.out.println(response.asPrettyString());
    }
}