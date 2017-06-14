package br.com.marisa.srv.filial.business;

import java.util.List;

import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.SRVException;


public class FilialBusinessAjax {
	
	
	
	public FilialVO obtemListaFiliais(String codFilialStr, String codEmpresaStr) throws Exception {
		int codFilial = Integer.parseInt(codFilialStr);
		int codEmpresa = Integer.parseInt(codEmpresaStr);
		FilialVO filialVO = (FilialVO)FilialBusiness.getInstance().obtemListaFiliais(codEmpresa, codFilial).get(0);
		return filialVO;
	}
	
	public List obterListaTipoFiliais() throws Exception {
    	return FilialBusiness.getInstance().obterListaTipoFiliais();
	}

	/**
	 * Obtém lista de filiais
	 * 
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
	public List obtemTodasFiliaisAtivas() throws Exception {
		return FilialBusiness.getInstance().obtemListaFiliais(Constantes.CODIGO_EMPRESA, -1, Boolean.TRUE);
	}	

	public List obterTodasFiliais() throws Exception {
    	return FilialBusiness.getInstance().obterTodasFiliais();
	}

}
