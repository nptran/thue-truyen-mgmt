CREATE DEFINER=`root`@`localhost` PROCEDURE `SEARCH_CUSTOMER_BY_NAME`(IN kw_name varchar(255))
BEGIN
SELECT c.*, n.first_name, n.last_name FROM `thue-truyen-mgmt`.customer AS c
                                               LEFT JOIN `thue-truyen-mgmt`.full_name AS n
                                                         ON c.name_id = n.id
WHERE n.first_name LIKE CONCAT("%",kw_name,"%")
   OR n.last_name LIKE CONCAT("%",kw_name,"%")
   OR CONCAT(n.first_name, n.last_name) LIKE CONCAT("%",kw_name,"%");
END