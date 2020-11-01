/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timetrackingexam.bll;

import java.sql.Connection;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import timetrackingexam.dal.Authentication;
import timetrackingexam.dal.ConnectionPool;
import timetrackingexam.help.Hash;

/**
 *
 * @author domin
 */
public class CredentialManager {

    ConnectionPool conpool = ConnectionPool.getInstance();
    Authentication au = new Authentication();
    Hash hash = new Hash();

    public boolean isEmailValid(String email) {
        Connection con = conpool.checkOut();
        boolean res = au.isEmailValid(con, email);
        conpool.checkIn(con);
        return res;
    }

    public void resetPassword(String email) {
        String newPassword = randomString();
        String newPasswordHashed = hash.hashPass(newPassword);
        Connection con = conpool.checkOut();
        au.resetPassword(con, email, newPasswordHashed);
        conpool.checkIn(con);
        sendMail(email, newPassword);
    }

    private String randomString() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    private void sendMail(String email, String newPass) {
        // Set up the SMTP server.
        java.util.Properties props = new java.util.Properties();
        props.put("mail.smtp.host", "smtp.myisp.com");
        Session session = Session.getDefaultInstance(props, null);

        // Construct the message
        String to = email;
        String from = "passwordreset@grumsen.dk";
        String subject = "password reset";
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setText("new password is: " + newPass);

            // Send the message.
            Transport.send(msg);
        } catch (MessagingException e) {
            System.out.println(e);
        }
    }
    
    public void userSetPassword(int id, String newPassword){
        Connection con = conpool.checkOut();
        String newPasswordHashed = hash.hashPass(newPassword);
        au.userSetPassword(con, id, newPasswordHashed);
        conpool.checkIn(con);
    }
}
