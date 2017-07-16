package com.budgeez.model.repository;

import com.budgeez.model.entities.dao.GeneralCategory;
import com.budgeez.model.enumerations.CategoryType;
import com.budgeez.model.entities.dao.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RecordRepository extends CrudRepository<Record, Long> {
    @Query("select avg (r.amount) from Record r where r.category.categoryId=?1 and r.date between ?2 and ?3")
    Double averageValueByCategoryId(long categoryId, long startDate, long endDate);

    @Query(nativeQuery = true, value = "SELECT (SELECT category.name FROM category WHERE category.category_id=record.category_id), avg(amount) FROM record WHERE record.category_id IN " +
            "(SELECT category.category_id FROM category WHERE category.type = 'GENERAL' AND (SELECT COUNT(*) > 0 FROM record WHERE record.category_id=category.category_id) IS TRUE) " +
            "AND date between ?1 and ?2 GROUP BY record.category_id " +
            "UNION ALL SELECT 'Others', AVG(amount) FROM record WHERE category_type='CUSTOM' AND date BETWEEN ?1 AND ?2")
    List<Object[]> getAverageForAllCategories(long startDate, long endDate);

    @Query("select avg (r.amount) from Record r where r.categoryType = com.budgeez.model.enumerations.CategoryType.CUSTOM and r.date between ?1 and ?2")
    Double averageOfCustomRecords(long startDate, long endDate);

    @Query("select count (r) > 0 from Record r where r.category.categoryId=?1 and r.date between ?2 and ?3")
    boolean isCategoryHaveRecords(long categoryId, long startDate, long endDate);

    @Query("select sum (r.amount) from Record r where r.userId.id=?2 and r.category.categoryId=?1 and r.date between ?3 and ?4")
    Double totalValueByCategoryIdAndUserId(long categoryId, long userId, long startDate, long endDate);

    @Query(nativeQuery = true, value = "SELECT (SELECT category.name FROM category WHERE category.category_id=record.category_id), SUM(record.amount) FROM record WHERE record.category_id IN (SELECT record.category_id FROM record WHERE record.id=?1 GROUP BY record.category_id) AND record.id = ?1 AND record.date BETWEEN ?2 AND ?3 GROUP BY record.category_id")
    List<Object[]> getTotalForAllActualCategoriesForUser(long userId, long startDate, long endDate);

    @Query(nativeQuery = true, value = "SELECT (SELECT category.name FROM category WHERE category.category_id=record.category_id), AVG(record.amount) FROM record WHERE record.category_id IN (SELECT record.category_id FROM record WHERE record.id=?1 GROUP BY record.category_id) AND record.id = ?1 AND record.date BETWEEN ?2 AND ?3 GROUP BY record.category_id")
    List<Object[]> getAverageForAllActualCategoriesForUser(long userId, long startDate, long endDate);

    @Query("select avg (r.amount) from Record r where r.userId.id=?2 and r.category.categoryId=?1 and r.date between ?3 and ?4")
    Double averageValueByCategoryIdAndUserId(long categoryId, long userId, long startDate, long endDate);

    @Query("select r from Record r where r.userId.id=?1 and r.date between ?2 and ?3")
    List<Record> getRecords(long userId, long firstDayOfCurrentMonth, long lastDayOfCurrentMonth);

    @Query("select r from Record r where r.userId.id=?1 and r.date between ?2 and ?3")
    Page<Record> getRecordsPageble(long userId, long firstDayOfCurrentMonth, long lastDayOfCurrentMonth, Pageable pageable);

    @Query("select r from Record r where r.userId.id=?2 and r.recordId=?1")
    Record getRecordByIdAndUserId(long recordId, long userId);

    @Modifying
    @Transactional
    @Query("delete from Record r where r.userId.id=?2 and r.recordId=?1")
    void deleteRecord(long recordId, long userId);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Record r set r.category=?1, r.categoryType=?2, r.amount=?3, r.comment=?4, r.date=?5 where r.userId.id=?7 and r.recordId=?6")
    void updateRecord(GeneralCategory category, CategoryType categoryType, double amount, String comment, long date, long recordId, long userId);
}