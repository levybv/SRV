package br.com.marisa.srv.geral.excecoes;

import org.apache.log4j.Logger;

/**
 * Classe de exce��o espec�fica para problemas da camada de apresenta��o
 * 
 * @author Walter Fontes
 */
public class ApresentacaoException extends SRVException {

    public ApresentacaoException(String mensagem) {
        super(mensagem);
    }
    
    public ApresentacaoException(Logger log, String mensagem) {
        super(mensagem);
    	log.error(mensagem);
    }      
    
    public ApresentacaoException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
    
    public ApresentacaoException(Logger log, String mensagem, Throwable throwable) {
        super(log, mensagem, throwable);
    }    
}
