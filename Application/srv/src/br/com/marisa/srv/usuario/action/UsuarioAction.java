package br.com.marisa.srv.usuario.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.perfil.business.PerfilBusiness;
import br.com.marisa.srv.perfil.vo.PerfilVO;
import br.com.marisa.srv.usuario.business.UsuarioBusiness;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

/**
 * Action para tratar as requisições de usuários
 * 
 * @author Walter Fontes
 */
public class UsuarioAction extends BasicAction {

	/**
	 * Possiveis destinos
	 */
	private static final String FORWARD_USUARIO = "usuario";
	
	
	
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
	public ActionForward inicio(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		montaTelaInicial(pRequest);
		return pMapping.findForward(FORWARD_USUARIO);
	}
	
	/**
	 * 
	 * @param pRequest
	 * @throws SRVException
	 */
	private void montaTelaInicial(HttpServletRequest pRequest) throws SRVException {

		UsuarioVO pesquisaVO = new UsuarioVO();
		pesquisaVO.setNome(getStringParam(pRequest, "nome"));
		pesquisaVO.setIdUsuario(getIntegerParam(pRequest, "codigo"));
		pesquisaVO.setMatricula(getStringParam(pRequest, "matricula"));
		pesquisaVO.setLogin(getStringParam(pRequest, "login"));
		pesquisaVO.setIdPerfil(getIntegerParam(pRequest, "perfil"));
		String ativo = getStringParam(pRequest, "ativo");
		if (ativo != null) {
			pesquisaVO.setAtivo(getBooleanParam(pRequest, "ativo"));
		}
		String autenticaAD = getStringParam(pRequest, "autenticaAD");
		if (autenticaAD != null) {
			pesquisaVO.setAutenticaAD(getBooleanParam(pRequest, "autenticaAD"));
		}

		List<UsuarioVO> usuarios = UsuarioBusiness.getInstance().obtemUsuarios(pesquisaVO);
		List<PerfilVO> listaPerfil = PerfilBusiness.getInstance().obtemListaPerfisByDescricao(null);
		
		pRequest.setAttribute("usuarios", usuarios);
		pRequest.setAttribute("nome", pesquisaVO.getNome());
		pRequest.setAttribute("codigo", pesquisaVO.getIdUsuario());
		pRequest.setAttribute("matricula", pesquisaVO.getMatricula());
		pRequest.setAttribute("login", pesquisaVO.getLogin());
		pRequest.setAttribute("ativo", pesquisaVO.getAtivo());
		pRequest.setAttribute("autenticaAD", pesquisaVO.getAutenticaAD());
		pRequest.setAttribute("listaPerfil", listaPerfil);
		pRequest.setAttribute("perfil", pesquisaVO.getIdPerfil());
		
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
	public ActionForward alteraUsuario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		UsuarioVO usuarioVO = new UsuarioVO();
		usuarioVO.setIdUsuario(getIntegerParam(pRequest, "idUsuarioM"));
		usuarioVO.setNome(getStringParam(pRequest, "nomeM"));
		usuarioVO.setMatricula(getStringParam(pRequest, "matriculaM"));
		usuarioVO.setAtivo(getBooleanParam(pRequest, "ativoM"));
		usuarioVO.setAutenticaAD(getBooleanParam(pRequest, "autenticaAdM"));
		usuarioVO.setLogin(getStringParam(pRequest, "loginM"));
		usuarioVO.setIdPerfil(getIntegerParam(pRequest, "perfilM"));
		usuarioVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
		
		boolean reiniciarSenha = getBoolParam(pRequest, "reiniciaSenhaM");
		
		UsuarioBusiness.getInstance().alteraUsuario(usuarioVO, reiniciarSenha);
		setMensagem(pRequest,"Usuário alterado com sucesso");
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
	public ActionForward incluiUsuario(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {

		UsuarioVO usuarioVO = new UsuarioVO();
		usuarioVO.setNome(getStringParam(pRequest, "nomeM"));
		usuarioVO.setMatricula(getStringParam(pRequest, "matriculaM"));
		usuarioVO.setAtivo(getBooleanParam(pRequest, "ativoM"));
		usuarioVO.setAutenticaAD(getBooleanParam(pRequest, "autenticaAdM"));
		usuarioVO.setLogin(getStringParam(pRequest, "loginM"));
		usuarioVO.setIdPerfil(getIntegerParam(pRequest, "perfilM"));
		usuarioVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(pRequest).getUsuarioVO().getIdUsuario());
		
		UsuarioBusiness.getInstance().incluiUsuario(usuarioVO);
		setMensagem(pRequest,"Usuário incluído com sucesso");
		
		return pMapping.findForward(PAGINA_INICIAL);
	}	
}