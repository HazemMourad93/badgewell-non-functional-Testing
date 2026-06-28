package example.HelperClassTUT;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class VideoGameFeeder {

    public static LocalDate randomDate() {
        int hundredYears = 100 * 365;
        return LocalDate.ofEpochDay(ThreadLocalRandom.current().nextInt(-hundredYears, hundredYears));
    }

    public static Iterator<Map<String, Object>> customFeeder() {
        return Stream.generate((Supplier<Map<String, Object>>) () -> {
            Random rand = new Random();

            int gameId           = rand.nextInt(10) + 1;
            String gameName      = RandomStringUtils.randomAlphanumeric(5) + "-gameName";
            String releaseDate   = randomDate().toString();
            int reviewScore      = rand.nextInt(100);
            String category      = RandomStringUtils.randomAlphanumeric(5) + "-category";
            String rating        = RandomStringUtils.randomAlphanumeric(4) + "-rating";

            HashMap<String, Object> hmap = new HashMap<>();
            hmap.put("gameId",      gameId);
            hmap.put("gameName",    gameName);
            hmap.put("releaseDate", releaseDate);
            hmap.put("reviewScore", reviewScore);
            hmap.put("category",    category);
            hmap.put("rating",      rating);
            return hmap;
        }).iterator();
    }

    /**
     * Builds a VideoGameBody from a Gatling session map entry.
     * Useful when you need to pass feeder data into request flows.
     */
    public static VideoGameBody bodyFromSession(Map<String, Object> map) {
        return new VideoGameBody(
                (String)  map.get("gameName"),
                (String)  map.get("releaseDate"),
                (Integer) map.get("reviewScore"),
                (String)  map.get("category"),
                (String)  map.get("rating")
        );
    }
}