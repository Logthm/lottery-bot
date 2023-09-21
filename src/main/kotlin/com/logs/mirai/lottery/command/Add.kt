package com.logs.mirai.lottery.command

import com.logs.mirai.lottery.LotteryBot
import com.logs.mirai.lottery.config.CommandConfig
import com.logs.mirai.lottery.config.GeneralConfig
import com.logs.mirai.lottery.data.Creator
import com.logs.mirai.lottery.data.Lottery
import com.logs.mirai.lottery.data.LotteryList
import com.logs.mirai.lottery.util.CacheType
import com.logs.mirai.lottery.util.cacheFolderByType
import com.logs.mirai.lottery.util.getByUrl
import kotlinx.coroutines.sync.Mutex
import net.mamoe.mirai.console.command.CommandSenderOnMessage
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.MessageChain.Companion.serializeToJsonString
import net.mamoe.mirai.message.nextMessage
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId

public object Add : SimpleCommand(
    owner = LotteryBot,
    primaryName = "lot add",
    secondaryNames = CommandConfig.add,
    description = "发起一个抽奖"
) {

    @ConsoleExperimentalApi
    @ExperimentalCommandDescriptors
    override val prefixOptional = true

    @Handler
    suspend fun CommandSenderOnMessage<GroupMessageEvent>.handle() {
        val sender = fromEvent.sender
        val subject = fromEvent.subject

        val mutex = Mutex()

        if (mutex.isLocked) {
            sendMessage("正在处理其他创建请求，请稍后")
            return
        }
        mutex.lock()

        val groupID = subject.id



        sendMessage(
            "请发送发送该抽奖的描述信息\n" +
                    "（支持图片、文字以及@）"
        )
        val description = try {
            fromEvent.nextMessage(GeneralConfig.wait_time)
        } catch (e: Throwable) {
            sendMessage("未识别消息，请重新创建！")
            mutex.unlock()
            return
        }

        sendMessage(
            "请发送该抽奖的截止时间\n" +
                    "时间格式为 年-月-日-时-分\n" +
                    "例：2077-11-4-5-14"
        )
        val endTime = try {
            SimpleDateFormat("yyyy-MM-dd-HH-mm").parse(
                fromEvent.nextMessage(GeneralConfig.wait_time).contentToString()
            )
        } catch (e: Throwable) {
            sendMessage("未识别消息，请重新创建！")
            mutex.unlock()
            return
        }
        if (LocalDateTime.now().isAfter(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()))) {
            sendMessage("截止时间不能早于现在时间，请重新创建！")
            mutex.unlock()
            return
        }

        sendMessage(
            "请发送该抽奖的抽选类型（仅回复数字）\n" +
                    "0：中奖人可重复 1：中奖人不可重复\n" +
                    "（中奖人不可重复时，若参与人数小于奖品数，将在覆盖每一个人的前提下产生重复中奖）"
        )
        val lotteryType = try {
            fromEvent.nextMessage(GeneralConfig.wait_time).contentToString().toInt()
        } catch (e: Throwable) {
            sendMessage("未识别消息，请重新创建！")
            mutex.unlock()
            return
        }

        sendMessage(
            "请发送抽奖奖品列表（一行一项，仅支持文字）\n" +
                    "注：可在每一行末尾添加 *n 来指定个数，如”色纸*3“会抽取三份色纸"
        )
        val prizeList = try {
            fromEvent.nextMessage(GeneralConfig.wait_time).contentToString()

        } catch (e: Throwable) {
            sendMessage("未识别消息，请重新创建！")
            mutex.unlock()
            return
        }
        var winnerNum = 0
        val prize = HashMap<String, Int>()
        try {
            prizeList.split("\n").forEach {
                var prizeNum: Int
                val prizeContent: String
                if (it.matches(Regex("^.+\\*[0-9]+$"))) {
                    prizeNum = it.substring(it.lastIndexOf("*") + 1).toInt()
                    prizeContent = it.substring(0, it.lastIndexOf("*"))
                } else {
                    prizeNum = 1
                    prizeContent = it
                }
                if (prize.contains(prizeContent)) {
                    prizeNum += prize.get(prizeContent)!!
                    prize.put(prizeContent, prizeNum)
                } else {
                    prize.put(prizeContent, prizeNum)
                }
            }
            for (p in prize) {
                winnerNum += p.value
            }
        } catch (e: Throwable) {
            sendMessage("格式错误，请重新创建！")
            mutex.unlock()
            return
        }

        sendMessage("请发送该抽奖的替换加入口令，如“ 我要抽！ ”\n添加后其他人可以通过发送该口令快捷加入抽奖\n若不需添加替换口令，请回复 0 以完成创建")
        val alternativeJoinCommand = try {
            fromEvent.nextMessage(GeneralConfig.wait_time).contentToString()
        } catch (e: Throwable) {
            sendMessage("未识别消息，请重新创建！")
            mutex.unlock()
            return
        }
        // Cache for img
        description.forEach {
            if (it is Image)
                cacheFolderByType(CacheType.IMAGE).resolve(it.imageId).getByUrl(it.queryUrl())
        }

        val creator = Creator(
            sender.id,
            groupID,
            sender.nick
        )
        val newLottery = Lottery(
            creator,
            lotteryType,
            endTime,
            description.serializeToJsonString(),
            winnerNum,
            prize,
        )
        if (alternativeJoinCommand != "0") newLottery.setAlternativeJoinCommand(alternativeJoinCommand)
        LotteryList.contents.add(newLottery)
        sendMessage(
            "创建成功，抽奖id为${newLottery.id}\n" +
                    "您可以通过 /detail ${newLottery.id} 来查看创建的抽奖"
        )

        mutex.unlock()
    }

    @Handler
    suspend fun CommandSenderOnMessage<FriendMessageEvent>.handle() {
        val sender = fromEvent.sender
        val subject = fromEvent.subject
        val mutex = Mutex()

        if (mutex.isLocked) {
            sendMessage("正在处理其他创建请求，请稍后")
            return
        }
        mutex.lock()

        // 通过私聊创建抽奖
        sendMessage("请发送您要创建抽奖所在的群号")
        val groupID = try {
            fromEvent.nextMessage(GeneralConfig.wait_time).contentToString().toLong()
        } catch (e: Throwable) {
            sendMessage("未识别消息，请重新创建！")
            mutex.unlock()
            return
        }
        if (bot?.getGroup(groupID)?.contains(sender.id) != true) {
            sendMessage("您或bot不在该群！")
            mutex.unlock()
            return
        }

        sendMessage(
            "请发送发送该抽奖的描述信息\n" +
                    "（支持图片、文字以及@）"
        )
        val description = try {
            fromEvent.nextMessage(GeneralConfig.wait_time)
        } catch (e: Throwable) {
            sendMessage("未识别消息，请重新创建！")
            mutex.unlock()
            return
        }

        sendMessage(
            "请发送该抽奖的截止时间\n" +
                    "时间格式为 年-月-日-时-分\n" +
                    "例：2077-11-4-5-14"
        )
        val endTime = try {
            SimpleDateFormat("yyyy-MM-dd-HH-mm").parse(
                fromEvent.nextMessage(GeneralConfig.wait_time).contentToString()
            )
        } catch (e: Throwable) {
            sendMessage("未识别消息，请重新创建！")
            mutex.unlock()
            return
        }
        if (LocalDateTime.now().isAfter(LocalDateTime.ofInstant(endTime.toInstant(), ZoneId.systemDefault()))) {
            sendMessage("截止时间不能早于现在时间，请重新创建！")
            mutex.unlock()
            return
        }

        sendMessage(
            "请发送该抽奖的抽选类型（仅回复数字）\n" +
                    "0：中奖人可重复 1：中奖人不可重复\n" +
                    "（中奖人不可重复时，若参与人数小于奖品数，将在覆盖每一个人的前提下产生重复中奖）"
        )
        val lotteryType = try {
            fromEvent.nextMessage(GeneralConfig.wait_time).contentToString().toInt()
        } catch (e: Throwable) {
            sendMessage("未识别消息，请重新创建！")
            mutex.unlock()
            return
        }

        sendMessage(
            "请发送抽奖奖品列表（一行一项，仅支持文字）\n" +
                    "注：可在每一行末尾添加 *n 来指定个数，如”色纸*3“会抽取三份色纸"
        )
        val prizeList = try {
            fromEvent.nextMessage(GeneralConfig.wait_time).contentToString()

        } catch (e: Throwable) {
            sendMessage("未识别消息，请重新创建！")
            mutex.unlock()
            return
        }
        var winnerNum = 0
        val prize = HashMap<String, Int>()
        try {
            prizeList.split("\n").forEach {
                var prizeNum: Int
                val prizeContent: String
                if (it.matches(Regex("^.+\\*[0-9]+$"))) {
                    prizeNum = it.substring(it.lastIndexOf("*") + 1).toInt()
                    prizeContent = it.substring(0, it.lastIndexOf("*"))
                } else {
                    prizeNum = 1
                    prizeContent = it
                }
                if (prize.contains(prizeContent)) {
                    prizeNum += prize.get(prizeContent)!!
                    prize.put(prizeContent, prizeNum)
                } else {
                    prize.put(prizeContent, prizeNum)
                }
            }
            for (p in prize) {
                winnerNum += p.value
            }
        } catch (e: Throwable) {
            sendMessage("格式错误，请重新创建！")
            mutex.unlock()
            return
        }

        sendMessage("请发送该抽奖的替换加入口令，如“ 我要抽！ ”\n添加后其他人可以通过发送该口令快捷加入抽奖\n若不需添加替换口令，请回复 0 以完成创建")
        val alternativeJoinCommand = try {
            fromEvent.nextMessage(GeneralConfig.wait_time).contentToString()
        } catch (e: Throwable) {
            sendMessage("未识别消息，请重新创建！")
            mutex.unlock()
            return
        }
        // Cache for img
        description.forEach {
            if (it is Image)
                cacheFolderByType(CacheType.IMAGE).resolve(it.imageId).getByUrl(it.queryUrl())
        }

        val creator = Creator(
            sender.id,
            groupID,
            sender.nick
        )
        val newLottery = Lottery(
            creator,
            lotteryType,
            endTime,
            description.serializeToJsonString(),
            winnerNum,
            prize,
        )
        if (alternativeJoinCommand != "0") newLottery.setAlternativeJoinCommand(alternativeJoinCommand)
        LotteryList.contents.add(newLottery)
        sendMessage(
            "向群 ${groupID} 创建抽奖成功，抽奖id为 ${newLottery.id}\n" +
                    "您可以通过 /detail ${newLottery.id} 来查看创建的抽奖"
        )

        mutex.unlock()
    }
}