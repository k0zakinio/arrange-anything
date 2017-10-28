package com.company.k0zak

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test

class PasswordHasherTest {

    private val passwordHasher = PasswordHasher()

    @Test
    fun shouldBeAbleToCalculateTheHash() {
        val password = "testyMcTestFace"
        val hashedPassword = passwordHasher.hashString(password)
        assertThat(passwordHasher.check(password, hashedPassword), equalTo(true))
    }

    @Test
    fun shouldDetectWhenHashesDontMatch() {
        val password = "testyMcTestFace"
        val hashedPassword = passwordHasher.hashString(password)
        assertThat(passwordHasher.check("NOT_testyMcTestFace", hashedPassword), equalTo(false))
    }

    @Test(expected = IllegalStateException::class)
    fun shouldThrowWhenHashDoesntMatchFormat() {
        passwordHasher.check("foobar", "blabla")
    }
}