import {defineConfig, loadEnv} from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig(({mode}) => {
  const env = loadEnv(mode, process.cwd(), "");

  return {
    server: { /* only applies to the DEVELOPMENT vite server */
      proxy: {
        "/api": {
          target: "http://localhost:8080",
          changeOrigin: true
        },
        "/oauth2": {
          target: "http://localhost:8080",
          changeOrigin: true
        }
      },
      port: parseInt(env.VITE_PORT, 10) || 3000
    },
    plugins: [react()]
  };
});
