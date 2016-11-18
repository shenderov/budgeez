package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.entities.GeneralPurpose;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomPurposeRepository extends CrudRepository<GeneralPurpose, Long> {
    @Query("select p from GeneralPurpose p where p.type='GENERAL' or p.user.id=:userId")
    List<GeneralPurpose> findAllUserSpecified(@Param("userId") Long userId);

    @Query("select p from GeneralPurpose p where p.type='GENERAL' or p.user.id=?1 and (select count (r) > 0 from Record r where r.purpose.purposeId=p.purposeId and r.date between ?2 and ?3) is true")
    List<GeneralPurpose> getAllActualGeneralPurposes(long userId, long startDate, long endDate);

    @Query("select p from GeneralPurpose p where p.type='GENERAL' or p.user.id=?1 and (select count (r) > 0 from Record r where r.purpose.purposeId=p.purposeId) is true")
    List<GeneralPurpose> getAllActualGeneralPurposes(long userId);
}
