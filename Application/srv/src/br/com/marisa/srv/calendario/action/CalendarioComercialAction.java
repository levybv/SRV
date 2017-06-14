package br.com.marisa.srv.calendario.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.calendario.business.CalendarioComercialBusiness;
import br.com.marisa.srv.calendario.vo.PeriodoCalendarioVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.DataHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;

/**
 * Data de Criação: 04/08/2011
 * 
 * @author Walter Fontes
 */
public class CalendarioComercialAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_CALENDARIO = "calendarioComercial";

	
	/**
	 * Cria um novo objeto PrincipalAction
	 * 
	 * @throws ExcecaoParametroInvalido
	 */
	public CalendarioComercialAction() throws Exception {
		//
	}


	/**
	 * Exibe a consulta do calendário
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}
	
	/**
	 */
	public ActionForward montaTelaPrincipal(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);

		Integer anoSelecionado =  getIntegerParam(pRequest, "anoSelecionadoF");
		List<PeriodoCalendarioVO> periodosCalendario = null;

		if (ObjectHelper.isEmpty(anoSelecionado)) {
			Integer anoSelecionadoFiltro =  getIntegerParam(pRequest, "anoSelecionadoFiltro");
			if (ObjectHelper.isEmpty(anoSelecionadoFiltro)) {
				anoSelecionado = DataHelper.anoAtual();
			} else {
				anoSelecionado = anoSelecionadoFiltro;
			}
			
		}

		periodosCalendario = CalendarioComercialBusiness.getInstance().obtemPeriodosCalendario(anoSelecionado);		

		pRequest.setAttribute("periodosCalendario", periodosCalendario);
		pRequest.setAttribute("anoFiltro", new Integer(anoSelecionado));
		pRequest.setAttribute("listaAnos", CalendarioComercialBusiness.getInstance().obtemListaAnos());

		return pMapping.findForward(CalendarioComercialAction.FORWARD_CALENDARIO);
	}

	/**
	 */
	public ActionForward alteraCalendarioComercial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			PeriodoCalendarioVO paramVO = new PeriodoCalendarioVO();
			paramVO.setPeriodo(getIntegerParam(pRequest, "idPeriodoA"));
			paramVO.setMes(getIntegerParam(pRequest, "mesA"));
			paramVO.setAno(getIntegerParam(pRequest, "anoA"));
			paramVO.setDataInicial(getDateParam(pRequest, "dataInicialA"));
			paramVO.setDataFinal(getDateParam(pRequest, "dataFinalA"));
			paramVO.setCodUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			CalendarioComercialBusiness.getInstance().alteraCalendarioComercial(paramVO);
			setMensagem(pRequest, "Calendário comercial alterado com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);

	}

	/**
	 */
	public ActionForward incluiCalendarioComercial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			PeriodoCalendarioVO paramVO = new PeriodoCalendarioVO();
			paramVO.setPeriodo(getIntegerParam(pRequest, "idPeriodoI"));
			paramVO.setMes(getIntegerParam(pRequest, "mesI"));
			paramVO.setAno(getIntegerParam(pRequest, "anoI"));
			paramVO.setDataInicial(getDateParam(pRequest, "dataInicialI"));
			paramVO.setDataFinal(getDateParam(pRequest, "dataFinalI"));
			paramVO.setCodUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			CalendarioComercialBusiness.getInstance().incluiCalendarioComercial(paramVO);
			setMensagem(pRequest, "Calendário comercial incluído com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);

	}

	/**
	 */
	public ActionForward excluiCalendarioComercial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			PeriodoCalendarioVO paramVO = new PeriodoCalendarioVO();
			paramVO.setPeriodo(getIntegerParam(pRequest, "idPeriodoE"));
			paramVO.setAno(getIntegerParam(pRequest, "anoE"));
			paramVO.setCodUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			CalendarioComercialBusiness.getInstance().excluiCalendarioComercial(paramVO);
			setMensagem(pRequest, "Calendário comercial excluído com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		pRequest.setAttribute("anoSelecionadoF", getIntegerParam(pRequest, "anoSelecionadoF"));

		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);

	}

}