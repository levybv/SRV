package br.com.marisa.srv.processo.action;

import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.agendamento.business.AgendamentoBusiness;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.enumeration.StatusProcessoPeriodoEnum;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.ponderacao.business.PonderacaoBusiness;
import br.com.marisa.srv.processo.business.ProcessoPeriodoBusiness;
import br.com.marisa.srv.processo.vo.ProcessoPeriodoVO;

/**
 * Action para tratar as requisições de Processos por Períodos
 * 
 * @author Walter Fontes
 */
public class ProcessoPeriodoAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_PROCESSO_PERIODO = "processoPeriodo";
	private static final String FORWARD_PROCESSO_GERACAO = "geraProcesso";
		
	
	/**
	 * Realiza a consulta dos processos por período
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

		Integer ano = getIntegerParam(pRequest, "anoF");
		Integer mes = getIntegerParam(pRequest, "mesF");

		List processosPeriodo = ProcessoPeriodoBusiness.getInstance().obtemProcessosPeriodo(ano, mes);

		if ( obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil().intValue() == 1 ) {
			pRequest.setAttribute("exibeReprocessar", "true");
		} else {
			pRequest.removeAttribute("exibeReprocessar");
		}

		pRequest.setAttribute("processosPeriodo", processosPeriodo);
		pRequest.setAttribute("anoF", ano);
		pRequest.setAttribute("mesF", mes);

		return pMapping.findForward(ProcessoPeriodoAction.FORWARD_PROCESSO_PERIODO);		
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
	public ActionForward alteraProcessosPeriodo(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {

			Integer status = getIntegerParam(pRequest, "statusAlt");
			String itensSel = getStringParam(pRequest, "chavesAlt");

			StringTokenizer stAll = new StringTokenizer(itensSel,";");
			while ( stAll.hasMoreTokens() ) {
				StringTokenizer item = new StringTokenizer(stAll.nextToken(),",");
				while ( item.hasMoreTokens() ) {
					ProcessoPeriodoVO processoPeriodoVO = new ProcessoPeriodoVO();
					processoPeriodoVO.setIdProcesso(new Integer(item.nextToken()));
					processoPeriodoVO.setMes(new Integer(item.nextToken()));
					processoPeriodoVO.setAno(new Integer(item.nextToken()));
					processoPeriodoVO.setStatus(status);
					processoPeriodoVO.setDataUltimaAlteracao(new Date());
					processoPeriodoVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
					ProcessoPeriodoBusiness.getInstance().alteraProcessoPeriodo(processoPeriodoVO);
				}
			}

			setMensagem(pRequest, "Período alterado com sucesso");

			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		Integer ano = getIntegerParam(pRequest, "anoFiltro");
		Integer mes = getIntegerParam(pRequest, "mesFiltro");

		List processosPeriodo = ProcessoPeriodoBusiness.getInstance().obtemProcessosPeriodo(ano, mes);

		pRequest.setAttribute("processosPeriodo", processosPeriodo);
		pRequest.setAttribute("anoF", ano);
		pRequest.setAttribute("mesF", mes);

		return pMapping.findForward(ProcessoPeriodoAction.FORWARD_PROCESSO_PERIODO);		
	}

	
	/**
	 * Efetiva inclusão
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward incluiProcessosPeriodo(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			ProcessoPeriodoVO processoPeriodoVO = new ProcessoPeriodoVO();
			Integer idProcesso = getIntegerParam(pRequest, "idProcessoPP");
			if (idProcesso != null && idProcesso.intValue() != -1) {
				processoPeriodoVO.setIdProcesso(getIntegerParam(pRequest, "idProcessoPP"));
			}
			processoPeriodoVO.setAno(getIntegerParam(pRequest, "anoPP"));
			processoPeriodoVO.setMes(getIntegerParam(pRequest, "mesPP"));
			processoPeriodoVO.setStatus(StatusProcessoPeriodoEnum.ABERTO.getCodigo());
			processoPeriodoVO.setDataUltimaAlteracao(new Date());
			processoPeriodoVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			ProcessoPeriodoBusiness.getInstance().incluiProcessoPeriodo(processoPeriodoVO);
			setMensagem(pRequest, "Período incluído com sucesso");			
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}

		Integer ano = getIntegerParam(pRequest, "anoFiltro");
		Integer mes = getIntegerParam(pRequest, "mesFiltro");

		List processosPeriodo = ProcessoPeriodoBusiness.getInstance().obtemProcessosPeriodo(ano, mes);

		pRequest.setAttribute("processosPeriodo", processosPeriodo);
		pRequest.setAttribute("anoF", ano);
		pRequest.setAttribute("mesF", mes);

		return pMapping.findForward(ProcessoPeriodoAction.FORWARD_PROCESSO_PERIODO);		
	}	
	
	
	/**
	 * Efetiva exclusão
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward excluiPonderacao(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			Integer idPonderacao = getIntegerParam(pRequest, "idPonderacaoP");

			PonderacaoBusiness.getInstance().excluiPonderacao(idPonderacao);
			setMensagem(pRequest, "Ponderação excluída com sucesso");		
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(ProcessoPeriodoAction.PAGINA_INICIAL);
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
	public ActionForward reprocessaPeriodo(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		AgendamentoBusiness.getInstance().reprocessaAgendamento();
		pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);

		return pMapping.findForward(ProcessoPeriodoAction.PAGINA_INICIAL);
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
	public ActionForward inicioGeraProcesso(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH, Boolean.TRUE);
		
		return pMapping.findForward(ProcessoPeriodoAction.FORWARD_PROCESSO_GERACAO);
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
	public ActionForward geraProcesso(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		ProcessoPeriodoBusiness.getInstance().geraProcesso(null, getStringParam(pRequest, "param1"));
		setMensagem(pRequest, "Processo gerado com sucesso!");
		return pMapping.findForward(ProcessoPeriodoAction.FORWARD_PROCESSO_GERACAO);
	}

}