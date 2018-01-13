package com.budgeez.model.repository;

import com.budgeez.model.entities.dao.GeneralCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomCategoryRepository extends CrudRepository<GeneralCategory, Long> {
    @Query("select c from GeneralCategory c where c.type='GENERAL' or c.user.id=?1")
    List<GeneralCategory> findAllUserSpecified(Long userId);

    @Query("select c from GeneralCategory c where c.type='CUSTOM' and c.user.id=?1")
    List<GeneralCategory> findAllCustomCategories(Long userId);

    @Query(nativeQuery = true, value = "SELECT * FROM category WHERE category_id IN (SELECT record.category_id FROM record WHERE record.id=?1 AND record.date BETWEEN ?2 AND ?3 GROUP BY record.category_id)")
    List<GeneralCategory> getAllActualUserCategories(long userId, long startDate, long endDate);

    @Query(nativeQuery = true, value = "SELECT * FROM category WHERE category_id IN (SELECT record.category_id FROM record WHERE record.id=?1 GROUP BY record.category_id)")
    List<GeneralCategory> getAllActualUserCategories(long userId);
}
