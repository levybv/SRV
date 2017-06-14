package br.com.marisa.srv.meta.business;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.meta.vo.MetaFilialVO;

/**
 * Classe para conter os métodos de negócio do módulo de Metas de Filiais para consultar Ajax
 * 
 * @author Walter Fontes
 */
public class MetaFilialBusinessAjax {
    

	/**
	 * Obtém meta de filial
	 * 
	 * @param mes
	 * @param ano
	 * @param idEmpresa
	 * @param idFilial
	 * @param idIndicador
	 * @return
	 * @throws SRVException
	 */
	public MetaFilialVO obtemMetaFilial(Integer mes, Integer ano, Integer idEmpresa, Integer idFilial, Integer idIndicador) throws SRVException {
		return MetaFilialBusiness.getInstance().obtemMetaFilial(mes, ano, idEmpresa, idFilial, idIndicador);
	}	
		
}