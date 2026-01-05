package com.narrativeprotagonist.choice.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import com.narrativeprotagonist.condition.domain.Condition
import com.narrativeprotagonist.node.domain.Node
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class Choice (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val node: Node,
    val text : String = "",

    val createdBy : Long = 0,

    @OneToMany(
        mappedBy = "choice",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    val conditions: MutableList<Condition> = mutableListOf(),

    ): Timestamped();