package com.narrativeprotagonist.condition.domain

import com.narrativeprotagonist._global.enums.ConditionType
import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.item.domain.Item
import com.narrativeprotagonist.node.domain.Node
import com.narrativeprotagonist.variable.domain.Variable
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Type

@Entity
@Table(
    indexes = [
        Index(name = "idx_condition_node", columnList = "node_id"),
        Index(name = "idx_condition_variable", columnList = "variable_id"),
        Index(name = "idx_condition_item", columnList = "item_id")
    ]
)
class Condition(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Enumerated(
        EnumType.STRING)
    @Column(name = "condition_type", nullable = false, length = 50)
    var conditionType : ConditionType,

    @ManyToOne(fetch = FetchType.LAZY)
    var variable: Variable? =null,

    @ManyToOne(fetch = FetchType.LAZY)
    var item : Item? = null,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var conditionParams: Map<String, Any> = emptyMap(),

    @ManyToOne(fetch = FetchType.LAZY)
    var node : Node

)  : Timestamped() {}