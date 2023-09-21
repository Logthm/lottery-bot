package com.logs.mirai.lottery.data

import kotlinx.serialization.Serializable

@Serializable
data class Creator(val qq: Long,val groupid: Long, val name: String)
