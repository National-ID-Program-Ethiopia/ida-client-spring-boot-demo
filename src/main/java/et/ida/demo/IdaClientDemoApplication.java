package et.ida.demo;

import et.fayda.ida.dto.request.AuthRequestDTO;
import et.fayda.ida.dto.request.AuthTypeDTO;
import et.fayda.ida.dto.request.OtpRequestDTO;
import et.fayda.ida.service.AuthService;
import et.fayda.ida.service.EkycService;
import et.fayda.ida.service.OtpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sound.midi.Soundbank;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class IdaClientDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdaClientDemoApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		return mapper;
	}

	@Bean
	public CommandLineRunner runDemo(AuthService authService, OtpService otpService, EkycService ekycService) {
		return args -> {
			if (args.length < 2) {
				System.out.println("Usage: java -jar app.jar <otp|auth|ekyc> <individualId> [otpValue]");
				return;
			}

			String action = args[0].toLowerCase();
			String individualId = args[1];
			String otpValue = args.length >= 3 ? args[2] : null;

			switch (action) {
				case "otp":
					System.out.println("Sending otp request....");
					OtpRequestDTO otpRequest = new OtpRequestDTO();
					otpRequest.setIndividualId(individualId);
					otpRequest.setIndividualIdType("VID");
					otpRequest.setRequestTime(Instant.now().toString());
					otpRequest.setOtpChannel(List.of("EMAIL", "PHONE"));

					Map<String, Object> otpResponse = otpService.requestOtp(otpRequest);
					if (otpResponse == null) {
						otpResponse = new HashMap<>();
						otpResponse.put("status", "OTP_SERVICE_NOT_AVAILABLE");
					}
					System.out.println("OTP Response: " + otpResponse);
					break;

				case "auth":
					if (otpValue == null) {
						System.out.println("For auth, you must provide the OTP value as the third argument.");
						return;
					}
					System.out.println("Sending auth request....");

					AuthRequestDTO authRequest = new AuthRequestDTO();
					authRequest.setId("mosip.identity.auth");
					authRequest.setVersion("1.0");
					authRequest.setTransactionID("1234567890");
					authRequest.setRequestTime(Instant.now().toString());
					authRequest.setConsentObtained(true);
					authRequest.setIndividualId(individualId);
					authRequest.setIndividualIdType("VID");
					authRequest.setOtp(otpValue);

					AuthTypeDTO authType = new AuthTypeDTO();
					authType.setOtp(true);
					authRequest.setRequestedAuth(authType);

					Map<String, Object> authResponse = authService.authenticate(authRequest);
					if (authResponse == null) {
						authResponse = new HashMap<>();
						authResponse.put("status", "AUTH_SERVICE_NOT_AVAILABLE");
					}
					System.out.println("Auth Response: " + authResponse);
					break;

				case "ekyc":
					if (otpValue == null) {
						System.out.println("For ekyc, you must provide the OTP value as the third argument.");
						return;
					}

					System.out.println("Sending ekyc request....");

					AuthRequestDTO ekycRequest = new AuthRequestDTO();
					ekycRequest.setId("mosip.identity.auth");
					ekycRequest.setVersion("1.0");
					ekycRequest.setTransactionID("1234567890");
					ekycRequest.setRequestTime(Instant.now().toString());
					ekycRequest.setConsentObtained(true);
					ekycRequest.setIndividualId(individualId);
					ekycRequest.setIndividualIdType("VID");
					ekycRequest.setOtp(otpValue);

					AuthTypeDTO ekycAuthType = new AuthTypeDTO();
					ekycAuthType.setOtp(true);
					ekycAuthType.setDemo(true);
					ekycRequest.setRequestedAuth(ekycAuthType);

					Map<String, Object> ekycResponse = ekycService.performEkyc(ekycRequest);
					if (ekycResponse == null) {
						ekycResponse = new HashMap<>();
						ekycResponse.put("status", "EKYC_SERVICE_NOT_AVAILABLE");
					}
					System.out.println("eKYC Response: " + ekycResponse);
					break;

				default:
					System.out.println("Unknown action: " + action);
					System.out.println("Valid options: otp, auth, ekyc");
			}
		};
	}
}
