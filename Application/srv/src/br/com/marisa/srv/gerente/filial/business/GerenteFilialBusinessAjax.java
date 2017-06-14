package br.com.marisa.srv.gerente.filial.business;

import java.util.List;

import br.com.marisa.srv.gerente.filial.vo.GerenteVO;

/**
 * 
 * @author levy.villar
 *
 */
public class GerenteFilialBusinessAjax {

	/**
	 * 
	 * @param codFilial
	 * @return
	 * @throws Exception
	 */
	public Long lojaCadastrada(Integer codFilial) throws Exception {
    	return GerenteFilialBusiness.getInstance().lojaCadastrada(codFilial);
	}

	/**
	 * 
	 * @param codFuncionario
	 * @param codFilial
	 * @return
	 * @throws Exception
	 */
	public GerenteVO obtemGerenteLoja(Long codFuncionario, Integer codFilial) throws Exception {
    	return GerenteFilialBusiness.getInstance().obtemGerenteLoja(codFuncionario, codFilial);
	}

	/**
	 * 
	 * @param codFilial
	 * @return
	 * @throws Exception
	 */
	public List<GerenteVO> obtemFuncionariosPorFilial(Integer codFilial) throws Exception {
    	return GerenteFilialBusiness.getInstance().obtemFuncionariosPorFilial(codFilial);
	}

	/**
	 * 
	 * @param codFuncionario
	 * @return
	 * @throws Exception
	 */
	public Integer obtemLojaCadastro(Long codFuncionario) throws Exception {
    	return GerenteFilialBusiness.getInstance().obtemLojaCadastro(codFuncionario);
	}

}
