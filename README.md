# Grasscutters的WEB控制面板

一款Grasscutters的WEB控制面板插件

## 服主控制台使用方法

**2.0版本更新需要删除1.0版本的配置文件！！**

1.下载GrasscuttersWebDashboard.jar放在服务器的plugins文件夹下

2.重启服务器

3.服务器启动后可以在控制台看到如下信息

```
[15:39:25] [INFO] [WEB控制台] 启动完成！！
[15:39:25] [INFO] [WEB控制台] 您设置的Token是：******
[15:39:25] [INFO] [WEB控制台] 连接地址是：wss://*******
[15:39:25] [INFO] [WEB控制台] 快速连接，用浏览器打开：https://liujiaqi7998.github.io/GrasscuttersWebDashboard/index.html?server=wss://*****/Dashboard/GrasscuttersWebDashboard
```

4.浏览器打开控制面板：复制‘快速连接，用浏览器打开’后面的链接用浏览器打开，或者[Grasscutters 网页控制台 (liujiaqi7998.github.io)](https://liujiaqi7998.github.io/GrasscuttersWebDashboard/)输入连接地址

5.输入连接地址，点击确定

## 服务器成员进入WebTools方法

**2.0版本更新需要删除1.0版本的配置文件！！**

1.向服务器发送 /webtools 或 /webt 或 /wt 命令

2.你会接收到一封带有网页链接的邮件

3.点击连接打开网页控制台

#### 注意事项：

1.只有玩家处于在线状态才可以使用。

2.如果玩家没有某条指令的权限，执行是没有效果的。

3.邮件默认有效时间是3分钟，请3分钟内打开邮件链接，已经打开的页面只要不关闭就会一直处于连接状态。

## 配置文件

| 参数        | 含义                                                         |
| ----------- | ------------------------------------------------------------ |
| token       | 服主连接密钥（请妥善保管！）                                 |
| WebtoolsURL | Webtools的网页链接，如需私有部署请修改（会影响邮件里面的地址） |
| key_timeout | 玩家Webtools邮件失效（单位：毫秒）                           |

## 插件原理（方便二次开发）

采用WebSocket协议连接的服务器

服务器控制台连接地址请看控制台输出内容

WebTools连接地址：ws(wss)://Host/WebTools?key=**

例如: ws://127.0.0.1/WebTools?key=12345678

通讯方法：

##### 向服务器发送:

是一个JSON数据包其中包含：

| 参数 | 含义     |
| ---- | -------- |
| type | 数据类型 |
| data | 数据内容 |

数据类型包括：

| 数据类型 | 含义                         | data数据类型            |
| -------- | ---------------------------- | ----------------------- |
| CMD      | 传输命令                     | 字符串:执行的控制台命令 |
| Player   | 获取在线玩家                 | 整数:0                  |
| State    | 服务器状态（仅服主接口可用） | 整数:0                  |

##### 服务器回传数据:

是一个JSON数据包其中包含：

| 参数      | 含义     |
| --------- | -------- |
| eventName | 数据类型 |
| data      | 数据内容 |

具体数据类型内容多半都是返回执行信息，不列举了，感兴趣自行抓包看代码。

## 错误指南

Q1. 插件安装后导致游戏无法启动

   可能是割草机升级导致插件需要重新适配置

Q2. 无法连接到服务器

   检测插件是否被服务器正常加载，检查连接地址是否输入错误。

   浏览器按F12打开开发者工具箱查看错误日志，普遍问题是NET::ERR_CERT_AUTHORITY_INVALID Error
   
   解决办法（任选其一）
   1. 部署一个有效的SSL证书
   2. 切换成http
   3. 设置浏览器安全级别不校验SSL证书
   4. 将服务器的证书添加到电脑信任目录
   5. 将网页拉取到本地部署打开

## 自行编译

这是一个IDEA创建的项目，直接下载导入并Maven一下。

## 注意事项

插件处于开发状态，非常的不稳定，存在安全隐患，所以请不要向任何人透露连接地址

本插件开源免费

感谢[Grasscutters/gcgm-plugin: A grasscutter game master web dashboard plugin (github.com)](https://github.com/Grasscutters/gcgm-plugin)提供的帮助

WebTools网页我真的是写不动了，部分代码来自[SpikeHD/MojoFrontend: Frontend for Mojo Console (github.com)](https://github.com/SpikeHD/MojoFrontend)请遵守GNU General Public License v3.0协议