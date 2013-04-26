package jfxtras.labs.map.tile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Loads the attributions for the bing map.
 * @author Mario Schröder
 */
class BingAttributionLoader {

    private String metadataUrl;

    private BingMetadataHandler handler;

    BingAttributionLoader(String metadataUrl, BingMetadataHandler handler) {
        this.handler = handler;
        this.metadataUrl = metadataUrl;
    }

    List<Attribution> load() {

        try {
            URL url = new URL(metadataUrl);
            URLConnection conn = url.openConnection();

            InputStream stream = conn.getInputStream();

            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(handler);
            parser.parse(new InputSource(stream));

        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        
        return handler.getAttributions();
    }

}
