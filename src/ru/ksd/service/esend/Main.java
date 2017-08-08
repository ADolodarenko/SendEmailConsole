package ru.ksd.service.esend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main
{
	private static Map<String, DataSourceType> sourceTypes;
	private static Main engine;

	static
	{
		sourceTypes = new HashMap<>();
		sourceTypes.put("console", DataSourceType.CONSOLE);
		sourceTypes.put("file", DataSourceType.FILE);
		sourceTypes.put("database", DataSourceType.DATABASE);
	}

	private Properties properties;
	private List<Email> emails;

    public static void main(String[] args)
    {
    	engine = new Main();

    	if (engine.loadProperties() && engine.loadEmails(args))
    		engine.send();
    }

	private void send()
	{
		try
		{
			EmailSender sender = new EmailSender(properties);

		}
		catch (WrongPropertiesException e)
		{

		}
	}

	private boolean loadProperties()
	{
		boolean result = false;

		try
		{
			PropertiesLoader loader = PropertiesLoaderFactory.getInstance(DataSourceType.FILE, "/usr/xxx.txt");
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

	private boolean loadEmails(String[] args)
	{
		boolean result = false;

		try
		{
			EmailLoader emailLoader = EmailLoaderFactory.getInstance(engine.getMailSourceType(), args);
			emails = emailLoader.loadEmails();

			if (emails != null && !emails.isEmpty())
				result = true;
		}
		catch (WrongParametersException e)
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
