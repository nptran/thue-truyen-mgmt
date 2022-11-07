USE `thue-truyen-mgmt`;
DROP procedure IF EXISTS `CLEAN_PENALTIES_OF_RENTED_BOOK`;

DELIMITER $$
USE `thue-truyen-mgmt`$$
CREATE PROCEDURE `CLEAN_PENALTIES_OF_RENTED_BOOK`(IN bookId int)
BEGIN
    DELETE FROM `thue-truyen-mgmt`.`rented_book_penalty` WHERE (`rented_book_id` = bookId);
    SELECT ROW_COUNT();
END$$

DELIMITER ;

