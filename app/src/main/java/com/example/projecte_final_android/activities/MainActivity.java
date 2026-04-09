package com.example.projecte_final_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.projecte_final_android.R;
public class MainActivity extends AppCompatActivity {
    private Button botonIniciar;
    private Button botonHistorial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        botonIniciar = findViewById(R.id.botonIniciar);
        botonHistorial = findViewById(R.id.botonHistorial);
        botonIniciar.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, MotionActivity.class)));
        botonHistorial.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, HistoryActivity.class)));
    }
}