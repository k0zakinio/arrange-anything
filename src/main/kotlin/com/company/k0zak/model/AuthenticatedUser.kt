package com.company.k0zak.model

import org.http4k.template.ViewModel

data class AuthenticatedUser(val username: String): ViewModel {
    override fun template(): String {
        return "user"
    }
}