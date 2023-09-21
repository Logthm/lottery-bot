@file:UseSerializers(DateSerializer::class)

package com.logs.mirai.lottery.data

import com.logs.mirai.lottery.util.CacheType
import com.logs.mirai.lottery.util.cacheFolderByType
import com.logs.mirai.lottery.util.getByUrl
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.Image.Key.queryUrl
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.buildMessageChain
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

object DateSerializer : KSerializer<Date> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeLong(value.time)
    override fun deserialize(decoder: Decoder): Date = Date(decoder.decodeLong())
}


@Serializable
class Lottery {
    val id: Long
    val creator: Creator
    val type: Int // 0：中奖人可重复 1：中奖人不可重复
    val endTime: Date
    private val description: String
    val winnerNum: Int
    val members: MutableList<String> = mutableListOf()
    val winners: MutableList<Long> = mutableListOf()
    val prize: HashMap<String, Int>
    val hasRemind: Boolean = false
    val isEnd: Boolean = false
    private var alternativeJoinCommand: String = ""

    constructor(
        creator: Creator,
        type: Int,
        endTime: Date,
        description: String,
        winnerNum: Int,
        prize: HashMap<String, Int>,
    ) {
        this.id = this.generateID()
        this.creator = creator
        this.type = type
        this.endTime = endTime
        this.description = description
        this.winnerNum = winnerNum
        this.prize = prize
    }

    private fun generateID(): Long {
        val num = ThreadLocalRandom.current().nextLong(1000,9999)
        var duplicate = false

        LotteryList.contents.forEach {
            if (num == it.id)
                duplicate = true
        }

        return if (duplicate) generateID()
        else num
    }

    fun setAlternativeJoinCommand(alternativeJoinCommand: String) {
        LotteryList.contents.forEach {
            if (this != it && alternativeJoinCommand == it.alternativeJoinCommand)
                throw RuntimeException("Duplicate command value!")
        }
        this.alternativeJoinCommand = alternativeJoinCommand
    }

    fun getAlternativeJoinCommand(): String {
        return alternativeJoinCommand
    }

    suspend fun getDescription(contact: Contact): Message {
        val messageChain = buildMessageChain {
            var content = description
            Image.IMAGE_ID_REGEX.findAll(description).forEach {
                val imageId = it.value.trimEnd('"', ',')
                val file = cacheFolderByType(CacheType.IMAGE).resolve(imageId)
                runCatching {
                    val image = file.uploadAsImage(contact)
                    if (imageId != image.imageId)
                        content = description.replace(imageId, image.imageId)
                }.onFailure {
                    val image = Image(imageId)
                    file.getByUrl(image.queryUrl())
                }
            }
            add(MessageChain.deserializeFromJsonString(content))
        }
        return messageChain
    }
}
