package com.pranavprksn.hedp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.pranavprksn.hedp.he.aesfor
import com.pranavprksn.hedp.he.file
import com.pranavprksn.hedp.he.pailier
import java.math.BigInteger
import java.sql.*
import java.util.*

class Home : AppCompatActivity() {

    private val TAG = "TAG"

    private val FILENAME = "encryption key location"
    var paillier: pailier? = null
    var aesen: aesfor? = null
    var fl: file? = null
    var r: BigInteger? = null

    var entered_text = "email"

    var encrypted_text: BigInteger? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //--------------------------------------------------------------------
        fl = file()
        paillier = pailier()
        if (fl!!.check(FILENAME)) {
            val rValue = fl!!.getKey(FILENAME)
            paillier!!.KeyGeneration(512, 62, rValue[0], rValue[1])
            r = rValue[2]
        } else {
            r = BigInteger(512, Random())
            paillier!!.KeyGeneration(512, 62)
            val temp: Array<BigInteger>
            temp = paillier!!.pq
            temp[2] = BigInteger("" + r)
            temp[3] = BigInteger("" + 100 + (Math.random() * (10000 - 100 + 1)).toInt())
            fl!!.fileWrite(temp, FILENAME)
        }
        aesen = aesfor()

        val text = findViewById<EditText>(R.id.text)
        val button = findViewById<TextView>(R.id.save_button)

        button.setOnClickListener {
            entered_text = text.getText().toString()

            Log.d("Entered text", entered_text)

            encrypted_text = paillier!!.EncrypStr(entered_text, r)
            Toast.makeText(applicationContext, "Entered text$entered_text", Toast.LENGTH_SHORT)
                .show()

            Log.d("Encrypted Text", encrypted_text.toString())
            Toast.makeText(applicationContext, "Encrred text$encrypted_text", Toast.LENGTH_SHORT)
                .show()


            //Operations

            Log.d(
                "Decrypted text",
                paillier!!.DecrpyStr(BigInteger(Objects.requireNonNull(encrypted_text).toString()))
            )

            Toast.makeText(
                applicationContext,
                "Decrypted text" + paillier!!.DecrpyStr(
                    BigInteger(
                        Objects.requireNonNull(encrypted_text).toString()
                    )
                ),
                Toast.LENGTH_SHORT
            ).show()


            val ena = paillier!!.EncrypStr(entered_text, r)

            Log.d("ena", ena.toString())
            Toast.makeText(applicationContext,"ena$ena",
                Toast.LENGTH_SHORT
            ).show()

            Log.d("decrypt ena", paillier!!.DecrpyStr(BigInteger(ena.toString())))
            Toast.makeText(applicationContext,"decrypt ena" + paillier!!.DecrpyStr(BigInteger(ena.toString())),
                Toast.LENGTH_SHORT
            ).show()


        }

    }
}