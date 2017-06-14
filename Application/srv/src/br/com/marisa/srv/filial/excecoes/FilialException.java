package br.com.marisa.srv.filial.excecoes;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe de exceção específica para problemas da camada de apresentação
 * 
 * @author Walter Fontes
 */
public class FilialException extends SRVException {

    public FilialException(String mensagem) {
        super(mensagem);
    }
    
    public FilialException(Logger log, String mensagem) {
        super(mensagem);
    	log.error(mensagem);
    }      
    
    public FilialException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
    
    public FilialException(Logger log, String mensagem, Throwable throwable) {
        super(log, mensagem, throwable);
    }    
}
