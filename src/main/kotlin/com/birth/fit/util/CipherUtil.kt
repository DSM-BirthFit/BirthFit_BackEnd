package com.birth.fit.util

import org.springframework.stereotype.Component
import java.io.UnsupportedEncodingException
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

@Component
class CipherUtil {

    @Throws(NoSuchAlgorithmException::class)
    fun genRSAKeyPair(): KeyPair {
        val secureRandom = SecureRandom()
        val gen: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
        gen.initialize(1024, secureRandom)
        return gen.genKeyPair()
    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class
    )
    fun encryptRSA(plainText: String, publicKey: PublicKey?): String {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val bytePlain = cipher.doFinal(plainText.toByteArray())
        return Base64.getEncoder().encodeToString(bytePlain)
    }

    @Throws(
        NoSuchPaddingException::class,
        NoSuchAlgorithmException::class,
        InvalidKeyException::class,
        BadPaddingException::class,
        IllegalBlockSizeException::class,
        UnsupportedEncodingException::class
    )
    fun decryptRSA(encrypted: String, privateKey: PrivateKey?): String {
        val cipher = Cipher.getInstance("RSA")
        val byteEncrypted = Base64.getDecoder().decode(encrypted.toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val bytePlain = cipher.doFinal(byteEncrypted)
        return String(bytePlain)
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun getPrivateKeyFromBase64String(keyString: String): PrivateKey {
        val privateKeyString = keyString.replace("\\n".toRegex(), "").replace("-{5}[ a-zA-Z]*-{5}".toRegex(), "")
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
        return keyFactory.generatePrivate(keySpecPKCS8)
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    fun getPublicKeyFromBase64String(keyString: String): PublicKey {
        val publicKeyString = keyString.replace("\\n".toRegex(), "").replace("-{5}[ a-zA-Z]*-{5}".toRegex(), "")
        val keyFactory = KeyFactory.getInstance("RSA")
        val keySpecX509 = X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString))
        return keyFactory.generatePublic(keySpecX509)
    }
}
