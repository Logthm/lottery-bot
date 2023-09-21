package com.logs.mirai.lottery.command

import com.logs.mirai.lottery.LotteryBot
import com.logs.mirai.lottery.config.CommandConfig
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

public object List : SimpleCommand(
    owner = LotteryBot,
    primaryName = "lot list",
    secondaryNames = CommandConfig.list,
    description = "列出抽奖列表"
) {

    @ConsoleExperimentalApi
    @ExperimentalCommandDescriptors
    override val prefixOptional = true


}