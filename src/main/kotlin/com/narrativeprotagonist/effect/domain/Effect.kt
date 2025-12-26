package com.narrativeprotagonist.effect.domain

import com.narrativeprotagonist._global.enums.EffectType
import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.item.domain.Item
import com.narrativeprotagonist.node.domain.Node
import com.narrativeprotagonist.variable.domain.Variable
import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(
    name = "effect",
    indexes = [
        Index(name = "idx_effect_node", columnList = "node_id"),
        Index(name = "idx_effect_variable", columnList = "variable_id"),
        Index(name = "idx_effect_item", columnList = "item_id")
    ]
)
class Effect
    (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(
        EnumType.STRING
    )
    @Column(name = "effect_type", nullable = false, length = 50)
    var effectType: EffectType,

    @ManyToOne(fetch = FetchType.LAZY)
    var variable: Variable? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    var item: Item? = null,

    @JdbcTypeCode(SqlTypes.JSON)
    var effectParams: Map<String, Any> = emptyMap(),

    @ManyToOne(fetch = FetchType.LAZY)
    var node: Node
) : Timestamped() {}