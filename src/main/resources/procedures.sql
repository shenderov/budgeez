DELIMITER //
DROP PROCEDURE IF EXISTS test1.getGeneralAverage;
CREATE DEFINER=root@localhost PROCEDURE getGeneralAverage()
  BEGIN
    DECLARE p_id BIGINT(20);
    DECLARE exit_loop BOOLEAN;
    DECLARE cur CURSOR FOR SELECT purpose_id FROM purpose WHERE purpose.type = 'GENERAL' AND (SELECT COUNT(*) > 0 FROM record WHERE record.purpose_id=purpose.purpose_id) is true;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET exit_loop = TRUE;
    CREATE TEMPORARY TABLE IF NOT EXISTS general_average (id BIGINT(20), average DOUBLE) ENGINE=memory;
    TRUNCATE TABLE general_average;
    OPEN cur;
    average_loop: LOOP
      FETCH cur INTO p_id;
      INSERT INTO test1.general_average (id, average) VALUES (p_id, (SELECT AVG(amount) FROM record WHERE record.purpose_id=p_id));
      IF exit_loop THEN
        CLOSE cur;
        LEAVE average_loop;
      END IF;
    END LOOP average_loop;
    INSERT INTO test1.general_average (id, average) VALUES (0, (select avg(amount) from record where purpose_type='CUSTOM'));
    SELECT * FROM general_average;
  END//