USE `rqzb`;

INSERT IGNORE INTO `sys_dept`
(`id`, `parent_id`, `ancestors`, `dept_name`, `dept_code`, `order_num`, `leader`, `phone`, `email`, `status`, `deleted`, `remark`)
VALUES
(1, 0, '0', '总公司', 'ROOT', 1, '管理员', NULL, NULL, 1, 0, '系统初始化部门');

INSERT IGNORE INTO `sys_post`
(`id`, `post_code`, `post_name`, `post_sort`, `status`, `deleted`, `remark`)
VALUES
(1, 'admin', '管理员', 1, 1, 0, '系统初始化岗位');

INSERT IGNORE INTO `sys_role`
(`id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `status`, `deleted`, `remark`)
VALUES
(1, '超级管理员', 'admin', 1, 1, 1, 0, '系统初始化角色');

INSERT IGNORE INTO `sys_user`
(`id`, `dept_id`, `username`, `password`, `nickname`, `real_name`, `email`, `phone`, `sex`, `avatar`, `status`, `deleted`, `remark`)
VALUES
(1, 1, 'admin', CONCAT('{SHA256}', SHA2('admin123', 256)), '管理员', '系统管理员', NULL, NULL, 2, NULL, 1, 0, '默认开发账号，请在生产环境修改或删除');

INSERT IGNORE INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);
INSERT IGNORE INTO `sys_user_post` (`user_id`, `post_id`) VALUES (1, 1);

INSERT IGNORE INTO `sys_menu`
(`id`, `parent_id`, `menu_name`, `menu_type`, `path`, `component`, `perms`, `icon`, `order_num`, `visible`, `status`, `deleted`, `remark`)
VALUES
(100, 0, '系统管理', 'M', '/system', NULL, NULL, 'setting', 1, 1, 1, 0, '系统管理目录'),
(101, 100, '用户管理', 'C', 'users', 'system/user/index', 'sys:user:list', 'user', 1, 1, 1, 0, '用户管理菜单'),
(102, 100, '角色管理', 'C', 'roles', 'system/role/index', 'sys:role:list', 'peoples', 2, 1, 1, 0, '角色管理菜单'),
(103, 100, '菜单管理', 'C', 'menus', 'system/menu/index', 'sys:menu:list', 'tree-table', 3, 1, 1, 0, '菜单管理菜单'),
(104, 100, '部门管理', 'C', 'depts', 'system/dept/index', 'sys:dept:list', 'tree', 4, 1, 1, 0, '部门管理菜单'),
(105, 100, '岗位管理', 'C', 'posts', 'system/post/index', 'sys:post:list', 'post', 5, 1, 1, 0, '岗位管理菜单'),
(106, 100, '字典管理', 'C', 'dict', 'system/dict/index', 'sys:dict:list', 'dict', 6, 1, 1, 0, '字典管理菜单'),
(107, 100, '日志管理', 'C', 'logs', 'system/log/index', 'sys:log:list', 'log', 7, 1, 1, 0, '日志管理菜单');

INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`)
VALUES
(1, 100),
(1, 101),
(1, 102),
(1, 103),
(1, 104),
(1, 105),
(1, 106),
(1, 107);

INSERT IGNORE INTO `sys_dict_type`
(`id`, `dict_name`, `dict_type`, `status`, `deleted`, `remark`)
VALUES
(1, '系统状态', 'sys_normal_disable', 1, 0, '系统通用状态'),
(2, '用户性别', 'sys_user_sex', 1, 0, '用户性别列表');

INSERT IGNORE INTO `sys_dict_data`
(`id`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `deleted`, `remark`)
VALUES
(1, 1, '正常', '1', 'sys_normal_disable', NULL, 'success', 1, 1, 0, '正常状态'),
(2, 2, '停用', '0', 'sys_normal_disable', NULL, 'danger', 0, 1, 0, '停用状态'),
(3, 1, '女', '0', 'sys_user_sex', NULL, 'default', 0, 1, 0, '女性'),
(4, 2, '男', '1', 'sys_user_sex', NULL, 'default', 0, 1, 0, '男性'),
(5, 3, '未知', '2', 'sys_user_sex', NULL, 'default', 1, 1, 0, '未知性别');
