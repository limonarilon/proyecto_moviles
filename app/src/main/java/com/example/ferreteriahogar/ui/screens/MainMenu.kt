package com.example.ferreteriahogar.ui.screens

import android.widget.Space
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ferreteriahogar.ui.components.BackIconButton
import com.example.ferreteriahogar.ui.components.NavBar
import org.w3c.dom.Text

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(paddingValues: PaddingValues, usuario : String, passwordHashed : String , navController : NavController){

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

            Spacer(Modifier.height(100.dp))

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

            Column (

            ) {
                Button(
                    onClick = {

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
                    Text(text = "Boton 1",
                        style = TextStyle(
                            fontSize = 21.sp
                        )
                    )
                }

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {

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
                    Text(text = "Boton 2",
                        style = TextStyle(
                            fontSize = 21.sp
                        )
                    )
                }

                Spacer(Modifier.height(10.dp))

                Button(
                    onClick = {

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
                    Text(text = "Boton 3",
                        style = TextStyle(
                            fontSize = 21.sp
                        )
                    )
                }

                Spacer(Modifier.height(10.dp))

                Row (
                    Modifier.fillMaxWidth().padding(horizontal = 35.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Button(
                        onClick = {

                        },
                        Modifier
                            .weight(1f)
                            .height(52.dp)
                            .padding(end = 8.dp),

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
                        Text(text = "Boton 4",
                            style = TextStyle(
                                fontSize = 21.sp
                            )
                        )
                    }

                    Button(
                        onClick = {

                        },
                        Modifier
                            .weight(1f)
                            .height(52.dp)
                            .padding(start = 8.dp),
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
                        Text(text = "Boton 5",
                            style = TextStyle(
                                fontSize = 21.sp
                            )
                        )
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
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = passwordHashed,
                    fontSize = 17.sp,
                    style = TextStyle(
                        textAlign = TextAlign.Center
                    )

                )


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
