package org.hpin.webservice.mail;


import java.util.*;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

/**
 * 发送邮件Util类;
 * @description: 
 * create by henry.xu 2016年12月14日
 */
public class MailUtil {
	private static Logger logger = Logger.getLogger(MailUtil.class);
	
	/**
	 * 根据MailEntity对象值发送邮件;
	 * create by henry.xu 2016年12月14日
	 * @param mail
	 * @return
	 */
	public static boolean send(MailEntity mail) {
        // 发送email  
        HtmlEmail email = new HtmlEmail();  
        try {  
        	
            // 这里是SMTP发送服务器的名字：163的如下："smtp.163.com"  
            email.setHostName(mail.getHost());  
            // 字符编码集的设置  
            email.setCharset(MailEntity.ENCODEING);  
            
            // 收件人的邮箱  
            List<String> receivers = mail.getReceiver();
            if(null != receivers) {
            	for (String receiver : receivers) {
            		email.addTo(receiver);  
					
				}
            }
            // 发送人的邮箱  
            email.setFrom(mail.getSender(), mail.getName());  
            // 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和密码  
            email.setAuthentication(mail.getUsername(), mail.getPassword());  
            // 要发送的邮件主题  
            email.setSubject(mail.getSubject());  
            // 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签  
            email.setMsg(mail.getMessage());  
            
            List<EmailAttachment> attachMents = mail.getAttachMents();
            if(null != attachMents) {
            	for (EmailAttachment attach : attachMents) {
            		email.attach(attach);            	
				}
            }
            // 发送  
            email.send();  
            logger.debug(mail.getSender() + " 发送邮件到 " + mail.getReceiver());  
            return true;  
        } catch (EmailException e) {  
            logger.info(mail.getSender() + " 发送邮件到 " + mail.getReceiver() + " 失败", e);  
            return false;  
        }  
    }
}
