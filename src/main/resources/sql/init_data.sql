DELETE FROM `thue-truyen-mgmt`.`full_name` WHERE TRUE;
DELETE FROM `thue-truyen-mgmt`.`address` WHERE TRUE;
DELETE FROM `thue-truyen-mgmt`.`publisher` WHERE TRUE;
DELETE FROM `thue-truyen-mgmt`.`book_title` WHERE TRUE;
DELETE FROM `thue-truyen-mgmt`.`customer` WHERE TRUE;
DELETE FROM `thue-truyen-mgmt`.`user` WHERE TRUE;
DELETE FROM `thue-truyen-mgmt`.`penalty` WHERE TRUE;
DELETE FROM `thue-truyen-mgmt`.`rented_book` WHERE TRUE;

-- ADDRESS
INSERT INTO `thue-truyen-mgmt`.`address` (`id`, `city`, `district`, `street`)
VALUES
    -- Customer's address
    (1, 'Hà Nội', 'Hà Đông', '1B Trần Phú'),
    (2, 'Hà Nội', 'Hà Đông', '101 Trần Phú'),
    (3, 'Hà Nội', 'Hà Đông', '98 Mỗ Lao'),
    (4, 'Hà Nội', 'Hà Đông', '72 Tô Hiệu'),
    (5, 'Hà Nội', 'Hai Bà Trưng', 'Trần Đông'),
    (6, 'Hà Nội', 'Cầu Giấy', '10 Xuân Thuỷ'),
    (7, 'Hà Nội', 'Cầu Giấy', '31 Đào Tấn'),
    (8, 'Hà Nội', 'Đống Đa', '22 Tây Sơn'),
    (9, 'Hà Nội', 'Thanh Xuân', '221 Nguyễn Trãi'),
    (10, 'Hà Nội', 'Thanh Xuân', 'An Phú'),

    -- Publisher's address
    (11, 'Bắc Ninh', 'Bắc Ninh', '22 Hải An Đông'),
    (12, 'Hải Phòng', 'An Dương', '109 Trần Phú'),
    (13, 'Hồ Chí Minh', 'Tân Phú', '99A KCN Bắc Bình'),
    (14, 'Đà Nẵng', 'Ngũ Hành Sơn', '669C Lê Trọng Tấn'),

    -- Staff's address
    (21, 'Hà Nội', 'Hà Đông', '1B Trần Phú'),
    (22, 'Hà Nội', 'Long Biên', '941 Thừa Thiên'),
    (23, 'Hà Nội', 'Thanh Xuân', '221 Láng'),
    (24, 'Hà Nội', 'Đống Đa', '202 Xuân Quan');

-- FULL NAME
INSERT INTO `thue-truyen-mgmt`.`full_name` (`id`, `first_name`, `last_name`)
VALUES
    -- Customer's name
    (1, 'Minh', 'Trần Văn'),
    (2, 'Minh', 'Trần Văn'),
    (3, 'Minh', 'Nguyễn Văn'),
    (4, 'Toàn', 'Nguyễn Thị'),
    (5, 'Phương', 'Phạm Thị'),
    (6, 'Phương', 'Trần Ái'),
    (7, 'Quân', 'Phạm Văn'),
    (8, 'Quang', 'Phạm Xuân'),
    (9, 'Bách', 'Đoàn Công'),
    (10, 'Chương', 'Đào Mỹ'),

    -- Staff's name
    (21, 'Yến', 'Phạm Hải'),
    (22, 'Hiếu', 'Đặng Văn'),
    (23, 'Ling', 'Đào Mỹ'),
    (24, 'Hà', 'Nguyễn Văn');

-- PUBLISHER
INSERT INTO `thue-truyen-mgmt`.`publisher`
(`id`, `code`, `description`, `email`, `name`, `phone`, `address_id`)
VALUES
    (1, 'P001', 'Nhà Xuất Bản', 'publisher01@mail.com', 'NXB Nhã Bắc', '089786567', 11),
    (2, 'P002', 'Nhà Xuất Bản', 'publisher02@mail.com', 'NXB Đoàn Kỳ', '089786567', 12),
    (3, 'P003', 'Nhà Xuất Bản', 'publisher03@mail.com', 'NXB Anh Hải', '089786567', 13),
    (4, 'P004', 'Nhà Cung Cấp', 'publisher04@mail.com', 'NCC Kỳ Xuân', '089786567', 14);

-- BOOK TITLE
INSERT INTO `thue-truyen-mgmt`.`book_title`
(`id`,`author`, `code`, `price`, `quantity`, `title_name`, `year`, `publisher_id`)
VALUES (11, 'Phạm Ánh Nhật', '1232435467EE', '50000', 99, 'Hoa Vàng', '2019', '1'),
       (12, 'JK. Rowling', '1223756434AA', '120000', 99, 'Harry Potter Tập 1', '2006', 1),
       (13, 'JK. Rowling', 'A34656UUH656', '120000', 99, 'Harry Potter Tập 2', '2002', 1),
       (14, 'Nhiều Tác Giả', 'H4YT84022222', '200000', 99, 'Bách Khoa Toàn Thư', '2010', 1),
       (21, 'Fujiko F.Fujio', 'HF383053FJ99', '20000', 99, 'Doraemon 1', '2010', 2),
       (22, 'Fujiko F.Fujio', 'FYH4834932UR', '20000', 99, 'Doraemon 2', '2010', 2),
       (23, 'Fujiko F.Fujio', '385493URF902', '20000', 99, 'Doraemon 3', '2010', 2),
       (24, 'Fujiko F.Fujio', 'HGV832U93EFJ', '20000', 99, 'Doraemon 4', '2010', 2),
       (31, 'Phạm Vũ', 'U5843EF90D32', '15000', 99, 'Quán Cóc', '2010', 3),
       (32, 'Trần Thế Ký', 'JF43T90923F3', '30000', 99, 'Ước mơ nhỏ', '2010', 3),
       (33, 'Tô Hoài', 'FF923U9TJVE3', '50000', 99, 'Dế Choắt Phiêu Lưu Ký', '2010', 3),
       (34, 'Nam Cao', '325UF89219ER', '120000', 99, 'Tuyển tập Nam Cao', '2010', 3),
       (41, 'Test', 'U359UF90329R', '10000', 99, 'Truyện 1', '2022', 4),
       (42, 'Test', '3I950RF9292F', '10000', 99, 'Truyện 2', '2022', 4),
       (43, 'Test', '2935I9F90DFW', '10000', 99, 'Truyện 3', '2022', 4),
       (44, 'Test', '32950UF90000', '10000', 99, 'Truyện 4', '2022', 4);

-- STAFF
INSERT INTO `thue-truyen-mgmt`.`user`
(`user_type`, `id`, `date_of_birth`, `email`, `login_code`, `phone`, `position`, `address_id`, `name_id`)
VALUES
    (1, 1, '1999-12-31', 'staff01@mail.com', 'NV00651', '0987652132', 'THU_NGAN', '21', '21'),
    (1, 2, '1999-12-31', 'staff02@mail.com', 'NV00652', '0985434155', 'THU_NGAN', '22', '22'),
    (1, 3, '1999-12-31', 'staff03@mail.com', 'NV00653', '0933765288', 'KHO', '23', '23'),
    (1, 4, '1999-12-31', 'staff04@mail.com', 'NV00654', '0967652187', 'KHO', '24', '24');

-- PENALTY
INSERT INTO `thue-truyen-mgmt`.`penalty`
(`id`, `description`, `name`, `recommended_fee`)
VALUES (1, 'Trang sách bị nhăn hoặc có vết gấp', 'Nhăn Sách', 500),
       (2, 'Trang sách bị rách nhỏ không ảnh hưởng đến nội dung', 'Rách Nhỏ', 1000),
       (3, 'Trang sách bị rách ảnh hưởng đến nội dung nhưng không quá 30%', 'Rách Lớn', 2000),
       (4, 'Trang sách bị rách quá 50% hoặc toàn bộ', 'Rách Hoàn Toàn', 5000),
       (5, 'Trang sách bị dây bẩn nhưng không ảnh hưởng đến nội dung', 'Bẩn Nhẹ', 500),
       (6, 'Trang sách bị dây bẩn ảnh hưởng đến không quá 20% nội dung', 'Bẩn Nặng', 1000),
       (7, 'Trách sách bị dây bẩn ảnh hưởng quá 50% nội dung', 'Bẩn Toàn Bộ', 4000),
       (8, 'Trả Chậm/Ngày', 'Trả Chậm', 1000);

-- CUSTOMER
INSERT INTO `thue-truyen-mgmt`.`customer` (`cccd`, `date_of_birth`, `email`, `phone`, `address_id`, `name_id`)
VALUES
    ('123832954', '1992-12-31', 'customer01@mail.com', '04329025353', '1', '1'),
    ('543654734', '1992-12-31', 'customer02@mail.com', '04329025353', '2', '2'),
    ('098765475', '1992-12-31', 'customer03@mail.com', '04329025353', '3', '3'),
    ('195645645', '1992-12-31', 'customer04@mail.com', '04329025353', '4', '4'),
    ('243567556', '1992-12-31', 'customer05@mail.com', '04329025353', '5', '5'),
    ('876068746', '1992-12-31', 'customer06@mail.com', '04329025353', '6', '6'),
    ('987654577', '1992-12-31', 'customer07@mail.com', '04329025353', '7', '7'),
    ('214356578', '1992-12-31', 'customer08@mail.com', '04329025353', '8', '8'),
    ('253678658', '1992-12-31', 'customer09@mail.com', '04329025353', '9', '9'),
    ('124534657', '1992-12-31', 'customer10@mail.com', '04329025353', '10', '10');

-- RENTED BOOK
INSERT INTO `thue-truyen-mgmt`.`rented_book`
(`id`, `amount`, `is_paid`, `rented_time`, `book_title_id`, `customer_id`)
VALUES (11, 5000, 0, '2022-11-01 14:15:00', 11, 1),
       (12, 5000, 0, '2022-11-01 14:15:00', 12, 1),
       (13, 6000, 0, '2022-11-02 16:00:00', 21, 1),
       (14, 3000, 0, '2022-11-02 16:00:00', 22, 1),
       (15, 7000, 0, '2022-11-03 08:00:00', 23, 1),
       (21, 3000, 0, '2022-11-01 10:15:00', 11, 2),
       (22, 2000, 0, '2022-11-01 10:15:00', 12, 2),
       (23, 4000, 1, '2022-11-01 10:15:00', 23, 2),
       (24, 4000, 1, '2022-11-01 10:15:00', 33, 2),
       (31, 5000, 1, '2022-11-01 12:15:00', 11, 3),
       (32, 5000, 1, '2022-11-01 12:15:00', 12, 3),
       (33, 5000, 1, '2022-11-01 12:15:00', 13, 3);
