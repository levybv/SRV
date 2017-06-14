package br.com.marisa.srv.geral.excecoes;

import org.apache.log4j.Logger;

/**
 * Classe de exceção específica para problemas de segurança
 * 
 * @author Walter Fontes
 */
public class SegurancaException extends SRVException {

    public SegurancaException(String mensagem) {
        super(mensagem);
    }
    
    public SegurancaException(Logger log, String mensagem) {
        super(mensagem);
    	log.error(mensagem);
    }      
    
    public SegurancaException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
    
    public SegurancaException(Logger log, String mensagem, Throwable throwable) {
        super(log, mensagem, throwable);
    }    
}
