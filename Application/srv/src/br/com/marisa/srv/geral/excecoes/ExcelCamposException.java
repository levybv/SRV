package br.com.marisa.srv.geral.excecoes;

public class ExcelCamposException extends SRVException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7886327713217772598L;

	/**
	 * 
	 * @param message
	 */
	public ExcelCamposException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public ExcelCamposException(String message, Throwable cause) {
		super(message, cause);
	}

}
