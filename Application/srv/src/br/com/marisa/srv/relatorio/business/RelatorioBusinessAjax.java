package br.com.marisa.srv.relatorio.business;

import java.util.List;

import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.relatorio.vo.RelatorioVO;

/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioBusinessAjax {

	/**
	 * 
	 * @param codigo
	 * @return
	 * @throws Exception
	 */
	public RelatorioVO obtemRelatorio(int codigo) throws Exception {
		RelatorioVO retornoVO = null;
		RelatorioVO relatorioVO = new RelatorioVO();
		relatorioVO.setCodigo(codigo);
		List<RelatorioVO> lista = RelatorioBusiness.getInstance().obtemRelatorio(relatorioVO);
		if (ObjectHelper.isNotEmpty(lista)) {
			retornoVO = lista.get(0);
		}
		return retornoVO;
	}

}