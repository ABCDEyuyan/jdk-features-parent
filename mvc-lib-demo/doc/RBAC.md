# 简介

RBAC模型（Role-Based Access Control：基于角色的访问控制）

在RBAC模型里面，有3个基础组成部分，分别是：用户、角色和权限



- User（用户）：每个用户都有唯一的UID识别，并被授予不同的角色
- Role（角色）：不同角色具有不同的权限
- Permission（权限）：访问权限
- 用户-角色映射：用户和角色之间的映射关系(多对多的关系)
- 角色-权限映射：角色和权限之间的映射(多对多的关系)



# 表结构

```mysql
# ================================= 数据表 =================================
# 权限类型表
create table permission_type
(
    id                   int auto_increment primary key comment '主键id',
    permission_type_name varchar(30) comment '权限类型名'
);

# 权限表
create table permission
(
    id                 int auto_increment primary key comment '主键id',
    permission_name    varchar(30) comment '菜单名称',
    permission_url     varchar(80) comment '菜单url',
    parent_id          int comment '父id',
    menu_icon          varchar(20) comment '图标',
    is_enable          bit comment '状态',# 启用，停用
    permission_type_id int comment '权限类型id',
    foreign key (parent_id) references permission (id),
    foreign key (permission_type_id) references permission_type (id)
);

# 管理员类型表
create table administrator_type
(
    id                             int auto_increment primary key comment '主键id',
    administrator_type_name        varchar(30) comment '管理员类型名称',
    administrator_type_add_time    datetime comment '创建时间',
    is_enable                      bit comment '是否启用',
    administrator_type_description varchar(250) comment '管理员类型描述'
);

# 管理员信息表
create table administrator_info
(
    id                              int auto_increment primary key comment '主键id',
    administrator_info_name         varchar(30) comment '管理员姓名',
    administrator_user_name              varchar(30) comment '账号',
    administrator_info_phone_number varchar(40) comment '手机号码',
    administrator_info_email        varchar(40) comment '电子邮箱',
    pwd                             varchar(30) comment '密码',
    administrator_add_time          datetime comment '创建时间',
    administrator_last_login_time   datetime comment '上一次登录时间',
    is_enable                       bit comment '是否启用'
);

# 管理员类型管理员信息关系表
create table administrator_type_info_merge
(
    id                    int auto_increment primary key comment '主键',
    administrator_type_id int comment '管理员类型id',
    administrator_info_id int comment '管理员信息id',
    foreign key (administrator_type_id) references administrator_type (id),
    foreign key (administrator_info_id) references administrator_info (id)
);

# 管理员类型权限关系表
create table administrator_type_permission_merge
(
    id                    int auto_increment primary key comment '主键',
    administrator_type_id int comment '管理员类型id',
    permission_id         int comment '权限id',
    foreign key (administrator_type_id) references administrator_type (id),
    foreign key (permission_id) references permission (id)
);
```

# 案例所用到的依赖

后端

- mvc
- dbutils
- jwt
- druid
- mysql-connector-java

前端

- vue
- axios
- element-ui



# 测试数据

```mysql
# 权限类型
insert into permission_type( permission_type_name) values ('菜单'),
                                                          ('资源');

# 权限表
insert into permission(permission_name, permission_url, parent_id, menu_icon, is_enable, permission_type_id)
values ('主页', 'index.html', null, 'el-icon-s-home', true, 1),
       ('产品', null, null, 'el-icon-s-goods', true, 1),
       ('订单', null, null, 'el-icon-s-order', true, 1),
       ('加盟', null, null, 'el-icon-s-check', true, 1),
       ('权限', null, null, 'el-icon-lock', true, 1),
       ('产品列表', 'product.html', 2, 'el-icon-menu', true, 1),
       ('产品类型', 'productType.html', 2, 'el-icon-plus', true, 1),
       ('材料管理', 'material.html', 2, 'el-icon-ice-cream', true, 1),
       ('仓库列表', 'warehouse.html', 2, 'el-icon-box', true, 1),
       ('订单列表', 'order.html', 3, 'el-icon-menu', true, 1),
       ('加盟商管理', 'franchisee.html', 4, 'el-icon-user-solid', true, 1),
       ('加盟店管理', 'franchiseStores.html', 4, 'el-icon-s-shop', true, 1),
       ('加盟申请管理', 'order.html', 4, 'el-icon-s-claim', true, 1),
       ('用户列表', 'addUser.html', 5, 'el-icon-user', true, 1),
       ('管理员列表', 'addUser.html', 5, 'el-icon-s-custom', true, 1),
       ('角色列表', 'addUser.html', 5, 'el-icon-menu', true, 1),
       ('菜单列表', 'addUser.html', 5, 'el-icon-notebook-2', true, 1),
       ('资源列表', 'addUser.html', 5, 'el-icon-s-tools', true, 1);

# 管理员类型表
insert
into administrator_type(administrator_type_name, administrator_type_add_time, is_enable, administrator_type_description)
values ('超级管理员', now(), true, '最高权限'),
       ('产品管理员', now(), true, '管理产品');

# 管理员信息表
insert into administrator_info(administrator_info_name, administrator_user_name, administrator_info_phone_number,
                               administrator_info_email, pwd, administrator_add_time, administrator_last_login_time,
                               is_enable)
values ('管理员A', '123456', '138707232138', '324762387@qq.com', '123456', now(), now(), true),
       ('产品管理员', '654321', '12673556831', '43989342@qq.com', '123456', now(), now(), true);

# 管理员信息与类型关系表
insert into administrator_type_info_merge(administrator_type_id, administrator_info_id)
values (1, 1),
       (2, 2);

# 权限与管理员关系表
insert into administrator_type_permission_merge(administrator_type_id, permission_id)
values (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (1, 10),
       (1, 11),
       (1, 12),
       (1, 13),
       (1, 14),
       (1, 15),
       (1, 16),
       (1, 17),
       (1, 18),
       (2, 1),
       (2, 2),
       (2, 7),
       (2, 8),
       (2, 9),
       (2, 10);
```



# 案例地址

前端：https://gitee.com/liu-xuejing/front-end-of-violet.git

后端：https://gitee.com/liu-xuejing/violet-backend.git

