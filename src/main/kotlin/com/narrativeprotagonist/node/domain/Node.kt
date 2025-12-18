package com.narrativeprotagonist.node.domain

import com.narrativeprotagonist._global.enums.NodeType
import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.project.domain.Project
import jakarta.persistence.*

@Entity
class Node(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    var project: Project,

    var authorId: String,

    var title: String,

    var content: String = "",

    @Enumerated(EnumType.STRING)
    var nodeType: NodeType = NodeType.SCENE,

    var nextNodeId: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_node_id")
    var originalNode: Node? = null,

    var version: Int = 0,

    @Column(columnDefinition = "jsonb")
    var conditions: String? = null,

    @Column(columnDefinition = "jsonb")
    var effects: String? = null,

    var xOffset: Int? = 0,
    var yOffset: Int? = 0,


    ) : Timestamped() {
    fun copy(
        title: String?,
        content: String?,
        nodeType: NodeType?,
        nextNodeId: String?,
        conditions: String?,
        effects: String?,
        xOffset: Int?,
        yOffset: Int?,
    ): Node {
        // if not null, use the new value; otherwise, use the existing value
        // if nextNodeId is blank, set to null
        val title = title ?: this.title
        val content = content ?: this.content
        val nodeType = nodeType ?: this.nodeType
        val nextNodeId = nextNodeId ?: this.nextNodeId ?: ""
        val conditions = conditions ?: this.conditions
        val effects = effects ?: this.effects
        val xOffset = xOffset ?: this.xOffset
        val yOffset = yOffset ?: this.yOffset
        return Node(
            project = this.project,
            authorId = this.authorId,
            title = title,
            content = content,
            nodeType = nodeType,
            nextNodeId = nextNodeId.ifBlank { null },
            originalNode = this.originalNode ?: this,
            version = this.version + 1,
            conditions = conditions,
            effects = effects,
            xOffset = xOffset,
            yOffset = yOffset,
        )
    }

    fun update(
        title: String?,
        content: String?,
        nodeType: NodeType?,
        nextNodeId: String?,
        conditions: String?,
        effects: String?,
        xOffset: Int?,
        yOffset: Int?,
    ) {
        this.title = title ?: this.title
        this.content = content ?: this.content
        this.nodeType = nodeType ?: this.nodeType
        this.nextNodeId = nextNodeId ?: this.nextNodeId ?: ""
        this.conditions = conditions ?: this.conditions
        this.effects = effects ?: this.effects
        this.xOffset = xOffset ?: this.xOffset
        this.yOffset = yOffset ?: this.yOffset
    }
}


