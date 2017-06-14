package br.com.marisa.srv.geral.excecoes;

import java.util.logging.Level;

import org.apache.log4j.Logger;

/**
 * Classe de exceção para centralizar as exceções do sistema
 * 
 * @author Walter Fontes
 */
public class SRVException extends Exception {

    public SRVException(String mensagem) {
        super(mensagem);
    }
    
    public SRVException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
    
    public SRVException(Logger log, String mensagem) {
        super(mensagem);
    	log.error(mensagem);
    }    
    
    public SRVException(Logger log, String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    	log.error(mensagem, throwable);
    }
    
    public SRVException(String mensagem, Throwable throwable,java.util.logging.Logger log, Level level, Class classe, String metodo) {
        super(mensagem, throwable);
        StringBuilder sb = new StringBuilder();
        if(throwable != null){
	        for(int i=0;i<throwable.getStackTrace().length ;i++){
	        	sb.append("\r\n"+throwable.getStackTrace()[i]);
	        }
	        sb.append("\r\n"+getCause().getMessage());
        }
    	log.logp(level, classe.getName(), metodo, mensagem+sb);
    }
}