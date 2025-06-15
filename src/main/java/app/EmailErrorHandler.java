package app;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class EmailErrorHandler extends Handler {

    private final String recipient = "Nazarii.Zakharchuk.OI.2024@lpnu.ua"; // Заміни на свій e-mail

    @Override
    public void publish(LogRecord record) {
        if (record.getLevel().intValue() >= Level.SEVERE.intValue()) {
            sendEmail(record.getMessage());
        }
    }

    private void sendEmail(String message) {
        String sender = "Nazarii.Zakharchuk.OI.2024@lpnu.ua"; // Заміни на свою пошту
        String password = "Krusn1Sl1b";          // Пароль або токен

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new jakarta.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, password);
                    }
                });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(sender));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            msg.setSubject("CRITICAL ERROR in JavaFX App");
            msg.setText(message);

            Transport.send(msg);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override public void flush() {}
    @Override public void close() throws SecurityException {}
}
