package br.com.marisa.srv.escala.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.escala.vo.EscalaVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;


/**
 * Classe para tratar dos métodos de acesso a dados de escalas 
 * 
 * @author Walter Fontes
 */
public class EscalaDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(EscalaDAO.class);
    
    /**
     * Obtem a lista de escalas
     * 
     * @param idEscala 
     * @param descricao
     * @return
     * @throws PersistenciaException
     */
	public List<EscalaVO> obtemEscalas(Integer idEscala, String descricao, Integer numEscala) throws PersistenciaException {
		
		 StringBuffer query =  new StringBuffer(
				"SELECT A.COD_ESCALA, A.COD_GRP_INDIC, A.COD_INDIC, A.COD_GRP_REM_VAR, A.DESCR_ESCALA, 	" +
				"       A.FLG_PCT_100, A.NUM_LIMITE_FX, A.DT_INI_SIT_SRV, A.COD_USUARIO, A.FLG_APLICA_FX_RESULT_NO_REALZ, " +
				"       B.DESCR_GRP_INDIC, C.DESCR_INDIC, D.DESCR_GRP_REM_VAR_ONLINE, A.NUM_ESCALA " +
				"  FROM SRV_ESCALA A, SRV_GRUPO_INDICADOR B, SRV_INDICADOR C, SRV_GRUPO_REM_VARIAVEL D  " +
		 		" WHERE A.COD_GRP_INDIC   = B.COD_GRP_INDIC   (+) " +
		 		"   AND A.COD_INDIC       = C.COD_INDIC       (+) " +
		 		"   AND A.COD_GRP_REM_VAR = D.COD_GRP_REM_VAR (+) " );
		 
 		if (idEscala != null) {
 			query.append(" AND A.COD_ESCALA = ? ");
 		}
        if(numEscala != null) {
            query.append(" AND A.NUM_ESCALA = ? ");
        }
 		if (descricao != null) {
 			query.append(" AND TRIM(UPPER(A.DESCR_ESCALA)) LIKE '%'||?||'%'");
 		}
 		query.append(" ORDER BY A.DESCR_ESCALA");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<EscalaVO> escalas = new ArrayList<EscalaVO>();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i=1;
			if (idEscala != null) {
				setInteger(stmt, i++, idEscala);
			}
	        if(numEscala != null) {
	            setInteger(stmt, i++, numEscala);
	        }
			if (descricao != null) {
				setString(stmt, i++, descricao.trim().toUpperCase());
			}
			rs = stmt.executeQuery();
		         
			while (rs.next()) {
				
				EscalaVO escalaVO = new EscalaVO();
				escalaVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				escalaVO.setDescricaoEscala(getString(rs, "DESCR_ESCALA"));
				escalaVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				escalaVO.setDescricaoGrupoIndicador(getString(rs, "DESCR_GRP_INDIC"));
				escalaVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				escalaVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				escalaVO.setIdGrupoRemVar(getInteger(rs, "COD_GRP_REM_VAR"));
				escalaVO.setDescricaoGrupoRemVar(getString(rs, "DESCR_GRP_REM_VAR_ONLINE"));
				escalaVO.setPercentual(getBoolean(rs, "FLG_PCT_100"));
				escalaVO.setLimite(getDouble(rs, "NUM_LIMITE_FX"));
				escalaVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				escalaVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				escalaVO.setFlgAplicaFxResultNoRealz(getString(rs, "FLG_APLICA_FX_RESULT_NO_REALZ"));
				escalaVO.setNumEscala(getInteger(rs, "NUM_ESCALA"));
				escalas.add(escalaVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de escalas ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		
		return escalas;
	}
	
	
	
    /**
     * Obtem uma escala
     * 
     * @param idEscala 
     * @return
     * @throws PersistenciaException
     */
	public EscalaVO obtemEscala(Integer idEscala) throws PersistenciaException {
		
		 StringBuffer query =  new StringBuffer(
				"SELECT A.COD_ESCALA, A.COD_GRP_INDIC, A.COD_INDIC, A.COD_GRP_REM_VAR, A.DESCR_ESCALA, 	" +
				"       A.FLG_PCT_100, A.NUM_LIMITE_FX, A.DT_INI_SIT_SRV, A.COD_USUARIO, 				" +
				"       B.DESCR_GRP_INDIC, C.DESCR_INDIC, D.DESCR_GRP_REM_VAR, A.FLG_APLICA_FX_RESULT_NO_REALZ, A.NUM_ESCALA " +
				"  FROM SRV_ESCALA A, SRV_GRUPO_INDICADOR B, SRV_INDICADOR C, SRV_GRUPO_REM_VARIAVEL D  " +
		 		" WHERE A.COD_GRP_INDIC   = B.COD_GRP_INDIC   (+) " +
		 		"   AND A.COD_INDIC       = C.COD_INDIC       (+) " +
		 		"   AND A.COD_GRP_REM_VAR = D.COD_GRP_REM_VAR (+) " +
		 		"   AND A.COD_ESCALA      = ?                     " );
		 
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		EscalaVO escalaVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i=1;
			setInteger(stmt, i++, idEscala);
			rs = stmt.executeQuery();
		         
			while (rs.next()) {
				
				escalaVO = new EscalaVO();
				escalaVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				escalaVO.setDescricaoEscala(getString(rs, "DESCR_ESCALA"));
				escalaVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				escalaVO.setDescricaoGrupoIndicador(getString(rs, "DESCR_GRP_INDIC"));
				escalaVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				escalaVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				escalaVO.setIdGrupoRemVar(getInteger(rs, "COD_GRP_REM_VAR"));
				escalaVO.setDescricaoGrupoRemVar(getString(rs, "DESCR_GRP_REM_VAR"));
				escalaVO.setPercentual(getBoolean(rs, "FLG_PCT_100"));
				escalaVO.setLimite(getDouble(rs, "NUM_LIMITE_FX"));
				escalaVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				escalaVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				escalaVO.setFlgAplicaFxResultNoRealz(getString(rs, "FLG_APLICA_FX_RESULT_NO_REALZ"));
				escalaVO.setNumEscala(getInteger(rs, "NUM_ESCALA"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a escala " + idEscala, e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		
		return escalaVO;
	}
	

	/**
	 * Incluir escala
	 * 
	 * @param escalaVO
	 * @return
	 * @throws PersistenciaException
	 */
	public void incluiEscala(EscalaVO escalaVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_ESCALA (                                       				" +
                "        COD_ESCALA, COD_GRP_INDIC, COD_INDIC, COD_GRP_REM_VAR, DESCR_ESCALA,  	" +
                "        FLG_PCT_100, NUM_LIMITE_FX, DT_INI_SIT_SRV, COD_USUARIO, NUM_ESCALA, FLG_APLICA_FX_RESULT_NO_REALZ) " +
				" VALUES (SEQ_SRV_ESCALA.nextVal, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "); 
				 
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	escalaVO.getIdGrupoIndicador())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	escalaVO.getIdIndicador())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	escalaVO.getIdGrupoRemVar())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, 	escalaVO.getDescricaoEscala()));
    	parametros.add(new ParametroSQL(PARAMTYPE_BOOLEAN, 	escalaVO.getPercentual()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	escalaVO.getLimite()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, 	escalaVO.getDataUltimaAlteracao())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	escalaVO.getIdUsuarioUltimaAlteracao())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, escalaVO.getNumEscala()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, escalaVO.getFlgAplicaFxResultNoRealz()));
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir escala.", e);
		} finally {
			closeStatement(stmt);
		}
	}	
	

	/**
	 * Alterar escala
	 * 
	 * @param escalaVO
	 * @return
	 * @throws PersistenciaException
	 */
	public void alteraEscala(EscalaVO escalaVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" UPDATE SRV_ESCALA 			 			 " +
				"    SET COD_GRP_INDIC 					= ?, " +
				"        COD_INDIC 						= ?, " +
				"        COD_GRP_REM_VAR 				= ?, " +
				"        DESCR_ESCALA 					= ?, " +
				"        FLG_PCT_100 					= ?, " +
				"        NUM_LIMITE_FX 					= ?, " +
				"        DT_INI_SIT_SRV 				= ?, " +
				"        COD_USUARIO 					= ?, " +
				" 		 FLG_APLICA_FX_RESULT_NO_REALZ 	= ?, " +
				"		 NUM_ESCALA 					= ?  " +
				"  WHERE COD_ESCALA 		= ?  ");
				 
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	escalaVO.getIdGrupoIndicador())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	escalaVO.getIdIndicador())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	escalaVO.getIdGrupoRemVar())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, 	escalaVO.getDescricaoEscala())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_BOOLEAN, 	escalaVO.getPercentual())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	escalaVO.getLimite())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, 	escalaVO.getDataUltimaAlteracao())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	escalaVO.getIdUsuarioUltimaAlteracao())); 
        parametros.add(new ParametroSQL(PARAMTYPE_STRING, 	escalaVO.getFlgAplicaFxResultNoRealz()));
        parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	escalaVO.getNumEscala()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	escalaVO.getIdEscala()));

		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível alterar a escala.", e);
		} finally {
			closeStatement(stmt);
		}
	}		
	
	
	/**
	 * Excluir escala
	 * 
	 * @param idEscala
	 * @return
	 * @throws PersistenciaException
	 */
	public void excluiEscala(Integer idEscala) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" DELETE FROM SRV_ESCALA  " +
				"  WHERE COD_ESCALA = ?   ");
				 
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idEscala)); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível excluir escala.", e);
		} finally {
			closeStatement(stmt);
		}
	}		
	
	
	/**
	 * Incluir histórico de escala
	 * 
	 * @param escalaVO
	 * @return
	 * @throws PersistenciaException
	 */
	public void incluiEscalaHistorico(EscalaVO escalaVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_ESCALA_HIST (                                          			" +
                "        COD_ESCALA, SEQ_ESCALA, COD_GRP_INDIC, COD_INDIC, COD_GRP_REM_VAR,			" +
                "        FLG_PCT_100, NUM_LIMITE_FX, DESCR_ESCALA, DT_INI_SIT_SRV, COD_USUARIO, FLG_APLICA_FX_RESULT_NO_REALZ, NUM_ESCALA) " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "); 
				 
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, escalaVO.getIdEscala())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(obtemProximaSequenciaHistorio(escalaVO.getIdEscala())))); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, escalaVO.getIdGrupoIndicador())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, escalaVO.getIdIndicador())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, escalaVO.getIdGrupoRemVar())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_BOOLEAN, escalaVO.getPercentual())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE,  escalaVO.getLimite())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING,  escalaVO.getDescricaoEscala())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE,    escalaVO.getDataUltimaAlteracao())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, escalaVO.getIdUsuarioUltimaAlteracao())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING,  escalaVO.getFlgAplicaFxResultNoRealz()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, escalaVO.getNumEscala()));
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir historico de escala.", e);
		} finally {
			closeStatement(stmt);
		}
	}		


    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idEscala
     * @return
     */
    private int obtemProximaSequenciaHistorio(Integer idEscala) throws PersistenciaException {

        String query =  " SELECT MAX(SEQ_ESCALA)   " +
                        "   FROM SRV_ESCALA_HIST   " +
						"  WHERE COD_ESCALA = ?    ";            
						
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idEscala)); 						
						
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            preencheParametros(stmt, parametros);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, 1) + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico da escala", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }	

    /**
     * 
     * @return
     * @throws PersistenciaException
     */
	public List<EscalaVO> obtemListaEscalaBonus() throws PersistenciaException {
		 StringBuffer query =  new StringBuffer(
				 " SELECT A.COD_ESCALA, " +
				 "        A.COD_GRP_INDIC, " +
				 "        A.COD_INDIC, " +
				 "        A.COD_GRP_REM_VAR, " +
				 "        A.DESCR_ESCALA, " +
				 "        A.FLG_PCT_100, " +
				 "        A.NUM_LIMITE_FX, " +
				 "        A.DT_INI_SIT_SRV, " +
				 "        A.COD_USUARIO, " +
				 "        A.FLG_APLICA_FX_RESULT_NO_REALZ, " +
				 "        B.DESCR_GRP_INDIC, " +
				 "        C.DESCR_INDIC, " +
				 "        D.DESCR_GRP_REM_VAR_ONLINE, " +
				 "        A.NUM_ESCALA " +
				 "   FROM SRV_ESCALA             A, " +
				 "        SRV_GRUPO_INDICADOR    B, " +
				 "        SRV_INDICADOR          C, " +
				 "        SRV_GRUPO_REM_VARIAVEL D " +
				 "  WHERE A.COD_GRP_INDIC = B.COD_GRP_INDIC(+) " +
				 "    AND A.COD_INDIC = C.COD_INDIC(+) " +
				 "    AND A.COD_GRP_REM_VAR = D.COD_GRP_REM_VAR(+) " +
				 "    AND A.NUM_ESCALA IS NOT NULL " +
				 " ORDER BY A.DESCR_ESCALA ");

			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			List<EscalaVO> escalas = new ArrayList<EscalaVO>();
			
			try {
				conn = getConn();
				stmt = conn.prepareStatement(query.toString());
				rs = stmt.executeQuery();
			         
				while (rs.next()) {
					
					EscalaVO escalaVO = new EscalaVO();
					escalaVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
					escalaVO.setDescricaoEscala(getString(rs, "DESCR_ESCALA"));
					escalaVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
					escalaVO.setDescricaoGrupoIndicador(getString(rs, "DESCR_GRP_INDIC"));
					escalaVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
					escalaVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
					escalaVO.setIdGrupoRemVar(getInteger(rs, "COD_GRP_REM_VAR"));
					escalaVO.setDescricaoGrupoRemVar(getString(rs, "DESCR_GRP_REM_VAR_ONLINE"));
					escalaVO.setPercentual(getBoolean(rs, "FLG_PCT_100"));
					escalaVO.setLimite(getDouble(rs, "NUM_LIMITE_FX"));
					escalaVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
					escalaVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
					escalaVO.setFlgAplicaFxResultNoRealz(getString(rs, "FLG_APLICA_FX_RESULT_NO_REALZ"));
					escalaVO.setNumEscala(getInteger(rs, "NUM_ESCALA"));
					escalas.add(escalaVO);
				}
			
			} catch (Exception e) {
				throw new PersistenciaException(log, "Não foi possível obter a lista de escalas bonus", e);
			} finally {
				closeStatementAndResultSet(stmt, rs);
			}
			
			return escalas;
	}

}