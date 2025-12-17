package com.narrativeprotagonist.variable.service

import com.narrativeprotagonist.variable.repository.VariableRepository
import com.narrativeprotagonist.variable.repository.VariableLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class VariableService(
    private val variableRepository: VariableRepository,
    private val variableLogRepository: VariableLogRepository
) {
    // TODO: 비즈니스 로직 작성
}
