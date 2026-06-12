package com.example.praktam_2417051020.data.api

import com.example.praktam_2417051020.data.model.*
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register.php")
    suspend fun register(
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("login.php")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("update_profile.php")
    suspend fun updateProfile(
        @Field("id") id: Int,
        @Field("nama") nama: String,
        @Field("email") email: String,
        @Field("password") password: String?
    ): AuthResponse

    @GET("get_istilah.php")
    suspend fun getKamusIT(
        @Query("user_id") userId: Int,
        @Query("search") search: String? = null,
        @Query("huruf") huruf: String? = null
    ): List<KamusIT>

    @FormUrlEncoded
    @POST("toggle_favorite.php")
    suspend fun toggleFavorite(
        @Field("user_id") userId: Int,
        @Field("istilah_id") istilahId: Int
    ): ApiResponse

    @GET("get_favorites.php")
    suspend fun getFavorites(
        @Query("user_id") userId: Int
    ): List<KamusIT>
}
