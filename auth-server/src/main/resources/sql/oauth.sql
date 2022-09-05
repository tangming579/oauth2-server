/*
 Navicat Premium Data Transfer

 Source Server         : localhost-posgresql
 Source Server Type    : PostgreSQL
 Source Server Version : 130007
 Source Host           : localhost:5432
 Source Catalog        : paas_base_auth_server
 Source Schema         : public

 Target Server Type    : PostgreSQL
 Target Server Version : 130007
 File Encoding         : 65001

 Date: 05/09/2022 18:24:57
*/


-- ----------------------------
-- Table structure for oauth_authority
-- ----------------------------
DROP TABLE IF EXISTS "public"."oauth_authority";
CREATE TABLE "public"."oauth_authority" (
  "id" int8 NOT NULL DEFAULT nextval('oauth_authority_id_seq'::regclass),
  "target_id" varchar(60) COLLATE "pg_catalog"."default",
  "methods" varchar(60) COLLATE "pg_catalog"."default",
  "paths" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "public"."oauth_authority"."target_id" IS '目标应用id';
COMMENT ON COLUMN "public"."oauth_authority"."methods" IS '目标应用授权的方法，多个方法用‘,’隔开';
COMMENT ON COLUMN "public"."oauth_authority"."paths" IS '目标应用授权的路径，多个路径用‘,’隔开';

-- ----------------------------
-- Table structure for oauth_client_authority_rel
-- ----------------------------
DROP TABLE IF EXISTS "public"."oauth_client_authority_rel";
CREATE TABLE "public"."oauth_client_authority_rel" (
  "id" int8 NOT NULL DEFAULT nextval('oauth_client_authority_rel_id_seq'::regclass),
  "client_id" varchar(60) COLLATE "pg_catalog"."default",
  "authority_id" int8
)
;
COMMENT ON COLUMN "public"."oauth_client_authority_rel"."client_id" IS '应用id';
COMMENT ON COLUMN "public"."oauth_client_authority_rel"."authority_id" IS '权限id';

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS "public"."oauth_client_details";
CREATE TABLE "public"."oauth_client_details" (
  "client_id" varchar(60) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
  "client_secret" varchar(256) COLLATE "pg_catalog"."default" NOT NULL,
  "client_name" varchar(256) COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
  "access_token_validity_seconds" int4,
  "client_desc" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "public"."oauth_client_details"."client_id" IS 'Oauth2 client_id';
COMMENT ON COLUMN "public"."oauth_client_details"."client_secret" IS 'Oauth2 client_secret';
COMMENT ON COLUMN "public"."oauth_client_details"."client_name" IS '应用名称';
COMMENT ON COLUMN "public"."oauth_client_details"."access_token_validity_seconds" IS '可选，access_token的有效时间值(单位:秒)，不填写框架(类refreshTokenValiditySeconds)默认12小时';
COMMENT ON COLUMN "public"."oauth_client_details"."client_desc" IS '应用描述信息';
COMMENT ON COLUMN "public"."oauth_client_details"."create_time" IS '创建时间';

-- ----------------------------
-- Table structure for oauth_client_keypair
-- ----------------------------
DROP TABLE IF EXISTS "public"."oauth_client_keypair";
CREATE TABLE "public"."oauth_client_keypair" (
  "client_id" varchar(60) COLLATE "pg_catalog"."default" NOT NULL,
  "public_key" varchar(512) COLLATE "pg_catalog"."default",
  "private_key" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Primary Key structure for table oauth_authority
-- ----------------------------
ALTER TABLE "public"."oauth_authority" ADD CONSTRAINT "oauth_authority_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table oauth_client_authority_rel
-- ----------------------------
ALTER TABLE "public"."oauth_client_authority_rel" ADD CONSTRAINT "oauth_client_authority_rel_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table oauth_client_details
-- ----------------------------
ALTER TABLE "public"."oauth_client_details" ADD CONSTRAINT "oauth_client_details_pkey" PRIMARY KEY ("client_id");

-- ----------------------------
-- Primary Key structure for table oauth_client_keypair
-- ----------------------------
ALTER TABLE "public"."oauth_client_keypair" ADD CONSTRAINT "oauth_client_keypair_pkey" PRIMARY KEY ("client_id");
