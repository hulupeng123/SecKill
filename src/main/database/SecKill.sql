/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.7.20 : Database - seckill
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`seckill` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `seckill`;

/*Table structure for table `seckill` */

DROP TABLE IF EXISTS `seckill`;

CREATE TABLE `seckill` (
  `seckill_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
  `name` varchar(120) NOT NULL COMMENT '商品名称',
  `number` int(11) NOT NULL COMMENT '库存数量',
  `start_time` timestamp NOT NULL COMMENT '秒杀开始时间',
  `end_time` timestamp NOT NULL COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

/*Data for the table `seckill` */

insert  into `seckill`(`seckill_id`,`name`,`number`,`start_time`,`end_time`,`create_time`) values (1,'7000元秒杀华为',199,'2019-09-01 00:00:00','2019-09-18 00:00:00','2019-09-17 15:51:56'),(1000,'1000元秒杀iphone6',99,'2019-09-05 00:00:00','2019-09-12 00:00:00','2019-09-07 09:56:37'),(1001,'800元秒杀ipad',190,'2019-09-12 00:00:00','2019-09-18 00:00:00','2019-09-19 09:56:37'),(1002,'6600元秒杀mac book pro',295,'2019-09-05 00:00:00','2019-09-15 00:00:00','2019-09-19 09:56:37'),(1003,'7000元秒杀iMac',400,'2019-09-01 00:00:00','2019-09-12 00:00:00','2019-08-19 09:56:37');

/*Table structure for table `success_killed` */

DROP TABLE IF EXISTS `success_killed`;

CREATE TABLE `success_killed` (
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀商品ID',
  `user_phone` bigint(20) NOT NULL COMMENT '用户手机号',
  `state` tinyint(4) NOT NULL DEFAULT '-1' COMMENT '状态标识:-1:无效 0:成功 1:已付款 2:已发货',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`seckill_id`,`user_phone`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';

/*Data for the table `success_killed` */

insert  into `success_killed`(`seckill_id`,`user_phone`,`state`,`create_time`) values (1,13212312312,0,'2019-09-17 11:50:05'),(1001,13211111111,0,'2019-09-17 14:39:25'),(1001,13212312312,0,'2019-09-17 11:52:03'),(1002,13243237331,0,'2019-09-12 11:30:32');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
