package br.com.marisa.srv.cargo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.cargo.vo.GrupoCargoVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;


/**
 * Classe para tratar dos métodos de acesso a dados do Grupos de Cargos por Remuneracao Variavel
 * 
 * @author Walter Fontes
 */
public class GrupoCargoDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(GrupoCargoDAO.class);
    
    
    
    /**
     * Obtém ids dos grupos de remuneracao variavel
     * 
     * @param idCargo
     * @return
     */
    public List<Integer> obtemListaCargosByIdGrupo(int idGrupo, Boolean fechaConexao) throws PersistenciaException {    	

        String query =  " select gc.COD_CARGO from SRV_GRUPO_CARGO gc, SRV_CARGO c "
        		+ " where c.cod_cargo = gc.cod_cargo "
        		+ " and gc.cod_grp_rem_var = ? "
        		+ " and c.flg_agrupa_fil_lider = ? ";
                        
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Integer> listaCargos = new ArrayList<Integer>();
        int campos = 1;
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
        	setInteger(stmt, campos++, idGrupo);
        	setString(stmt, campos, "S");
            rs = stmt.executeQuery();
            
            while (rs.next()) {
            	listaCargos.add(getInteger(rs, "COD_CARGO"));
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter lista de cargos dos grupos de remuneracao variavel", e);
        } finally {
        	if(fechaConexao){
        		closeStatementAndResultSet(stmt, rs,conn);
        	}else{
        		closeStatementAndResultSet(stmt, rs);
        	}
        }
        return listaCargos;
    }
    
    
    /**
     * Obtém ids dos grupos de remuneracao variavel
     * 
     * @param idCargo
     * @return
     */
    public List<Integer> obtemIdsGruposRemuneracao(int idCargo) throws PersistenciaException {

        String query =  "SELECT COD_GRP_REM_VAR    " +
                        "  FROM SRV_GRUPO_CARGO    " +
                        " WHERE COD_CARGO = ?      " +
                        " ORDER BY COD_GRP_REM_VAR ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Integer> idsRemuneracoes = new ArrayList<Integer>();
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
        	setInteger(stmt, 1, idCargo);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
            	idsRemuneracoes.add(getInteger(rs, "COD_GRP_REM_VAR"));
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter os ids dos grupos de remuneracao variavel", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return idsRemuneracoes;
    }
    
    
    /**
     * Obtém ids dos grupos de remuneracao variavel
     * 
     * @param idGrupoRemuneracao
     * @param idCargo
     * @return
     */
    public GrupoCargoVO obtemGrupoCargo(int idGrupoRemuneracao, int idCargo) throws PersistenciaException {

        String query =  "SELECT COD_GRP_REM_VAR, COD_CARGO, DT_EXCLUSAO, COD_USUARIO_EXCLUSAO " +
                        "  FROM SRV_GRUPO_CARGO       " +
                        " WHERE COD_GRP_REM_VAR = ?   " +
        				"   AND COD_CARGO       = ?   ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        GrupoCargoVO grupoCargoVO = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            setInteger(stmt, 1, idGrupoRemuneracao);
        	setInteger(stmt, 2, idCargo);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	grupoCargoVO = new GrupoCargoVO();
            	grupoCargoVO.setIdGrupoRemuneracao(getInteger(rs, "COD_GRP_REM_VAR"));
            	grupoCargoVO.setIdCargo(getInteger(rs, "COD_CARGO"));
            	grupoCargoVO.setDataExclusao(getTimestamp(rs, "DT_EXCLUSAO"));
            	grupoCargoVO.setIdUsuarioExclusao(getInteger(rs, "COD_USUARIO_EXCLUSAO"));
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter o grupos de cargo", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return grupoCargoVO;
    }
    
    
    /**
     * Inclui grupo de cargo x grupo de remuneracao
     * 
     * @param idGrupoRemuneracao
     * @param idCargo
     * @param dataAlteracao
     * @param idUsuario
     * @return
     */
    public void incluiGrupoCargo(int idGrupoRemuneracao, int idCargo, Date dataAlteracao, int idUsuario) throws PersistenciaException {

        String query =  "INSERT INTO SRV_GRUPO_CARGO      " +
        				"      (COD_GRP_REM_VAR         , " +
        				"       COD_CARGO 		  	    , " +
        				"		DT_INI_SIT_SRV 		    , " +
        				"		COD_USUARIO)              " +
                        " VALUES (?, ?, ?, ?)             ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, idGrupoRemuneracao);
            setInteger 	(stmt, ordemCampos++, idCargo);
            setTimestamp(stmt, ordemCampos++, dataAlteracao);
            setInteger 	(stmt, ordemCampos++, idUsuario);
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o grupo do cargo", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }
    

    /**
     * Inclui o historico do grupo de cargo x grupo de remuneracao
     * 
     * @param idGrupoRemuneracao
     * @param idCargo
     * @param dataExclusao
     * @param idUsuario
     * @return
     */
    public void incluiGrupoCargoHistorico(int idGrupoRemuneracao, int idCargo, Date dataExclusao, int idUsuario) throws PersistenciaException {

        String query =  "INSERT INTO SRV_GRUPO_CARGO_HIST " +
						"      (COD_GRP_REM_VAR         , " +
						"       COD_CARGO 		  	    , " +
						"       SEQ_GRUPO_CARGO_HIST    , " +
						"		DT_EXCLUSAO 		    , " +
						"		COD_USUARIO_EXCLUSAO)     " +
				        " VALUES (?, ?, ?, ?, ?)          ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, idGrupoRemuneracao);
            setInteger 	(stmt, ordemCampos++, idCargo);
            setInteger	(stmt, ordemCampos++, obtemProximaSequenciaHistorio(idGrupoRemuneracao, idCargo));
            setTimestamp(stmt, ordemCampos++, dataExclusao);
            setInteger 	(stmt, ordemCampos++, idUsuario);            
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o historico do grupo de cargo", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }

    
    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idGrupoRemuneracao
     * @param idCargo
     * @return
     */
    private int obtemProximaSequenciaHistorio(int idGrupoRemuneracao, int idCargo) throws PersistenciaException {

        String query =  "SELECT MAX(SEQ_GRUPO_CARGO_HIST) " +
                        "  FROM SRV_GRUPO_CARGO_HIST      " +
                        " WHERE COD_GRP_REM_VAR = ?       " +
                        "   AND COD_CARGO       = ?       ";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, idGrupoRemuneracao);
            setInteger(stmt, ordemCampos++, idCargo);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, 1) + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico do grupo de cargo x remuneracao " + idCargo + " / " + idGrupoRemuneracao, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }
    
    

    /**
     * Exclui o historico do grupo de cargo x grupo de remuneracao
     * 
     * @param idGrupoRemuneracao
     * @param idCargo
     * @return
     */
    public void excluiGrupoCargo(int idGrupoRemuneracao, int idCargo) throws PersistenciaException {

        String query =  "DELETE FROM SRV_GRUPO_CARGO      " +
						" WHERE COD_GRP_REM_VAR  = ?      " +
						"   AND COD_CARGO 		 = ? 	  ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, idGrupoRemuneracao);
            setInteger 	(stmt, ordemCampos++, idCargo);
            
            int resultado = stmt.executeUpdate();
            log.debug(String.valueOf(resultado));

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível excluir o grupo de cargo", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }    
}