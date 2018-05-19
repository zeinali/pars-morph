package ir.mavaji.parsmorph.repository

import ir.mavaji.parsmorph.domain.AffixAux
import ir.mavaji.parsmorph.domain.AffixDrv
import ir.mavaji.parsmorph.domain.AffixInfl
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author Vahid Mavaji
 */
interface AffixAuxRepository : JpaRepository<AffixAux, Long> {
    fun findByPositionOrderByAffixLenDesc(position: Int): List<AffixAux>
}

interface AffixDrvRepository : JpaRepository<AffixDrv, Long> {
    fun findByPositionOrderByAffixLenDesc(position: Int): List<AffixDrv>
}

interface AffixInflRepository : JpaRepository<AffixInfl, Long> {
    fun findAllByOrderByAffixLenDesc(): List<AffixInfl>

    @Query("from AffixInfl where affixType.code in (:affixTypeCodes) order by affixLen desc")
    fun findByAffixTypes(@Param("affixTypeCodes") affixTypeCodes: Array<String>): List<AffixInfl>
}