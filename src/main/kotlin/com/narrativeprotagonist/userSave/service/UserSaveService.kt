package com.narrativeprotagonist.userSave.service

import com.narrativeprotagonist.userSave.repository.UserSaveRepository
import com.narrativeprotagonist.userSave.repository.EventLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserSaveService(
    private val userSaveRepository: UserSaveRepository,
    private val eventLogRepository: EventLogRepository
) {
    // TODO: 비즈니스 로직 작성
}
