package com.example.project.models

import java.util.Date

data class NoteKey(
    var key: String? = null,
    var value: Note? = null
)

data class Note(
    var tittle: String? = null,
    var detail: String? = null,
    var date: Long? = Date().time
)
