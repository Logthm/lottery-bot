package com.logs.mirai.lottery.data

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import java.util.NoSuchElementException

object LotteryList : AutoSavePluginData(saveName = "LotteryList") {

    @ValueDescription("抽奖列表")
    val contents: MutableList<Lottery> by value()

    fun getLotteryByID(id: Long): Lottery {
        for (lottery in contents) {
            if (lottery.id == id) return lottery
        }
        throw NoSuchElementException("未找到指定的抽奖")
    }

    fun getLotteryIndexByID(id: Long): Int {
        for ((index, lottery) in contents.withIndex()) {
            if (lottery.id == id) return index
        }
        throw NoSuchElementException("未找到指定的抽奖")
    }
}