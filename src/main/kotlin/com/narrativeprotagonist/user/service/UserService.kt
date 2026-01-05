package com.narrativeprotagonist.user.service

import com.narrativeprotagonist._global.exception.BusinessException
import com.narrativeprotagonist.user.domain.User
import com.narrativeprotagonist.auth.dto.SignUpRequest
import com.narrativeprotagonist.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {
    fun getUserById(id: Long): User =
        userRepository.findByIdOrNull(id)
            ?: throw BusinessException.UserNotFound(id)

    fun getUserByEmail(email: String): User? =
        userRepository.findByEmail(email)

    fun createUser(request: SignUpRequest): User {
        val email = request.email
        if (userRepository.existsByEmail(email)) {
            throw BusinessException.DuplicateEmail(email)
        }

        return userRepository.save(User(id = 0, email = email, nickname = request.nickname))
    }

    fun getAllUsers(): List<User> =
        userRepository.findAll()
}