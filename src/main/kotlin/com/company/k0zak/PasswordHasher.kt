package com.company.k0zak

import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class PasswordHasher: Hasher {
    // The higher the number of iterations the more
    // expensive computing the hash is for us and
    // also for an attacker.
    private val iterations = 20 * 1000
    private val saltLen = 32
    private val desiredKeyLen = 256

    /** Computes a salted PBKDF2 hash of given plaintext password
     * suitable for storing in a database.
     * Empty passwords are not supported.  */
    override fun hashString(text: String): String {
        val salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(saltLen)
        // store the salt with the password
        return BASE64Encoder().encode(salt) + "$" + hash(text, salt)
    }

    /** Checks whether given plaintext password corresponds
     * to a stored salted hash of the password.  */
    override fun check(current: String, stored: String): Boolean {
        val saltAndPass = stored.split("\\$".toRegex()).toTypedArray()
        if (saltAndPass.size != 2) {
            throw IllegalStateException(
                    "The stored password have the form 'salt\$hash'")
        }
        val hashOfInput = hash(current, BASE64Decoder().decodeBuffer(saltAndPass[0]))
        return hashOfInput == saltAndPass[1]
    }

    @Throws(Exception::class)
    private fun hash(password: String?, salt: ByteArray): String {
        if (password.isNullOrEmpty()){
            throw IllegalArgumentException("Empty passwords are not supported.")
        } else {
            val f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val key = f.generateSecret(PBEKeySpec(
                    password!!.toCharArray(), salt, iterations, desiredKeyLen)
            )
            return BASE64Encoder().encode(key.encoded)
        }
    }
}