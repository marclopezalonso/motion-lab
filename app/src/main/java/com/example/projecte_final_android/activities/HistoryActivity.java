package com.example.projecte_final_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projecte_final_android.R;
import com.example.projecte_final_android.adapters.SessionHistoryAdapter;
import com.example.projecte_final_android.db.DBHelper;
import com.example.projecte_final_android.models.Session;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements SessionHistoryAdapter.AlSeleccionarSesion {
    private DBHelper baseDatos;
    private SessionHistoryAdapter adaptador;
    private TextView textoVacio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        RecyclerView listaSesiones = findViewById(R.id.listaSesiones);
        textoVacio = findViewById(R.id.textoVacio);
        Button botonVolver = findViewById(R.id.botonVolver);

        baseDatos = new DBHelper(this);
        listaSesiones.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new SessionHistoryAdapter(this);
        listaSesiones.setAdapter(adaptador);
        cargarSesiones();
        botonVolver.setOnClickListener(v -> finish());
    }
    @Override
    protected void onResume() {
        super.onResume();
        cargarSesiones();
    }
    private void cargarSesiones() {
        List<Session> sesiones = baseDatos.obtenerTodasLasSesiones();
        adaptador.establecerLista(sesiones);
        if (sesiones == null || sesiones.isEmpty()) {
            textoVacio.setVisibility(View.VISIBLE);
        } else {
            textoVacio.setVisibility(View.GONE);
        }
    }
    @Override
    public void alSeleccionarSesion(Session sesion) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_ID_SESION, sesion.getId());
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (baseDatos != null) {
            baseDatos.close();
        }
    }
}