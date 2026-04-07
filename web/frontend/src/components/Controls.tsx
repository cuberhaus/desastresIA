import { Show } from "solid-js";
import type { SolveRequest } from "../lib/api";

const SUCCESSOR_OPTIONS = [
  { value: 1, label: "1 — Swap" },
  { value: 2, label: "2 — Reassign General" },
  { value: 3, label: "3 — Reassign Reduced" },
  { value: 4, label: "4 — Swap + General" },
  { value: 5, label: "5 — Swap + Reduced" },
];

const HEURISTIC_OPTIONS = [
  { value: 1, label: "1 — Weighted Total" },
  { value: 2, label: "2 — Plain Sum" },
  { value: 3, label: "3 — Weighted + Priority" },
];

const INIT_STATE_OPTIONS = [
  { value: "greedy" as const, label: "Greedy" },
  { value: "random" as const, label: "Random" },
  { value: "all_to_one" as const, label: "All to One" },
];

interface Props {
  config: SolveRequest;
  onChange: (partial: Partial<SolveRequest>) => void;
  onSolve: () => void;
  onGenerate: () => void;
  solving: boolean;
}

export default function Controls(props: Props) {
  const c = () => props.config;

  return (
    <div class="controls-panel">
      <div class="card-title">Problem</div>
      <div class="controls-grid">
        <div class="form-group">
          <label class="form-label">Seed</label>
          <input
            class="form-input"
            type="number"
            value={c().seed}
            onInput={(e) => props.onChange({ seed: +e.currentTarget.value })}
          />
        </div>
        <div class="form-group">
          <label class="form-label">Groups</label>
          <input
            class="form-input"
            type="number"
            min="2"
            max="500"
            value={c().n_grupos}
            onInput={(e) => props.onChange({ n_grupos: +e.currentTarget.value })}
          />
        </div>
        <div class="form-group">
          <label class="form-label">Centers</label>
          <input
            class="form-input"
            type="number"
            min="1"
            max="20"
            value={c().n_centros}
            onInput={(e) => props.onChange({ n_centros: +e.currentTarget.value })}
          />
        </div>
        <div class="form-group">
          <label class="form-label">Heli/Center</label>
          <input
            class="form-input"
            type="number"
            min="1"
            max="5"
            value={c().n_helicopters_per_center}
            onInput={(e) => props.onChange({ n_helicopters_per_center: +e.currentTarget.value })}
          />
        </div>
      </div>

      <div class="card-title" style={{ "margin-top": "1rem" }}>Algorithm</div>
      <div class="controls-grid">
        <div class="form-group">
          <label class="form-label">Type</label>
          <select
            class="form-select"
            value={c().algorithm}
            onChange={(e) => props.onChange({
              algorithm: e.currentTarget.value as "hc" | "sa",
              successor_fn: e.currentTarget.value === "sa" ? 6 : c().successor_fn,
            })}
          >
            <option value="hc">Hill Climbing</option>
            <option value="sa">Simulated Annealing</option>
          </select>
        </div>
        <Show when={c().algorithm === "hc"}>
          <div class="form-group">
            <label class="form-label">Successor Fn</label>
            <select
              class="form-select"
              value={c().successor_fn}
              onChange={(e) => props.onChange({ successor_fn: +e.currentTarget.value })}
            >
              {SUCCESSOR_OPTIONS.map((o) => (
                <option value={o.value}>{o.label}</option>
              ))}
            </select>
          </div>
        </Show>
        <div class="form-group">
          <label class="form-label">Heuristic</label>
          <select
            class="form-select"
            value={c().heuristic_fn}
            onChange={(e) => props.onChange({ heuristic_fn: +e.currentTarget.value })}
          >
            {HEURISTIC_OPTIONS.map((o) => (
              <option value={o.value}>{o.label}</option>
            ))}
          </select>
        </div>
        <div class="form-group">
          <label class="form-label">Initial State</label>
          <select
            class="form-select"
            value={c().initial_state}
            onChange={(e) =>
              props.onChange({ initial_state: e.currentTarget.value as "random" | "all_to_one" | "greedy" })
            }
          >
            {INIT_STATE_OPTIONS.map((o) => (
              <option value={o.value}>{o.label}</option>
            ))}
          </select>
        </div>
      </div>

      <Show when={c().algorithm === "sa"}>
        <div class="card-title" style={{ "margin-top": "1rem" }}>SA Parameters</div>
        <div class="controls-grid">
          <div class="form-group">
            <label class="form-label">Steps</label>
            <input
              class="form-input"
              type="number"
              value={c().sa_steps}
              onInput={(e) => props.onChange({ sa_steps: +e.currentTarget.value })}
            />
          </div>
          <div class="form-group">
            <label class="form-label">Stiter</label>
            <input
              class="form-input"
              type="number"
              value={c().sa_stiter}
              onInput={(e) => props.onChange({ sa_stiter: +e.currentTarget.value })}
            />
          </div>
          <div class="form-group">
            <label class="form-label">K</label>
            <input
              class="form-input"
              type="number"
              value={c().sa_k}
              onInput={(e) => props.onChange({ sa_k: +e.currentTarget.value })}
            />
          </div>
          <div class="form-group">
            <label class="form-label">Lambda</label>
            <input
              class="form-input"
              type="number"
              step="0.1"
              value={c().sa_lambda}
              onInput={(e) => props.onChange({ sa_lambda: +e.currentTarget.value })}
            />
          </div>
        </div>
      </Show>

      <div style={{ display: "flex", gap: "0.5rem", "margin-top": "1rem" }}>
        <button class="btn btn-primary" style={{ flex: 1 }} onClick={props.onSolve} disabled={props.solving}>
          {props.solving ? "Solving..." : "Solve"}
        </button>
        <button class="btn btn-secondary" onClick={props.onGenerate} disabled={props.solving}>
          Randomize
        </button>
      </div>
    </div>
  );
}
