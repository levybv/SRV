package br.com.marisa.srv.escala.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.escala.business.EscalaBusiness;
import br.com.marisa.srv.escala.vo.EscalaVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;

/**
 * Action para tratar as requisições de Escalas
 * 
 * @author Walter Fontes
 */
public class EscalaAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_ESCALA = "escala";
		
	
	/**
	 * Realiza a consulta das escalas
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

		Map<Integer, List<Integer>> listaFuncionalidades = AcessoBusiness.getInstance().obtemAcessoPerfil(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdPerfil());
		List<Integer> listaAcessos = listaFuncionalidades.get(ConstantesFuncionalidades.FUNCIONALIDADE_ESCALA);

		pRequest.setAttribute(ConstantesRequestSession.BOTAO_INCLUIR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_ESCALA_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_ESCALA_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_REMOVER, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_ESCALA_ALTERACAO));

		Integer idEscala   = getIntegerParam(pRequest, "idEscalaF");
		Integer numEscala = getIntegerParam(pRequest, "numEscalaF");
		String  descricao  = getStringParam(pRequest, "descricaoF");
		
		pRequest.setAttribute("idEscalaF",	idEscala);
		pRequest.setAttribute("descricaoF", descricao);
		pRequest.setAttribute("numEscalaF", numEscala);
		
		List escalas = EscalaBusiness.getInstance().obtemEscalas(idEscala, descricao, numEscala);
		pRequest.setAttribute("escalas", escalas);
		
		return pMapping.findForward(EscalaAction.FORWARD_ESCALA);		
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
	public ActionForward alteraEscala(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
		
			EscalaVO escalaVO = new EscalaVO();
			escalaVO.setIdEscala(getIntegerParam(pRequest, "idEscalaES"));
			escalaVO.setDescricaoEscala(getStringParam(pRequest, "descricaoES"));
			escalaVO.setIdGrupoIndicador(getIntegerParam(pRequest, "idGrupoIndicadorES"));
			escalaVO.setIdGrupoRemVar(getIntegerParam(pRequest, "idGrupoRemVarES"));
			escalaVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorES"));
			escalaVO.setPercentual(getBooleanParam(pRequest, "percentualES"));
			escalaVO.setLimite(getDoubleParam(pRequest, "limiteES", true));
			escalaVO.setDataUltimaAlteracao(new Date());
			escalaVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
			escalaVO.setFlgAplicaFxResultNoRealz(getStringParam(pRequest, "aplEscalaRelz"));
			escalaVO.setNumEscala(getIntegerParam(pRequest, "nrEscalaES"));

			EscalaBusiness.getInstance().alteraEscala(escalaVO);
			setMensagem(pRequest, "Escala alterada com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(EscalaAction.PAGINA_INICIAL);		
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
	public ActionForward incluiEscala(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			EscalaVO escalaVO = new EscalaVO();
			escalaVO.setDescricaoEscala(getStringParam(pRequest, "descricaoES"));
			escalaVO.setIdGrupoIndicador(getIntegerParam(pRequest, "idGrupoIndicadorES"));
			escalaVO.setIdGrupoRemVar(getIntegerParam(pRequest, "idGrupoRemVarES"));
			escalaVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorES"));
			escalaVO.setPercentual(getBooleanParam(pRequest, "percentualES"));
			escalaVO.setLimite(getDoubleParam(pRequest, "limiteES", true));
			escalaVO.setDataUltimaAlteracao(new Date());
			escalaVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());		
			escalaVO.setNumEscala(getIntegerParam(pRequest, "nrEscalaES"));
			escalaVO.setFlgAplicaFxResultNoRealz(getStringParam(pRequest, "aplEscalaRelz"));

			EscalaBusiness.getInstance().incluiEscala(escalaVO);
			setMensagem(pRequest, "Escala incluída com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(EscalaAction.PAGINA_INICIAL);		
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
	public ActionForward excluiEscala(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {

			Integer idEscala = getIntegerParam(pRequest, "idEscalaES");
			String retorno = EscalaBusiness.getInstance().excluiEscala(idEscala);
			if (ObjectHelper.isNotEmpty(retorno)) {
				setMensagem(pRequest, retorno);
			} else {
				setMensagem(pRequest, "Escala excluída com sucesso");
			}
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(EscalaAction.PAGINA_INICIAL);		
	}		
}