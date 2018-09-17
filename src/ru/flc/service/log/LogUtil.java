package ru.flc.service.log;

import ru.flc.service.filesystem.FileSystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.LogManager;

public class LogUtil
{
	public static final String DEFAULT_LOG_PATH = FileSystem.getCurrentDir(LogUtil.class);
	public static final String DEFAULT_LOG_NAME = "logging.properties";

	public static void setLogger(Class<?> baseClass, String logName)
	{
		String logPath;
		if (baseClass == null)
			logPath = null;
		else
			logPath = FileSystem.getCurrentDir(baseClass);

		setLogger(logPath, logName);
	}

	public static void setLogger(String logPath, String logName)
	{
		if (logPath == null && logName == null)
			setLogger(null);
		else
		{
			if (logPath == null)
				logPath = DEFAULT_LOG_PATH;
			else if (logName == null)
				logName = DEFAULT_LOG_NAME;

			setLogger(logPath + File.separator + logName);
		}
	}

	public static void setLogger(String fullLogName)
	{
		if (fullLogName == null)
			fullLogName = DEFAULT_LOG_PATH + File.separator + DEFAULT_LOG_NAME;

		try
		{
			setLoggingConfig(fullLogName);
		}
		catch (IOException e)
		{
			System.err.println("Could not setup logger configuration: " + e.toString());
		}
	}
	
	private static void setLoggingConfig(String fullLogName) throws IOException
	{
		InputStream input = Files.newInputStream(Paths.get(fullLogName));
		LogManager manager = LogManager.getLogManager();
		manager.readConfiguration(input);
	}
}
