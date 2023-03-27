-- 创建库
create database if not exists `aba_api`;

-- 切换库
use aba_api;

-- 用户表
drop table if exists `user`;
create table if not exists `user`
(
    id            bigint auto_increment comment 'id' primary key,
    user_name     varchar(256)                           null comment '用户昵称',
    user_account  varchar(256)                           not null comment '账号',
    user_avatar   varchar(1024)                          null comment '用户头像',
    gender        tinyint                                null comment '性别',
    user_role     varchar(256) default 'user'            not null comment '用户角色：user / admin',
    user_password varchar(512)                           not null comment '密码',
    access_key    varchar(512) comment 'accessKey',
    secret_key    varchar(512) comment 'secretKey',
    create_time   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint      default 0                 not null comment '是否删除',
    constraint uni_userAccount
        unique (user_account)
) comment '用户';

-- 接口表
drop table if exists `interface_info`;
create table if not exists `interface_info`
(
    `id`           bigint unsigned auto_increment comment 'id' primary key,
    `name`         varchar(256) not null comment '接口名称',
    `description`  varchar(256) null comment '描述',
    `url`          varchar(512) not null comment '接口地址',
    `method`       tinyint      not null default 0 comment '请求类型',
    `content_type` tinyint      not null default 0 comment '内容类型',
    `status`       tinyint      not null default 0 comment '接口状态（0-关闭 1-开启）',
    `user_id`      bigint       not null comment '创建人',
    `create_time`  datetime              default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`  datetime              default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`    tinyint               default 0 not null comment '是否删除'
) comment '接口信息表';

-- 接口请求参数
drop table if exists `interface_param`;
create table if not exists `interface_param`
(
    `id`                bigint unsigned auto_increment comment 'id' primary key,
    `interface_info_id` bigint           not null comment '接口 id',
    `name`              varchar(64)      not null comment '参数名称',
    `type`              tinyint          not null default 0 comment '参数类型',
    `is_required`       tinyint          not null default 0 comment '是否必填',
    `max_length`        int              null comment '最大长度',
    `description`       varchar(512)     null comment '描述',
    `parent_id`         bigint unsigned  not null default 0 comment '父 id',
    `style`             tinyint unsigned not null default 0 comment '类型 0:path 1:query 2:body 3:header 4:返回参数',
    `create_time`       datetime                  default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`       datetime                  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`         tinyint                   default 0 not null comment '是否删除'
) comment '接口请求参数信息表';

-- 用户接口关系表
create table if not exists user_interface_info
(
    `id`                bigint auto_increment comment 'id' primary key,
    `user_id`           bigint                             not null comment '调用用户',
    `interface_info_id` bigint                             not null comment '接口信息',
    `total_num`         int      default 0 comment '调用次数',
    `left_num`          int      default 0 comment '剩余调用次数',
    `status`            tinyint  default 0 comment '0-正常 1-禁用',
    `create_time`       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`         tinyint  default 0                 not null comment '是否删除'
) comment '用户接口关系表';

drop table if exists `dict_type`;
create table if not exists `dict_type`
(
    `id`          bigint auto_increment comment 'id' primary key,
    `name`        varchar(20)                        not null comment '字典类型名称',
    `description`        varchar(100)                       null comment '字典类型描述',
    `status`      tinyint                            not null default 0 comment '状态',
    `user_id`     bigint                             not null comment '创建用户',
    `create_time` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`   tinyint  default 0                 not null comment '是否删除'
) comment '字典类型表';

drop table if exists `dict_data`;
create table if not exists `dict_data`
(
    `id`           bigint auto_increment comment 'id' primary key,
    `dict_type_id` bigint                             not null comment '字典类型 id',
    `name`         varchar(128)                       not null comment '字典数据名称',
    `code`         char(6)                            not null comment '字典数据代码',
    `value`        varchar(128)                       not null comment '字典数据值',
    `is_default`   boolean                            not null default false comment '是否默认',
    `style`        varchar(128)                       null comment '样式',
    `status`       tinyint                            not null default 0 comment '状态',
    `user_id`      bigint                             not null comment '创建用户',
    `create_time`  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `update_time`  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `is_delete`    tinyint  default 0                 not null comment '是否删除'
) comment '字典数据表';
