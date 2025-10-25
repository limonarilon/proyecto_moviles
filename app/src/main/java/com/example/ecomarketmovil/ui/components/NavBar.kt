package com.example.ecomarketmovil.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.ecomarketmovil.ui.Routes

@Composable
fun OverflowMenu(isExpanded: Boolean, onItemClick:(String)-> Unit, onDismiss:()->Unit){
    val opciones=listOf("Vision","Mision","Ajustes")
    DropdownMenu(expanded = isExpanded, onDismissRequest = onDismiss, containerColor = Color.White) {
        opciones.forEach {opcion->
            DropdownMenuItem(text={
                Text(text=opcion,)
            }, onClick = {onItemClick(opcion)})

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(navController: NavController){
    TopAppBar(title={ Text(text="Menu") },
        actions= {
            var isMenuOpened by remember { mutableStateOf(false) }
            IconButton(onClick = { isMenuOpened = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Opciones Menu"
                )
                OverflowMenu (
                    isExpanded = isMenuOpened,
                    onItemClick = { opcion ->
                        when (opcion) {
                        "Mision" -> navController.navigate(Routes.Mision)
                        }
                    }
                ) {
                    isMenuOpened=false
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.DarkGray,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}












