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


import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
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
    @Scheduled(cron="*/10 * * * * ?") // à toutes les 10 secondes.
    public void execute() {
        try {
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            System.out.println(is);
        } catch (IOException e) {
            System.out.println(e);
        }
  }
}
