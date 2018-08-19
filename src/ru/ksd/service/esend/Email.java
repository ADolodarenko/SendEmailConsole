package ru.ksd.service.esend;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class for one e-mail message.
 */
public class Email
{
    private static final String ADDRESS_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    private String sender;
    private List<String> recipientsTo;
    private List<String> recipientsCc;
    private List<String> recipientsBcc;
    private String title;
    private String body;
    private List<String> fileNames;
    private boolean isWrongAddressSensitive;

    public Email(String sender, String title, String body, String signPath, boolean isWrongAddressSensitive) throws WrongEmailsException
    {
        if (!addressIsCorrect(sender))
            throw new WrongEmailsException("Incorrect sender address.");

        if (!titleIsCorrect(title))
            throw new WrongEmailsException("Incorrect title.");

        if (!bodyIsCorrect(body))
            throw new WrongEmailsException("Incorrect body of message.");

        this.recipientsTo = new LinkedList<>();
        this.recipientsCc = new LinkedList<>();
        this.recipientsBcc = new LinkedList<>();
        this.sender = sender;
        this.title = title;
        this.body = body;
        this.fileNames = new LinkedList<>();
        this.isWrongAddressSensitive = isWrongAddressSensitive;

        if (signPath != null && (!signPath.isEmpty()))
        	setBodyWithSign(signPath);
    }

    private void setBodyWithSign(String signPath)
	{
		File signFile = new File(signPath);

		if (signFile.exists() && signFile.isFile())
			try
			{
				byte[] buffer = Files.readAllBytes(signFile.toPath());
				String sign = new String(buffer);

				this.body = sign.replace("<-->", this.body);
			}
			catch (IOException e)
			{
				//TODO: log here
			}
	}

    public boolean emailIsCorrect()
    {
        return (this.recipientsTo.size() + this.recipientsCc.size() + this.recipientsBcc.size() > 0);
    }

	public void addRecipients(String[] recipients) throws WrongEmailsException
	{
		for (String recipient : recipients)
		{
			int pos = recipient.indexOf(":");

			if (pos == -1)
				addRecipientTo(recipient);
			else
			{
				String type = recipient.substring(0, pos).trim().toLowerCase();
				String value = recipient.substring(pos + 1).trim();

				if ("bcc".equals(type))
					addRecipientBcc(value);
				else if ("cc".equals(type))
					addRecipientCc(value);
				else
					addRecipientTo(value);
			}
		}
	}

    public void addRecipientTo(String recipient) throws WrongEmailsException
    {
        addRecipient(recipientsTo, recipient);
    }

    public void addRecipientCc(String recipient) throws WrongEmailsException
    {
        addRecipient(recipientsCc, recipient);
    }

    public void addRecipientBcc(String recipient) throws WrongEmailsException
    {
        addRecipient(recipientsBcc, recipient);
    }

    private void addRecipient(List<String> recipients, String recipient) throws WrongEmailsException
    {
        if (addressIsCorrect(recipient))
            recipients.add(recipient);
        else
            if (isWrongAddressSensitive)
                throw new WrongEmailsException("Incorrect recipient address.");
    }

    private boolean addressIsCorrect(String recipient)
    {
        boolean result = false;

        if (recipient != null && (!recipient.isEmpty()))
        {
            Pattern pattern = Pattern.compile(ADDRESS_PATTERN, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(recipient);
            result = matcher.matches();
        }

        return result;
    }

	public void addFileNames(String[] fileNames)
	{
		for (String fileName : fileNames)
			addFileName(fileName);
	}

    public void addFileName(String fileName)
	{
		if (fileNameIsCorrect(fileName))
			fileNames.add(fileName);
	}

    private boolean fileNameIsCorrect(String fileName)
	{
		return (fileName != null && (!fileName.isEmpty()));
	}

    private boolean titleIsCorrect(String title)
    {
        return (title != null && (!title.isEmpty()));
    }

    private boolean bodyIsCorrect(String body)
    {
        return (body != null && (!body.isEmpty()));
    }

    public String getSender()
    {
        return sender;
    }

    public List<String> getRecipientsTo()
    {
        return recipientsTo;
    }

    public List<String> getRecipientsCc()
    {
        return recipientsCc;
    }

    public List<String> getRecipientsBcc()
    {
        return recipientsBcc;
    }
	
	public String getTitle()
	{
		return title;
	}
	
	public String getBody()
	{
		return body;
	}
	
	public List<String> getFileNames()
    {
        return fileNames;
    }
}
