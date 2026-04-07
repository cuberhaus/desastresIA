const BASE = import.meta.env.VITE_API_URL ?? "";

export interface Centro {
  x: number;
  y: number;
  n_helicopters: number;
}

export interface Grupo {
  x: number;
  y: number;
  priority: number;
  n_personas: number;
}

export interface RouteSegment {
  from_x: number;
  from_y: number;
  to_x: number;
  to_y: number;
  group_id: number;
  trip_number: number;
  pickup_people: number;
  is_return: boolean;
}

export interface SolveResponse {
  centros: Centro[];
  grupos: Grupo[];
  assignment: number[][];
  heuristic_initial: number;
  heuristic_final: number;
  exec_time_ms: number;
  nodes_expanded: number;
  helicopter_times: number[];
  routes: RouteSegment[][];
  trace: number[];
}

export interface SolveRequest {
  seed: number;
  n_grupos: number;
  n_centros: number;
  n_helicopters_per_center: number;
  algorithm: "hc" | "sa";
  successor_fn: number;
  heuristic_fn: number;
  initial_state: "random" | "all_to_one" | "greedy";
  sa_steps: number;
  sa_stiter: number;
  sa_k: number;
  sa_lambda: number;
}

export interface ExperimentConfig {
  label: string;
  algorithm: "hc" | "sa";
  successor_fn: number;
  heuristic_fn: number;
  sa_steps?: number;
  sa_stiter?: number;
  sa_k?: number;
  sa_lambda?: number;
}

export interface ExperimentRequest {
  seed_start: number;
  seed_end: number;
  n_grupos: number;
  n_centros: number;
  n_helicopters_per_center: number;
  initial_state: "random" | "all_to_one" | "greedy";
  configs: ExperimentConfig[];
}

export interface ExperimentRunResult {
  seed: number;
  heuristic_final: number;
  exec_time_ms: number;
  nodes_expanded: number;
}

export interface ExperimentResponse {
  configs: string[];
  results: Record<string, ExperimentRunResult[]>;
}

async function post<T>(url: string, body: unknown): Promise<T> {
  const res = await fetch(`${BASE}${url}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
  });
  if (!res.ok) throw new Error(`${res.status}: ${await res.text()}`);
  return res.json();
}

export interface GenerateRequest {
  seed: number;
  n_grupos: number;
  n_centros: number;
  n_helicopters_per_center: number;
}

export interface GenerateResponse {
  centros: Centro[];
  grupos: Grupo[];
}

export function generatePreview(req: GenerateRequest): Promise<GenerateResponse> {
  return post("/api/generate", req);
}

export function solve(req: SolveRequest): Promise<SolveResponse> {
  return post("/api/solve", req);
}

export function runExperiment(req: ExperimentRequest): Promise<ExperimentResponse> {
  return post("/api/experiment", req);
}
