-- Chạy Address và FullName trước
INSERT INTO `thue-truyen-mgmt`.`user`
    (`user_type`, `id`, `date_of_birth`, `email`, `login_code`, `phone`, `position`, `address_id`, `name_id`)
VALUES
    (1, 1, '1999-12-31', 'staff01@mail.com', 'NV00651', '0987652132', 'THU_NGAN', '21', '21'),
    (1, 2, '1999-12-31', 'staff02@mail.com', 'NV00652', '0985434155', 'THU_NGAN', '22', '22'),
    (1, 3, '1999-12-31', 'staff03@mail.com', 'NV00653', '0933765288', 'KHO', '23', '23'),
    (1, 4, '1999-12-31', 'staff04@mail.com', 'NV00654', '0967652187', 'KHO', '24', '24');
