-- create schema replayka;
-- alter schema replayka owner to quarkus;

create table replayka.blog
(
    blog_id   serial
        constraint blog_pk
            primary key,
    blog_name varchar(100) not null,
    blog_key  varchar(10)  not null
        constraint blog_key_uq
            unique,
    blog_misc jsonb
);

create index blog_blog_key_index on replayka.blog (blog_key);

create table replayka.request
(
    blog_id      integer not null
        constraint request_blog_fk references replayka.blog,
    request_uuid uuid    not null
        constraint request_pk primary key,
    time         timestamp default CURRENT_TIMESTAMP,
    info         jsonb,
    page         varchar(75)
);

comment
on column replayka.request.info is 'Headers / ip / all the strictly-legal tracking stuff goes here ';

create table replayka.response
(
    request_uuid uuid not null
        constraint response_request_fk references replayka.request,
    response_ts  timestamp default CURRENT_TIMESTAMP,
    score        integer,
    comment      varchar(500)
);