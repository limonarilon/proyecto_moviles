package com.example.ecomarketmovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ecomarketmovil.data.Usuario
import com.example.ecomarketmovil.ui.viewmodels.UsuarioViewModel


@Composable
fun FormUsuarioScreen(navController: NavController, viewModel: UsuarioViewModel, id: Int) {
    // Variables de estado para los campos del formulario de usuario
    var rut by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    // Recogemos los estados del ViewModel
    val mensajeError by viewModel.mensajeError.collectAsState()
    val navegacionExitosa by viewModel.navegacionExitosa.collectAsState()

    // Si la operación es exitosa, navegamos hacia atrás
    LaunchedEffect(navegacionExitosa) {
        if (navegacionExitosa) {
            navController.popBackStack()
            viewModel.onNavegacionCompleta() // Resetea el estado para evitar re-navegación
        }
    }

    // Si estamos en modo edición (id != -1), obtenemos y llenamos los campos del usuario
    LaunchedEffect(id) {
        if (id != -1) {
            val usuario = viewModel.obtenerPorRut(rut) // Asumimos que existe este método en el ViewModel
            if (usuario != null) {
                rut = usuario.rut
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
            val titulo = if (rut != "-1") "Editar Usuario" else "Agregar Usuario"
            Text(titulo, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Campo de RUT
            OutlinedTextField(
                value = rut,
                onValueChange = { rut = it },
                label = { Text("RUT") },
                modifier = Modifier.fillMaxWidth(),
                // Si es modo edición, el RUT no debería cambiarse
                enabled = (id == -1)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Contraseña
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Campo de Dirección
            OutlinedTextField(
                value = direccion,
                onValueChange = { direccion = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))


            // Muestra el mensaje de error si no es nulo
            mensajeError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    // Llama a una función de validación específica para usuario en el ViewModel
                    viewModel.validarYGuardar(rut, nombre, email, contrasena, direccion)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFormUsuario() {
    // Para el Preview, es mejor usar un ViewModel falso o sin dependencias
    // Aquí se mantiene la instancia directa para simplicidad, pero tenlo en cuenta.
    val navController = rememberNavController()
    val viewModel = UsuarioViewModel() // Considera usar un ViewModel de previsualización
    FormUsuarioScreen(navController, viewModel, -1) // Preview para agregar un usuario nuevo
}