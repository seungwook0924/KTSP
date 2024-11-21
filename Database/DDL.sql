DROP TABLE IF EXISTS users CASCADE;

DROP TYPE IF EXISTS role CASCADE;
DROP TYPE IF EXISTS level CASCADE;

-- ENUM 타입 정의
CREATE TYPE role AS ENUM ('user', 'admin');
CREATE TYPE level AS ENUM ('freshman', 'sophomore', 'junior', 'senior', 'etc');

-- users 테이블 생성
CREATE TABLE users (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    role role NOT NULL,
    student_number CHAR(9) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    level level NOT NULL,
    name VARCHAR(10) NOT NULL,
    tel CHAR(13) NOT NULL,
    major VARCHAR(20) NOT NULL,
    last_grades DECIMAL NOT NULL
);