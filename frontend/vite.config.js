import {defineConfig, loadEnv} from "vite";
import react from "@vitejs/plugin-react";

// https://vitejs.dev/config/
export default defineConfig(({mode}) => {
  // eslint-disable-next-line no-undef
  const env = loadEnv(mode, process.cwd(), "");
  return {
    define: {
      "process.env": env
    },
    server: {
      proxy: {
        "/api": {
          target: "http://backend:8080",
          changeOrigin: true
        }
      },
      port: parseInt(env.VITE_PORT, 10) || 3000
    },
    plugins: [react()],
    optimizeDeps: {
      exclude: ["vite-sample"],
      esbuildOptions: {
        jsx: "automatic"
      }
    }
  };
});
