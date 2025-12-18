package com.example.ecomarketmovil.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ecomarketmovil.data.AuthManager
import com.example.ecomarketmovil.remote.RetrofitClientWeather
import com.example.ecomarketmovil.ui.Routes
import com.example.ecomarketmovil.ui.components.NavBar
import com.example.ecomarketmovil.utils.Torch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.ecomarketmovil.ui.components.BackIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(paddingValues: PaddingValues, user: String, navController: NavController) {

    val context = LocalContext.current
    val authManager = remember { AuthManager(context) }
    val isAdmin = authManager.hasRole("ADMIN")

    val torch = remember { Torch(context) }
    var isTorchOn by remember { mutableStateOf(false) }
    var temperature by remember { mutableStateOf<Float?>(null) }
    var weatherLoading by remember { mutableStateOf(true) }
    var weatherError by remember { mutableStateOf<String?>(null) }
    val apiKey = "o1fziqhj6atwpfnflwamx03abfycy7nwv4wwnkvf"

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

    // Apagar la linterna cuando el composable se va
    DisposableEffect(Unit) {
        onDispose {
            if (isTorchOn) {
                torch.turnOff()
            }
        }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido, podemos encender la linterna
            if (!isTorchOn) {
                torch.turnOn()
                isTorchOn = true
            }
        } else {
            // Permiso denegado
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color.LightGray
                    )
                )
            )
    ) {
        NavBar(navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(80.dp))

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

            Text(
                text = "Bienvenido, $user!",
                style = TextStyle(
                    fontSize = 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    shadow = Shadow(
                        color = Color.Gray,
                        offset = Offset(1f, 1f),
                        blurRadius = 2f
                    )
                )
            )

            Spacer(Modifier.height(30.dp))

            Column {
                if (isAdmin) {
                    Button(
                        onClick = { navController.navigate(Routes.MenuUsuario) },
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 35.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(25.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF053900), contentColor = Color.White)
                    ) {
                        Text(text = "Gestión Usuarios", style = TextStyle(fontSize = 21.sp))
                    }
                    Spacer(Modifier.height(10.dp))
                }

                Button(
                    onClick = { navController.navigate(Routes.MenuProducto) },
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(25.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF053900), contentColor = Color.White)
                ) {
                    Text(text = "Gestión Productos", style = TextStyle(fontSize = 21.sp))
                }

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = { navController.navigate(Routes.MenuPedido) },
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(25.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF053900), contentColor = Color.White)
                ) {
                    Text(text = "Gestión Pedidos", style = TextStyle(fontSize = 21.sp))
                }

                Spacer(Modifier.height(10.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 35.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
                                PackageManager.PERMISSION_GRANTED -> {
                                    if (isTorchOn) torch.turnOff() else torch.turnOn()
                                    isTorchOn = !isTorchOn
                                }

                                else -> launcher.launch(Manifest.permission.CAMERA)
                            }
                        },
                        Modifier
                            .weight(1f)
                            .height(52.dp)
                            .padding(end = 8.dp),
                        shape = RoundedCornerShape(25.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTorchOn) Color(0xFFFFD700) else Color(0xFF053900),
                            contentColor = if (isTorchOn) Color.Black else Color.White
                        )
                    ) {
                        Text(text = if (isTorchOn) "Off" else "On", style = TextStyle(fontSize = 21.sp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainMenu() {
    MainMenu(
        paddingValues = PaddingValues(),
        user = "Villalobos",
        navController = rememberNavController()
    )
}
