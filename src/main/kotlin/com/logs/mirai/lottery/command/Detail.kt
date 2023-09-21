package com.logs.mirai.lottery.command

import com.logs.mirai.lottery.LotteryBot
import com.logs.mirai.lottery.config.CommandConfig
import com.logs.mirai.lottery.config.GeneralConfig
import com.logs.mirai.lottery.data.LotteryList
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.buildMessageChain
import java.text.SimpleDateFormat

public object Detail : SimpleCommand(
    owner = LotteryBot,
    primaryName = "lot detail",
    secondaryNames = CommandConfig.detail,
    description = "查询指定抽奖的详情"
) {

    @ConsoleExperimentalApi
    @ExperimentalCommandDescriptors
    override val prefixOptional = true

    @Handler
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.handle(lotteryID: Long) {
        val sender = fromEvent.sender
        val subject = fromEvent.subject

        val lottery = try {
            LotteryList.getLotteryByID(lotteryID)
        } catch (e : Throwable) {
            sendMessage("在该群未找到编号为 ${lotteryID} 的抽奖")
            return
        }

        if (lottery.isEnd) {
            sendMessage(buildMessageChain{
                +PlainText("[已结束]\n" +
                        "抽奖编号：${lottery.id}\n" +
                        "截止日期：\n")
                +SimpleDateFormat("yyyy/MM/dd HH:mm").format(lottery.endTime)
                +PlainText("\n描述：\n")
                +lottery.getDescription(subject)
                +PlainText("\n中奖名单：\n")

            })
        } else {
            sendMessage(buildMessageChain{

            })
        }
    }
}