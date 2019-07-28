package br.edu.ufabc.ufabcbarganha.data

import br.edu.ufabc.ufabcbarganha.model.Post
import java.util.*
import kotlin.collections.ArrayList

interface DAO {
    val posts: MutableList<Post>

    fun add(Post: Post)
    fun update(position: Int, Post: Post)
    fun removeAll(removePositions: IntArray): Boolean
    fun size(): Int
    fun getItemAt(pos: Int): Post

}