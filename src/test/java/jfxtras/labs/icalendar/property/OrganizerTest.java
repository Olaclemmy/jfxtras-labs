package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.properties.component.relationship.Organizer;

public class OrganizerTest
{
    @Test
    public void canParseOrganizer()
    {
        String content = "ORGANIZER;CN=John Smith:mailto:jsmith@example.com";
        Organizer madeProperty = new Organizer(content);
        Organizer expectedProperty = new Organizer("mailto:jsmith@example.com")
                .withCommonName("John Smith");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContentLine());
    }
    
    @Test
    public void canParseOrganizer2()
    {
        String content = "ORGANIZER;CN=John Smith;DIR=\"ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)\";LANGUAGE=en;SENT-BY=\"mailto:sray@example.com\":mailto:jsmith@example.com";
        Organizer madeProperty = new Organizer(content);
        Organizer expectedProperty = new Organizer("mailto:jsmith@example.com")
                .withCommonName("John Smith")
                .withDirectoryEntryReference("ldap://example.com:6666/o=ABC%20Industries,c=US???(cn=Jim%20Dolittle)")
                .withLanguage("en")
                .withSentBy("mailto:sray@example.com");
        expectedProperty.parameterSortOrder().put(ParameterEnum.COMMON_NAME, 0);
        expectedProperty.parameterSortOrder().put(ParameterEnum.DIRECTORY_ENTRY_REFERENCE, 1);
        expectedProperty.parameterSortOrder().put(ParameterEnum.LANGUAGE, 2);
        expectedProperty.parameterSortOrder().put(ParameterEnum.SENT_BY, 3);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(content, expectedProperty.toContentLine());
    }
    
}