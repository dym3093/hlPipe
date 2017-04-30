package org.hpin.webservice.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import sun.misc.BASE64Encoder;

public class SimpleMailSender {
	
	public static boolean sendTextMail(MailSenderInfo mailInfo) {
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			Message mailMessage = new MimeMessage(sendMailSession);
			Address from = new InternetAddress(mailInfo.getFromAddress());
			mailMessage.setFrom(from);
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			mailMessage.setSubject(mailInfo.getSubject());
			mailMessage.setSentDate(new Date());
			String mailContent = mailInfo.getContent();
			//mailMessage.setText(mailContent);
			
			// 构造Multipart
			Multipart mp = new MimeMultipart();
	
			// 向Multipart添加正文
			MimeBodyPart content = new MimeBodyPart();
			content.setText(mailContent.toString(), "UTF-8");
			//添加正文
			mp.addBodyPart(content);
			List<File> attachements = mailInfo.getAttaches();
			BASE64Encoder enc = new BASE64Encoder();
			
			if(attachements!=null&&attachements.size()>0){
				MimeBodyPart mbpFile = null;
				for (File file : attachements) {
					 if(file.exists()){
						mbpFile = new MimeBodyPart();
						FileDataSource fds = new FileDataSource(file);
						mbpFile.setFileName(MimeUtility.encodeWord(fds.getName()));
						mbpFile.setDataHandler(new DataHandler(fds));
						//添加附件
						mp.addBodyPart(mbpFile);
					 }
				}	
			}
			//所有内容添加到邮件中
			mailMessage.setContent(mp);
			mailMessage.saveChanges();
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean sendHtmlMail(MailSenderInfo mailInfo) {
		MyAuthenticator authenticator = null;
		Properties pro = mailInfo.getProperties();
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(mailInfo.getUserName(),
					mailInfo.getPassword());
		}
		Session sendMailSession = Session
				.getDefaultInstance(pro, authenticator);
		try {
			Message mailMessage = new MimeMessage(sendMailSession);
			Address from = new InternetAddress(mailInfo.getFromAddress());
			mailMessage.setFrom(from);
			Address to = new InternetAddress(mailInfo.getToAddress());
			mailMessage.setRecipient(Message.RecipientType.TO, to);
			mailMessage.setSubject(mailInfo.getSubject());
			mailMessage.setSentDate(new Date());
			Multipart mainPart = new MimeMultipart();
			BodyPart html = new MimeBodyPart();
			html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
			mainPart.addBodyPart(html);
			mailMessage.setContent(mainPart);
			return true;
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
}
