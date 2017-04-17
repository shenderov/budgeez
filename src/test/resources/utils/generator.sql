# Ver. Kamabizbazti 1.0
DELIMITER //
DROP PROCEDURE IF EXISTS kb_test.generator//
CREATE DEFINER = kb_test@localhost PROCEDURE generator(IN usersCount INT, IN customCategoriesCount INT, IN recordsPerDay INT, IN recordsDaysAgo INT)
  BEGIN
    DECLARE exit_loop BOOLEAN;
    DECLARE f_name VARCHAR(50);
    DECLARE l_name VARCHAR(50);
    DECLARE e_mail VARCHAR(100);
    DECLARE uid BIGINT;
    DECLARE cat_type VARCHAR(20);
    DECLARE cat_id BIGINT;
    DECLARE cat_count INT;
    DECLARE rec_per_day_count INT;
    DECLARE rec_days_ago_count INT;
    DECLARE time_stamp BIGINT;
    DECLARE cur CURSOR FOR SELECT * FROM generator_names LIMIT usersCount;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET exit_loop = TRUE;
    OPEN cur;
    names_loop: LOOP
      FETCH cur INTO f_name, l_name, e_mail;
      IF exit_loop
      THEN
        CLOSE cur;
        LEAVE names_loop;
      END IF;
      SET cat_count = 1;
      SET rec_days_ago_count = 1;
      SET time_stamp = (SELECT UNIX_TIMESTAMP()) * 1000;
      INSERT INTO user (id, creation_date, enabled, is_activated, last_password_reset_date, name, password, start_day, username, currency_code, language_code) VALUES (NULL, CURRENT_TIME(), b'1', b'0', CURRENT_TIME(),CONCAT(f_name, ' ', l_name), '$2a$10$hDxjZ8W1R/al7Pik07ilt.Nvxvs6C7kDG7vtobmUDH5n/etxr587C', '1',e_mail, '3', '1');
      SET uid = (SELECT LAST_INSERT_ID());
      INSERT INTO user_authorities (users_id, authorities_id) VALUES (uid, 2);
      WHILE cat_count <= customCategoriesCount DO
        INSERT INTO category (dtype, category_id, name, type, u_id, user_id)
        VALUES ('CustomCategory', NULL, CONCAT(f_name, ' ', cat_count), 'CUSTOM', uid, uid);
        SET cat_count = cat_count + 1;
      END WHILE;
      DROP TABLE IF EXISTS user_categories;
      CREATE TEMPORARY TABLE IF NOT EXISTS user_categories AS (SELECT type, category_id FROM category WHERE user_id = uid OR type = 'GENERAL');
      WHILE rec_days_ago_count <= recordsDaysAgo DO
        WHILE rec_per_day_count <= recordsPerDay DO
          SELECT type, category_id FROM user_categories ORDER BY RAND() LIMIT 1 INTO cat_type, cat_id;
          INSERT INTO record (record_id, amount, category_type, comment, date, category_id, id) VALUES (NULL, ROUND((RAND() * (100-1))+1), cat_type, NULL, time_stamp, cat_id, uid);
          SET rec_per_day_count = rec_per_day_count + 1;
        END WHILE;
        SET time_stamp = time_stamp - 86400000;
        SET rec_per_day_count = 1;
        SET rec_days_ago_count = rec_days_ago_count + 1;
      END WHILE;
    END LOOP names_loop;
    DROP TABLE IF EXISTS user_categories;
  END//

DELIMITER ;
CREATE TEMPORARY TABLE IF NOT EXISTS generator_names (fname VARCHAR(50), sname VARCHAR(50), email VARCHAR(100));
TRUNCATE TABLE generator_names;
LOAD DATA LOCAL INFILE '/Users/kostya/Documents/IntelijProjects/KamaBizbaztiBoot/src/test/resources/utils/names_database.csv' INTO TABLE generator_names
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\r\n' (fname, sname, email);
CALL generator(10, 3, 2, 150);
DROP TABLE IF EXISTS generator_names;
DROP PROCEDURE IF EXISTS generator