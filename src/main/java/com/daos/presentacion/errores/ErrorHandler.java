package com.daos.presentacion.errores;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.daos.excepciones.Excepcion;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ErrorHandler {

	@ExceptionHandler(Excepcion.class)
	public ResponseEntity<ErrorInfo> excepcion(HttpServletRequest request, Excepcion e) {
		System.out.println("ENTRÓ AL ERROR HANDLER: " + e.getMensaje());
		ErrorInfo errorInfo = new ErrorInfo(e, request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.valueOf(e.getStatusCode()));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorInfo> cuerpoInvalido(HttpServletRequest request, HttpMessageNotReadableException e) {
		ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),
				"El cuerpo de la solicitud no tiene un formato válido", request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorInfo> parametroInvalido(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
		ErrorInfo errorInfo = new ErrorInfo(HttpStatus.BAD_REQUEST.value(),
				"El parámetro " + e.getName() + " tiene un valor inválido: " + e.getValue(), request.getRequestURI());
		return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorInfo> validacionFallida(HttpServletRequest request, MethodArgumentNotValidException e) {
		StringBuilder mensaje = new StringBuilder();
    e.getBindingResult().getFieldErrors().forEach(error ->
        mensaje.append(error.getDefaultMessage())
    );
    ErrorInfo errorInfo = new ErrorInfo(
        HttpStatus.BAD_REQUEST.value(),
        mensaje.toString(),
        request.getRequestURI()
    );
    return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
	}
}
