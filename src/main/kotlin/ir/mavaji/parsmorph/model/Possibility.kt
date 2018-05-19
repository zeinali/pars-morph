package ir.mavaji.parsmorph.model

import ir.mavaji.parsmorph.domain.Affix
import ir.mavaji.parsmorph.domain.Entry

data class Possibility(var category: Category? = null, val stem: Entry? = null, var root: Entry? = null,
                       val prefixes: List<Affix>? = null, val suffixes: List<Affix>? = null,
                       val auxiliariesSuffixes: List<Affix>? = null, val auxiliariesPrefixes: List<Affix>? = null,
                       var derivationalSuffixes: List<Affix>? = null, var derivationalPrefixes: List<Affix>? = null,
                       var compounds: List<List<Entry>>? = null)