package com.example.ecomarketmovil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecomarketmovil.viewmodels.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun OlvidarPassword(
    paddingValues: PaddingValues,
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var wasRequestSent by remember { mutableStateOf(false) }
    val passwordResetState by authViewModel.passwordResetState.collectAsState()

    // Efecto para volver al login después de un éxito
    LaunchedEffect(passwordResetState) {
        if (passwordResetState is AuthViewModel.PasswordResetState.Success) {
            wasRequestSent = true
            delay(3000) // Espera 3 segundos
            navController.popBackStack() // Vuelve a la pantalla anterior (Login)
            authViewModel.resetPasswordState() // Limpia el estado
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFa7dc8f),
                        Color.LightGray
                    )
                )
            )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Recuperar Contraseña", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Ingresa tu correo electrónico y te enviaremos un enlace para restablecer tu contraseña.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email de tu cuenta") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(24.dp))

        when (val state = passwordResetState) {
            is AuthViewModel.PasswordResetState.Loading -> {
                CircularProgressIndicator()
            }
            is AuthViewModel.PasswordResetState.Success -> {
                Text(
                    "¡Correo enviado! Revisa tu bandeja de entrada.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            is AuthViewModel.PasswordResetState.Error -> {
                Text(
                    state.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
            is AuthViewModel.PasswordResetState.Idle -> {
                // Estado inicial, no se muestra nada extra
            }
        }

        // El botón solo es visible si la solicitud no ha sido enviada con éxito
        if (!wasRequestSent) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { authViewModel.sendPasswordResetEmail(email) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF053900),
                    contentColor = Color.White
                ),
                enabled = passwordResetState !is AuthViewModel.PasswordResetState.Loading
            ) {
                Text("Enviar")
            }
        }
    }
}