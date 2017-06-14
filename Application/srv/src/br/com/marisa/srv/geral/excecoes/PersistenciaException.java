package br.com.marisa.srv.geral.excecoes;

import java.util.logging.Level;

import org.apache.log4j.Logger;

/**
 * Classe de exceção específica para problemas de persistência
 * 
 * @author Walter Fontes
 */
public class PersistenciaException extends SRVException {

    public PersistenciaException(String mensagem) {
        super(mensagem);
    }
    
    public PersistenciaException(Logger log, String mensagem) {
        super(mensagem);
    	log.error(mensagem);
    }      
    
    public PersistenciaException(String mensagem, Throwable throwable) {
        super(mensagem, throwable);
    }
    
    public PersistenciaException(Logger log, String mensagem, Throwable throwable) {
        super(log, mensagem, throwable);
    } 
    
    public PersistenciaException(String mensagem, Throwable throwable,java.util.logging.Logger log, Level level, Class classe, String metodo) {
        super( mensagem,  throwable, log,  level,  classe,  metodo);
    } 
}
