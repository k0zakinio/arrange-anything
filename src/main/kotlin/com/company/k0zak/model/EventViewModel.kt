package com.company.k0zak.model

import org.http4k.template.ViewModel

data class EventViewModel(val owner: String, val title: String, val date: String) : ViewModel {
    override fun template(): String {
        return "event"
    }
}