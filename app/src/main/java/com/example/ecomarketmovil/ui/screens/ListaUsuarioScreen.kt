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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ecomarketmovil.data.UsuarioRespuesta
import com.example.ecomarketmovil.ui.components.BackIconButton
@Composable
fun ListaUsuarioScreen(
    usuarios: List<UsuarioRespuesta>,
    onEditar: (UsuarioRespuesta) -> Unit,
    onEliminar: (UsuarioRespuesta) -> Unit,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) { BackIconButton(navController = navController)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Gestión de Usuarios",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(usuarios) { usuario ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEditar(usuario) } // al tocar la tarjeta -> editar usuario
                        .padding(4.dp)
                ) {

                    Column(modifier = Modifier.padding(16.dp)) {

                        val estado = if (usuario.activo == 1) "Activo" else "Inactivo"
                        val colorEstado = if (usuario.activo == 1) Color(0xFF008000) else Color.Red

                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                "ID: ${usuario.id}",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                estado,
                                color = colorEstado,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text("Nombre: ${usuario.nombre}", style = MaterialTheme.typography.bodyLarge)
                        Text("Email: ${usuario.email}")
                        Text("RUT: ${usuario.rut}")
                        Text("Rol: ${usuario.rol}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Eliminar",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.clickable { onEliminar(usuario) }
                            )
                        }
                    }
                }
            }
        }
    }
}
}