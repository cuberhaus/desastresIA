import { createSignal, Show } from "solid-js";
import SolverPage from "./pages/SolverPage";
import ExperimentPage from "./pages/ExperimentPage";

type Tab = "solver" | "experiments";

export default function App() {
  const [tab, setTab] = createSignal<Tab>("solver");

  return (
    <div class="app">
      <header class="app-header">
        <div class="app-title">
          🚁 <span>DesastresIA</span> Local Search Solver
        </div>
        <nav class="app-tabs">
          <button
            class={`tab-btn ${tab() === "solver" ? "active" : ""}`}
            onClick={() => setTab("solver")}
          >
            Solver
          </button>
          <button
            class={`tab-btn ${tab() === "experiments" ? "active" : ""}`}
            onClick={() => setTab("experiments")}
          >
            Experiments
          </button>
        </nav>
      </header>
      <main class="app-body">
        <Show when={tab() === "solver"}>
          <SolverPage />
        </Show>
        <Show when={tab() === "experiments"}>
          <ExperimentPage />
        </Show>
      </main>
    </div>
  );
}
