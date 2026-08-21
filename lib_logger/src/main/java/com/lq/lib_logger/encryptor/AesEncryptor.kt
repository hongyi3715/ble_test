package com.lq.lib_logger.encryptor

import android.os.Build
import androidx.annotation.RequiresApi
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

internal class AesEncryptor : EncryptorInterface{

    private val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private val IV_SIZE = 16

    @RequiresApi(Build.VERSION_CODES.O)
    override fun encrypt(content: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)

        val iv = ByteArray(IV_SIZE).apply {
            SecureRandom().nextBytes(this)
        }

        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.ENCRYPT_MODE, KeyStoreUtil.getOrCreateAESKey(), ivSpec)

        val encrypted = cipher.doFinal(content.toByteArray(Charsets.UTF_8))

        // 拼接 IV + 密文，然后 Base64 编码
        val result = iv + encrypted
        return Base64.getEncoder().encodeToString(result)
    }

   @RequiresApi(Build.VERSION_CODES.O)
   override fun decrypt(content: String): String {
        val allData = Base64.getDecoder().decode(content)

        val iv = allData.copyOfRange(0, IV_SIZE)
        val encrypted = allData.copyOfRange(IV_SIZE, allData.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val ivSpec = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, KeyStoreUtil.getOrCreateAESKey(), ivSpec)

        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted, Charsets.UTF_8)
    }
}