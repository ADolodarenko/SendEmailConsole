package ru.ksd.service.esend;

import java.util.List;

public class Main
{
    public static void main(String[] args)
    {
        EmailLoader emailLoader = EmailLoaderFactory.getEmailLoader(EmailLoaderType.CONSOLE_LOADER, args);
        List<Email> emails = emailLoader.loadEmails();

        
    }
}
