package utils;
import java.util.HashMap;
import java.util.Map;

public class CountryUtils {

    private static final Map<String, String> COUNTRY_MAP = new HashMap<>();

    static {
        COUNTRY_MAP.put("germany", "DE");
        COUNTRY_MAP.put("romania", "RO");
        COUNTRY_MAP.put("france", "FR");
        COUNTRY_MAP.put("italy", "IT");
    }

    public static String toCountryCode(String countryName) {
        if (countryName == null) {
            return null;
        }
        return COUNTRY_MAP.getOrDefault(countryName.trim().toLowerCase(), countryName);
    }
}
