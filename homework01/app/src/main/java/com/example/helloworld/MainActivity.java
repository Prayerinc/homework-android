package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn1=findViewById(R.id.btn1);

        final TextView tv1=findViewById(R.id.tv1);

        btn1.setOnClickListener(new View.OnClickListener(){
                @Override
                        public void onClick(View v){
            tv1.setText("傻得可爱");
            }
        });
    }

    public static final String EXTRA_MESSAGE ="com.example.helloworld.MESSAGE";

            protected void OnCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
    }


    //Button btn_send=findViewById(R.id.btn3);
    public void sendMessage(View view){
        Intent intent=new Intent(this,DisplayMessageActivity.class);
        EditText editText=(EditText)findViewById(R.id.editText);
        String message=editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE,message);
        startActivity(intent);

    }
}
