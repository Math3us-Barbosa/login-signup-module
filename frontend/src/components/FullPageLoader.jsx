export function FullPageLoader() {
  return (
    <div className="flex min-h-screen items-center justify-center bg-cream">
      <div className="flex items-center gap-3 text-ink/60">
        <span className="h-5 w-5 animate-spin rounded-full border-2 border-plum/25 border-t-plum" />
        <span className="text-sm">Carregando…</span>
      </div>
    </div>
  )
}
