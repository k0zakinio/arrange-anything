package com.company.k0zak.model

import org.http4k.template.ViewModel

data class Event(val owner: String, val title: String): ViewModel {
    override fun template(): String {
        return "event"
    }
}
