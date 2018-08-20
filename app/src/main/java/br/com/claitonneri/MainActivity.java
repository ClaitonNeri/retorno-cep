package br.com.claitonneri;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView txtResultado;
    private EditText edtCep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtCep = findViewById(R.id.edt_cep);
        txtResultado = findViewById(R.id.txt_resultado);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TarefaBackground background = new TarefaBackground();
                String cep = edtCep.getText().toString();

                String url = "https://viacep.com.br/ws/" + cep + "/json/";
                background.execute(url);
            }
        });
    }

    class TarefaBackground extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // Neste metodo sera retornado a String do WebService
        @Override
        protected String doInBackground(String... strings) {

            String urlRetorno = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            // Criar a conexao HTTP
            try {
                // Config da conexao
                URL url = new URL(urlRetorno);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Recuperar os dados solicitados (em bytes)
                inputStream = connection.getInputStream();

                // Converte os dados de Bytes para Caracteres
                inputStreamReader = new InputStreamReader(inputStream);

                // Converter o objeto inputStreamReader em String
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                buffer = new StringBuffer();
                String row = "";

                // Adicionando as linhas recuperadas na varivel buffer
                while((row = bufferedReader.readLine()) != null){
                    buffer.append(row);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        // Neste metodo sera exibido a string retornada no metodo doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Sera convertido em um objeto JSON para manipular os atributos
            String logradouro = null;
            String complemento = null;
            String bairro = null;
            String localidade = null;
            String uf = null;

            try {
                JSONObject jsonObject = new JSONObject(result);
                logradouro = jsonObject.getString("logradouro");
                complemento = jsonObject.getString("complemento");
                bairro = jsonObject.getString("bairro");
                localidade = jsonObject.getString("localidade");
                uf = jsonObject.getString("uf");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            txtResultado.setText("Logradouro: " + logradouro + " Complemento: " + complemento + " Bairro: " + bairro + " Localidade: " + localidade + " UF: " + uf);
        }
    }
}
