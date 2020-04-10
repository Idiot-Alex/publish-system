/*用户信息表*/
DROP TABLE IF EXISTS t_user;

CREATE TABLE t_user(
user_id bigint NOT NULL   COMMENT '用户编号',
user_name varchar(255) NOT NULL   COMMENT '用户名称',
user_password varchar(255) NOT NULL   COMMENT '用户密码',
create_time datetime   COMMENT '创建时间',
update_time datetime   COMMENT '修改时间',
create_user bigint   COMMENT '创建用户',
update_user bigint   COMMENT '修改用户',
status int NOT NULL  DEFAULT '1'  COMMENT '状态',
PRIMARY KEY (user_id)
);

/*终端信息表*/
DROP TABLE IF EXISTS t_agent;

CREATE TABLE t_agent(
agent_id bigint NOT NULL   COMMENT '终端 ID',
agent_name varchar(255) NOT NULL   COMMENT '终端名称',
agent_code varchar(255) NOT NULL   COMMENT '终端 CODE',
heartbeat_frequency int  DEFAULT '60'  COMMENT '心跳频率(秒)',
last_heartbeat_time datetime   COMMENT '上次心跳时间',
online_status int  DEFAULT '0'  COMMENT '离线天数',
create_time datetime   COMMENT '创建时间',
update_time datetime   COMMENT '修改时间',
create_user bigint   COMMENT '创建用户',
update_user bigint   COMMENT '修改用户',
status int NOT NULL  DEFAULT '1'  COMMENT '状态',
PRIMARY KEY (agent_id)
);

/*用户终端关联信息表*/
DROP TABLE IF EXISTS t_user_agent;

CREATE TABLE t_user_agent(
user_agent_id bigint NOT NULL   COMMENT '关联 ID',
user_id bigint NOT NULL   COMMENT '用户 ID',
agent_id bigint NOT NULL   COMMENT '终端 ID',
create_time datetime   COMMENT '创建时间',
update_time datetime   COMMENT '修改时间',
create_user bigint   COMMENT '创建用户',
update_user bigint   COMMENT '修改用户',
status int NOT NULL  DEFAULT '1'  COMMENT '状态',
PRIMARY KEY (user_agent_id)
);

/*目录信息表*/
DROP TABLE IF EXISTS t_directory;

CREATE TABLE t_directory(
directory_id bigint NOT NULL   COMMENT '目录 ID',
directory_name varchar(255) NOT NULL   COMMENT '目录名称',
parent_directory_id bigint   COMMENT '父级目录 ID',
root_flag int  DEFAULT '1'  COMMENT '是否是根节点',
path_code varchar(255)   COMMENT '目录编码',
create_time datetime   COMMENT '创建时间',
update_time datetime   COMMENT '修改时间',
create_user bigint   COMMENT '创建用户',
update_user bigint   COMMENT '修改用户',
status int NOT NULL  DEFAULT '1'  COMMENT '状态',
PRIMARY KEY (directory_id)
);

/*文件信息表*/
DROP TABLE IF EXISTS t_file;

CREATE TABLE t_file(
file_id bigint NOT NULL   COMMENT '文件 ID',
directory_id bigint NOT NULL   COMMENT '目录 ID',
file_name varchar(255)   COMMENT '文件名称',
old_name varchar(255)   COMMENT '源文件名称',
file_type int   COMMENT '文件类型(1: 图片, 2: 视频)',
file_path varchar(255)   COMMENT '文件访问路径',
file_size bigint   COMMENT '文件大小 byte',
create_time datetime   COMMENT '创建时间',
update_time datetime   COMMENT '修改时间',
create_user bigint   COMMENT '创建用户',
update_user bigint   COMMENT '修改用户',
status int NOT NULL  DEFAULT '1'  COMMENT '状态',
PRIMARY KEY (file_id)
);

/*文稿信息表*/
DROP TABLE IF EXISTS t_article;

CREATE TABLE t_article(
article_id bigint NOT NULL   COMMENT '文稿 ID',
title varchar(255)   COMMENT '标题',
cover_image varchar(255)   COMMENT '封面图片',
content blob   COMMENT '富文本内容',
edit_status int  DEFAULT '0'  COMMENT '文稿状态(0: 未完成, 1: 已完成, 2: 已撤销)',
create_time datetime   COMMENT '创建时间',
update_time datetime   COMMENT '修改时间',
create_user bigint   COMMENT '创建用户',
update_user bigint   COMMENT '修改用户',
status int NOT NULL  DEFAULT '1'  COMMENT '状态',
PRIMARY KEY (article_id)
);

/*终端播单信息表*/
DROP TABLE IF EXISTS t_agent_playlist;

CREATE TABLE t_agent_playlist(
list_id bigint NOT NULL   COMMENT '播单 ID',
agent_id bigint NOT NULL   COMMENT '终端 ID',
article_id bigint NOT NULL   COMMENT '文稿 ID',
serial_number int   COMMENT '序号',
create_time datetime   COMMENT '创建时间',
update_time datetime   COMMENT '修改时间',
create_user bigint   COMMENT '创建用户',
update_user bigint   COMMENT '修改用户',
status int NOT NULL  DEFAULT '1'  COMMENT '状态',
PRIMARY KEY (list_id)
);


