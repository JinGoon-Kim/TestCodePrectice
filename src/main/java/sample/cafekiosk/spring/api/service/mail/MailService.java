package sample.cafekiosk.spring.api.service.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.cafekiosk.spring.client.mail.MailSendClient;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistory;
import sample.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSendClient mailSendClient;
    private final MailSendHistoryRepository mailSendHistoryRepository;

    public boolean sendMail(String fromMail, String toEmail, String subject, String content) {
        boolean result = mailSendClient.sendEmail(fromMail, toEmail, subject, content);
        if (result) {
            mailSendHistoryRepository.save(MailSendHistory.builder()
                    .fromEmail(fromMail)
                    .toEmail(toEmail)
                    .subject(subject)
                    .content(content)
                    .build()
            );
            return true;
        }
        return false;
    }
}
