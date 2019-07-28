package br.edu.ufabc.ufabcbarganha.data

import br.edu.ufabc.ufabcbarganha.model.Post
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList

object HousingDAO: DAO {
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
        val house1 = Post("Joazinho Testeiro", "Apartamento 1 quarto", "https://cdn.cyrela.com.br/Files/Imagens/Imoveis/2091/destaque/130863767826047625_1944x1080-16terraco-apartamento.jpg", 600.0, "Ótima oportunidade de moradia teste perto da faculdade você não pode perder, não perca por favor", Date())
        house1.latLng = LatLng(-23.643498, -46.527363)

        val house2 = Post("Maria do Teste", "A República", "https://imagens.zapcorp.com.br/imoveis/1899486/11992/5170c44d-fd52-4f76-9d08-34d67ff6d249_m.jpg", 900.0, "República masculina para teste, por favor apenas testadores oficiais blablabla, isso é so um teste", Date())
        house2.latLng = LatLng(-23.643975, -46.525655)

        posts.add(house1)
        posts.add(house2)
    }
}