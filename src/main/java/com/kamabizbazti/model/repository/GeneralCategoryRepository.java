package com.kamabizbazti.model.repository;

import com.kamabizbazti.model.dao.GeneralCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralCategoryRepository extends CrudRepository<GeneralCategory, Long> {

    @Query("select c from GeneralCategory c where c.type='GENERAL'")
    List<GeneralCategory> findAll();

    @Query("select c from GeneralCategory c where c.type='GENERAL' and " +
            "(select count (r) > 0 from Record r where r.category.categoryId=c.categoryId and r.date between ?1 and ?2) is true")
    List<GeneralCategory> getAllActualGeneralCategories(long startDate, long endDate);

    @Query("select c from GeneralCategory c where c.type='GENERAL' and " +
            "(select count (r) > 0 from Record r where r.category.categoryId=c.categoryId) is true")
    List<GeneralCategory> getAllActualGeneralCategories();

    @Query("select c from GeneralCategory c where ((c.type='CUSTOM' and c.uId=?1) or c.type='GENERAL') and  c.categoryId = ?2")
    GeneralCategory findCategoryForUser(long userId, long categoryId);
}