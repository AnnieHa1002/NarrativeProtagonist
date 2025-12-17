package com.narrativeprotagonist.user.controller

import com.narrativeprotagonist.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

}


