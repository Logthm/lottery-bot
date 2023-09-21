# lottery-bot

> 基于 [Mirai Console](https://github.com/mamoe/mirai-console) 的qq群抽奖插件，支持多种开奖方式

[![Downloads](https://img.shields.io/github/downloads/Logthm/lottery-bot/total)](https://github.com/Logthm/lottery-bot/releases/latest)
[![Release](https://img.shields.io/github/v/release/Logthm/lottery-bot?display_name=tag)](https://github.com/Logthm/lottery-bot/releases)

# 演示



# 指令一览

所有指令均支持私聊及群聊使用

`< >` 中内容为必填项，`[ ]` 中内容为私聊必填项

在 `./config/com.logs.lottery-bot/Command.yml` 中可以查看和配置所有指令的替代别名

## 帮助指令

| 指令        |     说明     |
| :---------- | :----------: |
| `/lot help` | 显示帮助信息 |

## 创建、删除抽奖

| 指令                     |  说明            |
| :----------------------- |  :----------------------------------------------------------: |
| `/lot add`          |           创建一个抽奖                                                 |
| `/lot delete <抽奖编号>` |        删除一个抽奖（需为**抽奖创建者**或**管理员**）   |
| `/lot end <抽奖编号>`    |         手动结束一个抽奖（需为**抽奖创建者**或**管理员**） |

## 查询抽奖

| 指令                     | 说明                   |
| :----------------------- | ---------------------- |
| `/lot list [群号]`       | 列出所有抽奖           |
| `/lot detail <抽奖编号>` | 查询指定抽奖的详情     |
| `/lot member <抽奖编号>` | 查询参与指定抽奖的群员 |

## 加入/退出抽奖

| 指令                 | 说明     |
|:-------------------|--------|
| `/lot join <抽奖编号>` | 参与指定抽奖 |
| `/lot quit <抽奖编号>`  | 退出指定抽奖 |

# 配置

配置目录位于 `./config/com.logs.lottery/General.yml`

| 配置项        | 说明                             |
| :------------ | :------------------------------- |
| `remain_hour` | 抽奖结束后，保留的时间（小时）   |
| `remind_hour` | 在抽奖截止前多久进行提醒（小时） |
| `wait_time`   | 获取信息时的等待时间（毫秒）     |

# 安装

* 运行 [Mirai Console](https://github.com/mamoe/mirai-console) 生成 `plugins` 文件夹
* 从 [Releases](https://github.com/Logthm/lottery/releases/latest) 下载 `jar` 并将其放入 `plugins` 文件夹中
* 启动 [Mirai Console](https://github.com/mamoe/mirai-console) 后关闭。第一次运行后，会自动生成配置文件以及存储文件
* [可选] 在 `./config/com.logs.lottery-bot/General.yml` 中填写各配置项
* [可选] 在 `./config/com.logs.lottery-bot/Command.yml` 中查看和填写各指令别称
* 再次启动 [Mirai Console](https://github.com/mamoe/mirai-console) 即可使用
