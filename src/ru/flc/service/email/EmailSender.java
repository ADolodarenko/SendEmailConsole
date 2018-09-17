package ru.flc.service.email;

import ru.flc.service.exceptions.WrongParametersException;
import ru.flc.service.exceptions.WrongPropertiesException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailSender
{
	private static final String INCORRECT_PROPERTIES_MESSAGE = "Incorrect properties.";
	private static final String[] MANDATORY_PROPERTY_NAMES = {"mail.smtp.host", "mail.smtp.port", "mail.user"};

	private static Logger log = Logger.getLogger(EmailSender.class.getName());

	private Properties properties;
	private Session mailSession;

	public EmailSender(Properties properties) throws WrongPropertiesException
	{
		if (properties == null && properties.isEmpty())
			throw new WrongPropertiesException(INCORRECT_PROPERTIES_MESSAGE);

		for (String name : MANDATORY_PROPERTY_NAMES)
			if (!properties.containsKey(name))
				throw new WrongPropertiesException(INCORRECT_PROPERTIES_MESSAGE);

		this.properties = properties;
	}

	public void open() throws WrongPropertiesException
	{
		openSession(buildProperties());
	}
	
	private Properties buildProperties()
	{
		Properties result = new Properties();
		
		Enumeration propertyNames = properties.propertyNames();
		while (propertyNames.hasMoreElements())
		{
			String name = (String)propertyNames.nextElement();
			
			if (name.startsWith("mail.smtp."))
				result.setProperty(name, properties.getProperty(name));
		}
		
		return result;
	}
	
	private void openSession(Properties sessionProperties) throws WrongPropertiesException
	{
		Authenticator authenticator = null;
		
		String auth = properties.getProperty("mail.smtp.auth");
		if ("true".equals(auth))
		{
			final String user = properties.getProperty("mail.user");
			
			if (user == null)
				throw new WrongPropertiesException("There has to be a user for authentication.");
			
			final String password = properties.getProperty("mail.password");
			
			authenticator = new Authenticator()
			{
				@Override
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(user, password);
				}
			};
		}
		
		if (authenticator != null)
			mailSession = Session.getInstance(sessionProperties, authenticator);
		else
			mailSession = Session.getInstance(sessionProperties);
	}
	
	public void sendAllEmails(List<Email> emails) throws WrongParametersException
	{
		if (mailSession == null)
			throw new WrongParametersException("There are now session established.");
		
		if (emails == null || emails.isEmpty())
			throw new WrongParametersException("There are now emails to send.");
		
		for (Email email : emails)
			try
			{
				sendOneEmail(email);
			}
			catch (MessagingException e)
			{
				log.log(Level.SEVERE, "Exception: ", e);
			}
	}
	
	public void sendOneEmail(Email email) throws MessagingException
	{
		Transport.send(buildMessage(email));
	}
	
	private Message buildMessage(Email email) throws MessagingException
	{
		Message result = new MimeMessage(mailSession);
		
		result.setFrom(new InternetAddress(email.getSender()));
		
		setMessageRecipients(result, Message.RecipientType.TO, email.getRecipientsTo());
		setMessageRecipients(result, Message.RecipientType.CC, email.getRecipientsCc());
		setMessageRecipients(result, Message.RecipientType.BCC, email.getRecipientsBcc());
		
		result.setSubject(email.getTitle());
		
		MimeMultipart multipart = new MimeMultipart();
		
		BodyPart messagePart = new MimeBodyPart();
		messagePart.setContent(email.getBody(), "text/html; charset=utf-8");
		multipart.addBodyPart(messagePart);
		
		setAttachments(multipart, email.getFileNames());
		
		result.setContent(multipart);
		
		return result;
	}
	
	private void setAttachments(MimeMultipart multipart, List<String> attachmentNames) throws MessagingException
	{
		if (attachmentNames != null)
		{
			BodyPart attachmentPart;
			
			for (String attachmentName : attachmentNames)
			{
				attachmentPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachmentName);
				attachmentPart.setDataHandler(new DataHandler(source));
				attachmentPart.setFileName(new File(attachmentName).getName());
				
				multipart.addBodyPart(attachmentPart);
			}
		}
	}
	
	private void setMessageRecipients(Message message, Message.RecipientType recipientType, List<String> recipients)
			throws MessagingException
	{
		if (recipients != null)
			for (String recipient: recipients)
				message.addRecipient(recipientType, new InternetAddress(recipient));
	}
}
