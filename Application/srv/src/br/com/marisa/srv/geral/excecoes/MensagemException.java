package br.com.marisa.srv.geral.excecoes;

import java.util.logging.Level;

import org.apache.log4j.Logger;

/**
 * 
 * @author levy.villar
 *
 */
public class MensagemException extends SRVException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7311877629112746362L;

	public MensagemException(String mensagem) {
        super(mensagem);
    }
    
    public MensagemException(Logger log, String mensagem) {
        super(mensagem);
    	log.error(mensagem);
    }      
    
    public MensagemException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
    
    public MensagemException(Logger log, String mensagem, Throwable throwable) {
        super(log, mensagem, throwable);
    } 
    
    @SuppressWarnings("rawtypes")
	public MensagemException(String mensagem, Throwable throwable,java.util.logging.Logger log, Level level, Class classe, String metodo) {
        super( mensagem,  throwable, log,  level,  classe,  metodo);
    } 
}
