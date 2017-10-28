package com.company.k0zak

class FakeHasher : Hasher {
    override fun check(stored: String, current: String) = stored == current

    override fun hashString(str: String) = "stubbedHashedString"
}