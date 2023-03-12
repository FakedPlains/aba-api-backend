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
create table if not exists interface_info
(
    `id`             bigint auto_increment comment 'id' primary key,
    `name`           varchar(256)                       not null comment '接口名称',
    `description`    varchar(256)                       null comment '描述',
    `url`            varchar(512)                       not null comment '接口地址',
    `requestParams`  text                               null comment '请求参数',
    `method`         varchar(256)                       not null comment '请求类型',
    `requestHeader`  text                               null comment '请求头',
    `responseHeader` text                               null comment '响应头',
    `status`         tinyint                            not null default 0 comment '接口状态（0-关闭 1-开启）',
    `userId`         bigint                             not null comment '创建人',
    `createTime`     datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`     datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`       tinyint  default 0                 not null comment '是否删除'
) comment '接口信息表';

-- 用户接口关系表
create table if not exists user_interface_info
(
    `id`              bigint auto_increment comment 'id' primary key,
    `userId`          bigint                             not null comment '调用用户',
    `interfaceInfoId` bigint                             not null comment '接口信息',
    `totalNum`        int      default 0 comment '调用次数',
    `leftNum`         int      default 0 comment '剩余调用次数',
    `status`          tinyint  default 0 comment '0-正常 1-禁用',
    `createTime`      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime`      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete`        tinyint  default 0                 not null comment '是否删除'
) comment '用户接口关系表';
