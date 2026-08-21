package com.lq.lib_logger.encryptor

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

internal object KeyStoreUtil {

    private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    private const val AES_KEY_ALIAS = "LogEncryptAesKey"
    private var cachedKey: SecretKey? = null

    /**
     * 获取或创建 AES 密钥（256位，GCM模式）
     */
    fun getOrCreateAESKey(): SecretKey {
        // 缓存优先，避免每次都访问 KeyStore
        cachedKey?.let { return it }

        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

        // 若已存在，加载密钥
        if (keyStore.containsAlias(AES_KEY_ALIAS)) {
            val key = keyStore.getKey(AES_KEY_ALIAS, null) as SecretKey
            cachedKey = key
            return key
        }

        // 否则，生成并存入 KeyStore
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )

        val parameterSpec = KeyGenParameterSpec.Builder(
            AES_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(parameterSpec)
        val newKey = keyGenerator.generateKey()
        cachedKey = newKey
        return newKey
    }
}