import { forwardRef, useState } from 'react'
import { TextField } from './TextField'

export const PasswordField = forwardRef(function PasswordField(props, ref) {
  const [visivel, setVisivel] = useState(false)

  return (
    <TextField
      ref={ref}
      type={visivel ? 'text' : 'password'}
      endAdornment={
        <button
          type="button"
          onClick={() => setVisivel((atual) => !atual)}
          className="text-sm font-medium text-plum hover:text-plum-deep"
          aria-label={visivel ? 'Ocultar senha' : 'Mostrar senha'}
        >
          {visivel ? 'Ocultar' : 'Mostrar'}
        </button>
      }
      {...props}
    />
  )
})
