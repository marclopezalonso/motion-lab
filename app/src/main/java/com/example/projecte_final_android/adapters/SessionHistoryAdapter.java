package com.example.projecte_final_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projecte_final_android.R;
import com.example.projecte_final_android.models.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class SessionHistoryAdapter extends RecyclerView.Adapter<SessionHistoryAdapter.VistaSesion> {
    public interface AlSeleccionarSesion {
        void alSeleccionarSesion(Session sesion);
    }
    private final AlSeleccionarSesion listener;
    private final List<Session> sesiones = new ArrayList<>();
    public SessionHistoryAdapter(AlSeleccionarSesion listener) {
        this.listener = listener;
    }
    public void establecerLista(List<Session> nuevasSesiones) {
        sesiones.clear();
        if (nuevasSesiones != null) {
            sesiones.addAll(nuevasSesiones);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public VistaSesion onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_session_history, parent, false);
        return new VistaSesion(vista);
    }
    @Override
    public void onBindViewHolder(@NonNull VistaSesion holder, int position) {
        Session sesion = sesiones.get(position);
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                .format(new Date(sesion.getCreadaEn()));
        holder.textoTitulo.setText("Sesión #" + sesion.getId());
        holder.textoFecha.setText(fecha);
        holder.textoResumen.setText(
                "Pasos: " + sesion.getPasos()
                        + " · Distancia: " + String.format(Locale.getDefault(), "%.2f m", sesion.getDistanciaMetros())
                        + " · Estado: " + sesion.getEstado()
        );
        holder.itemView.setOnClickListener(v -> listener.alSeleccionarSesion(sesion));
    }
    @Override
    public int getItemCount() {
        return sesiones.size();
    }
    static class VistaSesion extends RecyclerView.ViewHolder {
        TextView textoTitulo;
        TextView textoFecha;
        TextView textoResumen;
        public VistaSesion(@NonNull View itemView) {
            super(itemView);
            textoTitulo = itemView.findViewById(R.id.textoTitulo);
            textoFecha = itemView.findViewById(R.id.textoFecha);
            textoResumen = itemView.findViewById(R.id.textoResumen);
        }
    }
}