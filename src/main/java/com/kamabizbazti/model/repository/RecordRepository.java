package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.entities.Record;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository  extends CrudRepository<Record, Long> {
    @Query("select avg (r.amount) from Record r where r.purpose.purposeId=?1 and r.date between ?2 and ?3")
    Double averageValueByPurposeId(long purposeId, long startDate, long endDate);

    @Query("select avg (r.amount) from Record r where r.purposeType = com.kamabizbazti.model.entities.PurposeType.CUSTOM and r.date between ?1 and ?2")
    Double averageOfCustomRecords(long startDate, long endDate);
}