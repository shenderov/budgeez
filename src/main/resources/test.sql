SELECT
  (SELECT purpose.name FROM purpose WHERE purpose.purpose_id=record.purpose_id),
  avg(amount) AS amount
FROM record
WHERE record.purpose_id IN
      (SELECT purpose.purpose_id
       FROM purpose
       WHERE purpose.type = 'GENERAL' AND (SELECT COUNT(*) > 0
                                           FROM record
                                           WHERE record.purpose_id = purpose.purpose_id) IS TRUE) AND
      date BETWEEN 1477951200331 AND 1480543199332
GROUP BY record.purpose_id
UNION ALL
SELECT
  0,
  avg(amount)
FROM record
WHERE purpose_type = 'CUSTOM' AND date BETWEEN 1477951200331 AND 1480543199332;


SELECT
  record.purpose_id,
  avg(amount) AS amount
FROM record
WHERE record.purpose_id IN
      (SELECT purpose.purpose_id
       FROM purpose
       WHERE purpose.id = 11 AND (SELECT COUNT(*) > 0
                                           FROM record
                                           WHERE record.purpose_id = purpose.purpose_id) IS TRUE) AND
      date BETWEEN 1477951200331 AND 1480543199332
GROUP BY record.purpose_id;




SELECT record.purpose_id, SUM(record.amount)
FROM record
WHERE record.purpose_id IN
      (SELECT record.purpose_id FROM record WHERE record.id=11 AND record.date BETWEEN 1472677200023 AND 1480543199023 GROUP BY record.purpose_id) GROUP BY record.purpose_id;


SELECT (SELECT purpose.name FROM purpose WHERE purpose.purpose_id=record.purpose_id) AS purpose, SUM(record.amount) FROM record WHERE record.purpose_id IN (SELECT record.purpose_id FROM record WHERE record.id=11 GROUP BY record.purpose_id) AND record.date BETWEEN 1472677200023 AND 1480543199023 GROUP BY record.purpose_id;



SELECT purpose_id FROM record WHERE record.id=11 AND (record.date BETWEEN 1472677200023 AND 1480543199023) GROUP BY purpose_id;

DELETE FROM record WHERE record.id=11 AND (record.date BETWEEN 1472677200023 AND 1480543199023) AND record.purpose_id=5;

SELECT * FROM purpose WHERE purpose_id IN
(SELECT record.purpose_id FROM record WHERE record.id=11 AND record.date BETWEEN 1480543200263 AND 1483221599263 GROUP BY record.purpose_id);

DELETE FROM record WHERE purpose_type='CUSTOM';