# 文件相关规则

## 名称规则

例如一个网络地址的文件路径：

```
https://foodsup-wms.s3.ca-central-1.amazonaws.com/wms/file/1.23.png?Algorithm=SHA256&Date=20230427T133323Z
```

由以下部分组成：
protocol + host + path + "/" + fileName + paramPath

protocol（协议）：

```
https://
```

host（域名，含端口号）：

```
foodsup-wms.s3.ca-central-1.amazonaws.com

192.168.1.1:8000
```

hostPath或ip（指向计算机的域名、IP部分）port（端口号）：

```
192.168.1.1
8000
```

path（路径，本项目中的标准路径为前带斜杠、后不带斜杠，方便路径拼接）：

```
/wms/file
```

fileName（完整的文件名，前不带斜杠）：

```
1.23.png
```
fullPath（含文件名的完整的本地路径）:

```
/wms/file/1.23.png
```

filePrefixName、fileSuffixName（前缀名指最后一个点前的全部内容、后缀名指最后一个点后的全部内容，一般情况下后缀名表示文件类型，分割前后缀的点不在第一位（参考Unix隐藏文件））：

```
1.23
png
```

paramPath（写在路径中的参数，含首位问号，取值规则是先去?，再按&分割，再按=分割，分割后的键值进行urlencode）

```
?Algorithm=SHA256&Date=20230427T133323Z
```

param、params（解析后的参数对象或数组）

```
{"Algorithm":"SHA256","Date":"20230427T133323Z"}
```

## Path规则

 - 一、二级路径区分用途（Java常量FileUseType）；“库存导出”：【/export/inventory】、“采购单发票”【/upload/cginvoice】
 - 三级路径区分日期；“2023-04-28”：【/20230428】
 - 四级路径区分唯一标识，目前为时分秒；“09:35:36”：【/093536】
 - 目前以秒为单位不可出现重复文件名的文件，后续可以改为uuid、文件md5、或者其他。

 