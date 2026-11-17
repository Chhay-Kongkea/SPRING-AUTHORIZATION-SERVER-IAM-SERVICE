package kh.edu.istad.iam.web.i18n;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class Messages {

    private final MessageSource source;
    private final Locale locale;

    public Messages(MessageSource source, Locale locale) {
        this.source = source;
        this.locale = locale;
    }

    /** Resolves {@code key} for the current locale; unknown keys render as the key itself. */
    public String get(String key, Object... args) {
        return source.getMessage(key, args, key, locale);
    }
}
