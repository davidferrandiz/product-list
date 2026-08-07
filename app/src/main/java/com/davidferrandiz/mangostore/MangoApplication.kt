package com.davidferrandiz.mangostore

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Punto de entrada del proceso de la app y RAÍZ del grafo de Hilt.
 *
 * @HiltAndroidApp dispara la generación de código de Hilt: crea el
 * contenedor de dependencias a nivel de aplicación, del que colgarán
 * los ViewModels, repositorios, Retrofit, Room… (el "maître" de la
 * analogía del restaurante: quien sabe qué cocinero sirve cada plato).
 *
 * Paralelo iOS: no hay equivalente directo — sería como si en el
 * @main App struct se inicializara automáticamente un contenedor
 * global de dependencias que luego cada View puede pedir.
 *
 * Está vacía a propósito: si un día necesita inicializar librerías
 * (analytics, logging…), este es el sitio — y nada más.
 */
@HiltAndroidApp
class MangoApplication : Application()
