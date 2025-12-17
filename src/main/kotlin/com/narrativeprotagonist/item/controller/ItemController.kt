package com.narrativeprotagonist.item.controller

import com.narrativeprotagonist.item.service.ItemService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/items")
class ItemController(
    private val itemService: ItemService
) {
    // TODO: API 엔드포인트 작성
}
