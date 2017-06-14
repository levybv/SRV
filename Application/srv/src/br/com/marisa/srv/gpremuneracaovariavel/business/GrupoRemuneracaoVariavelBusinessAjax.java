package br.com.marisa.srv.gpremuneracaovariavel.business;

import java.util.List;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.gpremuneracaovariavel.vo.GrupoRemuneracaoVariavelVO;

/**
 * Classe para conter os métodos de negócio acessíveis por Ajax do módulo de Grupo de Remuneração Variável
 * 
 * @author Walter Fontes
 */
public class GrupoRemuneracaoVariavelBusinessAjax {
	
	/**
	 * Obtém Grupo de Remuneração Variável
	 * 
	 * @param idGrupoRemuneracaoSrt
	 * @return
	 * @throws SRVException
	 */
	public GrupoRemuneracaoVariavelVO obtemGrupoRemuneracaoVariavel(String idGrupoRemuneracaoSrt) throws SRVException {
		Integer idGrupoRemuneracaoHay = new Integer(idGrupoRemuneracaoSrt);
		return GrupoRemuneracaoVariavelBusiness.getInstance().obtemGrupoRemuneracaoVariavel(idGrupoRemuneracaoHay.intValue());
	}
	
	/**
	 * Obtém a lista com os grupos de remuneracao do sistema
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List obtemGruposRemuneracao() throws SRVException {
		return GrupoRemuneracaoVariavelBusiness.getInstance().obtemGruposRemuneracao(-1, null);
	}
}