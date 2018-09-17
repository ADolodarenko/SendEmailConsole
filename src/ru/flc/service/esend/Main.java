package ru.flc.service.esend;

import ru.flc.service.log.LogUtil;

public class Main
{
    public static void main(String[] args)
    {
        LogUtil.setLogger(Main.class, "esend_logging.properties");

    	Engine engine = new Engine();

    	if (engine.loadProperties() && engine.loadEmails(args))
    		engine.send();
    }
}
