package com.pranavprksn.hedp.he

import java.math.BigInteger
import java.util.*

class HEAdapter {

    private val FILENAME = "encryption key location"
    private var paillier: pailier? = null
    private var aesen: aesfor? = null
    var fl: file? = null
    var r: BigInteger? = null

    constructor(paillier: pailier?, aesen: aesfor?, fl: file?, r: BigInteger?) {
        this.paillier = paillier
        this.aesen = aesen
        this.fl = fl
        this.r = r
    }

    constructor() {

    }

    private fun initialise() : Array<Any?> {

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

        return arrayOf(paillier, r)
    }

    fun encryptHE(input: String): BigInteger? {
        val arr = initialise()

        val paillier: pailier? = arr[0] as pailier?
        val r = arr[1]

        return paillier!!.EncrypStr(input, arr[1] as BigInteger?)
    }

    fun decryptHE(input: BigInteger): String? {
        val arr = initialise()

        val paillier: pailier? = arr[0] as pailier?
        val r = arr[1]

        return paillier!!.DecrpyStr(input)
    }
}