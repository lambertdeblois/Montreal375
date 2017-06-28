package uqam.inf4375.mtl375.tasks;

import uqam.inf4375.mtl375.domain.*;

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

@Component
public class FetchBixiTask {

    private ArrayList<StationBixi> stationsBixi;

    private static final Logger log = LoggerFactory.getLogger(FetchBixiTask.class);
    private static final String URL = "https://montreal.bixi.com/data/bikeStations.xml";


  // @Scheduled(cron="*/10 * * * *") // à toutes les 10 minutes.
    @Scheduled(cron="*/5 * * * * ?") // à toutes les 10 secondes.
    public void execute() {
        String string = "";
        try {
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            string = new String(FileCopyUtils.copyToByteArray(is), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("fuck");
        }
        File file = new File(string);
        string = string.substring(string.indexOf("<station>"));
        String[] listeStation = string.split("</station><station>");

        System.out.println(listeStation[0]);
  }
}
