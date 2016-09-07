package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.entities.ChartSelection;
import com.kamabizbazti.model.entities.ChartSelectionId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChartSelectionRepository extends CrudRepository <ChartSelection, ChartSelectionId> {

    @Query("select s from ChartSelection s where s.authRequired='false'")
    List<ChartSelection> findAllGeneralSelections();

    @Query("select s from ChartSelection s where s.authRequired='true'")
    List<ChartSelection> findAllUserSelections();

}