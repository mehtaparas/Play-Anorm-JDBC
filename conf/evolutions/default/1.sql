# --- First database schema

# --- !Ups


create table customers (
  accountnbr                        bigint not null,
  siteId                            bigint not null,
  video                             bigint not null,
  voice                             bigint not null,
  data                              bigint not null,
  constraint pk_account primary key (accountnbr))
;

insert into customers (accountnbr, siteId, video, voice, data) values (1, 123, 1, 0, 1);
insert into customers (accountnbr, siteId, video, voice, data) values (2, 123, 1, 0, 1);
insert into customers (accountnbr, siteId, video, voice, data) values (3, 345, 1, 1, 1);
insert into customers (accountnbr, siteId, video, voice, data) values (4, 567, 0, 0, 1);
insert into customers (accountnbr, siteId, video, voice, data) values (5, 789, 0, 0, 1);
