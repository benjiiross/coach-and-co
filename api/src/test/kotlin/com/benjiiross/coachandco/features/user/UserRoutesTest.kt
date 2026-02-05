package com.benjiiross.coachandco.features.user

import com.benjiiross.coachandco.plugins.configureSerialization
import com.benjiiross.coachandco.plugins.configureStatusPages
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import org.junit.Test
import kotlin.test.assertEquals

class UserRoutesTest {

  @Test
  fun `GET users-id with invalid ID should return 400 BadRequest`() = testApplication {
    application {
      configureSerialization()
      configureStatusPages()

      this.routing { userRoutes() }
    }

    val response = client.get("/users/abc")

    assertEquals(HttpStatusCode.BadRequest, response.status)
  }

  @Test
  fun `GET users with unknown email should return 404`() = testApplication {
    val response = client.get("/users?email=nonexistent@test.com")

    assertEquals(HttpStatusCode.NotFound, response.status)
  }
}
