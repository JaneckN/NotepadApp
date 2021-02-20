drop table if exists notes;
create table if not exists notes (
                       id   INTEGER AUTO_INCREMENT,
                       date timestamp,
                       text varchar(255),
                       title varchar(255),
                       primary key (id));

