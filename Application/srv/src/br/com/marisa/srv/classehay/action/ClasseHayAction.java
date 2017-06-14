package br.com.marisa.srv.classehay.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.classehay.business.ClasseHayBusiness;
import br.com.marisa.srv.classehay.vo.ClasseHayVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesFuncionalidades;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.processo.action.ProcessoPeriodoAction;

/**
 * Action para tratar as requisições de Classes Hay
 * 
 * @author Walter Fontes
 */
public class ClasseHayAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_CLASSE_HAY = "classeHay";
		
	
	/**
	 * Realiza a consulta das classes Hay
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
		List<Integer> listaAcessos = listaFuncionalidades.get(ConstantesFuncionalidades.FUNCIONALIDADE_CLASSE_HAY);

		pRequest.setAttribute(ConstantesRequestSession.BOTAO_INCLUIR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_CLASSE_HAY_ALTERACAO));
		pRequest.setAttribute(ConstantesRequestSession.BOTAO_ALTERAR, listaAcessos.contains(ConstantesFuncionalidades.TIPO_ACESSO_CLASSE_HAY_ALTERACAO));

		int idClasseHay = -1; 
		if (getIntegerParam(pRequest, "idClasseHayF") != null) {
			idClasseHay = getIntegerParam(pRequest, "idClasseHayF").intValue();
			pRequest.setAttribute("idClasseHayF", String.valueOf(idClasseHay));
		}
		String descricao = getStringParam(pRequest, "descricaoF");
		pRequest.setAttribute("descricaoF", descricao);
		
		List classesHay = ClasseHayBusiness.getInstance().obtemClassesHay(idClasseHay, descricao);
		pRequest.setAttribute("classesHay", classesHay);
		
		return pMapping.findForward(ClasseHayAction.FORWARD_CLASSE_HAY);		
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
	public ActionForward alteraClasseHay(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
		
			ClasseHayVO classeHayVO = new ClasseHayVO();
			classeHayVO.setIdClasseHay(getIntegerParam(pRequest, "idClasseHayCH"));
			classeHayVO.setDescricao(getStringParam(pRequest, "descricaoCH"));
			classeHayVO.setSalarioMinimo(getDoubleParam(pRequest, "salarioMinimoCH", true));
			classeHayVO.setSalarioMaximo(getDoubleParam(pRequest, "salarioMaximoCH", true));
			classeHayVO.setDataUltimaAlteracao(new Date());
			classeHayVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
			
			ClasseHayBusiness.getInstance().alteraClasseHay(classeHayVO);
			setMensagem(pRequest,"Classe Hay alterada com sucesso");
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(ProcessoPeriodoAction.PAGINA_INICIAL);
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
	public ActionForward incluiClasseHay(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
			
			ClasseHayVO classeHayVO = new ClasseHayVO();
			classeHayVO.setIdClasseHay(getIntegerParam(pRequest, "idClasseHayCH"));
			classeHayVO.setDescricao(getStringParam(pRequest, "descricaoCH"));
			classeHayVO.setSalarioMinimo(getDoubleParam(pRequest, "salarioMinimoCH", true));
			classeHayVO.setSalarioMaximo(getDoubleParam(pRequest, "salarioMaximoCH", true));
			classeHayVO.setDataUltimaAlteracao(new Date());
			classeHayVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
			
			ClasseHayBusiness.getInstance().incluiClasseHay(classeHayVO);
			setMensagem(pRequest,"Classe Hay incluída com sucesso");		
			
			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		}
		
		return pMapping.findForward(ProcessoPeriodoAction.PAGINA_INICIAL);
	}	
}