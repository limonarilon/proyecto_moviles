package com.example.ecomarketmovil.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ecomarketmovil.ui.Routes

/*
La idea sería que acá el usuario pueda administrar usuarios del sistema, creando (redireccionando al register)
listando usuarios, eliminar usuarios y editar usuarios.
*/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuUsuario(paddingValues: PaddingValues,navController : NavController) {

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(100.dp))

            Text(
                text = "Gestión de Usuarios",
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

            Column(

            ) {
                Button(
                    onClick = {
                        navController.navigate(Routes.formularioUsuarioConRut("-1"))
                    },
                    Modifier.fillMaxWidth().padding(horizontal = 35.dp).height(52.dp),
                    shape = RoundedCornerShape(25.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        Color.DarkGray,
                        Color.White
                    )
                ) {
                    Text(
                        text = "Agregar nuevo Usuario",
                        style = TextStyle(
                            fontSize = 21.sp
                        )
                    )
                }

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        navController.navigate(Routes.ListaUsuarios)
                    },
                    Modifier.fillMaxWidth().padding(horizontal = 35.dp).height(52.dp),
                    shape = RoundedCornerShape(25.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        Color.DarkGray,
                        Color.White
                    )
                ) {
                    Text(
                        text = "Ver Usuarios existentes",
                        style = TextStyle(
                            fontSize = 21.sp
                        )
                    )
                }

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        navController.navigate(Routes.ListaUsuarios)
                    },
                    Modifier.fillMaxWidth().padding(horizontal = 35.dp).height(52.dp),
                    shape = RoundedCornerShape(25.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        Color.DarkGray,
                        Color.White
                    )
                ) {
                    Text(
                        text = "Buscar un Usuario",
                        style = TextStyle(
                            fontSize = 21.sp
                        )
                    )
                }
                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        navController.navigate(Routes.ListaUsuarios)
                    },
                    Modifier.fillMaxWidth().padding(horizontal = 35.dp).height(52.dp),
                    shape = RoundedCornerShape(25.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        Color.DarkGray,
                        Color.White
                    )
                ) {
                    Text(
                        text = "Editar un Usuario",
                        style = TextStyle(
                            fontSize = 21.sp
                        )
                    )
                }
                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    Modifier.fillMaxWidth().padding(horizontal = 35.dp).height(52.dp),
                    shape = RoundedCornerShape(25.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    ),
                    colors = ButtonDefaults.buttonColors(
                        Color.DarkGray,
                        Color.White
                    )
                ) {
                    Text(
                        text = "Volver",
                        style = TextStyle(
                            fontSize = 21.sp
                        )
                    )
                }
            }
        }
    }

}
@Preview(showBackground = true)
@Composable
fun PreviewMenuUsuario() {
    val navController = rememberNavController()
    MenuUsuario(PaddingValues(),navController)
    }
