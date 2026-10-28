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
            // BQ's decoder pads or truncates any base64 to 16 bytes, so a typo would become some random id
            if (java.util.Base64.getUrlDecoder()
                .decode(s).length != 16) return null;
            return UuidConverter.decodeUuid(s);
        } catch (RuntimeException e) {
            return null;
        }
    }

    public static String format(UUID id) {
        return UuidConverter.encodeUuid(id);
    }
}
