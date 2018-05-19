package ir.mavaji.parsmorph.domain

import javax.persistence.*

/**
 * @author Vahid Mavaji
 */
@MappedSuperclass
abstract class Affix(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        open val id: Long? = null,

        @Column(name = "phonological_form")
        open val phonologicalForm: String? = null,

        @Column(name = "position")
        open val position: Int? = null,

        @Column(name = "stress_pattern")
        open val stressPattern: String? = null,

        @Column(name = "vowel")
        open val vowel: Boolean? = null,

        @Column(name = "affix_len")
        open val affixLen: Int? = null,

        @ManyToOne
        @JoinColumn(name = "syntactic_category_id")
        open val stemCategory: SyntacticCategory? = null,

        @ManyToOne
        @JoinColumn(name = "affix_type_id")
        open val affixType: AffixType? = null,

        @Column(name = "written_form")
        open val writtenForm: String? = null
)

@Entity
@Table(name = "fl_affix_type")
data class AffixType(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        val id: Long? = null,

        @Column(name = "code")
        val code: String? = null,

        @Column(name = "name")
        val name: String? = null
) {
    companion object {
        const val PLM = "PLM"
        const val INM = "INM"
        const val RCM = "RCM0"
        const val PEC = "PEC"
        const val EZM = "EZM"
        const val COC = "COC"
        const val CAM = "CAM"
        const val SAM = "SAM"
        const val CAdM = "CAdM"
        const val ONM = "ONM"
        const val NEM = "NEM"
        const val ISM = "ISM"
        const val IMM = "IMM"
        const val PPM = "PPM"
        const val PEI = "PEI"
        const val SUC = "SUC"
    }
}

@Entity
@Table(name = "fl_affix_aux")
data class AffixAux(override val id: Long? = null, override val phonologicalForm: String? = null, override val position: Int? = null,
                    override val stressPattern: String? = null, override val vowel: Boolean? = null,
                    override val affixLen: Int? = null, override val stemCategory: SyntacticCategory? = null, override val affixType: AffixType? = null,
                    override val writtenForm: String? = null) : Affix(id, phonologicalForm, position, stressPattern, vowel, affixLen,
        stemCategory, affixType, writtenForm)

@Entity
@Table(name = "fl_affix_drv")
data class AffixDrv(override val id: Long? = null, override val phonologicalForm: String? = null, override val position: Int? = null,
                    override val stressPattern: String? = null, override val vowel: Boolean? = null,
                    override val affixLen: Int? = null, override val stemCategory: SyntacticCategory? = null, override val affixType: AffixType? = null,
                    override val writtenForm: String? = null) : Affix(id, phonologicalForm, position, stressPattern, vowel, affixLen,
        stemCategory, affixType, writtenForm)

@Entity
@Table(name = "fl_affix_infl")
data class AffixInfl(override val id: Long? = null, override val phonologicalForm: String? = null, override val position: Int? = null,
                     override val stressPattern: String? = null, override val vowel: Boolean? = null,
                     override val affixLen: Int? = null, override val stemCategory: SyntacticCategory? = null, override val affixType: AffixType? = null,
                     override val writtenForm: String? = null) : Affix(id, phonologicalForm, position, stressPattern, vowel, affixLen,
        stemCategory, affixType, writtenForm)