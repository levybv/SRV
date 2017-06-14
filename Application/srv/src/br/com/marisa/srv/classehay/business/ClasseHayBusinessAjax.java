package br.com.marisa.srv.classehay.business;

import java.util.List;

import br.com.marisa.srv.classehay.vo.ClasseHayVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os m�todos de neg�cio acess�veis por Ajax do m�dulo de Classe Hay 
 * 
 * @author Walter Fontes
 */
public class ClasseHayBusinessAjax {
	
	/**
	 * Obt�m classe hay
	 * 
	 * @param idClasseHaySrt
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
	public ClasseHayVO obtemClasseHay(String idClasseHaySrt) throws SRVException {
		Integer idClasseHay = new Integer(idClasseHaySrt);
		return ClasseHayBusiness.getInstance().obtemClasseHay(idClasseHay.intValue());
	}
	
	/**
	 * Verifica se o codigo j� existe
	 * 
	 * @param idClasseHaySrt
	 * @return
	 * @throws SRVException
	 */
	public boolean codigoJaExiste(String idClasseHaySrt) throws SRVException {
		Integer idClasseHay = new Integer(idClasseHaySrt);
		ClasseHayVO classeHayVO = ClasseHayBusiness.getInstance().obtemClasseHay(idClasseHay.intValue());
		return (classeHayVO != null);
	}
	
	/**
	 * Obt�m a lista com as classes hay do sistema
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List obtemClassesHay() throws SRVException {
		return ClasseHayBusiness.getInstance().obtemClassesHay(-1, null);
	}
}