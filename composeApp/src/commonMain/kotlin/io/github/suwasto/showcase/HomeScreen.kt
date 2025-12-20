package io.github.suwasto.showcase

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import showcase.composeapp.generated.resources.Res
import showcase.composeapp.generated.resources.autumn_essentials
import showcase.composeapp.generated.resources.classic_trench
import showcase.composeapp.generated.resources.striped_shirt
import showcase.composeapp.generated.resources.urban_runner

// Data classes for product and navigation
data class Product(val id: Int, val name: String, val price: String, val imageRes: DrawableResource)
data class BottomNavItem(val label: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector)

// Hardcoded data
val products = listOf(
    Product(1, "Striped Oxford Shirt", "$49.00", Res.drawable.striped_shirt),
    Product(2, "Beige Trench Coat", "$89.00", Res.drawable.classic_trench),
    Product(4, "Urban Runner", "$59.00", Res.drawable.urban_runner),
)

val categories = listOf("All", "New Arrivals", "Clothing", "Accessories")

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem("Favorites", Icons.Filled.FavoriteBorder, Icons.Outlined.FavoriteBorder),
    BottomNavItem("Bag", Icons.Filled.ShoppingBag, Icons.Outlined.ShoppingBag),
    BottomNavItem("Notifications", Icons.Filled.Notifications, Icons.Outlined.Notifications),
    BottomNavItem("Profile", Icons.Filled.Person, Icons.Outlined.Person),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text("FashionStore", fontWeight = FontWeight.SemiBold, color = Color.Black) },
                    navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, contentDescription = "Menu") } },
                    actions = { IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = "Search") } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
                HorizontalDivider(color = Color.LightGray, thickness = 1.dp)
            }
        },
        bottomBar = { BottomNavBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF9F9F9))
        ) {
            CategoryChipsRow()
            ShowcaseBanner()
            ProductGrid()
        }
    }
}

@Composable
fun CategoryChipsRow() {
    var selectedCategory by remember { mutableStateOf("All") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            AssistChip(
                onClick = { selectedCategory = category },
                label = { Text(category) },
                shape = RoundedCornerShape(50),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selectedCategory == category) Color.Black else Color.LightGray,
                    labelColor = if (selectedCategory == category) Color.White else Color.DarkGray
                ),
                border = null
            )
        }
    }
}

@Composable
fun ShowcaseBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box {
            Image(
                painter = painterResource(Res.drawable.autumn_essentials),
                contentDescription = "Autumn Essentials Banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().aspectRatio(1.5f)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 300f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Autumn Essentials",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Upgrade your wardrobe with our latest curated picks.",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                (1..4).forEach { i ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (i == 1) Color.White else Color.Gray.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}

@Composable
fun ProductGrid() {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
        modifier = Modifier.fillMaxSize()
    ) {
        items(products) { product ->
            ProductCard(product)
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                val imageModifier = if (product.name == "Beige Trench Coat") {
                    Modifier.fillMaxWidth().aspectRatio(1f / 1.5f)
                } else {
                    Modifier.fillMaxWidth().aspectRatio(1f)
                }
                Image(
                    painter = painterResource(product.imageRes),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = imageModifier
                )
                IconButton(
                    onClick = {},
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(product.name, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Text(product.price, color = Color.Gray, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun BottomNavBar() {
    var selectedItem by remember { mutableStateOf("Home") }
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = selectedItem == item.label
            val isCenterButton = item.label == "Bag"

            NavigationBarItem(
                selected = isSelected,
                onClick = { selectedItem = item.label },
                icon = {
                    Box(modifier = if (isCenterButton) Modifier
                        .size(56.dp)
                        .background(Color.Black, CircleShape)
                        .padding(4.dp) else Modifier.size(24.dp)
                    ) {
                         Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = if (isCenterButton) Color.White else Color.Black,
                    unselectedIconColor = if (isCenterButton) Color.White else Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
