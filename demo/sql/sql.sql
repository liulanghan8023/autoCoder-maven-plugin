CREATE TABLE `ceb_app` (
                           `id` varchar(36) NOT NULL,
                           `name` varchar(32) NOT NULL COMMENT '名称',
                           `status` varchar(12) NOT NULL COMMENT '状态,[EFFECTIVE:有效,INVALID:失效]',
                           `remark` varchar(255) DEFAULT NULL COMMENT '备注',
                           `create_time` datetime DEFAULT NULL,
                           PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='应用表';