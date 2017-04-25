package com.github.agurgul.httpfun;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.IOException;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.scrollText)
    TextView scrollView;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.goButton)
    Button button;

    JSONObject jsonObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    public void onClick(View view){
        new MyAsync().execute();
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    private class MyAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPostExecute(String s) {
            scrollView.setText(s);
            try {
                jsonObject = new JSONObject(s);
                String baseEur = jsonObject.getString("base");
                JSONObject rates = jsonObject.getJSONObject("rates");

                Iterator<String> it = rates.keys();

                StringBuilder sb = new StringBuilder(baseEur.compareToIgnoreCase("base"));

                while (it.hasNext()) {
                    String key = it.next();
                    sb.append(baseEur + " to " + key + " = ");
                    sb.append(rates.getString(key));
                    sb.append("\n");
                }

                scrollView.setText(sb.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            URL url = null;
            try {
                url = new URL("http://api.fixer.io/latest?symbols=USD,GBP");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String result;
            try{
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                result = readStream(in);
            } catch (IOException e) {
                e.printStackTrace();
                result = "error";
            } finally {
                urlConnection.disconnect();
            }
            return result;
        }
    }
}