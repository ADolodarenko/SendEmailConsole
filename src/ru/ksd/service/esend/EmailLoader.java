package ru.ksd.service.esend;

import java.util.List;

public interface EmailLoader
{
    List<Email> loadEmails() throws WrongEmailsException;
}
