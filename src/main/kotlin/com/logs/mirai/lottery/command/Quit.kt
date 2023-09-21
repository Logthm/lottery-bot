package com.logs.mirai.lottery.command

import com.logs.mirai.lottery.LotteryBot
import com.logs.mirai.lottery.config.CommandConfig
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

object Quit : SimpleCommand(
    owner = LotteryBot,
    primaryName = "lot quit",
    secondaryNames = CommandConfig.quit,
    description = "退出指定抽奖"
) {
    @ConsoleExperimentalApi
    @ExperimentalCommandDescriptors
    override val prefixOptional = true

    @Handler
    public fun handle() {

    }
}