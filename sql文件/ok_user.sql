/*
 Navicat Premium Data Transfer

 Source Server         : 111
 Source Server Type    : MySQL
 Source Server Version : 50644
 Source Host           : 47.244.38.236:3306
 Source Schema         : dbname_db

 Target Server Type    : MySQL
 Target Server Version : 50644
 File Encoding         : 65001

 Date: 17/10/2019 18:00:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ok_user
-- ----------------------------
DROP TABLE IF EXISTS `ok_user`;
CREATE TABLE `ok_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_no` varchar(225) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1',
  `groups` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `is_online` tinyint(4) NULL DEFAULT 0 COMMENT ' 是否在线 0下线 1 上线',
  `last_login_time` datetime(0) NULL DEFAULT NULL,
  `last_out_time` datetime(0) NULL DEFAULT NULL,
  `cellphone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `power` tinyint(4) NULL DEFAULT 0 COMMENT '0 为 员工 1 为老大',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 269 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
