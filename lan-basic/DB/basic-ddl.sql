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
    "delete_flag" int2                                    NOT NULL DEFAULT 0,
    "create_time"  timestamp(6)                          NOT NULL DEFAULT now()
)
;
COMMENT ON COLUMN "basic"."bs_file"."id" IS '主键';
COMMENT ON COLUMN "basic"."bs_file"."create_user_id" IS '创建用户id';
COMMENT ON COLUMN "basic"."bs_file"."name" IS '文件名';
COMMENT ON COLUMN "basic"."bs_file"."type" IS '文件类型，文件后缀生成';
COMMENT ON COLUMN "basic"."bs_file"."url" IS '存储地址';
COMMENT ON COLUMN "basic"."bs_file"."delete_flag" IS '是否删除，1-被删除';
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
    "delete_flag"       int2                                       NOT NULL DEFAULT 0,
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
    "country"             varchar(32) COLLATE "pg_catalog"."default"  DEFAULT NULL,
    "formatted_address"             varchar(32) COLLATE "pg_catalog"."default"  DEFAULT NULL,
    "province"             varchar(32) COLLATE "pg_catalog"."default"  DEFAULT NULL,
    "city"             varchar(32) COLLATE "pg_catalog"."default"  DEFAULT NULL,
    "city_code"             varchar(32) COLLATE "pg_catalog"."default"  DEFAULT NULL,
    "district"             varchar(32) COLLATE "pg_catalog"."default"  DEFAULT NULL,
    "ad_code"             varchar(32) COLLATE "pg_catalog"."default"  DEFAULT NULL,
    "town_code"             varchar(32) COLLATE "pg_catalog"."default"  DEFAULT NULL,
    "town_ship"             varchar(32) COLLATE "pg_catalog"."default"  DEFAULT NULL,
    "street"             varchar(32) COLLATE "pg_catalog"."default"  DEFAULT NULL,
    "number"             varchar(32) COLLATE "pg_catalog"."default"  DEFAULT NULL,
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


DROP TABLE IF EXISTS "basic"."oauth_client_details";
CREATE TABLE "basic"."oauth_client_details"
(
    "client_id"               varchar(128) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
    "resource_ids"            varchar(256) COLLATE "pg_catalog"."default"          DEFAULT NULL::character varying,
    "client_secret"           varchar(256) COLLATE "pg_catalog"."default"          DEFAULT NULL::character varying,
    "scope"                   varchar(256) COLLATE "pg_catalog"."default"          DEFAULT NULL::character varying,
    "authorized_grant_types"  varchar(256) COLLATE "pg_catalog"."default"          DEFAULT NULL::character varying,
    "web_server_redirect_uri" varchar(256) COLLATE "pg_catalog"."default"          DEFAULT NULL::character varying,
    "authorities"             varchar(256) COLLATE "pg_catalog"."default"          DEFAULT NULL::character varying,
    "access_token_validity"   int4                                                 DEFAULT NULL,
    "refresh_token_validity"  int4                                                 DEFAULT NULL,
    "additional_information"  varchar(4096) COLLATE "pg_catalog"."default"         DEFAULT NULL::character varying,
    "autoapprove"             varchar(256) COLLATE "pg_catalog"."default"          DEFAULT NULL::character varying
)
;

ALTER TABLE "basic"."oauth_client_details"
    ADD CONSTRAINT "oauth_client_details_pkey" PRIMARY KEY ("client_id");

DROP TABLE IF EXISTS "basic"."bs_menu";
CREATE TABLE "basic"."bs_menu"
(
    "id"             char(32) COLLATE "pg_catalog"."default"     NOT NULL,
    "menu_code"      varchar(90) COLLATE "pg_catalog"."default"  NOT NULL DEFAULT NULL::character varying,
    "menu_name"      varchar(128) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
    "menu_icon"    varchar(50) COLLATE "pg_catalog"."default"           DEFAULT NULL::character varying,
    "menu_path"    varchar(50) COLLATE "pg_catalog"."default"           DEFAULT NULL::character varying,
    "is_menu"        int2                                        NOT NULL DEFAULT 1,
    "is_show"          int2                                        NOT NULL DEFAULT 1,
    "sort"           int8                                                 DEFAULT NULL,
    "create_time"    timestamp(6)                                NOT NULL DEFAULT now(),
    "update_time"    timestamp(6)                                         DEFAULT NULL::timestamp without time zone
)
;
COMMENT ON COLUMN "basic"."bs_menu"."id" IS '主键';
COMMENT ON COLUMN "basic"."bs_menu"."menu_code" IS '菜单编码';
COMMENT ON COLUMN "basic"."bs_menu"."menu_name" IS '菜单名';
COMMENT ON COLUMN "basic"."bs_menu"."menu_icon" IS '菜单图标';
COMMENT ON COLUMN "basic"."bs_menu"."menu_path" IS '菜单路径';
COMMENT ON COLUMN "basic"."bs_menu"."is_menu" IS '是否菜单';
COMMENT ON COLUMN "basic"."bs_menu"."sort" IS '排序码';
COMMENT ON COLUMN "basic"."bs_menu"."is_show" IS '是否展示';
COMMENT ON COLUMN "basic"."bs_menu"."create_time" IS '创建时间';
COMMENT ON COLUMN "basic"."bs_menu"."update_time" IS '更新时间';
COMMENT ON TABLE "basic"."bs_menu" IS '菜单表';

ALTER TABLE "basic"."bs_menu"
    ADD CONSTRAINT "bs_menu" PRIMARY KEY ("id");

DROP TABLE IF EXISTS "basic"."bs_user_menu";
CREATE TABLE "basic"."bs_user_menu"
(
    "id"             char(32) COLLATE "pg_catalog"."default"    NOT NULL,
    "user_id"        char(32) COLLATE "pg_catalog"."default"    NOT NULL DEFAULT NULL::bpchar,
    "menu_code"      varchar(90) COLLATE "pg_catalog"."default" NOT NULL DEFAULT NULL::character varying,
    "create_time"    timestamp(6)                               NOT NULL DEFAULT now()
)
;
COMMENT ON COLUMN "basic"."bs_user_menu"."id" IS '主键';
COMMENT ON COLUMN "basic"."bs_user_menu"."user_id" IS '用户ID';
COMMENT ON COLUMN "basic"."bs_user_menu"."menu_code" IS '菜单编码';
COMMENT ON COLUMN "basic"."bs_user_menu"."create_time" IS '创建时间';
COMMENT ON TABLE "basic"."bs_user_menu" IS '用户菜单表';

ALTER TABLE "basic"."bs_user_menu"
    ADD CONSTRAINT "bs_user_menu" PRIMARY KEY ("id");


END;
