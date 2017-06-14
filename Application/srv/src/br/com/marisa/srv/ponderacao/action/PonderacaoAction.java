package br.com.marisa.srv.ponderacao.action;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.cargo.business.CargoBusiness;
import br.com.marisa.srv.filial.business.FilialBusiness;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.gpremuneracaovariavel.business.GrupoRemuneracaoVariavelBusiness;
import br.com.marisa.srv.grupoindicador.business.GrupoIndicadorBusiness;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.ponderacao.business.PonderacaoBusiness;
import br.com.marisa.srv.ponderacao.vo.PonderacaoVO;
import br.com.marisa.srv.unidade.business.UnidadeBusiness;

/**
 * Action para tratar as requisições de Ponderações
 * 
 * @author Walter Fontes
 */
public class PonderacaoAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_PONDERACAO = "ponderacao";
	private static final String FORWARD_INCLUSAO   = "inclusao";

	/**
	 * Realiza a consulta das ponderações
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

		pRequest.getSession().removeAttribute("grupoRemVarS");
		pRequest.getSession().removeAttribute("cargoS");
		pRequest.getSession().removeAttribute("grupoIndicadorS");
		pRequest.getSession().removeAttribute("indicadorS");
		pRequest.getSession().removeAttribute("tipoFilialS");
		pRequest.getSession().removeAttribute("filialS");

		return montaTelaPrincipal(pMapping, pForm, pRequest, pResponse);
	}
	
	public ActionForward inclusao(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
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
		
		Integer idGrupoRemVar 		= getIntegerParam(pRequest, "idGrupoRemVarF");
		Integer idCargo				= getIntegerParam(pRequest, "idCargoF");
		Integer idGrupoIndicador 	= getIntegerParam(pRequest, "idGrupoIndicadorF");
		Integer idIndicador 		= getIntegerParam(pRequest, "idIndicadorF");
		Integer idTipoFilial 		= getIntegerParam(pRequest, "idDescrTipoFiliaisF");
		Integer idFilial 			= getIntegerParam(pRequest, "idDescrFiliaisF");
		
		pRequest.setAttribute("idGrupoRemVarF",			idGrupoRemVar);
		pRequest.setAttribute("idCargoF", 				idCargo);
		pRequest.setAttribute("idGrupoIndicadorF",		idGrupoIndicador);
		pRequest.setAttribute("idIndicadorF", 			idIndicador);
		pRequest.setAttribute("idDescrTipoFiliaisF",	idTipoFilial);
		pRequest.setAttribute("idDescrFiliaisF", 		idFilial);
		pRequest.setAttribute("rdoRemuneracaoF", 		getStringParam(pRequest, "rdoRemuneracao"));
		pRequest.setAttribute("rdoIndicadorF", 			getStringParam(pRequest, "rdoIndicador"));
		pRequest.setAttribute("rdoFilialF", 			getStringParam(pRequest, "rdoFilial"));

		pRequest.setAttribute("ponderacoes", PonderacaoBusiness.getInstance().obtemPonderacoes(idIndicador, idGrupoIndicador, idGrupoRemVar, idCargo, idTipoFilial, idFilial));

		pRequest.getSession().setAttribute("listGrupoRemVar", GrupoRemuneracaoVariavelBusiness.getInstance().obtemGruposRemuneracao(-1, null));
		pRequest.getSession().setAttribute("listCargos", CargoBusiness.getInstance().obtemListaCargo(null));
		pRequest.getSession().setAttribute("listGrupoIndicadores", GrupoIndicadorBusiness.getInstance().obtemGruposIndicadores());
		pRequest.getSession().setAttribute("listTipoFiliais", FilialBusiness.getInstance().obterListaTipoFiliais());
		pRequest.getSession().setAttribute("listFiliais", FilialBusiness.getInstance().obterTodasFiliais());
		pRequest.getSession().setAttribute("listIndicadores", IndicadorBusiness.getInstance().obtemListaIndicadores(null));
		pRequest.getSession().setAttribute("listUnidades", UnidadeBusiness.getInstance().obtemUnidades());

		return pMapping.findForward(PonderacaoAction.FORWARD_PONDERACAO);		
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
	public ActionForward alteraPonderacao(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
		
			PonderacaoVO ponderacaoVO = new PonderacaoVO();
			ponderacaoVO.setIdGrupoRemVar(getIntegerParam(pRequest, "idGrupoRemVarP"));
			ponderacaoVO.setIdCargo(getIntegerParam(pRequest, "idCargoP"));
			ponderacaoVO.setIdGrupoIndicador(getIntegerParam(pRequest, "idGrupoIndicadorP"));
			ponderacaoVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorP"));
			ponderacaoVO.setPeso(getDoubleParam(pRequest, "pesoP", true));
			ponderacaoVO.setUnidadePeso(getIntegerParam(pRequest, "idUnidadePesoP"));
			ponderacaoVO.setValorPremio(getDoubleParam(pRequest, "valorPremioP", true));
			ponderacaoVO.setDataUltimaAlteracao(new Date());
			ponderacaoVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
			ponderacaoVO.setIdFilial(getIntegerParam(pRequest, "idFilialH"));
			ponderacaoVO.setIdTipoFilial(getIntegerParam(pRequest, "idTipoFilialH"));
			ponderacaoVO.setIdPonderacao(getIntegerParam(pRequest, "idPonderacaoK"));

			PonderacaoBusiness.getInstance().alteraPonderacao(ponderacaoVO);
			setMensagem(pRequest, "Ponderação alterada com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(PonderacaoAction.PAGINA_INICIAL);		
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
	public ActionForward incluiPonderacao(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			PonderacaoVO ponderacaoVO = new PonderacaoVO();
			ponderacaoVO.setIdFilial(getIntegerParam(pRequest, "idFilialH"));
			ponderacaoVO.setIdTipoFilial(getIntegerParam(pRequest, "idTipoFilialH"));
			ponderacaoVO.setIdGrupoRemVar(getIntegerParam(pRequest, "idGrupoRemVarP"));
			ponderacaoVO.setIdCargo(getIntegerParam(pRequest, "idCargoP"));
			ponderacaoVO.setIdGrupoIndicador(getIntegerParam(pRequest, "idGrupoIndicadorP"));
			ponderacaoVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorP"));
			ponderacaoVO.setUnidadePeso(getIntegerParam(pRequest, "idUnidadePesoP"));
			ponderacaoVO.setPeso(getDoubleParam(pRequest, "pesoP", true));
			ponderacaoVO.setValorPremio(getDoubleParam(pRequest, "valorPremioP", true));
			ponderacaoVO.setDataUltimaAlteracao(new Date());
			ponderacaoVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());			
			
			PonderacaoBusiness.getInstance().incluiPonderacao(ponderacaoVO);
			setMensagem(pRequest, "Ponderação incluída com sucesso");

			pRequest.getSession().setAttribute("grupoRemVarS", pRequest.getParameter("idGrupoRemVarP"));
			pRequest.getSession().setAttribute("cargoS", pRequest.getParameter("idCargoP"));
			pRequest.getSession().setAttribute("grupoIndicadorS", pRequest.getParameter("idGrupoIndicadorP"));
			pRequest.getSession().setAttribute("indicadorS", pRequest.getParameter("idIndicadorP"));
			pRequest.getSession().setAttribute("tipoFilialS", pRequest.getParameter("idTipoFilialH"));
			pRequest.getSession().setAttribute("filialS", pRequest.getParameter("idFilialH"));

			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
			getSession(pRequest).setAttribute("exibeInclusao", "true");
		}
		
		return pMapping.findForward(PonderacaoAction.FORWARD_INCLUSAO);
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
		
		return pMapping.findForward(PonderacaoAction.PAGINA_INICIAL);		
	}

}