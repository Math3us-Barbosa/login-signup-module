package com.matheus.loginsignupmodule.exception;

public class EmailJaCadastradoException extends RuntimeException {

	public EmailJaCadastradoException() {
		super("Email já cadastrado");
	}

}
