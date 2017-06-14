package br.com.marisa.srv.perfil.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.vo.UsuarioBean;
import br.com.marisa.srv.perfil.business.PerfilBusiness;
import br.com.marisa.srv.perfil.vo.PerfilVO;

/**
 * Action para tratar as requisições de usuários
 * 
 * 
 */
public class PerfilAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_PERFIL = "perfil";

	/**
	 * Realiza a consulta dos usuarios
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest request, HttpServletResponse pResponse) throws Exception {
		montaTelaInicial(request);
		return pMapping.findForward(PerfilAction.FORWARD_PERFIL);
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
	public ActionForward alterarPerfil(ActionMapping pMapping, ActionForm pForm, HttpServletRequest request, HttpServletResponse pResponse) throws Exception {
		UsuarioBean usuarioBean = obtemUsuarioDaSessao(request);

		PerfilVO perfilVO = new PerfilVO();
		perfilVO.setDescricao(request.getParameter("descricaoL"));
		perfilVO.setIsAtivo(getBooleanParam(request, "checkAtivo"));
		perfilVO.setIdPerfil(new Integer(request.getParameter("codPerfilL1")));
		perfilVO.setIsExibeBonus(getBooleanParam(request, "checkExibeBonus"));
		perfilVO.setIsValidaFaixaEscala(getBooleanParam(request, "checkFaixaEscala"));
		perfilVO.setIsReabreResultado(getBooleanParam(request, "checkReabreBonus"));

        String[] acessos = request.getParameterValues("acessos");
		PerfilBusiness.getInstance().atualizarPerfil(perfilVO, usuarioBean.getUsuarioVO().getIdUsuario(), acessos );
		setMensagem(request,"Perfil alterado com sucesso");
		
		return pMapping.findForward(PerfilAction.PAGINA_INICIAL);
	}

	/**
	 * 
	 * @param request
	 * @throws SRVException
	 */
	private void montaTelaInicial(HttpServletRequest request) throws SRVException {
		PerfilVO pesquisaVO = new PerfilVO();
		pesquisaVO.setIdPerfil(getIntegerParam(request, "codigo"));
		pesquisaVO.setDescricao(getStringParam(request, "descricao"));
		if (getStringParam(request, "ativo") != null) {
			pesquisaVO.setIsAtivo(getBooleanParam(request, "ativo"));
		}
		if (getStringParam(request, "acessoResultadoBonus") != null) {
			pesquisaVO.setIsExibeBonus(getBooleanParam(request, "acessoResultadoBonus"));
		}
		if (getStringParam(request, "reabreResultadoBonus") != null) {
			pesquisaVO.setIsReabreResultado(getBooleanParam(request, "reabreResultadoBonus"));
		}
		if (getStringParam(request, "validaVlrFxEscala") != null) {
			pesquisaVO.setIsValidaFaixaEscala(getBooleanParam(request, "validaVlrFxEscala"));
		}

		request.setAttribute("listaPerfis", PerfilBusiness.getInstance().obtemListaPerfil(pesquisaVO));
		request.setAttribute("pesquisaPerfilVO",  pesquisaVO);
	}

}