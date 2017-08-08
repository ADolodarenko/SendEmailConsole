package ru.ksd.service.esend;

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
