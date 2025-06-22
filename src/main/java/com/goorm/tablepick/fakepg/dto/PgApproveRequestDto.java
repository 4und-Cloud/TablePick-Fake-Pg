package com.goorm.tablepick.fakepg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// PG사로 결제 승인 요청 시 받는 DTO (결제 도메인의 PaymentRequestDto와 유사)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PgApproveRequestDto {
    private Long reservationId;
    private Long memberId;
    private Long amount;
    // ... PG사에 필요한 추가 정보
}
