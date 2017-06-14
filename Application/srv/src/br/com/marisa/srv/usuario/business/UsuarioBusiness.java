package br.com.marisa.srv.usuario.business;

import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.AutenticacaoException;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.helper.ActiveDirectoryAuthentication;
import br.com.marisa.srv.perfil.business.PerfilBusiness;
import br.com.marisa.srv.perfil.vo.PerfilVO;
import br.com.marisa.srv.usuario.dao.UsuarioDAO;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

/**
 * Classe para conter os métodos de negócio do módulo de Usuários 
 * 
 * @author Walter Fontes
 */
public class UsuarioBusiness {
    
	
    //Log4J
    private static final Logger log = Logger.getLogger(UsuarioBusiness.class);    

    
    //Instancia do Singleton
    private static UsuarioBusiness instance = new UsuarioBusiness();
    
    
    /**
     * Obtem uma instancia do objeto UsuarioBusiness
     * @return O objeto UsuarioBusiness
     */
    public static final UsuarioBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private UsuarioBusiness() {
        // vazio
    }
    
    /**
     * Pesquisa Usuários
     * 
     * @param matricula
     * @param login
     * @return
     */
    public List<UsuarioVO> obtemUsuarios(UsuarioVO pesquisaVO) throws SRVException {
    	UsuarioDAO usuarioDAO = new UsuarioDAO();
        try {
            return usuarioDAO.obtemUsuarios(pesquisaVO);
        } finally {
        	usuarioDAO.closeConnection();
        }
    }
    
    
    /**
     * Pesquisa Usuário
     * 
     * @param idUsuario
     * @return
     */
    public UsuarioVO obtemUsuario(int idUsuario) throws SRVException {
    	UsuarioDAO usuarioDAO = new UsuarioDAO();
        try {
            return usuarioDAO.obtemUsuario(idUsuario);
        } finally {
        	usuarioDAO.closeConnection();
        }
    }    
    
    
    /**
     * Pesquisa Usuário
     * 
     * @param login
     * @return
     */
    public UsuarioVO obtemUsuario(String login) throws SRVException {
    	UsuarioDAO usuarioDAO = new UsuarioDAO();
        try {
            return usuarioDAO.obtemUsuario(login);
        } finally {
        	usuarioDAO.closeConnection();
        }
    }       

    /**
     * 
     * @param usuario
     * @param senha
     * @return
     * @throws AutenticacaoException
     */
	public UsuarioVO loginUsuario(String usuario, String senha) throws AutenticacaoException {

		UsuarioVO usuarioVO = null;

		try {
			usuarioVO = loginUsuarioSistema(usuario, senha);
		} catch (PersistenciaException e) {
			log.error("Falha ao efetuar login do usuario no sistema: " + e.getMessage(), e);
			throw new AutenticacaoException("ERRO: Falha no login, tente novamente mais tarde");
		}

		if (usuarioVO.getAutenticaAD()) {
			//Autentica usuario e senha no Active Directory
			try {
				ActiveDirectoryAuthentication ad = new ActiveDirectoryAuthentication(Constantes.DOMAIN_MARISA);
				if (!ad.authenticate(usuario, senha)) {
					throw new AutenticacaoException("AD: Usuário não foi autenticado no domínio");
				}
			} catch (AutenticacaoException ex) {
				throw ex;
			}
		}

		return usuarioVO;
	}    

	/**
	 * 
	 * @param usuario
	 * @param senha
	 * @param isValidaSenha
	 * @return
	 * @throws PersistenciaException
	 * @throws AutenticacaoException
	 */
	private UsuarioVO loginUsuarioSistema(String usuario, String senha) throws PersistenciaException, AutenticacaoException {

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		UsuarioVO usuarioVO = null;

		try {

			usuarioVO = usuarioDAO.loginUsuario(usuario, senha);

			//Caso nao encontre o usuario no SRV
			if (usuarioVO == null) {
				throw new AutenticacaoException("SRV: Usuário não cadastrado no sistema");
			} else {
				PerfilVO perfilVO = PerfilBusiness.getInstance().obtemPerfilByCod(usuarioVO.getIdPerfil().intValue());
				if (perfilVO == null || !perfilVO.getIsAtivo()) {
					throw new AutenticacaoException("SRV: Perfil do usuário é inválido");
				}
			}
        } catch (AutenticacaoException aEx) {
        	throw aEx;
		} finally {
			usuarioDAO.closeConnection();
		}
		return usuarioVO;
	}

    /**
     * Realiza alteração de senha do usuario
     * 
     * @param usuario
     * @param senha
     * @return
     */
    public void alteraSenha(String usuario, String senha) throws SRVException {
    	UsuarioDAO usuarioDAO = new UsuarioDAO();
        try {
        	usuarioDAO.alteraSenha(usuario, senha);        	
        } finally {
        	usuarioDAO.closeConnection();
        }    		
    }
    
    
    /**
     * Realiza alteração do usuario
     * 
     * @param usuarioVO
     * @return
     */
    public void alteraUsuario(UsuarioVO usuarioVO, boolean reiniciarSenha) throws SRVException {
    	UsuarioDAO usuarioDAO = new UsuarioDAO();
        try {
        	usuarioDAO.beginTrans();
        	
        	//Grava historico da situacao anterior
        	UsuarioVO usuarioAnteriorVO = usuarioDAO.obtemUsuario(usuarioVO.getIdUsuario().intValue());
        	usuarioDAO.incluiUsuarioHistorico(usuarioAnteriorVO);
        	
        	//Efetiva a alteração
        	usuarioDAO.alteraUsuario(usuarioVO, reiniciarSenha);
        	
        	usuarioDAO.commitTrans();
        	
        } catch (Exception e) {
        	usuarioDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao de usuario", e);
        } finally {
        	usuarioDAO.closeConnection();
        }    		
    }
    
    
    /**
     * Realiza inclusao do usuario
     * 
     * @param usuarioVO
     * @return
     */
    public void incluiUsuario(UsuarioVO usuarioVO) throws SRVException {
    	UsuarioDAO usuarioDAO = new UsuarioDAO();
        try {
        	usuarioVO.setSenha(usuarioVO.getLogin());
        	usuarioDAO.incluiUsuario(usuarioVO);
        } finally {
        	usuarioDAO.closeConnection();
        }    		
    }     
}
