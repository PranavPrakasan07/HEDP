package com.pranavprksn.hedp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import java.math.BigInteger
import java.sql.*
import java.util.*
import com.pranavprksn.hedp.db.DBHandler
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pranavprksn.hedp.activities.LoginActivity
import com.pranavprksn.hedp.db.DBView
import com.pranavprksn.hedp.he.*
import com.pranavprksn.hedp.models.DataModel


class Home : AppCompatActivity() {

    private val TAG = "TAG"

    private val FILENAME = "encryption key location"
    var paillier: pailier? = null
    var aesen: aesfor? = null
    var fl: file? = null
    var r: BigInteger? = null

    var entered_text = "email"

    var encrypted_text: BigInteger? = null
    private var dbHandler: DBHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //--------------------------------------------------------------------

        // creating a new dbhandler class
        // and passing our context to it.

        dbHandler = DBHandler(this@Home)

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
            val temp: Array<BigInteger> = paillier!!.pq
            temp[2] = BigInteger("" + r)
            temp[3] = BigInteger("" + 100 + (Math.random() * (10000 - 100 + 1)).toInt())
            fl!!.fileWrite(temp, FILENAME)
        }
        aesen = aesfor()

        val text = findViewById<EditText>(R.id.text)
        val button = findViewById<TextView>(R.id.save_button)
        val chatButton = findViewById<ImageButton>(R.id.chat_button)
        val view_button = findViewById<TextView>(R.id.view_button)

        val error = findViewById<TextView>(R.id.error)

        val enc_text = findViewById<TextView>(R.id.e_text)
        val dec_text = findViewById<TextView>(R.id.d_text)

        chatButton.setOnClickListener {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
        }

        view_button.setOnClickListener {

            val bottomSheet = DBView()

            val dataObjects = dbHandler!!.readData()
            bottomSheet.fillData(dataObjects)

            bottomSheet.show(
                supportFragmentManager,
                "ModalBottomSheet"
            )
        }

        button.setOnClickListener {

            entered_text = text.text.toString()

            if (entered_text == "") {
                error.text = "*Please enter some text"
                entered_text = "Dummy"

            } else {
                error.text = ""

                val hed = HED()

                val ena = paillier!!.EncrypStr(entered_text, r)

//                val encFromClass= hed.encryptInput(entered_text)

//                Log.d("ENC-FROM-FUNC", encFromClass.toString());

                val dec = paillier!!.DecrpyStr(BigInteger(ena.toString()))

//                Log.d("DEC-FROM-FUNC", hed.decryptInput(encFromClass).toString());

//                var obj = HEAdapter()
//                val ena = obj.encryptHE(entered_text)
//                val dec = obj.decryptHE(BigInteger(ena.toString()))

                enc_text.text = ena.toString()
                dec_text.text = dec

                dbHandler!!.addNewData(dec_text.text as String?, enc_text.text as String)

//                Log.d("ena", ena.toString())
//                Log.d("decrypt ena", paillier!!.DecrpyStr(BigInteger(ena.toString())))
            }
        }
    }
}