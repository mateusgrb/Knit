package com.omerozer.sample.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.omerozer.knit.Knit;
import com.omerozer.knit.KnitView;
import com.omerozer.knit.Updating;
import com.omerozer.sample.R;

@KnitView
public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Knit.show(this);

        ((Button) findViewById(R.id.btn_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent next = new Intent(
                        MainActivity.this, SecondActivity.class);
                MainActivity.this.startActivity(next);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Knit.show(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Knit.dismiss(this);
    }

    @Updating("firstName")
    public void updateTestString(String testInteger) {
        Log.d("KNIT_TEST" , testInteger);
        ((TextView) findViewById(R.id.textView_t)).setText(testInteger);
    }

    @Updating("fullName")
    public void updateFullname(String testInteger) {
        Log.d("KNIT_TEST" , "Receiving fullName:" + testInteger);
        ((TextView) findViewById(R.id.textView_t)).setText(testInteger);
    }
}
