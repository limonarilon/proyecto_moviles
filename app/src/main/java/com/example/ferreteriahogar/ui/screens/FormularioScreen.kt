package com.example.ferreteriahogar.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ferreteriahogar.ui.viewmodels.FormularioEvent
import com.example.ferreteriahogar.ui.viewmodels.FormularioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    viewModel: FormularioViewModel = viewModel() // 1. Obtener instancia del ViewModel
) {
    // 2. Leer el estado del ViewModel
    val nombre = viewModel.nombre
    val email = viewModel.email
    val contrasena = viewModel.contrasena
    val rut = viewModel.rut
    val direccion = viewModel.direccion

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Formulario de Registro", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nombre,
            // 3. Enviar evento al cambiar el texto
            onValueChange = { viewModel.onEvent(FormularioEvent.OnNombreChanged(it)) },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onEvent(FormularioEvent.OnEmailChanged(it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { viewModel.onEvent(FormularioEvent.OnContrasenaChanged(it)) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = rut,
            onValueChange = { viewModel.onEvent(FormularioEvent.OnRutChanged(it)) },
            label = { Text("RUT") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = direccion,
            onValueChange = { viewModel.onEvent(FormularioEvent.OnDireccionChanged(it)) },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // 4. Enviar evento de submit
                viewModel.onEvent(FormularioEvent.OnSubmit)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewFormularioScreen() {
    FormularioScreen()
}