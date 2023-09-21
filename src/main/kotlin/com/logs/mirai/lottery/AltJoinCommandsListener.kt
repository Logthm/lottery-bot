package com.logs.mirai.lottery

import com.logs.mirai.lottery.data.LotteryList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineName
import net.mamoe.mirai.console.command.ConsoleCommandSender.sendMessage

import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.warning
import kotlin.coroutines.CoroutineContext

//TODO: 处理 Lottery 的加入口令
class AltJoinCommandsListener : SimpleListenerHost() {

    private val logger: MiraiLogger = MiraiLogger.Factory.create(this::class)

    /**
     * 异常处理
     */
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        when (exception) {
            is CancellationException -> {
                // ignore
            }
            // 事件处理中断异常
            is ExceptionInEventHandlerException -> {
                logger.warning({ "exception in ${exception.event}" }, exception.cause)
            }
            else -> {
                logger.warning({ "exception in ${context[CoroutineName]}" }, exception)
            }
        }
    }

    @EventHandler
    suspend fun GroupMessageEvent.onGroupMessage() {
        LotteryList.contents.forEach {
            if (message.contentToString() == it.getAlternativeJoinCommand()) {


                sendMessage("加入成功")
            }
        }
    }


}