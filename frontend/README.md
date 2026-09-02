# frontend

React + Vite. Convenções e arquitetura do projeto em [`CLAUDE.md`](CLAUDE.md).

## Rodando localmente

```bash
npm install
cp .env.example .env   # ajuste VITE_API_URL se o backend não estiver em localhost:8080
npm run dev
```

## Scripts

- `npm run dev` — servidor de desenvolvimento
- `npm run build` — build de produção
- `npm run test` — testes (Vitest + React Testing Library)
- `npm run lint` — Oxlint
