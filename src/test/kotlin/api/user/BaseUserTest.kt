package api.user

import org.testng.annotations.AfterClass
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import java.util.UUID

open class BaseUserTest {
    protected val BASE_URL = "http://3.73.86.8:3333/user"
    protected lateinit var uniqueUuidUserName: String
    protected lateinit var uniqueUuidEmail: String

    @BeforeClass
    fun setupTestEnv() {
        // генерация пользователей для тестов, желательно без привязки к post user/create
    }

    @BeforeMethod
    fun generateTestData() {
        uniqueUuidUserName = "test_user_" + UUID.randomUUID().toString()
        uniqueUuidEmail = UUID.randomUUID().toString() + "@gmail.com"
    }

    @AfterClass
    fun clearTestEnv() {
        // удаление тестовых пользователей
    }
}