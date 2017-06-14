package br.com.marisa.srv.funcionario.business;

import br.com.marisa.srv.funcionario.dao.FuncionarioDAO;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.funcionario.vo.SalarioBaseVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Obtém Classe para consultas Ajax de Salário Base
 * 
 * @author Walter Fontes
 */
public class SalarioBaseBusinessAjax {
    
    
	/**
	 * Obtém Salário Base
	 * 
	 * @param idEmpresa
	 * @param idFilial
	 * @param idFuncionario
	 * @param ano
	 * @param mes
	 * @return
	 * @throws SRVException
	 */
    public SalarioBaseVO obtemSalarioBase(Integer idEmpresa, Integer idFilial, Long idFuncionario, Integer ano, Integer mes) throws SRVException {
    	return SalarioBaseBusiness.getInstance().obtemSalarioBase(idEmpresa, idFilial, idFuncionario, ano, mes);
    }    
    
	public FuncionarioVO obtemCodigoFilialFuncionario(Long idFuncionario) throws SRVException {
		return SalarioBaseBusiness.getInstance().obterCodigoFilialFuncionario(idFuncionario);
	}

	/**
	 * Verifica se determinado salário base já existe na base
	 * 
	 * @param periodo
	 * @param idFuncionario
	 * @return
	 * @throws SRVException
	 */
    public Boolean salarioBaseExiste(String periodo, Long idFuncionario) throws SRVException {
    	
		int posicaoDivisao = periodo.indexOf("-");
		Integer ano = new Integer(periodo.substring(0, posicaoDivisao));
		Integer mes = new Integer(periodo.substring(posicaoDivisao+1));
    	
		FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
		FuncionarioVO funcionarioVO = funcionarioDAO.obtemFuncionario(idFuncionario);
		if (funcionarioVO == null) {
			return Boolean.FALSE;
		}
		Integer idEmpresa = new Integer(Constantes.CODIGO_EMPRESA);
		Integer idFilial = funcionarioVO.getIdFilial();
    	
    	return new Boolean(SalarioBaseBusiness.getInstance().obtemSalarioBase(idEmpresa, idFilial, idFuncionario, ano, mes) != null);
    }  
}