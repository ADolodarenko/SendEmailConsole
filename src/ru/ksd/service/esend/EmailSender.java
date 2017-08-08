package ru.ksd.service.esend;

import java.util.Properties;

public class EmailSender
{
	private static final String INCORRECT_PROPERTIES_MESSAGE = "Incorrect properties.";

	private Properties properties;

	public EmailSender(Properties properties) throws WrongPropertiesException
	{
		if (properties == null && properties.isEmpty())
			throw new WrongPropertiesException(INCORRECT_PROPERTIES_MESSAGE);

		if (!properties.containsKey("mail.smtp.host"))
			throw new WrongPropertiesException(INCORRECT_PROPERTIES_MESSAGE);

		if (!properties.containsKey("mail.smtp.port"))
			throw new WrongPropertiesException(INCORRECT_PROPERTIES_MESSAGE);

		if (!properties.containsKey("mail.login"))
			throw new WrongPropertiesException(INCORRECT_PROPERTIES_MESSAGE);

		this.properties = properties;
	}



}
