package com.narrativeprotagonist.choice.service

import com.narrativeprotagonist.choice.repository.ChoiceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChoiceService(
    private val choiceRepository: ChoiceRepository
) {
    // TODO: 비즈니스 로직 작성
}
