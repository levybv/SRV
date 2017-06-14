package br.com.marisa.srv.geral.excecoes;

import java.util.logging.Level;

import org.apache.log4j.Logger;

/**
 * 
 * @author levy.villar
 *
 */
public class BonusException extends SRVException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8135977039928401279L;

	public BonusException(String mensagem) {
        super(mensagem);
    }
    
    public BonusException(Logger log, String mensagem) {
        super(mensagem);
    	log.error(mensagem);
    }      
    
    public BonusException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
    
    public BonusException(Logger log, String mensagem, Throwable throwable) {
        super(log, mensagem, throwable);
    } 
    
    @SuppressWarnings("rawtypes")
	public BonusException(String mensagem, Throwable throwable,java.util.logging.Logger log, Level level, Class classe, String metodo) {
        super( mensagem,  throwable, log,  level,  classe,  metodo);
    } 
}
