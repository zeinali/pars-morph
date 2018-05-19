package ir.mavaji.parsmorph.repository

import ir.mavaji.parsmorph.domain.Entry
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

/**
 * @author Vahid Mavaji
 */
interface EntryRepository : JpaRepository<Entry, Long> {
    @Query("from Entry e where e.entryLen>1 and e.writtenForm= :writtenForm")
    fun findByWrittenForm(@Param("writtenForm") writtenForm: String): List<Entry>
}