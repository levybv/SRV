package br.com.marisa.srv.meta.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.meta.business.MetaFuncionarioBusiness;
import br.com.marisa.srv.meta.vo.MetaFuncionarioVO;

/**
 * Action para tratar as requisições de Metas de Funcionarios
 * 
 * @author Walter Fontes
 */
public class MetaFuncionarioAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_META_FUNCIONARIO = "metaFuncionario";
		
	
	/**
	 * Realiza a consulta das metas de funcionarios
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
		String  descricaoIndicador  = getStringParam(pRequest, "descricaoIndicadorF");
		Long    idFuncionario 		= getLongParam(pRequest, "idFuncionarioF");
		String  nomeFuncionario     = getStringParam(pRequest, "nomeFuncionarioF");
		
		pRequest.setAttribute("mesF",				 mes);
		pRequest.setAttribute("anoF", 				 ano);
		pRequest.setAttribute("idIndicadorF", 		 idIndicador);
		pRequest.setAttribute("descricaoIndicadorF", descricaoIndicador);
		pRequest.setAttribute("idFuncionarioF", 	 idFuncionario);
		pRequest.setAttribute("nomeFuncionarioF", 	 nomeFuncionario);
		
		if (mes != null || ano != null || idIndicador != null || descricaoIndicador != null || idFuncionario != null || nomeFuncionario != null) {
			List metasFuncionarios = MetaFuncionarioBusiness.getInstance().obtemMetasFuncionarios(mes, ano, idFuncionario, nomeFuncionario, idIndicador, descricaoIndicador);
			pRequest.setAttribute("metasFuncionarios", metasFuncionarios);
		}
		
		return pMapping.findForward(MetaFuncionarioAction.FORWARD_META_FUNCIONARIO);		
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
	public ActionForward alteraMetaFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
		
			MetaFuncionarioVO metaFuncionarioVO = new MetaFuncionarioVO();

			String periodo = getStringParam(pRequest, "idPeriodoMF");
			int posicaoDivisao = periodo.indexOf("-");
			int ano = Integer.parseInt(periodo.substring(0, posicaoDivisao));
			int mes = Integer.parseInt(periodo.substring(posicaoDivisao+1));			
			metaFuncionarioVO.setAno(new Integer(ano));
			metaFuncionarioVO.setMes(new Integer(mes));			
			
			metaFuncionarioVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorMF"));
			metaFuncionarioVO.setIdFuncionario(getLongParam(pRequest, "idFuncionarioMF"));
			metaFuncionarioVO.setIdUnidadeMeta(getIntegerParam(pRequest, "idUnidadeMetaMF"));
			metaFuncionarioVO.setValorMeta(getDoubleParam(pRequest, "valorMetaMF", true));
			metaFuncionarioVO.setDescricaoMeta(getStringParam(pRequest, "descricaoMetaMF"));
			metaFuncionarioVO.setDataAlteracao(new Date());
			metaFuncionarioVO.setIdUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			MetaFuncionarioBusiness.getInstance().alteraMetaFuncionario(metaFuncionarioVO);
			setMensagem(pRequest, "Meta de Funcionário alterada com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(PAGINA_INICIAL);
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
	public ActionForward incluiMetaFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			MetaFuncionarioVO metaFuncionarioVO = new MetaFuncionarioVO();
			
			metaFuncionarioVO.setAno(getIntegerParam(pRequest, "idAnoMF"));
			metaFuncionarioVO.setMes(getIntegerParam(pRequest, "idMesMF"));
			
			metaFuncionarioVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorMF"));
			metaFuncionarioVO.setIdFuncionario(getLongParam(pRequest, "idFuncionarioMF"));
			metaFuncionarioVO.setIdUnidadeMeta(getIntegerParam(pRequest, "idUnidadeMetaMF"));
			metaFuncionarioVO.setValorMeta(getDoubleParam(pRequest, "valorMetaMF", true));
			metaFuncionarioVO.setDescricaoMeta(getStringParam(pRequest, "descricaoMetaMF"));
			metaFuncionarioVO.setDataAlteracao(new Date());
			metaFuncionarioVO.setIdUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());			
			
			MetaFuncionarioBusiness.getInstance().incluiMetaFuncionario(metaFuncionarioVO);
			setMensagem(pRequest, "Meta de Funcionário incluída com sucesso");		
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(PAGINA_INICIAL);
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
	public ActionForward excluiMetaFuncionario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			String periodo = getStringParam(pRequest, "idPeriodoMF");
			int posicaoDivisao = periodo.indexOf("-");
			Integer ano = new Integer(periodo.substring(0, posicaoDivisao));
			Integer mes = new Integer(periodo.substring(posicaoDivisao+1));
			Long idFuncionario = getLongParam(pRequest, "idFuncionarioMF");
			Integer idIndicador = getIntegerParam(pRequest, "idIndicadorMF");

			MetaFuncionarioBusiness.getInstance().excluiMetaFuncionario(mes, ano, idFuncionario, idIndicador);
			setMensagem(pRequest, "Meta de Funcionário excluída com sucesso");		
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(PAGINA_INICIAL);		
	}		
}	