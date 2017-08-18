package ru.ksd.service.esend;

import java.util.Arrays;
import java.util.List;

public class ConsoleEmailLoader implements EmailLoader
{
	private String[] arguments;

    public ConsoleEmailLoader(String[] arguments) throws WrongParametersException
	{
    	if (arguments == null || arguments.length < 3 || arguments.length > 5)
    		throw new WrongParametersException("Incorrect email message parameters.");

    	this.arguments = Arrays.copyOf(arguments, arguments.length);
	}

    @Override
    public List<Email> loadEmails() throws WrongEmailsException
    {
    	String body = null;
    	if (arguments.length > 3)
		{


		}

    	Email email = new Email(arguments[0], arguments[2], body, false);
        return null;
    }
}
