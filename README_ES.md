## 📄 `README.es.md` (Español)

```markdown
# MotionLab

## Analizador Inteligente de Movimiento

<div align="center">
  
  <a href="README.md">
    <img src="https://img.shields.io/badge/📖-ENGLISH-blue?style=for-the-badge&logo=googletranslate" alt="English">
  </a>
  
  <br>
  <sub>*Haz clic en el botón de arriba para leer en inglés*</sub>

</div>

---

[![Android](https://img.shields.io/badge/Android-11%2B-green)](https://developer.android.com)
[![Java](https://img.shields.io/badge/Java-11-red)](https://www.java.com/)
[![License](https://img.shields.io/badge/Licencia-MIT-blue)](LICENSE)

---

## Descripción

**MotionLab** es una aplicación Android desarrollada en **Java 11** que analiza el movimiento físico del usuario utilizando el acelerómetro integrado del dispositivo. La app captura datos en tiempo real del sensor `TYPE_ACCELEROMETER` y aplica técnicas de procesamiento de señales para interpretar el comportamiento físico.

El sistema puede **detectar pasos**, **estimar la distancia recorrida** y **clasificar el estado del movimiento** (Quieto, Caminando o Corriendo) analizando la magnitud del vector de aceleración.

El objetivo principal no es solo usar el sensor, sino **comprender su comportamiento físico** y aplicar un procesamiento matemático básico a los datos brutos.

## Características principales

- ✅ Captura de datos del acelerómetro en tiempo real
- ✅ Filtro de paso bajo para eliminar la componente de gravedad
- ✅ Detección de pasos mediante algoritmo de detección de picos
- ✅ Clasificación del movimiento (Quieto / Caminando / Corriendo)
- ✅ Estimación de distancia basada en el conteo de pasos y longitud de zancada configurable
- ✅ Visualización de gráficos en tiempo real usando **Custom View + Canvas**
- ✅ Persistencia de sesiones con **SQLite / Room**
- ✅ `StatsFragment` compartido entre múltiples Activities
- ✅ Gestión adecuada del ciclo de vida del sensor (`onResume` / `onPause`)

## Pantallas y flujo de trabajo

### 1. Pantalla Principal (`MainActivity`)

| Pantalla | Vista previa |
|----------|--------------|
| Pantalla principal (inicial) | <img src="assets/main_screen_initial.jpg" alt="Pantalla principal inicial" width="200"> |
| Pantalla principal (con estadísticas diarias) | <img src="assets/main_screen_with_stats.jpg" alt="Pantalla principal con estadísticas" width="200"> |

### 2. Pantalla de Sesión (`MotionActivity`)

| Pantalla | Vista previa |
|----------|--------------|
| Pantalla de sesión (vacía / no iniciada) | <img src="assets/session_empty.jpg" alt="Sesión vacía" width="200"> |
| Pantalla de sesión (monitoreo activo) | <img src="assets/session_active.jpg" alt="Sesión activa" width="200"> |

### 3. Pantalla de Resultados (`ResultActivity`)

| Pantalla | Vista previa |
|----------|--------------|
| Pantalla de resultados | <img src="assets/results_screen.jpg" alt="Resultados" width="200"> |

### 4. Pantalla de Historial (`HistoryActivity`)

| Pantalla | Vista previa |
|----------|--------------|
| Pantalla de historial | <img src="assets/history_screen.jpg" alt="Historial" width="200"> |

## Persistencia de datos

Cuando finaliza una sesión, todos los datos se guardan automáticamente en una base de datos **SQLite** local (mediante **Room**). La pantalla de historial carga estos registros para mostrar las sesiones pasadas. La pantalla principal también puede mostrar estadísticas diarias.

## Tecnologías utilizadas

| Categoría | Tecnologías |
|-----------|-------------|
| Plataforma | Android Studio, Java 11, Android SDK |
| Sensores | SensorManager, SensorEventListener, TYPE_ACCELEROMETER, SENSOR_DELAY_GAME |
| Procesamiento | Filtro de paso bajo, cálculo de magnitud vectorial, detección de picos |
| Gráficos | Vista personalizada, Canvas, Paint, `invalidate()` |
| Arquitectura | Múltiples Activities, Fragment compartido (StatsFragment) |
| Persistencia | SQLite / Room |

## Requisitos

- Android Studio Ladybug o superior
- Android SDK (API mínima: 24 / Android 7.0)
- Un dispositivo Android físico con acelerómetro (el emulador puede no proporcionar datos de sensor realistas)

## Instalación

```bash
git clone https://github.com/tuusuario/motionlab.git
cd motionlab
# Abre el proyecto en Android Studio
# Compila y ejecuta en un dispositivo físico
