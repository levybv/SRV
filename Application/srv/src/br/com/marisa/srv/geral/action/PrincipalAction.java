package br.com.marisa.srv.geral.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Data de Criação: 04/08/2011
 * 
 * @author Walter Fontes
 */
public class PrincipalAction extends BasicAction {

	/**
	 */
	private static final String FORWARD_LOGIN = "login";

	/**
	 * Cria um novo objeto PrincipalAction
	 * 
	 * @throws ExcecaoParametroInvalido
	 */
	public PrincipalAction() throws Exception {
		//
	}


	/**
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward iniciarSistema(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		return pMapping.findForward(PrincipalAction.FORWARD_LOGIN);
	}
	
}