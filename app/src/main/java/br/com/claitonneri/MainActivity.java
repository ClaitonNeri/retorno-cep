package br.com.claitonneri;

import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView txtResultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtResultado = findViewById(R.id.txt_resultado);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TarefaBackground background = new TarefaBackground();
                String url = "https://viacep.com.br/ws/85555000/json/";
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
            txtResultado.setText(result);
        }
    }
}
