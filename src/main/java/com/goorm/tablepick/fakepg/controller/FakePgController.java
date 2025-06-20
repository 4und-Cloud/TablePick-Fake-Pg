package com.goorm.tablepick.fakepg.controller;

import com.goorm.tablepick.fakepg.dto.FakePgRequestDto;
import com.goorm.tablepick.fakepg.dto.FakePgResponseDto;
import java.util.Random;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pg")
public class FakePgController {

    private static final Random random = new Random();

    @PostMapping("/approve")
    public ResponseEntity<FakePgResponseDto> fakePay(@RequestBody FakePgRequestDto request) {
        try {
            Thread.sleep(random.nextInt(1000, 1500));
            FakePgResponseDto dto = new FakePgResponseDto();
            dto.setSuccess(true);
            dto.setPaymentId(UUID.randomUUID().toString());
            dto.setErrorMessage(null);

            return ResponseEntity.ok(dto);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

