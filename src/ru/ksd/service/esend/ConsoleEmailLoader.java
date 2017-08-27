package ru.ksd.service.esend;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConsoleEmailLoader implements EmailLoader
{
	private String[] arguments;

    public ConsoleEmailLoader(String[] arguments) throws WrongParametersException
	{
    	if (arguments == null || arguments.length < 3 || arguments.length > 6)
    		throw new WrongParametersException("Incorrect email message parameters.");

    	this.arguments = Arrays.copyOf(arguments, arguments.length);
	}

    @Override
    public List<Email> loadEmails() throws WrongEmailsException
    {
    	List<Email> emails = new ArrayList<>();

    	//0-from, 1-to, 2-title, 3-body, 4-files, 5-sign-file

		String body = "--";
    	if (arguments.length > 3)
			body = arguments[3];

    	String signPath = null;
    	if (arguments.length == 6)
    		signPath = arguments[5];

    	Email email = new Email(arguments[0], arguments[2], body, signPath, false);
    	email.addRecipients(arguments[1].split(","));

    	if (arguments.length == 5)
    		email.addFileNames(arguments[4].split(","));

    	if (!email.emailIsCorrect())
    		throw new WrongEmailsException("Wrong email");

    	emails.add(email);

        return emails;
    }
}
