# Contexto

Aplicação web para conectar e gerenciar serviços de um grupo de ~400 mulheres prestadoras de serviços.

**Fase atual:** apenas autenticação (cadastro e login). Nenhum outro requisito foi levantado. Não implemente agendamento, catálogo ou pagamento até que sejam especificados.

Projeto de portfólio para vagas de Engenharia de Software: cada decisão de arquitetura precisa ser defensável em entrevista.

# Stack

- **Backend:** Java 21, Spring Boot 4.1.x, Maven, Spring Security (JWT), Spring Data JPA, Bean Validation, Lombok
- **Banco:** PostgreSQL 16, migrations com Flyway
- **Testes:** JUnit 5, AssertJ, Mockito, Testcontainers
- **Local:** Docker Compose

Spring Boot 4 é uma major recente e a maioria dos tutoriais ainda mira 3.x. Siga a documentação do Boot 4, não padrões de 3.x.

# Arquitetura Backend

Arquitetura **em camadas** (package by layer). Não é Clean Architecture e não deve ser descrita como tal.

```
com.projeto.app
├── controller/    UsuarioController, AuthController
├── service/       UsuarioService, AuthService, JwtService
├── repository/    UsuarioRepository
├── entity/        Usuario (@Entity)
├── dto/           request/ e response/
├── config/        SecurityConfig
└── exception/     GlobalExceptionHandler + exceções de negócio
```

Prefixe as classes com o domínio (`UsuarioController`, `UsuarioService`) para que a busca por nome encontre o fluxo inteiro apesar dos pacotes separados.

## Regras

**1. Entity não cruza a borda HTTP.** O `Controller` nunca importa, recebe ou retorna classe `@Entity`. DTOs de resposta podem referenciar entities dentro de fábricas estáticas; a dependência aponta sempre DTO → Entity, nunca o contrário.

**2. Conversão dentro do Service.** Toda conversão `Entity ↔ DTO` acontece no `Service`, dentro da transação — fora dela, relacionamento lazy causa `LazyInitializationException`. O Service recebe DTO e devolve DTO.

**3. DTOs são `record`.** Responses têm fábrica estática `fromEntity(Usuario)`, sem reflexão nem `BeanUtils`. Requests **não** têm `toEntity()`: construir entity envolve regra de negócio (hash de senha, defaults, relacionamentos) e é responsabilidade do Service.

**4. Camadas.**
- `Controller`: rotas, `@Valid`, `ResponseEntity` com status adequado. Sem regra de negócio, sem `try/catch`.
- `Service`: regra de negócio, `@Transactional` (`readOnly = true` em leitura), conversão.
- `Repository`: interface Spring Data JPA, só persistência e consultas.

**5. Injeção por construtor.** `@RequiredArgsConstructor` com campos `private final`. Nunca `@Autowired` em campo.

**6. Lombok em entities.** Só `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`. Proibido `@Data`, `@EqualsAndHashCode` e `@ToString`: quebram com proxies do Hibernate, IDs nulos antes do persist e relacionamentos bidirecionais.

**7. Migrations.** Flyway em `src/main/resources/db/migration`, padrão `V1__descricao.sql`. `ddl-auto=validate`, nunca `update`. Alteração de entity vem com a migration na mesma tarefa. Migration aplicada nunca é editada — crie outra.

**8. Erros.** `@RestControllerAdvice` em `exception/GlobalExceptionHandler`, respostas em RFC 7807 via `ProblemDetail`. Não invente formato próprio. Validação retorna 400 com os campos inválidos. Stack trace nunca vai na resposta.

**9. Paginação.** Listagens retornam `Page<T>` com `Pageable`. Nunca `List<T>` sem limite.

# Segurança e Dados Pessoais

A aplicação guarda dados de contato de mulheres prestadoras de serviço. Isso é requisito, não detalhe.

**Autorização**
- Toda rota nova nasce autenticada. Só `/api/auth/**` é pública; qualquer exceção precisa ser justificada.
- Config via `SecurityFilterChain` como `@Bean`. Não use `WebSecurityConfigurerAdapter` (removido).
- API stateless: `SessionCreationPolicy.STATELESS`, CSRF desabilitado — seguro apenas porque não há cookie de sessão. Se migrar para cookie, CSRF volta.
- Nunca use `permitAll()` como atalho para resolver 401 em desenvolvimento.
- Regras de acesso ficam no `SecurityConfig`, ou em `@PreAuthorize` no service quando dependerem do recurso ("só a dona edita o próprio cadastro"). Nunca como `if` no controller.

**Dados**
- Senha com BCrypt. Campo `senhaHash`, coluna `senha_hash`.
- Nenhum DTO de resposta expõe senha ou hash.
- Email, telefone e nome nunca aparecem em log ou mensagem de exceção.
- Segredo do JWT vem de variável de ambiente. Nunca commitado, nem como default no `application.yml`.
- Minimização: só colete dado com requisito explícito. Não adicione CPF, endereço ou data de nascimento por conta própria.
- Email normalizado em minúsculas, com `UNIQUE` no banco.
- Falha de login retorna mensagem genérica, sem revelar se o email existe.

# Testes

Toda funcionalidade nasce com teste.

- **Service:** JUnit 5 + Mockito com repository mockado. Cobre regra de negócio e caminhos de erro.
- **Controller:** `@WebMvcTest` + MockMvc com service mockado. Cobre status, contrato JSON e validação.
- **Integração:** `@SpringBootTest` + Testcontainers com PostgreSQL real. Nunca H2 — diverge do Postgres e mascara bugs.
- Asserções com AssertJ. Nomes descrevem comportamento: `deveRetornar409QuandoEmailJaCadastrado`.

# Fluxo com Claude Code

- **Plan Mode** antes de gerar funcionalidade que toque múltiplos arquivos.
- Após alterar código Java: `./mvnw -q test`. Use `verify` só antes de commit; evite `clean` no ciclo normal.
- Não implemente o que não foi pedido. Se faltar algo, aponte em vez de codificar.
- Commits em Conventional Commits (`feat:`, `fix:`, `test:`, `chore:`).