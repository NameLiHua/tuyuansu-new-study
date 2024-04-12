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

## 1.什么是容器，什么是镜像
**什么是容器：**
文档对容器的介绍是：”容器是代码的隔离环境。...容器拥有代码运行所需的一切，甚至是基本操作系统。“
**什么是镜像**：
镜像(lmage):Docker将应用程序及其所需的依赖、函数库、环境、配置等文件打包在一起，称为镜像.
**区别**：
>1、镜像是包含了各种环境或者服务的一个模板，而容器是镜像的一个实例；
>2、镜像是不能运行的，是静态的，而容器是可以运行的，是动态的。
## 2. 用容器的方式启动一个postgres数据库
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

运行成功：
![](other/Pasted%20image%2020240410165016.png)

尝试使用navicat连接：
![](other/Pasted%20image%2020240411112226.png)

>ps:
>官方文档中说的是：PostgreSQL 镜像使用了几个很容易被忽略的环境变量。唯一需要的变量是`POSTGRES_PASSWORD`，其余都是可选的。
>其他环境变量参见：https://hub.docker.com/_/postgres

### 命令行操作
官方推荐以以下形式创建pg的镜像：
```console
docker run --name some-postgres -e POSTGRES_PASSWORD=mysecretpassword -d postgres
```
只需要指定名字和password就好了，日常使用中一般不需要关注别的参数。过程和之前一致。


## postgres 的docker file学习

[参考链接](https://github.com/docker-library/docs/blob/master/postgres/README.md)

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

[dockerfile参考文档](https://docs.docker.com/reference/dockerfile/#maintainer-deprecated)

参数比较多，我认为经常使用的有：

FROM -指定父工程

LABEL - 顾名思义，是docker的标签，有很多用处

ENV-设置环境变量，是docker file中的重要参数

ADD- 添加本地或远程文件和目录。

ENTRYPOINT-容器启动后运行的命令

## 6. 使用dockerfile把springboot的jar包制作成容器并启动
我创建了一个基础实例，写了简单的docker file：
```
FROM anapsix/alpine-java:latest  
EXPOSE 8088  
WORKDIR /workdir/server  
  
COPY ./target/demo-0.0.1-SNAPSHOT.jar /workdir/server/app.jar  
  
ENTRYPOINT ["java","-jar","/workdir/JpaApp.jar"]
```

在部署的时候遇到问题
```
Step 1/5 : FROM anapsix/alpine-java:latest
 ---> c45785c254c5
Step 2/5 : EXPOSE 8088
 ---> Using cache
 ---> 9d40f308825e
Step 3/5 : WORKDIR /workdir/server
 ---> Using cache
 ---> 42765156d4de
Step 4/5 : COPY target/demo-0.0.1-SNAPSHOT.jar /workdir/server/app.jar
Error response from daemon: COPY failed: file not found in build context or excluded by .dockerignore: stat target/demo-0.0.1-SNAPSHOT.jar: file does not exist
Failed to deploy 'demo Dockerfile: src/dockerfile': Can't retrieve image ID from build stream
```
发现docker file存在语法错误，原因是找不到jar包的位置，是因为路径写错了。

问题解决后运行成功：
![](other/Pasted%20image%2020240411111400.png)
## 7. 容器的网络以及容器间的通信方式（了解即可）

[参考链接](https://developer.aliyun.com/article/1349949)

有**IP通信**、**Docker DNS server**、  **Joined容器**三种方式。

## 8. 学习docker-compose来进行简单的容器编排
[参考文档](https://docs.docker.com/reference/cli/docker/compose/)

[开源地址](https://github.com/docker/compose)

官方文档中给出了基本示例：

```yaml
services:
  web:
    build: .
    ports:
      - "5000:5000"
    volumes:
      - .:/code
  redis:
    image: redis
```

在示例中创建了两个docker容器：首先读取当前路径（也可以指定其他的目录）下的docker file中的内容构建容器并指定卷和端口映射，再使用redis镜像构建容器。

将postgres和adminer来进行简单的容器编排：
``` yml

version: '3.9'

services:

  db:
    image: postgres
    restart: always
	shm_size: 128mb
    environment:
      POSTGRES_PASSWORD: example
	ports:
		- 5433:5432
  adminer:
    image: adminer
    restart: always
    ports:
      - 8081:8080
```

执行该docker-compose文件：

```
docker compose -f test1-docker-compose.yml up
```
![](other/Pasted%20image%2020240411144930.png)

运行成功后可以在idea的docker管理工具中看到：
![](other/Pasted%20image%2020240411145418.png)

使用navicat测试连接5433端口：
![](other/Pasted%20image%2020240411145519.png)


## 9. docker-compose的常见命令

[参考资料](https://docs.docker.com/reference/cli/docker/compose/#subcommands)

因为有idea和docker desktop等图形化界面工具，我暂时认为这部分不是特别重要。

## 小结

之前已经学过docker了，但是因为是很功利性地去学，错过了很多的细节，这次重新学习了docker，虽然时间比较短，但是相比于之前还是有所收获的。

对于docker的学习，我认为有以下重点：
- docker的相关概念
- docker的图形化界面操作（我认为命令行效率并不高，docker和git一样，都只是工具，不应该花费过多的时间去背/查表）
- docker file的编写
- docker-compose的使用（这个确实方便很多）