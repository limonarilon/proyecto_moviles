package com.example.ecomarketmovil.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ecomarketmovil.ui.viewmodels.FormularioEvent
import com.example.ecomarketmovil.ui.viewmodels.FormularioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioScreen(
    navController: NavController, // 1. Aceptar NavController
    viewModel: FormularioViewModel = viewModel()
) {
    val state = viewModel.state
    val navegacionExitosa by viewModel.navegacionExitosa.collectAsState()

    // 2. Efecto para navegar hacia atrás
    LaunchedEffect(navegacionExitosa) {
        if (navegacionExitosa) {
            navController.popBackStack()
            viewModel.onNavegacionCompleta()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text("Registro nuevo usuario",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = state.nombre,
            onValueChange = { viewModel.onEvent(FormularioEvent.OnNombreChanged(it)) },
            label = { Text("Nombre Completo") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.nombreError != null,
            supportingText = {
                if (state.nombreError != null) {
                    Text(state.nombreError)
                }

            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = { viewModel.onEvent(FormularioEvent.OnEmailChanged(it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.emailError != null,
            supportingText = {
                if (state.emailError != null) {
                    Text(state.emailError)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.contrasena,
            onValueChange = { viewModel.onEvent(FormularioEvent.OnContrasenaChanged(it)) },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.contrasenaError != null,
            supportingText = {
                if (state.contrasenaError != null) {
                    Text(state.contrasenaError)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.rut,
            onValueChange = { viewModel.onEvent(FormularioEvent.OnRutChanged(it)) },
            label = { Text("RUT") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.rutError != null,
            supportingText = {
                if (state.rutError != null) {
                    Text(state.rutError)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.direccion,
            onValueChange = { viewModel.onEvent(FormularioEvent.OnDireccionChanged(it)) },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth(),
            isError = state.direccionError != null,
            supportingText = {
                if (state.direccionError != null) {
                    Text(state.direccionError)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Cargo",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            listOf("Empleado", "Administrador", "Logística").forEach { cargoOption ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onEvent(FormularioEvent.OnCargoChanged(cargoOption))
                        }
                        .padding(vertical = 2.dp)
                ){
                    RadioButton(
                        selected = state.cargo == cargoOption,
                        onClick = {
                            viewModel.onEvent(FormularioEvent.OnCargoChanged(cargoOption))
                        }
                    )
                    Text(text = cargoOption,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        if (state.cargoError != null) {
            Text(
                text = state.cargoError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }
        val generos = listOf("Masculino", "Femenino", "Prefiero no decir")

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = state.genero,
                onValueChange = { },
                readOnly = true,
                label = { Text("Género") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth().menuAnchor(),
                    isError = state.generoError != null,
                    supportingText = {
                        if (state.generoError != null) {
                            Text(state.generoError)
                        }
                    }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                generos.forEach { generoOption ->
                    DropdownMenuItem(
                        text = { Text(generoOption) },
                        onClick = {
                            viewModel.onEvent(FormularioEvent.OnGeneroChanged(generoOption))
                            expanded = false
                        }
                    )
                }
            }
        }
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
    // 3. Actualizar la previsualización
    FormularioScreen(navController = rememberNavController())
}
