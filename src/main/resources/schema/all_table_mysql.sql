alter table house_work drop foreign key FKbjoc5vrx6bavg9c2dk1iexnl
alter table house_work drop foreign key FK9w5gyueejx4gjxr6folh04qvk
alter table house_work drop foreign key FK8dhlw89swuy8mpjw5rvn72eha
alter table member_role drop foreign key FKjkor8coasvpxd945p9qssuny4
alter table ziphap_group drop foreign key FK9tkqtci4kcbssvksao5j8kman
alter table ziphap_letter drop foreign key FK6aiw45f5qjnlld3ewy3u1du6e
alter table ziphap_letter drop foreign key FKhh4v3krxeohkpjae9yk2e8lcc
alter table ziphap_member drop foreign key FKt0cfjtahb47rrb384yui9oqns
drop table if exists cycle
drop table if exists house_work
drop table if exists member_role
drop table if exists ziphap_group
drop table if exists ziphap_heart
drop table if exists ziphap_letter
drop table if exists ziphap_member
create table cycle (cycle_id varchar(255) not null, created_at datetime not null, updated_at datetime, cycle_type varchar(255), end_at date, props varchar(255), start_at date, primary key (cycle_id)) engine=InnoDB
create table house_work (house_work_id varchar(255) not null, created_at datetime not null, updated_at datetime, difficulty varchar(255), house_work_status_type varchar(255), is_cycle bit, title varchar(255), today date, cycle_id varchar(255), zip_hap_group_id varchar(255), member_id varchar(255), primary key (house_work_id)) engine=InnoDB
create table member_role (id varchar(255) not null, member_role varchar(255)) engine=InnoDB
create table ziphap_group (zip_hap_group_id varchar(255) not null, created_at datetime not null, updated_at datetime, group_name varchar(255), link_id varchar(255), member_id varchar(255), primary key (zip_hap_group_id)) engine=InnoDB
create table ziphap_heart (heart_id varchar(255) not null, created_at datetime not null, updated_at datetime, is_create_all_members bit, primary key (heart_id)) engine=InnoDB
create table ziphap_letter (letter_id varchar(255) not null, created_at datetime not null, updated_at datetime, content varchar(255), heart_type varchar(255), letter_from varchar(255), letter_to varchar(255), title varchar(255), zip_hap_group_id varchar(255), heart_id varchar(255), primary key (letter_id)) engine=InnoDB
create table ziphap_member (member_id varchar(255) not null, created_at datetime not null, updated_at datetime, member_email varchar(30), member_login_role varchar(255), member_name varchar(255), member_profile_picture varchar(255), zip_hap_group_id varchar(255), primary key (member_id)) engine=InnoDB
alter table house_work add constraint FKbjoc5vrx6bavg9c2dk1iexnl foreign key (cycle_id) references cycle (cycle_id)
alter table house_work add constraint FK9w5gyueejx4gjxr6folh04qvk foreign key (zip_hap_group_id) references ziphap_group (zip_hap_group_id)
alter table house_work add constraint FK8dhlw89swuy8mpjw5rvn72eha foreign key (member_id) references ziphap_member (member_id)
alter table member_role add constraint FKjkor8coasvpxd945p9qssuny4 foreign key (id) references ziphap_member (member_id)
alter table ziphap_group add constraint FK9tkqtci4kcbssvksao5j8kman foreign key (member_id) references ziphap_member (member_id)
alter table ziphap_letter add constraint FK6aiw45f5qjnlld3ewy3u1du6e foreign key (zip_hap_group_id) references ziphap_group (zip_hap_group_id)
alter table ziphap_letter add constraint FKhh4v3krxeohkpjae9yk2e8lcc foreign key (heart_id) references ziphap_heart (heart_id)
alter table ziphap_member add constraint FKt0cfjtahb47rrb384yui9oqns foreign key (zip_hap_group_id) references ziphap_group (zip_hap_group_id)