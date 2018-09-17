package ru.flc.service.email;

import ru.flc.service.exceptions.WrongEmailsException;

import java.util.List;

/**
 * This interface describes the method for loading emails, wherever they are.
 */
public interface EmailLoader
{
    List<Email> loadEmails() throws WrongEmailsException;
}
