package ru.ksd.service.esend;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class FilePropertiesLoader implements PropertiesLoader
{
	private String filePath;

	public FilePropertiesLoader(String filePath) throws WrongParametersException
	{
		if (filePath == null || filePath.isEmpty())
			throw new WrongParametersException("Incorrect properties file name.");

		this.filePath = filePath;
	}

	@Override
	public Properties load() throws IOException
	{
		Properties properties = new Properties();
		FileReader reader = new FileReader(this.filePath);

		properties.load(reader);
		reader.close();

		return properties;
	}
}
