package com.example.food_order.utils.extension

import android.util.Patterns

fun CharSequence.isValidEmail(): Boolean {
    val validDomains = setOf(
        "gmail.com", "outlook.com", "yahoo.com", "icloud.com",
        "hotmail.com", "aol.com", "protonmail.com", "zoho.com"
    )
    val validTlds = setOf(
        "com", "net", "org", "edu", "gov", "mil", "int",
        "vn", "us", "uk", "jp", "kr", "de", "fr", "au",
        "co", "io", "me", "info", "biz", "name", "pro"
    )

    val email = this.toString()
    // Kiểm tra định dạng Email
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return false

    // Tách phần tên miền sau @
    val domainPart = email.substringAfterLast("@", "").lowercase()
    val tld = domainPart.substringAfterLast(".", "")

    // Kiểm tra tên miền phổ biến hoặc TLD hợp lệ
    return validDomains.contains(domainPart) ||
            (domainPart.matches(Regex("^[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) &&
                    validTlds.contains(tld))
}