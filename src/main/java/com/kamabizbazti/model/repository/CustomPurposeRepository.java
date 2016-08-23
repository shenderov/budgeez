package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.entities.CustomPurpose;
import com.kamabizbazti.model.entities.GeneralPurpose;
import com.kamabizbazti.security.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomPurposeRepository  extends CrudRepository<CustomPurpose, Long> {
 //   public interface CustomPurposeRepository {

    @Query("select p from GeneralPurpose p where p.type='GENERAL' or p.user.id=:userId")
    List<GeneralPurpose> findAllUserSpecified(@Param("userId") Long userId);

//    CustomPurpose save(CustomPurpose customPurpose);
}
