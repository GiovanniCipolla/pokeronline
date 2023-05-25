package it.prova.pokeronline.web.api.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.prova.pokeronline.exception.CreditoInsufficienteException;
import it.prova.pokeronline.exception.GiocatoriNelTavoloException;
import it.prova.pokeronline.exception.NessunTavoloADisposizioneLastGameException;
import it.prova.pokeronline.exception.NonSedutoAlTavoloException;
import it.prova.pokeronline.exception.UtenteGiaPresenteException;
import it.prova.pokeronline.exception.ImpossibileGiocareCreditoException;
import it.prova.pokeronline.exception.IdNonValidoException;
import it.prova.pokeronline.exception.EsperienzaInsufficienteException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status.value());

		// Get all errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
				.collect(Collectors.toList());

		body.put("errors", errors);

		return new ResponseEntity<>(body, headers, status);
	}

	@ExceptionHandler(IdNotNullForInsertException.class)
	public ResponseEntity<Object> handleIdNotNullForInsertException(IdNotNullForInsertException ex,
			WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);

		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler(CreditoInsufficienteException.class)
	public ResponseEntity<Object> CreditoInsufficienteException(CreditoInsufficienteException ex,
			WebRequest request) {
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
		
		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler(EsperienzaInsufficienteException.class)
	public ResponseEntity<Object> EsperienzaInsufficienteException(EsperienzaInsufficienteException ex,
			WebRequest request) {
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
		
		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler(GiocatoriNelTavoloException.class)
	public ResponseEntity<Object> GiocatoriNelTavoloException(GiocatoriNelTavoloException ex,
			WebRequest request) {
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
		
		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler(IdNonValidoException.class)
	public ResponseEntity<Object> IdNonValidoException(IdNonValidoException ex,
			WebRequest request) {
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
		
		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler(ImpossibileGiocareCreditoException.class)
	public ResponseEntity<Object> ImpossibileGiocareCreditoException(ImpossibileGiocareCreditoException ex,
			WebRequest request) {
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
		
		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler(NessunTavoloADisposizioneLastGameException.class)
	public ResponseEntity<Object> NessunTavoloADisposizioneLastGameException(NessunTavoloADisposizioneLastGameException ex,
			WebRequest request) {
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
		
		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler(NonSedutoAlTavoloException.class)
	public ResponseEntity<Object> NonSedutoAlTavoloException(NonSedutoAlTavoloException ex,
			WebRequest request) {
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
		
		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler(UtenteGiaPresenteException.class)
	public ResponseEntity<Object> UtenteGiaPresenteException(UtenteGiaPresenteException ex,
			WebRequest request) {
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
		
		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	@ExceptionHandler(RicaricaAndataMaleException.class)
	public ResponseEntity<Object> RicaricaAndataMaleException(RicaricaAndataMaleException ex,
			WebRequest request) {
		
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ex.getMessage());
		body.put("status", HttpStatus.UNPROCESSABLE_ENTITY);
		
		return new ResponseEntity<>(body, HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
}
