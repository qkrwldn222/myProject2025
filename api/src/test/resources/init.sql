CREATE DATABASE IF NOT EXISTS service;
CREATE DATABASE IF NOT EXISTS common;

USE service;

# CREATE TABLE `roles` (
#                          `id` int(11) NOT NULL AUTO_INCREMENT,
#                          `name` varchar(20) NOT NULL,
#                          `code` varchar(2) NOT NULL,
#                          `rank` int(11) NOT NULL,
#                          `created_at` datetime DEFAULT NULL,
#                          `created_by` varchar(10) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
#                          `updated_at` datetime DEFAULT NULL,
#                          `updated_by` varchar(100) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
#                          PRIMARY KEY (`id`)
# );
#
# CREATE TABLE `users` (
#                          `id` bigint(20) NOT NULL AUTO_INCREMENT,
#                          `user_id` varchar(100) NOT NULL,
#                          `user_name` varchar(50) NOT NULL,
#                          `password` varchar(255) DEFAULT NULL,
#                          `role_id` int(11) DEFAULT NULL,
#                          `created_at` datetime DEFAULT NULL,
#                          `created_by` varchar(20) DEFAULT NULL,
#                          `updated_at` datetime DEFAULT NULL,
#                          `updated_by` varchar(20) DEFAULT NULL,
#                          `provider` varchar(10) DEFAULT NULL,
#                          `email` varchar(20) DEFAULT NULL,
#                          `cell_phone` varchar(20) DEFAULT NULL,
#                          PRIMARY KEY (`id`),
#                          KEY `role_id` (`role_id`),
#                          CONSTRAINT `users_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
# );
#
# CREATE TABLE `restaurant` (
#                               `restaurant_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '가게 ID',
#                               `name` varchar(255) NOT NULL COMMENT '가게명',
#                               `category` varchar(100) NOT NULL COMMENT '카테고리',
#                               `phone_number` varchar(20) DEFAULT NULL COMMENT '연락처',
#                               `address` varchar(255) NOT NULL COMMENT '주소',
#                               `status` enum('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING' COMMENT '입점 상태',
#                               `created_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '생성일시',
#                               `updated_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '수정일시',
#                               `created_by` varchar(20) DEFAULT NULL COMMENT '생성자',
#                               `updated_by` varchar(20) DEFAULT NULL COMMENT '수정자',
#                               `management_status` enum('OPEN','CLOSED','PAUSED','PENDING') DEFAULT NULL COMMENT '가게 운영상태',
#                               `owner_id` bigint(20) NOT NULL COMMENT '가게 운영자ID',
#                               `auto_confirm` tinyint(1) DEFAULT 0 COMMENT '자동 승인 여부',
#                               `approval_timeout` int(11) DEFAULT 60 COMMENT '관리자 승인 제한 시간 (분)',
#                               `special_booking_days` mediumtext DEFAULT NULL COMMENT '예약 가능 날짜 (JSON 형태)',
#                               `reservation_open_type` enum('ANYTIME','WEEKLY','MONTHLY') DEFAULT 'ANYTIME' COMMENT '예약 신청 유형',
#                               `reservation_open_days` text DEFAULT NULL COMMENT '예약 신청 가능 요일 또는 날짜(JSON Array)',
#                               `reservation_available_days` text DEFAULT NULL COMMENT '예약 신청일 이후 예약 가능한 최소 및 최대 날짜 간격 JSON 설정 {minDays:1,maxDays:7}',
#                               `deposit_amount` decimal(10,2) DEFAULT 0.00 COMMENT '선약금 (0일 경우 선약금 없음)',
#                               PRIMARY KEY (`restaurant_id`)
# );
#
# CREATE TABLE `restaurant_seat` (
#                                    `seat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '좌석 ID',
#                                    `restaurant_id` bigint(20) NOT NULL COMMENT '가게 ID',
#                                    `seat_type` enum('TABLE','BAR','PRIVATE') NOT NULL COMMENT '좌석 타입',
#                                    `seat_number` varchar(10) NOT NULL COMMENT '좌석 번호',
#                                    `max_capacity` int(11) NOT NULL COMMENT '최대 수용 인원',
#                                    `is_available` tinyint(1) DEFAULT 1 COMMENT '사용 가능 여부',
#                                    `created_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '생성일시',
#                                    `updated_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '수정일시',
#                                    `created_by` varchar(20) DEFAULT NULL COMMENT '생성자',
#                                    `updated_by` varchar(20) DEFAULT NULL COMMENT '수정자',
#                                    PRIMARY KEY (`seat_id`),
#                                    KEY `restaurant_id` (`restaurant_id`),
#                                    CONSTRAINT `restaurant_seat_ibfk_1` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`) ON DELETE CASCADE
# );
#
# CREATE TABLE `restaurant_operating_hours` (
#                                               `operating_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '영업시간 ID',
#                                               `restaurant_id` bigint(20) NOT NULL COMMENT '가게 ID',
#                                               `day_of_week` enum('MON','TUE','WED','THU','FRI','SAT','SUN') NOT NULL COMMENT '요일',
#                                               `open_time` time NOT NULL COMMENT '영업 시작 시간',
#                                               `close_time` time NOT NULL COMMENT '영업 종료 시간',
#                                               `is_holiday` tinyint(1) DEFAULT 0 COMMENT '휴무 여부',
#                                               `created_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '생성일시',
#                                               `updated_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '수정일시',
#                                               `created_by` varchar(20) DEFAULT NULL COMMENT '생성자',
#                                               `updated_by` varchar(20) DEFAULT NULL COMMENT '수정자',
#                                               PRIMARY KEY (`operating_id`),
#                                               KEY `restaurant_id` (`restaurant_id`),
#                                               CONSTRAINT `restaurant_operating_hours_ibfk_1` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`) ON DELETE CASCADE
# );
#
# CREATE TABLE `restaurant_menu` (
#                                    `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
#                                    `restaurant_id` bigint(20) NOT NULL,
#                                    `name` varchar(255) NOT NULL,
#                                    `description` mediumtext DEFAULT NULL,
#                                    `price` decimal(10,2) NOT NULL,
#                                    `is_best` tinyint(1) DEFAULT 0,
#                                    PRIMARY KEY (`menu_id`),
#                                    FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`) ON DELETE CASCADE
# );
#
# CREATE TABLE `restaurant_reservation` (
#                                           `reservation_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '예약 ID',
#                                           `restaurant_id` bigint(20) NOT NULL COMMENT '가게 ID',
#                                           `user_id` bigint(20) NOT NULL COMMENT '예약한 사용자 ID',
#                                           `seat_id` bigint(20) NOT NULL COMMENT '예약된 좌석 ID',
#                                           `reservation_date` date NOT NULL COMMENT '예약 날짜',
#                                           `reservation_time` time NOT NULL COMMENT '예약 시간',
#                                           `status` enum('PENDING','CONFIRMED','CANCELED') DEFAULT 'PENDING' COMMENT '예약 상태',
#                                           `reservation_at` datetime DEFAULT current_timestamp() COMMENT '예약 생성일시',
#                                           `created_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '생성일시',
#                                           `updated_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '수정일시',
#                                           `created_by` varchar(20) DEFAULT NULL COMMENT '생성자',
#                                           `updated_by` varchar(20) DEFAULT NULL COMMENT '수정자',
#                                           `expires_at` datetime DEFAULT NULL COMMENT '예약 승인 만료 시간',
#                                           `depositAmount` decimal(10,2) DEFAULT 0.00 COMMENT '선 예약금액',
#                                           `payment_key` varchar(50) DEFAULT NULL COMMENT '결제 키',
#                                           `order_id` varchar(50) DEFAULT NULL COMMENT '결제 오더 id',
#                                           `cancelled_at` datetime DEFAULT NULL COMMENT '취소일시',
#                                           `refund_amount` decimal(10,2) DEFAULT NULL COMMENT '환불금액',
#                                           PRIMARY KEY (`reservation_id`),
#                                           KEY `restaurant_id` (`restaurant_id`),
#                                           KEY `user_id` (`user_id`),
#                                           KEY `seat_id` (`seat_id`),
#                                           CONSTRAINT `restaurant_reservation_ibfk_1` FOREIGN KEY (`restaurant_id`) REFERENCES `restaurant` (`restaurant_id`) ON DELETE CASCADE,
#                                           CONSTRAINT `restaurant_reservation_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
#                                           CONSTRAINT `restaurant_reservation_ibfk_3` FOREIGN KEY (`seat_id`) REFERENCES `restaurant_seat` (`seat_id`) ON DELETE CASCADE
# );
#
# INSERT INTO roles (id ,name,code,`rank`,created_at,created_by,updated_at,updated_by) VALUES
#     ( 1, 'ADMIN','00',1,now(),NULL,now(),NULL),
#     (2,'USER','01',3,now(),NULL,now(),NULL),
#     (3,'STORE_OWNER','02',2,now(),NULL,now(),NULL),
#     (4,'GUEST','03',4,now(),NULL,now(),NULL);
#
#
# -- 일반 User 10명 (ID: 1~10)
# INSERT INTO users (user_id, user_name, password, role_id, created_at, updated_at)
# VALUES
#     (1, 'user1', 'password', '2', NOW(), NOW()),
#     (2, 'user2', 'password', '2', NOW(), NOW()),
#     (3, 'user3', 'password', '2', NOW(), NOW()),
#     (4, 'user4', 'password', '2', NOW(), NOW()),
#     (5, 'user5', 'password', '2', NOW(), NOW()),
#     (6, 'user6', 'password', '2', NOW(), NOW()),
#     (7, 'user7', 'password', '2', NOW(), NOW()),
#     (8, 'user8', 'password', '2', NOW(), NOW()),
#     (9, 'user9', 'password', '2', NOW(), NOW()),
#     (10, 'user10', 'password', '2', NOW(), NOW());
#
# -- 관리자 User (ID: 11)
# INSERT INTO users (user_id, user_name, password, role_id, created_at, updated_at)
# VALUES (11, 'admin_user', 'password', '1', NOW(), NOW());
#
# -- 가게 운영자 User (ID: 12)
# INSERT INTO users (user_id, user_name, password, role_id, created_at, updated_at)
# VALUES (12, 'owner_user', 'password', '3', NOW(), NOW());
#
#
#
# -- 레스토랑 (가게 운영자: ID 12)
# INSERT INTO restaurant (
#     restaurant_id, name, category, phone_number, address, status,
#     management_status, owner_id, auto_confirm, approval_timeout,
#     special_booking_days, reservation_open_type, reservation_open_days,
#     reservation_available_days, deposit_amount, created_by, updated_by
# )
# VALUES (
#            1, '테스트 레스토랑', '한식', '02-1234-5678', '서울시 강남구 테헤란로 123',
#            'APPROVED', 'OPEN', 12, 1, 60,
#            NULL, 'ANYTIME', '[]', '{"minDays":1,"maxDays":30}', 10000.00, 'owner_user', 'owner_user'
#        );
#
# -- 레스토랑 좌석
# INSERT INTO restaurant_seat (
#     seat_id, restaurant_id, seat_type, seat_number, max_capacity, is_available, created_by, updated_by
# )
# VALUES (
#            1, 1, 'TABLE', 'A1', 4, 1, 'owner_user', 'owner_user'
#        );
#
# -- 레스토랑 메뉴
# INSERT INTO restaurant_menu (
#     menu_id, restaurant_id, name, description, price,  is_best
# )
# VALUES (
#            1, 1, '대표 메뉴', '테스트용 대표 메뉴 설명', 15000.00,  1
#        );
#
# -- 레스토랑 운영 시간 (월~일: 10:00~22:00)
# INSERT INTO restaurant_operating_hours (
#     operating_id, restaurant_id, day_of_week, open_time, close_time, is_holiday, created_by, updated_by
# )
# VALUES (
#            1, 1, 'MON', '10:00:00', '22:00:00', 0, 'owner_user', 'owner_user'
#        );