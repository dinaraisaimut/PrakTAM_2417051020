package com.example.praktam_2417051020.design

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.praktam_2417051020.data.SessionManager
import com.example.praktam_2417051020.data.model.KamusIT
import com.example.praktam_2417051020.data.repository.KamusRepository
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen(
    navController: NavController,
    repository: KamusRepository,
    sessionManager: SessionManager
) {
    val user = sessionManager.getUser()
    var listFavorites by remember { mutableStateOf<List<KamusIT>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isLoading = true
        listFavorites = repository.getFavorites(user?.id ?: 0)
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 4.dp
        ) {
            Text(
                text = "Favorit Saya",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (listFavorites.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Belum ada istilah favorit", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listFavorites) { istilah ->
                    KamusCard(
                        istilah = istilah,
                        onClick = { navController.navigate("detail/${istilah.id}") },
                        onFavoriteClick = {
                            scope.launch {
                                repository.toggleFavorite(user?.id ?: 0, istilah.id)
                                listFavorites = repository.getFavorites(user?.id ?: 0)
                            }
                        }
                    )
                }
            }
        }
    }
}
