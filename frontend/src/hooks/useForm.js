import { useCallback, useState } from 'react'

// Estado de campos + validação + submit, compartilhado entre Login e
// Cadastro: cada tela só passa seus próprios initialValues/validators.
export function useForm({ initialValues, validators, onSubmit }) {
  const [values, setValues] = useState(initialValues)
  const [errors, setErrors] = useState({})
  const [erroGeral, setErroGeral] = useState('')
  const [enviando, setEnviando] = useState(false)

  const handleChange = useCallback(
    (campo) => (valorOuEvento) => {
      const valor = valorOuEvento?.target ? valorOuEvento.target.value : valorOuEvento
      setValues((atual) => ({ ...atual, [campo]: valor }))
      setErrors((atual) => {
        if (!atual[campo]) return atual
        const { [campo]: _removido, ...resto } = atual
        return resto
      })
    },
    [],
  )

  const validar = useCallback(() => {
    const novosErros = {}
    for (const [campo, validador] of Object.entries(validators)) {
      const mensagem = validador(values[campo])
      if (mensagem) novosErros[campo] = mensagem
    }
    setErrors(novosErros)
    return Object.keys(novosErros).length === 0
  }, [validators, values])

  const handleSubmit = useCallback(
    async (event) => {
      event?.preventDefault?.()
      setErroGeral('')
      if (!validar()) return

      setEnviando(true)
      try {
        await onSubmit(values)
      } catch (error) {
        if (error?.fieldErrors && Object.keys(error.fieldErrors).length > 0) {
          setErrors((atual) => ({ ...atual, ...error.fieldErrors }))
        }
        setErroGeral(error?.detail || 'Não foi possível completar a operação.')
      } finally {
        setEnviando(false)
      }
    },
    [onSubmit, validar, values],
  )

  return { values, errors, erroGeral, enviando, handleChange, handleSubmit }
}
