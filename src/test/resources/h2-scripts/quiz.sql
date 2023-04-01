MERGE INTO quiz (id, hits, errors, hit_percent, registration_date, finalized, id_owner)
    VALUES (100, 0, 0, 0, now(), false, 3);

MERGE INTO quiz (id, hits, errors, hit_percent, registration_date, finalized, id_owner)
    VALUES (101, 0, 0, 80, now(), true, 2);

MERGE INTO quiz (id, hits, errors, hit_percent, registration_date, finalized, id_owner)
    VALUES (102, 0, 0, 90, now(), true, 3);