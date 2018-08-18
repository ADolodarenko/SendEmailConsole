package ru.ksd.service.esend;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class EmailSender
{
	private static final String INCORRECT_PROPERTIES_MESSAGE = "Incorrect properties.";
	private static final String[] MANDATORY_PROPERTY_NAMES = {"mail.smtp.host", "mail.smtp.port", "mail.user"};

	private Properties properties;
	private boolean isNeededSend;
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
	
	public void send(List<Email> emails) throws WrongParametersException
	{
		if (mailSession == null)
			throw new WrongParametersException("There are now session established.");
		
		if (emails == null || emails.isEmpty())
			throw new WrongParametersException("There are now emails to send.");
		
		for (Email email : emails)
		{
			//Transforming and Sending...
		}
	}
	
	public void close()
	{
		;
	}

	public boolean isNeededSend()
	{
		return isNeededSend;
	}
}
