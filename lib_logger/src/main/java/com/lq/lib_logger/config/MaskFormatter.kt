package com.lq.lib_logger.config

internal object MaskFormatter {

    fun mask(msg: String): String {
        var masked = msg

        // 手机号：中间脱敏
        masked = masked.replace(Regex("""1\d{2}\d{4}\d{4}""")) {
            val s = it.value
            s.replaceRange(3, 7, "****")
        }

        // 身份证：中间脱敏
        masked = masked.replace(Regex("""\d{6}\d{8}\d{4}""")) {
            val s = it.value
            s.replaceRange(6, 14, "********")
        }

        // Token：只保留前后几位
        masked = masked.replace(Regex("""(?i)token=([A-Za-z0-9\-_.]+)""")) {
            val token = it.groupValues[1]
            val safe = if (token.length > 10) {
                token.take(3) + "..." + token.takeLast(3)
            } else "***"
            "token=$safe"
        }

        // 密码字段
        masked = masked.replace(Regex("""(?i)password\s*=\s*[^&\s]+"""), "password=******")

        return masked
    }
}