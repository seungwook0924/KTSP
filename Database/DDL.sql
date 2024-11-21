-- 기존 테이블 삭제 (존재 시)
DROP TABLE IF EXISTS report_comments CASCADE;
DROP TABLE IF EXISTS reports CASCADE;
DROP TABLE IF EXISTS file_mapping CASCADE;
DROP TABLE IF EXISTS files CASCADE;
DROP TABLE IF EXISTS notice CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS boards CASCADE;
DROP TABLE IF EXISTS waiting CASCADE;
DROP TABLE IF EXISTS teams CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 기존 ENUM 타입 삭제 (존재 시)
DROP TYPE IF EXISTS role CASCADE;
DROP TYPE IF EXISTS level CASCADE;
DROP TYPE IF EXISTS type CASCADE;

-- ENUM 타입 정의
CREATE TYPE role AS ENUM ('admin', 'user');
CREATE TYPE level AS ENUM ('freshman', 'sophomore', 'junior', 'senior', 'etc');
CREATE TYPE type AS ENUM ('learning1', 'learning2', 'major1', 'major2', 'major3', 'challenge');

-- users 테이블
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       role role NOT NULL,
                       student_number CHAR(9) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       level level NOT NULL,
                       name VARCHAR(10) NOT NULL,
                       tel CHAR(13) NOT NULL,
                       major VARCHAR(20) NOT NULL,
                       last_grades DECIMAL NOT NULL,
                       email VARCHAR(50) NOT NULL UNIQUE
);

-- boards 테이블
CREATE TABLE boards (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        type type NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP NOT NULL,
                        title VARCHAR(50) NOT NULL,
                        content TEXT NOT NULL,
                        is_closed BOOLEAN NOT NULL
);

-- teams 테이블
CREATE TABLE teams (
                       id BIGSERIAL PRIMARY KEY,
                       board_id BIGINT NOT NULL REFERENCES boards(id) ON DELETE CASCADE,
                       user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- waiting 테이블
CREATE TABLE waiting (
                         id BIGSERIAL PRIMARY KEY,
                         board_id BIGINT NOT NULL REFERENCES boards(id) ON DELETE CASCADE,
                         user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                         content VARCHAR(255) NOT NULL
);

-- comments 테이블
CREATE TABLE comments (
                          id BIGSERIAL PRIMARY KEY,
                          user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          board_id BIGINT NOT NULL REFERENCES boards(id) ON DELETE CASCADE,
                          parent_comment_id BIGINT REFERENCES comments(id) ON DELETE CASCADE,
                          comment VARCHAR(100) NOT NULL,
                          created_at TIMESTAMP NOT NULL
);

-- files 테이블
CREATE TABLE files (
                       id BIGSERIAL PRIMARY KEY,
                       file_name VARCHAR(50) NOT NULL,
                       uuid CHAR(36) NOT NULL,
                       path VARCHAR(255) NOT NULL
);

-- reports 테이블
CREATE TABLE reports (
                         id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                         title VARCHAR(50) NOT NULL,
                         content VARCHAR(255) NOT NULL,
                         created_at TIMESTAMP NOT NULL
);

-- file_mapping 테이블
CREATE TABLE file_mapping (
                              id BIGSERIAL PRIMARY KEY,
                              board_id BIGINT REFERENCES boards(id) ON DELETE CASCADE,
                              report_id BIGINT REFERENCES reports(id) ON DELETE CASCADE
);

-- report_comments 테이블
CREATE TABLE report_comments (
                                 id BIGSERIAL PRIMARY KEY,
                                 report_id BIGINT NOT NULL REFERENCES reports(id) ON DELETE CASCADE,
                                 comment VARCHAR(255) NOT NULL,
                                 created_at TIMESTAMP NOT NULL
);

-- notice 테이블
CREATE TABLE notice (
                        id BIGSERIAL PRIMARY KEY,
                        user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        title VARCHAR(50) NOT NULL,
                        content TEXT NOT NULL
);