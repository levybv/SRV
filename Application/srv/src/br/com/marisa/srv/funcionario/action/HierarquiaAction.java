package br.com.marisa.srv.funcionario.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.filial.business.AgrupaFilialUsuarioBusiness;
import br.com.marisa.srv.funcionario.business.HierarquiaBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

/**
 * Action para tratar as requisições de Agrupamento de Filiais
 * 
 * @author Andre Chierichetti
 */
public class HierarquiaAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_HIERARQUIA = "paginaInicial";
		
	
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
		pRequest.setAttribute("repete", pRequest.getParameter("repete"));
		pRequest.setAttribute("grp", pRequest.getParameter("grp"));
		pRequest.setAttribute("grpLider", pRequest.getParameter("grpLider"));
		Integer grupo = new Integer(pRequest.getParameter("grp"));
		Integer grpLider = new Integer(pRequest.getParameter("grpLider"));
		List<FuncionarioVO> listaLideres = HierarquiaBusiness.getInstance().obtemListaFuncionariosByIdGrupo(grpLider);
		pRequest.setAttribute("listaLideres", listaLideres);
		pRequest.setAttribute("idFuncionario", pRequest.getParameter("idFuncionario"));
		String idFuncionarioS = getStringParam(pRequest, "idFuncionario");
		if(idFuncionarioS != null){
			Long idFuncionario = new Long(idFuncionarioS);
			List<FuncionarioVO> listaSubordinados = HierarquiaBusiness.getInstance().obtemListaSubordinados(idFuncionario, grupo);
			pRequest.setAttribute("listaSubordinados", listaSubordinados);
			List<FuncionarioVO> listaColaboradoresDisponiveis = HierarquiaBusiness.getInstance().obtemListaColaboradoresDisponiveis("S".equals(pRequest.getParameter("repete")),listaSubordinados,grupo);
			pRequest.setAttribute("listaColaboradoresDisponiveis", listaColaboradoresDisponiveis);
		}
		return pMapping.findForward(HierarquiaAction.FORWARD_HIERARQUIA);		
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
	public ActionForward vinculaSubordinadosLider(ActionMapping pMapping, ActionForm pForm, HttpServletRequest request, HttpServletResponse pResponse) throws Exception {
		UsuarioVO usuarioVO = obtemUsuarioDaSessao(request).getUsuarioVO();
		String funcVinculados [] = request.getParameterValues("funcionariosVinculados");
		String idFuncionario = request.getParameter("idFuncionario");
		HierarquiaBusiness.getInstance().vinculaSubordinadosLider(funcVinculados,idFuncionario,usuarioVO,request.getParameter("grp"));
		setMensagem(request, "Agrupamento atualizado com sucesso");			
		return montaTelaPrincipal(pMapping, pForm, request, pResponse);		
	}	
	

}