-- 先删库
BEGIN;
-- 先删模式， 再创建
DROP SCHEMA IF EXISTS basic CASCADE;
CREATE SCHEMA IF NOT EXISTS basic;

SET SEARCH_PATH TO basic;

DROP TABLE IF EXISTS "basic"."bs_file";
CREATE TABLE "basic"."bs_file"
(
    "id"      char(32)    COLLATE "pg_catalog"."default" NOT NULL,
    "create_user_id"      char(32)    COLLATE "pg_catalog"."default" NOT NULL,
    "name"    varchar(150) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
    "type"    varchar(25) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
    "url"    varchar(150)  COLLATE "pg_catalog"."default" DEFAULT NULL::character varying,
    "delete_flag" int2                                    NOT NULL DEFAULT 1,
    "create_time"  timestamp(6)                          NOT NULL DEFAULT now()
)
;
COMMENT ON COLUMN "basic"."bs_file"."id" IS '主键';
COMMENT ON COLUMN "basic"."bs_file"."create_user_id" IS '创建用户id';
COMMENT ON COLUMN "basic"."bs_file"."name" IS '文件名';
COMMENT ON COLUMN "basic"."bs_file"."type" IS '文件类型，文件后缀生成';
COMMENT ON COLUMN "basic"."bs_file"."url" IS '存储地址';
COMMENT ON COLUMN "basic"."bs_file"."delete_flag" IS '是否删除，0-被删除';
COMMENT ON COLUMN "basic"."bs_file"."create_time" IS '创建时间';
COMMENT ON TABLE "basic"."bs_file" IS '文件表';


DROP TABLE IF EXISTS "basic"."bs_user";
CREATE TABLE "basic"."bs_user"
(
    "id"                varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL,
    "login_name"        varchar(50) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
    "login_pass"        varchar(50) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
    "salt"              varchar(50) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
    "nick_name"         varchar(64) COLLATE "pg_catalog"."default"          DEFAULT NULL::character varying,
    "user_email"        varchar(100) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying,
    "user_pic"          varchar(255) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying,
    "memo"              varchar(255) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying,
    "last_login_date"   timestamp(6)                                        DEFAULT NULL::timestamp without time zone,
    "last_login_ip"     varchar(15) COLLATE "pg_catalog"."default"          DEFAULT NULL::character varying,
    "delete_flag"       int2                                       NOT NULL DEFAULT 1,
    "create_time"       timestamp(6)                               NOT NULL DEFAULT now(),
    "update_time"       timestamp(6)                                        DEFAULT NULL::timestamp without time zone
)
;
COMMENT ON COLUMN "basic"."bs_user"."id" IS '主键';
COMMENT ON COLUMN "basic"."bs_user"."login_name" IS '登录名';
COMMENT ON COLUMN "basic"."bs_user"."login_pass" IS '密码';
COMMENT ON COLUMN "basic"."bs_user"."salt" IS '加密盐值';
COMMENT ON COLUMN "basic"."bs_user"."nick_name" IS '昵称';
COMMENT ON COLUMN "basic"."bs_user"."user_email" IS '用户邮箱';
COMMENT ON COLUMN "basic"."bs_user"."user_pic" IS '用户图片url';
COMMENT ON COLUMN "basic"."bs_user"."memo" IS '描述';
COMMENT ON COLUMN "basic"."bs_user"."last_login_date" IS '最后登录时间';
COMMENT ON COLUMN "basic"."bs_user"."last_login_ip" IS '最后登录ip';
COMMENT ON COLUMN "basic"."bs_user"."delete_flag" IS '是否已删除 0：否 1：是';
COMMENT ON COLUMN "basic"."bs_user"."create_time" IS '创建时间';
COMMENT ON COLUMN "basic"."bs_user"."update_time" IS '更新时间';
COMMENT ON TABLE "basic"."bs_user" IS '用户表';


DROP TABLE IF EXISTS "basic"."bs_user_location";
CREATE TABLE "basic"."bs_user_location"
(
    "id"             varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL,
    "user_id"        varchar(32) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
    "longitude"        numeric(18,15),
    "latitude"         numeric(18,15),
    "accuracy"         decimal(12,6),
    "provider"        varchar(100) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying,
    "satellites"          int2          NOT NULL DEFAULT 1,
    "create_time"       timestamp(6)                                        NOT NULL DEFAULT now(),
    "update_time"       timestamp(6)                                        NOT NULL DEFAULT now()
)
;
COMMENT ON COLUMN "basic"."bs_user_location"."id" IS '主键id';
COMMENT ON COLUMN "basic"."bs_user_location"."user_id" IS '用户 id';
COMMENT ON COLUMN "basic"."bs_user_location"."longitude" IS '精度';
COMMENT ON COLUMN "basic"."bs_user_location"."latitude" IS '纬度';
COMMENT ON COLUMN "basic"."bs_user_location"."accuracy" IS '精度';
COMMENT ON COLUMN "basic"."bs_user_location"."provider" IS '提供方';
COMMENT ON COLUMN "basic"."bs_user_location"."satellites" IS '星级';
COMMENT ON COLUMN "basic"."bs_user_location"."create_time" IS '创建时间';
COMMENT ON COLUMN "basic"."bs_user_location"."update_time" IS '更新时间';
COMMENT ON TABLE "basic"."bs_user_location" IS '用户定位表';

END;
