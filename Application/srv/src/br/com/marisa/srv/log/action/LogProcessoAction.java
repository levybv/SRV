package br.com.marisa.srv.log.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.log.business.LogProcessoBusiness;
import br.com.marisa.srv.log.vo.LogProcessoVO;

public class LogProcessoAction extends BasicAction {

	private static final String FORWARD_LOG = "logProcesso";

	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}

	public ActionForward montaTelaPrincipal(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		List<LogProcessoVO> listaLogProcessos = LogProcessoBusiness.getInstance().obtemTodosLogsProcesso();
		pRequest.setAttribute("listaLogProcessos", listaLogProcessos);
		pRequest.getSession().setAttribute("sessionTokenRefresh", Boolean.TRUE);

		return pMapping.findForward(FORWARD_LOG);
	}

	public ActionForward pesquisaLogProcesso(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		String data = getStringParam(pRequest, "dataF");
		Integer processo = getIntegerParam(pRequest, "processoF");

		pRequest.setAttribute("data", data);
		pRequest.setAttribute("processo", processo);

		List<LogProcessoVO> listaLogProcessos = LogProcessoBusiness.getInstance().pesquisaLogProcesso(getDateParam(pRequest, "dataF"), processo);

		pRequest.setAttribute("listaProcessosEncontrados", listaLogProcessos);
		montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);

		return pMapping.findForward(FORWARD_LOG);
	}

}