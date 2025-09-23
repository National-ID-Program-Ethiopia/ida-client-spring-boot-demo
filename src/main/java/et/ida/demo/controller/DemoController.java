package et.ida.demo.controller;

import et.fayda.ida.dto.request.AuthRequestDTO;
import et.fayda.ida.dto.request.AuthTypeDTO;
import et.fayda.ida.dto.request.OtpRequestDTO;
import et.fayda.ida.service.AuthService;
import et.fayda.ida.service.EkycService;
import et.fayda.ida.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DemoController {

    private final AuthService authService;
    private final OtpService otpService;
    private final EkycService ekycService;

    @Autowired
    public DemoController(AuthService authService, OtpService otpService, EkycService ekycService) {
        this.authService = authService;
        this.otpService = otpService;
        this.ekycService = ekycService;
    }

    // Test OTP
    @GetMapping("/test-otp")
    public Map<String, Object> testOtp() {
        OtpRequestDTO otpRequest = new OtpRequestDTO();
        otpRequest.setIndividualId("6398491650413561");
        otpRequest.setIndividualIdType("VID");
        otpRequest.setRequestTime(Instant.now().toString());
        otpRequest.setOtpChannel(new String[]{"EMAIL", "PHONE"});

        Map<String, Object> response = otpService.requestOtp(otpRequest);
        if (response == null) {
            response = new HashMap<>();
            response.put("status", "OTP_SERVICE_NOT_AVAILABLE");
        }
        return response;
    }

    // Test Auth
    @GetMapping("/test-auth")
    public Map<String, Object> testAuth() {
        AuthRequestDTO authRequest = new AuthRequestDTO();
        authRequest.setId("mosip.identity.auth");
        authRequest.setVersion("1.0");
        authRequest.setTransactionID("1234567890");
        authRequest.setRequestTime(Instant.now().toString());
        authRequest.setConsentObtained(true);
        authRequest.setIndividualId("23627");
        authRequest.setIndividualIdType("VID");
        authRequest.setOtp("23637");

        AuthTypeDTO authType = new AuthTypeDTO();
        authType.setOtp(true);
        authRequest.setRequestedAuth(authType);

        Map<String, Object> response = authService.authenticate(authRequest);
        if (response == null) {
            response = new HashMap<>();
            response.put("status", "AUTH_SERVICE_NOT_AVAILABLE");
        }
        return response;
    }

    // Test eKYC
    @GetMapping("/test-ekyc")
    public Map<String, Object> testEkyc() {
        AuthRequestDTO authRequest = new AuthRequestDTO();
        authRequest.setId("mosip.identity.auth");
        authRequest.setVersion("1.0");
        authRequest.setTransactionID("1234567890");
        authRequest.setRequestTime(Instant.now().toString());
        authRequest.setConsentObtained(true);
        authRequest.setIndividualId("7237883");
        authRequest.setIndividualIdType("VID");
        authRequest.setOtp("33333");

        AuthTypeDTO authType = new AuthTypeDTO();
        authType.setOtp(true);
        authType.setDemo(true);
        authRequest.setRequestedAuth(authType);

        Map<String, Object> response = ekycService.performEkyc(authRequest);
        if (response == null) {
            response = new HashMap<>();
            response.put("status", "EKYC_SERVICE_NOT_AVAILABLE");
        }
        return response;
    }
}
