package it.prova.pokeronline.exception;

public class UtenteNonValidoException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public UtenteNonValidoException(String message) {
		super(message);
	}
}
