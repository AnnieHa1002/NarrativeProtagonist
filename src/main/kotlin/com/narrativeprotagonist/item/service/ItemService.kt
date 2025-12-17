package com.narrativeprotagonist.item.service

import com.narrativeprotagonist.item.repository.ItemRepository
import com.narrativeprotagonist.item.repository.ItemLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ItemService(
    private val itemRepository: ItemRepository,
    private val itemLogRepository: ItemLogRepository
) {
    // TODO: 비즈니스 로직 작성
}
