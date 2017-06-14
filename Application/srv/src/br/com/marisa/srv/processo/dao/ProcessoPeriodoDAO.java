package br.com.marisa.srv.processo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.processo.vo.ProcessoPeriodoVO;


/**
 * Classe para tratar dos métodos de acesso a dados de Processos por Período 
 * 
 * @author Walter Fontes
 */
public class ProcessoPeriodoDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(ProcessoPeriodoDAO.class);


	/**
	 * Obtém lista de processos por período
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 */
	public List obtemProcessosPeriodo(Integer ano, Integer mes, Integer idProcesso) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_PROC, B.DESCR_PROC, A.NUM_ANO, A.NUM_MES, 	" +
				"        A.STA_PROC, A.COD_USUARIO, A.DT_INI_SIT_SRV 		" +
				"   FROM SRV_PROCESSO_PER A, SRV_PROCESSO B   " +
				"  WHERE A.COD_PROC       = B.COD_PROC        " +
				"    AND B.FLG_ATIV_PROC  = ?                 " +
				"    AND A.NUM_ANO   	  = ?				  ");
		
		List parametros = new ArrayList();
		parametros.add(new ParametroSQL(PARAMTYPE_BOOLEAN, Boolean.TRUE));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		if (mes != null) {
			query.append(" AND A.NUM_MES = ? "); 
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		}
		if (idProcesso != null) {
			query.append(" AND A.COD_PROC = ? "); 
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idProcesso));
		}
		
		query.append(" ORDER BY B.ORDEM_PROC ");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List processos = new ArrayList(); 
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ProcessoPeriodoVO processoPeriodoVO = new ProcessoPeriodoVO();
				processoPeriodoVO.setAno(getInteger(rs, "NUM_ANO"));
				processoPeriodoVO.setMes(getInteger(rs, "NUM_MES"));
				processoPeriodoVO.setIdProcesso(getInteger(rs, "COD_PROC"));
				processoPeriodoVO.setDescricaoProcesso(getString(rs, "DESCR_PROC"));
				processoPeriodoVO.setStatus(getInteger(rs, "STA_PROC"));
				processoPeriodoVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				processoPeriodoVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				processos.add(processoPeriodoVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a lista de processos por periodo.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return processos;
	}
	

    /**
     * Altera processos por período
     * 
     * @param processoPeriodoVO
     * @return
     */
    public void alteraProcessoPeriodo(ProcessoPeriodoVO processoPeriodoVO) throws PersistenciaException {

        String query =  "UPDATE SRV_PROCESSO_PER     " +
        				"   SET STA_PROC		= ?, " +
        				"		DT_INI_SIT_SRV 	= ?, " +
        				"       COD_USUARIO 	= ?  " +
        				" WHERE NUM_ANO 		= ?  " +
        				"   AND NUM_MES 		= ?	 ";
        
        if (processoPeriodoVO.getIdProcesso() != null) {
        	query += "   AND COD_PROC 		= ?	"; 
        } else {
        	query += "   AND COD_PROC 		IN (SELECT COD_PROC	          " +
					 "                           FROM SRV_PROCESSO        " +
					 "                          WHERE FLG_ATIV_PROC = ?)  "; 
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger	(stmt, ordemCampos++, processoPeriodoVO.getStatus());
            setTimestamp(stmt, ordemCampos++, processoPeriodoVO.getDataUltimaAlteracao());
            setInteger	(stmt, ordemCampos++, processoPeriodoVO.getIdUsuarioUltimaAlteracao());
            setInteger	(stmt, ordemCampos++, processoPeriodoVO.getAno());
            setInteger	(stmt, ordemCampos++, processoPeriodoVO.getMes());
            if (processoPeriodoVO.getIdProcesso() != null) {
                setInteger	(stmt, ordemCampos++, processoPeriodoVO.getIdProcesso());
            } else {
            	setBoolean	(stmt, ordemCampos++, Boolean.TRUE);
            }
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível alterar os processos por periodo.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }
    
    
    
    /**
     * Inclui um processo por período
     * 
     * @param processoPeriodoVO
     * @return
     */
    public void incluiProcessoPeriodo(ProcessoPeriodoVO processoPeriodoVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_PROCESSO_PER  " +
        				"      (COD_PROC          	 , " +
        				"       NUM_ANO 	 		 , " +
        				"       NUM_MES 		  	 , " +
        				"       STA_PROC 		 	 , " +
        				"       COD_USUARIO 		 , " +
        				"		DT_INI_SIT_SRV)  	   " +
                        " VALUES (?, ?, ?, ?, ?, ?)    ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, processoPeriodoVO.getIdProcesso());
            setInteger 	(stmt, ordemCampos++, processoPeriodoVO.getAno());
            setInteger 	(stmt, ordemCampos++, processoPeriodoVO.getMes());
            setInteger 	(stmt, ordemCampos++, processoPeriodoVO.getStatus());
            setInteger  (stmt, ordemCampos++, processoPeriodoVO.getIdUsuarioUltimaAlteracao());
            setTimestamp(stmt, ordemCampos++, processoPeriodoVO.getDataUltimaAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o processo por período", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }   
    

    /**
     * Inclui historico de processo por periodo
     *  
     * @param ponderacaoVO
     * @return
     */
    public void incluiProcessoPeriodoHistorico(ProcessoPeriodoVO processoPeriodoVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_PROCESSO_PER_HIST  " +
						"      (COD_PROC          	 	  , " +
						"       NUM_ANO 	 		 	  , " +
						"       NUM_MES 		  	 	  , " +
						"       SEQ_PROCESSO	 	 	  , " +
						"       STA_PROC 		 	 	  , " +
						"       COD_USUARIO 		 	  , " +
						"		DT_INI_SIT_SRV)  	   	 	" +
				        " VALUES (?, ?, ?, ?, ?, ?, ?)      ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, processoPeriodoVO.getIdProcesso());
            setInteger 	(stmt, ordemCampos++, processoPeriodoVO.getAno());
            setInteger 	(stmt, ordemCampos++, processoPeriodoVO.getMes());
            setInteger	(stmt, ordemCampos++, obtemProximaSequenciaHistorio(processoPeriodoVO.getIdProcesso(), processoPeriodoVO.getAno(), processoPeriodoVO.getMes()));
            setInteger 	(stmt, ordemCampos++, processoPeriodoVO.getStatus());
            setInteger  (stmt, ordemCampos++, processoPeriodoVO.getIdUsuarioUltimaAlteracao());
            setTimestamp(stmt, ordemCampos++, processoPeriodoVO.getDataUltimaAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o historico do processo por periodo ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }      

    
    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idPonderacao
     * @param ano
     * @param mes
     * @return
     */
    private int obtemProximaSequenciaHistorio(Integer idProcesso, Integer ano, Integer mes) throws PersistenciaException {

        String query =  "SELECT MAX(SEQ_PROCESSO)      	" +
                        "  FROM SRV_PROCESSO_PER_HIST  	" +
                        " WHERE COD_PROC   = ? 			" +
						"   AND NUM_ANO    = ? 			" +
						"   AND NUM_MES    = ? 			";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, idProcesso);
            setInteger(stmt, ordemCampos++, ano);
            setInteger(stmt, ordemCampos++, mes);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, 1) + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico da processo por periodo.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }

    /**
     * 
     * @param processoPeriodoVO
     * @param param
     * @throws PersistenciaException
     */
    public void geraProcesso(ProcessoPeriodoVO processoPeriodoVO, String param) throws PersistenciaException {

        String query =  " SELECT COD_PROC, ORDEM_PROC, NOME_PROC, DESCR_PROC, FLG_ATIV_PROC, DT_PROC FROM SRV_PROCESSO_PERIODO ";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(param);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível executar geracao do processo por período", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }   

}