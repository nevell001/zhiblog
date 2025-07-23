-- MySQL dump 10.13  Distrib 8.4.5, for macos15.2 (arm64)
--
-- Host: localhost    Database: newblog
-- ------------------------------------------------------
-- Server version	8.4.5

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `blog_article`
--

DROP TABLE IF EXISTS `blog_article`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_article` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `title` varchar(255) NOT NULL COMMENT '文章标题',
  `summary` varchar(512) DEFAULT NULL COMMENT '摘要',
  `content` longtext NOT NULL COMMENT '文章内容',
  `cover_url` varchar(512) DEFAULT NULL COMMENT '封面图片',
  `category_id` bigint DEFAULT NULL COMMENT '分类ID',
  `author_id` bigint NOT NULL COMMENT '作者ID（关联sys_user）',
  `is_top` tinyint DEFAULT '0' COMMENT '是否置顶 0否 1是',
  `is_recommend` tinyint DEFAULT '0' COMMENT '是否推荐 0否 1是',
  `status` tinyint DEFAULT '1' COMMENT '状态 0草稿 1发布',
  `view_count` int DEFAULT '0' COMMENT '浏览量',
  `like_count` int DEFAULT '0' COMMENT '点赞数',
  `comment_count` int DEFAULT '0' COMMENT '评论数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint DEFAULT '0' COMMENT '删除标志 0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_title` (`title`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客文章表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_article`
--

LOCK TABLES `blog_article` WRITE;
/*!40000 ALTER TABLE `blog_article` DISABLE KEYS */;
INSERT INTO `blog_article` VALUES (19,'vivo',NULL,'<p><strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。</p>',NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-18 21:21:03','2025-07-18 23:35:07',NULL),(20,'美国解禁h20芯片深层原因 白宫ai主管：打压华为',NULL,'<p><strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。</p>',NULL,1,1,1,NULL,NULL,NULL,NULL,NULL,'2025-07-18 21:23:06','2025-07-18 23:40:34',NULL),(21,'vivo和oppo紧随其后，排在第4位的是小米',NULL,'<p><strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。</p>',NULL,2,1,NULL,1,NULL,NULL,NULL,NULL,'2025-07-18 21:39:03','2025-07-18 23:32:04',NULL),(22,'vivo和OPPO紧随其后排在第4位的是小米',NULL,'<p>鑫</p>',NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-18 21:39:44','2025-07-18 22:02:35',NULL),(23,'hello你们好',NULL,'<p>磊在在在在</p>',NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-18 21:50:30','2025-07-18 22:59:57',NULL),(24,'vivo和oppo紧随其后，排在第4位的',NULL,'<p>vivo和OPPO紧随其后，排在第4位的是小米</p>',NULL,3,1,NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-18 23:03:15','2025-07-18 23:32:17',NULL),(25,'vivo和oppo紧随其后，4位的是小米',NULL,'<p>vivo和OPPO紧随其后，排在第4位的是小米</p>',NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-18 23:03:44',NULL,NULL),(26,'vivo vivo',NULL,'<p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">购车的陈女士介绍，她是被人介绍来这家门店买车的，她签订的购车合同显示，</span><strong style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">一周后提车，但到了交车日期，这家4S店一拖再拖，迟迟无法轿车。</strong></p><p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">另有其他车主表示，她是早早拿到了车，却因为门店负责人将合格证与购车发票抵押给了债主，导致无法上牌。</span></p><p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">她们把情况投诉给奇瑞的400客服后，却得到相同的回答，查无此店，无法提供帮助。</span></p><p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">媒体来到涉事门店发现，这家所谓的“奇瑞4S店”，门头悬挂的却是正规的奇瑞logo，无论是门头布置还是展厅设计，都让人感觉是一家正规的品牌4S店。</span></p><p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">而该店的销售员工表示：</span><strong style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">该店为厂家店，厂家直营，还可以送售后服务。</strong></p><p><br></p>',NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-18 23:04:42',NULL,NULL),(27,'我们再次把不可能变为可能',NULL,'<p><strong style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">发布会结束后，贾总一如既往的煽情</strong><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">：“洛杉矶DTLA 7.17，California sunset映照天际线，FX Super One全球公众产品发布会成功，Super EAI F.A.C.E.也终于揭幕与你们见面，且正式开启C端预定。</span></p><p><span style=\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\">今晚最遗憾的是没能送可儿乐儿回家，爸爸又一次缺席了，但我知道，你们会一如既往的理解和支持爸爸。追梦者的新征程新起点，我们再次把不可能变为可能”。</span></p><p><br></p>',NULL,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,'2025-07-18 23:41:38',NULL,NULL);
/*!40000 ALTER TABLE `blog_article` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_article_tag`
--

DROP TABLE IF EXISTS `blog_article_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_article_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `tag_id` bigint NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_article_tag` (`article_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='文章与标签关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_article_tag`
--

LOCK TABLES `blog_article_tag` WRITE;
/*!40000 ALTER TABLE `blog_article_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog_article_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_category`
--

DROP TABLE IF EXISTS `blog_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(64) NOT NULL COMMENT '分类名称',
  `sort` int DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint DEFAULT '0' COMMENT '删除标志 0正常 1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_category`
--

LOCK TABLES `blog_category` WRITE;
/*!40000 ALTER TABLE `blog_category` DISABLE KEYS */;
INSERT INTO `blog_category` VALUES (1,'技术',0,'2025-07-18 19:45:51','2025-07-18 19:45:51',0),(2,'生活',0,'2025-07-18 19:45:51','2025-07-18 19:45:51',0),(3,'随笔',0,'2025-07-18 19:45:51','2025-07-18 19:45:51',0);
/*!40000 ALTER TABLE `blog_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_comment`
--

DROP TABLE IF EXISTS `blog_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_comment` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` bigint NOT NULL COMMENT '文章ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID（可为空，匿名评论）',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称（匿名评论用）',
  `email` varchar(128) DEFAULT NULL COMMENT '邮箱（匿名评论用）',
  `content` text NOT NULL COMMENT '评论内容',
  `parent_id` bigint DEFAULT '0' COMMENT '父评论ID',
  `status` tinyint DEFAULT '1' COMMENT '状态 0待审核 1正常 2已删除',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `ip` varchar(64) DEFAULT NULL COMMENT '评论IP',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客评论表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_comment`
--

LOCK TABLES `blog_comment` WRITE;
/*!40000 ALTER TABLE `blog_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_friend_link`
--

DROP TABLE IF EXISTS `blog_friend_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_friend_link` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(64) NOT NULL COMMENT '友链名称',
  `url` varchar(255) NOT NULL COMMENT '友链地址',
  `logo` varchar(255) DEFAULT NULL COMMENT '友链Logo',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` tinyint DEFAULT '1' COMMENT '状态 0禁用 1启用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客友链表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_friend_link`
--

LOCK TABLES `blog_friend_link` WRITE;
/*!40000 ALTER TABLE `blog_friend_link` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog_friend_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_setting`
--

DROP TABLE IF EXISTS `blog_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_setting` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_key` varchar(64) NOT NULL COMMENT '配置项Key',
  `config_value` varchar(1024) DEFAULT NULL COMMENT '配置项Value',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客系统设置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_setting`
--

LOCK TABLES `blog_setting` WRITE;
/*!40000 ALTER TABLE `blog_setting` DISABLE KEYS */;
/*!40000 ALTER TABLE `blog_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blog_tag`
--

DROP TABLE IF EXISTS `blog_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blog_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(64) NOT NULL COMMENT '标签名称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` tinyint DEFAULT '0' COMMENT '删除标志 0正常 1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='博客标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blog_tag`
--

LOCK TABLES `blog_tag` WRITE;
/*!40000 ALTER TABLE `blog_tag` DISABLE KEYS */;
INSERT INTO `blog_tag` VALUES (1,'Java','2025-07-18 19:45:51','2025-07-18 19:45:51',0),(2,'前端','2025-07-18 19:45:51','2025-07-18 19:45:51',0),(3,'数据库','2025-07-18 19:45:51','2025-07-18 19:45:51',0);
/*!40000 ALTER TABLE `blog_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table`
--

DROP TABLE IF EXISTS `gen_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table` (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table`
--

LOCK TABLES `gen_table` WRITE;
/*!40000 ALTER TABLE `gen_table` DISABLE KEYS */;
INSERT INTO `gen_table` VALUES (1,'blog_article','博客文章表',NULL,NULL,'BlogArticle','crud','element-ui','com.ruoyi.system','system','blog_article','博客文章','nevell','0','/','{\"parentMenuId\":0}','admin','2025-07-18 19:47:38','','2025-07-18 23:15:22',NULL),(2,'blog_article_tag','文章与标签关联表',NULL,NULL,'BlogArticleTag','crud','','com.ruoyi.system','system','tag','文章与标签关联','ruoyi','0','/',NULL,'admin','2025-07-18 19:47:38','',NULL,NULL),(3,'blog_category','文章分类表',NULL,NULL,'BlogCategory','crud','element-ui','com.ruoyi.system','system','category','文章分类','nevell','0','/','{}','admin','2025-07-18 19:47:38','','2025-07-18 23:16:57',NULL),(4,'blog_comment','博客评论表',NULL,NULL,'BlogComment','crud','','com.ruoyi.system','system','comment','博客评论','ruoyi','0','/',NULL,'admin','2025-07-18 19:47:38','',NULL,NULL),(5,'blog_friend_link','博客友链表',NULL,NULL,'BlogFriendLink','crud','','com.ruoyi.system','system','link','博客友链','ruoyi','0','/',NULL,'admin','2025-07-18 19:47:38','',NULL,NULL),(6,'blog_setting','博客系统设置表',NULL,NULL,'BlogSetting','crud','element-plus','com.ruoyi.system','system','setting','博客系统设置','ruoyi','1','/','{}','admin','2025-07-18 19:47:38','','2025-07-18 19:51:09',NULL),(7,'blog_tag','博客标签表',NULL,NULL,'BlogTag','crud','','com.ruoyi.system','system','tag','博客标签','ruoyi','0','/',NULL,'admin','2025-07-18 19:47:38','',NULL,NULL);
/*!40000 ALTER TABLE `gen_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table_column`
--

DROP TABLE IF EXISTS `gen_table_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table_column` (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table_column`
--

LOCK TABLES `gen_table_column` WRITE;
/*!40000 ALTER TABLE `gen_table_column` DISABLE KEYS */;
INSERT INTO `gen_table_column` VALUES (1,1,'id','主键ID','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:25'),(2,1,'title','文章标题','varchar(255)','String','title','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:25'),(3,1,'summary','摘要','varchar(512)','String','summary','0','0','0','1','1','1','1','EQ','textarea','',3,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:25'),(4,1,'content','文章内容','longtext','String','content','0','0','1','1','1','1','1','EQ','editor','',4,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(5,1,'cover_url','封面图片','varchar(512)','String','coverUrl','0','0','0','1','1','1','1','EQ','textarea','',5,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(6,1,'category_id','分类ID','bigint','Long','categoryId','0','0','0','1','1','1','1','EQ','input','',6,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(7,1,'author_id','作者ID（关联sys_user）','bigint','Long','authorId','0','0','0','1','1','1','1','EQ','input','',7,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(8,1,'is_top','是否置顶 0否 1是','tinyint','Long','isTop','0','0','0','1','1','1','1','EQ','input','',8,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(9,1,'is_recommend','是否推荐 0否 1是','tinyint','Long','isRecommend','0','0','0','1','1','1','1','EQ','input','',9,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(10,1,'status','状态 0草稿 1发布','tinyint','Long','status','0','0','0','1','1','1','1','EQ','radio','',10,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(11,1,'view_count','浏览量','int','Long','viewCount','0','0','0','1','1','1','1','EQ','input','',11,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(12,1,'like_count','点赞数','int','Long','likeCount','0','0','0','1','1','1','1','EQ','input','',12,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(13,1,'comment_count','评论数','int','Long','commentCount','0','0','0','1','1','1','1','EQ','input','',13,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(14,1,'create_time','创建时间','datetime','Date','createTime','0','0','0','1',NULL,NULL,NULL,'EQ','datetime','',14,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(15,1,'update_time','更新时间','datetime','Date','updateTime','0','0','0','1','1',NULL,NULL,'EQ','datetime','',15,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(16,1,'del_flag','删除标志 0正常 1删除','tinyint','Long','delFlag','0','0','0','1',NULL,NULL,NULL,'EQ','input','',16,'admin','2025-07-18 19:47:38','','2025-07-18 23:15:26'),(17,2,'id','主键ID','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:18'),(18,2,'article_id','文章ID','bigint','Long','articleId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:18'),(19,2,'tag_id','标签ID','bigint','Long','tagId','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:18'),(20,3,'id','主键ID','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-07-18 19:47:38','','2025-07-18 23:17:03'),(21,3,'name','分类名称','varchar(64)','String','name','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2025-07-18 19:47:38','','2025-07-18 23:17:03'),(22,3,'sort','排序','int','Long','sort','0','0','0','1','1','1','1','EQ','input','',3,'admin','2025-07-18 19:47:38','','2025-07-18 23:17:03'),(23,3,'create_time','创建时间','datetime','Date','createTime','0','0','0','1',NULL,NULL,NULL,'EQ','datetime','',4,'admin','2025-07-18 19:47:38','','2025-07-18 23:17:03'),(24,3,'update_time','更新时间','datetime','Date','updateTime','0','0','0','1','1',NULL,NULL,'EQ','datetime','',5,'admin','2025-07-18 19:47:38','','2025-07-18 23:17:03'),(25,3,'del_flag','删除标志 0正常 1删除','tinyint','Long','delFlag','0','0','0','1',NULL,NULL,NULL,'EQ','input','',6,'admin','2025-07-18 19:47:38','','2025-07-18 23:17:03'),(26,4,'id','主键ID','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:23'),(27,4,'article_id','文章ID','bigint','Long','articleId','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:23'),(28,4,'user_id','用户ID（可为空，匿名评论）','bigint','Long','userId','0','0','0','1','1','1','1','EQ','input','',3,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:23'),(29,4,'nickname','昵称（匿名评论用）','varchar(64)','String','nickname','0','0','0','1','1','1','1','LIKE','input','',4,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:23'),(30,4,'email','邮箱（匿名评论用）','varchar(128)','String','email','0','0','0','1','1','1','1','EQ','input','',5,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:23'),(31,4,'content','评论内容','text','String','content','0','0','1','1','1','1','1','EQ','editor','',6,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:23'),(32,4,'parent_id','父评论ID','bigint','Long','parentId','0','0','0','1','1','1','1','EQ','input','',7,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:23'),(33,4,'status','状态 0待审核 1正常 2已删除','tinyint','Long','status','0','0','0','1','1','1','1','EQ','radio','',8,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:23'),(34,4,'create_time','创建时间','datetime','Date','createTime','0','0','0','1',NULL,NULL,NULL,'EQ','datetime','',9,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:23'),(35,4,'ip','评论IP','varchar(64)','String','ip','0','0','0','1','1','1','1','EQ','input','',10,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:23'),(36,5,'id','主键ID','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:26'),(37,5,'name','友链名称','varchar(64)','String','name','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:26'),(38,5,'url','友链地址','varchar(255)','String','url','0','0','1','1','1','1','1','EQ','input','',3,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:26'),(39,5,'logo','友链Logo','varchar(255)','String','logo','0','0','0','1','1','1','1','EQ','input','',4,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:26'),(40,5,'description','描述','varchar(255)','String','description','0','0','0','1','1','1','1','EQ','input','',5,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:26'),(41,5,'status','状态 0禁用 1启用','tinyint','Long','status','0','0','0','1','1','1','1','EQ','radio','',6,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:26'),(42,5,'create_time','创建时间','datetime','Date','createTime','0','0','0','1',NULL,NULL,NULL,'EQ','datetime','',7,'admin','2025-07-18 19:47:38','','2025-07-18 19:50:26'),(43,6,'id','主键ID','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-07-18 19:47:38','','2025-07-18 19:51:09'),(44,6,'config_key','配置项Key','varchar(64)','String','configKey','0','0','1','1','1','1','1','EQ','input','',2,'admin','2025-07-18 19:47:38','','2025-07-18 19:51:09'),(45,6,'config_value','配置项Value','varchar(1024)','String','configValue','0','0','0','1','1','1','1','EQ','textarea','',3,'admin','2025-07-18 19:47:38','','2025-07-18 19:51:09'),(46,6,'description','描述','varchar(255)','String','description','0','0','0','1','1','1','1','EQ','input','',4,'admin','2025-07-18 19:47:38','','2025-07-18 19:51:09'),(47,6,'create_time','创建时间','datetime','Date','createTime','0','0','0','1',NULL,NULL,NULL,'EQ','datetime','',5,'admin','2025-07-18 19:47:38','','2025-07-18 19:51:09'),(48,6,'update_time','更新时间','datetime','Date','updateTime','0','0','0','1','1',NULL,NULL,'EQ','datetime','',6,'admin','2025-07-18 19:47:38','','2025-07-18 19:51:09'),(49,7,'id','主键ID','bigint','Long','id','1','1','0','1',NULL,NULL,NULL,'EQ','input','',1,'admin','2025-07-18 19:47:39','',NULL),(50,7,'name','标签名称','varchar(64)','String','name','0','0','1','1','1','1','1','LIKE','input','',2,'admin','2025-07-18 19:47:39','',NULL),(51,7,'create_time','创建时间','datetime','Date','createTime','0','0','0','1',NULL,NULL,NULL,'EQ','datetime','',3,'admin','2025-07-18 19:47:39','',NULL),(52,7,'update_time','更新时间','datetime','Date','updateTime','0','0','0','1','1',NULL,NULL,'EQ','datetime','',4,'admin','2025-07-18 19:47:39','',NULL),(53,7,'del_flag','删除标志 0正常 1删除','tinyint','Long','delFlag','0','0','0','1',NULL,NULL,NULL,'EQ','input','',5,'admin','2025-07-18 19:47:39','',NULL);
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_BLOB_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_BLOB_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_BLOB_TRIGGERS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Blob类型的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_BLOB_TRIGGERS`
--

LOCK TABLES `QRTZ_BLOB_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_BLOB_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_BLOB_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_CALENDARS`
--

DROP TABLE IF EXISTS `QRTZ_CALENDARS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_CALENDARS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='日历信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_CALENDARS`
--

LOCK TABLES `QRTZ_CALENDARS` WRITE;
/*!40000 ALTER TABLE `QRTZ_CALENDARS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_CALENDARS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_CRON_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_CRON_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_CRON_TRIGGERS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Cron类型的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_CRON_TRIGGERS`
--

LOCK TABLES `QRTZ_CRON_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_CRON_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_CRON_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_FIRED_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_FIRED_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_FIRED_TRIGGERS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint NOT NULL COMMENT '触发的时间',
  `sched_time` bigint NOT NULL COMMENT '定时器制定的时间',
  `priority` int NOT NULL COMMENT '优先级',
  `state` varchar(16) NOT NULL COMMENT '状态',
  `job_name` varchar(200) DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='已触发的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_FIRED_TRIGGERS`
--

LOCK TABLES `QRTZ_FIRED_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_FIRED_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_FIRED_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_JOB_DETAILS`
--

DROP TABLE IF EXISTS `QRTZ_JOB_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_JOB_DETAILS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) NOT NULL COMMENT '任务组名',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_JOB_DETAILS`
--

LOCK TABLES `QRTZ_JOB_DETAILS` WRITE;
/*!40000 ALTER TABLE `QRTZ_JOB_DETAILS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_JOB_DETAILS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_LOCKS`
--

DROP TABLE IF EXISTS `QRTZ_LOCKS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_LOCKS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='存储的悲观锁信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_LOCKS`
--

LOCK TABLES `QRTZ_LOCKS` WRITE;
/*!40000 ALTER TABLE `QRTZ_LOCKS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_LOCKS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

DROP TABLE IF EXISTS `QRTZ_PAUSED_TRIGGER_GRPS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_PAUSED_TRIGGER_GRPS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='暂停的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_PAUSED_TRIGGER_GRPS`
--

LOCK TABLES `QRTZ_PAUSED_TRIGGER_GRPS` WRITE;
/*!40000 ALTER TABLE `QRTZ_PAUSED_TRIGGER_GRPS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_PAUSED_TRIGGER_GRPS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SCHEDULER_STATE`
--

DROP TABLE IF EXISTS `QRTZ_SCHEDULER_STATE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_SCHEDULER_STATE` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='调度器状态表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SCHEDULER_STATE`
--

LOCK TABLES `QRTZ_SCHEDULER_STATE` WRITE;
/*!40000 ALTER TABLE `QRTZ_SCHEDULER_STATE` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_SCHEDULER_STATE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SIMPLE_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPLE_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_SIMPLE_TRIGGERS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='简单触发器的信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SIMPLE_TRIGGERS`
--

LOCK TABLES `QRTZ_SIMPLE_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_SIMPLE_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_SIMPLE_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_SIMPROP_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_SIMPROP_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_SIMPROP_TRIGGERS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `QRTZ_TRIGGERS` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='同步机制的行锁表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_SIMPROP_TRIGGERS`
--

LOCK TABLES `QRTZ_SIMPROP_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_SIMPROP_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_SIMPROP_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `QRTZ_TRIGGERS`
--

DROP TABLE IF EXISTS `QRTZ_TRIGGERS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `QRTZ_TRIGGERS` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) NOT NULL COMMENT '触发器的类型',
  `start_time` bigint NOT NULL COMMENT '开始时间',
  `end_time` bigint DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `QRTZ_JOB_DETAILS` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='触发器详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `QRTZ_TRIGGERS`
--

LOCK TABLES `QRTZ_TRIGGERS` WRITE;
/*!40000 ALTER TABLE `QRTZ_TRIGGERS` DISABLE KEYS */;
/*!40000 ALTER TABLE `QRTZ_TRIGGERS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='参数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2025-07-18 18:58:46','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2025-07-18 18:58:46','',NULL,'初始化密码 123456'),(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2025-07-18 18:58:46','',NULL,'深色主题theme-dark，浅色主题theme-light'),(4,'账号自助-验证码开关','sys.account.captchaEnabled','true','Y','admin','2025-07-18 18:58:46','',NULL,'是否开启验证码功能（true开启，false关闭）'),(5,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2025-07-18 18:58:46','',NULL,'是否开启注册用户功能（true开启，false关闭）'),(6,'用户登录-黑名单列表','sys.login.blackIPList','','Y','admin','2025-07-18 18:58:46','',NULL,'设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）'),(7,'用户管理-初始密码修改策略','sys.account.initPasswordModify','1','Y','admin','2025-07-18 18:58:46','',NULL,'0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框'),(8,'用户管理-账号密码更新周期','sys.account.passwordValidateDays','0','Y','admin','2025-07-18 18:58:46','',NULL,'密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (100,0,'0','若依科技',0,'若依','15888888888','ry@qq.com','0','0','admin','2025-07-18 18:58:46','',NULL),(101,100,'0,100','深圳总公司',1,'若依','15888888888','ry@qq.com','0','0','admin','2025-07-18 18:58:46','',NULL),(102,100,'0,100','长沙分公司',2,'若依','15888888888','ry@qq.com','0','0','admin','2025-07-18 18:58:46','',NULL),(103,101,'0,100,101','研发部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2025-07-18 18:58:46','',NULL),(104,101,'0,100,101','市场部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2025-07-18 18:58:46','',NULL),(105,101,'0,100,101','测试部门',3,'若依','15888888888','ry@qq.com','0','0','admin','2025-07-18 18:58:46','',NULL),(106,101,'0,100,101','财务部门',4,'若依','15888888888','ry@qq.com','0','0','admin','2025-07-18 18:58:46','',NULL),(107,101,'0,100,101','运维部门',5,'若依','15888888888','ry@qq.com','0','0','admin','2025-07-18 18:58:46','',NULL),(108,102,'0,100,102','市场部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2025-07-18 18:58:46','',NULL),(109,102,'0,100,102','财务部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2025-07-18 18:58:46','',NULL);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` VALUES (1,1,'男','0','sys_user_sex','','','Y','0','admin','2025-07-18 18:58:46','',NULL,'性别男'),(2,2,'女','1','sys_user_sex','','','N','0','admin','2025-07-18 18:58:46','',NULL,'性别女'),(3,3,'未知','2','sys_user_sex','','','N','0','admin','2025-07-18 18:58:46','',NULL,'性别未知'),(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2025-07-18 18:58:46','',NULL,'显示菜单'),(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2025-07-18 18:58:46','',NULL,'隐藏菜单'),(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2025-07-18 18:58:46','',NULL,'正常状态'),(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2025-07-18 18:58:46','',NULL,'停用状态'),(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2025-07-18 18:58:46','',NULL,'正常状态'),(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2025-07-18 18:58:46','',NULL,'停用状态'),(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2025-07-18 18:58:46','',NULL,'默认分组'),(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2025-07-18 18:58:46','',NULL,'系统分组'),(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2025-07-18 18:58:46','',NULL,'系统默认是'),(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2025-07-18 18:58:46','',NULL,'系统默认否'),(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2025-07-18 18:58:46','',NULL,'通知'),(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2025-07-18 18:58:46','',NULL,'公告'),(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2025-07-18 18:58:46','',NULL,'正常状态'),(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2025-07-18 18:58:46','',NULL,'关闭状态'),(18,99,'其他','0','sys_oper_type','','info','N','0','admin','2025-07-18 18:58:46','',NULL,'其他操作'),(19,1,'新增','1','sys_oper_type','','info','N','0','admin','2025-07-18 18:58:46','',NULL,'新增操作'),(20,2,'修改','2','sys_oper_type','','info','N','0','admin','2025-07-18 18:58:46','',NULL,'修改操作'),(21,3,'删除','3','sys_oper_type','','danger','N','0','admin','2025-07-18 18:58:46','',NULL,'删除操作'),(22,4,'授权','4','sys_oper_type','','primary','N','0','admin','2025-07-18 18:58:46','',NULL,'授权操作'),(23,5,'导出','5','sys_oper_type','','warning','N','0','admin','2025-07-18 18:58:46','',NULL,'导出操作'),(24,6,'导入','6','sys_oper_type','','warning','N','0','admin','2025-07-18 18:58:46','',NULL,'导入操作'),(25,7,'强退','7','sys_oper_type','','danger','N','0','admin','2025-07-18 18:58:46','',NULL,'强退操作'),(26,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2025-07-18 18:58:46','',NULL,'生成操作'),(27,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2025-07-18 18:58:46','',NULL,'清空操作'),(28,1,'成功','0','sys_common_status','','primary','N','0','admin','2025-07-18 18:58:46','',NULL,'正常状态'),(29,2,'失败','1','sys_common_status','','danger','N','0','admin','2025-07-18 18:58:46','',NULL,'停用状态');
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'用户性别','sys_user_sex','0','admin','2025-07-18 18:58:46','',NULL,'用户性别列表'),(2,'菜单状态','sys_show_hide','0','admin','2025-07-18 18:58:46','',NULL,'菜单状态列表'),(3,'系统开关','sys_normal_disable','0','admin','2025-07-18 18:58:46','',NULL,'系统开关列表'),(4,'任务状态','sys_job_status','0','admin','2025-07-18 18:58:46','',NULL,'任务状态列表'),(5,'任务分组','sys_job_group','0','admin','2025-07-18 18:58:46','',NULL,'任务分组列表'),(6,'系统是否','sys_yes_no','0','admin','2025-07-18 18:58:46','',NULL,'系统是否列表'),(7,'通知类型','sys_notice_type','0','admin','2025-07-18 18:58:46','',NULL,'通知类型列表'),(8,'通知状态','sys_notice_status','0','admin','2025-07-18 18:58:46','',NULL,'通知状态列表'),(9,'操作类型','sys_oper_type','0','admin','2025-07-18 18:58:46','',NULL,'操作类型列表'),(10,'系统状态','sys_common_status','0','admin','2025-07-18 18:58:46','',NULL,'登录状态列表');
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job`
--

DROP TABLE IF EXISTS `sys_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务调度表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job`
--

LOCK TABLES `sys_job` WRITE;
/*!40000 ALTER TABLE `sys_job` DISABLE KEYS */;
INSERT INTO `sys_job` VALUES (1,'系统默认（无参）','DEFAULT','ryTask.ryNoParams','0/10 * * * * ?','3','1','1','admin','2025-07-18 18:58:46','',NULL,''),(2,'系统默认（有参）','DEFAULT','ryTask.ryParams(\'ry\')','0/15 * * * * ?','3','1','1','admin','2025-07-18 18:58:46','',NULL,''),(3,'系统默认（多参）','DEFAULT','ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)','0/20 * * * * ?','3','1','1','admin','2025-07-18 18:58:46','',NULL,'');
/*!40000 ALTER TABLE `sys_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job_log`
--

DROP TABLE IF EXISTS `sys_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job_log` (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) DEFAULT NULL COMMENT '日志信息',
  `status` char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务调度日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job_log`
--

LOCK TABLES `sys_job_log` WRITE;
/*!40000 ALTER TABLE `sys_job_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_job_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_logininfor`
--

DROP TABLE IF EXISTS `sys_logininfor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`),
  KEY `idx_sys_logininfor_s` (`status`),
  KEY `idx_sys_logininfor_lt` (`login_time`)
) ENGINE=InnoDB AUTO_INCREMENT=117 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统访问记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_logininfor`
--

LOCK TABLES `sys_logininfor` WRITE;
/*!40000 ALTER TABLE `sys_logininfor` DISABLE KEYS */;
INSERT INTO `sys_logininfor` VALUES (100,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 19:46:48'),(101,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 20:02:30'),(102,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 20:12:31'),(103,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 20:16:59'),(104,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 20:21:13'),(105,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','退出成功','2025-07-18 20:25:16'),(106,'ry','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 20:25:30'),(107,'ry','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','退出成功','2025-07-18 20:26:01'),(108,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 20:26:07'),(109,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 20:36:46'),(110,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','退出成功','2025-07-18 20:39:24'),(111,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 20:39:30'),(112,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 20:43:53'),(113,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 20:58:05'),(114,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','退出成功','2025-07-18 21:56:41'),(115,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 21:56:49'),(116,'admin','127.0.0.1','内网IP','Chrome 13','Mac OS X','0','登录成功','2025-07-18 22:56:04');
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2007 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系统管理',0,1,'system',NULL,'','',1,0,'M','0','0','','system','admin','2025-07-18 18:58:46','',NULL,'系统管理目录'),(2,'系统监控',0,2,'monitor',NULL,'','',1,0,'M','0','0','','monitor','admin','2025-07-18 18:58:46','',NULL,'系统监控目录'),(3,'系统工具',0,3,'tool',NULL,'','',1,0,'M','0','0','','tool','admin','2025-07-18 18:58:46','',NULL,'系统工具目录'),(4,'若依官网',0,4,'http://ruoyi.vip',NULL,'','',0,0,'M','0','0','','guide','admin','2025-07-18 18:58:46','',NULL,'若依官网地址'),(100,'用户管理',1,1,'user','system/user/index','','',1,0,'C','0','0','system:user:list','user','admin','2025-07-18 18:58:46','',NULL,'用户管理菜单'),(101,'角色管理',1,2,'role','system/role/index','','',1,0,'C','0','0','system:role:list','peoples','admin','2025-07-18 18:58:46','',NULL,'角色管理菜单'),(102,'菜单管理',1,3,'menu','system/menu/index','','',1,0,'C','0','0','system:menu:list','tree-table','admin','2025-07-18 18:58:46','',NULL,'菜单管理菜单'),(103,'部门管理',1,4,'dept','system/dept/index','','',1,0,'C','0','0','system:dept:list','tree','admin','2025-07-18 18:58:46','',NULL,'部门管理菜单'),(104,'岗位管理',1,5,'post','system/post/index','','',1,0,'C','0','0','system:post:list','post','admin','2025-07-18 18:58:46','',NULL,'岗位管理菜单'),(105,'字典管理',1,6,'dict','system/dict/index','','',1,0,'C','0','0','system:dict:list','dict','admin','2025-07-18 18:58:46','',NULL,'字典管理菜单'),(106,'参数设置',1,7,'config','system/config/index','','',1,0,'C','0','0','system:config:list','edit','admin','2025-07-18 18:58:46','',NULL,'参数设置菜单'),(107,'通知公告',1,8,'notice','system/notice/index','','',1,0,'C','0','0','system:notice:list','message','admin','2025-07-18 18:58:46','',NULL,'通知公告菜单'),(108,'日志管理',1,9,'log','','','',1,0,'M','0','0','','log','admin','2025-07-18 18:58:46','',NULL,'日志管理菜单'),(109,'在线用户',2,1,'online','monitor/online/index','','',1,0,'C','0','0','monitor:online:list','online','admin','2025-07-18 18:58:46','',NULL,'在线用户菜单'),(110,'定时任务',2,2,'job','monitor/job/index','','',1,0,'C','0','0','monitor:job:list','job','admin','2025-07-18 18:58:46','',NULL,'定时任务菜单'),(111,'数据监控',2,3,'druid','monitor/druid/index','','',1,0,'C','0','0','monitor:druid:list','druid','admin','2025-07-18 18:58:46','',NULL,'数据监控菜单'),(112,'服务监控',2,4,'server','monitor/server/index','','',1,0,'C','0','0','monitor:server:list','server','admin','2025-07-18 18:58:46','',NULL,'服务监控菜单'),(113,'缓存监控',2,5,'cache','monitor/cache/index','','',1,0,'C','0','0','monitor:cache:list','redis','admin','2025-07-18 18:58:46','',NULL,'缓存监控菜单'),(114,'缓存列表',2,6,'cacheList','monitor/cache/list','','',1,0,'C','0','0','monitor:cache:list','redis-list','admin','2025-07-18 18:58:46','',NULL,'缓存列表菜单'),(115,'表单构建',3,1,'build','tool/build/index','','',1,0,'C','0','0','tool:build:list','build','admin','2025-07-18 18:58:46','',NULL,'表单构建菜单'),(116,'代码生成',3,2,'gen','tool/gen/index','','',1,0,'C','0','0','tool:gen:list','code','admin','2025-07-18 18:58:46','',NULL,'代码生成菜单'),(117,'系统接口',3,3,'swagger','tool/swagger/index','','',1,0,'C','0','0','tool:swagger:list','swagger','admin','2025-07-18 18:58:46','',NULL,'系统接口菜单'),(500,'操作日志',108,1,'operlog','monitor/operlog/index','','',1,0,'C','0','0','monitor:operlog:list','form','admin','2025-07-18 18:58:46','',NULL,'操作日志菜单'),(501,'登录日志',108,2,'logininfor','monitor/logininfor/index','','',1,0,'C','0','0','monitor:logininfor:list','logininfor','admin','2025-07-18 18:58:46','',NULL,'登录日志菜单'),(1000,'用户查询',100,1,'','','','',1,0,'F','0','0','system:user:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1001,'用户新增',100,2,'','','','',1,0,'F','0','0','system:user:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1002,'用户修改',100,3,'','','','',1,0,'F','0','0','system:user:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1003,'用户删除',100,4,'','','','',1,0,'F','0','0','system:user:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1004,'用户导出',100,5,'','','','',1,0,'F','0','0','system:user:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1005,'用户导入',100,6,'','','','',1,0,'F','0','0','system:user:import','#','admin','2025-07-18 18:58:46','',NULL,''),(1006,'重置密码',100,7,'','','','',1,0,'F','0','0','system:user:resetPwd','#','admin','2025-07-18 18:58:46','',NULL,''),(1007,'角色查询',101,1,'','','','',1,0,'F','0','0','system:role:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1008,'角色新增',101,2,'','','','',1,0,'F','0','0','system:role:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1009,'角色修改',101,3,'','','','',1,0,'F','0','0','system:role:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1010,'角色删除',101,4,'','','','',1,0,'F','0','0','system:role:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1011,'角色导出',101,5,'','','','',1,0,'F','0','0','system:role:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1012,'菜单查询',102,1,'','','','',1,0,'F','0','0','system:menu:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1013,'菜单新增',102,2,'','','','',1,0,'F','0','0','system:menu:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1014,'菜单修改',102,3,'','','','',1,0,'F','0','0','system:menu:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1015,'菜单删除',102,4,'','','','',1,0,'F','0','0','system:menu:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1016,'部门查询',103,1,'','','','',1,0,'F','0','0','system:dept:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1017,'部门新增',103,2,'','','','',1,0,'F','0','0','system:dept:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1018,'部门修改',103,3,'','','','',1,0,'F','0','0','system:dept:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1019,'部门删除',103,4,'','','','',1,0,'F','0','0','system:dept:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1020,'岗位查询',104,1,'','','','',1,0,'F','0','0','system:post:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1021,'岗位新增',104,2,'','','','',1,0,'F','0','0','system:post:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1022,'岗位修改',104,3,'','','','',1,0,'F','0','0','system:post:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1023,'岗位删除',104,4,'','','','',1,0,'F','0','0','system:post:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1024,'岗位导出',104,5,'','','','',1,0,'F','0','0','system:post:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1025,'字典查询',105,1,'#','','','',1,0,'F','0','0','system:dict:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1026,'字典新增',105,2,'#','','','',1,0,'F','0','0','system:dict:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1027,'字典修改',105,3,'#','','','',1,0,'F','0','0','system:dict:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1028,'字典删除',105,4,'#','','','',1,0,'F','0','0','system:dict:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1029,'字典导出',105,5,'#','','','',1,0,'F','0','0','system:dict:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1030,'参数查询',106,1,'#','','','',1,0,'F','0','0','system:config:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1031,'参数新增',106,2,'#','','','',1,0,'F','0','0','system:config:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1032,'参数修改',106,3,'#','','','',1,0,'F','0','0','system:config:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1033,'参数删除',106,4,'#','','','',1,0,'F','0','0','system:config:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1034,'参数导出',106,5,'#','','','',1,0,'F','0','0','system:config:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1035,'公告查询',107,1,'#','','','',1,0,'F','0','0','system:notice:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1036,'公告新增',107,2,'#','','','',1,0,'F','0','0','system:notice:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1037,'公告修改',107,3,'#','','','',1,0,'F','0','0','system:notice:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1038,'公告删除',107,4,'#','','','',1,0,'F','0','0','system:notice:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1039,'操作查询',500,1,'#','','','',1,0,'F','0','0','monitor:operlog:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1040,'操作删除',500,2,'#','','','',1,0,'F','0','0','monitor:operlog:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1041,'日志导出',500,3,'#','','','',1,0,'F','0','0','monitor:operlog:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1042,'登录查询',501,1,'#','','','',1,0,'F','0','0','monitor:logininfor:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1043,'登录删除',501,2,'#','','','',1,0,'F','0','0','monitor:logininfor:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1044,'日志导出',501,3,'#','','','',1,0,'F','0','0','monitor:logininfor:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1045,'账户解锁',501,4,'#','','','',1,0,'F','0','0','monitor:logininfor:unlock','#','admin','2025-07-18 18:58:46','',NULL,''),(1046,'在线查询',109,1,'#','','','',1,0,'F','0','0','monitor:online:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1047,'批量强退',109,2,'#','','','',1,0,'F','0','0','monitor:online:batchLogout','#','admin','2025-07-18 18:58:46','',NULL,''),(1048,'单条强退',109,3,'#','','','',1,0,'F','0','0','monitor:online:forceLogout','#','admin','2025-07-18 18:58:46','',NULL,''),(1049,'任务查询',110,1,'#','','','',1,0,'F','0','0','monitor:job:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1050,'任务新增',110,2,'#','','','',1,0,'F','0','0','monitor:job:add','#','admin','2025-07-18 18:58:46','',NULL,''),(1051,'任务修改',110,3,'#','','','',1,0,'F','0','0','monitor:job:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1052,'任务删除',110,4,'#','','','',1,0,'F','0','0','monitor:job:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1053,'状态修改',110,5,'#','','','',1,0,'F','0','0','monitor:job:changeStatus','#','admin','2025-07-18 18:58:46','',NULL,''),(1054,'任务导出',110,6,'#','','','',1,0,'F','0','0','monitor:job:export','#','admin','2025-07-18 18:58:46','',NULL,''),(1055,'生成查询',116,1,'#','','','',1,0,'F','0','0','tool:gen:query','#','admin','2025-07-18 18:58:46','',NULL,''),(1056,'生成修改',116,2,'#','','','',1,0,'F','0','0','tool:gen:edit','#','admin','2025-07-18 18:58:46','',NULL,''),(1057,'生成删除',116,3,'#','','','',1,0,'F','0','0','tool:gen:remove','#','admin','2025-07-18 18:58:46','',NULL,''),(1058,'导入代码',116,4,'#','','','',1,0,'F','0','0','tool:gen:import','#','admin','2025-07-18 18:58:46','',NULL,''),(1059,'预览代码',116,5,'#','','','',1,0,'F','0','0','tool:gen:preview','#','admin','2025-07-18 18:58:46','',NULL,''),(1060,'生成代码',116,6,'#','','','',1,0,'F','0','0','tool:gen:code','#','admin','2025-07-18 18:58:46','',NULL,''),(2001,'文章管理',0,1,'article','system/article/index',NULL,'',1,0,'C','0','0','system:article:list','edit','admin','2025-07-18 20:11:29','admin','2025-07-18 21:07:30','博客文章菜单'),(2002,'博客文章查询',2001,1,'#','',NULL,'',1,0,'F','0','0','system:article:query','#','admin','2025-07-18 20:11:29','',NULL,''),(2003,'博客文章新增',2001,2,'#','',NULL,'',1,0,'F','0','0','system:article:add','#','admin','2025-07-18 20:11:29','',NULL,''),(2004,'博客文章修改',2001,3,'#','',NULL,'',1,0,'F','0','0','system:article:edit','#','admin','2025-07-18 20:11:29','',NULL,''),(2005,'博客文章删除',2001,4,'#','',NULL,'',1,0,'F','0','0','system:article:remove','#','admin','2025-07-18 20:11:29','',NULL,''),(2006,'博客文章导出',2001,5,'#','',NULL,'',1,0,'F','0','0','system:article:export','#','admin','2025-07-18 20:11:29','',NULL,'');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES (1,'温馨提醒：2018-07-01 若依新版本发布啦','2',_binary '新版本内容','0','admin','2025-07-18 18:58:46','',NULL,'管理员'),(2,'维护通知：2018-07-01 若依系统凌晨维护','1',_binary '维护内容','0','admin','2025-07-18 18:58:46','',NULL,'管理员');
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint DEFAULT '0' COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`),
  KEY `idx_sys_oper_log_bt` (`business_type`),
  KEY `idx_sys_oper_log_s` (`status`),
  KEY `idx_sys_oper_log_ot` (`oper_time`)
) ENGINE=InnoDB AUTO_INCREMENT=218 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
INSERT INTO `sys_oper_log` VALUES (100,'创建表',0,'com.ruoyi.generator.controller.GenController.createTableSave()','POST',1,'admin','研发部门','/tool/gen/createTable','127.0.0.1','内网IP','{\"sql\":\"CREATE TABLE blog_article (\\n    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT \'主键ID\',\\n    title VARCHAR(255) NOT NULL COMMENT \'文章标题\',\\n    summary VARCHAR(512) COMMENT \'摘要\',\\n    content LONGTEXT NOT NULL COMMENT \'文章内容\',\\n    cover_url VARCHAR(512) COMMENT \'封面图片\',\\n    category_id BIGINT COMMENT \'分类ID\',\\n    author_id BIGINT NOT NULL COMMENT \'作者ID（关联sys_user）\',\\n    is_top TINYINT DEFAULT 0 COMMENT \'是否置顶 0否 1是\',\\n    is_recommend TINYINT DEFAULT 0 COMMENT \'是否推荐 0否 1是\',\\n    status TINYINT DEFAULT 1 COMMENT \'状态 0草稿 1发布\',\\n    view_count INT DEFAULT 0 COMMENT \'浏览量\',\\n    like_count INT DEFAULT 0 COMMENT \'点赞数\',\\n    comment_count INT DEFAULT 0 COMMENT \'评论数\',\\n    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT \'创建时间\',\\n    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT \'更新时间\',\\n    del_flag TINYINT DEFAULT 0 COMMENT \'删除标志 0正常 1删除\'\\n) COMMENT=\'博客文章表\';\"}','{\"msg\":\"创建表结构异常\",\"code\":500}',0,NULL,'2025-07-18 19:37:07',50),(101,'创建表',0,'com.ruoyi.generator.controller.GenController.createTableSave()','POST',1,'admin','研发部门','/tool/gen/createTable','127.0.0.1','内网IP','{\"sql\":\"CREATE TABLE blog_article (\\n    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT \'主键ID\',\\n    title VARCHAR(255) NOT NULL COMMENT \'文章标题\',\\n    summary VARCHAR(512) COMMENT \'摘要\',\\n    content LONGTEXT NOT NULL COMMENT \'文章内容\',\\n    cover_url VARCHAR(512) COMMENT \'封面图片\',\\n    category_id BIGINT COMMENT \'分类ID\',\\n    author_id BIGINT NOT NULL COMMENT \'作者ID（关联sys_user）\',\\n    is_top TINYINT DEFAULT 0 COMMENT \'是否置顶 0否 1是\',\\n    is_recommend TINYINT DEFAULT 0 COMMENT \'是否推荐 0否 1是\',\\n    status TINYINT DEFAULT 1 COMMENT \'状态 0草稿 1发布\',\\n    view_count INT DEFAULT 0 COMMENT \'浏览量\',\\n    like_count INT DEFAULT 0 COMMENT \'点赞数\',\\n    comment_count INT DEFAULT 0 COMMENT \'评论数\',\\n    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT \'创建时间\',\\n    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT \'更新时间\',\\n    del_flag TINYINT DEFAULT 0 COMMENT \'删除标志 0正常 1删除\'\\n) COMMENT=\'博客文章表\';\"}','{\"msg\":\"创建表结构异常\",\"code\":500}',0,NULL,'2025-07-18 19:41:22',7),(102,'代码生成',6,'com.ruoyi.generator.controller.GenController.importTableSave()','POST',1,'admin','研发部门','/tool/gen/importTable','127.0.0.1','内网IP','{\"tables\":\"blog_article,blog_category,blog_tag,blog_article_tag,blog_comment,blog_friend_link,blog_setting\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 19:47:39',208),(103,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"article\",\"className\":\"BlogArticle\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"主键ID\",\"columnId\":1,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"文章标题\",\"columnId\":2,\"columnName\":\"title\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Summary\",\"columnComment\":\"摘要\",\"columnId\":3,\"columnName\":\"summary\",\"columnType\":\"varchar(512)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"textarea\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"summary\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Content\",\"columnComment\":\"文章内容\",\"columnId\":4,\"columnName\":\"content\",\"columnType\":\"longtext\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"editor\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"content\",\"ja','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 19:48:13',88),(104,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/blog_article','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 19:50:14',66),(105,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/blog_article_tag','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 19:50:18',20),(106,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/blog_category','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 19:50:20',25),(107,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/blog_comment','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 19:50:23',32),(108,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/blog_friend_link','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 19:50:26',26),(109,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"setting\",\"className\":\"BlogSetting\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"主键ID\",\"columnId\":43,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":6,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"ConfigKey\",\"columnComment\":\"配置项Key\",\"columnId\":44,\"columnName\":\"config_key\",\"columnType\":\"varchar(64)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"configKey\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":6,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"ConfigValue\",\"columnComment\":\"配置项Value\",\"columnId\":45,\"columnName\":\"config_value\",\"columnType\":\"varchar(1024)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"textarea\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"configValue\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":6,\"updateBy\":\"\",\"usableColumn\":false},{\"capJavaField\":\"Description\",\"columnComment\":\"描述\",\"columnId\":46,\"columnName\":\"description\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\"','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 19:51:09',27),(110,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"article\",\"className\":\"BlogArticle\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"主键ID\",\"columnId\":1,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 19:50:14\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"文章标题\",\"columnId\":2,\"columnName\":\"title\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 19:50:14\",\"usableColumn\":false},{\"capJavaField\":\"Summary\",\"columnComment\":\"摘要\",\"columnId\":3,\"columnName\":\"summary\",\"columnType\":\"varchar(512)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"textarea\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"summary\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 19:50:14\",\"usableColumn\":false},{\"capJavaField\":\"Content\",\"columnComment\":\"文章内容\",\"columnId\":4,\"columnName\":\"content\",\"columnType\":\"longtext\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"editor\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncre','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 19:57:05',143),(111,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"article\",\"className\":\"BlogArticle\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"主键ID\",\"columnId\":1,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 19:57:05\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"文章标题\",\"columnId\":2,\"columnName\":\"title\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 19:57:05\",\"usableColumn\":false},{\"capJavaField\":\"Summary\",\"columnComment\":\"摘要\",\"columnId\":3,\"columnName\":\"summary\",\"columnType\":\"varchar(512)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"textarea\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"summary\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 19:57:05\",\"usableColumn\":false},{\"capJavaField\":\"Content\",\"columnComment\":\"文章内容\",\"columnId\":4,\"columnName\":\"content\",\"columnType\":\"longtext\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"editor\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncre','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 19:58:21',55),(112,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/blog_article','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 19:59:00',74),(113,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"blog_article\"}',NULL,0,NULL,'2025-07-18 19:59:02',105),(114,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system:article:list\",\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"文章管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"system/article\",\"status\":\"0\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:05:40',47),(115,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"test\",\"createTime\":\"2025-07-18 20:12:47\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article          ( title,                          content,                                                                                                                                  create_time )           values ( ?,                          ?,                                                                                                                                  ? )\n### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value','2025-07-18 20:12:47',59),(116,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2000','127.0.0.1','内网IP','2000','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:13:29',22),(117,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/article/index\",\"createTime\":\"2025-07-18 20:11:29\",\"icon\":\"edit\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2001,\"menuName\":\"博客文章\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"article\",\"perms\":\"system:article:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:13:51',14),(118,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"### Error updating database. Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value ### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml] ### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline ### The error occurred while setting parameters ### SQL: insert into blog_article ( title, content, create_time ) values ( ?, ?, ? ) ### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value ; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\",\"createTime\":\"2025-07-18 20:14:09\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article          ( title,                          content,                                                                                                                                  create_time )           values ( ?,                          ?,                                                                                                                                  ? )\n### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value','2025-07-18 20:14:09',19),(119,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"### Error updating database. Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value ### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml] ### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline ### The error occurred while setting parameters ### SQL: insert into blog_article ( title, content, create_time ) values ( ?, ?, ? ) ### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value ; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\",\"createTime\":\"2025-07-18 20:16:27\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article          ( title,                          content,                                                                                                                                  create_time )           values ( ?,                          ?,                                                                                                                                  ? )\n### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value','2025-07-18 20:16:27',59),(120,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"test\",\"createTime\":\"2025-07-18 20:17:15\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article          ( title,                          content,                                                                                                                                  create_time )           values ( ?,                          ?,                                                                                                                                  ? )\n### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value','2025-07-18 20:17:15',24),(121,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"快科技7月18日消息，时隔4年多，华为重回中国智能手机出货量榜首。IDC发布的数据显示，中国第二季度智能手机的出货量同比减少4%，降至6900万部。时隔6个季度出现负增长。从各厂商来看，<strong>华为以1250万部的出货量位居第一。自美国政府的制裁影响其智能手机生产、销售陷入低迷以来，4年多来重回首位。</strong>IDC指出，“品牌的强力宣传和有效的出货政策奏效”。\",\"createTime\":\"2025-07-18 20:21:48\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article          ( title,                          content,                                                                                                                                  create_time )           values ( ?,                          ?,                                                                                                                                  ? )\n### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value','2025-07-18 20:21:48',42),(122,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"快科技7月18日消息，时隔4年多，华为重回中国智能手机出货量榜首。IDC发布的数据显示，中国第二季度智能手机的出货量同比减少4%，降至6900万部。时隔6个季度出现负增长。从各厂商来看，<strong>华为以1250万部的出货量位居第一。自美国政府的制裁影响其智能手机生产、销售陷入低迷以来，4年多来重回首位。</strong>IDC指出，“品牌的强力宣传和有效的出货政策奏效”。\",\"createTime\":\"2025-07-18 20:22:06\",\"id\":1,\"params\":{},\"title\":\"test\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:22:06',12),(123,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"blog_article\",\"className\":\"BlogArticle\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"主键ID\",\"columnId\":1,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 19:59:00\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"文章标题\",\"columnId\":2,\"columnName\":\"title\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 19:59:00\",\"usableColumn\":false},{\"capJavaField\":\"Summary\",\"columnComment\":\"摘要\",\"columnId\":3,\"columnName\":\"summary\",\"columnType\":\"varchar(512)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"textarea\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"summary\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 19:59:00\",\"usableColumn\":false},{\"capJavaField\":\"Content\",\"columnComment\":\"文章内容\",\"columnId\":4,\"columnName\":\"content\",\"columnType\":\"longtext\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"editor\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"is','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:28:24',82),(124,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/blog_article','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:28:28',70),(125,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"commentCount\":0,\"content\":\"快科技7月18日消息，时隔4年多，华为重回中国智能手机出货量榜首。IDC发布的数据显示，中国第二季度智能手机的出货量同比减少4%，降至6900万部。时隔6个季度出现负增长。从各厂商来看，<strong>华为以1250万部的出货量位居第一。自美国政府的制裁影响其智能手机生产、销售陷入低迷以来，4年多来重回首位。</strong>IDC指出，“品牌的强力宣传和有效的出货政策奏效”。\",\"createTime\":\"2025-07-18 20:22:07\",\"delFlag\":1,\"id\":1,\"isRecommend\":0,\"isTop\":0,\"likeCount\":0,\"params\":{},\"status\":1,\"title\":\"test\",\"updateTime\":\"2025-07-18 20:30:23\",\"viewCount\":0}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:30:23',24),(126,'博客文章',5,'com.ruoyi.system.controller.BlogArticleController.export()','POST',1,'admin','研发部门','/system/article/export','127.0.0.1','内网IP','{\"pageSize\":\"10\",\"pageNum\":\"1\"}',NULL,0,NULL,'2025-07-18 20:30:36',361),(127,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/1','127.0.0.1','内网IP','[1]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:31:33',77),(128,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"test\",\"createTime\":\"2025-07-18 20:31:43\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article          ( title,                          content,                                                                                                                                  create_time )           values ( ?,                          ?,                                                                                                                                  ? )\n### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value','2025-07-18 20:31:43',7),(129,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"test\",\"createTime\":\"2025-07-18 20:31:58\",\"id\":2,\"params\":{},\"title\":\"test\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:31:58',13),(130,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"commentCount\":2,\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"createTime\":\"2025-07-18 20:31:58\",\"delFlag\":0,\"id\":2,\"isRecommend\":1,\"isTop\":0,\"likeCount\":2,\"params\":{},\"status\":1,\"summary\":\"vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。\",\"title\":\"test\",\"updateTime\":\"2025-07-18 20:32:55\",\"viewCount\":222}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:32:55',22),(131,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"createTime\":\"2025-07-18 20:33:12\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article          ( title,                          content,                                                                                                                                  create_time )           values ( ?,                          ?,                                                                                                                                  ? )\n### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value','2025-07-18 20:33:12',3),(132,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"createTime\":\"2025-07-18 20:33:19\",\"id\":3,\"params\":{},\"title\":\"test\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:33:19',4),(133,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"test\",\"createTime\":\"2025-07-18 20:37:05\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article          ( title,                          content,                                                                                                                                  create_time )           values ( ?,                          ?,                                                                                                                                  ? )\n### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value','2025-07-18 20:37:05',67),(134,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"test\",\"createTime\":\"2025-07-18 20:37:11\",\"id\":4,\"params\":{},\"title\":\"test\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:37:11',8),(135,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"test\",\"createTime\":\"2025-07-18 20:39:18\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article          ( title,                          content,                                                                                                                                  create_time )           values ( ?,                          ?,                                                                                                                                  ? )\n### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value','2025-07-18 20:39:18',45),(136,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"test\",\"createTime\":\"2025-07-18 20:39:41\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article          ( title,                          content,                                                                                                                                  create_time )           values ( ?,                          ?,                                                                                                                                  ? )\n### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value','2025-07-18 20:39:41',7),(137,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"test\",\"createTime\":\"2025-07-18 20:39:47\",\"id\":5,\"params\":{},\"title\":\"test\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:39:47',5),(138,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"test\",\"createTime\":\"2025-07-18 20:41:55\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article          ( title,                          content,                                                                                                                                  create_time )           values ( ?,                          ?,                                                                                                                                  ? )\n### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value','2025-07-18 20:41:55',164),(139,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"test\",\"createTime\":\"2025-07-18 20:44:02\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article          ( title,                          content,                                                                                                                                  create_time )           values ( ?,                          ?,                                                                                                                                  ? )\n### Cause: java.sql.SQLException: Field \'author_id\' doesn\'t have a default value\n; Field \'author_id\' doesn\'t have a default value; nested exception is java.sql.SQLException: Field \'author_id\' doesn\'t have a default value','2025-07-18 20:44:03',41),(140,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"test\",\"createTime\":\"2025-07-18 20:50:38\",\"id\":6,\"params\":{},\"title\":\"test\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:50:38',109),(141,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/2','127.0.0.1','内网IP','[2]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:57:04',62),(142,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/3','127.0.0.1','内网IP','[3]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:57:07',4),(143,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"test\",\"createTime\":\"2025-07-18 20:57:18\",\"id\":7,\"params\":{},\"title\":\"test\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:57:18',17),(144,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/7','127.0.0.1','内网IP','[7]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:57:25',7),(145,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"test\",\"createTime\":\"2025-07-18 20:57:34\",\"id\":8,\"params\":{},\"title\":\"test\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:57:34',14),(146,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"test\",\"createTime\":\"2025-07-18 20:58:15\",\"id\":9,\"params\":{},\"title\":\"test\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:58:15',8),(147,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/9','127.0.0.1','内网IP','[9]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:58:19',5),(148,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"commentCount\":0,\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"createTime\":\"2025-07-18 20:37:11\",\"delFlag\":0,\"id\":4,\"isRecommend\":0,\"isTop\":0,\"likeCount\":0,\"params\":{},\"status\":1,\"title\":\"test\",\"updateTime\":\"2025-07-18 20:58:55\",\"viewCount\":0}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:58:55',10),(149,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/8','127.0.0.1','内网IP','[8]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 20:59:57',13),(150,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/6','127.0.0.1','内网IP','[6]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:00:00',7),(151,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/5','127.0.0.1','内网IP','[5]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:00:02',6),(152,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"createTime\":\"2025-07-18 21:02:31\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Column \'content\' cannot be null\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article         (title, summary, content, cover_url, category_id, author_id, is_top, is_recommend, status, view_count, like_count, comment_count, create_time, update_time, del_flag)         values         (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Column \'content\' cannot be null\n; Column \'content\' cannot be null; nested exception is java.sql.SQLIntegrityConstraintViolationException: Column \'content\' cannot be null','2025-07-18 21:02:31',135),(153,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"test\",\"createTime\":\"2025-07-18 21:02:37\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'test\' for key \'blog_article.uk_title\'\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article         (title, summary, content, cover_url, category_id, author_id, is_top, is_recommend, status, view_count, like_count, comment_count, create_time, update_time, del_flag)         values         (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'test\' for key \'blog_article.uk_title\'\n; Duplicate entry \'test\' for key \'blog_article.uk_title\'; nested exception is java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'test\' for key \'blog_article.uk_title\'','2025-07-18 21:02:37',9),(154,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"test\",\"createTime\":\"2025-07-18 21:02:47\",\"id\":11,\"params\":{},\"title\":\"test1\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:02:47',11),(155,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"\",\"createTime\":\"2025-07-18 21:02:57\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'test\' for key \'blog_article.uk_title\'\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article         (title, summary, content, cover_url, category_id, author_id, is_top, is_recommend, status, view_count, like_count, comment_count, create_time, update_time, del_flag)         values         (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'test\' for key \'blog_article.uk_title\'\n; Duplicate entry \'test\' for key \'blog_article.uk_title\'; nested exception is java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'test\' for key \'blog_article.uk_title\'','2025-07-18 21:02:57',5),(156,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"\",\"createTime\":\"2025-07-18 21:03:02\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'test\' for key \'blog_article.uk_title\'\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article         (title, summary, content, cover_url, category_id, author_id, is_top, is_recommend, status, view_count, like_count, comment_count, create_time, update_time, del_flag)         values         (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'test\' for key \'blog_article.uk_title\'\n; Duplicate entry \'test\' for key \'blog_article.uk_title\'; nested exception is java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'test\' for key \'blog_article.uk_title\'','2025-07-18 21:03:02',7),(157,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"111\",\"createTime\":\"2025-07-18 21:03:12\",\"params\":{},\"title\":\"test\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'test\' for key \'blog_article.uk_title\'\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article         (title, summary, content, cover_url, category_id, author_id, is_top, is_recommend, status, view_count, like_count, comment_count, create_time, update_time, del_flag)         values         (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'test\' for key \'blog_article.uk_title\'\n; Duplicate entry \'test\' for key \'blog_article.uk_title\'; nested exception is java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'test\' for key \'blog_article.uk_title\'','2025-07-18 21:03:12',6),(158,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"params\":{},\"title\":\"test\"}',NULL,1,'文章标题已存在，请更换标题！','2025-07-18 21:05:37',25),(159,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"params\":{},\"title\":\"test1\"}',NULL,1,'文章标题已存在，请更换标题！','2025-07-18 21:05:44',5),(160,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"createTime\":\"2025-07-18 21:05:51\",\"params\":{},\"title\":\"test2\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Column \'content\' cannot be null\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.insertBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: insert into blog_article         (title, summary, content, cover_url, category_id, author_id, is_top, is_recommend, status, view_count, like_count, comment_count, create_time, update_time, del_flag)         values         (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Column \'content\' cannot be null\n; Column \'content\' cannot be null; nested exception is java.sql.SQLIntegrityConstraintViolationException: Column \'content\' cannot be null','2025-07-18 21:05:52',56),(161,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"test\",\"createTime\":\"2025-07-18 21:05:59\",\"id\":15,\"params\":{},\"title\":\"test2\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:05:59',12),(162,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/article/index\",\"createTime\":\"2025-07-18 20:11:29\",\"icon\":\"edit\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2001,\"menuName\":\"文章管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"article\",\"perms\":\"system:article:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:07:30',45),(163,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"params\":{},\"title\":\"test3\"}',NULL,1,'文章内容不能为空，请填写内容后再发布！','2025-07-18 21:09:11',11),(164,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"createTime\":\"2025-07-18 21:09:29\",\"id\":16,\"params\":{},\"title\":\"test3\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:09:29',44),(165,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"params\":{},\"title\":\"test\"}',NULL,1,'文章标题已存在，请更换标题！','2025-07-18 21:10:03',15),(166,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"params\":{},\"title\":\"test2\"}',NULL,1,'文章标题已存在，请更换标题！','2025-07-18 21:10:18',8),(167,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"params\":{},\"title\":\"test2\"}',NULL,1,'文章标题已存在，请更换标题！','2025-07-18 21:10:34',6),(168,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"params\":{},\"title\":\"test3\"}',NULL,1,'文章标题已存在，请更换标题！','2025-07-18 21:10:39',4),(169,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"createTime\":\"2025-07-18 21:10:57\",\"id\":17,\"params\":{},\"title\":\"vivo和oppo紧随其后\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:10:57',26),(170,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"createTime\":\"2025-07-18 21:10:57\",\"id\":17,\"params\":{},\"title\":\"vivo和oppo紧随其后\",\"updateTime\":\"2025-07-18 21:11:29\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:11:29',16),(171,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"请检查&nbsp;editor&nbsp;组件注册\",\"params\":{},\"title\":\"test\"}',NULL,1,'文章标题已存在，请更换标题！','2025-07-18 21:20:02',16),(172,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"请检查&nbsp;editor&nbsp;组件注册<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"createTime\":\"2025-07-18 21:20:23\",\"id\":18,\"params\":{},\"title\":\"test4\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:20:23',25),(173,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/4','127.0.0.1','内网IP','[4]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:20:36',16),(174,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/11','127.0.0.1','内网IP','[11]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:20:39',4),(175,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/15','127.0.0.1','内网IP','[15]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:20:41',6),(176,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/16','127.0.0.1','内网IP','[16]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:20:44',5),(177,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/17','127.0.0.1','内网IP','[17]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:20:47',5),(178,'博客文章',3,'com.ruoyi.system.controller.BlogArticleController.remove()','DELETE',1,'admin','研发部门','/system/article/18','127.0.0.1','内网IP','[18]','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:20:50',2),(179,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"createTime\":\"2025-07-18 21:21:03\",\"id\":19,\"params\":{},\"title\":\"vivo\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:21:03',12),(180,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"blog_article\",\"className\":\"BlogArticle\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"主键ID\",\"columnId\":1,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 20:28:28\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"文章标题\",\"columnId\":2,\"columnName\":\"title\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 20:28:28\",\"usableColumn\":false},{\"capJavaField\":\"Summary\",\"columnComment\":\"摘要\",\"columnId\":3,\"columnName\":\"summary\",\"columnType\":\"varchar(512)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"textarea\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"0\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"summary\",\"javaType\":\"String\",\"list\":false,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 20:28:28\",\"usableColumn\":false},{\"capJavaField\":\"Content\",\"columnComment\":\"文章内容\",\"columnId\":4,\"columnName\":\"content\",\"columnType\":\"longtext\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"editor\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"i','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:21:47',56),(181,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"blog_article\",\"className\":\"BlogArticle\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"主键ID\",\"columnId\":1,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 21:21:47\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"文章标题\",\"columnId\":2,\"columnName\":\"title\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 21:21:47\",\"usableColumn\":false},{\"capJavaField\":\"Summary\",\"columnComment\":\"摘要\",\"columnId\":3,\"columnName\":\"summary\",\"columnType\":\"varchar(512)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"textarea\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"0\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"summary\",\"javaType\":\"String\",\"list\":false,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 21:21:47\",\"usableColumn\":false},{\"capJavaField\":\"Content\",\"columnComment\":\"文章内容\",\"columnId\":4,\"columnName\":\"content\",\"columnType\":\"longtext\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"editor\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"i','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:22:41',39),(182,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/blog_article','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:22:44',67),(183,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"createTime\":\"2025-07-18 21:23:05\",\"id\":20,\"params\":{},\"title\":\"test\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:23:05',21),(184,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"params\":{},\"title\":\"test\"}',NULL,1,'文章标题已存在，请更换标题！','2025-07-18 21:38:57',68),(185,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"createTime\":\"2025-07-18 21:39:03\",\"id\":21,\"params\":{},\"title\":\"test1\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:39:03',14),(186,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"params\":{},\"title\":\"test2\"}',NULL,1,'文章内容不能为空，请填写内容后再发布！','2025-07-18 21:39:36',3),(187,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"鑫\",\"createTime\":\"2025-07-18 21:39:43\",\"id\":22,\"params\":{},\"title\":\"test2\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:39:43',19),(188,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p>磊在在在在</p>\",\"createTime\":\"2025-07-18 21:50:30\",\"id\":23,\"params\":{},\"title\":\"test3\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:50:30',187),(189,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"blog_article\",\"className\":\"BlogArticle\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"主键ID\",\"columnId\":1,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 21:22:44\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"文章标题\",\"columnId\":2,\"columnName\":\"title\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 21:22:44\",\"usableColumn\":false},{\"capJavaField\":\"Summary\",\"columnComment\":\"摘要\",\"columnId\":3,\"columnName\":\"summary\",\"columnType\":\"varchar(512)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"textarea\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"summary\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 21:22:44\",\"usableColumn\":false},{\"capJavaField\":\"Content\",\"columnComment\":\"文章内容\",\"columnId\":4,\"columnName\":\"content\",\"columnType\":\"longtext\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"editor\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"is','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:57:38',55),(190,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/blog_article','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:57:42',73),(191,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。\",\"createTime\":\"2025-07-18 21:21:03\",\"id\":19,\"params\":{},\"title\":\"vivo\",\"updateTime\":\"2025-07-18 21:58:05\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:58:06',17),(192,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p>磊在在在在</p>\",\"createTime\":\"2025-07-18 21:50:30\",\"id\":23,\"params\":{},\"title\":\"test3\",\"updateTime\":\"2025-07-18 21:58:16\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 21:58:17',5),(193,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p><strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。</p>\",\"createTime\":\"2025-07-18 21:23:06\",\"id\":20,\"params\":{},\"title\":\"美国解禁H20芯片深层原因 白宫AI主管：打压华为\",\"updateTime\":\"2025-07-18 22:01:47\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 22:01:47',26),(194,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p><strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。</p>\",\"createTime\":\"2025-07-18 21:39:03\",\"id\":21,\"params\":{},\"title\":\"vivo和OPPO紧随其后，排在第4位的是小米\",\"updateTime\":\"2025-07-18 22:02:01\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 22:02:01',24),(195,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p>鑫</p>\",\"createTime\":\"2025-07-18 21:39:44\",\"id\":22,\"params\":{},\"title\":\"vivo和OPPO紧随其后，排在第4位的是小米\",\"updateTime\":\"2025-07-18 22:02:22\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'vivo和OPPO紧随其后，排在第4位的是小米\' for key \'blog_article.uk_title\'\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.updateBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: update blog_article          SET title = ?,                          content = ?,                                       author_id = ?,                                                                                           create_time = ?,             update_time = ?          where id = ?\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'vivo和OPPO紧随其后，排在第4位的是小米\' for key \'blog_article.uk_title\'\n; Duplicate entry \'vivo和OPPO紧随其后，排在第4位的是小米\' for key \'blog_article.uk_title\'; nested exception is java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'vivo和OPPO紧随其后，排在第4位的是小米\' for key \'blog_article.uk_title\'','2025-07-18 22:02:23',78),(196,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p>鑫</p>\",\"createTime\":\"2025-07-18 21:39:44\",\"id\":22,\"params\":{},\"title\":\"vivo和OPPO紧随其后排在第4位的是小米\",\"updateTime\":\"2025-07-18 22:02:35\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 22:02:35',5),(197,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p>磊在在在在</p>\",\"createTime\":\"2025-07-18 21:50:30\",\"id\":23,\"params\":{},\"title\":\"vivo和OPPO紧随其后，排在第4位的是小米\",\"updateTime\":\"2025-07-18 22:02:48\"}',NULL,1,'\n### Error updating database.  Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'vivo和OPPO紧随其后，排在第4位的是小米\' for key \'blog_article.uk_title\'\n### The error may exist in URL [jar:file:/Users/nevell/.m2/repository/com/ruoyi/ruoyi-system/3.9.0/ruoyi-system-3.9.0.jar!/mapper/system/BlogArticleMapper.xml]\n### The error may involve com.ruoyi.system.mapper.BlogArticleMapper.updateBlogArticle-Inline\n### The error occurred while setting parameters\n### SQL: update blog_article          SET title = ?,                          content = ?,                                       author_id = ?,                                                                                           create_time = ?,             update_time = ?          where id = ?\n### Cause: java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'vivo和OPPO紧随其后，排在第4位的是小米\' for key \'blog_article.uk_title\'\n; Duplicate entry \'vivo和OPPO紧随其后，排在第4位的是小米\' for key \'blog_article.uk_title\'; nested exception is java.sql.SQLIntegrityConstraintViolationException: Duplicate entry \'vivo和OPPO紧随其后，排在第4位的是小米\' for key \'blog_article.uk_title\'','2025-07-18 22:02:48',4),(198,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p>磊在在在在</p>\",\"createTime\":\"2025-07-18 21:50:30\",\"id\":23,\"params\":{},\"title\":\"hello你们好\",\"updateTime\":\"2025-07-18 22:59:56\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 22:59:56',41),(199,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"<p>vivo和OPPO紧随其后，排在第4位的是小米</p>\",\"params\":{},\"title\":\"vivo和OPPO紧随其后，排在第4位的是小米\"}',NULL,1,'文章标题已存在，请更换标题！','2025-07-18 23:03:07',21),(200,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p>vivo和OPPO紧随其后，排在第4位的是小米</p>\",\"createTime\":\"2025-07-18 23:03:14\",\"id\":24,\"params\":{},\"title\":\"vivo和oppo紧随其后，排在第4位的\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:03:14',29),(201,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p>vivo和OPPO紧随其后，排在第4位的是小米</p>\",\"createTime\":\"2025-07-18 23:03:43\",\"id\":25,\"params\":{},\"title\":\"vivo和oppo紧随其后，4位的是小米\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:03:43',10),(202,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">购车的陈女士介绍，她是被人介绍来这家门店买车的，她签订的购车合同显示，</span><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">一周后提车，但到了交车日期，这家4S店一拖再拖，迟迟无法轿车。</strong></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">另有其他车主表示，她是早早拿到了车，却因为门店负责人将合格证与购车发票抵押给了债主，导致无法上牌。</span></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">她们把情况投诉给奇瑞的400客服后，却得到相同的回答，查无此店，无法提供帮助。</span></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">媒体来到涉事门店发现，这家所谓的“奇瑞4S店”，门头悬挂的却是正规的奇瑞logo，无论是门头布置还是展厅设计，都让人感觉是一家正规的品牌4S店。</span></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">而该店的销售员工表示：</span><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">该店为厂家店，厂家直营，还可以送售后服务。</strong></p><p><br></p>\",\"params\":{},\"title\":\"vivo\"}',NULL,1,'文章标题已存在，请更换标题！','2025-07-18 23:04:33',69),(203,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">购车的陈女士介绍，她是被人介绍来这家门店买车的，她签订的购车合同显示，</span><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">一周后提车，但到了交车日期，这家4S店一拖再拖，迟迟无法轿车。</strong></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">另有其他车主表示，她是早早拿到了车，却因为门店负责人将合格证与购车发票抵押给了债主，导致无法上牌。</span></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">她们把情况投诉给奇瑞的400客服后，却得到相同的回答，查无此店，无法提供帮助。</span></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">媒体来到涉事门店发现，这家所谓的“奇瑞4S店”，门头悬挂的却是正规的奇瑞logo，无论是门头布置还是展厅设计，都让人感觉是一家正规的品牌4S店。</span></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">而该店的销售员工表示：</span><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">该店为厂家店，厂家直营，还可以送售后服务。</strong></p><p><br></p>\",\"params\":{},\"title\":\"vivo\"}',NULL,1,'文章标题已存在，请更换标题！','2025-07-18 23:04:36',7),(204,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">购车的陈女士介绍，她是被人介绍来这家门店买车的，她签订的购车合同显示，</span><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">一周后提车，但到了交车日期，这家4S店一拖再拖，迟迟无法轿车。</strong></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">另有其他车主表示，她是早早拿到了车，却因为门店负责人将合格证与购车发票抵押给了债主，导致无法上牌。</span></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">她们把情况投诉给奇瑞的400客服后，却得到相同的回答，查无此店，无法提供帮助。</span></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">媒体来到涉事门店发现，这家所谓的“奇瑞4S店”，门头悬挂的却是正规的奇瑞logo，无论是门头布置还是展厅设计，都让人感觉是一家正规的品牌4S店。</span></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">而该店的销售员工表示：</span><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">该店为厂家店，厂家直营，还可以送售后服务。</strong></p><p><br></p>\",\"createTime\":\"2025-07-18 23:04:42\",\"id\":26,\"params\":{},\"title\":\"vivo vivo\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:04:42',16),(205,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"blog_article\",\"className\":\"BlogArticle\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"主键ID\",\"columnId\":1,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 21:57:42\",\"usableColumn\":false},{\"capJavaField\":\"Title\",\"columnComment\":\"文章标题\",\"columnId\":2,\"columnName\":\"title\",\"columnType\":\"varchar(255)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"title\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 21:57:42\",\"usableColumn\":false},{\"capJavaField\":\"Summary\",\"columnComment\":\"摘要\",\"columnId\":3,\"columnName\":\"summary\",\"columnType\":\"varchar(512)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"textarea\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"0\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"summary\",\"javaType\":\"String\",\"list\":false,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":1,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 21:57:42\",\"usableColumn\":false},{\"capJavaField\":\"Content\",\"columnComment\":\"文章内容\",\"columnId\":4,\"columnName\":\"content\",\"columnType\":\"longtext\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"editor\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"i','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:15:22',71),(206,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/blog_article','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:15:26',69),(207,'代码生成',2,'com.ruoyi.generator.controller.GenController.editSave()','PUT',1,'admin','研发部门','/tool/gen','127.0.0.1','内网IP','{\"businessName\":\"category\",\"className\":\"BlogCategory\",\"columns\":[{\"capJavaField\":\"Id\",\"columnComment\":\"主键ID\",\"columnId\":20,\"columnName\":\"id\",\"columnType\":\"bigint\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"input\",\"increment\":true,\"insert\":true,\"isIncrement\":\"1\",\"isInsert\":\"1\",\"isPk\":\"1\",\"isRequired\":\"0\",\"javaField\":\"id\",\"javaType\":\"Long\",\"list\":false,\"params\":{},\"pk\":true,\"query\":false,\"queryType\":\"EQ\",\"required\":false,\"sort\":1,\"superColumn\":false,\"tableId\":3,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 19:50:20\",\"usableColumn\":false},{\"capJavaField\":\"Name\",\"columnComment\":\"分类名称\",\"columnId\":21,\"columnName\":\"name\",\"columnType\":\"varchar(64)\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"1\",\"javaField\":\"name\",\"javaType\":\"String\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"LIKE\",\"required\":true,\"sort\":2,\"superColumn\":false,\"tableId\":3,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 19:50:20\",\"usableColumn\":false},{\"capJavaField\":\"Sort\",\"columnComment\":\"排序\",\"columnId\":22,\"columnName\":\"sort\",\"columnType\":\"int\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":true,\"htmlType\":\"input\",\"increment\":false,\"insert\":true,\"isEdit\":\"1\",\"isIncrement\":\"0\",\"isInsert\":\"1\",\"isList\":\"1\",\"isPk\":\"0\",\"isQuery\":\"1\",\"isRequired\":\"0\",\"javaField\":\"sort\",\"javaType\":\"Long\",\"list\":true,\"params\":{},\"pk\":false,\"query\":true,\"queryType\":\"EQ\",\"required\":false,\"sort\":3,\"superColumn\":false,\"tableId\":3,\"updateBy\":\"\",\"updateTime\":\"2025-07-18 19:50:20\",\"usableColumn\":false},{\"capJavaField\":\"CreateTime\",\"columnComment\":\"创建时间\",\"columnId\":23,\"columnName\":\"create_time\",\"columnType\":\"datetime\",\"createBy\":\"admin\",\"createTime\":\"2025-07-18 19:47:38\",\"dictType\":\"\",\"edit\":false,\"htmlType\":\"datetime\",\"increment\":false,\"insert\":true,\"isIncrement\":\"0\",\"isInsert\":\"','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:16:57',30),(208,'代码生成',2,'com.ruoyi.generator.controller.GenController.synchDb()','GET',1,'admin','研发部门','/tool/gen/synchDb/blog_category','127.0.0.1','内网IP','{}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:17:03',33),(209,'代码生成',8,'com.ruoyi.generator.controller.GenController.batchGenCode()','GET',1,'admin','研发部门','/tool/gen/batchGenCode','127.0.0.1','内网IP','{\"tables\":\"blog_category\"}',NULL,0,NULL,'2025-07-18 23:17:08',141),(210,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p><strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。</p>\",\"createTime\":\"2025-07-18 21:23:06\",\"id\":20,\"isTop\":1,\"params\":{},\"title\":\"美国解禁h20芯片深层原因 白宫ai主管：打压华为\",\"updateTime\":\"2025-07-18 23:30:19\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:30:19',49),(211,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p><strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。</p>\",\"createTime\":\"2025-07-18 21:39:03\",\"id\":21,\"isRecommend\":1,\"params\":{},\"title\":\"vivo和oppo紧随其后，排在第4位的是小米\",\"updateTime\":\"2025-07-18 23:30:32\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:30:32',10),(212,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"categoryId\":1,\"content\":\"<p><strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。</p>\",\"createTime\":\"2025-07-18 21:23:06\",\"id\":20,\"isTop\":1,\"params\":{},\"title\":\"美国解禁h20芯片深层原因 白宫ai主管：打压华为\",\"updateTime\":\"2025-07-18 23:31:55\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:31:55',10),(213,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"categoryId\":2,\"content\":\"<p><strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。</p>\",\"createTime\":\"2025-07-18 21:39:03\",\"id\":21,\"isRecommend\":1,\"params\":{},\"title\":\"vivo和oppo紧随其后，排在第4位的是小米\",\"updateTime\":\"2025-07-18 23:32:03\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:32:03',9),(214,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"categoryId\":3,\"content\":\"<p>vivo和OPPO紧随其后，排在第4位的是小米</p>\",\"createTime\":\"2025-07-18 23:03:15\",\"id\":24,\"params\":{},\"title\":\"vivo和oppo紧随其后，排在第4位的\",\"updateTime\":\"2025-07-18 23:32:16\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:32:16',15),(215,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p><strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。</p>\",\"createTime\":\"2025-07-18 21:21:03\",\"id\":19,\"params\":{},\"title\":\"vivo\",\"updateTime\":\"2025-07-18 23:35:07\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:35:07',21),(216,'博客文章',2,'com.ruoyi.system.controller.BlogArticleController.edit()','PUT',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"categoryId\":1,\"content\":\"<p><strong>vivo和OPPO紧随其后，排在第4位的是小米。小米的出货量增长3.4%，是排名TOP 5厂商中唯一增加的。</strong>在节约意识增强的背景下，小米以擅长的低价产品为核心，“受到重视价格的消费者的支持”，IDC称。苹果排在第5位，受到中国企业挤压的状况仍在持续。苹果把iPhone16、iPhone16 Pro的价格下调至政府补贴的对象范围之内，缩小了出货量的降幅。值得一提的是，<strong>中国市场TOP5排名中已经看不到荣耀的身影。尽管进入2025年其产品发布密集，但在华为强势回归之后，荣耀急需向市场证明自身产品特色，撕掉以往“华为替代品”的标签</strong>。</p>\",\"createTime\":\"2025-07-18 21:23:06\",\"id\":20,\"isTop\":1,\"params\":{},\"title\":\"美国解禁h20芯片深层原因 白宫ai主管：打压华为\",\"updateTime\":\"2025-07-18 23:40:33\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:40:34',42),(217,'博客文章',1,'com.ruoyi.system.controller.BlogArticleController.add()','POST',1,'admin','研发部门','/system/article','127.0.0.1','内网IP','{\"authorId\":1,\"content\":\"<p><strong style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">发布会结束后，贾总一如既往的煽情</strong><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">：“洛杉矶DTLA 7.17，California sunset映照天际线，FX Super One全球公众产品发布会成功，Super EAI F.A.C.E.也终于揭幕与你们见面，且正式开启C端预定。</span></p><p><span style=\\\"background-color: rgb(255, 255, 255); color: rgb(51, 51, 51);\\\">今晚最遗憾的是没能送可儿乐儿回家，爸爸又一次缺席了，但我知道，你们会一如既往的理解和支持爸爸。追梦者的新征程新起点，我们再次把不可能变为可能”。</span></p><p><br></p>\",\"createTime\":\"2025-07-18 23:41:38\",\"id\":27,\"params\":{},\"title\":\"我们再次把不可能变为可能\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2025-07-18 23:41:38',86);
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_post`
--

LOCK TABLES `sys_post` WRITE;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT INTO `sys_post` VALUES (1,'ceo','董事长',1,'0','admin','2025-07-18 18:58:46','',NULL,''),(2,'se','项目经理',2,'0','admin','2025-07-18 18:58:46','',NULL,''),(3,'hr','人力资源',3,'0','admin','2025-07-18 18:58:46','',NULL,''),(4,'user','普通员工',4,'0','admin','2025-07-18 18:58:46','',NULL,'');
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2025-07-18 18:58:46','',NULL,'超级管理员'),(2,'普通角色','common',2,'2',1,1,'0','0','admin','2025-07-18 18:58:46','',NULL,'普通角色');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_dept`
--

DROP TABLE IF EXISTS `sys_role_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和部门关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_dept`
--

LOCK TABLES `sys_role_dept` WRITE;
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
INSERT INTO `sys_role_dept` VALUES (2,100),(2,101),(2,105);
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (2,1),(2,2),(2,3),(2,4),(2,100),(2,101),(2,102),(2,103),(2,104),(2,105),(2,106),(2,107),(2,108),(2,109),(2,110),(2,111),(2,112),(2,113),(2,114),(2,115),(2,116),(2,117),(2,500),(2,501),(2,1000),(2,1001),(2,1002),(2,1003),(2,1004),(2,1005),(2,1006),(2,1007),(2,1008),(2,1009),(2,1010),(2,1011),(2,1012),(2,1013),(2,1014),(2,1015),(2,1016),(2,1017),(2,1018),(2,1019),(2,1020),(2,1021),(2,1022),(2,1023),(2,1024),(2,1025),(2,1026),(2,1027),(2,1028),(2,1029),(2,1030),(2,1031),(2,1032),(2,1033),(2,1034),(2,1035),(2,1036),(2,1037),(2,1038),(2,1039),(2,1040),(2,1041),(2,1042),(2,1043),(2,1044),(2,1045),(2,1046),(2,1047),(2,1048),(2,1049),(2,1050),(2,1051),(2,1052),(2,1053),(2,1054),(2,1055),(2,1056),(2,1057),(2,1058),(2,1059),(2,1060);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `pwd_update_date` datetime DEFAULT NULL COMMENT '密码最后更新时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,103,'admin','若依','00','ry@163.com','15888888888','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2025-07-18 22:56:05','2025-07-18 18:58:46','admin','2025-07-18 18:58:46','','2025-07-18 22:56:04','管理员'),(2,105,'ry','若依','00','ry@qq.com','15666666666','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2025-07-18 20:25:31','2025-07-18 18:58:46','admin','2025-07-18 18:58:46','','2025-07-18 20:25:30','测试员');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_post`
--

DROP TABLE IF EXISTS `sys_user_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与岗位关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_post`
--

LOCK TABLES `sys_user_post` WRITE;
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT INTO `sys_user_post` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-23 23:03:50
