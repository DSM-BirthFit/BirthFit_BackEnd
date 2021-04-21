package com.birth.fit.common.util

import org.apache.commons.codec.binary.Base64
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.Key
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Component
class AES256Util(
    @Value("\${user.secret.key}")
    private val key: String,
) {
    private var iv: String? = null
    private var keySpec: Key? = null

    init {
        iv = key.substring(0, 16)
        val keyBytes = ByteArray(16)
        val b = key.toByteArray(charset("UTF-8"))
        var length = b.size
        if (length > keyBytes.size) {
            length = keyBytes.size
        }
        System.arraycopy(b, 0, keyBytes, 0, length)
        val keySpec = SecretKeySpec(keyBytes, "AES")
        this.keySpec = keySpec
    }

    @Throws(
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun aesEncode(str: String): String {
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv!!.toByteArray()))
        val encrypted = c.doFinal(str.toByteArray(charset("UTF-8")))
        return String(Base64.encodeBase64(encrypted))
    }

    @Throws(
        UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun aesDecode(str: String): String {
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv!!.toByteArray()))
        val byteStr = Base64.decodeBase64(str.toByteArray())
        return String(c.doFinal(byteStr))
    }
}