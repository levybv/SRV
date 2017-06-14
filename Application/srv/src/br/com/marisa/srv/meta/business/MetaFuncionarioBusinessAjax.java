package br.com.marisa.srv.meta.business;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.meta.vo.MetaFuncionarioVO;

/**
 * Classe para conter os m�todos de neg�cio do m�dulo de Metas de Funcion�rios
 * para consultar Ajax
 * 
 * @author Walter Fontes
 */
public class MetaFuncionarioBusinessAjax {

	/**
	 * Obt�m meta de funcion�rio
	 * 
	 * @param mes
	 * @param ano
	 * @param idFuncionario
	 * @param idIndicador
	 * @return
	 * @throws SRVException
	 */
	public MetaFuncionarioVO obtemMetaFuncionario(Integer mes, Integer ano, Long idFuncionario, Integer idIndicador) throws SRVException {

		return MetaFuncionarioBusiness.getInstance().obtemMetaFuncionario(mes, ano, idFuncionario, idIndicador);
	}

}