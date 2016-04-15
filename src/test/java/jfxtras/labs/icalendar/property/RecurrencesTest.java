package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import jfxtras.labs.icalendar.properties.component.recurrence.Recurrences;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

public class RecurrencesTest
{    
    @Test
    public void canParseRDate1()
    {
        String s = "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "19970304,19970504,19970704,19970904");
        expectedMap.put("VALUE", "DATE");
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canParseRDate2()
    {
        String s = "RDATE;TZID=America/New_York:19970714T083000";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "19970714T083000");
        expectedMap.put("TZID", "America/New_York");
        assertEquals(expectedMap, valueMap);
    }
    
    // Can parsing period, but period not currently supported in streaming dates
    @Test
    public void canParseRDate3()
    {
        String s = "RDATE;VALUE=PERIOD:19960403T020000Z/19960403T040000Z,19960404T010000Z/PT3H";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "19960403T020000Z/19960403T040000Z,19960404T010000Z/PT3H");
        expectedMap.put("VALUE", "PERIOD");
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canMakeRecurrences1()
    {
        Recurrences<LocalDateTime> property = new Recurrences<LocalDateTime>(
                FXCollections.observableSet(LocalDateTime.of(2015, 11, 12, 10, 0)
                     , LocalDateTime.of(2015, 11, 14, 12, 0)));
        ObservableSet<LocalDateTime> expectedDates = FXCollections.observableSet(
                LocalDateTime.of(2015, 11, 12, 10, 0)
                , LocalDateTime.of(2015, 11, 14, 12, 0) );
        assertEquals(expectedDates, property.getValue());
        assertEquals("RDATE:20151112T100000,20151114T120000", property.toContentLine());
    }
    
    @Test
    public void canParseRecurrences2()
    {
        String content = "RDATE;TZID=America/Los_Angeles:19960402T010000";
        Recurrences<ZonedDateTime> madeProperty = new Recurrences<ZonedDateTime>(content);
        madeProperty.getValue().add(ZonedDateTime.of(LocalDateTime.of(1996, 4, 3, 1, 0), ZoneId.of("America/Los_Angeles")));
        madeProperty.getValue().add(ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("America/Los_Angeles")));
        assertEquals(content + ",19960403T010000,19960404T010000", madeProperty.toContentLine());
        Recurrences<ZonedDateTime> expectedProperty = new Recurrences<ZonedDateTime>(FXCollections.observableSet(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 2, 1, 0), ZoneId.of("America/Los_Angeles")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 3, 1, 0), ZoneId.of("America/Los_Angeles")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("America/Los_Angeles")) ));
        assertEquals(expectedProperty, madeProperty);
        
        Set<ZonedDateTime> expectedValues = new HashSet<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 2, 1, 0), ZoneId.of("America/Los_Angeles")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 3, 1, 0), ZoneId.of("America/Los_Angeles")),
                ZonedDateTime.of(LocalDateTime.of(1996, 4, 4, 1, 0), ZoneId.of("America/Los_Angeles")) ));
        assertEquals(expectedValues, madeProperty.getValue());
    }
}