BEGIN;
-- 切换模式
SET SEARCH_PATH TO basic;

INSERT INTO "basic"."oauth_client_details" ("client_id", "resource_ids", "client_secret", "scope", "authorized_grant_types", "web_server_redirect_uri", "authorities", "access_token_validity", "refresh_token_validity", "additional_information", "autoapprove")
VALUES ('browser', null, '$2a$10$vLnjaOdvE.3ba9bU7LSAMeMQ3LkNaAyAoIFdEsmyVRX6KYlAALV0u', 'ui', 'password', null, null, '604800', '1209600', null, null);
INSERT INTO "basic"."oauth_client_details" ("client_id", "resource_ids", "client_secret", "scope", "authorized_grant_types", "web_server_redirect_uri", "authorities", "access_token_validity", "refresh_token_validity", "additional_information", "autoapprove")
VALUES ('basic', null, '$2a$10$hw8KwUqlCtH9j0J3Br6//.xmoGeT/HDyAekl9VTDuh6TOjWapzJr6', 'server', 'client_credentials', null, null, '604800', '1209600', null, null);
INSERT INTO "basic"."oauth_client_details" ("client_id", "resource_ids", "client_secret", "scope", "authorized_grant_types", "web_server_redirect_uri", "authorities", "access_token_validity", "refresh_token_validity", "additional_information", "autoapprove")
VALUES ('auth', null, '$2a$10$hw8KwUqlCtH9j0J3Br6//.xmoGeT/HDyAekl9VTDuh6TOjWapzJr6', 'server', 'client_credentials', null, null, '604800', '1209600', null, null);

-- 新增菜单
INSERT INTO "basic"."bs_menu"("id", "menu_code", "menu_name", "menu_icon", "menu_path", "is_show", "is_menu", "sort", "create_time", "update_time")
VALUES (replace(cast(public.uuid_generate_v4() as VARCHAR), '-', ''), '001', '首页', 'HomeOutlined', '/', 1, 1, 100, now(), now());
INSERT INTO "basic"."bs_menu"("id", "menu_code", "menu_name", "menu_icon", "menu_path", "is_show", "is_menu", "sort", "create_time", "update_time")
VALUES (replace(cast(public.uuid_generate_v4() as VARCHAR), '-', ''), '002', '工具', 'ToolOutlined', '/tools', 1, 1, 200, now(), now());
INSERT INTO "basic"."bs_menu"("id", "menu_code", "menu_name", "menu_icon", "menu_path", "is_show", "is_menu", "sort", "create_time", "update_time")
VALUES (replace(cast(public.uuid_generate_v4() as VARCHAR), '-', ''), '003', '文件', 'HomeOutlined', '/files', 1, 1, 300, now(), now());
INSERT INTO "basic"."bs_menu"("id", "menu_code", "menu_name", "menu_icon", "menu_path", "is_show", "is_menu", "sort", "create_time", "update_time")
VALUES (replace(cast(public.uuid_generate_v4() as VARCHAR), '-', ''), '004', '登录', 'UserOutlined', null, 1, 1, 400, now(), now());

-- 默认用户
INSERT INTO "basic"."bs_user_menu"("id", "user_id", "menu_code", "create_time")
VALUES (replace(cast(public.uuid_generate_v4() as VARCHAR), '-', ''), '1', '001', now());
INSERT INTO "basic"."bs_user_menu"("id", "user_id", "menu_code", "create_time")
VALUES (replace(cast(public.uuid_generate_v4() as VARCHAR), '-', ''), '1', '002', now());
INSERT INTO "basic"."bs_user_menu"("id", "user_id", "menu_code", "create_time")
VALUES (replace(cast(public.uuid_generate_v4() as VARCHAR), '-', ''), '1', '003', now());
INSERT INTO "basic"."bs_user_menu"("id", "user_id", "menu_code", "create_time")
VALUES (replace(cast(public.uuid_generate_v4() as VARCHAR), '-', ''), '1', '004', now());

-- 管理员用户
INSERT INTO "basic"."bs_user_menu"("id", "user_id", "menu_code", "create_time")
VALUES (replace(cast(public.uuid_generate_v4() as VARCHAR), '-', ''), 'c69932b86390d0e8fa5a1c86cb9440b3', '001', now());
INSERT INTO "basic"."bs_user_menu"("id", "user_id", "menu_code", "create_time")
VALUES (replace(cast(public.uuid_generate_v4() as VARCHAR), '-', ''), 'c69932b86390d0e8fa5a1c86cb9440b3', '002', now());
INSERT INTO "basic"."bs_user_menu"("id", "user_id", "menu_code", "create_time")
VALUES (replace(cast(public.uuid_generate_v4() as VARCHAR), '-', ''), 'c69932b86390d0e8fa5a1c86cb9440b3', '003', now());

END;