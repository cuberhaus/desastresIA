import { defineConfig } from "vite";
import solidPlugin from "vite-plugin-solid";

export default defineConfig({
  plugins: [solidPlugin()],
  server: { port: 3000, proxy: { "/api": "http://localhost:8083" } },
  build: { target: "esnext" },
});
