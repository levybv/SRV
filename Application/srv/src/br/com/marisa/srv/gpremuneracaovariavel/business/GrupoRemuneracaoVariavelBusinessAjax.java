package br.com.marisa.srv.gpremuneracaovariavel.business;

import java.util.List;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.gpremuneracaovariavel.vo.GrupoRemuneracaoVariavelVO;

/**
 * Classe para conter os m�todos de neg�cio acess�veis por Ajax do m�dulo de Grupo de Remunera��o Vari�vel
 * 
 * @author Walter Fontes
 */
public class GrupoRemuneracaoVariavelBusinessAjax {
	
	/**
	 * Obt�m Grupo de Remunera��o Vari�vel
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
	 * Obt�m a lista com os grupos de remuneracao do sistema
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List obtemGruposRemuneracao() throws SRVException {
		return GrupoRemuneracaoVariavelBusiness.getInstance().obtemGruposRemuneracao(-1, null);
	}
}