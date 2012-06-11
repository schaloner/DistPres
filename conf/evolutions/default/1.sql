# --- !Ups

create table binary_content (
  id                        bigint not null,
  bytes                     bytea not null,
  constraint pk_binary_content primary key (id))
;

create table image (
  id                        bigint not null,
  presentation_id           bigint not null,
  name                      varchar(255) not null,
  binary_content_id         bigint,
  constraint pk_image primary key (id))
;

create table presentation (
  id                        bigint not null,
  name                      varchar(255) not null,
  current_position          integer not null,
  upload_date               timestamp not null,
  constraint pk_presentation primary key (id))
;

create sequence binary_content_seq;

create sequence image_seq;

create sequence presentation_seq;

alter table image add constraint fk_image_presentation_1 foreign key (presentation_id) references presentation (id);
create index ix_image_presentation_1 on image (presentation_id);
alter table image add constraint fk_image_binaryContent_2 foreign key (binary_content_id) references binary_content (id);
create index ix_image_binaryContent_2 on image (binary_content_id);



# --- !Downs

drop table if exists binary_content cascade;

drop table if exists image cascade;

drop table if exists presentation cascade;

drop sequence if exists binary_content_seq;

drop sequence if exists image_seq;

drop sequence if exists presentation_seq;

