package ru.flc.service.esend;

import ru.flc.service.data.PropertiesLoader;
import ru.flc.service.email.Email;
import ru.flc.service.email.EmailLoader;
import ru.flc.service.email.EmailLoaderFactory;
import ru.flc.service.email.EmailSender;
import ru.flc.service.exceptions.WrongEmailsException;
import ru.flc.service.exceptions.WrongPropertiesException;
import ru.flc.service.filesystem.FileSystem;
import ru.flc.service.data.DataSourceType;
import ru.flc.service.data.PropertiesLoaderFactory;
import ru.flc.service.exceptions.WrongParametersException;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Engine is a class that encapsulates the main functionality of the app.
 * This class gets all parameters and emails in order to initialize the EmailSender.
 */
public class Engine
{
	private static Map<String, DataSourceType> sourceTypes;

	private static Logger log = Logger.getLogger(Engine.class.getName());

	static
	{
		sourceTypes = new HashMap<>();
		sourceTypes.put("console", DataSourceType.CONSOLE);
		sourceTypes.put("file", DataSourceType.FILE);
		sourceTypes.put("database", DataSourceType.DATABASE);
	}

	private Properties properties;
	private List<Email> emails;

	public void send()
	{
		try
		{
			EmailSender sender = new EmailSender(properties);
			
			sender.open();
			sender.sendAllEmails(emails);
		}
		catch (WrongPropertiesException e)
		{
			log.log(Level.SEVERE, "Exception: ", e);
		}
		catch (WrongParametersException e)
		{
			log.log(Level.SEVERE, "Exception: ", e);
		}
	}

	public boolean loadProperties()
	{
		boolean result = false;

		try
		{
			String propertiesPath = FileSystem.getCurrentDir(Engine.class) + File.separatorChar + "esend.properties";
			
			PropertiesLoader loader = PropertiesLoaderFactory.getInstance(DataSourceType.FILE, propertiesPath);
			properties = loader.load();

			if (properties != null && !properties.isEmpty())
				result = true;
		}
		catch (Exception e)
		{
			log.log(Level.SEVERE, "Exception: ", e);
		}

		return result;
	}

	public boolean loadEmails(String[] args)
	{
		boolean result = false;

		try
		{
			EmailLoader emailLoader = EmailLoaderFactory.getInstance(getMailSourceType(), args);
			emails = emailLoader.loadEmails();

			if (emails != null && !emails.isEmpty())
				result = true;
		}
		catch (WrongParametersException e)
		{
			log.log(Level.SEVERE, "Exception: ", e);
		}
		catch (WrongEmailsException e)
		{
			log.log(Level.SEVERE, "Exception: ", e);
		}

		return result;
	}

	private DataSourceType getMailSourceType()
	{
		DataSourceType type = sourceTypes.get(properties.getProperty("mail.source").toLowerCase());
		if (type == null)
			type = DataSourceType.CONSOLE;

		return type;
	}
}
