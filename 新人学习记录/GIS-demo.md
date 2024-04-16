# demo描述

- 读取shp/cad文件，数据写入空间库的图层，对图层的属性做叠加的分析。
- 叠加分析结果可以用excel下载。

# 思路
## 基本步骤

- 找到可用的shp/cad文件
- 创建demo项目，引入相关依赖。
- 获取数据库链接
- 读取shp文件
- 处理shp文件，获取图层信息
- 将shapfile中读取数据导入到postgis中
- 对图层属性进行叠加分析
	- 叠加分析
- 将分析结果写入excel中

# 待学习

- 更多GIS的相关基础知识。
- GeoTools的基本使用

## 叠加分析

[参考链接](https://desktop.arcgis.com/zh-cn/arcmap/latest/analyze/commonly-used-tools/overlay-analysis.htm)
[参考链接2](https://cloud.tencent.com/developer/article/1357056)
使用叠加分析可将多个数据集的特征合并为一个特征。

**数据结构+算法**
![](other/Pasted%20image%2020240415115325.png)
# 遇到的问题

## 无法通过maven导入GeoTool的配置。

![](other/Pasted%20image%2020240415145202.png)

按照网上的方法对settings.xml进行了修改。
新增了以下配置：
``` xml
<repository>
   <id>osgeo</id>
   <name>OSGeo Release Repository</name>
   <url>https://repo.osgeo.org/repository/release/</url>
   <snapshots><enabled>false</enabled></snapshots>
   <releases><enabled>true</enabled></releases>
</repository>
<repository>
   <id>osgeo-snapshot</id>
   <name>OSGeo Snapshot Repository</name>
   <url>https://repo.osgeo.org/repository/snapshot/</url>
   <snapshots><enabled>true</enabled></snapshots>
   <releases><enabled>false</enabled></releases>
</repository>
```

但还是没有用。

此处有两个问题：

**问题一**

一直以来都用的都是idea来配置maven的settings.xml文件，但实际上，这个没有生效，导致osgeo仓库一直没有被引入。

![](other/Pasted%20image%2020240415151438.png)

**问题二**

没有理解maven中，Maven 的 repository 与 mirror的区别。
之前网上搜索到的解决方案是：在maven中加入仓库配置(这是正确的):
```xml
<repository>
   <id>osgeo</id>
   <name>OSGeo Release Repository</name>
   <url>https://repo.osgeo.org/repository/release/</url>
   <snapshots><enabled>false</enabled></snapshots>
   <releases><enabled>true</enabled></releases>
</repository>
```
但是因为我之前设置了全局mirror（当一个镜像的`mirrodOf`参数与某个仓库的`id`参数相匹配，就向该镜像请求资源，否则向原仓库请求资源。），导致所有的请求都发往阿里云，但实际上要全球osgeo的仓库才能找到正确的jar包。
所以应该将mirror修改一下。

原mirror：
```xml
<mirrors>

        <mirror>

            <id>aliyunMaven</id>

            <mirrorOf>*</mirrorOf>

            <name>阿里云公共仓库</name>

            <url>https://maven.aliyun.com/repository/public</url>

        </mirror>

    </mirrors>
```

修改后：
```xml
<mirrors>

        <mirror>

            <id>aliyunMaven</id>
			# 需要配置aliyun-central,aliyun-snapshots
            <mirrorOf>aliyun-central,aliyun-snapshots</mirrorOf>

            <name>阿里云公共仓库</name>

            <url>https://maven.aliyun.com/repository/public</url>

        </mirror>

    </mirrors>
```

配置aliyun-central,aliyun-snapshots：

``` xml
 <!--        aliyun-->

        <profile>

            <id>aliyun</id>

            <repositories>

                <repository>

                    <id>aliyun-central</id>

                    <url>https://maven.aliyun.com/nexus/content/groups/public</url>

                    <releases>

                        <enabled>true</enabled>

                    </releases>

                    <snapshots>

                        <enabled>false</enabled>

                    </snapshots>

                </repository>

                <repository>

                    <id>aliyun-snapshots</id>

                    <url>https://maven.aliyun.com/nexus/content/groups/public</url>

                    <releases>

                        <enabled>false</enabled>

                    </releases>

                    <snapshots>

                        <enabled>true</enabled>

                    </snapshots>

                </repository>

            </repositories>

            <pluginRepositories>

                <pluginRepository>

                    <id>aliyun-central</id>

                    <url>https://maven.aliyun.com/nexus/content/groups/public</url>

                    <releases>

                        <enabled>true</enabled>

                    </releases>

                    <snapshots>

                        <enabled>false</enabled>

                    </snapshots>

                </pluginRepository>

                <pluginRepository>

                    <id>aliyun-snapshots</id>

                    <url>https://maven.aliyun.com/nexus/content/groups/public</url>

                    <releases>

                        <enabled>false</enabled>

                    </releases>

                    <snapshots>

                        <enabled>true</enabled>

                    </snapshots>

                </pluginRepository>

            </pluginRepositories>

        </profile>
```