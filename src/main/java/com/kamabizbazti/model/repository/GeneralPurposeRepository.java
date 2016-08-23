package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.entities.GeneralPurpose;
import com.kamabizbazti.security.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralPurposeRepository extends CrudRepository<GeneralPurpose, Long> {
//public interface GeneralPurposeRepository {
//    @Query("select p from GeneralPurpose p")
  //  Iterable <GeneralPurpose> getAllGeneralPurposes();

    @Query("select p from GeneralPurpose p where p.type='GENERAL'")
    List<GeneralPurpose> findAll();

    GeneralPurpose save(GeneralPurpose generalPurpose);
}
