package br.com.marisa.srv.meta.business;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.meta.vo.MetaFuncionarioVO;

/**
 * Classe para conter os métodos de negócio do módulo de Metas de Funcionários
 * para consultar Ajax
 * 
 * @author Walter Fontes
 */
public class MetaFuncionarioBusinessAjax {

	/**
	 * Obtém meta de funcionário
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