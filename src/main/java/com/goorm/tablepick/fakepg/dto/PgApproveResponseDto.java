package com.goorm.tablepick.fakepg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// PG사에서 결제 승인 요청에 대한 응답 DTO (결제 도메인의 PaymentResponseDto와 유사)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PgApproveResponseDto {
    private boolean success;
    private String paymentUrl; // 사용자가 리다이렉션될 PG사 결제 페이지 URL
    private String tid; // PG사 거래 ID
    private String paymentId; // PG사 내부에서 발급한 최종 결제 ID
    private String errorMessage;
}
