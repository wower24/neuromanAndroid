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

public class XmlPullParserHandler {
    List<String> operatorNames = new ArrayList<>();
    List<Profile> profiles = new ArrayList<>();
    Profile profile;
    String text;

    public List<String> getOperatorNames() {
        return operatorNames;
    }

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
                    case XmlPullParser.START_TAG:
                        if (tagName.equalsIgnoreCase("profile")) {
                            profile = new Profile();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagName.equalsIgnoreCase("operator")) {
                            operatorNames.add(text);
                        } else if (tagName.equalsIgnoreCase("profile")) {
                            profiles.add(profile);
                        } else if (tagName.equalsIgnoreCase("duration")) {
                            profile.setDuration(Integer.parseInt(text));
                        } else if (tagName.equalsIgnoreCase("maxx")) {
                            profile.setMaxX(Integer.parseInt(text));
                        } else if (tagName.equalsIgnoreCase("maxy")) {
                            profile.setMaxY(Integer.parseInt(text));
                        } else if (tagName.equalsIgnoreCase("minx")) {
                            profile.setMinX(Integer.parseInt(text));
                        } else if (tagName.equalsIgnoreCase("miny")) {
                            profile.setMinY(Integer.parseInt(text));
                        } else if (tagName.equalsIgnoreCase("name")) {
                            profile.setName(text);
                        } else if (tagName.equalsIgnoreCase("patience")) {
                            profile.setPatience(Integer.parseInt(text));
                        } else if (tagName.equalsIgnoreCase("radius")) {
                            profile.setRadius(Integer.parseInt(text));
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
        result.put("profiles", profiles);

        return result;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public Profile getProfile() {
        return profile;
    }
}
