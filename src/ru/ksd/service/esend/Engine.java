package ru.ksd.service.esend;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Engine is a class that encapsulates the main functionality of the app.
 * This class gets all parameters and emails in order to initialize the EmailSender.
 */
public class Engine
{
	private static Map<String, DataSourceType> sourceTypes;

	static
	{
		sourceTypes = new HashMap<>();
		sourceTypes.put("console", DataSourceType.CONSOLE);
		sourceTypes.put("file", DataSourceType.FILE);
		sourceTypes.put("database", DataSourceType.DATABASE);
	}

	private Properties properties;
	private List<Email> emails;

	public static String getCurrentDir()
	{
		URL location = Engine.class.getProtectionDomain().getCodeSource().getLocation();
		String path = location.getFile();

		try
		{
			path = URLDecoder.decode(path.replace('/', File.separatorChar),
					Charset.defaultCharset().name());
		}
		catch (UnsupportedEncodingException e)
		{
			//TODO: Logging
		}

		return path;
	}

	public void send()
	{
		try
		{
			EmailSender sender = new EmailSender(properties);
			
			sender.open();
			sender.send(emails);
			sender.close();
		}
		catch (WrongPropertiesException e)
		{
			//TODO: Logging
		}
		catch (WrongParametersException e)
		{
			//TODO: Logging
		}
	}

	public boolean loadProperties()
	{
		boolean result = false;

		try
		{
			PropertiesLoader loader = PropertiesLoaderFactory.getInstance(DataSourceType.FILE,
																		  getCurrentDir() + "esend.properties");
			properties = loader.load();

			if (properties != null && !properties.isEmpty())
				result = true;
		}
		catch (Exception e)
		{
			//TODO: Logging
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
			//TODO: Logging
		}
		catch (WrongEmailsException e)
		{
			//TODO: Logging
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
