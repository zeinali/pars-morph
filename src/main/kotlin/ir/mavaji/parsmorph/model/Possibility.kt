package ir.mavaji.parsmorph.model

import ir.mavaji.parsmorph.domain.Affix
import ir.mavaji.parsmorph.domain.Entry

/**
 * @author Vahid Mavaji
 */
data class Possibility(var category: Category? = null, val stem: Entry? = null, var root: Entry? = null,
                       val prefixes: List<Affix>? = null, val suffixes: List<Affix>? = null,
                       val auxiliariesSuffixes: List<Affix>? = null, val auxiliariesPrefixes: List<Affix>? = null,
                       var derivationalSuffixes: List<Affix>? = null, var derivationalPrefixes: List<Affix>? = null,
                       var compounds: List<List<Entry>>? = null) {
    fun phonology(): String {
        var result = ""
        if (this.prefixes != null && this.prefixes.isNotEmpty()) {
            for (prefix in this.prefixes) {
                result += prefix.phonologicalForm
            }
        }

        if (this.auxiliariesPrefixes != null && this.auxiliariesPrefixes.isNotEmpty()) {
            for (auxiliariesPrefix in this.auxiliariesPrefixes) {
                result += auxiliariesPrefix.phonologicalForm
            }
        }

        if (stem!!.id != null) {
            result += stem.phonologicalForm
        }

        if (stem.id == null) {
            if (this.derivationalPrefixes != null && this.derivationalPrefixes!!.isNotEmpty()) {
                for (derivationalPrefix in this.derivationalPrefixes!!) {
                    result += derivationalPrefix.phonologicalForm
                }
            }
            if (this.root != null) {
                result += this.root!!.phonologicalForm
            }
            if (this.derivationalSuffixes != null && this.derivationalSuffixes!!.isNotEmpty()) {
                for (i in this.derivationalSuffixes!!.size - 1 downTo 0) {
                    val derivationalSuffix = this.derivationalSuffixes!![i]
                    result += derivationalSuffix.phonologicalForm
                }
            }
        }

        if (this.suffixes != null && this.suffixes.isNotEmpty()) {
            for (i in this.suffixes.size - 1 downTo 0) {
                val suffix = this.suffixes[i]
                result += suffix.phonologicalForm
            }
        }

        if (this.auxiliariesSuffixes != null && this.auxiliariesSuffixes.isNotEmpty()) {
            for (i in this.auxiliariesSuffixes.size - 1 downTo 0) {
                val auxiliariesSuffix = this.auxiliariesSuffixes[i]
                result += auxiliariesSuffix.phonologicalForm
            }
        }

        return result;
    }
}