package com.example.myproject.Model

class Anki( var ankiflashcard:MutableList<AnkiFlashCard>) {
    constructor(): this(mutableListOf())
}