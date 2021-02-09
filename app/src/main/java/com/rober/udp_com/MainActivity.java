package com.rober.udp_com;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.DatagramSocket;
import java.net.SocketException;

public class MainActivity extends AppCompatActivity {
    Comunicacion cliente = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            cliente = new Comunicacion(MainActivity.this);
            cliente.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button btnCliente = findViewById(R.id.btnCliente);
        Button btnLimpiar = findViewById(R.id.btnLimpiar);
        Button btnLight = findViewById(R.id.btnLight);
        Button btnLuz = findViewById(R.id.btnLuz);

        btnCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cliente.Discover();
            }
        });

        btnLimpiar.setOnClickListener(v -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView textView = findViewById(R.id.textViewChat);
                    textView.setText("");
                }
            });
        });

        btnLight.setOnClickListener(v -> {
            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    cliente.SendTramaLights();
                }
            });
        });

        btnLuz.setOnClickListener(v -> {
            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void run() {
                    if(GlobalInfo.LISTALUCES.size()>0){
                        cliente.UpDownoneLight();
                    }else{
                        cliente.SendPeticionRoomItems();
                        cliente.UpDownoneLight();
                    }

                }
            });
        });


    }
}
