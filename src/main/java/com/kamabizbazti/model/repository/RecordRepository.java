package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.entities.GeneralPurpose;
import com.kamabizbazti.model.entities.PurposeType;
import com.kamabizbazti.model.entities.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RecordRepository extends CrudRepository<Record, Long> {
    @Query("select avg (r.amount) from Record r where r.purpose.purposeId=?1 and r.date between ?2 and ?3")
    Double averageValueByPurposeId(long purposeId, long startDate, long endDate);

    @Query(nativeQuery = true, value = "SELECT (SELECT purpose.name FROM purpose WHERE purpose.purpose_id=record.purpose_id), avg(amount) FROM record WHERE record.purpose_id IN " +
            "(SELECT purpose.purpose_id FROM purpose WHERE purpose.type = 'GENERAL' AND (SELECT COUNT(*) > 0 FROM record WHERE record.purpose_id=purpose.purpose_id) IS TRUE) " +
            "AND date between ?1 and ?2 GROUP BY record.purpose_id " +
            "UNION ALL SELECT 'Others', AVG(amount) FROM record WHERE purpose_type='CUSTOM' AND date BETWEEN ?1 AND ?2")
    List<Object[]> getAverageForAllPurposes(long startDate, long endDate);

    @Query("select avg (r.amount) from Record r where r.purposeType = com.kamabizbazti.model.entities.PurposeType.CUSTOM and r.date between ?1 and ?2")
    Double averageOfCustomRecords(long startDate, long endDate);

    @Query("select count (r) > 0 from Record r where r.purpose.purposeId=?1 and r.date between ?2 and ?3")
    boolean isPurposeHaveRecords(long purposeId, long startDate, long endDate);

    @Query("select sum (r.amount) from Record r where r.userId.id=?2 and r.purpose.purposeId=?1 and r.date between ?3 and ?4")
    Double totalValueByPurposeIdAndUserId(long purposeId, long userId, long startDate, long endDate);

    @Query(nativeQuery = true, value = "SELECT (SELECT purpose.name FROM purpose WHERE purpose.purpose_id=record.purpose_id), SUM(record.amount) FROM record WHERE record.purpose_id IN (SELECT record.purpose_id FROM record WHERE record.id=?1 GROUP BY record.purpose_id) AND record.id = ?1 AND record.date BETWEEN ?2 AND ?3 GROUP BY record.purpose_id")
    List<Object[]> getTotalForAllActualPurposesForUser(long userId, long startDate, long endDate);

    @Query(nativeQuery = true, value = "SELECT (SELECT purpose.name FROM purpose WHERE purpose.purpose_id=record.purpose_id), AVG(record.amount) FROM record WHERE record.purpose_id IN (SELECT record.purpose_id FROM record WHERE record.id=?1 GROUP BY record.purpose_id) AND record.id = ?1 AND record.date BETWEEN ?2 AND ?3 GROUP BY record.purpose_id")
    List<Object[]> getAverageForAllActualPurposesForUser(long userId, long startDate, long endDate);

    @Query("select avg (r.amount) from Record r where r.userId.id=?2 and r.purpose.purposeId=?1 and r.date between ?3 and ?4")
    Double averageValueByPurposeIdAndUserId(long purposeId, long userId, long startDate, long endDate);

    @Query("select r from Record r where r.userId.id=?1 and r.date between ?2 and ?3")
    List<Record> getRecords(long userId, long firstDayOfCurrentMonth, long lastDayOfCurrentMonth);

    @Query("select r from Record r where r.userId.id=?1 and r.date between ?2 and ?3")
    Page<Record> getRecordsPageble(long userId, long firstDayOfCurrentMonth, long lastDayOfCurrentMonth, Pageable pageable);

    @Query("select r from Record r where r.userId.id=?2 and r.recordId=?1")
    Record getRecordByIdAndUserId(long recordId, long userId);

    @Modifying
    @Transactional
    @Query("delete from Record r where r.userId.id=?2 and r.recordId=?1")
    void deleteRecord(long recordId, long userId);

    @Modifying
    @Transactional
    @Query("update Record r set r.purpose=?1, r.purposeType=?2, r.amount=?3, r.comment=?4, r.date=?5 where r.userId.id=?7 and r.recordId=?6")
    void updateRecord(GeneralPurpose purpose, PurposeType purposeType, double amount, String comment, long date, long recordId, long userId);
}