
package com.example.manco.myapplication.sharedpreferences;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.manco.myapplication.MainActivity;
import com.example.manco.myapplication.R;


/**
 * Created by Marjan Sherdnekovski
 */

public class SharedPrefs extends Activity implements View.OnClickListener {

    EditText sharedData;
    TextView dataResults;
    public static String filename = "MySharedString";
    SharedPreferences someData;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dialog);
        setupVariables();
        someData = getSharedPreferences(filename, 0);
//
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout((int) (width * .8), (int) (height * .35));

        final Button ok = (Button) findViewById(R.id.btnOk);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(SharedPrefs.this, MainActivity.class);
                SharedPrefs.this.startActivity(registerIntent);

            }
        });


    }

    private void setupVariables(){
        Button save = (Button)findViewById(R.id.btnSave);
        sharedData = (EditText)findViewById(R.id.etName);
        dataResults = (TextView) findViewById(R.id.tvLoadSharedPrefs);
        save.setOnClickListener(this);
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnSave:
                String stringData = sharedData.getText().toString();
                SharedPreferences.Editor editor = someData.edit();
                editor.putString("sharedString", stringData);
                editor.commit();
                someData = getSharedPreferences(filename, 0);
                String dataReturned = someData.getString("sharedString", "Couldn't Load Data");
                dataResults.setText(dataReturned);
                break;


        }
    }
}

