create table replayka.social_login
(
    id     serial
        constraint social_login_pk
            primary key,
    name   character varying(128),
    source character varying(20)
);

create index social_login_name_source_index on replayka.social_login (name, source);

create table replayka.blog_ownership
(
    login_id integer
        constraint blog_ownership_social_login_fk
            references replayka.social_login (id),
    blog_id  integer
        constraint blog_ownership_blog_fk
            references replayka.blog (blog_id)
);