package ru.ksd.service.esend;

import java.io.IOException;
import java.util.Properties;

public interface PropertiesLoader
{
	Properties load() throws IOException;
}
