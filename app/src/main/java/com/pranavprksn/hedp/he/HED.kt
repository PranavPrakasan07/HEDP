package com.pranavprksn.hedp.he

import java.math.BigInteger
import java.util.*

class HED {

    private val FILENAME = "encryption key location"
    var paillier: pailier? = null
    var aesen: aesfor? = null
    var fl: file? = null
    var r: BigInteger? = null

    fun encryptInput(input: String): BigInteger? {
        fl = file()
        paillier = pailier()
        if (fl!!.check(FILENAME)) {
            val rValue = fl!!.getKey(FILENAME)
            paillier!!.KeyGeneration(512, 62, rValue[0], rValue[1])
            r = rValue[2]
        } else {
            r = BigInteger(512, Random())
            paillier!!.KeyGeneration(512, 62)
            val temp: Array<BigInteger> = paillier!!.pq
            temp[2] = BigInteger("" + r)
            temp[3] = BigInteger("" + 100 + (Math.random() * (10000 - 100 + 1)).toInt())
            fl!!.fileWrite(temp, FILENAME)
        }
        aesen = aesfor()

        val ena = paillier!!.EncrypStr(input, r)

        return ena

    }

    fun decryptInput(input: BigInteger?): String? {
        fl = file()
        paillier = pailier()
        if (fl!!.check(FILENAME)) {
            val rValue = fl!!.getKey(FILENAME)
            paillier!!.KeyGeneration(512, 62, rValue[0], rValue[1])
            r = rValue[2]
        } else {
            r = BigInteger(512, Random())
            paillier!!.KeyGeneration(512, 62)
            val temp: Array<BigInteger> = paillier!!.pq
            temp[2] = BigInteger("" + r)
            temp[3] = BigInteger("" + 100 + (Math.random() * (10000 - 100 + 1)).toInt())
            fl!!.fileWrite(temp, FILENAME)
        }
        aesen = aesfor()

        val dec = paillier!!.DecrpyStr(BigInteger(input.toString()))

        return dec
    }


}