package com.logs.mirai.lottery.config

import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object CommandConfig : ReadOnlyPluginConfig("Command") {

    @ValueDescription("add命令的别名")
    val add by value(arrayOf("发起抽奖","添加抽奖","创建抽奖","增加抽奖"))

    @ValueDescription("detail命令的别名")
    val detail by value(arrayOf("详情"))

    @ValueDescription("delete命令的别名")
    val delete by value(arrayOf("删除抽奖","结束抽奖"))

    @ValueDescription("member命令的别名")
    val member by value(arrayOf("成员列表"))

    @ValueDescription("join命令的别名")
    val join by value(arrayOf("参加"))

    @ValueDescription("quit命令的别名")
    val quit by value(arrayOf("退出"))

    @ValueDescription("end命令的别名")
    val end by value(arrayOf("结束"))

    @ValueDescription("list命令的别名")
    val list by value(arrayOf("抽奖列表"))
}