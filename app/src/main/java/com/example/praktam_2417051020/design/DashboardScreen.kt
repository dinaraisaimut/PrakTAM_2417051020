package com.example.praktam_2417051020.design

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.praktam_2417051020.data.SessionManager
import com.example.praktam_2417051020.data.model.KamusIT
import com.example.praktam_2417051020.data.repository.KamusRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(
    navController: NavController,
    repository: KamusRepository,
    sessionManager: SessionManager
) {
    val currentUser = FirebaseAuth.getInstance().currentUser

    val namaUser = currentUser?.displayName
        ?.takeIf { it.isNotBlank() }
        ?: currentUser?.email?.substringBefore("@")
        ?: "User"

    val userId = currentUser?.uid?.hashCode() ?: 0

    var searchQuery by remember { mutableStateOf("") }
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    var listIstilah by remember { mutableStateOf<List<KamusIT>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(searchQuery, selectedLetter) {
        isLoading = true
        listIstilah = repository.getKamusIT(userId, searchQuery, selectedLetter)
        isLoading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F7FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Text(
                text = "Selamat Datang,",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            Text(
                text = namaUser,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Cari istilah IT...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val alphabets = ('A'..'Z').map { it.toString() }

            item {
                AlphabetChip(
                    letter = "All",
                    isSelected = selectedLetter == null,
                    onClick = { selectedLetter = null }
                )
            }

            items(alphabets) { letter ->
                AlphabetChip(
                    letter = letter,
                    isSelected = selectedLetter == letter,
                    onClick = { selectedLetter = letter }
                )
            }
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(listIstilah) { istilah ->
                    KamusCard(
                        istilah = istilah,
                        onClick = {
                            navController.navigate("detail/${istilah.id}")
                        },
                        onFavoriteClick = {
                            scope.launch {
                                repository.toggleFavorite(userId, istilah.id)
                                listIstilah = repository.getKamusIT(
                                    userId,
                                    searchQuery,
                                    selectedLetter
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AlphabetChip(
    letter: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White,
        tonalElevation = 2.dp,
        shadowElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Text(
            text = letter,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun KamusCard(
    istilah: KamusIT,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF0F2F5)),
                contentAlignment = Alignment.Center
            ) {
                if (!istilah.imageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = istilah.imageUrl,
                        contentDescription = istilah.istilah,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = istilah.istilah.take(1),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = istilah.istilah,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = istilah.definisi,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = istilah.kategori,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }

            IconButton(onClick = onFavoriteClick) {
                Icon(
                    imageVector = if (istilah.isFavorite) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = null,
                    tint = if (istilah.isFavorite) Color.Red else Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailIstilahScreen(
    istilahId: Int?,
    navController: NavController,
    repository: KamusRepository,
    sessionManager: SessionManager
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val userId = currentUser?.uid?.hashCode() ?: 0

    var istilah by remember { mutableStateOf<KamusIT?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(istilahId) {
        if (istilahId != null) {
            val list = repository.getKamusIT(userId)
            istilah = list.find { it.id == istilahId }
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(istilah?.istilah ?: "Detail")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (istilah != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                if (!istilah!!.imageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = istilah!!.imageUrl,
                        contentDescription = istilah!!.istilah,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text(
                    text = istilah!!.istilah,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = istilah!!.kategori,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Definisi:",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Text(
                    text = istilah!!.definisi,
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )

                Text(
                    text = "Penjelasan:",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Text(
                    text = istilah!!.penjelasan,
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )

                Text(
                    text = "Contoh Penggunaan:",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )

                Text(
                    text = istilah!!.contohPenggunaan,
                    color = Color.Gray,
                    fontSize = 15.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        scope.launch {
                            repository.toggleFavorite(userId, istilah!!.id)
                            istilah = istilah!!.copy(
                                isFavorite = !istilah!!.isFavorite
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = if (istilah!!.isFavorite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (istilah!!.isFavorite) {
                            "Hapus dari Favorit"
                        } else {
                            "Tambah ke Favorit"
                        }
                    )
                }
            }
        }
    }
}