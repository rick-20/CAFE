package mx.edu.utng.cafe.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.edu.utng.cafe.model.Promocion
import mx.edu.utng.cafe.ui.components.PromoCard

@Composable
fun PromotionsScreen() {
    val promociones = listOf(
        Promocion(1, "Descuento 10%", "10% en electrónica", 100, "2025-01-01", "2025-03-31"),
        Promocion(2, "Envío Gratis", "Envío gratuito", 50, "2025-02-01", "2025-04-30"),
        Promocion(3, "Black Friday 2025", "20% de descuento", 200, "2025-11-20", "2025-11-30"),
        Promocion(4, "Bono $500", "Cupón de $500", 150, "2025-01-15", "2025-06-30")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Promociones Disponibles",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(promociones) { promocion ->
            PromoCard(promocion)
        }
    }
}