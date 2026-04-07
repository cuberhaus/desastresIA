import { createSignal } from "solid-js";
import type { SolveRequest, SolveResponse } from "../lib/api";
import { solve } from "../lib/api";
import Controls from "../components/Controls";
import MapCanvas from "../components/MapCanvas";
import ResultsPanel from "../components/ResultsPanel";

const DEFAULT_CONFIG: SolveRequest = {
  seed: 1000,
  n_grupos: 50,
  n_centros: 5,
  n_helicopters_per_center: 1,
  algorithm: "hc",
  successor_fn: 5,
  heuristic_fn: 1,
  initial_state: "greedy",
  sa_steps: 20000,
  sa_stiter: 5,
  sa_k: 125,
  sa_lambda: 1.0,
};

export default function SolverPage() {
  const [config, setConfig] = createSignal<SolveRequest>({ ...DEFAULT_CONFIG });
  const [result, setResult] = createSignal<SolveResponse | null>(null);
  const [solving, setSolving] = createSignal(false);
  const [error, setError] = createSignal<string | null>(null);

  function updateConfig(partial: Partial<SolveRequest>) {
    setConfig((prev) => ({ ...prev, ...partial }));
  }

  async function handleSolve() {
    setSolving(true);
    setError(null);
    try {
      const res = await solve(config());
      setResult(res);
    } catch (e: any) {
      setError(e.message ?? "Solve failed");
    } finally {
      setSolving(false);
    }
  }

  function handleGenerate() {
    updateConfig({ seed: Math.floor(Math.random() * 100000) });
    setResult(null);
  }

  return (
    <div class="solver-layout">
      <aside class="solver-left">
        <Controls
          config={config()}
          onChange={updateConfig}
          onSolve={handleSolve}
          onGenerate={handleGenerate}
          solving={solving()}
        />
      </aside>
      <section class="solver-center">
        {error() && (
          <div class="error-banner">{error()}</div>
        )}
        <MapCanvas
          centros={result()?.centros ?? []}
          grupos={result()?.grupos ?? []}
          routes={result()?.routes ?? []}
          assignment={result()?.assignment ?? []}
        />
      </section>
      <aside class="solver-right">
        <ResultsPanel result={result()} />
      </aside>
    </div>
  );
}
