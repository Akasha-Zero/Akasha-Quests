#!/usr/bin/env python3
"""Procedural textures for Akasha Quests UI.

Every quest shape is defined as an exact closed polygon in the unit frame [-1, 1]^2
(y grows downwards, like the screen). From the polygon we compute an exact signed
distance field d(p) (negative inside) and derive every layer analytically:

    coverage(p) = clamp(1/2 - d(p)/px, 0, 1)            (1-pixel analytic anti-aliasing)
    outline(p)  = coverage(d) * clamp(1/2 + (d + t)/px)  (ring of thickness t inside the edge)
    glow(p)     = exp(-max(d, 0)^2 / (2 sigma^2))         (gaussian halo outside the edge)

The same polygons are written to shapes.json so the game hit-tests with the exact
geometry it draws. See docs/DESIGN.md for the derivations and constants.

Usage: python3 tools/gen_textures.py   (writes into src/main/resources/assets/akashaquestui)
"""

import json
import math
import os

import numpy as np
from PIL import Image

ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)), '..', 'src', 'main', 'resources', 'assets',
                    'akashaquestui')
TEX = os.path.join(ROOT, 'textures')

# ---------------------------------------------------------------------------------------------
# Constants (docs/DESIGN.md §2)
# ---------------------------------------------------------------------------------------------
PX_PER_UNIT = 64           # texture resolution: 64 texels per half-size unit
SHAPE_TEX = 128            # body layers cover [-1, 1]   -> 128 px
GLOW_TEX = 256             # glow layer covers   [-2, 2]   -> 256 px
FIT = 1.0 - 1.5 / PX_PER_UNIT  # shapes fit [-FIT, FIT] so the AA ramp never touches the border
OUTLINE_T = 0.11           # outline thickness, in half-size units
GLOW_SIGMA = 0.24          # glow gaussian sigma, in half-size units
BEVEL_B = 0.18             # inner bevel band width
LIGHT = np.array([-0.55, -0.835])  # light direction (top-left), unit length
LIGHT = LIGHT / np.linalg.norm(LIGHT)
HIT_VERTS = 96             # max vertices kept for the in-game hit-test polygon of curved shapes


# ---------------------------------------------------------------------------------------------
# Geometry
# ---------------------------------------------------------------------------------------------
def regular_polygon(n, phase):
    """Vertices of a regular n-gon of circumradius 1, first vertex at angle `phase` (radians)."""
    return [(math.cos(phase + 2 * math.pi * k / n), math.sin(phase + 2 * math.pi * k / n)) for k in range(n)]


def fit_to_box(pts, fit=FIT):
    """Uniformly scale and translate so the bounding box is centred and touches [-fit, fit]."""
    xs = [p[0] for p in pts]
    ys = [p[1] for p in pts]
    cx, cy = (min(xs) + max(xs)) / 2, (min(ys) + max(ys)) / 2
    s = 2 * fit / max(max(xs) - min(xs), max(ys) - min(ys))
    return [((x - cx) * s, (y - cy) * s) for x, y in pts]


def rounded_square(rho, seg=24):
    """Square [-1,1]^2 with corner radius rho, arcs sampled with `seg` segments each."""
    c = 1 - rho
    pts = []
    for qx, qy, a0 in ((c, c, 0), (-c, c, 90), (-c, -c, 180), (c, -c, 270)):
        for i in range(seg + 1):
            a = math.radians(a0 + 90 * i / seg)
            pts.append((qx + rho * math.cos(a), qy + rho * math.sin(a)))
    return pts


def heart(n=720):
    """Classic heart curve x = 16 sin^3 t, y = 13 cos t - 5 cos 2t - 2 cos 3t - cos 4t (y flipped)."""
    pts = []
    for i in range(n):
        t = 2 * math.pi * i / n
        x = 16 * math.sin(t) ** 3
        y = 13 * math.cos(t) - 5 * math.cos(2 * t) - 2 * math.cos(3 * t) - math.cos(4 * t)
        pts.append((x, -y))
    return pts


def gear(teeth=8, r_out=1.0, r_in=0.78, top=0.36, flank=0.14, per_tooth=64):
    """Gear outline in polar form r(theta) = r_in + (r_out - r_in) * T(theta).

    T is a periodic trapezoid of period 1 (one tooth): rises over `flank`, holds 1 over `top`,
    falls over `flank`, holds 0 for the rest. Rotated so a tooth points straight up.
    """
    def T(u):
        u %= 1.0
        if u < flank:
            return u / flank
        if u < flank + top:
            return 1.0
        if u < 2 * flank + top:
            return 1.0 - (u - flank - top) / flank
        return 0.0

    pts = []
    n = teeth * per_tooth
    centre = flank + top / 2  # phase of a tooth's middle
    for i in range(n):
        u = i / per_tooth
        theta = 2 * math.pi * (u - centre) / teeth - math.pi / 2
        r = r_in + (r_out - r_in) * T(u)
        pts.append((r * math.cos(theta), r * math.sin(theta)))
    return pts


def star(points=5, inner=0.5):
    pts = []
    for k in range(2 * points):
        r = 1.0 if k % 2 == 0 else inner
        a = -math.pi / 2 + math.pi * k / points
        pts.append((r * math.cos(a), r * math.sin(a)))
    return pts


SHAPES = {
    'circle': fit_to_box(regular_polygon(256, 0)),
    'square': fit_to_box([(-1, -1), (1, -1), (1, 1), (-1, 1)]),
    'rsquare': fit_to_box(rounded_square(0.32)),
    'diamond': fit_to_box([(0, -1), (1, 0), (0, 1), (-1, 0)]),
    'pentagon': fit_to_box(regular_polygon(5, -math.pi / 2)),
    'hexagon': fit_to_box(regular_polygon(6, -math.pi / 2)),
    'octagon': fit_to_box(regular_polygon(8, math.pi / 8)),
    'heart': fit_to_box(heart()),
    'gear': fit_to_box(gear()),
    'star': fit_to_box(star()),
}


def signed_distance(poly, xs, ys):
    """Exact signed distance from grid points to a closed polygon (negative inside).

    Distance = min over edges of the point-segment distance; sign from the even-odd rule.
    """
    px = xs.ravel()
    py = ys.ravel()
    best = np.full(px.shape, np.inf)
    inside = np.zeros(px.shape, dtype=bool)
    n = len(poly)
    for i in range(n):
        ax, ay = poly[i]
        bx, by = poly[(i + 1) % n]
        ex, ey = bx - ax, by - ay
        wx, wy = px - ax, py - ay
        ee = ex * ex + ey * ey
        t = np.clip((wx * ex + wy * ey) / ee, 0.0, 1.0)
        dx, dy = wx - ex * t, wy - ey * t
        best = np.minimum(best, dx * dx + dy * dy)
        # even-odd crossing test of a horizontal ray towards +x
        cond = (ay > py) != (by > py)
        xint = ax + (py - ay) * ex / np.where(ey == 0, 1e-12, ey)
        inside ^= cond & (px < xint)
    d = np.sqrt(best)
    d[inside] *= -1
    return d.reshape(xs.shape)


def grid(size, extent):
    """Texel-centre coordinates of a size x size texture covering [-extent, extent]^2."""
    c = (np.arange(size) + 0.5) / size * 2 * extent - extent
    return np.meshgrid(c, c)


def smooth_cov(d, px):
    return np.clip(0.5 - d / px, 0.0, 1.0)


def save_rgba(path, rgb, alpha):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    rgb = np.clip(rgb, 0.0, 1.0)
    if rgb.ndim == 2:
        rgb = np.stack([rgb] * 3, axis=-1)
    a = np.clip(alpha, 0.0, 1.0)
    img = np.concatenate([rgb, a[..., None]], axis=-1)
    Image.fromarray((img * 255 + 0.5).astype(np.uint8), 'RGBA').save(path, optimize=True)


def gradient(d, xs, ys):
    """Unit outward normal of the distance field by central differences."""
    gy, gx = np.gradient(d, ys[:, 0], xs[0, :])
    n = np.sqrt(gx * gx + gy * gy) + 1e-9
    return gx / n, gy / n


def shape_layers(name, poly):
    px = 1.0 / PX_PER_UNIT
    xs, ys = grid(SHAPE_TEX, 1.0)
    d = signed_distance(poly, xs, ys)
    cov = smooth_cov(d, px)

    # Fill: vertical luminance ramp + directional inner bevel (light from the top-left).
    nx, ny = gradient(d, xs, ys)
    lum = 0.92 - 0.12 * (ys + 1) / 2
    band = np.clip(1 + d / BEVEL_B, 0.0, 1.0) ** 2          # 1 at the edge, 0 at depth BEVEL_B
    facing = -(nx * LIGHT[0] + ny * LIGHT[1])                # +1 where the edge faces the light
    lum = lum + 0.16 * facing * band
    save_rgba(os.path.join(TEX, 'shapes', name + '_fill.png'), lum, cov)

    # Outline ring and flat mask.
    ring = cov * np.clip(0.5 + (d + OUTLINE_T) / px, 0.0, 1.0)
    save_rgba(os.path.join(TEX, 'shapes', name + '_outline.png'), np.ones_like(d), ring)
    save_rgba(os.path.join(TEX, 'shapes', name + '_mask.png'), np.ones_like(d), cov)

    # Glow halo over [-2, 2].
    gx, gy = grid(GLOW_TEX, 2.0)
    dg = signed_distance(poly, gx, gy)
    glow = np.exp(-np.maximum(dg, 0.0) ** 2 / (2 * GLOW_SIGMA ** 2))
    save_rgba(os.path.join(TEX, 'shapes', name + '_glow.png'), np.ones_like(dg), glow)


def simplify(poly, max_verts):
    if len(poly) <= max_verts:
        return poly
    step = len(poly) / max_verts
    return [poly[int(i * step)] for i in range(max_verts)]


# ---------------------------------------------------------------------------------------------
# Badges: coloured glyphs with a dark outline, composited "outline under fill".
# ---------------------------------------------------------------------------------------------
def seg_dist(xs, ys, a, b):
    ax, ay = a
    bx, by = b
    ex, ey = bx - ax, by - ay
    wx, wy = xs - ax, ys - ay
    t = np.clip((wx * ex + wy * ey) / (ex * ex + ey * ey), 0, 1)
    return np.hypot(wx - ex * t, wy - ey * t)


def polyline_dist(xs, ys, pts):
    return np.minimum.reduce([seg_dist(xs, ys, pts[i], pts[i + 1]) for i in range(len(pts) - 1)])


def arc_dist(xs, ys, c, r, a0, a1):
    """Distance to a circular arc from angle a0 to a1 (radians, counter-clockwise on screen)."""
    ang = np.arctan2(ys - c[1], xs - c[0])
    span = (a1 - a0) % (2 * math.pi)
    rel = (ang - a0) % (2 * math.pi)
    on = rel <= span
    d_arc = np.abs(np.hypot(xs - c[0], ys - c[1]) - r)
    e0 = (c[0] + r * math.cos(a0), c[1] + r * math.sin(a0))
    e1 = (c[0] + r * math.cos(a1), c[1] + r * math.sin(a1))
    d_end = np.minimum(np.hypot(xs - e0[0], ys - e0[1]), np.hypot(xs - e1[0], ys - e1[1]))
    return np.where(on, d_arc, d_end)


def badge(name, sdf_fn, color, outline=(0.07, 0.07, 0.08), out_w=0.13, size=64):
    """sdf_fn(xs, ys) returns the glyph's signed distance; outline grows it by out_w."""
    px = 2.0 / size
    xs, ys = grid(size, 1.0)
    d = sdf_fn(xs, ys)
    fill = smooth_cov(d, px)
    edge = smooth_cov(d - out_w, px)
    col = np.array(color, dtype=float)
    out = np.array(outline, dtype=float)
    rgb = out[None, None, :] * (1 - fill[..., None]) + col[None, None, :] * fill[..., None]
    # premultiplied "over" of fill on outline collapses to alpha = edge (fill is inside edge)
    save_rgba(os.path.join(TEX, 'badges', name + '.png'), rgb, edge)


def badges():
    # Check mark: polyline stroke of half-width 0.17.
    badge('check', lambda x, y: polyline_dist(x, y, [(-0.58, 0.02), (-0.18, 0.42), (0.6, -0.44)]) - 0.17,
          (0.36, 0.88, 0.31))
    # Exclamation: capsule + dot.
    badge('alert', lambda x, y: np.minimum(seg_dist(x, y, (0, -0.6), (0, 0.12)) - 0.16,
                                           np.hypot(x, y - 0.55) - 0.17),
          (1.0, 0.78, 0.24))
    # Repeat: open ring + arrow head (triangle via max of three half-planes).
    def repeat(x, y):
        ring = arc_dist(x, y, (0, 0), 0.5, math.radians(-50), math.radians(230)) - 0.13
        tip = np.array([0.5 * math.cos(math.radians(-50)), 0.5 * math.sin(math.radians(-50))])
        # Arrow head: isoceles triangle pointing along the arc tangent at the end.
        tangent = np.array([math.sin(math.radians(-50)), -math.cos(math.radians(-50))])  # clockwise end
        normal = np.array([-tangent[1], tangent[0]])
        apex = tip + tangent * 0.34
        b1 = tip + normal * 0.3
        b2 = tip - normal * 0.3
        tri = [tuple(apex), tuple(b1), tuple(b2)]
        dt = signed_distance(tri, x, y)
        return np.minimum(ring, dt)
    badge('repeat', repeat, (0.71, 0.52, 1.0))
    # Padlock: rounded body + shackle arc.
    def lock(x, y):
        body_poly = [(px_ * 0.55, py_ * 0.42 + 0.3) for px_, py_ in rounded_square(0.3)]
        body = signed_distance(body_poly, x, y)
        shackle = arc_dist(x, y, (0, -0.12), 0.3, math.radians(180), math.radians(360)) - 0.1
        legs = np.minimum(seg_dist(x, y, (-0.3, -0.12), (-0.3, 0.0)), seg_dist(x, y, (0.3, -0.12), (0.3, 0.0))) - 0.1
        return np.minimum(body, np.minimum(shackle, legs))
    badge('lock', lock, (0.72, 0.72, 0.74))
    # Pin (bookmark): round head + needle.
    def pin(x, y):
        head = np.hypot(x - 0.18, y + 0.22) - 0.36
        needle = seg_dist(x, y, (0.0, 0.0), (-0.55, 0.6)) - 0.09
        return np.minimum(head, needle)
    badge('pin', pin, (0.93, 0.33, 0.33))


# ---------------------------------------------------------------------------------------------
# Dependency line tiles. u runs along the line (2 line-widths per tile), v across it.
# ---------------------------------------------------------------------------------------------
def line_tiles():
    W, H = 64, 32                      # u: 2 widths -> 64 px, v: 1 width -> 32 px
    u = (np.arange(W) + 0.5) / W * 2.0
    v = (np.arange(H) + 0.5) / H - 0.5
    U, V = np.meshgrid(u, v)
    px = 1.0 / H
    edge = np.clip((0.5 - np.abs(V)) / px, 0, 1)                    # band coverage
    border = np.clip((np.abs(V) - (0.5 - 1.6 * px)) / px, 0, 1)      # 1.6 px dark rim
    body_l = 0.62 * (1 - border) + 0.30 * border

    # Chevron ">" centred in the tile; repeated by GL_REPEAT. Evaluate at U and U +- 2 for seamless wrap.
    def chev(Uc):
        return polyline_dist(Uc, V, [(0.62, -0.40), (1.22, 0.0), (0.62, 0.40)]) - 0.12
    dch = np.minimum.reduce([chev(U), chev(U - 2.0), chev(U + 2.0)])
    ch = smooth_cov(dch, px)
    lum = body_l * (1 - ch) + 1.0 * ch
    alpha = edge * (0.78 * (1 - ch) + 1.0 * ch)
    save_rgba(os.path.join(TEX, 'lines', 'chevron.png'), lum, alpha)
    save_rgba(os.path.join(TEX, 'lines', 'plain.png'), body_l, edge * 0.85)

    # Dashed variant for OR/implicit links: band visible on half the period.
    dash = np.clip((0.5 - np.abs(((U % 1.0) - 0.5)) * 2 - 0.0) / (2 * px) + 0.5, 0, 1)
    save_rgba(os.path.join(TEX, 'lines', 'dashed.png'), body_l, edge * 0.85 * dash)


# ---------------------------------------------------------------------------------------------
# BetterQuesting theme atlas: flat FTB-style 9-slice pieces (pixel exact, sampled GL_NEAREST by BQ).
# ---------------------------------------------------------------------------------------------
def hexrgba(c, a=255):
    return ((c >> 16) & 255, (c >> 8) & 255, c & 255, a)


THEME_PIECES = {}  # key -> (bounds, padding, sliceMode)


def theme_atlas():
    S = 64
    img = np.zeros((S, S, 4), dtype=np.uint8)

    def box(key, x, y, w, h, fill, edge=None, pad=(1, 1, 1, 1), mode=2, edge2=None, fill_a=255):
        """Filled rectangle with a 1px border. edge2 colours the bottom/right edges (bevel)."""
        img[y:y + h, x:x + w] = hexrgba(fill, fill_a)
        if edge is not None:
            e2 = edge if edge2 is None else edge2
            img[y, x:x + w] = hexrgba(edge)
            img[y:y + h, x] = hexrgba(edge)
            img[y + h - 1, x:x + w] = hexrgba(e2)
            img[y:y + h, x + w - 1] = hexrgba(e2)
        if key:
            THEME_PIECES[key] = ([x, y, w, h], list(pad), mode)

    # Panels (16x16, padding 3)
    box('panel_main', 0, 0, 16, 16, 0x18181D, 0x3A3A44, (3, 3, 3, 3))
    img[1, 1:15] = hexrgba(0x24242B)          # faint inner top highlight
    box('panel_inner', 16, 0, 16, 16, 0x121216, 0x2C2C34, (3, 3, 3, 3))
    box('aux_frame_0', 32, 0, 16, 16, 0x000000, 0x3A3A44, (3, 3, 3, 3), fill_a=0)
    box('tooltip_bg', 48, 0, 8, 8, 0x100010, 0x3D2A6E, (2, 2, 2, 2), fill_a=240)
    # Buttons (8x8, padding 1): disabled / idle / hover
    box('btn_normal_0', 0, 16, 8, 8, 0x1E1E22, 0x2E2E35)
    box('btn_normal_1', 8, 16, 8, 8, 0x26262C, 0x4A4A55)
    box('btn_normal_2', 16, 16, 8, 8, 0x33333B, 0xBFBFC8)
    # Item slot: recessed, dark top-left edge, light bottom-right edge
    box('item_frame', 24, 16, 8, 8, 0x0E0E11, 0x060608, edge2=0x3A3A44)
    # Text boxes: idle / hover / focused
    box('text_box_0', 32, 16, 8, 8, 0x0E0E11, 0x3A3A44)
    box('text_box_1', 40, 16, 8, 8, 0x0E0E11, 0x6A6A76)
    box('text_box_2', 48, 16, 8, 8, 0x0E0E11, 0x3FD2E0)
    # Scroll bars
    box('scroll_v_bg', 0, 24, 8, 16, 0x0E0E11, 0x26262C)
    box('scroll_v_0', 8, 24, 8, 16, 0x4A4A55, 0x5E5E6A, (3, 4, 3, 4))
    box('scroll_v_1', 16, 24, 8, 16, 0x5A5A66, 0x70707C, (3, 4, 3, 4))
    box('scroll_v_2', 24, 24, 8, 16, 0x6A6A76, 0x8A8A96, (3, 4, 3, 4))
    box('scroll_h_bg', 32, 24, 16, 8, 0x0E0E11, 0x26262C)
    box('scroll_h_0', 32, 32, 16, 8, 0x4A4A55, 0x5E5E6A, (4, 3, 4, 3))
    box('scroll_h_1', 48, 24, 16, 8, 0x5A5A66, 0x70707C, (4, 3, 4, 3))
    box('scroll_h_2', 48, 32, 16, 8, 0x6A6A76, 0x8A8A96, (4, 3, 4, 3))
    # Progress meter (BQ: meter_h_0 background, meter_h_1 fill)
    box('meter_h_0', 0, 40, 8, 8, 0x0E0E11, 0x26262C)
    box('meter_h_1', 8, 40, 8, 8, 0x3FD2E0, 0x2BA9B5)

    # Quest frames for BQ's own canvases (designer): white ring circle / hexagon, tinted by BQ.
    def ring(key, x, y, poly):
        xs, ys = grid(24, 1.0)
        d = signed_distance(poly, xs, ys)
        px = 2.0 / 24
        a = (d <= 0)                         # pixel-exact (nearest) coverage
        ring_ = a & (d > -2.2 * px)
        rgba = np.zeros((24, 24, 4), dtype=np.uint8)
        rgba[a] = hexrgba(0x2A2A30)
        rgba[ring_] = (255, 255, 255, 255)
        img[y:y + 24, x:x + 24] = rgba
        THEME_PIECES[key] = ([x, y, 24, 24], [8, 8, 8, 8], 2)

    ring('_circle', 16, 40, SHAPES['circle'])
    ring('_hexagon', 40, 40, SHAPES['hexagon'])
    for i in range(5):
        THEME_PIECES['quest_norm_%d' % i] = THEME_PIECES['_circle']
        THEME_PIECES['quest_main_%d' % i] = THEME_PIECES['_hexagon']
    del THEME_PIECES['_circle'], THEME_PIECES['_hexagon']

    os.makedirs(os.path.join(TEX, 'gui'), exist_ok=True)
    Image.fromarray(img, 'RGBA').save(os.path.join(TEX, 'gui', 'theme.png'), optimize=True)


def placeholder():
    """Neutral 'image' glyph for newly added chapter images: framed card, sun and two mountains."""
    S = 64
    px = 2.0 / S
    xs, ys = grid(S, 1.0)
    card = signed_distance(fit_to_box(rounded_square(0.18), 0.94), xs, ys)
    inner = signed_distance(fit_to_box(rounded_square(0.14), 0.80), xs, ys)
    sun = np.hypot(xs - 0.38, ys + 0.36) - 0.15
    m1 = signed_distance([(-0.80, 0.62), (-0.22, -0.08), (0.36, 0.62)], xs, ys)
    m2 = signed_distance([(-0.05, 0.62), (0.36, 0.12), (0.80, 0.62)], xs, ys)
    clip = inner
    rgb = np.zeros((S, S, 3))
    a = smooth_cov(card, px)
    rgb[:] = (0.23, 0.23, 0.27)
    ins = smooth_cov(inner, px)
    rgb = rgb * (1 - ins[..., None]) + np.array((0.12, 0.12, 0.15)) * ins[..., None]
    for d, col in ((sun, (1.0, 0.78, 0.24)), (m2, (0.45, 0.47, 0.55)), (m1, (0.62, 0.64, 0.72))):
        c = smooth_cov(np.maximum(d, clip), px)
        rgb = rgb * (1 - c[..., None]) + np.array(col) * c[..., None]
    save_rgba(os.path.join(TEX, 'gui', 'placeholder.png'), rgb, a)


def static(c):
    return {'colorType': 'betterquesting:color_static', 'color': '%08X' % c}


def pulse(c1, c2, period=1.6):
    return {'colorType': 'betterquesting:color_pulse', 'period': period, 'phase': 0.0,
            'color1': static(c1), 'color2': static(c2)}


def theme_json():
    textures = {}
    for key, (bounds, pad, mode) in sorted(THEME_PIECES.items()):
        textures['betterquesting:' + key] = {
            'textureType': 'betterquesting:texture_sliced',
            'atlas': 'akashaquestui:textures/gui/theme.png',
            'bounds': bounds, 'padding': pad, 'sliceMode': mode}
    colors = {
        'text_header': static(0xFFFFFFFF), 'text_main': static(0xFFE0E0E6),
        'text_aux_0': static(0xFFA6A6B0), 'text_aux_1': static(0xFF62626C),
        'text_highlight': static(0xFF3FD2E0), 'text_watermark': static(0x40FFFFFF),
        'item_highlight': static(0x40FFFFFF), 'gui_divider': static(0xFF2A2A32),
        'grid_major': static(0x12FFFFFF), 'grid_minor': static(0x09FFFFFF),
        'btn_disabled': static(0xFF62626C), 'btn_idle': static(0xFFEDEDED), 'btn_hover': static(0xFFFFFFFF),
        'quest_line_locked': static(0xFF4A4A52), 'quest_line_unlocked': static(0xFFBDBDC6),
        'quest_line_pending': static(0xFF5BD94F), 'quest_line_complete': static(0xFF5BD94F),
        'quest_line_repeatable': static(0xFFB584F0), 'quest_line_implicit_mixin': static(0xFF46E0FF),
        'quest_icon_locked': static(0xFF5E5E66), 'quest_icon_unlocked': pulse(0xFFE6E6E6, 0xFF8A8A92),
        'quest_icon_pending': pulse(0xFF5BD94F, 0xFF2F7A28), 'quest_icon_complete': static(0xFF5BD94F),
        'quest_icon_repeatable': static(0xFFB584F0),
    }
    theme = [{
        'themeParent': 'betterquesting:dark',
        'themeName': 'Akasha FTB',
        'themeID': 'akashaquestui:ftb_dark',
        'textures': textures,
        'colors': {'betterquesting:' + k: v for k, v in colors.items()},
        'lines': {},
    }]
    with open(os.path.join(ROOT, 'bq_themes.json'), 'w') as f:
        json.dump(theme, f, indent=2)


def main():
    meta = {'version': 1, 'fit': FIT, 'outline': OUTLINE_T, 'glowExtent': 2.0, 'shapes': {}}
    for name, poly in SHAPES.items():
        shape_layers(name, poly)
        hit = simplify(poly, HIT_VERTS)
        meta['shapes'][name] = [[round(x, 5), round(y, 5)] for x, y in hit]
        print('shape %-9s %4d vertices (hit %d)' % (name, len(poly), len(hit)))
    os.makedirs(os.path.join(ROOT, 'shapes'), exist_ok=True)
    with open(os.path.join(ROOT, 'shapes', 'shapes.json'), 'w') as f:
        json.dump(meta, f, separators=(',', ':'))
    badges()
    line_tiles()
    theme_atlas()
    theme_json()
    placeholder()
    print('ok')


if __name__ == '__main__':
    main()
