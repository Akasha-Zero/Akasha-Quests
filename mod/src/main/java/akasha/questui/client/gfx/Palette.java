package akasha.questui.client.gfx;

/** Colour tokens (DESIGN §3, §4, §10). ARGB. */
public final class Palette {

    private Palette() {}

    /** Map background with the configured opacity (DESIGN §10). */
    public static int mapBg() {
        return Draw.withAlpha(MAP_BG, (float) akasha.questui.Config.backgroundOpacity);
    }

    /** Side panels and toolbar: a step more opaque than the map, a = min(1, a_map + 0.15). */
    public static int panelBg() {
        return Draw.withAlpha(PANEL, (float) Math.min(1.0, akasha.questui.Config.backgroundOpacity + 0.15));
    }

    public static int panelHeaderBg() {
        return Draw.withAlpha(PANEL_HEADER, (float) Math.min(1.0, akasha.questui.Config.backgroundOpacity + 0.15));
    }

    // Surfaces
    public static final int MAP_BG = 0xFF16161A;
    public static final int GRID = 0xFFFFFFFF;
    public static final int PANEL = 0xFF101014;
    public static final int PANEL_EDGE = 0xFF3A3A44;
    public static final int PANEL_HEADER = 0xFF1C1C22;
    public static final int ROW_HOVER = 0x1FFFFFFF;
    public static final int ROW_SELECTED = 0x33FFFFFF;
    public static final int ACCENT = 0xFF3FD2E0;
    public static final int DIM = 0x99000000;
    public static final int DIVIDER = 0xFF2A2A32;

    // Text
    public static final int TEXT = 0xFFEDEDED;
    public static final int TEXT_2 = 0xFFA6A6B0;
    public static final int TEXT_OFF = 0xFF62626C;
    public static final int TEXT_GROUP = 0xFFC9C9D1;
    public static final int TEXT_CHAPTER = 0xFFD8D8DE;
    public static final int TEXT_DONE = 0xFF7CD977;
    public static final int TASKS = 0xFF8E8EFF;
    public static final int REWARDS = 0xFFFFC64A;

    // Buttons
    public static final int BTN = 0xFF26262C;
    public static final int BTN_HOVER = 0xFF33333B;
    public static final int BTN_EDGE = 0xFF4A4A55;
    public static final int BTN_EDGE_HOVER = 0xFFBFBFC8;

    // Quest states: fill / outline
    public static final int Q_LOCKED_FILL = 0xFF2B2B2E;
    public static final int Q_LOCKED_EDGE = 0xFF5E5E66;
    public static final int Q_FILL = 0xFF3A3A40;
    public static final int Q_AVAILABLE_EDGE = 0xFFE6E6E6;
    public static final int Q_PROGRESS_EDGE = 0xFF3FD2E0;
    public static final int Q_DONE_FILL = 0xFF2E3A2C;
    public static final int Q_DONE_EDGE = 0xFF5BD94F;
    public static final int Q_REPEAT_EDGE = 0xFFB584F0;

    // Dependency lines
    public static final int L_LOCKED = 0xFF4A4A52;
    public static final int L_OPEN = 0xFFBDBDC6;
    public static final int L_DONE = 0xFF5BD94F;
    public static final int L_REPEAT = 0xFFB584F0;
    public static final int L_REQUIRES = 0xFF46E0FF;
    public static final int L_REQUIRED_FOR = 0xFFFFD34A;

    // Editing
    public static final int SELECT = 0xFF3FD2E0;
    public static final int SELECT_FILL = 0x223FD2E0;
    public static final int HANDLE = 0xFFFFFFFF;
}
