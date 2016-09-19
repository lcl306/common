DROP TABLE IF EXISTS `seq_id`;
CREATE TABLE `seq_id`(
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
   PRIMARY KEY(`id`)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

DROP TABLE IF EXISTS seq_no;  
CREATE TABLE seq_no (  
         name VARCHAR(10) NOT NULL,  
         curr_value INT NOT NULL,  
         increment INT NOT NULL DEFAULT 1,
     curr_no VARCHAR(10),
         PRIMARY KEY (name)  
) ENGINE=InnoDB;  

DELIMITER $$ 
DROP FUNCTION IF EXISTS `currval` $$
CREATE FUNCTION `currval`(seq_name VARCHAR(50))
RETURNS varchar(12)
DETERMINISTIC 
BEGIN 
   DECLARE value varchar(12) DEFAULT '';
   IF LOCATE('no',seq_name)>0 THEN
   SELECT curr_no INTO value FROM seq_no WHERE name = seq_name;  
  ELSE
      SELECT id INTO value FROM `seq_id` WHERE name = seq_name;
  END IF;
   RETURN value;
END $$ 
DELIMITER

DELIMITER $$
DROP FUNCTION IF EXISTS `nextval` $$
CREATE FUNCTION `nextval`(seq_name VARCHAR(10))
RETURNS varchar(12)
DETERMINISTIC
BEGIN
  DECLARE `value` INTEGER;
   IF LOCATE('no', seq_name)>0 THEN
   SELECT count(*) INTO `value` FROM seq_no WHERE name = seq_name;
      IF `value`=0 THEN
    INSERT INTO seq_no VALUES (seq_name, 0, 1, '');
   END IF;
   UPDATE seq_no SET curr_value = curr_value+increment, curr_no = LPAD(curr_value,10,'0') WHERE name = seq_name;
  ELSE
      DELETE FROM `seq_id` WHERE name=seq_name;
      INSERT INTO `seq_id`(name) VALUES(seq_name);
  END IF;
   RETURN currval(seq_name);
END $$
DELIMITER