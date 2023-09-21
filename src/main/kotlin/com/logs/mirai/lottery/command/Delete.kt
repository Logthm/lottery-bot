package com.logs.mirai.lottery.command

import com.logs.mirai.lottery.LotteryBot
import com.logs.mirai.lottery.config.CommandConfig
import com.logs.mirai.lottery.data.LotteryList
import net.mamoe.mirai.console.command.BuiltInCommands.AutoLoginCommand.remove
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.isOperator

public object Delete : SimpleCommand(
    owner = LotteryBot,
    primaryName = "lot delete",
    secondaryNames = CommandConfig.delete,
    description = "删除一个抽奖"
) {

    @ConsoleExperimentalApi
    @ExperimentalCommandDescriptors
    override val prefixOptional = true

    @Handler
    suspend fun CommandSenderOnMessage<*>.handle(lotteryID: Long) {
        val sender = fromEvent.sender
        val subject = fromEvent.subject

        val index = try {
            LotteryList.getLotteryIndexByID(lotteryID)
        } catch (e: Throwable) {
            sendMessage("未找到指定的抽奖")
            return
        }
        // 创建者或管理员
        if (bot?.getGroup(LotteryList.contents[index].creator.groupid)?.contains(sender.id) != true) {
            sendMessage("您或bot不在该群！")
            return
        }
        if (
            LotteryList.contents[index].creator.qq == sender.id ||
            bot?.getGroup(LotteryList.contents[index].creator.groupid)?.get(sender.id)?.permission?.isOperator() == true
        ) {
            LotteryList.contents.removeAt(index)
        } else {
            sendMessage("仅有群管理员及创建者可以删除该抽奖！")
        }
    }
}