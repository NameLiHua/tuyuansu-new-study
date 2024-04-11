## 环境搭建
已完成
## 学习大纲

1. 了解什么是容器，什么是镜像
    
2. 用容器的方式启动一个postgres数据库
    
3. 容器的创建、启动、停止、删除
    
4. docker run 常见参数以及作用
    
5. 学习dockerfile的编写
    
6. 使用dockerfile把springboot的jar包制作成容器并启动
    
7. 容器的网络以及容器间的通信方式（了解即可）
    
8. 学习docker-compose来进行简单的容器编排
    
9. docker-compose的常见命令
## 目标

1. 掌握容器与镜像的关系
    
2. 启动容器的时候 -v 参数的目的是什么，什么时候要加-v参数
    
3. 将任意一个jar包打包成镜像并启动
    
4. 容器之间是否可以进行通信，如果可以，如何通信，需要具备什么样的条件
    
5. 会编写docker-compose文件并使用docker-compose启动容器

## 什么是容器，什么是镜像
**什么是容器：**
文档对容器的介绍是：”容器是代码的隔离环境。...容器拥有代码运行所需的一切，甚至是基本操作系统。“
**什么是镜像**：
镜像(lmage):Docker将应用程序及其所需的依赖、函数库、环境、配置等文件打包在一起，称为镜像.
**区别**：
>1、镜像是包含了各种环境或者服务的一个模板，而容器是镜像的一个实例；
>2、镜像是不能运行的，是静态的，而容器是可以运行的，是动态的。
## 用容器的方式启动一个postgres数据库
### 图形界面的操作
官方文档中推荐使用Docker Desktop进行相关操作。
但我使用idea代替（因为用习惯了）

直接在dockerHub上拉最新版本的pg：
![](other/Pasted%20image%2020240410162351.png)
拉取成功后创建容器。
在运行容器时**报错**了：
```
Error: Database is uninitialized and superuser password is not specified.
2024-04-10T08:34:20.024314500Z        You must specify POSTGRES_PASSWORD to a non-empty value for the
2024-04-10T08:34:20.024318610Z        superuser. For example, "-e POSTGRES_PASSWORD=password" on "docker run".
2024-04-10T08:34:20.024343881Z 
2024-04-10T08:34:20.024348399Z        You may also use "POSTGRES_HOST_AUTH_METHOD=trust" to allow all
2024-04-10T08:34:20.024352745Z        connections without a password. This is *not* recommended.
2024-04-10T08:34:20.024355714Z 
2024-04-10T08:34:20.024358476Z        See PostgreSQL documentation about "trust":
2024-04-10T08:34:20.024361412Z        https://www.postgresql.org/docs/current/auth-trust.html
```

根据报错，应该设置环境变量：POSTGRES_PASSWORD或POSTGRES_HOST_AUTH_METHOD。
添加环境变量POSTGRES_PASSWORD后启动成功：
（此处不选用POSTGRES_HOST_AUTH_METHOD=true，因为不安全，它允许任何人无需密码即可连接，即使设置了密码）
![](other/Pasted%20image%2020240410165016.png)

>ps:
>官方文档中说的是：PostgreSQL 镜像使用了几个很容易被忽略的环境变量。唯一需要的变量是`POSTGRES_PASSWORD`，其余都是可选的。
>其他环境变量参见：https://hub.docker.com/_/postgres


### 命令行操作
官方推荐以以下形式创建pg的镜像：
```console
docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres
```
只需要指定名字和password就好了

## 3. 容器的创建、启动、停止、删除
这些步骤可以使用桌面端进行简单操作。

**容器的创建**：
```
docker create
```

**启动**：
```
docker run 
```

**停止**：
- `docker stop [NAME]/[CONTAINER ID]`:将容器退出。
- `docker kill [NAME]/[CONTAINER ID]`:强制停止一个容器。

**删除镜像**：
```
docker rmi 
```

## 4. docker run 常见参数以及作用

| 用法  | `docker container run [OPTIONS] IMAGE [COMMAND] [ARG...]` |     |     |
| --- | --------------------------------------------------------- | --- | --- |
| 别名  | `docker run`                                              |     |     |
官网中给出了很多的参数，详见如下：
https://docs.docker.com/reference/cli/docker/container/run/

**个人感觉最为常用的有：**
--name 设别名 （没有使用该标志指定自定义名称，则守护程序会为容器分配一个随机生成的名称）
-d   在后台运行容器并打印容器 ID
-e, --env  设置环境变量
-p 将容器的端口发布到主机
-v 绑定挂载卷


## 5. 学习dockerfile的编写

