package br.com.marisa.srv.logger.business;



import java.util.logging.Level;

public enum NivelLogger {
	
		SEVERE(Level.SEVERE), WARNING(Level.WARNING), INFO(Level.INFO), CONFIG(Level.CONFIG), FINE(Level.FINE), FINER(Level.FINER), FINEST(Level.FINEST);
	     
	    private final Level valor;
	    
	    NivelLogger(Level valorOpcao){
	        valor = valorOpcao;
	    }
	    public Level getValor(){
	        return valor;
	    }
	
}
