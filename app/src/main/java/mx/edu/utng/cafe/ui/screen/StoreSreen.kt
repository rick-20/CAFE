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
import mx.edu.utng.cafe.model.Producto
import mx.edu.utng.cafe.ui.components.ProductCard

@Composable
fun StoreScreen() {
    val productos = listOf(
        Producto(1, "Laptop HP Pavilion 15", "Intel i5, 8GB RAM, 256GB SSD", 12999.0, true),
        Producto(2, "Mouse Logitech MX", "Mouse inalámbrico ergonómico", 1299.0, true),
        Producto(3, "Audífonos Sony WH-1000XM4", "Cancelación de ruido premium", 5499.0, true),
        Producto(5, "Monitor Samsung 24\"", "Full HD IPS 24 pulgadas", 3299.0, true)
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
                "Catálogo de Productos",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(productos) { producto ->
            ProductCard(producto)
        }
    }
}