package com.goorm.tablepick.fakepg.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
public class FakePgRequestDto {
    private Long reservationId;
    private Long userId;
    private int amount;
}

