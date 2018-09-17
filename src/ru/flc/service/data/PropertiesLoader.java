package ru.flc.service.data;

import java.io.IOException;
import java.util.Properties;

/**
 * The interface for loading properties.
 */
public interface PropertiesLoader
{
	Properties load() throws IOException;
}
