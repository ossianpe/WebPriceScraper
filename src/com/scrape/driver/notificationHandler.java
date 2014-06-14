package com.scrape.driver;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;

class NotificationHandler { 
	
		static PrivateData prive = new PrivateData();
		public static void NotificationSender(String passEmail, String notSubject, String notBody, String notPhoneNumber) {
			try {
				System.out.println("Sending email and SMS");
				TwilioSMS(notSubject, notBody, notPhoneNumber);
				System.out.print("Email Sent");
				SendEmailGoogle(passEmail, notSubject, notBody);
				System.out.println(" & Text Sent!");
			} catch (TwilioRestException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	  
		public static void TwilioSMS(String subject, String body, String phoneNumber) throws TwilioRestException {
		    TwilioRestClient client = new TwilioRestClient(prive.getACCOUNT_SID(), prive.getAUTH_TOKEN());
			 
		    // Build a filter for the MessageList
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("Body", ("\n" + subject + "\n" + body)));
			//	    + "\n" + notificationBody)));
			params.add(new BasicNameValuePair("To", "+1" + phoneNumber));
			params.add(new BasicNameValuePair("From", "+1" + prive.getFROM_PHONE_NUMBER()));
			//	    params.add(new BasicNameValuePair("MediaUrl", "http://www.example.com/hearts.png"));
			 
			MessageFactory messageFactory = client.getAccount().getMessageFactory();
			com.twilio.sdk.resource.instance.Message message = (com.twilio.sdk.resource.instance.Message)messageFactory.create(params);
			//	    System.out.println(message.getSid());
		}
	  
		//Send Email through Google SSL
		public static void SendEmailGoogle(String nEmail, String nSubject, String nBody) {
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
	 
			Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(prive.getGMAIL_ACCOUNT(),prive.getGMAIL_PASSWORD());
					}
				});
	 
			try {
	 
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("frombob@no-spam.com"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(nEmail));
//						InternetAddress.parse("2489825810@tmomail.net"));
				message.setSubject(nSubject);
				message.setText(nBody); 
				Transport.send(message);
	 
				System.out.println("Done");
	 
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		}
}