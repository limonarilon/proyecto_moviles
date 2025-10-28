package com.example.ecomarketmovil.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ecomarketmovil.ui.viewmodels.UsuarioViewModel

@Composable
fun FormUsuarioScreen(navController: NavController, viewModel: UsuarioViewModel, rut: String?) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var rutState by remember { mutableStateOf("") }
    val esNuevo = rut == null

    val mensajeError by viewModel.mensajeError.collectAsState()
    val navegacionExitosa by viewModel.navegacionExitosa.collectAsState()

    LaunchedEffect(navegacionExitosa) {
        if (navegacionExitosa) {
            navController.popBackStack()
            viewModel.onNavegacionCompleta()
        }
    }

    LaunchedEffect(rut) {
        if (!esNuevo) {
            val usuario = viewModel.obtenerPorRut(rut!!)
            if (usuario != null) {
                rutState = usuario.rut
                nombre = usuario.nombre
                email = usuario.email
                contrasena = usuario.contrasena
                direccion = usuario.direccion
            }
        }
    }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp)
        ) {
            val titulo = if (esNuevo) "Agregar Usuario" else "Editar Usuario"
            Text(titulo, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = rutState,
                onValueChange = { rutState = it },
                label = { Text("RUT") },
                modifier = Modifier.fillMaxWidth(),
                enabled = esNuevo
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))


            mensajeError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    viewModel.validarYGuardar(rutState, nombre, email, contrasena, direccion, esNuevo)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF053900),
                    Color.White
                )
            ){
                Text("Guardar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFormUsuario() {
    val navController = rememberNavController()
    val viewModel = UsuarioViewModel()
    FormUsuarioScreen(navController, viewModel, null)
}