package com.narrativeprotagonist.userSave.repository

import com.narrativeprotagonist.userSave.domain.UserSave
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserSaveRepository : JpaRepository<UserSave, String> {
    // TODO: 추가 쿼리 메서드 작성
}
