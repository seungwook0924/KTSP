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
CREATE TYPE type AS ENUM ('mentor', 'mentee', 'major1', 'major2', 'major3', 'project_contest', 'notice', 'report');

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
                        updated_at TIMESTAMP NULL,
                        title VARCHAR(20) NOT NULL,
                        content TEXT NOT NULL,
                        is_closed BOOLEAN NOT NULL,
    hits BIGINT NOT NULL,
    team_size SMALLINT NOT NULL
);

-- teams 테이블
CREATE TABLE teams (
                       id BIGSERIAL PRIMARY KEY,
                       board_id BIGINT NOT NULL REFERENCES boards(id) ON DELETE CASCADE,
                       is_valid BOOLEAN NOT NULL,
                       user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- waiting 테이블
CREATE TABLE waiting (
                         id BIGSERIAL PRIMARY KEY,
                         board_id BIGINT NOT NULL REFERENCES boards(id) ON DELETE CASCADE,
                         user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    is_valid BOOLEAN NOT NULL,
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
CREATE TABLE file_mapping (
                              id BIGSERIAL PRIMARY KEY,
                              board_id BIGINT REFERENCES boards(id) ON DELETE CASCADE,
                              file_name VARCHAR(50) NOT NULL,
                              uuid CHAR(36) NOT NULL,
                              path VARCHAR(255) NOT NULL,
                              extension VARCHAR(20) NOT NULL
);

INSERT INTO users VALUES
    (1, 'admin', 201921321, '$2a$10$kBQhpciRe.xAY1MpT6OKveoF5d5SVoc2/HzSG9peXoY9uYJo.pqYK', 'senior', '이승욱', '010-5325-2904', 'AI소프트웨어', 4.2, 'lso_0924@kangwon.ac.kr');
