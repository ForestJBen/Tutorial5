package nz.ac.massey.cs.rss.runner;

import nz.ac.massey.cs.sdc.parsers.Rss;
import nz.ac.massey.cs.sdc.parsers.RssChannel;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import java.io.File;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        try {
            JAXBContext jc = JAXBContext.newInstance("nz.ac.massey.cs.sdc.parsers");
            Unmarshaller parser = jc.createUnmarshaller();
            File file = new File("media-technology.xml");
            Rss rss = (Rss) parser.unmarshal(file);

            RssChannel rssChannel = rss.getChannel();

            Pattern pattern = Pattern.compile("<[^>]+>");

            rssChannel.getItem().forEach(rssItem -> {
                for (Object obj : rssItem.getTitleOrDescriptionOrLink()) {
                    JAXBElement rssItemElement = (JAXBElement) obj;
                    QName qName = rssItemElement.getName();
                    String localName = qName.getLocalPart();

                    switch (localName.toLowerCase()) {
                        case "title":
                        case "description":
                        case "link":
                            String content = rssItemElement.getValue().toString();
                            String cleanContent = pattern.matcher(content).replaceAll(""); // Remove XML tags
                            System.out.println(localName + ": " + cleanContent);
                            break;
                    }
                }
            });
        } catch (JAXBException ex) {
            System.out.println("Error: " + ex.toString());
        }
    }
}

