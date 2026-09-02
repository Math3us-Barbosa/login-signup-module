import { useNavigate } from 'react-router-dom'
import { AuthLayout } from '../../components/AuthLayout'
import { AuthSwitchLink } from '../../components/AuthSwitchLink'
import { Button } from '../../components/Button'
import { FormAlert } from '../../components/FormAlert'
import { PasswordField } from '../../components/PasswordField'
import { TextField } from '../../components/TextField'
import { useAuth } from '../../hooks/useAuth'
import { useForm } from '../../hooks/useForm'
import { parseApiError } from '../../services/apiError'
import { formatarTelefone } from '../../utils/formatters'
import {
  validarEmail,
  validarNome,
  validarSenhaCadastro,
  validarTelefone,
} from '../../utils/validators'

export default function CadastroPage() {
  const navigate = useNavigate()
  const { cadastrar } = useAuth()

  const { values, errors, erroGeral, enviando, handleChange, handleSubmit } = useForm({
    initialValues: { nome: '', email: '', telefone: '', senha: '' },
    validators: {
      nome: validarNome,
      email: validarEmail,
      telefone: validarTelefone,
      senha: validarSenhaCadastro,
    },
    onSubmit: async (dados) => {
      try {
        await cadastrar(dados)
        // Cadastro não devolve token (a API não autentica automaticamente) —
        // manda pra tela de login já com o e-mail preenchido.
        navigate('/login', {
          replace: true,
          state: {
            mensagemSucesso: 'Conta criada! Entre com sua senha para continuar.',
            email: dados.email,
          },
        })
      } catch (error) {
        throw parseApiError(error)
      }
    },
  })

  return (
    <AuthLayout eyebrow="Junte-se à rede" title="Crie sua conta">
      <form className="flex flex-col gap-5" onSubmit={handleSubmit} noValidate>
        <FormAlert tone="error">{erroGeral}</FormAlert>
        <TextField
          label="Nome completo"
          autoComplete="name"
          value={values.nome}
          onChange={handleChange('nome')}
          error={errors.nome}
        />
        <TextField
          label="Email"
          type="email"
          autoComplete="email"
          value={values.email}
          onChange={handleChange('email')}
          error={errors.email}
        />
        <TextField
          label="Telefone"
          type="tel"
          inputMode="numeric"
          autoComplete="tel"
          placeholder="(11) 91234-5678"
          helperText={errors.telefone ? undefined : 'Opcional'}
          value={values.telefone}
          onChange={(event) => handleChange('telefone')(formatarTelefone(event.target.value))}
          error={errors.telefone}
        />
        <PasswordField
          label="Senha"
          autoComplete="new-password"
          helperText={errors.senha ? undefined : 'Mínimo de 8 caracteres'}
          value={values.senha}
          onChange={handleChange('senha')}
          error={errors.senha}
        />
        <Button type="submit" loading={enviando}>
          {enviando ? 'Criando conta…' : 'Criar conta'}
        </Button>
        <AuthSwitchLink pergunta="Já tem conta?" texto="Entrar" to="/login" />
      </form>
    </AuthLayout>
  )
}
