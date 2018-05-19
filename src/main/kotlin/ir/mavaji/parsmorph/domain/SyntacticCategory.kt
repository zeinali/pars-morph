package ir.mavaji.parsmorph.domain

import javax.persistence.*

/**
 * @author Vahid Mavaji
 */
@Entity
@Table(name = "fl_syntactic_category")
class SyntacticCategory(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @Column(name = "code")
        val code: String? = null,

        @Column(name = "name")
        val name: String? = null
) {
    companion object {
        const val N1 = "N1"
        const val N2 = "N2"
        const val N3 = "N3"
        const val N4 = "N4"
        const val N5 = "N5"
        const val N6 = "N6"
        const val N7 = "N7"
        const val N8 = "N8"
        const val N9 = "N9"
        const val NA = "NA"
        const val A0 = "A0"
        const val A1 = "A1"
        const val A2 = "A2"
        const val Ad = "Ad"
        const val No = "No"
        const val Nu = "Nu"
        const val V1 = "V1"
        const val V2 = "V2"
        const val V3 = "V3"
        const val V4 = "V4"
        const val V5 = "V5"
        const val V1IM = "V1IM"

        fun nouns() = arrayOf(N1, N2, N3, N4, N5, N6, N7, N8, N9, NA)
        fun adjectives() = arrayOf(A0, A1, A2)
        fun adverbs() = arrayOf(Ad)
        fun numbers() = arrayOf(No, Nu)
        fun verbs() = arrayOf(V1, V2, V3, V4, V5, V1IM)
    }
}