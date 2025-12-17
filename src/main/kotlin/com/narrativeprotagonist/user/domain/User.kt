package com.narrativeprotagonist.user.domain

import com.narrativeprotagonist._global.timestamp.Timestamped
import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.util.Locale

@Entity
@Table(name = "app_user")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: String? = null,

    @field:Email
    @field:NotBlank
    @Column(unique = true, nullable = false)
    val email: String,

    var verified: Boolean = false,

    val nickname: String = "",

    val locale : Locale = Locale.getDefault(),

    ) : Timestamped() {

    protected constructor() : this(id = null, email = "", nickname = "")
}
