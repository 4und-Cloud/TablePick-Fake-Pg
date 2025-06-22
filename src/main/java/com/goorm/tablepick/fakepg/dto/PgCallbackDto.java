package com.goorm.tablepick.fakepg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// PG사에서 우리 서비스로 콜백 시 전송하는 DTO
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PgCallbackDto {
    private Long reservationId;
    private String paymentId; // 최종 결제 ID (성공 시)
    private Long amount; // 결제 금액
    private String errorMessage; // 실패 시 오류 메시지
    // ... PG사마다 추가 정보 (예: 승인 일시, 카드 정보 등)
}