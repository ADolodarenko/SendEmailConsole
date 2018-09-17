package ru.flc.service.exceptions;

public class WrongEmailsException extends Exception
{
    public WrongEmailsException(String message)
    {
        super(message);
    }
}
