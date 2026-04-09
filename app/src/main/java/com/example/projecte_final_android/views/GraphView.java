package com.example.projecte_final_android.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
public class GraphView extends View {
    private static final int MAXIMO_PUNTOS = 120;
    private final Paint pinturaCuadricula = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pinturaEje = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pinturaLinea = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint pinturaTexto = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final ArrayList<Float> datos = new ArrayList<>();
    public GraphView(Context context) {
        super(context);
        inicializar();
    }
    public GraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inicializar();
    }
    public GraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inicializar();
    }
    private void inicializar() {
        pinturaCuadricula.setColor(Color.parseColor("#DADADA"));
        pinturaCuadricula.setStrokeWidth(2f);
        pinturaEje.setColor(Color.parseColor("#9E9E9E"));
        pinturaEje.setStrokeWidth(3f);
        pinturaLinea.setColor(Color.parseColor("#1565C0"));
        pinturaLinea.setStrokeWidth(5f);
        pinturaLinea.setStyle(Paint.Style.STROKE);
        pinturaLinea.setStrokeJoin(Paint.Join.ROUND);
        pinturaLinea.setStrokeCap(Paint.Cap.ROUND);
        pinturaTexto.setColor(Color.parseColor("#666666"));
        pinturaTexto.setTextSize(28f);
    }
    public synchronized void agregarPunto(float valor) {
        datos.add(valor);
        if (datos.size() > MAXIMO_PUNTOS) {
            datos.remove(0);
        }
        postInvalidateOnAnimation();
    }
    public synchronized void reiniciar() {
        datos.clear();
        postInvalidateOnAnimation();
    }
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        dibujarCuadricula(canvas);
        List<Float> copia;
        synchronized (this) {
            copia = new ArrayList<>(datos);
        }
        if (copia.size() < 2) {
            canvas.drawText("Sin datos todavía", 24f, 48f, pinturaTexto);
            return;
        }
        float ancho = getWidth();
        float alto = getHeight();
        float centroY = alto / 2f;
        float maximoAbsoluto = 1f;
        for (Float valor : copia) {
            maximoAbsoluto = Math.max(maximoAbsoluto, Math.abs(valor));
        }
        float pasoX = ancho / (float) (MAXIMO_PUNTOS - 1);
        Path ruta = new Path();
        for (int i = 0; i < copia.size(); i++) {
            float valor = copia.get(i);
            float x = i * pasoX;
            float normalizado = valor / maximoAbsoluto;
            float y = centroY - normalizado * (alto * 0.38f);
            if (i == 0) {
                ruta.moveTo(x, y);
            } else {
                ruta.lineTo(x, y);
            }
        }
        canvas.drawLine(0f, centroY, ancho, centroY, pinturaEje);
        canvas.drawPath(ruta, pinturaLinea);
    }
    private void dibujarCuadricula(Canvas canvas) {
        float ancho = getWidth();
        float alto = getHeight();
        for (int i = 1; i < 5; i++) {
            float y = alto * i / 5f;
            canvas.drawLine(0f, y, ancho, y, pinturaCuadricula);
        }
        for (int i = 1; i < 5; i++) {
            float x = ancho * i / 5f;
            canvas.drawLine(x, 0f, x, alto, pinturaCuadricula);
        }
    }
}