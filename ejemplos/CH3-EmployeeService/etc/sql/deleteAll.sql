DELETE FROM SYS_USER;
ALTER TABLE SYS_USER ALTER COLUMN ID RESTART WITH 1;

DELETE FROM PERMISSION;
ALTER TABLE PERMISSION ALTER COLUMN ID RESTART WITH 1;

DELETE FROM EMPLOYEE;
ALTER TABLE EMPLOYEE ALTER COLUMN ID RESTART WITH 1;

DELETE FROM PARCEL;
ALTER TABLE PARCEL ALTER COLUMN ID RESTART WITH 1;