package uqam.inf4375.mtl375.tasks;

import uqam.inf4375.mtl375.domain.*;

import java.util.*;
import java.util.stream.*;

import com.fasterxml.jackson.annotation.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.*;
import org.slf4j.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.*;

@Component
public class FetchPistesCyclablesTask {

    private ArrayList<PisteCyclable> pistesCyclables;

    private static final Logger log = LoggerFactory.getLogger(FetchPistesCyclablesTask.class);
    private static final String URL = "http://donnees.ville.montreal.qc.ca/dataset/5ea29f40-1b5b-4f34-85b3-7c67088ff536/resource/0dc6612a-be66-406b-b2d9-59c9e1c65ebf/download/reseaucyclable2016dec2016.geojson";

    // @Scheduled(cron="0 0 1 */6 *") // à tous les 6 mois.
    @Scheduled(cron="*/10 * * * * ?") // à toutes les 10 secondes.
    public void execute() {
        String jsonString = "";
        try {
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            jsonString = new String(FileCopyUtils.copyToByteArray(is), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("fuck");
        }
        jsonString = jsonString.substring(jsonString.indexOf("[") + 2); //p-e 11 (pour enlever le [ )
        String[] listePistes = jsonString.split("\\} \\},"); // spliter a la fin d'un objet,debut objet
        List<PisteCyclable> lPistes = new ArrayList<PisteCyclable>();
        for (String piste : listePistes) {
            lPistes.add(stringToPiste(piste));
        }
        System.out.println(lPistes);
    }

    public PisteCyclable stringToPiste(String jsonString) {
        Pattern pId = Pattern.compile("\"ID\": ([0-9]*\\.[0-9]*)");
        double id = Double.parseDouble(valueMatcher(pId, jsonString));

        Pattern pType = Pattern.compile("geometry\": \\{ \"type\": \"([A-Za-z]*)");
        String type = valueMatcher(pType, jsonString);

        Pattern pCoor = Pattern.compile("coordinates\":([0-9\\[\\],\\. ]*)");
        String coordinates = valueMatcher(pCoor, jsonString);
        // a tester (remplissage des coordonees)

        ArrayList<ArrayList<ArrayList<Double>>> lllDouble = new ArrayList<ArrayList<ArrayList<Double>>>();
        // float[][][] tCoordinates = null;
        if (type.equals("MultiLineString")) {
            String[] lCoordinates = coordinates.split(("\\] \\], \\[ \\["));
            for (int i = 0; i < lCoordinates.length; i++) {
                ArrayList<ArrayList<Double>> llDouble = new ArrayList<ArrayList<Double>>();
                Pattern pLineString = Pattern.compile("([0-9]*\\.[0-9]*), ([0-9]*\\.[0-9]*)");
                Matcher mLineString = pLineString.matcher(lCoordinates[i]);
                while (mLineString.find()) {
                    ArrayList<Double> lDouble = new ArrayList<Double>();
                    lDouble.add(Double.parseDouble(mLineString.group(1)));
                    lDouble.add(Double.parseDouble(mLineString.group(2)));
                    llDouble.add(lDouble);
                }
                lllDouble.add(llDouble);
            }
        } else {
            Pattern pLineString = Pattern.compile("([0-9]*\\.[0-9]*), ([0-9]*\\.[0-9]*)");
            Matcher mLineString = pLineString.matcher(coordinates);

            ArrayList<ArrayList<Double>> llDouble = new ArrayList<ArrayList<Double>>();
            while (mLineString.find()) {
                ArrayList<Double> lDouble = new ArrayList<Double>();
                lDouble.add(Double.parseDouble(mLineString.group(1)));
                lDouble.add(Double.parseDouble(mLineString.group(2)));
                llDouble.add(lDouble);
            }
            lllDouble.add(llDouble);
        }
        return new PisteCyclable(id, new Geometry(type, lllDouble));
    }

    public String valueMatcher(Pattern pattern, String xmlString) {
        Matcher matcher = pattern.matcher(xmlString);
        matcher.find();
        return matcher.group(1);
    }
}
