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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ecomarketmovil.ui.components.NavBar

@Composable
fun Vision(paddingValues: PaddingValues, navController: NavController){
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



            Spacer(Modifier.height(90.dp))

            Text(text = "VISIÓN",
                style = TextStyle(
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                    shadow = Shadow(
                        color = Color.Gray,
                        offset = Offset(1f, 1f),
                        blurRadius = 2f
                    )
                )
            )

            Text(text = "Ser la tienda en línea líder en productos ecológicos y sustentables, reconocida por inspirar una comunidad que valora el respeto por la naturaleza, el consumo responsable y la innovación verde. En EcoMarket, aspiramos a construir un futuro donde cada elección de compra sea una oportunidad para cuidar el planeta.",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Justify
                ),
                modifier = Modifier.fillMaxWidth(0.9f)
                    .padding(top = 10.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVision() {
    Mision(
        paddingValues = PaddingValues(),
        navController = rememberNavController()
    )
}