create type attendance_type as enum ('enter', 'exit');

create table Users
(
    id   int          not null generated by default as identity primary key,
    name varchar(255) not null
);

create table Subscriptions
(
    id         int  not null generated by default as identity primary key,
    user_id    int  not null,
    start_date date,
    due_date   date not null
);

create table Attendances
(
    user_id int             not null,
    kind    attendance_type not null,
    time_   timestamp       not null
);