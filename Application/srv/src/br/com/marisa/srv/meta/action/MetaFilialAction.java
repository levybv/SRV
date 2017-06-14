package br.com.marisa.srv.meta.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.meta.business.MetaFilialBusiness;
import br.com.marisa.srv.meta.vo.MetaFilialVO;

/**
 * Action para tratar as requisições de Metas de Filiais
 * 
 * @author Walter Fontes
 */
public class MetaFilialAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_META_FILIAL = "metaFilial";
		
	
	/**
	 * Realiza a consulta das metas de filiais
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
		Integer idFilial 			= getIntegerParam(pRequest, "idFilialF");
		
		pRequest.setAttribute("mesF",				 mes);
		pRequest.setAttribute("anoF", 				 ano);
		pRequest.setAttribute("idIndicadorF", 		 idIndicador);
		pRequest.setAttribute("descricaoIndicadorF", descricaoIndicador);
		pRequest.setAttribute("idFilialF", 			 idFilial);
		
		if (mes != null || ano != null || idIndicador != null || descricaoIndicador != null || idFilial != null) {
			List metasFiliais = MetaFilialBusiness.getInstance().obtemMetasFiliais(mes, ano, idFilial, idIndicador, descricaoIndicador);
			pRequest.setAttribute("metasFiliais", metasFiliais);
		}
		
		return pMapping.findForward(MetaFilialAction.FORWARD_META_FILIAL);		
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
	public ActionForward alteraMetaFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
		
			MetaFilialVO metaFilialVO = new MetaFilialVO();
			String periodo = getStringParam(pRequest, "idPeriodoMF");
			int posicaoDivisao = periodo.indexOf("-");
			int ano = Integer.parseInt(periodo.substring(0, posicaoDivisao));
			int mes = Integer.parseInt(periodo.substring(posicaoDivisao+1));			
			metaFilialVO.setAno(new Integer(ano));
			metaFilialVO.setMes(new Integer(mes));
			metaFilialVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorMF"));
			metaFilialVO.setIdEmpresa(new Integer(Constantes.CODIGO_EMPRESA));
			metaFilialVO.setIdFilial(getIntegerParam(pRequest, "idFilialMF"));
			metaFilialVO.setIdUnidadeMeta(getIntegerParam(pRequest, "idUnidadeMetaMF"));
			metaFilialVO.setValorMeta(getDoubleParam(pRequest, "valorMetaMF", true));
			metaFilialVO.setValorPremioFilial(getDoubleParam(pRequest, "valorPremioFilialMF", true));
			metaFilialVO.setDataAlteracao(new Date());
			metaFilialVO.setIdUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());

			MetaFilialBusiness.getInstance().alteraMetaFilial(metaFilialVO);
			setMensagem(pRequest, "Meta de Filial alterada com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(MetaFilialAction.PAGINA_INICIAL);		
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
	public ActionForward incluiMetaFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			MetaFilialVO metaFilialVO = new MetaFilialVO();
			metaFilialVO.setAno(getIntegerParam(pRequest, "idAnoMF"));
			metaFilialVO.setMes(getIntegerParam(pRequest, "idMesMF"));
			metaFilialVO.setIdIndicador(getIntegerParam(pRequest, "idIndicadorMF"));
			metaFilialVO.setIdEmpresa(new Integer(Constantes.CODIGO_EMPRESA));
			metaFilialVO.setIdFilial(getIntegerParam(pRequest, "idFilialMF"));
			metaFilialVO.setIdUnidadeMeta(getIntegerParam(pRequest, "idUnidadeMetaMF"));
			metaFilialVO.setValorMeta(getDoubleParam(pRequest, "valorMetaMF", true));
			metaFilialVO.setValorPremioFilial(getDoubleParam(pRequest, "valorPremioFilialMF", true));
			metaFilialVO.setDataAlteracao(new Date());
			metaFilialVO.setIdUsuario(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());			
			
			MetaFilialBusiness.getInstance().incluiMetaFilial(metaFilialVO);
			setMensagem(pRequest, "Meta de Filial incluída com sucesso");		
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(MetaFilialAction.PAGINA_INICIAL);		
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
	public ActionForward excluiMetaFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			String periodo = getStringParam(pRequest, "idPeriodoMF");
			int posicaoDivisao = periodo.indexOf("-");
			Integer ano = new Integer(periodo.substring(0, posicaoDivisao));
			Integer mes = new Integer(periodo.substring(posicaoDivisao+1));
			Integer idEmpresa = new Integer(Constantes.CODIGO_EMPRESA);
			Integer idFilial = getIntegerParam(pRequest, "idFilialMF");
			Integer idIndicador = getIntegerParam(pRequest, "idIndicadorMF");

			MetaFilialBusiness.getInstance().excluiMetaFilial(mes, ano, idEmpresa, idFilial, idIndicador);
			setMensagem(pRequest, "Meta de Filial excluída com sucesso");		
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(MetaFilialAction.PAGINA_INICIAL);		
	}		
}