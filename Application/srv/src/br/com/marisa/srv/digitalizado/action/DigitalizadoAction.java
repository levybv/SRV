package br.com.marisa.srv.digitalizado.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.calendario.business.CalendarioComercialBusiness;
import br.com.marisa.srv.geral.action.BasicAction;

/**
 * Action para tratar funcionarios digitalizados
 */
public class DigitalizadoAction extends BasicAction {

	public DigitalizadoAction() throws Exception {}

	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		List listaMes = CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaMes(false);
		List listaAno = CalendarioComercialBusiness.getInstance().obtemCalendarioComercialListaAno(true);

		pRequest.setAttribute("listaMes", listaMes);
		pRequest.setAttribute("listaAno", listaAno);

		return pMapping.findForward(DigitalizadoAction.PAGINA_INICIAL);
	}

}