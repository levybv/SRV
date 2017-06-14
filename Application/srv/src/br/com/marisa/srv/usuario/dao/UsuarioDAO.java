package br.com.marisa.srv.usuario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.AutenticacaoException;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.AutenticacaoHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.usuario.vo.UsuarioVO;


/**
 * Classe para tratar dos métodos de acesso a dados de usuários
 * 
 * @author Walter Fontes
 */
public class UsuarioDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(UsuarioDAO.class);
    
    
    /**
     * Obtém usuários
     * 
     * @param matricula
     * @param login
     * @return
     */
    public List<UsuarioVO> obtemUsuarios(UsuarioVO pesquisaVO) throws PersistenciaException {

        String query =  " SELECT U.COD_USUARIO, " +
		        		"        U.NOME_USUARIO, " +
		        		"        U.LOGIN_USUARIO, " +
		        		"        U.FUNC_MATRICULA_ID, " +
		        		"        U.FLG_ATIVO, " +
		        		"        U.FLG_VALIDA_AD, " +
		        		"        U.COD_PERFIL, " +
		        		"        U.DT_ULT_ALT, " +
		        		"        U.COD_USUARIO_ULT_ALT, " +
		        		"        P.DESCR_PERFIL " +
		        		"   FROM SRV_USUARIO U, " +
		        		"        SRV_PERFIL P " +
		        		"  WHERE U.COD_PERFIL = P.COD_PERFIL ";

        if ( ObjectHelper.isNotEmpty(pesquisaVO) ) {
            if ( ObjectHelper.isNotEmpty(pesquisaVO.getIdUsuario()) ) {
            	query += "    AND U.COD_USUARIO  = ? ";
            }
            if ( ObjectHelper.isNotNull(pesquisaVO.getAtivo()) ) {
            	query += "    AND U.FLG_ATIVO  = ? ";
            }
            if ( ObjectHelper.isNotNull(pesquisaVO.getAutenticaAD()) ) {
            	query += "    AND U.FLG_VALIDA_AD  = ? ";
            }
            if ( ObjectHelper.isNotNull(pesquisaVO.getIdPerfil()) ) {
            	query += "    AND U.COD_PERFIL  = ? ";
            }
            if ( ObjectHelper.isNotEmpty(pesquisaVO.getNome()) ) {
            	query += "    AND UPPER(U.NOME_USUARIO)  LIKE '%"+pesquisaVO.getNome().toUpperCase().trim()+"%' ";
            }
            if ( ObjectHelper.isNotEmpty(pesquisaVO.getMatricula()) ) {
            	query += "    AND UPPER(U.FUNC_MATRICULA_ID)  LIKE '%"+pesquisaVO.getMatricula().toUpperCase().trim()+"%' ";
            }
            if ( ObjectHelper.isNotEmpty(pesquisaVO.getLogin()) ) {
            	query += "    AND UPPER(U.LOGIN_USUARIO)  LIKE '%"+pesquisaVO.getLogin().toUpperCase().trim()+"%' ";
            }
        }
        query += " ORDER BY U.NOME_USUARIO ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<UsuarioVO> usuarios = new ArrayList<UsuarioVO>();
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int nroCampo = 1;

            if ( ObjectHelper.isNotEmpty(pesquisaVO) ) {
                if ( ObjectHelper.isNotEmpty(pesquisaVO.getIdUsuario()) ) {
                	setInteger(stmt, nroCampo++, pesquisaVO.getIdUsuario());
                }
                if ( ObjectHelper.isNotNull(pesquisaVO.getAtivo()) ) {
                	setString(stmt, nroCampo++, pesquisaVO.getAtivo()?"S":"N");
                }
                if ( ObjectHelper.isNotNull(pesquisaVO.getAutenticaAD()) ) {
                	setString(stmt, nroCampo++, pesquisaVO.getAutenticaAD()?"S":"N");
                }
                if ( ObjectHelper.isNotNull(pesquisaVO.getIdPerfil()) ) {
                	setInteger(stmt, nroCampo++, pesquisaVO.getIdPerfil());
                }
            }

            rs = stmt.executeQuery();
            UsuarioVO usuarioVO = null;

            while (rs.next()) {
            	usuarioVO = new UsuarioVO();
            	usuarioVO.setIdUsuario(getInteger(rs, "COD_USUARIO"));
            	usuarioVO.setNome(getString(rs, "NOME_USUARIO"));
            	usuarioVO.setMatricula(getString(rs, "FUNC_MATRICULA_ID"));
            	usuarioVO.setAtivo(getBoolean(rs, "FLG_ATIVO"));
            	usuarioVO.setAutenticaAD(getBoolean(rs, "FLG_VALIDA_AD"));
            	usuarioVO.setLogin(getString(rs, "LOGIN_USUARIO"));
            	usuarioVO.setIdPerfil(getInteger(rs, "COD_PERFIL"));
            	usuarioVO.setDescricaoPerfil(getString(rs, "DESCR_PERFIL"));
            	usuarioVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_ULT_ALT"));
            	usuarioVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO_ULT_ALT"));
            	usuarios.add(usuarioVO);
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter os usuarios", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return usuarios;
    }
    
    
    /**
     * Obtém usuário por id
     * 
     * @param idUsuario
     * @return
     */
    public UsuarioVO obtemUsuario(int idUsuario) throws PersistenciaException {

        String query =  "SELECT NOME_USUARIO, FUNC_MATRICULA_ID, FLG_ATIVO, LOGIN_USUARIO, SENHA_USUARIO, COD_PERFIL, DT_ULT_ALT, COD_USUARIO_ULT_ALT, FLG_VALIDA_AD " +
                        "  FROM SRV_USUARIO      " +
                        " WHERE COD_USUARIO = ?  ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UsuarioVO usuarioVO = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            setInteger(stmt, 1, idUsuario);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	usuarioVO = new UsuarioVO();
            	usuarioVO.setIdUsuario(new Integer(idUsuario));
            	usuarioVO.setNome(getString(rs, "NOME_USUARIO"));
            	usuarioVO.setMatricula(getString(rs, "FUNC_MATRICULA_ID"));
            	usuarioVO.setAtivo(getBoolean(rs, "FLG_ATIVO"));
            	usuarioVO.setAutenticaAD(getBoolean(rs, "FLG_VALIDA_AD"));
            	usuarioVO.setLogin(getString(rs, "LOGIN_USUARIO"));
            	usuarioVO.setSenha(getString(rs, "SENHA_USUARIO"));
            	usuarioVO.setIdPerfil(getInteger(rs, "COD_PERFIL"));
            	usuarioVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_ULT_ALT"));
            	usuarioVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO_ULT_ALT"));
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter o usuario " + idUsuario, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return usuarioVO;
    }
    
    
    /**
     * Obtém usuário por login
     * 
     * @param login
     * @return
     */
    public UsuarioVO obtemUsuario(String login) throws PersistenciaException {

        String query =  "SELECT COD_USUARIO, NOME_USUARIO, FUNC_MATRICULA_ID, FLG_ATIVO, SENHA_USUARIO, COD_PERFIL, DT_ULT_ALT, COD_USUARIO_ULT_ALT, FLG_VALIDA_AD " +
                        "  FROM SRV_USUARIO      " +
                        " WHERE LOGIN_USUARIO = ?  ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UsuarioVO usuarioVO = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            setString(stmt, 1, login);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	usuarioVO = new UsuarioVO();
            	usuarioVO.setIdUsuario(getInteger(rs, "COD_USUARIO"));
            	usuarioVO.setNome(getString(rs, "NOME_USUARIO"));
            	usuarioVO.setMatricula(getString(rs, "FUNC_MATRICULA_ID"));
            	usuarioVO.setAtivo(getBoolean(rs, "FLG_ATIVO"));
            	usuarioVO.setAutenticaAD(getBoolean(rs, "FLG_VALIDA_AD"));
            	usuarioVO.setLogin(login);
            	usuarioVO.setSenha(getString(rs, "SENHA_USUARIO"));
            	usuarioVO.setIdPerfil(getInteger(rs, "COD_PERFIL"));
            	usuarioVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_ULT_ALT"));
            	usuarioVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO_ULT_ALT"));
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter o usuario com o login " + login, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return usuarioVO;
    }    
    
    
    /**
     * Valida login de usuario
     * 
     * @param usuario
     * @param senha
     * @return
     * @throws AutenticacaoException 
     */
    public UsuarioVO loginUsuario(String usuario, String senha) throws PersistenciaException, AutenticacaoException {

        String query =  "SELECT COD_USUARIO, NOME_USUARIO, SENHA_USUARIO, FUNC_MATRICULA_ID, FLG_ATIVO, COD_PERFIL, DT_ULT_ALT, COD_USUARIO_ULT_ALT, FLG_VALIDA_AD " +
                        "  FROM SRV_USUARIO WHERE LOWER(LOGIN_USUARIO) = ? AND FLG_ATIVO = ? ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UsuarioVO usuarioVO = null;
        
        try {
        	setConn(null);
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setString (stmt, ordemCampos++, usuario.toLowerCase());
            setBoolean(stmt, ordemCampos++, Boolean.TRUE);
            rs = stmt.executeQuery();
            
            if (rs.next()) {

            	boolean isValidaSenhaAD = getBool(rs, "FLG_VALIDA_AD");
            	if (!isValidaSenhaAD){
                	String senhaBco = getString(rs, "SENHA_USUARIO");
            		if (!AutenticacaoHelper.compare(senha, senhaBco)) {
                		throw new AutenticacaoException("SRV: Senha inválida");
                	}
            	}

            	usuarioVO = new UsuarioVO();
            	usuarioVO.setIdUsuario(getInteger(rs, "COD_USUARIO"));
            	usuarioVO.setNome(getString(rs, "NOME_USUARIO"));
            	usuarioVO.setMatricula(getString(rs, "FUNC_MATRICULA_ID"));
            	usuarioVO.setAtivo(getBoolean(rs, "FLG_ATIVO"));
            	usuarioVO.setAutenticaAD(isValidaSenhaAD);
            	usuarioVO.setLogin(usuario.toLowerCase());
            	usuarioVO.setIdPerfil(getInt(rs, "COD_PERFIL"));
            	usuarioVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_ULT_ALT"));
            	usuarioVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO_ULT_ALT"));
            }

        } catch (AutenticacaoException aEx) {
        	throw aEx;
        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível fazer o login do usuario " + usuario, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return usuarioVO;
    }
    
    
    /**
     * Valida login de usuario
     * 
     * @param usuario
     * @param senha
     * @return
     */
    public UsuarioVO alteraSenha(String usuario, String novaSenha) throws PersistenciaException {

        String query =  "UPDATE SRV_USUARIO " +
        				"   SET SENHA_USUARIO = ?, " +
        				"       COD_USUARIO_ULT_ALT = ?, " +
        				"       DT_ULT_ALT = SYSDATE " +
                        " WHERE LOGIN_USUARIO  = ?  ";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UsuarioVO usuarioVO = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            String novaSenhaEmb = AutenticacaoHelper.embaralha(novaSenha).toString();

            setString(stmt, ordemCampos++, novaSenhaEmb);
            setInteger(stmt, ordemCampos++, Constantes.SRV_ID_USER_PADRAO_SISTEMA);
            setString(stmt, ordemCampos++, usuario);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível fazer a alteracao da senha do usuario " + usuario, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return usuarioVO;
    }
    
    
    /**
     * Altera o usuário
     * 
     * @param usuarioVO
     * @return
     */
    public void alteraUsuario(UsuarioVO usuarioVO, boolean reiniciaSenha) throws PersistenciaException {

        String query =  "UPDATE SRV_USUARIO              " +
        				"   SET NOME_USUARIO 		= ?, " +
        				"		LOGIN_USUARIO 		= ?, " +
        				"		FUNC_MATRICULA_ID 	= ?, " +
        				"		FLG_ATIVO 			= ?, " +
        				"		FLG_VALIDA_AD		= ?, " +
        				"       COD_PERFIL 			= ?, " +
        				"       DT_ULT_ALT 			= SYSDATE, " +
        				"       COD_USUARIO_ULT_ALT = ?  ";
        				
        if (reiniciaSenha) {
        	query += ", SENHA_USUARIO = ?";
        }
        query += " WHERE COD_USUARIO = ? ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setString 	(stmt, ordemCampos++, usuarioVO.getNome().toUpperCase());
            setString 	(stmt, ordemCampos++, usuarioVO.getLogin().toLowerCase());
            setString 	(stmt, ordemCampos++, usuarioVO.getMatricula());
            setBoolean	(stmt, ordemCampos++, usuarioVO.getAtivo());
            setBoolean	(stmt, ordemCampos++, usuarioVO.getAutenticaAD());
            setInteger	(stmt, ordemCampos++, usuarioVO.getIdPerfil());
            setInteger  (stmt, ordemCampos++, usuarioVO.getIdUsuarioUltimaAlteracao());
            if (reiniciaSenha) {
            	setString (stmt, ordemCampos++, AutenticacaoHelper.embaralha(usuarioVO.getLogin()).toString());
            }
            setInteger	(stmt, ordemCampos++, usuarioVO.getIdUsuario());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível alterar o usuario", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }
    
    
    
    /**
     * Inclui o usuário
     * 
     * @param usuarioVO
     * @return
     */
    public void incluiUsuario(UsuarioVO usuarioVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_USUARIO      " +
        				"      (COD_USUARIO         , " +
        				"       NOME_USUARIO 		, " +
        				"		FUNC_MATRICULA_ID 	, " +
        				"		FLG_ATIVO 			, " +
        				"		FLG_VALIDA_AD		, " +
        				"		LOGIN_USUARIO 		, " +
        				"		SENHA_USUARIO 		, " +
        				"       COD_PERFIL 			, " +
        				"       DT_ULT_ALT 			, " +
        				"       COD_USUARIO_ULT_ALT)  " +
                        " VALUES (SEQ_SRV_USUARIO.nextVal, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?) ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setString 	(stmt, ordemCampos++, usuarioVO.getNome().toUpperCase());
            setString 	(stmt, ordemCampos++, usuarioVO.getMatricula());
            setBoolean	(stmt, ordemCampos++, usuarioVO.getAtivo());
            setBoolean	(stmt, ordemCampos++, usuarioVO.getAutenticaAD());
            setString 	(stmt, ordemCampos++, usuarioVO.getLogin().toLowerCase());
            setString 	(stmt, ordemCampos++, AutenticacaoHelper.embaralha(usuarioVO.getSenha()).toString());
            setInteger	(stmt, ordemCampos++, usuarioVO.getIdPerfil() != null ? usuarioVO.getIdPerfil().intValue() : 0);
            setInteger  (stmt, ordemCampos++, usuarioVO.getIdUsuarioUltimaAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o usuario", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }    
    

    /**
     * Inclui o usuário
     * 
     * @param usuarioVO
     * @return
     */
    public void incluiUsuarioHistorico(UsuarioVO usuarioVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_USUARIO_HIST " +
        				"      (COD_USUARIO         , " +
        				"       SEQ_ALT_USUARIO     , " +
        				"       NOME_USUARIO 		, " +
        				"		FUNC_MATRICULA_ID 	, " +
        				"		FLG_ATIVO 			, " +
        				"		FLG_VALIDA_AD		, " +
        				"		LOGIN_USUARIO 		, " +
        				"		SENHA_USUARIO 		, " +
        				"       COD_PERFIL 			, " +
        				"       DT_ULT_ALT 			, " +
        				"       COD_USUARIO_ULT_ALT)  " +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, usuarioVO.getIdUsuario());
            setInteger	(stmt, ordemCampos++, obtemProximaSequenciaHistorio(usuarioVO.getIdUsuario().intValue()));
            setString 	(stmt, ordemCampos++, usuarioVO.getNome());
            setString 	(stmt, ordemCampos++, usuarioVO.getMatricula());
            setBoolean	(stmt, ordemCampos++, usuarioVO.getAtivo());
            setBoolean	(stmt, ordemCampos++, usuarioVO.getAutenticaAD());
            setString 	(stmt, ordemCampos++, usuarioVO.getLogin());
            setString 	(stmt, ordemCampos++, usuarioVO.getSenha());
            setInteger	(stmt, ordemCampos++, usuarioVO.getIdPerfil());
            setTimestamp(stmt, ordemCampos++, usuarioVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, usuarioVO.getIdUsuarioUltimaAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o usuario", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }      

    
    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idUsuario
     * @return
     */
    private int obtemProximaSequenciaHistorio(int idUsuario) throws PersistenciaException {

        String query =  "SELECT MAX(SEQ_ALT_USUARIO) " +
                        "  FROM SRV_USUARIO_HIST     " +
                        " WHERE COD_USUARIO =?       ";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, idUsuario);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, 1) + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico do usuario " + idUsuario, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }
}