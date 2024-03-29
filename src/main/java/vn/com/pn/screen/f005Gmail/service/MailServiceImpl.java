package vn.com.pn.screen.f005Gmail.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import vn.com.pn.screen.f002Booking.service.BookingServiceImpl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailServiceImpl implements MailService {
    private static Log logger = LogFactory.getLog(BookingServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(String emailAddress, String emailSubject, StringBuilder text) throws MailException {
        logger.info("MailServiceImpl.sendEmail");

        /*
         * This JavaMailSender Interface is used to send Mail in Spring Boot. This
         * JavaMailSender extends the MailSender Interface which contains send()
         * function. SimpleMailMessage Object is required because send() function uses
         * object of SimpleMailMessage as a Parameter
         */

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(emailAddress);
        mail.setSubject(emailSubject);
        mail.setText(text.toString());

        /*
         * This send() contains an Object of SimpleMailMessage as an Parameter
         */
        javaMailSender.send(mail);
    }

    /**
     * This fucntion is used to send mail that contains a attachment.
     *
     * @param emailAddress
     * @throws MailException
     * @throws MessagingException
     */
    @Override
    public void sendEmailWithAttachment(String emailAddress) throws MailException, MessagingException {
        logger.info("MailServiceImpl.sendEmailWithAttachment");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(emailAddress);
        helper.setSubject("Testing Mail API with Attachment");
        helper.setText("Please find the attached document below.");

        ClassPathResource classPathResource = new ClassPathResource("Attachment.pdf");
        helper.addAttachment(classPathResource.getFilename(), classPathResource);

        javaMailSender.send(mimeMessage);
    }
}
