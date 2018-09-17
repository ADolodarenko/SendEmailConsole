package ru.flc.service.email;

import ru.flc.service.exceptions.WrongEmailsException;
import ru.flc.service.exceptions.WrongParametersException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is designed to load an email from arguments that are given through the console as part of the program call.
 */
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
	
		StringBuilder signPath = new StringBuilder();
		StringBuilder filePaths = new StringBuilder();
    	setPaths(arguments, signPath, filePaths);

    	Email email = new Email(arguments[0], arguments[2], body, signPath.toString(), false);
    	email.addRecipients(arguments[1].split(","));

    	if (filePaths.length() > 0)
    		email.addFileNames(filePaths.toString().split(","));

    	if (!email.emailIsCorrect())
    		throw new WrongEmailsException("Wrong email");

    	emails.add(email);

        return emails;
    }
	
	private void setPaths(String[] arguments, StringBuilder signPath, StringBuilder filePaths)
	{
		if (arguments.length > 4)
			for (int i = 4; i < arguments.length; i++)
				modifyPath(arguments[i], signPath, filePaths);
	}
	
	private void modifyPath(String buffer, StringBuilder signPath, StringBuilder filePaths)
	{
		int indexS = buffer.toLowerCase().indexOf("sign:");
		int indexF = buffer.toLowerCase().indexOf("files:");
		
		if (indexS == 0)
		{
			if (signPath.length() > 0)
				signPath.delete(0, signPath.length() - 1);
			signPath.append(buffer.substring(5));
		}
		else
		{
			String paths = null;
			if (indexF == 0)
				paths = buffer.substring(6);
			else
				paths = buffer;
			
			if (filePaths.length() > 0)
				filePaths.append(',');
			
			filePaths.append(paths);
		}
	}
}
