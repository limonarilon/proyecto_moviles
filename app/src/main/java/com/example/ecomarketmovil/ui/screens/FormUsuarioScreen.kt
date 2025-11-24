package com.example.ecomarketmovil.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ecomarketmovil.ui.viewmodels.UsuarioViewModel

@Composable
fun FormUsuarioScreen(navController: NavController, viewModel: UsuarioViewModel, idUsuario: Int?) {

    val esNuevo = idUsuario == null

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("USER") } // Estado para el rol, con "USER" como default

    val mensajeError by viewModel.mensajeError.collectAsState()
    val navegacionExitosa by viewModel.navegacionExitosa.collectAsState()

    LaunchedEffect(navegacionExitosa) {
        if (navegacionExitosa) {
            navController.popBackStack()
            viewModel.onNavegacionCompleta()
        }
    }

    LaunchedEffect(idUsuario) {
        if (!esNuevo) {
            val usuario = viewModel.obtenerPorIdLocal(idUsuario!!)
            if (usuario != null) {
                nombre = usuario.nombre
                email = usuario.email
                rut = usuario.rut
                rol = usuario.rol // Cargar el rol del usuario que se está editando
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

            val titulo = if (esNuevo) "Registrar Usuario" else "Editar Usuario"
            Text(titulo, style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
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
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                placeholder = { if (!esNuevo) Text("Dejar en blanco para no cambiar") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selección de Rol con RadioButtons
            val roles = listOf("USER", "ADMIN")
            Text("Rol", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                roles.forEach { roleValue ->
                    Row(
                        modifier = Modifier.clickable { rol = roleValue },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (rol == roleValue),
                            onClick = { rol = roleValue }
                        )
                        Text(
                            text = roleValue,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }

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
                    viewModel.validarYGuardar(
                        id = idUsuario,
                        nombre = nombre,
                        email = email,
                        password = password,
                        rut = rut,
                        rol = rol
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF053900),
                    contentColor = Color.White
                )
            ) {
                Text("Guardar")
            }
        }
    }
}

@Composable
fun PreviewFormUsuario() {
    val navController = rememberNavController()
    val viewModel = UsuarioViewModel()
    FormUsuarioScreen(navController, viewModel, null)
}
