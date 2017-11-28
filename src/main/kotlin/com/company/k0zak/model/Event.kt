package com.company.k0zak.model

import org.http4k.template.ViewModel
import java.time.LocalDateTime

data class Event(val owner: String, val title: String, val date: LocalDateTime): ViewModel {
    override fun template(): String {
        return "event"
    }
}
