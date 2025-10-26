package com.example.ecomarketmovil.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ecomarketmovil.ui.Routes
import com.example.ecomarketmovil.ui.viewmodels.UsuarioViewModel

@Composable
fun ListaUsuarioScreen(navController: NavController, viewModel: UsuarioViewModel) {

    // Recogemos los estados del ViewModel
    val textoBusqueda by viewModel.textoBusqueda.collectAsState()
    val usuarios by viewModel.usuariosFiltrados.collectAsState()

    Scaffold(
        floatingActionButton = {
            // Navega al formulario en modo "crear" (usando id -1)
            FloatingActionButton(onClick = { navController.navigate(Routes.FormularioUsuario + "/-1") }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Lista de Usuarios", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo de Búsqueda ---
            OutlinedTextField(
                value = textoBusqueda,
                onValueChange = viewModel::onTextoBusquedaChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Buscar por nombre") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            // -------------------------

            if (usuarios.isEmpty()) {
                Text("No hay usuarios registrados o que coincidan con la búsqueda.")
            } else {
                LazyColumn {
                    items(usuarios) { usuario ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = usuario.nombre,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = usuario.email,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "RUT: ${usuario.rut}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                Row {
                                    Button(
                                        onClick = {
                                            // Navega al formulario en modo "editar", pasando el id
                                            navController.navigate(Routes.formularioUsuarioConRut(usuario.rut))
                                        }
                                    ) {
                                        Text("Editar")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(
                                        onClick = {
                                            viewModel.eliminar(usuario)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.errorContainer,
                                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                                        )

                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewListaUsuario() {
    val navController = rememberNavController()
    val viewModel = UsuarioViewModel().apply {
        cargarDatosDeEjemplo()
    }
    ListaUsuarioScreen(navController, viewModel)
}
