package az.kon.academy.application.core.helper;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public final class Slugify {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern MULTIPLE_HYPHENS = Pattern.compile("-{2,}");

    private Slugify() {
    }

    public static String of(String input) {

        if(Objects.isNull(input)) return null;

        String result = Normalizer.normalize(input, Normalizer.Form.NFD);

        result = result.replaceAll("\\p{M}", "");

        result = result.toLowerCase(Locale.ROOT);

        result = WHITESPACE.matcher(result).replaceAll("-");

        result = NONLATIN.matcher(result).replaceAll("");

        result = MULTIPLE_HYPHENS.matcher(result).replaceAll("-");

        result = result.replaceAll("^-|-$", "");

        return result;
    }
}
