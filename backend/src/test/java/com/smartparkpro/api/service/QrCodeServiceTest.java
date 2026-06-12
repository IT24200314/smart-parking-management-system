package com.smartparkpro.api.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QrCodeServiceTest {
    @Test
    void generatesBase64PngQrCode() {
        String qr = new QrCodeService().generateBase64Png("SMARTPARKPRO:TEST");

        assertThat(qr).isNotBlank();
        assertThat(qr.length()).isGreaterThan(100);
    }
}
