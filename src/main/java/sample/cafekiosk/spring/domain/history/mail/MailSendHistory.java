package sample.cafekiosk.spring.domain.history.mail;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MailSendHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    private Long id;

    private String fromEmail;
    private String toEmail;
    private String subject;
    private String content;

    @Builder
    public MailSendHistory(String fromEmail, String toEmail, String subject, String content) {
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.subject = subject;
        this.content = content;
    }
}
