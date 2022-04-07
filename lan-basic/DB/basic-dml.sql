BEGIN;
-- 切换模式
SET SEARCH_PATH TO basic;

INSERT INTO "basic"."oauth_client_details" ("client_id", "resource_ids", "client_secret", "scope", "authorized_grant_types", "web_server_redirect_uri", "authorities", "access_token_validity", "refresh_token_validity", "additional_information", "autoapprove")
VALUES ('browser', null, '$2a$10$vLnjaOdvE.3ba9bU7LSAMeMQ3LkNaAyAoIFdEsmyVRX6KYlAALV0u', 'ui', 'password', null, null, '604800', '1209600', null, null);
INSERT INTO "basic"."oauth_client_details" ("client_id", "resource_ids", "client_secret", "scope", "authorized_grant_types", "web_server_redirect_uri", "authorities", "access_token_validity", "refresh_token_validity", "additional_information", "autoapprove")
VALUES ('basic', null, '$2a$10$hw8KwUqlCtH9j0J3Br6//.xmoGeT/HDyAekl9VTDuh6TOjWapzJr6', 'server', 'client_credentials', null, null, '604800', '1209600', null, null);
INSERT INTO "basic"."oauth_client_details" ("client_id", "resource_ids", "client_secret", "scope", "authorized_grant_types", "web_server_redirect_uri", "authorities", "access_token_validity", "refresh_token_validity", "additional_information", "autoapprove")
VALUES ('auth', null, '$2a$10$hw8KwUqlCtH9j0J3Br6//.xmoGeT/HDyAekl9VTDuh6TOjWapzJr6', 'server', 'client_credentials', null, null, '604800', '1209600', null, null);

END;