package br.com.marisa.srv.grupoindicador.business;

import java.util.List;

/**
 * Business para os acessos a grupos de indicadores via AJAX
 * 
 * @author Walter Fontes
 *
 */
public class GrupoIndicadorBusinessAjax {
    
	/**
	 * Obt�m grupos de indicadores corporativos
	 * 
	 * @return
	 * @throws Exception
	 */
    public List obtemGruposIndicadoresCorporativos() throws Exception {
    	return GrupoIndicadorBusiness.getInstance().obtemGruposIndicadoresCorporativos();
    }
    
    
	/**
	 * Obt�m grupos de indicadores corporativos
	 * 
	 * @return
	 * @throws Exception
	 */
    public List obtemGruposIndicadores() throws Exception {
    	return GrupoIndicadorBusiness.getInstance().obtemGruposIndicadores();
    }    
}
