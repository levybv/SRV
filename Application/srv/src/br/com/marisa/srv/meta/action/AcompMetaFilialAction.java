package br.com.marisa.srv.meta.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.meta.business.MetaFilialBusiness;

/**
 * Action para tratar as requisições de Acompanhamento de Metas de Filiais
 * 
 * @author Walter Fontes
 */
public class AcompMetaFilialAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_ACOMP_META_FILIAL = "acompMetaFilial";
		
	
	/**
	 * Realiza a consulta dos acompanhamentos das metas de filiais
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}
	
	
	/**
	 * Monta os dados e chama a tela principal
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	private ActionForward montaTelaPrincipal(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		Integer mes 				= getIntegerParam(pRequest, "mesF");
		Integer ano					= getIntegerParam(pRequest, "anoF");
		Integer idIndicador 		= getIntegerParam(pRequest, "idIndicadorF");
		String  descricaoIndicador  = getStringParam (pRequest, "descricaoIndicadorF");
		Integer idFilial 			= getIntegerParam(pRequest, "idFilialF");
		Boolean apenasNaoRealizadas = getBooleanParam(pRequest, "apenasNaoRealizadasF");
		
		pRequest.setAttribute("mesF",				  mes);
		pRequest.setAttribute("anoF", 				  ano);
		pRequest.setAttribute("idIndicadorF", 		  idIndicador);
		pRequest.setAttribute("descricaoIndicadorF",  descricaoIndicador);
		pRequest.setAttribute("idFilialF", 			  idFilial);
		pRequest.setAttribute("apenasNaoRealizadasF", apenasNaoRealizadas);
		
		if (mes != null || ano != null || idIndicador != null || descricaoIndicador != null || idFilial != null) {
			List metasFiliais = MetaFilialBusiness.getInstance().obtemAcompMetasFiliais(mes, ano, idFilial, idIndicador, descricaoIndicador, apenasNaoRealizadas);
			pRequest.setAttribute("metasFiliais", metasFiliais);
		}
		
		return pMapping.findForward(AcompMetaFilialAction.FORWARD_ACOMP_META_FILIAL);		
	}
	
}