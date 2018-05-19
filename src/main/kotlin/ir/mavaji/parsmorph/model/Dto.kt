package ir.mavaji.parsmorph.model

import ir.mavaji.parsmorph.domain.Affix

/**
 * @author Vahid Mavaji
 */
enum class Category {
    NOUN, VERB, ADJECTIVE, ADVERB, NUMBER;
}

data class DerivationDto(val morpheme: String?, val derivationalAffixes: List<Affix>?)

data class MorphemeDto(val morpheme: String?, val affixes: List<Affix>?)