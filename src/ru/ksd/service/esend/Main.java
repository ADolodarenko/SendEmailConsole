package ru.ksd.service.esend;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.LogManager;

public class Main
{
    public static void main(String[] args)
    {
        setLogger();

    	Engine engine = new Engine();

    	if (engine.loadProperties() && engine.loadEmails(args))
    		engine.send();
    }

    private static void setLogger()
    {
        String path = Engine.getCurrentDir() + File.separator + "logging.properties";

        try
        {
            InputStream input = Files.newInputStream(Paths.get(path));
            LogManager.getLogManager().readConfiguration(input);
        }
        catch (IOException e)
        {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }
    }
}
