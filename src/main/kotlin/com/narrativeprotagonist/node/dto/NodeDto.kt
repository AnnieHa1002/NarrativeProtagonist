package com.narrativeprotagonist.node.dto

import com.narrativeprotagonist._global.enums.NodeType
import com.narrativeprotagonist.node.domain.Node

data class NodeResponse(
    val id: Long,
    val title: String,
    val content: String,
    val projectId: Long,
    val nodeType: NodeType,
    val nextNodeId: Long?,
    val originalNodeId: Long?,
    val version: Int,
    val createdAt: Long?,
    val modifiedAt: Long?,
    ) {
    constructor(node: Node,  projectId : Long) : this(
        id = node.id,
        title = node.title,
        content = node.content,
        projectId = projectId,
        nodeType = node.nodeType,
        nextNodeId = node.nextNodeId,
        originalNodeId = node.originalNode?.id,
        version = node.version,
        createdAt = node.createdAtEpochMilli(),
        modifiedAt = node.modifiedAtEpochMilli(),

        )
}

data class NodeRequest(
    val title: String,
    val content: String,
    val nodeType: NodeType,
    val nextNodeId: Long?,
    val xOffset: Int?,
    val yOffset: Int?,
)
