package jfxtras.labs.icalendarfx.calendar;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import jfxtras.labs.icalendarfx.VCalendar;

public class ReadICSFileTest
{
    @Test
    public void canReadICSFile1() throws IOException
    {
        String fileName = "Yahoo_Sample_Calendar.ics";
//        "/jfxtras-labs/src/test/resources/jfxtras/labs/icalendarfx/calendar/mathBirthdays.ics"
//        URL url = this.getClass().getResource(fileName);
        ClassLoader classLoader = getClass().getClassLoader();
//        File file = new File(classLoader.getResource(fileName).getFile());
//        Enumeration<URL> e = Test.class.getClassLoader().getResources("");
//        while (e.hasMoreElements())
//        {
//            System.out.println("ClassLoader Resource: " + e.nextElement());
//        }
//        System.out.println("Class Resource: " + Test.class.getResource("/"));
        
//        URL resource = getClass().getResource("/");
//        System.out.println(resource);
        
        URL url = getClass().getResource(fileName);
        Path icsFilePath = Paths.get(url.getFile());
        VCalendar vCalendar = VCalendar.parseICalendarFile(icsFilePath);
        System.out.println("vCalendar vevents:" + vCalendar.getVEvents().size());
//        System.out.println(vCalendar.toContent());
    }
}