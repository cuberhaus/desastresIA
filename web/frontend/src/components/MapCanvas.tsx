import { createEffect, createSignal, onCleanup, onMount } from "solid-js";
import type { Centro, Grupo, RouteSegment } from "../lib/api";

const HELI_COLORS = [
  "#06b6d4", "#d946ef", "#84cc16", "#f97316", "#ec4899",
  "#14b8a6", "#a855f7", "#eab308", "#f43f5e", "#22d3ee",
];

const PADDING = 40;
const COORD_MAX = 100;

interface Props {
  centros: Centro[];
  grupos: Grupo[];
  routes: RouteSegment[][];
  assignment: number[][];
}

export default function MapCanvas(props: Props) {
  let canvasRef!: HTMLCanvasElement;
  let containerRef!: HTMLDivElement;
  const [size, setSize] = createSignal({ w: 600, h: 600 });
  const [tooltip, setTooltip] = createSignal<{ x: number; y: number; text: string } | null>(null);

  function scale(coord: number, dim: number): number {
    return PADDING + (coord / COORD_MAX) * (dim - 2 * PADDING);
  }

  function draw() {
    const canvas = canvasRef;
    if (!canvas) return;
    const ctx = canvas.getContext("2d");
    if (!ctx) return;

    const { w, h } = size();
    const dpr = window.devicePixelRatio || 1;
    canvas.width = Math.round(w * dpr);
    canvas.height = Math.round(h * dpr);
    ctx.setTransform(dpr, 0, 0, dpr, 0, 0);

    ctx.fillStyle = "#0a0a15";
    ctx.fillRect(0, 0, w, h);

    // Grid
    ctx.strokeStyle = "#1a1a2e";
    ctx.lineWidth = 0.5;
    for (let i = 0; i <= 10; i++) {
      const gx = scale(i * 10, w);
      const gy = scale(i * 10, h);
      ctx.beginPath();
      ctx.moveTo(gx, PADDING);
      ctx.lineTo(gx, h - PADDING);
      ctx.stroke();
      ctx.beginPath();
      ctx.moveTo(PADDING, gy);
      ctx.lineTo(w - PADDING, gy);
      ctx.stroke();
    }

    // Routes
    const routes = props.routes;
    if (routes.length > 0) {
      for (let hi = 0; hi < routes.length; hi++) {
        const color = HELI_COLORS[hi % HELI_COLORS.length];
        const segs = routes[hi];
        for (const seg of segs) {
          const x1 = scale(seg.from_x, w);
          const y1 = scale(seg.from_y, h);
          const x2 = scale(seg.to_x, w);
          const y2 = scale(seg.to_y, h);

          ctx.strokeStyle = seg.is_return ? color + "40" : color + "90";
          ctx.lineWidth = seg.is_return ? 1 : 1.8;
          if (seg.is_return) ctx.setLineDash([4, 4]);
          else ctx.setLineDash([]);

          ctx.beginPath();
          ctx.moveTo(x1, y1);
          ctx.lineTo(x2, y2);
          ctx.stroke();

          // Arrow
          if (!seg.is_return) {
            const angle = Math.atan2(y2 - y1, x2 - x1);
            const mx = (x1 + x2) / 2;
            const my = (y1 + y2) / 2;
            ctx.fillStyle = color;
            ctx.beginPath();
            ctx.moveTo(mx + 5 * Math.cos(angle), my + 5 * Math.sin(angle));
            ctx.lineTo(
              mx - 4 * Math.cos(angle) + 3 * Math.sin(angle),
              my - 4 * Math.sin(angle) - 3 * Math.cos(angle)
            );
            ctx.lineTo(
              mx - 4 * Math.cos(angle) - 3 * Math.sin(angle),
              my - 4 * Math.sin(angle) + 3 * Math.cos(angle)
            );
            ctx.fill();
          }
        }
      }
      ctx.setLineDash([]);
    }

    // Groups
    for (let i = 0; i < props.grupos.length; i++) {
      const g = props.grupos[i];
      const gx = scale(g.x, w);
      const gy = scale(g.y, h);
      const r = Math.max(3, Math.min(8, g.n_personas / 2));

      let heliId = -1;
      for (let hi = 0; hi < props.assignment.length; hi++) {
        if (props.assignment[hi].includes(i)) {
          heliId = hi;
          break;
        }
      }

      if (heliId >= 0 && routes.length > 0) {
        ctx.fillStyle = HELI_COLORS[heliId % HELI_COLORS.length] + "cc";
      } else {
        ctx.fillStyle = g.priority === 1 ? "#ef4444cc" : "#f59e0bcc";
      }

      ctx.beginPath();
      ctx.arc(gx, gy, r, 0, Math.PI * 2);
      ctx.fill();

      if (g.priority === 1) {
        ctx.strokeStyle = "#ef4444";
        ctx.lineWidth = 1.5;
        ctx.beginPath();
        ctx.arc(gx, gy, r + 2, 0, Math.PI * 2);
        ctx.stroke();
      }
    }

    // Centres
    for (const c of props.centros) {
      const cx = scale(c.x, w);
      const cy = scale(c.y, h);
      const s = 10;

      ctx.fillStyle = "#3b82f6";
      ctx.fillRect(cx - s / 2, cy - s / 2, s, s);
      ctx.strokeStyle = "#60a5fa";
      ctx.lineWidth = 1.5;
      ctx.strokeRect(cx - s / 2, cy - s / 2, s, s);

      ctx.fillStyle = "#fff";
      ctx.font = "bold 9px sans-serif";
      ctx.textAlign = "center";
      ctx.textBaseline = "middle";
      ctx.fillText(`${c.n_helicopters}`, cx, cy);
    }

    // Legend
    const ly = h - 14;
    ctx.font = "11px sans-serif";
    ctx.textAlign = "left";

    ctx.fillStyle = "#3b82f6";
    ctx.fillRect(PADDING, ly - 5, 10, 10);
    ctx.fillStyle = "#94a3b8";
    ctx.fillText("Center", PADDING + 14, ly + 2);

    ctx.fillStyle = "#ef4444";
    ctx.beginPath();
    ctx.arc(PADDING + 80, ly, 4, 0, Math.PI * 2);
    ctx.fill();
    ctx.fillStyle = "#94a3b8";
    ctx.fillText("Priority", PADDING + 88, ly + 2);

    ctx.fillStyle = "#f59e0b";
    ctx.beginPath();
    ctx.arc(PADDING + 150, ly, 4, 0, Math.PI * 2);
    ctx.fill();
    ctx.fillStyle = "#94a3b8";
    ctx.fillText("Normal", PADDING + 158, ly + 2);
  }

  function handleMouseMove(e: MouseEvent) {
    const rect = canvasRef.getBoundingClientRect();
    const mx = e.clientX - rect.left;
    const my = e.clientY - rect.top;
    const { w, h } = size();

    for (let i = 0; i < props.grupos.length; i++) {
      const g = props.grupos[i];
      const gx = scale(g.x, w);
      const gy = scale(g.y, h);
      const dist = Math.sqrt((mx - gx) ** 2 + (my - gy) ** 2);
      if (dist < 12) {
        setTooltip({
          x: e.clientX,
          y: e.clientY,
          text: `Group ${i}: ${g.n_personas} people${g.priority === 1 ? " (PRIORITY)" : ""}`,
        });
        return;
      }
    }
    setTooltip(null);
  }

  onMount(() => {
    const ro = new ResizeObserver(entries => {
      for (const entry of entries) {
        const r = entry.contentRect;
        setSize({ w: r.width, h: r.height });
      }
    });
    ro.observe(containerRef);
    onCleanup(() => ro.disconnect());
  });

  createEffect(() => {
    props.centros;
    props.grupos;
    props.routes;
    props.assignment;
    size();
    draw();
  });

  return (
    <div
      ref={containerRef!}
      style={{ position: "relative", width: "100%", height: "100%", "min-height": "400px" }}
    >
      <canvas
        ref={canvasRef!}
        onMouseMove={handleMouseMove}
        onMouseLeave={() => setTooltip(null)}
        style={{ width: "100%", height: "100%", display: "block" }}
      />
      {tooltip() && (
        <div
          style={{
            position: "fixed",
            left: `${tooltip()!.x + 12}px`,
            top: `${tooltip()!.y - 8}px`,
            background: "var(--bg-card)",
            border: "1px solid var(--border)",
            "border-radius": "var(--radius-sm)",
            padding: "0.3rem 0.5rem",
            "font-size": "0.75rem",
            color: "var(--text-primary)",
            "pointer-events": "none",
            "z-index": "100",
            "white-space": "nowrap",
          }}
        >
          {tooltip()!.text}
        </div>
      )}
    </div>
  );
}
