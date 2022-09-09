package com.zasa.xyzfood.response

data class BranchResult(
    val description: String,
    val id: String,
    val image_url: String,
    val latitude: Double,
    val longitude: Double,
    val name: String
)