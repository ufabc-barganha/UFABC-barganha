package br.edu.ufabc.ufabcbarganha.data

import br.edu.ufabc.ufabcbarganha.model.Post
import java.util.*
import kotlin.collections.ArrayList

object ProductDAO: DAO {
    override val posts: MutableList<Post> = ArrayList()
    val instance = this

    init {
        // TODO: remove when "add" operation is implemented
        insertMockData()
    }

    override fun add(Post: Post) {
        posts.add(Post)
    }

    override fun update(position: Int, Post: Post) {
        posts.set(position, Post)
    }

    override fun removeAll(removePositions: IntArray): Boolean {
        return posts.removeAll(
            Array(removePositions.size) {
                    i -> posts.get(removePositions[i])
            })
    }

    override fun size(): Int {
        return posts.size
    }

    override fun getItemAt(pos: Int): Post {
        return posts[pos]
    }

    /**
     * Load mock data from an assets file
     */
    private fun insertMockData() {
        for (i in 0..5) {
            posts.add(Post("Fulano de Tal",
                "Caneta - 4 cores",
                "https://img.kalunga.com.br/FotosdeProdutos/176470z.jpg",
                5.00,
                "Ótimo estado de conservação. Escreve que é uma beleza!",
                Date()))
        }
    }
}