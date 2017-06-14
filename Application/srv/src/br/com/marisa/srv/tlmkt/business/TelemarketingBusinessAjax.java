package br.com.marisa.srv.tlmkt.business;

import java.util.List;

import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.tlmkt.vo.TelemarketingVO;

/**
 * 
 * @author Levy Villar
 *
 */
public class TelemarketingBusinessAjax {

	/**
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List<FuncionarioVO> obtemListaFuncionariosDisponiveis() throws SRVException {
		return TelemarketingBusiness.getInstance().obtemListaFuncionariosDisponiveis();
	}

	/**
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List<DadosIndicadorVO> obtemListaIndicadoresTlmkt() throws SRVException {
		DadosIndicadorVO pesquisaVO = new DadosIndicadorVO();
		pesquisaVO.setAtivo(Constantes.CD_VERDADEIRO);
		pesquisaVO.setIdGrupoIndicador(Constantes.COD_GRUPO_INDIC_TLMKT);
		List<DadosIndicadorVO> listaIndicadores = IndicadorBusiness.getInstance().obtemListaIndicadores(pesquisaVO, null, Boolean.FALSE, Boolean.FALSE);
		return listaIndicadores;
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 * @throws SRVException
	 */
	public TelemarketingVO obtemTlmktElegivel(Integer ano, Integer mes) throws SRVException {
		return TelemarketingBusiness.getInstance().obtemTlmktElegivel(ano, mes, null);
	}

}
