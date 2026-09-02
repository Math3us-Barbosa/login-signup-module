const TONES = {
  error: 'border-danger/25 bg-danger-bg text-danger',
  success: 'border-success/25 bg-success-bg text-success',
}

export function FormAlert({ tone = 'error', children }) {
  if (!children) return null

  return (
    <div
      role={tone === 'error' ? 'alert' : 'status'}
      className={`rounded-lg border px-4 py-3 text-sm ${TONES[tone]}`}
    >
      {children}
    </div>
  )
}
