DELETE FROM `thue-truyen-mgmt`.`full_name` WHERE TRUE;

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
    (24, 'Hà', 'Nguyễn Văn')
    ;
