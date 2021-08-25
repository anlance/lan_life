-- 先删库
BEGIN;
-- 先删模式， 再创建
DROP SCHEMA IF EXISTS demo CASCADE;
CREATE SCHEMA IF NOT EXISTS demo;

SET SEARCH_PATH TO demo;

DROP TABLE IF EXISTS "demo"."bs_file";
CREATE TABLE "demo"."bs_file"
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
COMMENT ON COLUMN "demo"."bs_file"."id" IS '主键';
COMMENT ON COLUMN "demo"."bs_file"."create_user_id" IS '创建用户id';
COMMENT ON COLUMN "demo"."bs_file"."name" IS '文件名';
COMMENT ON COLUMN "demo"."bs_file"."type" IS '文件类型，文件后缀生成';
COMMENT ON COLUMN "demo"."bs_file"."url" IS '存储地址';
COMMENT ON COLUMN "demo"."bs_file"."delete_flag" IS '是否删除，0-被删除';
COMMENT ON COLUMN "demo"."bs_file"."create_time" IS '创建时间';
COMMENT ON TABLE "demo"."bs_file" IS '文件表';

END;
