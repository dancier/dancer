package net.dancier.dancer.mail.configuration;

import liquibase.pro.packaged.P;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfiguration {

    @Bean("javaMailSender")
    @Profile("prod")
    public JavaMailSender getSpringJavaMailSender(
            @Value("${app.mail.host}") String hostname,
            @Value("${app.mail.port}") String port,
            @Value("${app.mail.user}") String user,
            @Value("${app.mail.pass}") String pass
    ) {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(hostname);
        javaMailSender.setPort(Integer.valueOf(port));

        javaMailSender.setUsername(user);
        javaMailSender.setPassword(pass);

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.debug", "true");

        return javaMailSender;
    }

    @Bean("javaMailSender")
        @Profile("staging")
    public JavaMailSender getAllForOneJavaMailSender(
            @Value("${app.mail.host}") String hostname,
            @Value("${app.mail.port}") String port,
            @Value("${app.mail.user}") String user,
            @Value("${app.mail.pass}") String pass
    ) {
        JavaMailSender javaMailSender = new AllForOneMailSender(hostname, Integer.valueOf(port), user, pass);
        return javaMailSender;
    }

    @Bean("javaMailSender")
    @Profile("dev")
    public JavaMailSender getDumpingOnlySender() {
        return new DumpingMailSender();
    }
}
