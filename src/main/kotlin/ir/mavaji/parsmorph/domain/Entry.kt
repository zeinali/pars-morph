package ir.mavaji.parsmorph.domain

import javax.persistence.*

@Entity
@Table(name = "fl_entry")
data class Entry(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        val id: Long? = null,

        @Column(name = "written_form")
        var writtenForm: String? = null,

        @Column(name = "phonological_form")
        val phonologicalForm: String? = null,

        @Column(name = "freq")
        val freq: String? = null,

        @Column(name = "stress_pattern")
        val stressPattern: String? = null,

        @Column(name = "entry_len")
        val entryLen: Int? = null,

        @ManyToOne
        @JoinColumn(name = "syntactic_category_id")
        val category: SyntacticCategory? = null
)