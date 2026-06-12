package com.example.praktam_2417051020.data.repository

import com.example.praktam_2417051020.data.model.ApiResponse
import com.example.praktam_2417051020.data.model.AuthResponse
import com.example.praktam_2417051020.data.model.KamusIT
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class KamusRepository {

    private val database = FirebaseDatabase.getInstance(
        "https://praktam2417051020-7b46a448-default-rtdb.firebaseio.com"
    )

    // Karena data kamu di Realtime Database masuk langsung sebagai 0, 1, 2, 3, dst.
    // Jadi ambil dari root database, bukan dari child "kamus_it".
    private val kamusRef = database.reference

    private val favoriteIds = mutableSetOf<Int>()

    // Login dan register sekarang sudah diurus FirebaseAuth di AuthScreen.kt
    suspend fun register(nama: String, email: String, password: String): AuthResponse {
        return AuthResponse(false, "Register sudah menggunakan Firebase Authentication", null)
    }

    suspend fun login(email: String, password: String): AuthResponse {
        return AuthResponse(false, "Login sudah menggunakan Firebase Authentication", null)
    }

    suspend fun updateProfile(
        id: Int,
        nama: String,
        email: String,
        password: String?
    ): AuthResponse {
        return AuthResponse(false, "Update profil belum digunakan", null)
    }

    suspend fun getKamusIT(
        userId: Int,
        search: String? = null,
        huruf: String? = null
    ): List<KamusIT> {
        return try {
            val snapshot = kamusRef.get().await()

            val data = snapshot.children.mapNotNull { item ->
                item.getValue(KamusIT::class.java)
            }

            data.map { item ->
                item.copy(isFavorite = favoriteIds.contains(item.id))
            }.filter { item ->
                val cocokSearch = search.isNullOrBlank() ||
                        item.istilah.contains(search, ignoreCase = true) ||
                        item.definisi.contains(search, ignoreCase = true) ||
                        item.kategori.contains(search, ignoreCase = true)

                val cocokHuruf = huruf.isNullOrBlank() ||
                        huruf == "All" ||
                        item.hurufAwal.equals(huruf, ignoreCase = true)

                cocokSearch && cocokHuruf
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun toggleFavorite(userId: Int, istilahId: Int): ApiResponse {
        return if (favoriteIds.contains(istilahId)) {
            favoriteIds.remove(istilahId)
            ApiResponse(true, "Berhasil menghapus dari favorite")
        } else {
            favoriteIds.add(istilahId)
            ApiResponse(true, "Berhasil menambahkan ke favorite")
        }
    }

    suspend fun getFavorites(userId: Int): List<KamusIT> {
        return try {
            val snapshot = kamusRef.get().await()

            snapshot.children.mapNotNull { item ->
                item.getValue(KamusIT::class.java)
            }.filter { item ->
                favoriteIds.contains(item.id)
            }.map { item ->
                item.copy(isFavorite = true)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}