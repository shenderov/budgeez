package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.entities.GeneralPurpose;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomPurposeRepository extends CrudRepository<GeneralPurpose, Long> {
    @Query("select p from GeneralPurpose p where p.type='GENERAL' or p.user.id=?1")
    List<GeneralPurpose> findAllUserSpecified(Long userId);

    @Query("select p from GeneralPurpose p where p.type='CUSTOM' and p.user.id=?1")
    List<GeneralPurpose> findAllCustomPurposes(Long userId);

    @Query(nativeQuery = true, value = "SELECT * FROM purpose WHERE purpose_id IN (SELECT record.purpose_id FROM record WHERE record.id=?1 AND record.date BETWEEN ?2 AND ?3 GROUP BY record.purpose_id)")
    List<GeneralPurpose> getAllActualUserPurposesBetweenDates(long userId, long startDate, long endDate);

    @Query(nativeQuery = true, value = "SELECT * FROM purpose WHERE purpose_id IN (SELECT record.purpose_id FROM record WHERE record.id=?1 GROUP BY record.purpose_id)")
    List<GeneralPurpose> getAllActualUserPurposes(long userId);
}
