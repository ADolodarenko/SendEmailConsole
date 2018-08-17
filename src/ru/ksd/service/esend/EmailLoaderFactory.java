package ru.ksd.service.esend;

/**
 * This class is a factory which generates email loaders.
 */
public class EmailLoaderFactory
{
    public static EmailLoader getInstance(DataSourceType type, String[] args) throws WrongParametersException
    {
        switch (type)
        {
            case CONSOLE:
                return new ConsoleEmailLoader(args);
            default:
                return null;
        }
    }
}
