package it.prova.pokeronline.exception;

public class CreditoInsufficienteException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public CreditoInsufficienteException(String message) {
		super(message);
	}
}