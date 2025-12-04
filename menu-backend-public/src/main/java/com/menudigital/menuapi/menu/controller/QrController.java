package com.menudigital.menuapi.menu.controller;
import com.menudigital.menuapi.menu.service.QrCodeService;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/qrs")
@RequiredArgsConstructor
public class QrController {

    private final QrCodeService qrCodeService;

    @GetMapping("/menu/{companyId}")
    public ResponseEntity<byte[]> getMenuQr(@PathVariable String companyId)
            throws IOException, WriterException {

        byte[] png = qrCodeService.generateMenuQr(companyId);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .body(png);
    }
}

