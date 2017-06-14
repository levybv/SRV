package br.com.marisa.srv.indicador.excecoes;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe de exceção específica para problemas da camada de apresentação
 * 
 * @author Walter Fontes
 */
public class IndicadorException extends SRVException {

    public IndicadorException(String mensagem) {
        super(mensagem);
    }
    
    public IndicadorException(Logger log, String mensagem) {
        super(mensagem);
    	log.error(mensagem);
    }      
    
    public IndicadorException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
    
    public IndicadorException(Logger log, String mensagem, Throwable throwable) {
        super(log, mensagem, throwable);
    }    
}
