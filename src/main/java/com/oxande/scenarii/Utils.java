package com.oxande.scenarii;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Utils {
    public static final char[] FORBIDDEN = new char[]{
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x0a, 0x0b, 0x0d, 0x0e, 0x0f, 0x10,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
            0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27,
    };

    public static String clean(String str, char[] toRemove) {
        if (str == null || str.length() < 1) return null;
        StringBuilder buf = new StringBuilder();
        // Create the filter
        Set<Integer> filter = IntStream.range(0, toRemove.length)
                .map(i -> (int) toRemove[i])
                .boxed()
                .collect(Collectors.toSet());
        // then filter
        str.codePoints()
                .filter(c -> !filter.contains(c))
                .forEach(buf::append);
        return StringUtils.trimToNull(buf.toString());
    }

    public static String checked(String str, int length) {
        String cleaned = clean(str, FORBIDDEN);
        if (cleaned != null && cleaned.length() > length) {
            throw new IllegalArgumentException("String is limited to " + length + " characters.");
        }
        return cleaned;
    }

    public static String checked(String str) {
        return StringUtils.normalizeSpace(checked(str, 255));
    }

    public static String checkedCmt(String str) {
        return StringUtils.normalizeSpace(checked(str, 4000));
    }

    public static <A> A checkIn(A reference, A... values) {
        if (reference != null) {
            List<A> valueList = Arrays.asList(values);
            if (!valueList.contains(reference)) {
                throw new IllegalArgumentException("Expected values are " + valueList);
            }
        }
        return reference;
    }

}
