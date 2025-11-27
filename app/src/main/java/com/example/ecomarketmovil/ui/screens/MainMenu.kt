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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ecomarketmovil.ui.Routes
import com.example.ecomarketmovil.ui.components.NavBar
import com.example.ecomarketmovil.viewmodels.UiState
import com.example.ecomarketmovil.viewmodels.WeatherViewModel
import com.example.ecomarketmovil.utils.Torch
import com.google.android.gms.location.LocationServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(paddingValues: PaddingValues, usuario : String, passwordHashed : String , navController : NavController){

    val context = LocalContext.current
    val torch = remember { Torch(context) }
    var isTorchOn by remember { mutableStateOf(false) }
    val weatherViewModel: WeatherViewModel = viewModel()
    val weatherData by weatherViewModel.weatherData.collectAsState()
    val uiState by weatherViewModel.uiState.collectAsState()

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Permiso concedido, intenta obtener la ubicación de nuevo
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            weatherViewModel.fetchWeatherData(it.latitude.toString(), it.longitude.toString())
                        } ?: weatherViewModel.onLocationUnavailable()
                    }
                }
            } else {
                // Permiso denegado
                weatherViewModel.onLocationUnavailable()
            }
        }
    )

    LaunchedEffect(Unit) {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) -> {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        weatherViewModel.fetchWeatherData(it.latitude.toString(), it.longitude.toString())
                    } ?: weatherViewModel.onLocationUnavailable()
                }
            }
            else -> {
                locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
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
    ){

        NavBar(navController)

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Spacer(Modifier.height(80.dp))

            when (val state = uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UiState.Success -> {
                    weatherData?.current?.let {
                        Text(
                            text = "Temperatura actual: ${it.temperature}°C",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                }
                is UiState.Error -> {
                    Text(
                        text = state.message,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            color = Color.Red
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            Text(text = "Bienvenido, $usuario!",
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
                Button(
                    onClick = { navController.navigate(Routes.MenuUsuario) },
                    Modifier.fillMaxWidth().padding(horizontal = 35.dp).height(52.dp),
                    shape = RoundedCornerShape(25.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF053900), contentColor = Color.White)
                ) {
                    Text(text = "Gestión Usuarios", style = TextStyle(fontSize = 21.sp))
                }

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = { navController.navigate(Routes.MenuProducto) },
                    Modifier.fillMaxWidth().padding(horizontal = 35.dp).height(52.dp),
                    shape = RoundedCornerShape(25.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF053900), contentColor = Color.White)
                ) {
                    Text(text = "Gestión Productos", style = TextStyle(fontSize = 21.sp))
                }

                Spacer(Modifier.height(10.dp))

                // Botón para Gestión de Pedidos (futuro)
                Button(
                    onClick = { navController.navigate(Routes.MenuPedido) },
                    Modifier.fillMaxWidth().padding(horizontal = 35.dp).height(52.dp),
                    shape = RoundedCornerShape(25.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF053900), contentColor = Color.White)
                ) {
                    Text(text = "Gestión Pedidos", style = TextStyle(fontSize = 21.sp))
                }

                Spacer(Modifier.height(10.dp))

                Row (
                    Modifier.fillMaxWidth().padding(horizontal = 35.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    // Botón para la linterna
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
                        Modifier.weight(1f).height(52.dp).padding(end = 8.dp),
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

            Spacer(Modifier.height(70.dp))

            Column (
                Modifier.fillMaxWidth().padding(horizontal = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Contraseña en SHA256:  ",
                    style = TextStyle(textAlign = TextAlign.Center, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                )

                Spacer(Modifier.height(5.dp))

                Text(text = passwordHashed, fontSize = 17.sp, style = TextStyle(textAlign = TextAlign.Center))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainMenu() {
    MainMenu(
        paddingValues = PaddingValues(),
        usuario = "Villalobos",
        passwordHashed = "abc123123abc",
        navController = rememberNavController()
    )
}
