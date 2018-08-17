package ru.ksd.service.esend;

import java.util.List;

/**
 * This interface describes the method for loading emails, wherever they are.
 */
public interface EmailLoader
{
    List<Email> loadEmails() throws WrongEmailsException;
}
