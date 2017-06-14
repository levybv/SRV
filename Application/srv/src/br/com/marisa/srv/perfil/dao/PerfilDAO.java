package br.com.marisa.srv.perfil.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.perfil.vo.PerfilVO;


/**
 * Classe para tratar dos métodos de acesso a dados de usuários
 * 
 * @author Walter Fontes
 */
public class PerfilDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(PerfilDAO.class);

    /**
     * 
     * @param pesquisaVO
     * @return
     * @throws PersistenciaException
     */
	public List<PerfilVO> obtemListaPerfil(PerfilVO pesquisaVO) throws PersistenciaException {

		StringBuffer query =  new StringBuffer(
			" SELECT " +
			"		COD_PERFIL, " +
			"		DESCR_PERFIL, " +
			"		FLG_ATIVO, " +
			"		FLG_RESULT_BONUS, " +
			"		FLG_REABRE_BONUS, " +
			"		FLG_VALIDA_VLR_FX_ESCALA " +
			" FROM " +
			"		SRV_PERFIL " +
			" WHERE " +
			"		1 = 1 ");

		if (ObjectHelper.isNotNull(pesquisaVO)) {
			if (ObjectHelper.isNotEmpty(pesquisaVO.getIdPerfil())) {
				query.append(" AND COD_PERFIL = ? ");
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricao())) {
				query.append(" AND UPPER(DESCR_PERFIL) LIKE ? ");
			}
			if (ObjectHelper.isNotNull(pesquisaVO.getIsAtivo())) {
				query.append(" AND FLG_ATIVO = ? ");
			}
			if (ObjectHelper.isNotNull(pesquisaVO.getIsExibeBonus())) {
				query.append(" AND FLG_RESULT_BONUS = ? ");
			}
			if (ObjectHelper.isNotNull(pesquisaVO.getIsReabreResultado())) {
				query.append(" AND FLG_REABRE_BONUS = ? ");
			}
			if (ObjectHelper.isNotNull(pesquisaVO.getIsValidaFaixaEscala())) {
				query.append(" AND FLG_VALIDA_VLR_FX_ESCALA = ? ");
			}
		}
	    query.append(" ORDER BY DESCR_PERFIL ");

	    List<PerfilVO> lista = new ArrayList<PerfilVO>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			int ordemCampos = 1;
			if (ObjectHelper.isNotNull(pesquisaVO)) {
				if (ObjectHelper.isNotEmpty(pesquisaVO.getIdPerfil())) {
					setInteger(stmt, ordemCampos++, pesquisaVO.getIdPerfil());
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricao())) {
					setString(stmt, ordemCampos++, "%"+pesquisaVO.getDescricao().toUpperCase()+"%");
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getIsAtivo())) {
					setBoolean(stmt, ordemCampos++, pesquisaVO.getIsAtivo());
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getIsExibeBonus())) {
					setBoolean(stmt, ordemCampos++, pesquisaVO.getIsExibeBonus());
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getIsReabreResultado())) {
					setBoolean(stmt, ordemCampos++, pesquisaVO.getIsReabreResultado());
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getIsValidaFaixaEscala())) {
					setBoolean(stmt, ordemCampos++, pesquisaVO.getIsValidaFaixaEscala());
				}
			}
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				PerfilVO perfilVO = new PerfilVO();
				perfilVO.setIdPerfil(getInteger(rs, "COD_PERFIL")) ;
				perfilVO.setDescricao(getString(rs,"DESCR_PERFIL"));
				perfilVO.setIsAtivo(getBoolean(rs,"FLG_ATIVO"));
				perfilVO.setIsExibeBonus(getBoolean(rs,"FLG_RESULT_BONUS"));
				perfilVO.setIsReabreResultado(getBoolean(rs, "FLG_REABRE_BONUS"));
				perfilVO.setIsValidaFaixaEscala(getBoolean(rs, "FLG_VALIDA_VLR_FX_ESCALA"));
				lista.add(perfilVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista do filtro de perfil.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}

	public PerfilVO obtemPerfilByCod(Integer codPerfil) throws PersistenciaException {
		StringBuffer query =  new StringBuffer("SELECT COD_PERFIL, DESCR_PERFIL, FLG_ATIVO, FLG_RESULT_BONUS, FLG_REABRE_BONUS, FLG_VALIDA_VLR_FX_ESCALA  FROM SRV_PERFIL   ");
	    if(codPerfil != null) {
	       	query.append(" WHERE COD_PERFIL = ? ");
	    }
	    PerfilVO perfilVO = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int ordemCampos = 1;
			if(codPerfil != null) {
				setInteger(stmt, ordemCampos++, codPerfil);
			}
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				perfilVO = new PerfilVO();
				perfilVO.setIdPerfil(getInteger(rs, "COD_PERFIL")) ;
				perfilVO.setDescricao(getString(rs,"DESCR_PERFIL"));
				perfilVO.setIsAtivo(getBoolean(rs,"FLG_ATIVO"));
				perfilVO.setIsExibeBonus(getBoolean(rs,"FLG_RESULT_BONUS"));
				perfilVO.setIsReabreResultado(getBoolean(rs, "FLG_REABRE_BONUS"));
				perfilVO.setIsValidaFaixaEscala(getBoolean(rs,"FLG_VALIDA_VLR_FX_ESCALA"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter lista de perfil by codigo" + codPerfil, e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return perfilVO;
	}

	/**
	 * 
	 * @param descricao
	 * @return
	 * @throws PersistenciaException
	 */
	public List<PerfilVO> obtemListaPerfisByDescricao(String descricao) throws PersistenciaException {
		StringBuffer query =  new StringBuffer("SELECT COD_PERFIL, DESCR_PERFIL, FLG_ATIVO, FLG_RESULT_BONUS, FLG_REABRE_BONUS, FLG_VALIDA_VLR_FX_ESCALA  FROM SRV_PERFIL   ");
	    if(descricao != null) {
	       	query.append(" WHERE lower(DESCR_PERFIL) LIKE '%'||?||'%' ");
	    }
	    query.append(" ORDER BY DESCR_PERFIL ");
	    List<PerfilVO> lista = new ArrayList<PerfilVO>();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int ordemCampos = 1;
			if(descricao != null) {
				setString(stmt, ordemCampos++, descricao.toLowerCase().trim());
			}
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				PerfilVO perfilVO = new PerfilVO();
				perfilVO.setIdPerfil(getInteger(rs, "COD_PERFIL")) ;
				perfilVO.setDescricao(getString(rs,"DESCR_PERFIL"));
				perfilVO.setIsAtivo(getBoolean(rs,"FLG_ATIVO"));
				perfilVO.setIsExibeBonus(getBoolean(rs,"FLG_RESULT_BONUS"));
				perfilVO.setIsReabreResultado(getBoolean(rs, "FLG_REABRE_BONUS"));
				perfilVO.setIsValidaFaixaEscala(getBoolean(rs,"FLG_VALIDA_VLR_FX_ESCALA"));
				lista.add(perfilVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter lista de perfil by descricao " + descricao, e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}


	public void atualizarPerfil(PerfilVO perfilVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer("UPDATE SRV_PERFIL             " +
											   "   SET DESCR_PERFIL     = ?,  " +
											   "       FLG_ATIVO        = ?,  " +
											   "       FLG_RESULT_BONUS 		= ?,	" +
											   "       FLG_VALIDA_VLR_FX_ESCALA = ?,   	" +
											   "	   FLG_REABRE_BONUS = ? " +
        									   " WHERE COD_PERFIL       = ?   ");
    
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int ordemCampos = 1;
			setString  (stmt, ordemCampos++, perfilVO.getDescricao());
			setBoolean (stmt, ordemCampos++, perfilVO.getIsAtivo());
			setBoolean (stmt, ordemCampos++, perfilVO.getIsExibeBonus());
			setBoolean (stmt, ordemCampos++, perfilVO.getIsValidaFaixaEscala());
			setBoolean (stmt, ordemCampos++, perfilVO.getIsReabreResultado());
			setInteger (stmt, ordemCampos++, perfilVO.getIdPerfil());
			stmt.execute();
			
			
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível atualizar perfil " + perfilVO.getIdPerfil(), e);
		} finally {
			closeStatementAndResultSet(stmt, null);
		}
	}
	
	/**
     * Obtém próxima sequencia do histórico
     * 
     * @param idUsuario
     * @return
     */
    private int obtemProximaSequenciaHistorio(Integer idPerfil) throws PersistenciaException {

        String query =  "SELECT MAX(SEQ_ALT_PERFIL)  SEQ_ALT_PERFIL  " +
                        "  FROM SRV_PERFIL_HIST   " +
                        " WHERE COD_PERFIL =?     ";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, idPerfil);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, "SEQ_ALT_PERFIL") + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico do perfil " + idPerfil, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }
    
	
    /**
     * Inclui histórico de perfil
     * 
     * @param perfilVO
     * @param idUsuario
     * @throws PersistenciaException
     */
	public void salvaPerfilHist(PerfilVO perfilVO, Integer idUsuario) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(	"INSERT INTO SRV_PERFIL_HIST (COD_PERFIL, SEQ_ALT_PERFIL, DESCR_PERFIL,                      " +
												"                             FLG_ATIVO, FLG_RESULT_BONUS, DT_ULT_ALT, COD_USUARIO_ULT_ALT)  " +
												" VALUES (?, ?, ?, ?, ?, ?, ?)                                                                 ");
    
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int ordemCampos = 1;
			setInteger	(stmt, ordemCampos++, perfilVO.getIdPerfil());
			setInteger	(stmt, ordemCampos++, obtemProximaSequenciaHistorio(perfilVO.getIdPerfil()));
			setString	(stmt, ordemCampos++, perfilVO.getDescricao());
			setBoolean	(stmt, ordemCampos++, perfilVO.getIsAtivo());
			setBoolean	(stmt, ordemCampos++, perfilVO.getIsExibeBonus());
			setTimestamp(stmt, ordemCampos++, new Date());
			setInteger	(stmt, ordemCampos++, idUsuario);
			
			stmt.execute();
			
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível atualizar perfil " + perfilVO.getIdPerfil(), e);
		} finally {
			closeStatementAndResultSet(stmt, null);
		}
	}

	/**
	 * 
	 * @param codPerfil
	 * @return
	 * @throws PersistenciaException
	 */
	public boolean isUsuarioPodeReabrirBonus(Integer codPerfil) throws PersistenciaException {

		boolean isPodeReabrirBonus = false;

		PreparedStatement stmt = null;
		ResultSet rs = null;
		Connection conn = null;

		try {
			StringBuffer query = new StringBuffer(" SELECT FLG_REABRE_BONUS FROM SRV_PERFIL WHERE COD_PERFIL = ? ");
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int nroCampo = 1;
			setInteger(stmt, nroCampo++, codPerfil);
			rs = stmt.executeQuery();

			if (rs.next()) {
				isPodeReabrirBonus = getBoolean(rs, "FLG_REABRE_BONUS");
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Ocorreu erro ao verificar se usuário possui permissão para reabrir bonus", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return isPodeReabrirBonus;
	}

}