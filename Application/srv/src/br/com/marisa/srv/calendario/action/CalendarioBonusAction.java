package br.com.marisa.srv.calendario.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.calendario.business.CalendarioBonusBusiness;
import br.com.marisa.srv.calendario.vo.CalendarioBonusVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;

/**
 */
public class CalendarioBonusAction extends BasicAction {

	/**
	 */
	private static final String FORWARD_CALENDARIO = "calendarioBonus";

	
	/**
	 */
	public CalendarioBonusAction() throws Exception {}


	/**
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}
	
	/**
	 */
	public ActionForward montaTelaPrincipal(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		Map<Integer, List<Integer>> listaFuncionalidades = AcessoBusiness.getInstance().obtemAcessoPerfil(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil());
		List<Integer> listaAcessos = listaFuncionalidades.get(ConstantesFuncionalidades.FUNCIONALIDADE_CALENDARIO_BONUS);

		pRequest.setAttribute(ConstantesRequestSession.BOTAO_INCLUIR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_CALENDARIO_BONUS_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_CALENDARIO_BONUS_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_REMOVER, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_CALENDARIO_BONUS_ALTERACAO));

		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);

		CalendarioBonusVO filtroVO = new CalendarioBonusVO();
		filtroVO.setAno(getIntegerParam(pRequest, "anoF"));
		filtroVO.setMes(getIntegerParam(pRequest, "mesF"));
		filtroVO.setDescricaoMes(getStringParam(pRequest, "mesDescrF"));
		filtroVO.setDataInicial(getDateParam(pRequest, "dataInicialF"));
		filtroVO.setDataFinal(getDateParam(pRequest, "dataFinalF"));
		List<CalendarioBonusVO> lista = CalendarioBonusBusiness.getInstance().obtemCalendarioBonus(filtroVO);

		pRequest.setAttribute("listaCalendarioBonus", lista);
		pRequest.setAttribute("calendarioBonusFiltro", filtroVO);
		pRequest.setAttribute("listaMesCalendarioBonus", CalendarioBonusBusiness.getInstance().obtemMesesCalendarioBonus());
		pRequest.setAttribute("listaAnosCalendarioBonus", CalendarioBonusBusiness.getInstance().obtemAnosCalendarioBonus());

		return pMapping.findForward(CalendarioBonusAction.FORWARD_CALENDARIO);
	}

	/**
	 */
	public ActionForward alteraCalendarioBonus(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			CalendarioBonusVO paramVO = new CalendarioBonusVO();
			paramVO.setMes(getIntegerParam(pRequest, "mesA"));
			paramVO.setAno(getIntegerParam(pRequest, "anoA"));
			paramVO.setDataInicial(getDateParam(pRequest, "dataInicialA"));
			paramVO.setDataFinal(getDateParam(pRequest, "dataFinalA"));
			paramVO.setDescricaoMes(getStringParam(pRequest, "mesDescrA"));
			paramVO.setCodUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			CalendarioBonusBusiness.getInstance().alteraCalendarioBonus(paramVO);
			setMensagem(pRequest, "Calendário de bônus alterado com sucesso.");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);

		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}

	/**
	 */
	public ActionForward incluiCalendarioBonus(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			CalendarioBonusVO paramVO = new CalendarioBonusVO();
			paramVO.setMes(getIntegerParam(pRequest, "mesI"));
			paramVO.setAno(getIntegerParam(pRequest, "anoI"));
			paramVO.setDataInicial(getDateParam(pRequest, "dataInicialI"));
			paramVO.setDataFinal(getDateParam(pRequest, "dataFinalI"));
			paramVO.setDescricaoMes(getStringParam(pRequest, "mesDescrI"));
			paramVO.setCodUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			CalendarioBonusBusiness.getInstance().incluiCalendarioBonus(paramVO);
			setMensagem(pRequest, "Calendário de bônus incluído com sucesso.");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);

		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}

	/**
	 */
	public ActionForward excluiCalendarioBonus(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			CalendarioBonusVO paramVO = new CalendarioBonusVO();
			paramVO.setMes(getIntegerParam(pRequest, "mesE"));
			paramVO.setAno(getIntegerParam(pRequest, "anoE"));
			paramVO.setCodUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			CalendarioBonusBusiness.getInstance().excluiCalendarioBonus(paramVO);
			setMensagem(pRequest, "Calendário de bônus excluído com sucesso.");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}

}