import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// En desarrollo, Vite corre en 5173 y reenvia /api al backend de Spring en 8080.
// En produccion, `vite build` genera dist/ que Maven copia dentro del JAR.
export default defineConfig({
  plugins: [react()],
  build: { outDir: 'dist', emptyOutDir: true },
  server: {
    port: 5173,
    proxy: {
      '/api': { target: 'http://localhost:8080', changeOrigin: true }
    }
  }
})
