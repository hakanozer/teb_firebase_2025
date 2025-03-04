package com.example.project.models

import java.util.Date

data class Note(
    val tittle: String? = null,
    val detail: String? = null,
    val date: Long? = Date().time
)
