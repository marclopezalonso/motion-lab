# MotionLab

## Intelligent Movement Analyzer | Analizador Inteligente de Movimiento

<div align="center">
  
  [![EN](https://img.shields.io/badge/📖-ENGLISH-blue?style=for-the-badge&logo=googletranslate)](#english)
  [![ES](https://img.shields.io/badge/📖-ESPAÑOL-red?style=for-the-badge&logo=googletranslate)](#español)
  
  <sub>*Click on the badges above to jump to your preferred language*</sub>
  <br>
  <sub>*Haz clic en los botones de arriba para ir al idioma deseado*</sub>

</div>

---

[![Android](https://img.shields.io/badge/Android-11%2B-green)](https://developer.android.com)
[![Java](https://img.shields.io/badge/Java-11-red)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

---

## Description

**MotionLab** is an Android application developed in **Java 11** that analyzes the user's physical movement using the device's built-in accelerometer sensor. The app captures real-time data from the `TYPE_ACCELEROMETER` sensor and applies signal processing techniques to interpret physical behavior.

The system can **detect steps**, **estimate distance traveled**, and **classify movement state** (Quiet, Walking, or Running) by analyzing the magnitude of the acceleration vector.

The main goal is not just to use the sensor, but to **understand its physical behavior** and apply basic mathematical processing to raw data.

## Key Features

- ✅ Real-time accelerometer data capture
- ✅ Low-pass filter to remove gravity component
- ✅ Step detection via peak detection algorithm
- ✅ Movement classification (Quiet / Walking / Running)
- ✅ Distance estimation based on step count and configurable step length
- ✅ Real-time graph visualization using **Custom View + Canvas**
- ✅ Session persistence with **SQLite / Room**
- ✅ Shared `StatsFragment` used across multiple Activities
- ✅ Proper sensor lifecycle management (`onResume` / `onPause`)

## Screens & Workflow

### 1. Main Screen (`MainActivity`)

The home screen presents two main options:
- **Start analysis session** – Begins a new motion monitoring session
- **View history** – Displays all past sessions

The design is minimal and clean, prioritizing functionality and visual clarity.

| Screen | Preview |
|--------|---------|
| Main Screen (initial) | `![Main Screen Initial](assets/main_screen_initial.jpg)` |
| Main Screen (with daily stats) | `[IMAGE: assets/main_screen_stats.jpg]` |

### 2. Session Screen (`MotionActivity`)

This is the core functional screen. It contains:
- **Shared `StatsFragment`** showing:
  - Elapsed time
  - Step count
  - Estimated distance
  - Current state (Quiet / Walking / Running)
- **Custom GraphView** – Real-time acceleration magnitude plot using Canvas drawing
- **Control buttons**: Start, Pause, End session

When "Start" is pressed, the accelerometer begins capturing data. The graph updates in real time, steps are detected, and the movement state changes dynamically.

| Screen | Preview |
|--------|---------|
| Session Screen (empty / not started) | `[Main Screen Initial: assets/session_empty.jpg]` |
| Session Screen (active monitoring) | `[IMAGE: assets/session_active.jpg]` |

### 3. Results Screen (`ResultActivity`)

After ending a session, this screen shows a summary:
- Final step count
- Total distance
- Final movement state
- Session duration

This screen reuses the same `StatsFragment` to demonstrate **Fragment reusability** across multiple Activities.

| Screen | Preview |
|--------|---------|
| Results Screen | `[IMAGE: assets/results_screen.jpg]` |

### 4. History Screen (`HistoryActivity`)

Displays a list of all saved sessions with:
- Session number
- Date and time
- Step count
- Distance
- Movement state

| Screen | Preview |
|--------|---------|
| History Screen | `[IMAGE: assets/history_screen.jpg]` |

## Data Persistence

When a session ends, all data is automatically saved to a local **SQLite** database (via **Room**). The history screen loads these records to display past sessions. The main screen can also show daily statistics.

## Technologies Used

| Category | Technologies |
|----------|--------------|
| Platform | Android Studio, Java 11, Android SDK |
| Sensors | SensorManager, SensorEventListener, TYPE_ACCELEROMETER, SENSOR_DELAY_GAME |
| Processing | Low-pass filter, vector magnitude calculation, peak detection |
| Graphics | Custom View, Canvas, Paint, `invalidate()` |
| Architecture | Multiple Activities, Shared Fragment (StatsFragment) |
| Persistence | SQLite / Room |

## Requirements

- Android Studio Ladybug or newer
- Android SDK (minimum API level: 24 / Android 7.0)
- A physical Android device with an accelerometer (emulator may not provide realistic sensor data)

## Installation

```bash
git clone https://github.com/yourusername/motionlab.git
cd motionlab
# Open the project in Android Studio
# Build and run on a physical device
