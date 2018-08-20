package ru.ksd.service.esend;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
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
		Class cls = Engine.class;
		
		URL url;
		String extURL;
		
		try
		{
			url = cls.getProtectionDomain().getCodeSource().getLocation();
		}
		catch (SecurityException ex)
		{
			url = cls.getResource(cls.getSimpleName() + ".class");
		}
		
		extURL = url.toExternalForm();
		
		if (extURL.endsWith(".jar"))
			extURL = extURL.substring(0, extURL.lastIndexOf("/"));  //!
		else
		{
			String suffix = "/" + (cls.getName()).replace(".", "/") + ".class";
			extURL = extURL.replace(suffix, "");
			if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
				extURL = extURL.substring(4, extURL.lastIndexOf("/"));
		}
		
		try
		{
			url = new URL(extURL);
		}
		catch (MalformedURLException mux) {}
		
		String path;
		
		try
		{
			path = new File(url.toURI()).getAbsolutePath();
		}
		catch(URISyntaxException ex)
		{
			path = new File(url.getPath()).getAbsolutePath();
		}
		
		/*
		Class cl = Engine.class;
		
		String path =  cl.getResource(cl.getSimpleName() + ".class").toString();
		
		
		File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
		String path = jarDir.getAbsolutePath();
		
		System.out.println(path);*/
		
		/*URL location = Engine.class.getProtectionDomain().getCodeSource().getLocation();
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
		*/

		return path;
	}

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
			String propertiesPath = getCurrentDir() + File.separatorChar + "esend.properties";
			
			PropertiesLoader loader = PropertiesLoaderFactory.getInstance(DataSourceType.FILE, propertiesPath);
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
