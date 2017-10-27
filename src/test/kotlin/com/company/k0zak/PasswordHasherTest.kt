package com.company.k0zak

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class PasswordHasherTest {

    @Test
    fun shouldBeAbleToCalculateTheHash() {
        val password = "testyMcTestFace"
        val hashedPassword = PasswordHasher.getSaltedHash(password)
        assertThat(PasswordHasher.check(password, hashedPassword), equalTo(true))
    }

    @Test
    fun shouldDetectWhenHashesDontMatch() {
        val password = "testyMcTestFace"
        val hashedPassword = PasswordHasher.getSaltedHash(password)
        assertThat(PasswordHasher.check("NOT_testyMcTestFace", hashedPassword), equalTo(false))
    }

    @Test(expected = IllegalStateException::class)
    fun shouldThrowWhenHashDoesntMatchFormat() {
        PasswordHasher.check("foobar", "blabla")
    }
}