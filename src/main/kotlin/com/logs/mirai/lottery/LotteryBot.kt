package com.logs.mirai.lottery

import kotlinx.coroutines.cancel
import net.mamoe.mirai.console.command.*
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.data.PluginConfig
import net.mamoe.mirai.console.data.PluginData
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.ListenerHost
import net.mamoe.mirai.event.SimpleListenerHost
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.registerTo
import net.mamoe.mirai.utils.info

object LotteryBot : KotlinPlugin(
    JvmPluginDescription(
        id = "com.logs.lottery-bot",
        name = "lottery-bot",
        version = "0.1.0",
    ) {

        author("logs")
        info("""A lottery tool for qq group""")
    }
) {
    private val config: List<PluginConfig> by services()
    private val data: List<PluginData> by services()
    private val commands: List<Command> by services()
    private val listeners: List<ListenerHost> by services()

    override fun onEnable() {
        logger.info { "Lottery-Bot Plugin loaded" }

        for (config in config) config.reload()
        for (data in data) data.reload()
        for (command in commands) command.register()
        for (listener in listeners) (listener as SimpleListenerHost).registerTo(globalEventChannel())
    }

    override fun onDisable() {
        for (command in commands) command.unregister()
        for (listener in listeners) (listener as SimpleListenerHost).cancel()
    }
}