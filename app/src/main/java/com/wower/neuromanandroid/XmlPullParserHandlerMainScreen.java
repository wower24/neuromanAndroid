package com.wower.neuromanandroid;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Handles the parsing of XML data for the Main Screen of the application.
 * This class is responsible for loading initial data such as the list of operator names.
 */
public class XmlPullParserHandlerMainScreen {
    /**
     * List to store operator names extracted from the XML.
     */
    List<String> operatorNames = new ArrayList<>();
    /**
     * String to hold text content parsed from the XML.
     */
    String text;
    /**
     * Parses the input stream of XML data and extracts necessary information.
     * Specifically, it extracts operator names to be displayed on the main screen.
     *
     * @param inputStream The input stream of XML data.
     * @return A Map containing the list of operator names.
     */
    public Map<String, List<?>> parse(InputStream inputStream) {

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();

            while(eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = parser.getName();

                switch(eventType) {
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("operator")) {
                            operatorNames.add(text);
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch(XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        Map<String, List<?>> result =  new HashMap<>();
        result.put("operatorNames", operatorNames);

        return result;
    }
}
