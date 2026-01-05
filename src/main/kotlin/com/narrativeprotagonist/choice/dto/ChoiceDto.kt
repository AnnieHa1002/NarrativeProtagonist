package com.narrativeprotagonist.choice.dto

data class ChoiceRequest(
    val choices: List<ChoiceInfo>,
    val originalNodeId: Long
)

data class ChoiceInfo(
    val id: Long?,
    val text: String,
    val conditions: String
)