package com.pranavprksn.hedp.localcompress;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.pranavprksn.hedp.R;
import com.pranavprksn.hedp.activities.LoginActivity;
import com.pranavprksn.hedp.db.DBHandler;
import com.pranavprksn.hedp.he.HED;
import com.pranavprksn.hedp.localcompress.Huffman;
import com.pranavprksn.hedp.models.DataModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String encrypted = "17876279380594145073732461027673296699199149073663428301327841551147520513504327298851848423302175291524434727422569763332949087648249202398242780886981203514825481403324042361991767438266260806967168826603999292007197340849967667853424828852849255962354275436142438496628034573817110960778939377542300660010";

        //Reading to file
        Log.d("TAG", "Reading from file");

        String fileData = encrypted;
        String compress = String.valueOf(Huffman.Compress(fileData, "compress.txt", "com//pranavprksn//hedp//localcompress//Huffman.java"));
        String decompress = String.valueOf(Huffman.Decompress("compress.huffman", "com//pranavprksn//hedp//localcompress//Huffman.java"));
        Log.d("TAG", fileData);
        Log.d("TAG", "Compressed:" + compress);
        Log.d("TAG", "Decompressed:" + decompress);


    }
}