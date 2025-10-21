package et.ida.demo.dto;

import lombok.Data;

@Data
public class AuthOtpRequestDTO {
    private String individualId;
    private String otp;
}
