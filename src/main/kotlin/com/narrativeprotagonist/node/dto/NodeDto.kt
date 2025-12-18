package com.narrativeprotagonist.node.dto

import com.narrativeprotagonist._global.enums.NodeType
import com.narrativeprotagonist.node.domain.Node

data class NodeResponse(
    val id: String,
    val title: String,
    val content: String,
    val projectId: String,
    val nodeType: NodeType,
    val nextNodeId: String?,
    val originalNodeId: String?,
    val version: Int,
    val conditions: String?,
    val effects: String?,
    val createdAt: Long?,
    val modifiedAt: Long?,
    ) {
    constructor(node: Node,  projectId : String) : this(
        id = node.id ?: "",
        title = node.title,
        content = node.content,
        projectId = projectId,
        nodeType = node.nodeType,
        nextNodeId = node.nextNodeId,
        originalNodeId = node.originalNode?.id ,
        version = node.version,
        conditions = node.conditions,
        effects = node.effects,
        createdAt = node.createdAtEpochMilli(),
        modifiedAt = node.modifiedAtEpochMilli(),

        )
}

data class NodeRequest(
    val title: String,
    val content: String,
    val nodeType: NodeType,
    val nextNodeId: String?,
    val conditions: String?,
    val effects: String?,
    val xOffset: Int?,
    val yOffset: Int?,
)
