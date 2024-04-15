# 开发环境准备

## 参考
[新人文档](https://f0si8reot2c.feishu.cn/wiki/X2RewIZCjiFTg2kWEflclaeunqc)

## 遇到的问题
### arcGIS下载问题
新人文档中给的资料不全，不能够安装好ArcGIS Desktop。

如果按照新人文档中的来，就会报以下错误：

![](other/Pasted%20image%2020240415092320.png)

查询了一下，官网给出的解决方案如下：[参考链接](https://support.esri.com/en-us/knowledge-base/error-could-not-connect-to-an-arcgis-license-manager-ru-000019941)

也就是没有破解好，参考了其他的解决方案才弄好[参考链接](https://baijiahao.baidu.com/s?id=1780321137992538690&wfr=spider&for=pc)

### PostGis下载问题

在按照教程安装的时候遇到问题：

![](other/Pasted%20image%2020240415100720.png)

这个时候查看log：

```
错误:  所需要的扩展"postgis"没被安装
提示:  请使用 CREATE EXTENSION ... CASCADE 安装所需的扩展。
```

这个时候执行安装指令：
```
CREATE EXTENSION postgis;
```

报错如下：
![](other/Pasted%20image%2020240415100410.png)

最后发现是postGIS的版本与PG的版本不兼容的问题，在[官网](https://download.osgeo.org/postgis/windows/)找到对应的版本就好了。(虽然是很简单的问题，但是还是卡了很久，这类问题往往不能直接通过日志直接找到解决方案)

## 基本定义


地理数据（Geographic Data）
	定义
		是各种地理特征和现象之间关系的符号化表示，包括空间位置特征、属性特征及时态特征三个基本特征部分
		也可称为空间数据（Spatial Data）
	根据数据结构分类
		矢量数据
			矢量数据是通过记录坐标的方式尽可能精确地表示点、线和多边形等地理实体。常见矢量数据格式：ShapeFile、GDB/MDB、DWG、 KML、MapGIS（*.wt,*.wl,*.wp）等。
		栅格数据
			最简形式的栅格由按行和列（或格网）组织的像元（或像素）矩阵组成，其中的每个像元都包含一个信息值（例如温度、高程、光谱等）。常见栅格数据格式：*.img、*.tif、*.sid、*.dt等。
			栅格数据可以是卫星影像、航空像片、甚至扫描的地图。分辨率越高，要素表达越精细，数据存储空间越大。
	三个基本特征部分
		空间位置特征
			空间位置数据描述地理实体所在的空间绝对位置以及实体间存在的空间关系的相对位置。空间位置可由定义的坐标参照系统描述，空间关系可由拓扑关系，如邻接、关联、连通、包含、重整等来描述。
		属性特征
			属性特征有时又称为非空间特征，是属于一定地理实体的定性、定盈指标，即描述了地理信息的非空间组成成分，包括语义数据和统计数据等。
		时态特征
			时态特征是指地理数据采集或地理现象发生的时刻或时段。