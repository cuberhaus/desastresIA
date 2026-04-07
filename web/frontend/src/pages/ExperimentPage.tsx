import { createSignal, For, Show } from "solid-js";
import type { ExperimentConfig, ExperimentRequest, ExperimentResponse } from "../lib/api";
import { runExperiment } from "../lib/api";
import BoxplotChart from "../components/BoxplotChart";

const PRESET_CONFIGS: ExperimentConfig[] = [
  { label: "HC Swap", algorithm: "hc", successor_fn: 1, heuristic_fn: 1 },
  { label: "HC General", algorithm: "hc", successor_fn: 2, heuristic_fn: 1 },
  { label: "HC Reduced", algorithm: "hc", successor_fn: 3, heuristic_fn: 1 },
  { label: "HC Swap+Gen", algorithm: "hc", successor_fn: 4, heuristic_fn: 1 },
  { label: "HC Swap+Red", algorithm: "hc", successor_fn: 5, heuristic_fn: 1 },
  { label: "SA", algorithm: "sa", successor_fn: 6, heuristic_fn: 1, sa_steps: 20000, sa_stiter: 5, sa_k: 125, sa_lambda: 1.0 },
];

export default function ExperimentPage() {
  const [seedStart, setSeedStart] = createSignal(1000);
  const [seedEnd, setSeedEnd] = createSignal(1005);
  const [nGrupos, setNGrupos] = createSignal(50);
  const [nCentros, setNCentros] = createSignal(5);
  const [nHeli, setNHeli] = createSignal(1);
  const [initState, setInitState] = createSignal<"random" | "all_to_one" | "greedy">("greedy");
  const [selected, setSelected] = createSignal<boolean[]>([false, false, true, false, true, true]);
  const [running, setRunning] = createSignal(false);
  const [result, setResult] = createSignal<ExperimentResponse | null>(null);
  const [error, setError] = createSignal<string | null>(null);

  function toggle(i: number) {
    setSelected((prev) => {
      const next = [...prev];
      next[i] = !next[i];
      return next;
    });
  }

  async function handleRun() {
    const configs = PRESET_CONFIGS.filter((_, i) => selected()[i]);
    if (configs.length === 0) return;

    setRunning(true);
    setError(null);
    try {
      const req: ExperimentRequest = {
        seed_start: seedStart(),
        seed_end: seedEnd(),
        n_grupos: nGrupos(),
        n_centros: nCentros(),
        n_helicopters_per_center: nHeli(),
        initial_state: initState(),
        configs,
      };
      const res = await runExperiment(req);
      setResult(res);
    } catch (e: any) {
      setError(e.message ?? "Experiment failed");
    } finally {
      setRunning(false);
    }
  }

  return (
    <div class="experiment-layout">
      <aside class="experiment-sidebar">
        <div class="card-title">Experiment Config</div>

        <div class="controls-grid" style={{ "margin-bottom": "0.75rem" }}>
          <div class="form-group">
            <label class="form-label">Seed Start</label>
            <input class="form-input" type="number" value={seedStart()} onInput={(e) => setSeedStart(+e.currentTarget.value)} />
          </div>
          <div class="form-group">
            <label class="form-label">Seed End</label>
            <input class="form-input" type="number" value={seedEnd()} onInput={(e) => setSeedEnd(+e.currentTarget.value)} />
          </div>
          <div class="form-group">
            <label class="form-label">Groups</label>
            <input class="form-input" type="number" min="2" max="500" value={nGrupos()} onInput={(e) => setNGrupos(+e.currentTarget.value)} />
          </div>
          <div class="form-group">
            <label class="form-label">Centers</label>
            <input class="form-input" type="number" min="1" max="20" value={nCentros()} onInput={(e) => setNCentros(+e.currentTarget.value)} />
          </div>
          <div class="form-group">
            <label class="form-label">Heli/Center</label>
            <input class="form-input" type="number" min="1" max="5" value={nHeli()} onInput={(e) => setNHeli(+e.currentTarget.value)} />
          </div>
          <div class="form-group">
            <label class="form-label">Init State</label>
            <select class="form-select" value={initState()} onChange={(e) => setInitState(e.currentTarget.value as any)}>
              <option value="greedy">Greedy</option>
              <option value="random">Random</option>
              <option value="all_to_one">All to One</option>
            </select>
          </div>
        </div>

        <div class="card-title">Configurations</div>
        <div class="config-list">
          <For each={PRESET_CONFIGS}>
            {(cfg, i) => (
              <label class="config-item">
                <input
                  type="checkbox"
                  checked={selected()[i()]}
                  onChange={() => toggle(i())}
                />
                <span>{cfg.label}</span>
                <span class="config-meta">
                  {cfg.algorithm.toUpperCase()} · SF{cfg.successor_fn} · H{cfg.heuristic_fn}
                </span>
              </label>
            )}
          </For>
        </div>

        <button
          class="btn btn-primary"
          style={{ width: "100%", "margin-top": "0.75rem" }}
          onClick={handleRun}
          disabled={running()}
        >
          {running() ? `Running ${seedEnd() - seedStart()} seeds...` : "Run Experiment"}
        </button>
      </aside>

      <section class="experiment-main">
        {error() && <div class="error-banner">{error()}</div>}

        <Show when={result()} fallback={<ExperimentEmpty />}>
          {(res) => {
            const r = res();
            const heuristicData = r.configs.map((label) => ({
              label,
              values: r.results[label].map((run) => run.heuristic_final),
            }));
            const timeData = r.configs.map((label) => ({
              label,
              values: r.results[label].map((run) => run.exec_time_ms),
            }));
            const nodesData = r.configs.map((label) => ({
              label,
              values: r.results[label].map((run) => run.nodes_expanded),
            }));

            return (
              <div class="experiment-charts">
                <div class="card">
                  <BoxplotChart data={heuristicData} title="Final Heuristic Value" unit="" />
                </div>
                <div class="card">
                  <BoxplotChart data={timeData} title="Execution Time (ms)" unit="ms" />
                </div>
                <div class="card">
                  <BoxplotChart data={nodesData} title="Nodes Expanded" />
                </div>

                <div class="card">
                  <div class="card-title">Summary Table</div>
                  <table class="summary-table">
                    <thead>
                      <tr>
                        <th>Config</th>
                        <th>Median H</th>
                        <th>Mean Time</th>
                        <th>Mean Nodes</th>
                      </tr>
                    </thead>
                    <tbody>
                      <For each={r.configs}>
                        {(label) => {
                          const runs = r.results[label];
                          const hVals = runs.map((r) => r.heuristic_final).sort((a, b) => a - b);
                          const medH = hVals[Math.floor(hVals.length / 2)] ?? 0;
                          const meanT = runs.reduce((s, r) => s + r.exec_time_ms, 0) / runs.length;
                          const meanN = runs.reduce((s, r) => s + r.nodes_expanded, 0) / runs.length;
                          return (
                            <tr>
                              <td>{label}</td>
                              <td>{medH.toFixed(1)}</td>
                              <td>{meanT.toFixed(0)} ms</td>
                              <td>{meanN.toFixed(0)}</td>
                            </tr>
                          );
                        }}
                      </For>
                    </tbody>
                  </table>
                </div>
              </div>
            );
          }}
        </Show>
      </section>
    </div>
  );
}

function ExperimentEmpty() {
  return (
    <div style={{ padding: "4rem 2rem", "text-align": "center", color: "var(--text-muted)" }}>
      <div style={{ "font-size": "2.5rem", "margin-bottom": "0.75rem" }}>📊</div>
      <div style={{ "font-size": "0.9rem", "max-width": "400px", margin: "0 auto" }}>
        Select algorithm configurations and a seed range, then click <strong>Run Experiment</strong> to
        compare local search strategies across multiple seeds.
      </div>
    </div>
  );
}
