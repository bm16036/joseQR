package com.menudigital.menuapi.menu.service;
//package com.menudigital.menuapi.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class QrCodeService {

    @Value("${app.public-base-url:http://localhost:8080}")
    private String publicBaseUrl;

    public byte[] generateMenuQr(String companyId) throws IOException, WriterException {
        String url = String.format("%s/api/public/companies/%s/menu.pdf", publicBaseUrl, companyId);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, 300, 300);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out);
            return out.toByteArray();
        }
    }
}

