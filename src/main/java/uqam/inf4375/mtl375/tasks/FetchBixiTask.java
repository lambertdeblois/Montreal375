package uqam.inf4375.mtl375.tasks;

import uqam.inf4375.mtl375.domain.*;
import uqam.inf4375.mtl375.repositories.*;

import java.util.*;
import java.util.stream.*;

import com.fasterxml.jackson.annotation.*;
import org.jsoup.*;
import org.slf4j.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.web.client.*;
import org.springframework.util.FileCopyUtils;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.nio.charset.StandardCharsets;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FetchBixiTask {

    private ArrayList<StationBixi> stationsBixi;

    private static final Logger log = LoggerFactory.getLogger(FetchBixiTask.class);
    private static final String URL = "https://montreal.bixi.com/data/bikeStations.xml";

    @Autowired private StationBixiRepository repository;

    @Scheduled(cron="*/10 * * * *") // à toutes les 10 minutes.
    //@Scheduled(cron = "*/5 * * * * ?") // à toutes les 10 secondes.
    public void execute() {
        String xmlString = "";
        try {
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            xmlString = new String(FileCopyUtils.copyToByteArray(is), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("fuck");
        }
        xmlString = xmlString.substring(xmlString.indexOf("<station>") + 9);
        String[] listeStation = xmlString.split("</station><station>");

        for (String station: listeStation){
          StationBixi s = stringToStation(station);
          repository.insert(s);
          log.info(s.toString());
        }
    }

    public StationBixi stringToStation(String xmlString) {
        Pattern patternId = Pattern.compile("<id>([0-9]*)</id>");
        int id = Integer.parseInt(valueMatcher(patternId, xmlString));

        Pattern patternName = Pattern.compile("<name>(.*)</name>");
        String name = valueMatcher(patternName, xmlString);

        Pattern patternLat = Pattern.compile("<lat>(-?[0-9]*.[0-9]*)</lat>");
        Double lat = Double.parseDouble(valueMatcher(patternLat, xmlString));

        Pattern patternLong = Pattern.compile("<long>(-?[0-9]*.[0-9]*)</long>");
        Double longueur = Double.parseDouble(valueMatcher(patternLong, xmlString));

        Pattern patternNbBikes = Pattern.compile("<nbBikes>([0-9]*)</nbBikes>");
        int nbBikes = Integer.parseInt(valueMatcher(patternNbBikes, xmlString));

        Pattern patternNbEmptyDocks = Pattern.compile("<nbEmptyDocks>([0-9]*)</nbEmptyDocks>");
        int nbEmptyDocks = Integer.parseInt(valueMatcher(patternNbEmptyDocks, xmlString));

        return new StationBixi(id, name, nbBikes, nbEmptyDocks, lat, longueur);
    }

    public String valueMatcher(Pattern pattern, String xmlString) {
        Matcher matcher = pattern.matcher(xmlString);
        matcher.find();
        return matcher.group(1);
    }
}
