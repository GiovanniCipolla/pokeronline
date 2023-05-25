package it.prova.pokeronline.exception;

public class UtenteGiaPresenteException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public UtenteGiaPresenteException(String message) {
		super(message);
	}
}
