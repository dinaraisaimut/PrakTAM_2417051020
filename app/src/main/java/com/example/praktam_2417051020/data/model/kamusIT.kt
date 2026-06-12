package com.example.praktam_2417051020.data.model

import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName

data class KamusIT(
    @SerializedName("id")
    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: Int = 0,

    @SerializedName("istilah")
    @get:PropertyName("istilah")
    @set:PropertyName("istilah")
    var istilah: String = "",

    @SerializedName("definisi")
    @get:PropertyName("definisi")
    @set:PropertyName("definisi")
    var definisi: String = "",

    @SerializedName("penjelasan")
    @get:PropertyName("penjelasan")
    @set:PropertyName("penjelasan")
    var penjelasan: String = "",

    @SerializedName("contoh_penggunaan")
    @get:PropertyName("contoh_penggunaan")
    @set:PropertyName("contoh_penggunaan")
    var contohPenggunaan: String = "",

    @SerializedName("kategori")
    @get:PropertyName("kategori")
    @set:PropertyName("kategori")
    var kategori: String = "",

    @SerializedName("huruf_awal")
    @get:PropertyName("huruf_awal")
    @set:PropertyName("huruf_awal")
    var hurufAwal: String = "",

    @SerializedName("image_url")
    @get:PropertyName("image_url")
    @set:PropertyName("image_url")
    var imageUrl: String? = null,

    @SerializedName("is_favorite")
    @get:PropertyName("is_favorite")
    @set:PropertyName("is_favorite")
    var isFavorite: Boolean = false
)