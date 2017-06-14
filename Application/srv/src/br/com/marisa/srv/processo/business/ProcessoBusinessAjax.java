package br.com.marisa.srv.processo.business;

import java.util.List;

import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os m�todos de neg�cio do m�dulo de Processos para acesso Ajax
 * 
 * @author Walter Fontes
 */
public class ProcessoBusinessAjax {
	
    //Log4J
    //private static final Logger log = Logger.getLogger(ProcessoBusinessAjax.class);    
    
	/**
	 * Obt�m processos ativos
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List obtemProcessosAtivos() throws SRVException {
		return ProcessoBusiness.getInstance().obtemProcessosAtivos();
	}	
}