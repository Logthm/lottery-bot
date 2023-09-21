package com.logs.mirai.lottery.command

import com.logs.mirai.lottery.LotteryBot
import com.logs.mirai.lottery.config.CommandConfig
import kotlinx.coroutines.sync.Mutex
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent

object Join : SimpleCommand(
    owner = LotteryBot,
    primaryName = "lot join",
    secondaryNames = CommandConfig.join,
    description = "参与指定抽奖"
) {

    @ConsoleExperimentalApi
    @ExperimentalCommandDescriptors
    override val prefixOptional = true

    @Handler
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.handle(lotteryID : Long) {
        val sender = fromEvent.sender
        val subject = fromEvent.subject


    }
    @Handler
    suspend fun CommandSenderOnMessage<FriendMessageEvent>.handle(lotteryID : Long) {

    }
}