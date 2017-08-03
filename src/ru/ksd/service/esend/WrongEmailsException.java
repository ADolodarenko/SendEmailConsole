package ru.ksd.service.esend;

public class WrongEmailsException extends Exception
{
    public WrongEmailsException(String message)
    {
        super(message);
    }
}
