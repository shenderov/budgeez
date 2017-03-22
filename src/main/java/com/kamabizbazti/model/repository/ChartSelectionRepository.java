package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.dao.ChartSelection;
import com.kamabizbazti.model.enumerations.ChartSelectionIdEnum;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChartSelectionRepository extends CrudRepository<ChartSelection, ChartSelectionIdEnum> {
    @Query("select s from ChartSelection s where s.authRequired = false")
    List<ChartSelection> findAllGeneralSelections();

    @Query("select s from ChartSelection s where s.authRequired = true")
    List<ChartSelection> findAllUserSelections();
}