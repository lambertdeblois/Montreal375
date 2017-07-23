package uqam.inf4375.mtl375.tasks;

import uqam.inf4375.mtl375.domain.*;
import uqam.inf4375.mtl375.repositories.*;

import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;

import java.nio.charset.StandardCharsets;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.util.FileCopyUtils;

@Component
public class FetchPistesCyclablesTask {

    private static final Logger log = LoggerFactory.getLogger(FetchPistesCyclablesTask.class);
    private static final String URL = "http://donnees.ville.montreal.qc.ca/dataset/5ea29f40-1b5b-4f34-85b3-7c67088ff536/resource/248cc537-a328-43ae-af5c-1fa8293f1c1f/download/reseaucyclable2017juin2017.kml";

    @Autowired
    private PisteCyclableRepository repository;

    //@Scheduled(cron="*/10 * * * * *") // every 10 seconds.
    @Scheduled(cron = "0 0 0 1 */6 ?") // every 6 months.
    public void execute() {
        String kmlString = "";
        try {
            URL url = new URL(URL);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            kmlString = new String(FileCopyUtils.copyToByteArray(is), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("fuck");
        }
        kmlString = kmlString.substring(kmlString.indexOf("table") + 2); //p-e 11 (pour enlever le [ )
        String[] listePistes = kmlString.split("<br><br><br>"); // spliter a la fin d'un objet,debut objet
        for (String piste : listePistes) {
            PisteCyclable p = stringToPiste(piste);
            repository.insert(p);
            log.info(p.toString());
        }
    }

    public PisteCyclable stringToPiste(String string) {
        Pattern pId = Pattern.compile("ID</td><td>([0-9]*)");
        int id = Integer.parseInt(valueMatcher(pId, string));
        string = string.replaceAll("\\s+", "");

        // matcher toutes les coordonnees et enlever les 0
        Pattern pCoor = Pattern.compile("<coordinates>(-?[0-9]*\\.[0-9]*),(-?[0-9]*\\.[0-9]*)");
        Matcher match = pCoor.matcher(string);
        match.find();
        String coord = match.group(1) + " " + match.group(2);
        coord = coord.replaceAll(",", " ");
        Pattern pCoord = Pattern.compile(",0(-?[0-9]*\\.[0-9]*),(-?[0-9]*\\.[0-9]*)");
        Matcher matcher = pCoord.matcher(string);
        while (matcher.find()) {
            coord = coord + ',' + matcher.group(1) + " " + matcher.group(2);
        }
        coord = "LINESTRING(" + coord + ")";
        return new PisteCyclable(id, coord);
    }

    public String valueMatcher(Pattern pattern, String string) {
        Matcher matcher = pattern.matcher(string);
        matcher.find();
        return matcher.group(1);
    }
}
