-- PERMISSION TABLE
INSERT INTO PERMISSION (NAME) VALUES ('administrator');
INSERT INTO PERMISSION (NAME) VALUES ('customer');

-- SYS_USER TABLE
INSERT INTO SYS_USER (USER_NAME, PASSWORD, FK_PERMISSION) VALUES ('admin', 'admin', 1);
INSERT INTO SYS_USER (USER_NAME, PASSWORD, FK_PERMISSION) VALUES ('jane', 'jane', 2);