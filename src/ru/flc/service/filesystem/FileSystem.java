package ru.flc.service.filesystem;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class FileSystem
{
    public static String getCurrentDir(Class<?> cls)
    {
        URL url;
        String extURL;

        try
        {
            url = cls.getProtectionDomain().getCodeSource().getLocation();
        }
        catch (SecurityException ex)
        {
            url = cls.getResource(cls.getSimpleName() + ".class");
        }

        extURL = url.toExternalForm();

        if (extURL.endsWith(".jar"))
            extURL = extURL.substring(0, extURL.lastIndexOf("/"));  //!
        else
        {
            String suffix = "/" + (cls.getName()).replace(".", "/") + ".class";
            extURL = extURL.replace(suffix, "");
            if (extURL.startsWith("jar:") && extURL.endsWith(".jar!"))
                extURL = extURL.substring(4, extURL.lastIndexOf("/"));
        }

        try
        {
            url = new URL(extURL);
        }
        catch (MalformedURLException mux) {}

        String path;

        try
        {
            path = new File(url.toURI()).getAbsolutePath();
        }
        catch(URISyntaxException ex)
        {
            path = new File(url.getPath()).getAbsolutePath();
        }

		return path;
    }

    /*
		Class cl = Engine.class;

		String path =  cl.getResource(cl.getSimpleName() + ".class").toString();


		File jarDir = new File(ClassLoader.getSystemClassLoader().getResource(".").getPath());
		String path = jarDir.getAbsolutePath();

		System.out.println(path);*/

		/*URL location = Engine.class.getProtectionDomain().getCodeSource().getLocation();
		String path = location.getFile();

		try
		{
			path = URLDecoder.decode(path.replace('/', File.separatorChar),
									 Charset.defaultCharset().name());
		}
		catch (UnsupportedEncodingException e)
		{}
		*/
}
