package jfxtras.labs.icalendarfx.calendar;

import org.junit.Test;

import jfxtras.labs.icalendarfx.ICalendarTestAbstract2;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendarfx.properties.calendar.Version;

public class GeneralCalendarTest extends ICalendarTestAbstract2
{
    @Test
    public void canBuildVCalendar()
    {
        String expectedContent = 
                "BEGIN:VTIMEZONE" + System.lineSeparator() +
                "TZID:America/New_York" + System.lineSeparator() +
                "LAST-MODIFIED:20050809T050000Z" + System.lineSeparator() +
                "BEGIN:DAYLIGHT" + System.lineSeparator() +
                "DTSTART:19670430T020000" + System.lineSeparator() +
                "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19730429T070000Z" + System.lineSeparator() +
                "TZOFFSETFROM:-0500" + System.lineSeparator() +
                "TZOFFSETTO:-0400" + System.lineSeparator() +
                "TZNAME:EDT" + System.lineSeparator() +
                "END:DAYLIGHT" + System.lineSeparator() +
                "BEGIN:STANDARD" + System.lineSeparator() +
                "DTSTART:19671029T020000" + System.lineSeparator() +
                "RRULE:FREQ=YEARLY;BYMONTH=10;BYDAY=-1SU;UNTIL=20061029T060000Z" + System.lineSeparator() +
                "TZOFFSETFROM:-0400" + System.lineSeparator() +
                "TZOFFSETTO:-0500" + System.lineSeparator() +
                "TZNAME:EST" + System.lineSeparator() +
                "END:STANDARD" + System.lineSeparator() +
                "BEGIN:DAYLIGHT" + System.lineSeparator() +
                "DTSTART:19740106T020000" + System.lineSeparator() +
                "RDATE:19750223T020000" + System.lineSeparator() +
                "TZOFFSETFROM:-0500" + System.lineSeparator() +
                "TZOFFSETTO:-0400" + System.lineSeparator() +
                "TZNAME:EDT" + System.lineSeparator() +
                "END:DAYLIGHT" + System.lineSeparator() +
                "BEGIN:DAYLIGHT" + System.lineSeparator() +
                "DTSTART:19760425T020000" + System.lineSeparator() +
                "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU;UNTIL=19860427T070000Z" + System.lineSeparator() +
                "TZOFFSETFROM:-0500" + System.lineSeparator() +
                "TZOFFSETTO:-0400" + System.lineSeparator() +
                "TZNAME:EDT" + System.lineSeparator() +
                "END:DAYLIGHT" + System.lineSeparator() +
                "BEGIN:DAYLIGHT" + System.lineSeparator() +
                "DTSTART:19870405T020000" + System.lineSeparator() +
                "RRULE:FREQ=YEARLY;BYMONTH=4;BYDAY=1SU;UNTIL=20060402T070000Z" + System.lineSeparator() +
                "TZOFFSETFROM:-0500" + System.lineSeparator() +
                "TZOFFSETTO:-0400" + System.lineSeparator() +
                "TZNAME:EDT" + System.lineSeparator() +
                "END:DAYLIGHT" + System.lineSeparator() +
                "BEGIN:DAYLIGHT" + System.lineSeparator() +
                "DTSTART:20070311T020000" + System.lineSeparator() +
                "RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU" + System.lineSeparator() +
                "TZOFFSETFROM:-0500" + System.lineSeparator() +
                "TZOFFSETTO:-0400" + System.lineSeparator() +
                "TZNAME:EDT" + System.lineSeparator() +
                "END:DAYLIGHT" + System.lineSeparator() +
                "BEGIN:STANDARD" + System.lineSeparator() +
                "DTSTART:20071104T020000" + System.lineSeparator() +
                "RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU" + System.lineSeparator() +
                "TZOFFSETFROM:-0400" + System.lineSeparator() +
                "TZOFFSETTO:-0500" + System.lineSeparator() +
                "TZNAME:EST" + System.lineSeparator() +
                "END:STANDARD" + System.lineSeparator() +
                "END:VTIMEZONE";
            VTimeZone vTimeZone = VTimeZone.parse(expectedContent);
        
        VCalendar c = new VCalendar()
                .withVEvents(getYearly1())
                .withCalendarScale(new CalendarScale())
                .withVersion(new Version())
                .withProductIdentifier(new ProductIdentifier())
                .withVTimeZones(vTimeZone)
                .withVTodos(new VTodo()
                        .withDateTimeCompleted("COMPLETED:19960401T150000Z")
                        .withDateTimeDue("TZID=America/Los_Angeles:19960401T050000")
                        .withPercentComplete(35))
                .withVEvents(getMonthly6());
        System.out.println(c.toContentLines());
    }
    
    @Test
    public void canParseVCalendar()
    {
        String content = 
       "BEGIN:VCALENDAR" + System.lineSeparator() +
       "VERSION:2.0" + System.lineSeparator() +
       "PRODID:-//hacksw/handcal//NONSGML v1.0//EN" + System.lineSeparator() +
       "BEGIN:VEVENT" + System.lineSeparator() +
       "UID:19970610T172345Z-AF23B2@example.com" + System.lineSeparator() +
       "DTSTAMP:19970610T172345Z" + System.lineSeparator() +
       "DTSTART:19970714T170000Z" + System.lineSeparator() +
       "DTEND:19970715T040000Z" + System.lineSeparator() +
       "SUMMARY:Bastille Day Party" + System.lineSeparator() +
       "END:VEVENT" + System.lineSeparator() +
       "BEGIN:VEVENT" + System.lineSeparator() +
       "SUMMARY:New Event" + System.lineSeparator() +
       "CLASS:PUBLIC" + System.lineSeparator() +
       "DTSTART;TZID=Etc/GMT:20150902T133000Z" + System.lineSeparator() +
       "DTEND;TZID=Etc/GMT:20150902T180000Z" + System.lineSeparator() +
       "PRIORITY:0" + System.lineSeparator() +
       "SEQUENCE:1" + System.lineSeparator() +
       "STATUS:CANCELLED" + System.lineSeparator() +
       "UID:dc654e79-cc85-449c-a1b2-71b2d20b80df" + System.lineSeparator() +
       "RECURRENCE-ID;TZID=Etc/GMT:20150902T133000Z" + System.lineSeparator() +
       "DTSTAMP:20150831T053218Z" + System.lineSeparator() +
       "ORGANIZER;CN=David Bal;SENT-BY=\"mailto:ddbal1@yahoo.com\":mailto:ddbal1@yaho" + System.lineSeparator() +
       " o.com" + System.lineSeparator() +
       "X-YAHOO-YID:testuser" + System.lineSeparator() +
       "TRANSP:OPAQUE" + System.lineSeparator() +
       "X-YAHOO-USER-STATUS:BUSY" + System.lineSeparator() +
       "X-YAHOO-EVENT-STATUS:BUSY" + System.lineSeparator() +
       "BEGIN:VALARM" + System.lineSeparator() +
       "ACTION:DISPLAY" + System.lineSeparator() +
       "DESCRIPTION:" + System.lineSeparator() +
       "TRIGGER;RELATED=START:-PT30M" + System.lineSeparator() +
       "END:VALARM" + System.lineSeparator() +
       "END:VEVENT" + System.lineSeparator() +
       "END:VCALENDAR";
        VCalendar vCalendar = VCalendar.parse(content);
//        System.out.println(vCalendar.toContentLines());
        VEventNew e = vCalendar.getVEvents().get(1);
        e.getNonStandardProperties().stream().forEach(System.out::println);
    }

}
