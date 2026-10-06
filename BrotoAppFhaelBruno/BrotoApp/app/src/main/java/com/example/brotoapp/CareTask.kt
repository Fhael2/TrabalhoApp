package com.example.brotoapp


data class CareTask(
    val id: Int,
    val title: String,
    val description: String,
    val plantId: Int,
    val done: Boolean = false
)
