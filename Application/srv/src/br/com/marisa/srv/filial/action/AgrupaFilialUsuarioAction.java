package br.com.marisa.srv.filial.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.filial.business.AgrupaFilialUsuarioBusiness;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.geral.vo.UsuarioBean;
import br.com.marisa.srv.parametros.business.ParametroBusiness;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

/**
 * Action para tratar as requisições de Agrupamento de Filiais
 * 
 * @author Andre Chierichetti
 */
public class AgrupaFilialUsuarioAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_AGRUPA_FILIAL_REM_VAR = "agrupaFilialRemVar";
		
	
	/**
	 * Realiza a consulta de agrupamentos de filiais
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
	public ActionForward montaTelaPrincipal(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		UsuarioBean usuarioBean = obtemUsuarioDaSessao(pRequest);
		UsuarioVO usuarioVO = usuarioBean.getUsuarioVO();
		Integer matricula = pRequest.getParameter("idFuncionario")!= null?new Integer(pRequest.getParameter("idFuncionario")):null;
		pRequest.setAttribute("matricula", matricula);
		Map <String, String> mapa = ParametroBusiness.getInstance().obtemParametro("AGRUPAMENTO_FILIAIS_REM_VAR", "PRM_COD_FUNC_COD_GRP_REM_VAR");
		List<FuncionarioVO> listaFunc = AgrupaFilialUsuarioBusiness.getInstance().obtemListaFuncionarioByArea(usuarioVO,mapa);
		pRequest.setAttribute("listaFunc", listaFunc);
		String idFuncionarioS = pRequest.getParameter("idFuncionario");
		if(idFuncionarioS != null){
			Integer idFuncionario = new Integer(idFuncionarioS);
			List<FilialVO> listaFilialVinvuladasColaborador = AgrupaFilialUsuarioBusiness.getInstance().obtemListaVinculadasColaborador(usuarioVO, idFuncionario, mapa);
			pRequest.setAttribute("listaFilialVinvuladasColaborador", listaFilialVinvuladasColaborador);
			List<FilialVO> listaFilial = AgrupaFilialUsuarioBusiness.getInstance().obtemListaFilialDisponivel(usuarioVO,mapa,idFuncionario);
			pRequest.setAttribute("filiaisDisponiveis", listaFilial);
		}
		return pMapping.findForward(AgrupaFilialUsuarioAction.FORWARD_AGRUPA_FILIAL_REM_VAR);		
	}
	
	
	/**
	 * Efetiva inclusão
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param request
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward incluiAgrupamentoFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest request, HttpServletResponse pResponse) throws Exception {
		UsuarioVO usuarioVO = obtemUsuarioDaSessao(request).getUsuarioVO();
		String filiais [] = request.getParameterValues("filiaisUtilizadas");
		String idFuncionario = request.getParameter("idFuncionario");
		AgrupaFilialUsuarioBusiness.getInstance().incluiAgrupamentoFilial(filiais,idFuncionario,usuarioVO);
		setMensagem(request, "Agrupamento de filial incluído com sucesso");			
		request.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
		
		return montaTelaPrincipal(pMapping, pForm, request, pResponse);		
	}	
	
	
//	/**
//	 * Efetiva exclusão
//	 * 
//	 * @param pMapping
//	 * @param pForm
//	 * @param pRequest
//	 * @param pResponse
//	 * @return
//	 * @throws Exception
//	 */
//	public ActionForward excluiAgrupamentoFilial(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
//
//		if (ObjectHelper.isNotNull(pRequest.getSession().getAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH))) {
//			
//			Integer idEmpresa 		= getIntegerParam(pRequest, "idEmpresaAF");
//			Integer idFilial 		= getIntegerParam(pRequest, "idFilialAF");
//			Long    idFuncionario 	= getLongParam	 (pRequest, "idFuncionarioAF");
//			
//			String periodo = getStringParam(pRequest, "idPeriodoAF");
//			int posicaoDivisao = periodo.indexOf("-");
//			Integer ano = new Integer(periodo.substring(0, posicaoDivisao));
//			Integer mes = new Integer(periodo.substring(posicaoDivisao+1));	
//
//			SalarioBaseBusiness.getInstance().excluiSalarioBase(idEmpresa, idFilial, idFuncionario, ano, mes);
//			setMensagem(pRequest, "Agrupamento de filial excluído com sucesso");
//			
//			pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_TOKEN_REFRESH);
//		}
//		
//		return pMapping.findForward(AgrupaFilialUsuarioAction.PAGINA_INICIAL);		
//	}		
}