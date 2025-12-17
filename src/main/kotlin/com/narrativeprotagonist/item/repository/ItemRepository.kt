package com.narrativeprotagonist.item.repository

import com.narrativeprotagonist.item.domain.Item
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ItemRepository : JpaRepository<Item, String> {
    // TODO: 추가 쿼리 메서드 작성
}
