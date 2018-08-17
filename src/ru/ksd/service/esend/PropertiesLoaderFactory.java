package ru.ksd.service.esend;

/**
 * This class is a factory for loading properties.
 */
public class PropertiesLoaderFactory
{
	public static PropertiesLoader getInstance(DataSourceType type, String argument) throws WrongParametersException
	{
		switch (type)
		{
			case FILE:
				return new FilePropertiesLoader(argument);
			default:
				return null;
		}
	}
}
