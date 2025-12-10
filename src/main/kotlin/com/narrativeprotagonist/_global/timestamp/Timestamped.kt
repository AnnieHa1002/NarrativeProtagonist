package com.narrativeprotagonist._global.timestamp

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class) // 생성/변경 시간을 자동으로 업데이트합니다.
abstract class Timestamped {
    @CreatedDate
    @Column(name = "created_at")
    private var createdAt: LocalDateTime? = null

    @LastModifiedDate
    @Column(name = "modified_at")
    private var modifiedAt: LocalDateTime? = null
}