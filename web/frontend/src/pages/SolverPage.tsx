import { createEffect, createSignal, on, onCleanup, onMount } from "solid-js";
import type { Centro, Grupo, SolveRequest, SolveResponse } from "../lib/api";
import { generatePreview, solve } from "../lib/api";
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

function useDragHandle(initial: number, min: number, max: number, side: "left" | "right") {
  const [width, setWidth] = createSignal(initial);
  const [dragging, setDragging] = createSignal(false);

  function onPointerDown(e: PointerEvent) {
    e.preventDefault();
    setDragging(true);
    const startX = e.clientX;
    const startW = width();

    function onMove(ev: PointerEvent) {
      const delta = side === "left" ? ev.clientX - startX : startX - ev.clientX;
      setWidth(Math.min(max, Math.max(min, startW + delta)));
    }
    function onUp() {
      setDragging(false);
      window.removeEventListener("pointermove", onMove);
      window.removeEventListener("pointerup", onUp);
    }
    window.addEventListener("pointermove", onMove);
    window.addEventListener("pointerup", onUp);
  }

  return { width, dragging, onPointerDown };
}

export default function SolverPage() {
  const [config, setConfig] = createSignal<SolveRequest>({ ...DEFAULT_CONFIG });
  const [result, setResult] = createSignal<SolveResponse | null>(null);
  const [preview, setPreview] = createSignal<{ centros: Centro[]; grupos: Grupo[] } | null>(null);
  const [solving, setSolving] = createSignal(false);
  const [error, setError] = createSignal<string | null>(null);

  const left = useDragHandle(300, 200, 500, "left");
  const right = useDragHandle(300, 200, 500, "right");

  const previewKey = () => {
    const c = config();
    return `${c.seed}-${c.n_grupos}-${c.n_centros}-${c.n_helicopters_per_center}`;
  };

  createEffect(
    on(previewKey, () => {
      setResult(null);
      const c = config();
      generatePreview({
        seed: c.seed,
        n_grupos: c.n_grupos,
        n_centros: c.n_centros,
        n_helicopters_per_center: c.n_helicopters_per_center,
      })
        .then(setPreview)
        .catch(() => {});
    })
  );

  const mapCentros = () => result()?.centros ?? preview()?.centros ?? [];
  const mapGrupos = () => result()?.grupos ?? preview()?.grupos ?? [];
  const mapRoutes = () => result()?.routes ?? [];
  const mapAssignment = () => result()?.assignment ?? [];

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
  }

  const isDragging = () => left.dragging() || right.dragging();

  return (
    <div
      class="solver-layout"
      classList={{ "is-dragging": isDragging() }}
      style={{
        "grid-template-columns": `${left.width()}px 4px 1fr 4px ${right.width()}px`,
      }}
    >
      <aside class="solver-left">
        <Controls
          config={config()}
          onChange={updateConfig}
          onSolve={handleSolve}
          onGenerate={handleGenerate}
          solving={solving()}
        />
      </aside>

      <div
        class="drag-handle"
        classList={{ active: left.dragging() }}
        onPointerDown={left.onPointerDown}
      />

      <section class="solver-center">
        {error() && <div class="error-banner">{error()}</div>}
        <MapCanvas
          centros={mapCentros()}
          grupos={mapGrupos()}
          routes={mapRoutes()}
          assignment={mapAssignment()}
        />
        {!result() && !solving() && <div class="preview-badge">Preview</div>}
        {solving() && <div class="preview-badge solving">Solving...</div>}
      </section>

      <div
        class="drag-handle"
        classList={{ active: right.dragging() }}
        onPointerDown={right.onPointerDown}
      />

      <aside class="solver-right">
        <ResultsPanel result={result()} />
      </aside>
    </div>
  );
}
