package com.narrativeprotagonist._global.enums

enum class ConditionType {
    // Variable 관련
    VARIABLE_EQUALS,      // 변수 == 값
    VARIABLE_GT,          // 변수 > 값
    VARIABLE_LT,          // 변수 < 값
    VARIABLE_GTE,         // 변수 >= 값
    VARIABLE_LTE,         // 변수 <= 값
    VARIABLE_BETWEEN,     // min <= 변수 <= max

    // Item 관련
    HAS_ITEM,            // 특정 아이템 보유
    NOT_HAS_ITEM,        // 특정 아이템 미보유
    ITEM_COUNT_GT,       // 아이템 개수 > 값

    // 시나리오 관련
    SCENARIO_VIEWED,     // 특정 시나리오 열람함
    SCENARIO_NOT_VIEWED  // 특정 시나리오 미열람
}