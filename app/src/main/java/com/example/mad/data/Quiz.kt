package com.example.mad.data

data class Quiz(
    val id: Int,
    val questionNumber: String,
    val choices: List<String>,
    val correctAnswer: String
)
