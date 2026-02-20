package com.benjiiross.coachandco.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class OutcomeTest {

    // ── isSuccess / isFailure ─────────────────────────────────────────────────

    @Test
    fun `Success isSuccess is true and isFailure is false`() {
        val outcome = Outcome.Success(42)
        assertTrue(outcome.isSuccess)
        assertFalse(outcome.isFailure)
    }

    @Test
    fun `Failure isFailure is true and isSuccess is false`() {
        val outcome = Outcome.Failure("error")
        assertTrue(outcome.isFailure)
        assertFalse(outcome.isSuccess)
    }

    // ── getOrNull ─────────────────────────────────────────────────────────────

    @Test
    fun `getOrNull returns value for Success`() {
        assertEquals("hello", Outcome.Success("hello").getOrNull())
    }

    @Test
    fun `getOrNull returns null for Failure`() {
        assertNull(Outcome.Failure<Nothing, String>("err").getOrNull())
    }

    // ── errorOrNull ───────────────────────────────────────────────────────────

    @Test
    fun `errorOrNull returns error for Failure`() {
        assertEquals("err", Outcome.Failure("err").errorOrNull())
    }

    @Test
    fun `errorOrNull returns null for Success`() {
        assertNull(Outcome.Success<Int, Nothing>(1).errorOrNull())
    }

    // ── map ───────────────────────────────────────────────────────────────────

    @Test
    fun `map transforms Success value`() {
        val result = Outcome.Success(2).map { it * 3 }
        assertEquals(Outcome.Success(6), result)
    }

    @Test
    fun `map passes Failure through unchanged`() {
        val outcome: Outcome<Int, String> = Outcome.Failure("err")
        val result = outcome.map { it * 3 }
        assertEquals(Outcome.Failure("err"), result)
    }

    // ── onSuccess ─────────────────────────────────────────────────────────────

    @Test
    fun `onSuccess executes action for Success`() {
        var called = false
        Outcome.Success(1).onSuccess { called = true }
        assertTrue(called)
    }

    @Test
    fun `onSuccess does not execute action for Failure`() {
        var called = false
        Outcome.Failure<Int, String>("err").onSuccess { called = true }
        assertFalse(called)
    }

    @Test
    fun `onSuccess returns same outcome for chaining`() {
        val outcome = Outcome.Success(1)
        val result = outcome.onSuccess {}
        assertEquals(outcome, result)
    }

    // ── onFailure ─────────────────────────────────────────────────────────────

    @Test
    fun `onFailure executes action for Failure`() {
        var called = false
        Outcome.Failure("err").onFailure { called = true }
        assertTrue(called)
    }

    @Test
    fun `onFailure does not execute action for Success`() {
        var called = false
        Outcome.Success(1).onFailure { called = true }
        assertFalse(called)
    }

    @Test
    fun `onFailure returns same outcome for chaining`() {
        val outcome = Outcome.Failure("err")
        val result = outcome.onFailure {}
        assertEquals(outcome, result)
    }
}
