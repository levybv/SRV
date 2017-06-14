package br.com.marisa.srv.unidade.business;

import java.util.List;

/**
 * Business para os acessos a unidades via AJAX
 * 
 * @author Walter Fontes
 *
 */
public class UnidadeBusinessAjax {
    
	/**
	 * Obt�m unidades
	 * 
	 * @return
	 * @throws Exception
	 */
    public List obtemUnidades() throws Exception {
    	return UnidadeBusiness.getInstance().obtemUnidades();
    }
    
    
	/**
	 * Obt�m unidades agregadas
	 * 
	 * @return
	 * @throws Exception
	 */
    public List obtemUnidadesAgregadas() throws Exception {
    	return UnidadeBusiness.getInstance().obtemUnidadesAgregadas();
    }    
}
