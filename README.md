# MCQQ
Koishi & Whitelist

配合Koishi插件使用: https://github.com/liert/koishi-plugin-mcqq

该插件和Koishi插件同步更新的，出现问题请同时更新两个插件，如有问题可以发Issue
如果还有问题请自己构建master分支的代码，确保是最新版本。

# 配置文件
```
# MYSQL 配置
MySQL_IP: localhost
MySQL_port: 3306
MySQL_database: liert
MySQL_Table: liert
MySQL_user: root
MySQL_password: root

# Koishi 配置
Koishi: false
KoishiWsUrl: ws://localhost:11223

# 抽奖保底次数
VeryLuck: 30

Message:
  noWhiteList: "用户[%player%]不在白名单，请加QQ群: XXX"
  firstWhiteList: "第一次绑定 %player%"
  haveWhiteList: "已经绑定 %player%"
  repeatWhiteList: "[%player%]名称重复"
  delWhiteList: "解除绑定[%player%]"
```