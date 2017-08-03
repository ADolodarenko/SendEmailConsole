package ru.ksd.service.esend;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email
{
    private static final String ADDRESS_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

    private String sender;
    private List<String> recipientsTo;
    private List<String> recipientsCc;
    private List<String> recipientsDcc;
    private String title;
    private String body;
    private List<String> fileNames;
    private boolean isWrongAddressSensitive;

    public Email(String sender, String title, String body, boolean isWrongAddressSensitive) throws WrongEmailsException
    {
        if (!addressIsCorrect(sender))
            throw new WrongEmailsException("Incorrect sender address.");

        if (!titleIsCorrect(title))
            throw new WrongEmailsException("Incorrect title.");

        if (!bodyIsCorrect(body))
            throw new WrongEmailsException("Incorrect body of message.");

        this.recipientsTo = new LinkedList<>();
        this.recipientsCc = new LinkedList<>();
        this.recipientsDcc = new LinkedList<>();
        this.sender = sender;
        this.title = title;
        this.body = body;
        this.fileNames = new LinkedList<>();
        this.isWrongAddressSensitive = isWrongAddressSensitive;
    }

    public boolean emailIsCorrect()
    {
        return (this.recipientsTo.size() + this.recipientsCc.size() + this.recipientsDcc.size() > 0);
    }

    public void addRecipientTo(String recipient) throws WrongEmailsException
    {
        addRecipient(recipientsTo, recipient);
    }

    public void addRecipientCc(String recipient) throws WrongEmailsException
    {
        addRecipient(recipientsCc, recipient);
    }

    public void addRecipientDcc(String recipient) throws WrongEmailsException
    {
        addRecipient(recipientsDcc, recipient);
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

    public List<String> getRecipientsDcc()
    {
        return recipientsDcc;
    }

    public List<String> getFileNames()
    {
        return fileNames;
    }
}
