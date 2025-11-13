package et.ida.demo.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import et.fayda.ida.client.EkycClient;
import et.fayda.ida.client.IdaClientFactory;
import et.fayda.ida.config.IdaConfiguration;

@Configuration
public class IdaClientConfig {

    @Autowired
    private Environment environment;

    @Bean
    public EkycClient ekycClient() {
        // Load properties from Spring Environment (which has resolved placeholders)
        Properties properties = new Properties();

        // Copy all properties from Spring Environment to Properties object
        // Spring Environment already resolves ${...} placeholders
        properties.setProperty("fayda.partner.id", environment.getProperty("fayda.partner.id", ""));
        properties.setProperty("fayda.base.url", environment.getProperty("fayda.base.url", ""));
        properties.setProperty("fayda.env", environment.getProperty("fayda.env", ""));
        properties.setProperty("clientId", environment.getProperty("clientId", ""));
        properties.setProperty("secretKey", environment.getProperty("secretKey", ""));
        properties.setProperty("appId", environment.getProperty("appId", ""));
        properties.setProperty("ida.reference.id", environment.getProperty("ida.reference.id", "PARTNER"));
        properties.setProperty("mispLicenseKey", environment.getProperty("mispLicenseKey", ""));
        properties.setProperty("partnerId", environment.getProperty("partnerId", ""));
        properties.setProperty("partnerApiKey", environment.getProperty("partnerApiKey", ""));
        properties.setProperty("partner.p12.certificate", environment.getProperty("partner.p12.certificate", ""));
        properties.setProperty("partner.p12.certificate.keyAlias", environment.getProperty("partner.p12.certificate.keyAlias", ""));
        properties.setProperty("partner.p12.certificate.keyPassword", environment.getProperty("partner.p12.certificate.keyPassword", ""));
        properties.setProperty("partner.public.key.certificate", environment.getProperty("partner.public.key.certificate", ""));
        properties.setProperty("p12.password", environment.getProperty("p12.password", ""));
        properties.setProperty("p12.path", environment.getProperty("p12.path", ""));
        properties.setProperty("ida.ssl.verify", environment.getProperty("ida.ssl.verify", "false"));

        // These URLs are already resolved by Spring
        properties.setProperty("ida.otp.url", environment.getProperty("ida.otp.url", ""));
        properties.setProperty("ida.auth.url", environment.getProperty("ida.auth.url", ""));
        properties.setProperty("ida.ekyc.url", environment.getProperty("ida.ekyc.url", ""));
        properties.setProperty("ida.certificate.url", environment.getProperty("ida.certificate.url", ""));
        properties.setProperty("ida.authmanager.url", environment.getProperty("ida.authmanager.url", ""));
        properties.setProperty("ida.internal.jwtSign.url", environment.getProperty("ida.internal.jwtSign.url", ""));

        IdaConfiguration configuration = new IdaConfiguration(properties);
        return IdaClientFactory.createClient(configuration);
    }
}

