package akasha.questui.client.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.UUID;

import org.junit.Test;

public class IdsTest {

    @Test
    public void roundTripAndPlainUuid() {
        UUID id = UUID.fromString("0b1d2c3e-4f50-6172-8394-a5b6c7d8e9f0");
        assertEquals(id, Ids.parse(Ids.format(id)));
        assertEquals(id, Ids.parse(id.toString()));
    }

    /** BQ's decoder pads or truncates any base64; a typo must not become some other id. */
    @Test
    public void rejectsWrongLength() {
        assertNull(Ids.parse("abcd"));
        assertNull(Ids.parse("abcdefghijklmnopqrstuvwxyzAB"));
        assertNull(Ids.parse("not base64!"));
        assertNull(Ids.parse(""));
    }
}
