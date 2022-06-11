package src.main.kotlin.Server.MainLog

import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


fun md5Custom(st: String): String? {
    var messageDigest: MessageDigest? = null
    var digest: ByteArray? = ByteArray(0)
    try {
        messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.reset()
        messageDigest.update(st.toByteArray())
        digest = messageDigest.digest()
    } catch (e: NoSuchAlgorithmException) {
        // тут можно обработать ошибку
        // возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
        e.printStackTrace()
    }
    val bigInt = BigInteger(1, digest)
    var md5Hex = bigInt.toString(16)
    while (md5Hex.length < 16) {
        md5Hex = "0$md5Hex"
    }
    md5Hex= md5Hex.substring(0, 16)
    return md5Hex
}