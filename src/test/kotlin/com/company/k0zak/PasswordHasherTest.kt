package com.company.k0zak

import org.junit.Assert.*
import org.junit.Test

class PasswordHasherTest {

    @Test
    fun shouldBeAbleToCalculateTheHash() {
        val password = "testyMcTestFace"
        val hashedPassword = PasswordHasher.getSaltedHash(password)
        assertTrue(PasswordHasher.check(password, hashedPassword))
    }

    @Test
    fun shouldDetectWhenHashesDontMatch() {
        val password = "testyMcTestFace"
        val hashedPassword = PasswordHasher.getSaltedHash(password)
        assertFalse(PasswordHasher.check("NOT_testyMcTestFace", hashedPassword))
    }

    @Test(expected = IllegalStateException::class)
    fun shouldThrowWhenHashDoesntMatchFormat() {
        PasswordHasher.check("foobar", "blabla")
    }
}