package br.com.marisa.srv.processo.business;

import java.util.List;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.processo.dao.ProcessoDAO;

/**
 * Classe para conter os métodos de negócio do módulo de Processos Ativos 
 * 
 * @author Walter Fontes
 */
public class ProcessoBusiness {
    
	
    //Log4J
    //private static final Logger log = Logger.getLogger(ProcessoBusiness.class);    

    
    //Instancia do Singleton
    private static ProcessoBusiness instance = new ProcessoBusiness();
    
    
    /**
     * Obtem uma instancia do objeto ProcessoBusiness
     * @return O objeto ProcessoBusiness
     */
    public static final ProcessoBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private ProcessoBusiness() {
    }

    
	/**
	 * Obtém processos ativos
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List obtemProcessosAtivos() throws SRVException {
		ProcessoDAO processoDAO = new ProcessoDAO();
		try {
			return processoDAO.obtemProcessosAtivos();
		} finally {
			processoDAO.closeConnection();
		}
	}    
}