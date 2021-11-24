package com.pranavprksn.hedp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.pranavprksn.hedp.activities.LoginActivity;
import com.pranavprksn.hedp.db.DBHandler;
import com.pranavprksn.hedp.models.DataModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHandler dbHandler = new DBHandler(MainActivity.this);

        ArrayList<DataModel> dataObject = dbHandler.readData();

        Toast.makeText(MainActivity.this, dataObject.toString(), Toast.LENGTH_SHORT).show();

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}