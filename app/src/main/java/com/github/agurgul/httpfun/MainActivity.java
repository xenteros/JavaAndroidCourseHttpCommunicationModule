package com.github.agurgul.httpfun;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        textView.setText(String.valueOf(1));
    }

    public void onClick(View view) {
        new MyAsync().execute(textView.getText().toString());
    }

    private class MyAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
        }

        @Override
        protected String doInBackground(String... params) {
            return String.valueOf(Integer.parseInt(params[0]) + 1);
        }
    }

}
