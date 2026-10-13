package utils;
import java.util.Locale;
public class CountryUtils {
    public static String toCountryCode(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Country is required");
        for (String code : Locale.getISOCountries()) {
            if (code.equalsIgnoreCase(name.trim()) || new Locale("", code).getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(name.trim())) return code;
        }
        throw new IllegalArgumentException("Unknown country: " + name);
    }
}