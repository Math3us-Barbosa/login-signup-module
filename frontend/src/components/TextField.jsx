import { forwardRef, useId } from 'react'

export const TextField = forwardRef(function TextField(
  { label, error, helperText, endAdornment, className = '', ...props },
  ref,
) {
  const id = useId()
  const errorId = `${id}-error`
  const helperId = `${id}-helper`

  return (
    <div className={`flex flex-col gap-1.5 ${className}`}>
      <label htmlFor={id} className="text-sm font-medium text-ink">
        {label}
      </label>
      <div className="relative">
        <input
          id={id}
          ref={ref}
          aria-invalid={Boolean(error)}
          aria-describedby={error ? errorId : helperText ? helperId : undefined}
          className={`w-full rounded-lg border bg-white px-3.5 py-2.5 text-[15px] text-ink placeholder:text-ink/35 transition-colors focus:outline-none focus:ring-2 focus:ring-gold-dark/50 ${
            endAdornment ? 'pr-20' : ''
          } ${error ? 'border-danger' : 'border-ink/15 focus:border-gold-dark'}`}
          {...props}
        />
        {endAdornment ? (
          <div className="absolute inset-y-0 right-0 flex items-center pr-3">{endAdornment}</div>
        ) : null}
      </div>
      {error ? (
        <p id={errorId} className="text-sm text-danger">
          {error}
        </p>
      ) : helperText ? (
        <p id={helperId} className="text-sm text-ink/45">
          {helperText}
        </p>
      ) : null}
    </div>
  )
})
