package org.pingle.pingleserver.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "데모데이용 QR code", description = "데모데이 X 배너용 qrcode(redirect)")
@RestController
@RequestMapping("/qr")
public class QrController {

    @Value("${qr.ios}")
    private String iOSQr;
    @Value("${qr.android}")
    private String androidQr;


    @GetMapping("/iOS")
    public void getiOSQr(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect(iOSQr);
    }

    @GetMapping("/Android")
    public void getAndroidQr(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect(androidQr);
    }
}
