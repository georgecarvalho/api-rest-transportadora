package br.edu.ifma.frete.exception;

public class FreteException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FreteException(Exception e) throws FreteException{
		super(e);
	}
}
