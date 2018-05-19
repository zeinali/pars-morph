package ir.mavaji.parsmorph.controller

import ir.mavaji.parsmorph.model.Possibility
import ir.mavaji.parsmorph.service.MorphologicalAnalyser
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

/**
 * @author Vahid Mavaji
 */
@RestController
@RequestMapping("/possibilities")
class MorphologicalAnalyserController(@Inject private val morphologicalAnalyser: MorphologicalAnalyser) {

    @GetMapping("/")
    @ResponseBody
    fun analyse(@RequestParam("q") q: String): ResponseEntity<List<Possibility>> {
        val possibilities = morphologicalAnalyser.analyse(q)

        return ResponseEntity(possibilities, HttpStatus.OK)
    }
}