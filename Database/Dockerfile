# PostgreSQL 15 버전을 베이스 이미지로 사용
FROM postgres:14.13

# init.sql 파일을 /docker-entrypoint-initdb.d/로 복사
COPY ./init.sql /docker-entrypoint-initdb.d/

# PostgreSQL 기본 포트 5432 노출
EXPOSE 5432