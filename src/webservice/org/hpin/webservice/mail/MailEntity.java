package org.hpin.webservice.mail;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.mail.EmailAttachment;

/**
 * 邮件实体类;
 * @description: 
 * create by henry.xu 2016年12月14日
 */
@SuppressWarnings("serial")
public class MailEntity implements Serializable {
	public static final String ENCODEING = "UTF-8";  
	  
    private String host; // 服务器地址  smtp.exmail.qq.com
  
    private String sender; // 发件人的邮箱  
  
    private List<String> receiver; // 收件人的邮箱  
  
    private String name; // 发件人昵称  
  
    private String username; // 账号  
  
    private String password; // 密码  
  
    private String subject; // 主题  
  
    private String message; // 信息(支持HTML)  
    
    private List<EmailAttachment> attachMents; //通过文件路径处理后的 EmailAttachment对象;
  
    public List<EmailAttachment> getAttachMents() {
		return attachMents;
	}

	public void setAttachMents(List<File> files) {
		
		List<EmailAttachment> attachMents = new ArrayList<EmailAttachment>();
		if(null != files) {
			EmailAttachment attachment = null;
			for (int i=0; i<files.size(); i++) {
				attachment = new EmailAttachment(); 
				/* 附件的地址 */  
				attachment.setPath(files.get(i).getPath());  
				// 设定为附件  
				attachment.setDisposition(EmailAttachment.ATTACHMENT);  
				/* 附件的描述 */  
				attachment.setDescription("基因系统附件");
				/* 附件的名称，必须和文件名一致 */  
				attachment.setName(files.get(i).getName());  
				
				attachMents.add(attachment);
			}
		}
		
		this.attachMents = attachMents;
	}

	public String getHost() {  
        return host;  
    }  
  
    public void setHost(String host) {  
        this.host = host;  
    }  
  
    public String getSender() {  
        return sender;  
    }  
  
    public void setSender(String sender) {  
        this.sender = sender;  
    }  
  
    public List<String> getReceiver() {
		return receiver;
	}

	public void setReceiver(List<String> receiver) {
		this.receiver = receiver;
	}

	public String getName() {  
        return name;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public String getUsername() {  
        return username;  
    }  
  
    public void setUsername(String username) {  
        this.username = username;  
    }  
  
    public String getPassword() {  
        return password;  
    }  
  
    public void setPassword(String password) {  
        this.password = password;  
    }  
  
    public String getSubject() {  
        return subject;  
    }  
  
    public void setSubject(String subject) {  
        this.subject = subject;  
    }  
  
    public String getMessage() {  
        return message;  
    }  
  
    public void setMessage(String message) {  
        this.message = message;  
    }  
}
