package br.com.marisa.srv.usuario.business;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

/**
 * Classe para conter os métodos de negócio do módulo de Usuários 
 * 
 * @author Walter Fontes
 */
public class UsuarioBusinessAjax {
	
	/**
	 * Obtém usuário
	 * 
	 * @param idUsuarioSrt
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
	public UsuarioVO obtemUsuario(String idUsuarioSrt) throws SRVException {
		Integer idUsuario = new Integer(idUsuarioSrt);
		return UsuarioBusiness.getInstance().obtemUsuario(idUsuario.intValue());
	}
	
	/**
	 * Verifica se o login é válido
	 * 
	 * @param login
	 * @return
	 * @throws SRVException
	 */
	public boolean validaLoginUsuario(String login) throws SRVException {
		if (login == null) return false;
		for (int i=0; i<login.length(); i++) {
			if ("abcdefghijklmnopqrstuvwxyz0123456789._".indexOf(login.toLowerCase().charAt(i)) == -1) {
				return false;
			}
		}
		return true;
	}	
	
	
	/**
	 * Verifica se o login já existe
	 * 
	 * @param login
	 * @return
	 * @throws SRVException
	 */
	public boolean validaLoginJaExistente(String login, String idUsuario) throws SRVException {
		if (login == null) return true;
		UsuarioVO usuarioVO = UsuarioBusiness.getInstance().obtemUsuario(login);
		if (usuarioVO != null && (ObjectHelper.isEmpty(idUsuario) || usuarioVO.getIdUsuario().intValue() != Integer.parseInt(idUsuario))) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Verifica se a matrícula é numérica
	 * 
	 * @param matricula
	 * @return
	 * @throws SRVException
	 */
	public boolean validaMatriculaUsuario(String matricula) throws SRVException {
		
		if (matricula != null) {
			for (int i=0; i<matricula.length(); i++) {
				if ("0123456789".indexOf(matricula.charAt(i)) == -1) {
					return false;
				}
			}
		}
		return true;
	}	

	/**
	 * 
	 * @param login
	 * @return
	 * @throws SRVException
	 */
	public String validaUsuarioSistema(String login) throws SRVException {
		String mensagem = "";
		UsuarioVO vo = null;
		if (login != null) {
			vo = UsuarioBusiness.getInstance().obtemUsuario(login.toLowerCase());
		}
		if (vo == null) {
			mensagem = "Usuário não encontrado no sistema!";
		} else {
			if (!vo.getAtivo()) {
				mensagem = "Usuário desativado no sistema!";
			} else if (vo.getAutenticaAD()) {
				mensagem = "Usuário com autenticação no AD, favor alterar sua senha pelo \"Portal de Senha\" na Intranet!";
			}
		}
		return mensagem;
	}

}