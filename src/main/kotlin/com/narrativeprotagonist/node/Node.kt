package com.narrativeprotagonist.node

import com.narrativeprotagonist._global.enums.NodeType
import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.project.Project
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

    ) : Timestamped()


