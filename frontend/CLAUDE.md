# Contexto

Aplicação web para conectar e gerenciar serviços de um grupo de ~400 mulheres
prestadoras de serviços. Projeto de portfólio: cada decisão precisa ser
defensável em entrevista.

**Fase atual:** só as telas de login e cadastro. A primeira reunião de
elencamento de requisitos ainda não aconteceu. Não implemente dashboard,
catálogo de serviços, agendamento ou edição de perfil além do mínimo do
cadastro até que sejam especificados.

# Stack

- React + Vite
- React Router (rotas /login, /cadastro e /conta — essa última é um
  placeholder autenticado mínimo pra fechar o fluxo, não um dashboard)
- Axios para chamadas HTTP
- Tailwind CSS v4 (via `@tailwindcss/vite`, sem `tailwind.config.js`/PostCSS
  manual) — tokens de design (cor, fonte) em `src/index.css` via `@theme`
- Vitest + React Testing Library para testes

# Estrutura

```
src/
├── pages/       Login/, Cadastro/, Conta/ (placeholder pós-login)
├── components/  só o que for reaproveitado entre as duas telas
├── hooks/       hooks customizados
├── services/    instância Axios + chamadas à API
├── context/     sessão (Context API) — não usar Context para outro estado
└── utils/       funções puras (validação, formatação) sem estado nem I/O
```

# Integração com o backend

- Base URL via variável de ambiente (`VITE_API_URL` — prefixo `VITE_`
  obrigatório pro Vite expor a variável no bundle do cliente).
- Endpoints disponíveis hoje: `POST /api/auth/cadastro`, `POST /api/auth/login`.
  Não assuma nenhum outro endpoint.
- Erros do backend vêm em RFC 7807 (`ProblemDetail`): 400 tem um campo
  `errors` (mapa campo→mensagem) pra validação; 401/409/500 têm só `detail`
  genérico. Trate os dois formatos.

# Segurança

- Token JWT fica em `sessionStorage` (não `localStorage`): sobrevive a um
  refresh de página mas some ao fechar a aba — reduz a janela de exposição a
  XSS sem exigir um endpoint de refresh token, que o backend ainda não tem.
- Acesso ao token só pelo módulo de sessão em `context/` — nenhum componente
  lê `sessionStorage` diretamente.
- Interceptor do Axios injeta o header `Authorization: Bearer <token>` e trata
  401 global (desloga e redireciona pro login).

# Fluxo com Claude Code

- Plan Mode antes de tocar em múltiplos arquivos.
- Não implemente o que não foi pedido — se faltar requisito, aponte em vez
  de supor.
- Commits em Conventional Commits (`feat:`, `fix:`, `test:`, `chore:`).
