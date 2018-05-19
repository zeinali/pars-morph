package ir.mavaji.parsmorph.service

import ir.mavaji.parsmorph.domain.*
import ir.mavaji.parsmorph.model.Category
import ir.mavaji.parsmorph.model.DerivationDto
import ir.mavaji.parsmorph.model.MorphemeDto
import ir.mavaji.parsmorph.model.Possibility
import ir.mavaji.parsmorph.repository.AffixAuxRepository
import ir.mavaji.parsmorph.repository.AffixDrvRepository
import ir.mavaji.parsmorph.repository.AffixInflRepository
import ir.mavaji.parsmorph.repository.EntryRepository
import org.springframework.stereotype.Service
import javax.inject.Inject

/**
 * @author Vahid Mavaji
 */
@Service
class MorphologicalAnalyser(@Inject private val affixInflRepository: AffixInflRepository,
                            @Inject private val affixDrvRepository: AffixDrvRepository,
                            @Inject private val affixAuxRepository: AffixAuxRepository,
                            @Inject private val entryRepository: EntryRepository) {

    private var verbPrefixes = hashMapOf<Int, MutableList<AffixInfl>>()
    private var verbSuffixes = hashMapOf<Int, MutableList<AffixInfl>>()
    private var numberSuffixes = hashMapOf<Int, MutableList<AffixInfl>>()
    private var adverbSuffixes = hashMapOf<Int, MutableList<AffixInfl>>()
    private var adjectiveSuffixes = hashMapOf<Int, MutableList<AffixInfl>>()
    private var nounSuffixes = hashMapOf<Int, MutableList<AffixInfl>>()

    private lateinit var derivationalsSuffixes: List<Affix>
    private lateinit var derivationalsPrefixes: List<Affix>

    init {
        initNounSuffixes()
        initAdjectiveSuffixes()
        initAdverbSuffixes()
        initNumberSuffixes()
        initVerbSuffixes()
        initVerbPrefixes()
        initDerivationals()
    }

    private fun initVerbPrefixes() {
        verbPrefixes[2] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.NEM)).toMutableList()
        verbPrefixes[1] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.IMM, AffixType.INM, AffixType.ISM)).toMutableList()
    }

    private fun initVerbSuffixes() {
        verbSuffixes[3] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.PEC, AffixType.SUC)).toMutableList()
        verbSuffixes[2] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.PEI)).toMutableList()
        verbSuffixes[1] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.PPM)).toMutableList()
    }

    private fun initNumberSuffixes() {
        numberSuffixes[4] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.COC)).toMutableList()
        numberSuffixes[3] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.INM, AffixType.RCM, AffixType.PEC, AffixType.EZM)).toMutableList()
        numberSuffixes[2] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.PLM)).toMutableList()
        numberSuffixes[1] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.ONM)).toMutableList()
    }

    private fun initAdverbSuffixes() {
        adverbSuffixes[2] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.EZM, AffixType.PEC)).toMutableList()
        adverbSuffixes[1] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.CAdM, AffixType.PLM)).toMutableList()
    }

    private fun initAdjectiveSuffixes() {
        adjectiveSuffixes[4] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.COC)).toMutableList()
        adjectiveSuffixes[3] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.INM, AffixType.RCM, AffixType.PEC, AffixType.EZM)).toMutableList()
        adjectiveSuffixes[2] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.PLM)).toMutableList()
        adjectiveSuffixes[1] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.CAM, AffixType.SAM)).toMutableList()
    }

    private fun initNounSuffixes() {
        nounSuffixes[3] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.COC)).toMutableList()
        nounSuffixes[2] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.IMM, AffixType.RCM, AffixType.PEC, AffixType.EZM)).toMutableList()
        nounSuffixes[1] = affixInflRepository.findByAffixTypes(arrayOf(AffixType.PLM)).toMutableList()
    }

    private fun initDerivationals() {
        derivationalsSuffixes = affixDrvRepository.findByPositionOrderByAffixLenDesc(1)
        derivationalsPrefixes = affixDrvRepository.findByPositionOrderByAffixLenDesc(-1)
    }

    private fun removeSuffix(morpheme: String, suffixes: List<Affix>): MorphemeDto {
        var morpheme = morpheme
        if (suffixes == null) {
            return MorphemeDto(morpheme, listOf())
        }

        var suffix: Affix? = null
        for (s in suffixes) {
            val writtenForm = s.writtenForm

            if (morpheme.endsWith(writtenForm!!)) {
                morpheme = morpheme.substring(0, morpheme.lastIndexOf(writtenForm))
                suffix = s
                break
            }
        }

        morpheme = morpheme.trim { it <= ' ' }
        return MorphemeDto(morpheme, listOfNotNull(suffix))
    }

    private fun removePrefix(morpheme: String, prefixes: List<Affix>): MorphemeDto {
        var morpheme = morpheme
        var prefix: Affix? = null
        for (p in prefixes) {
            val writtenForm = p.writtenForm
            if (morpheme.startsWith(writtenForm!!)) {
                morpheme = morpheme.substring(writtenForm.length)
                prefix = p
                break
            }
        }

        morpheme = morpheme.trim { it <= ' ' }
        return MorphemeDto(morpheme, listOfNotNull(prefix))
    }

    private fun getEntry(morpheme: String, syntacticCategories: Array<String>): Entry? {
        val entries = entryRepository.findByWrittenForm(morpheme)
        if (entries.size != 0) {
            for (entry in entries) {
                if (entry.category!!.code in syntacticCategories) {
                    return entry
                }
            }
        }

        return null
    }

    private fun analyseNonVerb(morpheme: String, syntacticCategories: Array<String>, affixes_list: MutableList<MutableList<AffixInfl>>): Possibility? {
        return analyseNonVerb(morpheme, syntacticCategories, affixes_list, false)
    }


    fun analyseNonVerb(morpheme: String, syntacticCategories: Array<String>, affixesList: MutableList<MutableList<AffixInfl>>, patch: Boolean): Possibility? {
        var morpheme = morpheme
        var entry: Entry? = getEntry(morpheme, syntacticCategories)
        if (entry != null) {
            return Possibility(stem = entry)
        }

        val originalMorpheme = morpheme

        val maxIter = getMaxIter(affixesList)

        val affixList = mutableListOf<AffixInfl>()
        for (j in 0 until maxIter) {
            for (anAffixesList in affixesList) {
                val morphemeHolder = removeSuffix(morpheme, anAffixesList)
                morpheme = morphemeHolder.morpheme!!
                if (morphemeHolder.affixes != null && morphemeHolder.affixes.isNotEmpty()) {
                    val al = morphemeHolder.affixes[0]
                    if (al != null) {
                        affixList.add(al as AffixInfl)
                    }
                }
            }

            if (patch && affixList.size != 0) {
                val lastAffix = affixList[affixList.size - 1]
                if (lastAffix != null) {
                    if (lastAffix.writtenForm.equals("گان") || lastAffix.writtenForm.equals("کان")) {
                        morpheme += "ه"
                    }
                }
            }

            entry = getEntry(morpheme, syntacticCategories)
            if (entry != null) {
                return Possibility(stem = entry, suffixes = affixList)
            }

            for (i in affixesList.indices) {
                if (i >= affixList.size) {
                    continue
                }

//
//                if (affixesList.contains(affixList[i] as AffixInfl)) {
//                    affixesList[i].remove(affixList[i])
//                    affixesList.set(i, affixesList[i])
//                }
            }

            morpheme = originalMorpheme
        }

        return null
    }

    private fun getMaxIter(affixesList: List<List<Affix>>): Int {
        return getMaxIter(affixesList, 10)
    }

    private fun getMaxIter(affixesList: List<List<Affix>>, minIter: Int): Int {
        var maxIter = 1
        for (s1 in affixesList) {
            if (s1 != null && s1.size > 0) {
                maxIter *= s1.size
            }
        }

        maxIter = Math.min(maxIter, minIter)
        return maxIter
    }

    private fun analyseNoun(morpheme: String): Possibility? {
        val affixesList = mutableListOf<MutableList<AffixInfl>>()
        affixesList.add(nounSuffixes[3]!!)
        affixesList.add(nounSuffixes[2]!!)
        affixesList.add(nounSuffixes[1]!!)

        val possibility = analyseNonVerb(morpheme, SyntacticCategory.nouns(), affixesList, true)
        if (possibility != null) {
            possibility!!.category = (Category.NOUN)
        }

        return possibility
    }

    private fun analyseAdjective(morpheme: String): Possibility? {
        val affixesList = mutableListOf<MutableList<AffixInfl>>()
        affixesList.add(adjectiveSuffixes[4]!!)
        affixesList.add(adjectiveSuffixes[3]!!)
        affixesList.add(adjectiveSuffixes[2]!!)
        affixesList.add(adjectiveSuffixes[1]!!)

        val possibility = analyseNonVerb(morpheme, SyntacticCategory.adjectives(), affixesList)
        possibility?.category = Category.ADJECTIVE

        return possibility
    }

    private fun analyseAdverb(morpheme: String): Possibility? {
        val affixesList = mutableListOf<MutableList<AffixInfl>>()
        affixesList.add(adverbSuffixes[2]!!)
        affixesList.add(adverbSuffixes[1]!!)


        val possibility = analyseNonVerb(morpheme, SyntacticCategory.adverbs(), affixesList)
        possibility?.category = Category.ADVERB

        return possibility
    }

    private fun analyseNumber(morpheme: String): Possibility? {
        val affixesList = mutableListOf<MutableList<AffixInfl>>()
        affixesList.add(numberSuffixes[4]!!)
        affixesList.add(numberSuffixes[3]!!)
        affixesList.add(numberSuffixes[2]!!)
        affixesList.add(numberSuffixes[1]!!)

        val possibility = analyseNonVerb(morpheme, SyntacticCategory.numbers(), affixesList)
        possibility?.category = Category.NUMBER

        return possibility
    }

    private fun removeAuxVerbEndings(morpheme: String): MorphemeDto {
        var morpheme = morpheme

        val endings = affixAuxRepository.findByPositionOrderByAffixLenDesc(1)
        val affixes = ArrayList<Affix>()
        while (true) {
            var found = false
            for (aux in endings) {
                val writtenForm = aux.writtenForm
                if (morpheme.endsWith(writtenForm!!)) {
                    morpheme = morpheme.substring(0, morpheme.lastIndexOf(writtenForm))
                    morpheme = morpheme.trim { it <= ' ' }

                    affixes.add(aux)

                    found = true
                    break
                }
            }

            if (!found) {
                break
            }
        }

        return MorphemeDto(morpheme, affixes)
    }

    private fun removeAuxVerbBeginnings(morpheme: String): MorphemeDto {
        var morpheme = morpheme
        val beginnings = affixAuxRepository.findByPositionOrderByAffixLenDesc(-1)
        val affixes = ArrayList<Affix>()
        while (true) {
            var found = false
            for (aux in beginnings) {
                val writtenForm = aux.writtenForm
                if (morpheme.startsWith(writtenForm!!)) {
                    morpheme = morpheme.substring(writtenForm.length)
                    morpheme = morpheme.trim { it <= ' ' }

                    affixes.add(aux)

                    found = true
                    break
                }
            }

            if (!found) {
                break
            }
        }

        return MorphemeDto(morpheme, affixes)
    }

    private fun analyseVerb(morpheme: String): Possibility? {
        var morpheme = morpheme
        var entry: Entry? = getEntry(morpheme, SyntacticCategory.verbs())
        if (entry != null) {
            return Possibility(Category.VERB, entry)
        }

        var auxiliariesPrefixes = arrayListOf<Affix>()
        var auxiliariesSuffixes = arrayListOf<Affix>()
        var result = removeAuxVerbEndings(morpheme)
        morpheme = result.morpheme!!
        val endings = result.affixes
        if (endings != null) {
            auxiliariesSuffixes.addAll(endings)
        }

        val originalMorpheme = morpheme
        var suffixes3 = verbSuffixes[3]
        var suffixes2 = verbSuffixes[2]
        var suffixes1 = verbSuffixes[1]

        val prefixes2 = verbPrefixes[2]
        val prefixes1 = verbPrefixes[1]

        val affixesList = ArrayList<List<Affix>>()
        affixesList.add(suffixes1!!)
        affixesList.add(suffixes2!!)
        affixesList.add(suffixes3!!)
        affixesList.add(prefixes1!!)
        affixesList.add(prefixes2!!)

        val maxIter = getMaxIter(affixesList)

        for (i in 0 until maxIter) {
            var suffixes = arrayListOf<Affix>()
            var morphemeHolder = removeSuffix(morpheme, suffixes3!!)
            morpheme = morphemeHolder.morpheme!!
            val suffix3 = if (morphemeHolder.affixes!!.isNotEmpty()) morphemeHolder.affixes!![0] else null

            result = removeAuxVerbBeginnings(morpheme)
            morpheme = result.morpheme!!
            val endings3 = result.affixes!!

            morphemeHolder = removeSuffix(morpheme, suffixes2!!)
            morpheme = morphemeHolder.morpheme!!
            val suffix2 = if (morphemeHolder.affixes!!.isNotEmpty()) morphemeHolder.affixes!![0] else null

            result = removeAuxVerbEndings(morpheme)
            morpheme = result.morpheme!!
            val endings2 = result.affixes!!

            morphemeHolder = removeSuffix(morpheme, suffixes1!!)
            morpheme = morphemeHolder.morpheme!!
            val suffix1 = if (morphemeHolder.affixes!!.isNotEmpty()) morphemeHolder.affixes!![0] else null

            result = removeAuxVerbEndings(morpheme)
            morpheme = result.morpheme!!
            val endings1 = result.affixes

            morphemeHolder = removePrefix(morpheme, prefixes2)
            morpheme = morphemeHolder.morpheme!!
            val prefix2 = if (morphemeHolder.affixes!!.isNotEmpty()) morphemeHolder.affixes!![0] else null

            morphemeHolder = removePrefix(morpheme, prefixes1)
            morpheme = morphemeHolder.morpheme!!
            val prefix1 = if (morphemeHolder.affixes!!.isNotEmpty()) morphemeHolder.affixes!![0] else null

            result = removeAuxVerbBeginnings(morpheme)
            morpheme = result.morpheme!!
            val beginnings = result.affixes!!
            auxiliariesPrefixes.addAll(beginnings)

            entry = getEntry(morpheme, SyntacticCategory.verbs())
            if (entry != null) {
                var prefixes = arrayListOf<Affix>()
                if (prefix2 != null) {
                    prefixes.add(prefix2)
                }

                if (prefix1 != null) {
                    prefixes.add(prefix1)
                }

                if (suffix3 != null) {
                    suffixes.add(suffix3)
                }

                if (endings3 != null) {
                    suffixes.addAll(endings3)
                }

                if (suffix2 != null) {
                    suffixes.add(suffix2)
                }

                if (endings2 != null) {
                    suffixes.addAll(endings2)
                }

                if (suffix1 != null) {
                    suffixes.add(suffix1)
                }

                if (endings1 != null) {
                    suffixes.addAll(endings1)
                }

                return Possibility(Category.VERB, entry, null, prefixes, suffixes,
                        auxiliariesSuffixes, auxiliariesPrefixes, null, null, null)
            }

            try {
                suffixes3.remove(suffix3)
                suffixes2.remove(suffix2)
                suffixes1.remove(suffix1)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            morpheme = originalMorpheme
        }

        return null
    }

    private fun analyseDerivationalSuffixes(morpheme: String): DerivationDto {
        var morpheme = morpheme
        var derivationalSuffixes = arrayListOf<Affix>()
        var originalMorpheme = morpheme
        for (derivational in derivationalsSuffixes) {
            val writtenForm = derivational.writtenForm
            if (morpheme.endsWith(writtenForm!!)) {
                morpheme = morpheme.substring(0, morpheme.lastIndexOf(writtenForm))
                val entries = entryRepository.findByWrittenForm(morpheme)
                if (entries.size == 0) {
                    morpheme = originalMorpheme
                } else {
                    originalMorpheme = morpheme
                    morpheme = entries[0].writtenForm!!
                    derivationalSuffixes.add(derivational)
                }
            }
        }

        return DerivationDto(morpheme, derivationalSuffixes)
    }

    private fun analyseDerivationalPrefixes(morpheme: String): DerivationDto {
        var morpheme = morpheme
        var derivationalPrefixes = arrayListOf<Affix>()
        var originalMorpheme = morpheme
        for (derivational in derivationalsPrefixes) {
            val writtenForm = derivational.writtenForm
            if (morpheme.startsWith(writtenForm!!)) {
                morpheme = morpheme.substring(writtenForm.length)
                val entries = entryRepository.findByWrittenForm(morpheme)
                if (entries.size == 0) {
                    morpheme = originalMorpheme
                } else {
                    originalMorpheme = morpheme
                    morpheme = entries[0].writtenForm!!
                    derivationalPrefixes.add(derivational)
                }
            }
        }
        return DerivationDto(morpheme, derivationalPrefixes)
    }

    private fun analyseDerivationals(possibility: Possibility): Possibility? {
        var morpheme = possibility.stem!!.writtenForm

        var derivationHolder = analyseDerivationalPrefixes(morpheme!!)
        morpheme = derivationHolder.morpheme
        var derivationalPrefixes = derivationHolder.derivationalAffixes

        derivationHolder = analyseDerivationalSuffixes(morpheme!!)
        morpheme = derivationHolder.morpheme
        var derivationalSuffixes = derivationHolder.derivationalAffixes

        if (derivationalSuffixes!!.size == 0) {
            derivationalSuffixes = null
        }

        if (derivationalPrefixes!!.size == 0) {
            derivationalPrefixes = null
        }

        if (entryRepository.findByWrittenForm(morpheme!!).size === 0) {
            return null
        }

        possibility.derivationalSuffixes = derivationalSuffixes
        possibility.derivationalPrefixes = derivationalPrefixes
        possibility.root = entryRepository.findByWrittenForm(morpheme!!)[0]

        return possibility
    }

    private fun analyseCompounds(possibility: Possibility?, originalMorpheme: String?): Possibility? {
        var possibility = possibility
        val compounds = ArrayList<List<Entry>>()
        val morpheme: String
        if (possibility != null) {
            if (possibility.root != null) {
                morpheme = possibility.root!!.writtenForm!!
            } else {
                morpheme = possibility.stem!!.writtenForm!!
            }
        } else if (originalMorpheme != null) {
            morpheme = originalMorpheme
        } else {
            return null
        }

        val morphemeTrim = morpheme.replace(" ".toRegex(), "")

        val max = morphemeTrim.length
        for (i in 2 until max) {
            val morpheme1 = morphemeTrim.substring(0, i)
            val morpheme2 = morphemeTrim.substring(i, max)
            val entries1 = entryRepository.findByWrittenForm(morpheme1)
            val entries2 = entryRepository.findByWrittenForm(morpheme2)
            if (entries1.size == 0 || entries2.size == 0) {
                continue
            } else {
                compounds.add(listOf(entries1[0], entries2[0]))
            }
        }

        if (possibility == null) {
            val stem = Entry()
            stem.writtenForm = morpheme
            possibility = Possibility(stem = stem)
        }
        possibility.compounds = compounds

        return possibility
    }

    fun analyse(morpheme: String): List<Possibility>? {
        var morpheme = morpheme
        if (morpheme == "") {
            return null
        }

        morpheme = morpheme.replace("\u200c".toRegex(), "")
        morpheme = morpheme.replace("\u200e".toRegex(), "")
        morpheme = morpheme.replace("\u200f".toRegex(), "")
        morpheme = morpheme.trim { it <= ' ' }

        var possibilities = arrayListOf<Possibility>()

        val nounPossibility = analyseNoun(morpheme)
        val numberPossibility = analyseNumber(morpheme)
        val adjectivePossibility = analyseAdjective(morpheme)
        val adverbPossibility = analyseAdverb(morpheme)
        val verbPossibility = analyseVerb(morpheme)

        if (nounPossibility != null) {
            possibilities.add(nounPossibility)
        }

        if (numberPossibility != null) {
            possibilities.add(numberPossibility)
        }

        if (adjectivePossibility != null) {
            possibilities.add(adjectivePossibility)
        }

        if (adverbPossibility != null) {
            possibilities.add(adverbPossibility)
        }

        if (verbPossibility != null) {
            possibilities.add(verbPossibility)
        }

        var resultDeriv = arrayListOf<Possibility>()
        for (possibility in possibilities) {
            resultDeriv.add(analyseDerivationals(possibility)!!)
        }
        if (possibilities.size == 0) {
            val stem = Entry()
            stem.writtenForm = morpheme
            val possibility = Possibility(stem = stem)
            resultDeriv.add(analyseDerivationals(possibility)!!)
        }

        var result = arrayListOf<Possibility>()
        if (resultDeriv.size > 0) {
            for (possibility in resultDeriv) {
                val p = analyseCompounds(possibility, null)
                if (p != null) {
                    result.add(p)
                }
            }
        }

        val compoundPossibility = analyseCompounds(null, morpheme)
        if (compoundPossibility != null && compoundPossibility.compounds != null && compoundPossibility.compounds!!.size > 0) {
            result.add(compoundPossibility)
        }

        return result
    }
}