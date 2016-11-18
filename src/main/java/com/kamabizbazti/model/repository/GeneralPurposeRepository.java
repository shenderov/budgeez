package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.entities.GeneralPurpose;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralPurposeRepository extends CrudRepository<GeneralPurpose, Long> {

    @Query("select p from GeneralPurpose p where p.type='GENERAL'")
    List<GeneralPurpose> findAll();

    @Query("select p from GeneralPurpose p where p.type='GENERAL' and " +
            "(select count (r) > 0 from Record r where r.purpose.purposeId=p.purposeId and r.date between ?1 and ?2) is true")
    List<GeneralPurpose> getAllActualGeneralPurposes(long startDate, long endDate);

    @Query("select p from GeneralPurpose p where p.type='GENERAL' and " +
            "(select count (r) > 0 from Record r where r.purpose.purposeId=p.purposeId) is true")
    List<GeneralPurpose> getAllActualGeneralPurposes();
}