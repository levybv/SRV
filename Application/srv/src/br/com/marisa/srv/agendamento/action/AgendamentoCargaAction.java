package br.com.marisa.srv.agendamento.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.agendamento.business.AgendamentoBusiness;
import br.com.marisa.srv.agendamento.vo.AgendamentoVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;

/**
 * @author Levy Villar
 */
public class AgendamentoCargaAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_AGENDAMENTO = "agendamento";
		
	
	/**
	 * Realiza a consulta
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

		AgendamentoVO pesquisaVO = new AgendamentoVO();
		pesquisaVO.setCodigoCarga(getIntegerParam(pRequest, "codigoCargaF"));
		pesquisaVO.setDescricaoCarga(getStringParam(pRequest, "descricaoCargaF"));
		pesquisaVO.setDataAgendamento(getDateParam(pRequest, "dataAgendamentoF"));
		pesquisaVO.setDataUltimoProcessamento(getDateParam(pRequest, "dataUltimoProcessamentoF"));
		pesquisaVO.setMes(getIntegerParam(pRequest, "mesF"));
		pesquisaVO.setAno(getIntegerParam(pRequest, "anoF"));
		pesquisaVO.setFlagAtiva(getIntegerParam(pRequest, "flagAtivaF"));

		List<AgendamentoVO> listaAgendamento = AgendamentoBusiness.getInstance().obtemAgendamento(pesquisaVO);

		if ( obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil().intValue() == 1 ) {
			pRequest.setAttribute("exibeCargaArquivos", "true");
		} else {
			pRequest.removeAttribute("exibeCargaArquivos");
		}

		pRequest.setAttribute("listaAgendamento", listaAgendamento);
		pRequest.setAttribute("filtroAgendamento", pesquisaVO);

		return pMapping.findForward(AgendamentoCargaAction.FORWARD_AGENDAMENTO);		
	}
	

	/**
	 * Efetiva alteração
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward alteraAgendamento(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
		
			AgendamentoVO paramVO = new AgendamentoVO();
			paramVO.setCodigoCarga(getIntegerParam(pRequest, "codigoCargaA"));
			paramVO.setDataAgendamento(getDateParam(pRequest, "dataAgendamentoA"));
			paramVO.setMes(getIntegerParam(pRequest, "mesA"));
			paramVO.setAno(getIntegerParam(pRequest, "anoA"));
			paramVO.setFlagAtiva(getIntegerParam(pRequest, "flagAtivaA"));

			AgendamentoBusiness.getInstance().alteraAgendamento(paramVO);
			setMensagem(pRequest, "Agendamento de carga alterado com sucesso.");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(AgendamentoCargaAction.PAGINA_INICIAL);
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
	public ActionForward reprocessaCargaArquivos(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		AgendamentoBusiness.getInstance().reprocessaCargaArquivos();
		pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);

		return pMapping.findForward(AgendamentoCargaAction.PAGINA_INICIAL);
	}

}