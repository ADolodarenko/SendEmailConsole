package ru.ksd.service.esend;

public class Main
{
    public static void main(String[] args)
    {
        LogUtil.setLogger();

    	Engine engine = new Engine();

    	if (engine.loadProperties() && engine.loadEmails(args))
    		engine.send();
    }
}
