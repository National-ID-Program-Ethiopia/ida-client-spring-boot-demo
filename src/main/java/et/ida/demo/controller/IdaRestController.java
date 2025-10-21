package et.ida.demo.controller;

import et.fayda.ida.dto.request.AuthRequestDTO;
import et.fayda.ida.dto.request.AuthTypeDTO;
import et.fayda.ida.dto.request.OtpRequestDTO;
import et.fayda.ida.service.AuthService;
import et.fayda.ida.service.EkycService;
import et.fayda.ida.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("")
public class IdaRestController {

    private final OtpService otpService;
    private final AuthService authService;
    private final EkycService ekycService;

    public IdaRestController(OtpService otpService, AuthService authService, EkycService ekycService) {
        this.otpService = otpService;
        this.authService = authService;
        this.ekycService = ekycService;
    }

    @PostMapping("/otp/{individualId}")
    public ResponseEntity<Map<String, Object>> requestOtp(@PathVariable String individualId) {
        try {
            OtpRequestDTO otpRequest = new OtpRequestDTO();
            otpRequest.setIndividualId(individualId);
            otpRequest.setIndividualIdType("VID");
            otpRequest.setRequestTime(Instant.now().toString());
            otpRequest.setOtpChannel(List.of("EMAIL", "PHONE"));

            Map<String, Object> otpResponse = otpService.requestOtp(otpRequest);
            return ResponseEntity.ok(otpResponse);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "ERROR");
            error.put("message", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/auth/{individualId}/{otp}")
    public ResponseEntity<Map<String, Object>> authenticate(
            @PathVariable String individualId,
            @PathVariable String otp) {

        try {
            AuthRequestDTO authRequest = new AuthRequestDTO();
            authRequest.setId("fayda.identity.auth");
            authRequest.setVersion("1.0");
            authRequest.setTransactionID("1234567890");
            authRequest.setRequestTime(Instant.now().toString());
            authRequest.setConsentObtained(true);
            authRequest.setIndividualId(individualId);
            authRequest.setIndividualIdType("VID");
            authRequest.setOtp(otp);

            AuthTypeDTO authType = new AuthTypeDTO();
            authType.setOtp(true);
            authRequest.setRequestedAuth(authType);

            Map<String, Object> authResponse = authService.authenticate(authRequest);
            return ResponseEntity.ok(authResponse);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "ERROR");
            error.put("message", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/ekyc/{individualId}/{otp}")
    public ResponseEntity<Map<String, Object>> performEkyc(
            @PathVariable String individualId,
            @PathVariable String otp) {

        try {
            AuthRequestDTO ekycRequest = new AuthRequestDTO();
            ekycRequest.setId("fayda.identity.auth");
            ekycRequest.setVersion("1.0");
            ekycRequest.setTransactionID("1234567890");
            ekycRequest.setRequestTime(Instant.now().toString());
            ekycRequest.setConsentObtained(true);
            ekycRequest.setIndividualId(individualId);
            ekycRequest.setIndividualIdType("VID");
            ekycRequest.setOtp(otp);

            AuthTypeDTO authType = new AuthTypeDTO();
            authType.setOtp(true);
            authType.setDemo(true);
            ekycRequest.setRequestedAuth(authType);

            Map<String, Object> ekycResponse = ekycService.performEkyc(ekycRequest);
            return ResponseEntity.ok(ekycResponse);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "ERROR");
            error.put("message", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }

    }
}
