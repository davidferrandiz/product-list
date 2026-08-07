package com.davidferrandiz.mangostore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import com.davidferrandiz.mangostore.core.ui.theme.MangoTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Única Activity de la app (patrón single-activity).
 *
 * En Compose la Activity deja de ser "una pantalla" y pasa a ser solo
 * el CONTENEDOR donde vive el árbol de Composables; las "pantallas"
 * serán destinos de Navigation (Tarea 6).
 * Paralelo iOS: es el WindowGroup del @main App — el marco, no el contenido.
 *
 * @AndroidEntryPoint le dice a Hilt: "en esta Activity (y sus
 * composables) se pueden inyectar dependencias" — sin ella,
 * hiltViewModel() petaría en runtime.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dibuja detrás de las barras del sistema (recomendación actual)
        enableEdgeToEdge()

        // setContent {} sustituye al setContentView(R.layout...) clásico:
        // aquí arranca el árbol Compose. Como el body de un App de SwiftUI.
        setContent {
            // Nuestro tema (definido en :core:ui) envuelve TODO el árbol
            MangoTheme {
                // ------- PLACEHOLDER Tarea 1 -------
                // En la Tarea 6 esto será el NavHost con las 3 pantallas.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding), // respeta status/navigation bar
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🥭 Mango Store — proyecto base OK")
                    }
                }
            }
        }
    }
}
