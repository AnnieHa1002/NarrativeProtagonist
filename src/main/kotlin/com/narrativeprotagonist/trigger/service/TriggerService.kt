package com.narrativeprotagonist.trigger.service

import com.narrativeprotagonist.trigger.repository.TriggerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TriggerService(
    private val triggerRepository: TriggerRepository
) {
    // TODO: 비즈니스 로직 작성
}
