package com.company.k0zak

import org.junit.Assert.*
import org.junit.Test

class PasswordHasherTest {

    @Test
    fun shouldBeAbleToCalculateTheHash() {
        val password = "testyMcTestFace"
        val hashedPassword = PasswordHasher.getSaltedHash(password)
        assertTrue(PasswordHasher.check("testyMcTestFace", hashedPassword))
    }
}