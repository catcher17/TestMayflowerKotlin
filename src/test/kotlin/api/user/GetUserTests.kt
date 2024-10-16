package api.user

import io.qameta.allure.Description
import io.restassured.RestAssured.given
import org.testng.annotations.Test
import org.hamcrest.Matchers.equalTo

class GetUserTests : BaseUserTest() {

    @Test
    @Description("Проверка получения списка пользователей при наличии зарегистрированных пользователей")
    fun getAllUsersTest() {
        given()
            .baseUri(BASE_URL)
            .`when`()
            .get("/get")
            .then()
            .statusCode(200) // Проверка статуса ответа
            .body("[0].id", equalTo(1)) // Проверка полей первого пользователя
            .body("[0].username", equalTo("test_user"))
            .body("[0].email", equalTo("test_user@test.test"))
            .body("[0].password", equalTo("\$2a\$10\$eOSMiWoR.qcG3khiRpwWRO2DJLoI5KqUQ7fSWAWdmJIYjTyl7tM1e"))
            .body("[0].created_at", equalTo("2024-10-16 07:17:20"))
    }

    @Test
    @Description("Проверка получения списка пользователей при отсутствии пользователей")
    fun getAllUsersWithEmptyDBTest() {
        given()
            .baseUri(BASE_URL)
            .`when`()
            .get("/get")
            .then()
            .statusCode(200)
            .body(equalTo("[]"))
    }
}