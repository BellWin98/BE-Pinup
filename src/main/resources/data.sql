SET @min_date = '2023-01-01 00:00:00';
SET @max_date = '2024-11-22 23:59:59';

-- Members
INSERT INTO member (email, name, nickname, profile_image_url, role, status, bio, login_type, password, created_at, updated_at)
VALUES ('user1@test.com', 'User1', 'nick1', 'http://example.com/profile/1.jpg', 'ROLE_USER', 'Y', 'Bio for user 1', 'NORMAL', '$2a$10$password1',
        DATE_ADD(@min_date, INTERVAL FLOOR(RAND() * TIMESTAMPDIFF(SECOND, @min_date, @max_date)) SECOND),
        DATE_ADD(@min_date, INTERVAL FLOOR(RAND() * TIMESTAMPDIFF(SECOND, @min_date, @max_date)) SECOND));

-- Places
INSERT INTO place (kakao_map_id, name, address, road_address, longitude, latitude, status, place_category, created_at, updated_at)
SELECT
    CONCAT('kakao', seq.n),
    CONCAT('Place ', seq.n),
    CONCAT('서울시 강남구 역삼동 ', seq.n, '번지'),
    CONCAT('서울시 강남구 테헤란로 ', seq.n),
    CONCAT('127.', LPAD(seq.n, 6, '0')),
    CONCAT('37.', LPAD(seq.n, 6, '0')),
    'Y',
    CASE seq.n % 2 WHEN 0 THEN 'RESTAURANT' ELSE 'CAFE' END,
    DATE_ADD(@min_date, INTERVAL FLOOR(RAND() * TIMESTAMPDIFF(SECOND, @min_date, @max_date)) SECOND),
    DATE_ADD(@min_date, INTERVAL FLOOR(RAND() * TIMESTAMPDIFF(SECOND, @min_date, @max_date)) SECOND)
FROM (
    SELECT @rownum:=@rownum+1 as n FROM
    (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t1,
    (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t2,
    (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t3,
    (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t4,
    (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t5,
    (SELECT @rownum:=-1) t0
    LIMIT 100000
    ) seq;

-- Reviews
INSERT INTO review (comment, rating, member_id, place_id, created_at, updated_at)
SELECT
    CONCAT('Review comment ', seq.n),
    3.5 + (seq.n % 15) / 10.0,
    1,
    (seq.n % 100000) + 1,
    DATE_ADD(@min_date, INTERVAL FLOOR(RAND() * TIMESTAMPDIFF(SECOND, @min_date, @max_date)) SECOND),
    DATE_ADD(@min_date, INTERVAL FLOOR(RAND() * TIMESTAMPDIFF(SECOND, @min_date, @max_date)) SECOND)
FROM (
         SELECT @rownum:=@rownum+1 as n FROM
             (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t1,
             (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t2,
             (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t3,
             (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t4,
             (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) t5,
             (SELECT @rownum:=-1) t0
             LIMIT 100
     ) seq;

-- Review Images
INSERT INTO review_image (url, review_id, created_at, updated_at)
SELECT
    CONCAT('http://example.com/review/', id, '.jpg'),
    id,
    DATE_ADD(@min_date, INTERVAL FLOOR(RAND() * TIMESTAMPDIFF(SECOND, @min_date, @max_date)) SECOND),
    DATE_ADD(@min_date, INTERVAL FLOOR(RAND() * TIMESTAMPDIFF(SECOND, @min_date, @max_date)) SECOND)
FROM review;

-- Keywords
INSERT INTO keyword (keyword, review_id, created_at, updated_at)
SELECT
    CASE
        WHEN id % 5 = 0 THEN '맛있는'
        WHEN id % 5 = 1 THEN '친절한'
        WHEN id % 5 = 2 THEN '분위기좋은'
        WHEN id % 5 = 3 THEN '청결한'
        ELSE '합리적인'
        END,
    id,
    DATE_ADD(@min_date, INTERVAL FLOOR(RAND() * TIMESTAMPDIFF(SECOND, @min_date, @max_date)) SECOND),
    DATE_ADD(@min_date, INTERVAL FLOOR(RAND() * TIMESTAMPDIFF(SECOND, @min_date, @max_date)) SECOND)
FROM review;