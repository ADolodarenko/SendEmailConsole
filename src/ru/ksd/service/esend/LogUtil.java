package ru.ksd.service.esend;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.LogManager;

public class LogUtil
{
	public static void setLogger()
	{
		try
		{
			setLoggingConfig();
		}
		catch (IOException e)
		{
			System.err.println("Could not setup logger configuration: " + e.toString());
		}
	}
	
	private static void setLoggingConfig() throws IOException
	{
		String path = Engine.getCurrentDir() + File.separator + "logging.properties";
		
		InputStream input = Files.newInputStream(Paths.get(path));
		LogManager manager = LogManager.getLogManager();
		manager.readConfiguration(input);
	}
}
