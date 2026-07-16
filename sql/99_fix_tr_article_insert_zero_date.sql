USE `zhiblog`;

DROP TRIGGER IF EXISTS `tr_article_insert`;

DELIMITER $$

CREATE TRIGGER `tr_article_insert`
BEFORE INSERT ON `blog_article`
FOR EACH ROW
BEGIN
    DECLARE category_count_updated BOOLEAN DEFAULT FALSE;

    IF NEW.create_time IS NULL THEN
        SET NEW.create_time = NOW();
    END IF;
    SET NEW.update_time = NOW();

    IF @DISABLE_TRIGGERS IS NULL THEN
        IF NEW.category_id IS NOT NULL AND NEW.del_flag = '0' AND NEW.status = 1 THEN
            SET category_count_updated = update_category_article_count(NEW.category_id, 1);
        END IF;
    END IF;
END$$

DELIMITER ;
