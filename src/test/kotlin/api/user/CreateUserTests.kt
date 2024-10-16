package api.user

import io.qameta.allure.Description
import io.restassured.RestAssured
import org.mindrot.jbcrypt.BCrypt
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.containsString
import org.testng.Assert

class CreateUserTests : BaseUserTest() {

    @Test
    @Description("Регистрация пользователя с корректными данными")
    fun successfullyCreatingUserTest() {
        RestAssured.baseURI = BASE_URL

        val response = given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("username", uniqueUuidUserName)
            .formParam("email", uniqueUuidEmail)
            .formParam("password", "test_password")
            .post("/create")
            .then()
            .statusCode(200)
            .body("success", equalTo(true))
            .body("message[0]", equalTo("User Successully created"))
            .body("details.username", equalTo(uniqueUuidUserName))
            .body("details.email", equalTo(uniqueUuidEmail))
            .extract()
            .response()

        // Извлечение и проверка пароля
        val hashedPassword = response.path<String>("details.password")
        Assert.assertTrue(BCrypt.checkpw("test_password", hashedPassword))
    }

    @DataProvider(name = "missingFieldsUserData")
    fun createMissingFieldsUserData(): Array<Array<String>> {
        return arrayOf(
            arrayOf("", "valid_email1@test.com", "test_password", "A username is required"),
            arrayOf("validUsername1", "", "test_password", "An Email is required"),
            arrayOf("validUsername1", "valid_email1@test.com", "", "A password for the user")
        )
    }

    @Test(dataProvider = "missingFieldsUserData")
    @Description("Регистрация пользователя с незаполненным полем")
    fun tryCreateUserWithMissingFieldTest(userName: String, userEmail: String, password: String, errorMessage: String) {
        RestAssured.baseURI = BASE_URL

        given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("username", userName)
            .formParam("email", userEmail)
            .formParam("password", password)
            .post("/create")
            .then()
            .statusCode(400)
            .body("success", equalTo(false))
            .body("message[0]", containsString(errorMessage))
    }

    @DataProvider(name = "existenceFieldsUserData")
    fun createExistenceFieldsUserData(): Array<Array<String>> {
        return arrayOf(
            arrayOf("validUsername", "new_valid_email@test.com", "This username is taken. Try another."),
            arrayOf("new_validUsername", "valid_email@test.com", "Email already exists")
        )
    }

    @Test(dataProvider = "existenceFieldsUserData")
    @Description("Регистрация пользователя с уже зарегистрированными userName/email")
    fun tryCreateUserWithExistingUserNameOrEmail(userName: String, userEmail: String, errorMessage: String) {
        RestAssured.baseURI = BASE_URL

        given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("username", userName)
            .formParam("email", userEmail)
            .formParam("password", "test_password")
            .post("/create")
            .then()
            .statusCode(400)
            .body("success", equalTo(false))
            .body("message[0]", containsString(errorMessage))
    }

    @Test
    @Description("Регистрация пользователя с невалидным email")
    fun tryCreateUserWithInvalidEmail() {
        RestAssured.baseURI = BASE_URL

        given()
            .contentType("application/x-www-form-urlencoded")
            .formParam("username", uniqueUuidUserName)
            .formParam("email", "invalidEmail")
            .formParam("password", "test_password")
            .post("/create")
            .then()
            .statusCode(400)
            .body("success", equalTo(false))
            .body("message[0]", containsString("Required valid email"))
    }
}
