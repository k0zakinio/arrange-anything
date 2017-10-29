package com.company.k0zak

class FakeHasher(private val stubbedValue: String) : Hasher {
    override fun check(current: String, stored: String) = stored == current

    override fun hashString(text: String) = stubbedValue
}