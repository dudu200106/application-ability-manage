# application Ability Manage

共性支撑-应用与能力管理

## 项目结构
```
src
├─main
│  ├─java
│  │  └─com.dsj.csp
│  │           ├─common
│  │           │  ├─config        配置
│  │           │  ├─const         公共常量
│  │           │  ├─entity        公共对象
│  │           │  └─enums         公共枚举
│  │           └─manage
│  │              ├─controller    接口层
│  │              ├─biz           业务层（调用多服务组装数据）
│  │              ├─dto           数据传输对象
│  │              ├─entity        实体对象
│  │              ├─mapper        数据库操作层
│  │              └─service       服务层
│  └─resources                    配置文件
│      
└─test
    ├─http                        接口测试（idea内置工具，启动服务后可直接调用）
    └─java                        单元测试
```

