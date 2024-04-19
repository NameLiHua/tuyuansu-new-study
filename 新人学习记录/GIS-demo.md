# demo描述

## V1
- 读取shp/cad文件，数据写入空间库的图层，对图层的属性做叠加分析。
- 叠加分析结果可以用excel下载。

## V2（）
- 写接口上传两个图层，创建新的图层。
- 结果导出excel（可以用poitl）
- 要导出图层要素的wkt信息并在excel
- 自己创建图层，写入相交文件
- 用postGIS 中的st函数进行叠加分析

## V3（上午10点）
- 意见：
	- 1：把当前的接口拆分成至少三个接口 
	- 2：不要临时文件夹
	- 3：重整sql语句，sql语句存在错误（要相交的结果，而不是单个的）
	- 4：按照V2的要求去重新实现（导出一个excel，wkt文件要写入excel里面）
	- 5：使用swagger调试
- 拆分成模块

## V4
- 一定要用临时文件吗？可以直接返回流吗
- 单个表格如何写入大量的数据

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
		- 1. 遍历第一个 `List<SimpleFeature>` 中的每个要素。
		- 1. 对于每个要素，在第二个 `List<SimpleFeature>` 中查找与之相交的要素。
		- 1. 如果找到相交的要素，计算它们的相交部分的面积。
		- 2. 将每对相交要素的相交部分的面积累加起来。
		- 3. 最后，计算相交面积总和与两个列表中所有要素的总面积之比，即为相交的比例。
- 将分析结果写入excel中
- 将分析结果写入postGIS中

## 分析过程


# 新学

- 更多GIS的相关基础知识。
- GeoTools的基本使用

## 叠加分析

[参考链接](https://desktop.arcgis.com/zh-cn/arcmap/latest/analyze/commonly-used-tools/overlay-analysis.htm#GUID-BF7FF5E5-302F-4DC4-9108-7C91795B5427)
![](other/Pasted%20image%2020240417154638.png)

## geotools 使用

### 连接数据库
```java
Map<String, Object> params = new HashMap<String, Object>();
params.put(PostgisNGDataStoreFactory.DBTYPE.key, dbtype);
params.put(PostgisNGDataStoreFactory.HOST.key, host);
params.put(PostgisNGDataStoreFactory.PORT.key, new Integer(port));
params.put(PostgisNGDataStoreFactory.DATABASE.key, database);
params.put(PostgisNGDataStoreFactory.SCHEMA.key, "public");
params.put(PostgisNGDataStoreFactory.USER.key, userName);
params.put(PostgisNGDataStoreFactory.PASSWD.key, password);
try {
	pgDatastore = DataStoreFinder.getDataStore(params);
	if (pgDatastore != null) {
		System.out.println("系统连接到位于：" + host + "的空间数据库" + database
				+ "成功！");
	} else {
		System.out.println("系统连接到位于：" + host + "的空间数据库" + database
				+ "失败！请检查相关参数");
	}
} catch (IOException e) {
	e.printStackTrace();
	System.out.println("系统连接到位于：" + host + "的空间数据库" + database
			+ "失败！请检查相关参数");
```

### 图层操作
#### 查询
**根据图层名称获取要素集**:
``` java
SimpleFeatureSource featureSource =pgDatastore.getFeatureSource(layerName)
```
**获取要素集合**：
```
SimpleFeatureCollection result = featureSource.getFeatures(filter);
```
**遍历要素几集合**：

根据要素集合获取要素迭代器，遍历填充到要素list中
``` java
ArrayList<SimpleFeature> featureList = new ArrayList<SimpleFeature>();FeatureIterator<SimpleFeature> itertor = result.features();
	while (itertor.hasNext()) {
		SimpleFeature feature = itertor.next();
		featureList.add(feature);
	}
	itertor.close();
```

**获取feature中的属性：**

获取feature中的属性
```java
feature.getAttribute(arg0);
```

### shp文件->postGIS数据库

**步骤**：

文件读取->

## 叠加分析

[参考链接](https://desktop.arcgis.com/zh-cn/arcmap/latest/analyze/commonly-used-tools/overlay-analysis.htm)
[参考链接2](https://cloud.tencent.com/developer/article/1357056)
使用叠加分析可将多个数据集的特征合并为一个特征。

**数据结构+算法**
![](other/Pasted%20image%2020240415115325.png)

## PostGIS中ST函数
[官网参考1](https://postgis.net/docs/reference.html)
[官网参考2](https://postgis.net/docs/ST_AsGeoJSON.html)

# 相关概念

[要素类](https://desktop.arcgis.com/zh-cn/arcmap/latest/manage-data/geodatabases/feature-class-basics.htm)

由于知识点实在是太多了，只记录比较热点的。

## 基础的三个要素类的类型

通常，要素类是点、线或面的专题集合（但存在七种要素类类型，只有地利数据库存在其他四种类型）
- **点**：表示过小而无法表示为线或面以及点位置（如 GPS 观测值）的要素。
- **线**：表示形状和位置过窄而无法表示为区域的地理对象（如，街道中心线与河流）。也使用线来表示具有长度但没有面积的要素，如等值线和边界。
- **面**一组具有多个边的面要素，表示同类要素类型（如州、县、宗地、土壤类型和土地使用区域）的形状和位置。

## 要素几何

图层可能有很多要素，要素可能有很多几何。

要素类包含各要素的几何形状和描述性属性。

各要素几何主要由各自的要素类型（点、线或面）定义。

线要素类和面要素类可由单部分或多部分构成。

![](other/Pasted%20image%2020240416112342.png)


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

此处有存在两个问题：

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

## 类型引用错误

在写demo的时候，遇到类型引用的错误，但是我明明正确引用了。

Geometry有两个引用方式：
``` java
import org.locationtech.jts.geom.Geometry;
import org.opengis.geometry.Geometry;
```

要注意鉴别

## 中文乱码问题

在读取文件的时候要设置中文编码格式：
`store.setCharset(StandardCharsets.UTF_8);  `

``` java
public static SimpleFeatureCollection getFeatureCollectionFromShp(String shpPath) {  
    File shpFile = getShpFileFromPath(shpPath);  
    try {  
        ShapefileDataStore store = (ShapefileDataStore) FileDataStoreFinder.getDataStore(shpFile);  
        // 设置UTF-8，不然会中文乱码  
        store.setCharset(StandardCharsets.UTF_8);  
        return store.getFeatureSource().getFeatures();  
    } catch (IOException e) {  
        throw new RuntimeException(e.getMessage(), e);  
    }  
}
```

## excel空指针异常问题

我使用官网的示例：

## GeoTools不能直接读取CAD文件问题

**解决方案**：

解析dxf用开源的GDAL，调用GDAL驱动把dxf转成shp文件，然后再用开源的GeoTools去解析shp文件。

## 调用GDAL失败

报错如下：
```
Native library load failed.
java.lang.UnsatisfiedLinkError: no gdalalljni in java.library.path
```

当时是使用maven直接导入了依赖，但是没有用：
```xml
<!--GDAL-->  
<dependency>  
    <groupId>org.gdal</groupId>  
    <artifactId>gdal</artifactId>  
    <version>3.8.0</version>  
</dependency>
```

解决方式：
[参考链接](https://blog.csdn.net/qq_42316200/article/details/103243960)

## 遇到Swagger引入失败问题

[参考链接](https://stackoverflow.com/questions/40241843/failed-to-start-bean-documentationpluginsbootstrapper-in-spring-data-rest)

```log
org.springframework.context.ApplicationContextException: Failed to start bean 'documentationPluginsBootstrapper'; nested exception is java.lang.NullPointerException
```

此问题是由于Spring MVC`Spring Fox 3.0.0`不支持新的基于 PathPattern 的路径匹配策略引起的.

springboot 2.6.0及以上版本与Spring fox版本不匹配

加上yml配置就可以解决：
```yml
spring:  
  mvc:  
    pathmatch:  
      matching-strategy: ant_path_matcher
```

## 静态字段不支持注入问题
```
Description: Parameter 0 of constructor in com.example.demogis.service.PostgisService required a bean of type 'org.geotools.data.DataStore' that could not be found. The following candidates were found but could not be injected: - User-defined bean method 'connectDatabase' in 'PgConfig' ignored as the bean value is null Action: Consider revisiting the entries above or defining a bean of type 'org.geotools.data.DataStore' in your configuration. 与目标 VM 断开连接, 地址为: ''127.0.0.1:8809'，传输: '套接字''
```
这段代码看起来是用于在Spring Boot应用程序中配置一个PostGIS数据源的Java配置类。不过，这里有几个问题可能导致你遇到无法注入`DataStore`类型的bean的错误。

首先，`@Value`注解用于注入配置属性到Spring管理的bean的实例字段中，但是这里你尝试将它们注入到静态字段中，这是不被支持的。静态字段不属于任何Spring管理的bean实例，因此Spring无法注入值到这些字段中。