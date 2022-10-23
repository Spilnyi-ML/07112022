package lesson3;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;


public class ExampleTest extends AbstractTest {

    @Test
    void getRecipeInformationTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("includeNutrition", "false")
                .pathParam("id", 716429)
                .when()
                .get(getBaseUrl() + "recipes/{id}/information")
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"))
                .time(lessThan(3000L))
                .contentType(ContentType.JSON)
                .header("Connection", "keep-alive")
        ;
    }

    @Test
    void getSimilarRecipesTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("number", "10")
                .queryParam("limitLicense", "true")
                .pathParam("id", 715538)
                .when()
                .get(getBaseUrl() + "recipes/{id}/similar")
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"))
                .time(lessThan(3000L))
                .contentType(ContentType.JSON)
                .header("Connection", "keep-alive")
        ;
    }

    @Test
    void getComplexSearchTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("query", "burger")
                .queryParam("cuisine", "italian")
                .queryParam("excludeCuisine", "greek")
                .queryParam("diet", "vegetarian")
                .queryParam("intolerances", "gluten")
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"))
                .time(lessThan(3000L))
                .contentType(ContentType.JSON)
                .header("Connection", "keep-alive")
        ;
    }

    @Test
    void getRecipeInformationBulkTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("ids", "715538,716429")
                .queryParam("includeNutrition", "false")
                .when()
                .get(getBaseUrl() + "recipes/informationBulk")
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"))
                .time(lessThan(3000L))
                .contentType(ContentType.JSON)
                .header("Connection", "keep-alive")
        ;
    }

    @Test
    void getExtractRecipeFromWebsiteTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("url", "https://foodista.com/recipe/ZHK4KPB6/chocolate-crinkle-cookies")
                .queryParam("forceExtraction", "true")
                .queryParam("analyze", "false")
                .queryParam("includeNutrition", "false")
                .queryParam("includeTaste", "false")
                .when()
                .get(getBaseUrl() + "recipes/extract")
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"))
                .time(lessThan(30000L))
                .contentType(ContentType.JSON)
                .header("Connection", "keep-alive")
        ;
    }

    @Test
    void postClassifyCuisineTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"))
                .time(lessThan(3000L))
                .contentType(ContentType.JSON)
                .header("Connection", "keep-alive")
        ;
    }

    @Test
    void postRecipeTasteWidgetTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("language", "en")
                .queryParam("normalize", "false")
                .queryParam("rgb", "75,192,192")
                .when()
                .post(getBaseUrl() + "recipes/visualizeTaste")
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"))
                .time(lessThan(3000L))
                .contentType(ContentType.HTML)
                .header("Connection", "keep-alive")
        ;
    }

    @Test
    void postRecipeNutritionWidgetTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("language", "en")
                .when()
                .post(getBaseUrl() + "recipes/visualizeNutrition")
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"))
                .time(lessThan(3000L))
                .contentType(ContentType.HTML)
                .header("Connection", "keep-alive")
        ;
    }

    @Test
    void postPriceBreakdownWidgetTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .queryParam("language", "en")
                .when()
                .post(getBaseUrl() + "recipes/visualizePriceEstimator")
                .then()
                .statusCode(200)
                .statusLine(containsString("OK"))
                .time(lessThan(3000L))
                .contentType(ContentType.HTML)
                .header("Connection", "keep-alive")
        ;
    }

    @Test
    void postEquipmentWidgetTest() {
        given()
                .queryParam("apiKey", getApiKey())
                .when()
                .post(getBaseUrl() + "recipes/visualizeEquipment")
                .then()
                .statusCode(500)
                .statusLine(containsString("Error"))
                .time(lessThan(3000L))
                .contentType(ContentType.HTML)
                .header("Connection", "keep-alive")
        ;
    }

        @Test
        void addMealShoppingList() {
            //Создание списка покупок
            given()
                    .queryParam("hash", getHashCode())
                    .queryParam("apiKey", getApiKey())
                    .pathParam("username", getUserName())
                    .pathParam("start-date", "2022-10-24")
                    .pathParam("end-date", "2020-10-30")
                    .when()
                    .post(getBaseUrl() + "mealplanner/{username}/shopping-list/{start-date}/{end-date}")
                    .then()
                    .statusCode(200);

            //Добавить в список покупок
            String id = given()
                    .queryParam("hash", getHashCode())
                    .queryParam("apiKey", getApiKey())
                    .pathParam("username", getUserName())
                    .body("{\n"
                            + " \"item\": 1 package baking powder,\n"
                            + " \"aisle\": Baking,\n"
                            + " \"parse\": true,\n"
                            + "}")
                    .when()
                    .post(getBaseUrl() + "mealplanner/{username}/shopping-list/items")
                    .then()
                    .statusCode(200)
                    .extract()
                    .jsonPath()
                    .get("id")
                    .toString();

            //Получить список покупок
            given()
                    .queryParam("hash", getHashCode())
                    .queryParam("apiKey", getApiKey())
                    .pathParam("username", getUserName())
                    .when()
                    .get(getBaseUrl() + "mealplanner/{username}/shopping-list")
                    .then()
                    .statusCode(200);

            //Удалить из списка покупок
            given()
                    .queryParam("hash", getHashCode())
                    .queryParam("apiKey", getApiKey())
                    .pathParam("username", getUserName())
                    .when()
                    .delete(getBaseUrl() + "mealplanner/{username}/shopping-list/items/" + id)
                    .then()
                    .statusCode(200);
        }
    }