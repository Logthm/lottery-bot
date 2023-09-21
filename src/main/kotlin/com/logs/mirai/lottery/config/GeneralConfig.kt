package com.logs.mirai.lottery.config

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.ReadOnlyPluginConfig
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value

object GeneralConfig : AutoSavePluginConfig("General") {

    @ValueDescription("抽奖结束后保留的时间（单位为小时）")
    val remain_hour: Long by value(72L)

    @ValueDescription("在抽奖截止前多久进行提醒（单位为小时）")
    val remind_hour: Long by value(1L)

    @ValueDescription("获取信息时的等待时间（单位为毫秒）")
    val wait_time: Long by value(120_000L)
}