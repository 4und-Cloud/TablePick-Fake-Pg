package com.goorm.tablepick.fakepg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FakePgResponseDto {
    private boolean success;
    private String paymentId;
    private String errorMessage;
}
