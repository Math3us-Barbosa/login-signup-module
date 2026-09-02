export function AuthLayout({ eyebrow, title, subtitle, children }) {
  return (
    <div className="flex min-h-screen flex-col bg-cream lg:flex-row">
      {/* <BrandPanel /> — fora por enquanto, ver comentário abaixo */}
      <main className="flex flex-1 flex-col justify-center px-6 py-12 sm:px-10 lg:px-16">
        <div className="mx-auto w-full max-w-sm">
          {eyebrow ? (
            <p className="mb-2 text-sm font-semibold uppercase tracking-wide text-gold-dark">
              {eyebrow}
            </p>
          ) : null}
          <h1 className="font-display text-3xl font-medium text-ink">{title}</h1>
          {subtitle ? <p className="mt-2 text-[15px] text-ink/60">{subtitle}</p> : null}
          <div className="mt-8">{children}</div>
        </div>
      </main>
    </div>
  )
}

// Não usado nas telas por enquanto — mantido pronto pra quando o requisito
// de branding for definido com a cliente.
export function BrandPanel() {
  return (
    <aside className="relative hidden overflow-hidden bg-plum-deep px-12 py-16 text-cream lg:flex lg:w-[42%] lg:flex-col lg:justify-end">
      <LinkPattern />
      <div className="relative z-10 max-w-sm">
       
      </div>
    </aside>
  )
}

// Padrão visual: anéis entrelaçados, representando a rede formada entre as prestadoras.
function LinkPattern() {
  return (
    <svg
      className="pointer-events-none absolute inset-0 h-full w-full"
      viewBox="0 0 400 800"
      fill="none"
      aria-hidden="true"
    >
      <g className="elo-float" stroke="currentColor">
        <circle cx="70" cy="110" r="58" className="text-cream/15" strokeWidth="1.5" />
        <circle cx="138" cy="158" r="58" className="text-gold" strokeWidth="2" opacity="0.55" />
        <circle cx="322" cy="240" r="86" className="text-cream/10" strokeWidth="1" />
        <circle cx="40" cy="430" r="34" className="text-cream/15" strokeWidth="1.5" />
        <circle cx="252" cy="512" r="66" className="text-cream/15" strokeWidth="1.5" />
        <circle cx="318" cy="562" r="66" className="text-gold" strokeWidth="1.5" opacity="0.4" />
        <circle cx="86" cy="656" r="48" className="text-cream/15" strokeWidth="1.5" />
        <circle cx="126" cy="696" r="48" className="text-cream/15" strokeWidth="1.5" />
      </g>
    </svg>
  )
}
