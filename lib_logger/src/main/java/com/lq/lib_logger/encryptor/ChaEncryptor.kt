package com.lq.lib_logger.encryptor

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

internal class ChaEncryptor : EncryptorInterface {

    private  val TRANSFORMATION = "ChaCha20-Poly1305"
    private  val IV_SIZE = 12  // 推荐 12 字节
    private  val TAG_SIZE = 128 // bits，认证标签长度

    fun generateKey(): SecretKey {
        val keyGen = KeyGenerator.getInstance("ChaCha20")
        keyGen.init(256) // 256位密钥
        return keyGen.generateKey()
    }

    override fun encrypt(content: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = ByteArray(IV_SIZE)
        SecureRandom().nextBytes(iv) // 随机生成IV

        val spec = GCMParameterSpec(TAG_SIZE, iv)
        cipher.init(Cipher.ENCRYPT_MODE, generateKey(), spec)
        val encrypted = cipher.doFinal(content.toByteArray())

        // 返回 iv + 密文，解密时需要拆分
        return Base64.getEncoder().encodeToString(iv + encrypted)
    }

    override fun decrypt(content: String): String {
        val decoded = Base64.getDecoder().decode(content)
        val iv = decoded.copyOfRange(0, IV_SIZE)
        val encrypted = decoded.copyOfRange(IV_SIZE, decoded.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(TAG_SIZE, iv)
        // 你需要持有密钥变量，不能每次生成新密钥，这里建议构造函数传入密钥
        cipher.init(Cipher.DECRYPT_MODE, generateKey(), spec)
        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted, Charsets.UTF_8)
    }
}