package com.example.ecomarketmovil.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ecomarketmovil.data.AuthManager

@Composable
fun RequireAnyRole(
    navController: NavController,
    requiredRoles: Set<String>,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }

    if (authManager.hasAnyRole(*requiredRoles.toTypedArray())) {
        content()
    } else {
        // Si el usuario no tiene ninguno de los roles requeridos, muestra un mensaje de acceso denegado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Acceso Denegado", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text("No tienes permiso para acceder a esta pantalla.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Volver")
            }
        }
    }
}

// Sobrecarga para un solo rol para mantener la simplicidad
@Composable
fun RequireRole(
    navController: NavController,
    requiredRole: String,
    content: @Composable () -> Unit
) {
    RequireAnyRole(navController = navController, requiredRoles = setOf(requiredRole), content = content)
}
