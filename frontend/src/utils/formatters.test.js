import { describe, expect, it } from 'vitest'
import { formatarTelefone, somenteDigitos } from './formatters'

describe('somenteDigitos', () => {
  it('remove tudo que não for número', () => {
    expect(somenteDigitos('(11) 91234-5678')).toBe('11912345678')
    expect(somenteDigitos('')).toBe('')
    expect(somenteDigitos(undefined)).toBe('')
  })
})

describe('formatarTelefone', () => {
  it('formata progressivamente enquanto digita', () => {
    expect(formatarTelefone('1')).toBe('(1')
    expect(formatarTelefone('11')).toBe('(11')
    expect(formatarTelefone('1191')).toBe('(11) 91')
  })

  it('formata celular (11 dígitos)', () => {
    expect(formatarTelefone('11912345678')).toBe('(11) 91234-5678')
  })

  it('formata fixo (10 dígitos)', () => {
    expect(formatarTelefone('1112345678')).toBe('(11) 1234-5678')
  })

  it('ignora dígitos além do 11º', () => {
    expect(formatarTelefone('119123456789999')).toBe('(11) 91234-5678')
  })
})
