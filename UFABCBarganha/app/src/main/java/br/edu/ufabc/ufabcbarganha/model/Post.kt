package br.edu.ufabc.ufabcbarganha.model

data class Post(
        val username: String,
        val productName: String,
        val photo: String,
        val price: Double,
        val description: String) {  }