package com.narrativeprotagonist._global.enums

enum class EffectType {
    // Variable 관련
    SET_VARIABLE,        // 변수 = 값
    ADD_VARIABLE,        // 변수 += 값
    SUBTRACT_VARIABLE,   // 변수 -= 값
    MULTIPLY_VARIABLE,   // 변수 *= 값

    // Item 관련
    ADD_ITEM,           // 아이템 획득
    REMOVE_ITEM,        // 아이템 삭제
    REPLACE_ITEM,       // 아이템 교체
    CHANGE_ITEM_COUNT,  // 아이템 수량 변경

    // 시나리오 관련
    MARK_SCENARIO_VIEWED  // 시나리오 열람 기록
}