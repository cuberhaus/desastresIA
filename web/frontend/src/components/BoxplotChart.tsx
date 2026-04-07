import { createMemo } from "solid-js";

interface BoxplotData {
  label: string;
  values: number[];
}

interface Props {
  data: BoxplotData[];
  title: string;
  unit?: string;
}

function quantile(sorted: number[], q: number): number {
  const pos = (sorted.length - 1) * q;
  const lo = Math.floor(pos);
  const hi = Math.ceil(pos);
  if (lo === hi) return sorted[lo];
  return sorted[lo] + (pos - lo) * (sorted[hi] - sorted[lo]);
}

const COLORS = [
  "#06b6d4", "#d946ef", "#84cc16", "#f97316", "#ec4899",
  "#14b8a6", "#a855f7", "#eab308",
];

export default function BoxplotChart(props: Props) {
  const W = 500;
  const H = 200;
  const PAD_L = 10;
  const PAD_R = 10;
  const PAD_T = 24;
  const PAD_B = 40;

  const stats = createMemo(() =>
    props.data.map((d) => {
      const s = [...d.values].sort((a, b) => a - b);
      if (s.length === 0) return { label: d.label, min: 0, q1: 0, median: 0, q3: 0, max: 0 };
      return {
        label: d.label,
        min: s[0],
        q1: quantile(s, 0.25),
        median: quantile(s, 0.5),
        q3: quantile(s, 0.75),
        max: s[s.length - 1],
      };
    })
  );

  const yRange = createMemo(() => {
    const all = stats();
    if (all.length === 0) return { lo: 0, hi: 1 };
    let lo = Infinity, hi = -Infinity;
    for (const s of all) {
      if (s.min < lo) lo = s.min;
      if (s.max > hi) hi = s.max;
    }
    const pad = (hi - lo) * 0.1 || 1;
    return { lo: lo - pad, hi: hi + pad };
  });

  function yToSvg(v: number): number {
    const { lo, hi } = yRange();
    return PAD_T + (1 - (v - lo) / (hi - lo)) * (H - PAD_T - PAD_B);
  }

  return (
    <div>
      <div style={{ "font-size": "0.72rem", "font-weight": "700", color: "var(--text-muted)", "margin-bottom": "0.5rem", "text-transform": "uppercase", "letter-spacing": "0.04em" }}>
        {props.title}
      </div>
      <svg viewBox={`0 0 ${W} ${H}`} style={{ width: "100%", height: `${H}px`, display: "block" }}>
        {/* Y axis ticks */}
        {[0, 0.25, 0.5, 0.75, 1].map((f) => {
          const { lo, hi } = yRange();
          const v = lo + f * (hi - lo);
          const y = yToSvg(v);
          return (
            <>
              <line x1={PAD_L} y1={y} x2={W - PAD_R} y2={y} stroke="#1a1a2e" stroke-width="0.5" />
              <text x={W - PAD_R + 2} y={y + 3} font-size="8" fill="#64748b" text-anchor="start">
                {v.toFixed(0)}
              </text>
            </>
          );
        })}

        {stats().map((s, i) => {
          const n = stats().length;
          const boxW = Math.min(50, (W - PAD_L - PAD_R) / n * 0.6);
          const cx = PAD_L + (i + 0.5) * ((W - PAD_L - PAD_R) / n);
          const color = COLORS[i % COLORS.length];

          const yMin = yToSvg(s.min);
          const yQ1 = yToSvg(s.q1);
          const yMed = yToSvg(s.median);
          const yQ3 = yToSvg(s.q3);
          const yMax = yToSvg(s.max);

          return (
            <g>
              {/* Whisker */}
              <line x1={cx} y1={yMin} x2={cx} y2={yQ1} stroke={color} stroke-width="1" />
              <line x1={cx} y1={yQ3} x2={cx} y2={yMax} stroke={color} stroke-width="1" />
              <line x1={cx - boxW / 4} y1={yMin} x2={cx + boxW / 4} y2={yMin} stroke={color} stroke-width="1.5" />
              <line x1={cx - boxW / 4} y1={yMax} x2={cx + boxW / 4} y2={yMax} stroke={color} stroke-width="1.5" />

              {/* Box */}
              <rect
                x={cx - boxW / 2}
                y={Math.min(yQ1, yQ3)}
                width={boxW}
                height={Math.abs(yQ3 - yQ1)}
                fill={color + "30"}
                stroke={color}
                stroke-width="1.5"
                rx="2"
              />

              {/* Median */}
              <line x1={cx - boxW / 2} y1={yMed} x2={cx + boxW / 2} y2={yMed} stroke={color} stroke-width="2" />

              {/* Label */}
              <text
                x={cx}
                y={H - PAD_B + 14}
                font-size="9"
                fill="#94a3b8"
                text-anchor="middle"
                font-weight="600"
              >
                {s.label.length > 12 ? s.label.slice(0, 12) + "…" : s.label}
              </text>
            </g>
          );
        })}
      </svg>
    </div>
  );
}
