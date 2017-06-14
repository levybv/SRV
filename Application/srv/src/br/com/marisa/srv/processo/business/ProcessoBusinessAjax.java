package br.com.marisa.srv.processo.business;

import java.util.List;

import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os métodos de negócio do módulo de Processos para acesso Ajax
 * 
 * @author Walter Fontes
 */
public class ProcessoBusinessAjax {
	
    //Log4J
    //private static final Logger log = Logger.getLogger(ProcessoBusinessAjax.class);    
    
	/**
	 * Obtém processos ativos
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List obtemProcessosAtivos() throws SRVException {
		return ProcessoBusiness.getInstance().obtemProcessosAtivos();
	}	
}