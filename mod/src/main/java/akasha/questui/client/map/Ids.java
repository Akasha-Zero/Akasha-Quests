package akasha.questui.client.map;

import java.util.UUID;

import betterquesting.api.utils.UuidConverter;

/** Quest/chapter ids as written by people: BQ's base64 form (what "copy quest id" gives) or a plain UUID. */
public final class Ids {

    private Ids() {}

    public static UUID parse(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        try {
            if (s.length() == 36 && s.charAt(8) == '-') return UUID.fromString(s);
            return UuidConverter.decodeUuid(s);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static String format(UUID id) {
        return UuidConverter.encodeUuid(id);
    }
}
