package com.benjiiross.coachandco.data

import com.benjiiross.coachandco.fakes.TestSettings
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TokenStorageTest {

    private lateinit var storage: TokenStorage

    @BeforeTest
    fun setUp() {
        storage = TokenStorage(TestSettings())
    }

    // ── access token ──────────────────────────────────────────────────────────

    @Test
    fun `getToken returns null when nothing saved`() {
        assertNull(storage.getToken())
    }

    @Test
    fun `saveToken and getToken round-trip correctly`() {
        storage.saveToken("access.token")
        assertEquals("access.token", storage.getToken())
    }

    @Test
    fun `clearToken removes access token`() {
        storage.saveToken("access.token")
        storage.clearToken()
        assertNull(storage.getToken())
    }

    // ── refresh token ─────────────────────────────────────────────────────────

    @Test
    fun `getRefreshToken returns null when nothing saved`() {
        assertNull(storage.getRefreshToken())
    }

    @Test
    fun `saveRefreshToken and getRefreshToken round-trip correctly`() {
        storage.saveRefreshToken("refresh.token")
        assertEquals("refresh.token", storage.getRefreshToken())
    }

    // ── clearAll ──────────────────────────────────────────────────────────────

    @Test
    fun `clearAll removes both tokens`() {
        storage.saveToken("access.token")
        storage.saveRefreshToken("refresh.token")

        storage.clearAll()

        assertNull(storage.getToken())
        assertNull(storage.getRefreshToken())
    }

    @Test
    fun `clearAll is safe when no tokens are stored`() {
        storage.clearAll() // must not throw
        assertNull(storage.getToken())
        assertNull(storage.getRefreshToken())
    }

    // ── independence ──────────────────────────────────────────────────────────

    @Test
    fun `access token and refresh token are stored independently`() {
        storage.saveToken("access.token")
        storage.saveRefreshToken("refresh.token")

        storage.clearToken()

        assertNull(storage.getToken())
        assertEquals("refresh.token", storage.getRefreshToken())
    }
}
