create table book
(
    id          bigserial primary key,
    title       varchar(100) not null,
    description varchar(100) not null
);
