package br.com.marisa.srv.indicador.ppt.action;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.filial.business.FilialBusiness;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.indicador.ppt.business.PecaTicketBusiness;
import br.com.marisa.srv.indicador.ppt.vo.PecaTicketVO;

/**
 * 
 * @author levy.villar
 *
 */
public class PecaTicketAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		pRequest.removeAttribute("listaFilial");
		pRequest.setAttribute("listaFilial", FilialBusiness.getInstance().obtemTodasFiliais(true));

		return pMapping.findForward(PecaTicketAction.PAGINA_INICIAL);
	}

	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward executarFiltro(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		Integer idFilial = getIntegerParam(pRequest, "idFilialF");
		String periodo = getStringParam(pRequest, "periodoF");
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		Calendar periodoCalendar = Calendar.getInstance();
		periodoCalendar.setTime(sdf.parse(periodo));
		int ano = periodoCalendar.get(Calendar.YEAR);
		int mes = periodoCalendar.get(Calendar.MONTH)+1;

		PecaTicketVO pesquisaVO = new PecaTicketVO();
		pesquisaVO.setNumAno(ano);
		pesquisaVO.setNumMes(mes);
		pesquisaVO.setCodFilial(idFilial);
		pesquisaVO.setCodEmpresa(Constantes.CODIGO_EMPRESA);
		List<PecaTicketVO> listaPpt = PecaTicketBusiness.getInstance().obterListaRealizadoFilialPecaTicket(pesquisaVO);

		pRequest.setAttribute("periodoFiltro", periodo);
		pRequest.setAttribute("listaFilial", FilialBusiness.getInstance().obtemTodasFiliais(true));
		pRequest.setAttribute("idFilialF",idFilial);
		pRequest.setAttribute("listaPpt", listaPpt);

		return pMapping.findForward(PecaTicketAction.PAGINA_INICIAL);

	}

}