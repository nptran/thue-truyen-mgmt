INSERT INTO `thue-truyen-mgmt`.`penalty`
    (`id`, `description`, `name`, `recommended_fee`)
VALUES (1, 'Trang sách bị nhăn hoặc có vết gấp', 'Nhăn Sách', 500),
       (2, 'Trang sách bị rách nhỏ không ảnh hưởng đến nội dung', 'Rách Nhỏ', 1000),
       (3, 'Trang sách bị rách ảnh hưởng đến nội dung nhưng không quá 30%', 'Rách Lớn', 2000),
       (4, 'Trang sách bị rách quá 50% hoặc toàn bộ', 'Rách Hoàn Toàn', 5000),
       (5, 'Trang sách bị dây bẩn nhưng không ảnh hưởng đến nội dung', 'Bẩn Ít', 500),
       (6, 'Trang sách bị dây bẩn ảnh hưởng đến không quá 20% nội dung', 'Bẩn Nhẹ', 1000),
       (7, 'Trang sách bị dây bẩn ảnh hưởng quá 50% nội dung', 'Bẩn Vừa', 4000),
       (8, 'Trang sách bị dây bẩn ảnh hưởng quá 80% nội dung', 'Bẩn Nhiều', 4000),
       (9, 'Trang sách bị dây bẩn ảnh hưởng quá 100% nội dung', 'Bẩn Toàn Bộ', 4000),
       (10, 'Bìa sách bị rách', 'Rách Bìa', 1000);
