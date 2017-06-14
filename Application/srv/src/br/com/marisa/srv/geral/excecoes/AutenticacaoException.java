package br.com.marisa.srv.geral.excecoes;

/**
 * 
 * @author levy.villar
 *
 */
public class AutenticacaoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5920778141208084069L;

	/**
	 * 
	 */
	public AutenticacaoException() {
	}

	/**
	 * 
	 * @param message
	 */
	public AutenticacaoException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param cause
	 */
	public AutenticacaoException(Throwable cause) {
		super(cause);
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public AutenticacaoException(String message, Throwable cause) {
		super(message, cause);
	}

}
