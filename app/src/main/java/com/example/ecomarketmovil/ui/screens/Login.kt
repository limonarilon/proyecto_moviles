package com.example.ecomarketmovil.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ecomarketmovil.R
import com.example.ecomarketmovil.ui.Routes
import com.example.ecomarketmovil.viewmodels.AuthViewModel
import com.example.ecomarketmovil.viewmodels.LoginState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.ecomarketmovil.remote.RetrofitClientWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun Login(paddingValues: PaddingValues, navController: NavController, authViewModel: AuthViewModel = viewModel()) {

    var email by remember { mutableStateOf("admin@test.com") } // Valor por defecto para pruebas
    var password by remember { mutableStateOf("hola123") } // Valor por defecto para pruebas

    val loginState by authViewModel.loginState.collectAsState()
    var showSuccessAnimation by remember { mutableStateOf(false) }
    var temperature by remember { mutableStateOf<Float?>(null) }
    var weatherLoading by remember { mutableStateOf(true) }
    var weatherError by remember { mutableStateOf<String?>(null) }
    var apiKey= "o1fziqhj6atwpfnflwamx03abfycy7nwv4wwnkvf"

    // Observador para manejar la navegación o mostrar errores
   LaunchedEffect(Unit) {
       if (apiKey == "YOUR_API_KEY") {
           println("ADVERTENCIA: La API Key de Meteosource no ha sido configurada.")
           return@LaunchedEffect
       }
       try {
           val response = withContext(Dispatchers.IO) {
               RetrofitClientWeather.apiWeather.getWeather(
                   lat = "-33.44",      // Latitud de Santiago
                   lon = "-70.66",      // Longitud de Santiago
                   apiKey = apiKey
               )
           }
               if (response.isSuccessful) {
                   temperature = response.body()?.current?.temperature
               } else {
                   weatherError = "Error al obtener clima: ${response.message()}"
               }
           } catch (e: Exception) {
               weatherError = "Error de conexión: ${e.localizedMessage}"
           } finally {
               weatherLoading = false
           }
       }



       LaunchedEffect(loginState) {
        when (val state = loginState) {
            is LoginState.Success -> {
                showSuccessAnimation = true // Activa la animación
                delay(1500)
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

        Spacer(modifier = Modifier.height(16.dp))
        if (weatherLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
        } else if (weatherError != null) {
            Text(
                text = weatherError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        } else {
            temperature?.let {
                Text(
                    text = "Temperatura actual: ${it}°C",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }


        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo de la marca",
            modifier = Modifier
                .fillMaxWidth(0.5f) // Ocupa el 50% del ancho
                .padding(bottom = 32.dp)
        )

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
                CircularProgressIndicator() // Animación de carga durante el proceso
            }
            else -> {
                if (loginState is LoginState.Error) {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text(
                            text = (loginState as LoginState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }

                // Animación post-login: solo se muestra cuando showSuccessAnimation es true
                AnimatedVisibility(
                    visible = showSuccessAnimation,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "¡Login exitoso! Redirigiendo...",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CircularProgressIndicator(modifier = Modifier.size(24.dp)) // Spinner pequeño para reforzar
                    }
                }

                // Botón solo visible si no hay loading ni animación de éxito
                if (!showSuccessAnimation && loginState is LoginState.Error) {
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

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "¿Olvidaste tu contraseña?",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { navController.navigate(Routes.OlvidarPassword) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¿No tienes cuenta? Regístrate aquí",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { navController.navigate(Routes.Register) }
        )
    }
}
