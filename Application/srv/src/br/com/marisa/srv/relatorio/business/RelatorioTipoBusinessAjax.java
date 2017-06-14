package br.com.marisa.srv.relatorio.business;

import java.util.List;

import br.com.marisa.srv.relatorio.vo.RelatorioTipoVO;

/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioTipoBusinessAjax {

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<RelatorioTipoVO> obtemListaRelatorioTipo() throws Exception {
		return RelatorioTipoBusiness.getInstance().obtemRelatorioTipo(null);
	}

}