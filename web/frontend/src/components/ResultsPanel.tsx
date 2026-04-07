import { For, Show } from "solid-js";
import type { SolveResponse } from "../lib/api";

const HELI_COLORS = [
  "#06b6d4", "#d946ef", "#84cc16", "#f97316", "#ec4899",
  "#14b8a6", "#a855f7", "#eab308", "#f43f5e", "#22d3ee",
];

interface Props {
  result: SolveResponse | null;
}

export default function ResultsPanel(props: Props) {
  return (
    <div class="results-panel">
      <Show when={props.result} fallback={<EmptyState />}>
        {(res) => {
          const r = res();
          const improvement = r.heuristic_initial > 0
            ? ((1 - r.heuristic_final / r.heuristic_initial) * 100)
            : 0;

          return (
            <>
              <div class="card-title">Results</div>
              <div class="stats-grid">
                <div class="stat-item">
                  <div class="stat-value">{r.heuristic_final.toFixed(1)}</div>
                  <div class="stat-label">Final Heuristic</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{r.exec_time_ms.toFixed(0)}<span class="stat-unit">ms</span></div>
                  <div class="stat-label">Execution Time</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value">{r.nodes_expanded.toLocaleString()}</div>
                  <div class="stat-label">Nodes Expanded</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value" style={{ color: improvement > 0 ? "var(--success)" : "var(--text-primary)" }}>
                    {improvement > 0 ? "-" : ""}{Math.abs(improvement).toFixed(1)}%
                  </div>
                  <div class="stat-label">Improvement</div>
                </div>
              </div>

              <div class="card-title" style={{ "margin-top": "1rem" }}>Initial vs Final</div>
              <div class="compare-row">
                <span style={{ color: "var(--text-muted)" }}>{r.heuristic_initial.toFixed(1)}</span>
                <span style={{ color: "var(--text-muted)" }}> → </span>
                <span style={{ color: "var(--success)", "font-weight": "700" }}>{r.heuristic_final.toFixed(1)}</span>
              </div>

              <div class="card-title" style={{ "margin-top": "1rem" }}>Helicopters</div>
              <div class="heli-table">
                <For each={r.helicopter_times}>
                  {(time, i) => {
                    const groupCount = r.assignment[i()]?.length ?? 0;
                    const tripCount = r.routes[i()]
                      ? Math.max(0, ...r.routes[i()].map(s => s.trip_number)) + 1
                      : 0;
                    return (
                      <div class="heli-row">
                        <span
                          class="heli-dot"
                          style={{ background: HELI_COLORS[i() % HELI_COLORS.length] }}
                        />
                        <span class="heli-id">H{i()}</span>
                        <span class="heli-stat">{time.toFixed(1)} min</span>
                        <span class="heli-stat">{groupCount} grp</span>
                        <span class="heli-stat">{tripCount} trips</span>
                      </div>
                    );
                  }}
                </For>
              </div>

              <Show when={r.trace.length > 0}>
                <div class="card-title" style={{ "margin-top": "1rem" }}>SA Convergence</div>
                <TraceChart trace={r.trace} />
              </Show>
            </>
          );
        }}
      </Show>
    </div>
  );
}

function EmptyState() {
  return (
    <div style={{ padding: "2rem", "text-align": "center", color: "var(--text-muted)" }}>
      <div style={{ "font-size": "2rem", "margin-bottom": "0.5rem" }}>🚁</div>
      <div style={{ "font-size": "0.85rem" }}>
        Configure parameters and click <strong>Solve</strong> to run the local search algorithm
      </div>
    </div>
  );
}

function TraceChart(props: { trace: number[] }) {
  const W = 260;
  const H = 80;
  const PAD = 4;

  const points = () => {
    const t = props.trace;
    if (t.length < 2) return "";
    const minV = Math.min(...t);
    const maxV = Math.max(...t);
    const range = maxV - minV || 1;
    return t
      .map((v, i) => {
        const x = PAD + (i / (t.length - 1)) * (W - 2 * PAD);
        const y = PAD + (1 - (v - minV) / range) * (H - 2 * PAD);
        return `${i === 0 ? "M" : "L"}${x},${y}`;
      })
      .join(" ");
  };

  return (
    <svg viewBox={`0 0 ${W} ${H}`} style={{ width: "100%", height: "80px" }}>
      <path d={points()} fill="none" stroke="var(--accent)" stroke-width="1.5" />
    </svg>
  );
}
