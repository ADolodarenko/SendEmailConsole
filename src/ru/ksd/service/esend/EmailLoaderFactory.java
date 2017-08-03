package ru.ksd.service.esend;

public class EmailLoaderFactory
{
    public static EmailLoader getEmailLoader(EmailLoaderType type, String[] args)
    {
        switch (type)
        {
            case CONSOLE_LOADER:
                return new ConsoleEmailLoader(args);
            default:
                return null;
        }
    }
}
