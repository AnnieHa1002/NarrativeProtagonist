package com.narrativeprotagonist.choice.service

import com.narrativeprotagonist.choice.domain.Choice
import com.narrativeprotagonist.choice.dto.ChoiceRequest
import com.narrativeprotagonist.choice.repository.ChoiceRepository
import com.narrativeprotagonist.node.service.NodeService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChoiceService(
    private val choiceRepository: ChoiceRepository,
    private val nodeService: NodeService
) {
    fun createChoices(userId: Long, requestBody: ChoiceRequest) {
        val node = nodeService.getNodeById(requestBody.originalNodeId)

        for(choiceInfo in requestBody.choices) {
            val choice = Choice(
                node = node,
                text = choiceInfo.text,
                createdBy = userId
            )
            choiceRepository.save(choice)
        }

    }
    // TODO: 비즈니스 로직 작성
}
