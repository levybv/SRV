package br.com.marisa.srv.login.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.geral.action.BasicAction;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.excecoes.AutenticacaoException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.util.MenuBuild;
import br.com.marisa.srv.geral.vo.UsuarioBean;
import br.com.marisa.srv.usuario.business.UsuarioBusiness;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

/**
 * Data de Criação: 04/08/2011
 * 
 * @author Walter Fontes
 */
public class LoginAction extends BasicAction {

	/**
	 */
	private static final String FORWARD_INICIO 			= "inicio";
	private static final String FORWARD_LOGIN 			= "login";
	private static final String FORWARD_ALTERACAO_SENHA = "alteracaoSenha";

	
	/**
	 * Cria um novo objeto PrincipalAction
	 * 
	 * @throws ExcecaoParametroInvalido
	 */
	public LoginAction() throws Exception {
		//
	}


	/**
	 * Realiza o login do usuário
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws SRVException
	 */
	public ActionForward login(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
		
		String login   = getStringParam(pRequest, "login").toLowerCase();
		String senha   = getStringParam(pRequest, "senha");

		UsuarioVO usuarioVO;
		try {
			usuarioVO = UsuarioBusiness.getInstance().loginUsuario(login, senha);

			Map<?,?> acessos = AcessoBusiness.getInstance().obtemAcessoPerfil(usuarioVO.getIdPerfil().intValue());
			montaAcessosMenu(acessos, pRequest);
			
			UsuarioBean usuarioBean = new UsuarioBean();
			usuarioBean.setUsuarioVO(usuarioVO);
			usuarioBean.setIp(pRequest.getRemoteAddr());
			usuarioBean.setPermissoes(acessos);
			usuarioBean.setQtdeTentativasSemSucesso(new Integer(0));
			usuarioBean.setSenhaExpirada(Boolean.FALSE);
			
			pRequest.getSession().setAttribute(ConstantesRequestSession.SESSION_USUARIO_BEAN, usuarioBean);
			return pMapping.findForward(LoginAction.FORWARD_INICIO);

		} catch (AutenticacaoException e) {
			pRequest.setAttribute("login", login);
			setMensagemErro(pRequest, e.getMessage());
			return pMapping.findForward(LoginAction.FORWARD_LOGIN);
		}
	}
	
	/**
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws SRVException
	 */
	public ActionForward logoff(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws SRVException {
		pRequest.getSession().removeAttribute(ConstantesRequestSession.SESSION_USUARIO_BEAN);
		return pMapping.findForward(LoginAction.FORWARD_LOGIN);
	}

	/**
	 * Monta permissões do menu conforme os acessos do perfil 
	 * 
	 * @param acessos
	 * @param pRequest
	 * @throws SRVException 
	 */
	private void montaAcessosMenu(Map<?,?> acessos, HttpServletRequest pRequest) throws SRVException {
		pRequest.getSession().setAttribute("menuSRV", new MenuBuild().getMenuHtml(acessos));
	}
	
	
	/** 
	 * Vai para a tela de alteração de senha
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */
	public ActionForward preparaAlteracaoSenha(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		return pMapping.findForward(LoginAction.FORWARD_ALTERACAO_SENHA);
	}
	
	
	/**
	 * Efetiva a alteração de senha
	 * 
	 * @param pMapping
	 * @param pForm
	 * @param pRequest
	 * @param pResponse
	 * @return
	 * @throws Exception
	 */	
	public ActionForward alterarSenha(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) throws Exception {
		
		String login   				  = getStringParam(pRequest, "login");
		String senha   				  = getStringParam(pRequest, "senha");
		String novaSenha  			  = getStringParam(pRequest, "novaSenha");
		String confirmacaoNovaSenha   = getStringParam(pRequest, "confirmacaoNovaSenha");
		
		pRequest.setAttribute("login", login);
		
		if (!novaSenha.equals(confirmacaoNovaSenha)) {
			setMensagemErro(pRequest, "Confirmação da nova senha não corresponde a nova senha informada.");
			return pMapping.findForward(LoginAction.FORWARD_ALTERACAO_SENHA);
		}
		
		UsuarioVO usuarioVO = UsuarioBusiness.getInstance().loginUsuario(login, senha);
		if (usuarioVO != null) {		
			UsuarioBusiness.getInstance().alteraSenha(login, novaSenha);
			setMensagem(pRequest, "A sua senha foi alterada com sucesso. Realize o login com a nova senha cadastrada.");
			return pMapping.findForward(LoginAction.FORWARD_LOGIN);
		} else {
			setMensagemErro(pRequest, "O usuário e/ou a senha atual informada estão incorretos.");
			return pMapping.findForward(LoginAction.FORWARD_ALTERACAO_SENHA);
		}	
	}	

}