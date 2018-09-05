package ir.mavaji.parsmorph.service

import org.springframework.stereotype.Service
import javax.inject.Inject

@Service
class PhonemeAnalyser(@Inject private val morphologicalAnalyser: MorphologicalAnalyser) {
    fun textToPhoneme(text: String): List<String> {
        val chunks = text.split(" ")

        val chunkPhonemes: MutableList<MutableList<String>> = arrayListOf()

        for (chunk in chunks) {
            val chunkPhonologies: MutableList<String> = arrayListOf()

            val possibilities = morphologicalAnalyser.analyse(chunk)

            var lastPhonology: String? = null

            if (possibilities != null) {
                for (possibility in possibilities) {
                    val toPhonology = possibility.phonology();
                    if (!toPhonology.equals(lastPhonology) && toPhonology.isNotBlank()) {
                        chunkPhonologies.add(toPhonology)
                        lastPhonology = toPhonology
                    }
                }
                chunkPhonemes.add(chunkPhonologies)
            }
        }

        val phonemes: MutableList<String> = arrayListOf()
        for (chunkPhoneme in chunkPhonemes) {

            phonemes.addAll(phonemes)

            if (phonemes.isEmpty()) {
                phonemes.addAll(chunkPhoneme)
            } else {
                for (i in 0 until chunkPhoneme.size) {
                    val c = chunkPhoneme[i]
                    val index = i * (phonemes.size / chunkPhoneme.size);
                    for (j in index until (index + (phonemes.size / chunkPhoneme.size))) {
                        phonemes[j] += " $c"
                    }
                }
            }
        }

        return phonemes
    }
}