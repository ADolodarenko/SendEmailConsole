package ru.ksd.service.esend;

import java.util.List;

public class ConsoleEmailLoader implements EmailLoader
{
    public ConsoleEmailLoader(String[] arguments) throws WrongParametersException
	{
    	if (arguments == null || arguments.length < 3 || arguments.length > 5)
    		throw new WrongParametersException("Incorrect email message parameters.");



    }

    @Override
    public List<Email> loadEmails()
    {
        return null;
    }
}
