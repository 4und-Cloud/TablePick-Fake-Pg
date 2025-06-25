package com.goorm.tablepick.fakepg.controller;


import com.goorm.tablepick.fakepg.dto.PgApproveRequestDto;
import com.goorm.tablepick.fakepg.dto.PgApproveResponseDto;
import com.goorm.tablepick.fakepg.dto.PgCallbackDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/api/pg")
@RequiredArgsConstructor
@Slf4j
public class FakePgController {

    private final WebClient.Builder webClientBuilder; // WebClient 주입은 Builder로 받는 게 관례입니다.

    // 실제 PG사는 미리 설정된 가맹점 콜백 URL을 사용하지만,
    // 여기서는 테스트를 위해 요청 시 콜백 URL을 받도록 합니다.
    // 실제 PG사에서는 이 URL이 고정되어 있고, 우리 서버는 그 고정된 URL을 PG사에 등록합니다.
    private static final String SUCCESS_CALLBACK_URL = "http://tablepick-payments-new:8082/api/payments/pg-callback/success"; // 결제 도메인의 콜백 URL
    private static final String FAIL_CALLBACK_URL = "http://tablepick-payments-new:8082/api/payments/pg-callback/fail"; // 결제 도메인의 콜백 URL

    @PostMapping("/approve")
    public ResponseEntity<PgApproveResponseDto> approvePayment(@RequestBody PgApproveRequestDto request) {
        log.info("[Fake PG] 결제 승인 요청 수신: {}", request);

        // 가상 결제 처리 (성공/실패 로직)
        boolean isSuccess = Math.random() > 0; // 100% 확률로 성공

        if (isSuccess) {
            String paymentId = UUID.randomUUID().toString(); // 가상의 결제 ID 생성
            String tid = UUID.randomUUID().toString().substring(0, 8); // 가상의 거래 ID (TID)

            // PG사에서 제공하는 결제 페이지 URL (가상)
            // 실제 PG사에서는 결제 승인 요청 시 이 URL을 반환해줍니다.
            String paymentUrl = "http://fake-pg.com/payment/" + tid;

            // 비동기로 콜백 호출 (실제 PG사는 백그라운드에서 처리)
            callPaymentServiceCallback(request.getReservationId(), paymentId, request.getAmount(), null, true);

            log.info("[Fake PG] 결제 승인 성공, 결제 URL 반환: {}", paymentUrl);
            return ResponseEntity.ok(PgApproveResponseDto.builder()
                    .success(true)
                    .paymentUrl(paymentUrl) // 클라이언트를 리다이렉션할 PG사 결제 페이지 URL
                    .tid(tid) // PG사 거래 ID
                    .paymentId(paymentId) // PG사 최종 결제 ID (여기서는 approve 시 바로 생성)
                    .build());
        } else {
            String errorMessage = "결제 실패: 잔액 부족 또는 카드 오류";

            // 비동기로 실패 콜백 호출
            callPaymentServiceCallback(request.getReservationId(), null, request.getAmount(), errorMessage, false);

            log.warn("[Fake PG] 결제 승인 실패: {}", errorMessage);
            return ResponseEntity.ok(PgApproveResponseDto.builder()
                    .success(false)
                    .errorMessage(errorMessage)
                    .build());
        }
    }

    // 결제 도메인 서비스의 콜백 URL을 호출하는 메서드
    private void callPaymentServiceCallback(Long reservationId, String paymentId, Long amount, String errorMessage, boolean isSuccess) {
        WebClient webClient = webClientBuilder.build(); // 매번 새로운 WebClient 인스턴스 생성 (혹은 싱글톤 사용)

        PgCallbackDto callbackDto = PgCallbackDto.builder()
                .reservationId(reservationId)
                .paymentId(paymentId)
                .amount(amount)
                .errorMessage(errorMessage)
                .build();

        String callbackUrl = isSuccess ? SUCCESS_CALLBACK_URL : FAIL_CALLBACK_URL;

        log.info("[Fake PG] 결제 도메인 콜백 요청 전송 (URL: {}, 데이터: {})", callbackUrl, callbackDto);

        // 비동기적으로 콜백을 호출
        webClient.post()
                .uri(callbackUrl)
                .bodyValue(callbackDto)
                .retrieve()
                .toBodilessEntity() // 응답 본문이 필요 없을 때
                .doOnError(e -> log.error("[Fake PG] 콜백 전송 실패 (URL: {}): {}", callbackUrl, e.getMessage()))
                .subscribe(); // Mono를 구독하여 실행
    }
}

