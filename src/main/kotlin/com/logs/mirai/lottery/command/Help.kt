package com.logs.mirai.lottery.command

import com.logs.mirai.lottery.LotteryBot
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand

object Help : SimpleCommand(
    owner = LotteryBot,
    primaryName = "lot help",
    description = "回复帮助信息"
) {
    @Handler
    suspend fun CommandSenderOnMessage<*>.handle() {
        sendMessage("""
            
        """.trimIndent())
    }
}