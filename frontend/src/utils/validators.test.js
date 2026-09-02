import { describe, expect, it } from 'vitest'
import {
  validarEmail,
  validarNome,
  validarSenhaCadastro,
  validarSenhaLogin,
  validarTelefone,
} from './validators'

describe('validarNome', () => {
  it('exige nome preenchido', () => {
    expect(validarNome('')).toBe('Nome é obrigatório')
    expect(validarNome('   ')).toBe('Nome é obrigatório')
  })

  it('rejeita nome acima de 150 caracteres', () => {
    expect(validarNome('a'.repeat(151))).toBe('Nome deve ter no máximo 150 caracteres')
  })

  it('aceita nome válido', () => {
    expect(validarNome('Maria da Silva')).toBeUndefined()
  })
})

describe('validarEmail', () => {
  it('exige email preenchido', () => {
    expect(validarEmail('')).toBe('Email é obrigatório')
  })

  it('rejeita formato inválido', () => {
    expect(validarEmail('nao-e-email')).toBe('Email inválido')
  })

  it('aceita email válido', () => {
    expect(validarEmail('maria@exemplo.com')).toBeUndefined()
  })
})

describe('validarTelefone', () => {
  it('é opcional', () => {
    expect(validarTelefone('')).toBeUndefined()
    expect(validarTelefone(undefined)).toBeUndefined()
  })

  it('rejeita quantidade de dígitos fora de 10-11', () => {
    expect(validarTelefone('123')).toBe('Telefone deve conter 10 ou 11 dígitos (DDD + número)')
  })

  it('aceita 10 ou 11 dígitos, ignorando máscara', () => {
    expect(validarTelefone('(11) 1234-5678')).toBeUndefined()
    expect(validarTelefone('(11) 91234-5678')).toBeUndefined()
  })
})

describe('validarSenhaCadastro', () => {
  it('exige senha preenchida', () => {
    expect(validarSenhaCadastro('')).toBe('Senha é obrigatória')
  })

  it('rejeita senha fora do intervalo 8-72', () => {
    expect(validarSenhaCadastro('curta')).toBe('Senha deve ter entre 8 e 72 caracteres')
    expect(validarSenhaCadastro('a'.repeat(73))).toBe('Senha deve ter entre 8 e 72 caracteres')
  })

  it('aceita senha válida', () => {
    expect(validarSenhaCadastro('senhaSegura123')).toBeUndefined()
  })
})

describe('validarSenhaLogin', () => {
  it('só exige que não esteja vazia', () => {
    expect(validarSenhaLogin('')).toBe('Senha é obrigatória')
    expect(validarSenhaLogin('123')).toBeUndefined()
  })
})
