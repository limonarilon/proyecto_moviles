package com.example.ecomarketmovil.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecomarketmovil.ui.Routes
import com.example.ecomarketmovil.viewmodels.AuthViewModel
import com.example.ecomarketmovil.viewmodels.LoginState

@Composable
fun Login(paddingValues: PaddingValues, navController: NavController, authViewModel: AuthViewModel = viewModel()) {

    var email by remember { mutableStateOf("admin@test.com") } // Valor por defecto para pruebas
    var password by remember { mutableStateOf("hola123") } // Valor por defecto para pruebas

    val loginState by authViewModel.loginState.collectAsState()

    // Observador para manejar la navegación o mostrar errores
    LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginState.Success -> {
                // Navegamos al menú principal. Pasamos el email y el rol para uso futuro.
                // El password ya no es necesario aquí.
                navController.navigate(Routes.MainMenu + "/$email/${state.role}") {
                    popUpTo(Routes.Login) { inclusive = true } // Limpiar el backstack
                }
                authViewModel.resetLoginState() // Limpiar el estado para futuras navegaciones
            }
            is LoginState.Error -> {
                // El mensaje de error se mostrará en la UI
            }
            else -> Unit // Idle o Loading
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("Iniciar Sesión", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Lógica condicional para el botón y el feedback
        when (loginState) {
            is LoginState.Loading -> {
                CircularProgressIndicator()
            }
            else -> {
                if (loginState is LoginState.Error) {
                    Text(
                        text = (loginState as LoginState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = { authViewModel.login(email, password) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF053900),
                        contentColor = Color.White
                    )
                ) {
                    Text("Ingresar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿No tienes cuenta? Regístrate aquí",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { navController.navigate(Routes.Register) }
        )
    }
}
