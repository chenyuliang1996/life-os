package io.github.yuliangchen.lifeos.domain.support;

import java.util.Locale;
import java.util.regex.Pattern;

public final class LocaleSupport {

    private static final Pattern HAN_PATTERN = Pattern.compile(".*\\p{IsHan}+.*");

    private LocaleSupport() {
    }

    public static String resolve(String locale, String fallbackText) {
        if (locale != null && !locale.isBlank()) {
            return normalize(locale);
        }
        if (fallbackText != null && HAN_PATTERN.matcher(fallbackText).matches()) {
            return "zh-CN";
        }
        return "en-US";
    }

    public static boolean isChinese(String locale) {
        return normalize(locale).startsWith("zh");
    }

    public static String pick(String locale, String zhValue, String enValue) {
        return isChinese(locale) ? zhValue : enValue;
    }

    public static String normalize(String locale) {
        if (locale == null || locale.isBlank()) {
            return "en-US";
        }
        String lowered = locale.toLowerCase(Locale.ROOT);
        if (lowered.startsWith("zh")) {
            return "zh-CN";
        }
        return "en-US";
    }
}
