ALTER SESSION SET "_ORACLE_SCRIPT"=true;
DROP USER FIAP CASCADE;
CREATE USER FIAP IDENTIFIED BY FIAP;
GRANT CONNECT, RESOURCE TO FIAP;
GRANT CREATE SESSION, CREATE TABLE, CREATE VIEW, CREATE PROCEDURE, CREATE SEQUENCE, CREATE TRIGGER to FIAP;
GRANT ALTER ANY TABLE, ALTER ANY PROCEDURE, ALTER ANY TRIGGER to FIAP;
GRANT DELETE ANY TABLE to FIAP;
GRANT DROP ANY PROCEDURE to FIAP;
GRANT DROP ANY TRIGGER to FIAP;
GRANT DROP ANY VIEW to FIAP;
ALTER USER FIAP quota unlimited on USERS;
ALTER SESSION SET "_ORACLE_SCRIPT"=false;