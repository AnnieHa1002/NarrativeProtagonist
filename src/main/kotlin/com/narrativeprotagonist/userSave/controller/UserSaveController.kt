package com.narrativeprotagonist.userSave.controller

import com.narrativeprotagonist.userSave.service.UserSaveService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user-saves")
class UserSaveController(
    private val userSaveService: UserSaveService
) {
    // TODO: API 엔드포인트 작성
}
