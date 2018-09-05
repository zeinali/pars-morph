package ir.mavaji.parsmorph.controller

import ir.mavaji.parsmorph.service.PhonemeAnalyser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

/**
 * @author Vahid Mavaji
 */
@RestController
@RequestMapping("/phonemes")
class PhonemeAnalyserController(@Inject private val phonemeAnalyser: PhonemeAnalyser) {

    @GetMapping("/")
    @ResponseBody
    fun analyse(@RequestParam("q") q: String): ResponseEntity<List<String>> {
        val phonemes = phonemeAnalyser.textToPhoneme(q)

        return ResponseEntity(phonemes, HttpStatus.OK)
    }
}