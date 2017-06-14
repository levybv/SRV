package br.com.marisa.srv.escala.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.escala.vo.FaixaEscalaVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;


/**
 * Classe para tratar dos métodos de acesso a dados de faixas de escalas 
 * 
 * @author Walter Fontes
 */
public class FaixaEscalaDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(FaixaEscalaDAO.class);
    

    /**
     * Obtem a lista de faixas de escalas
     * 
     * @param idEscala 
     * @return
     * @throws PersistenciaException
     */
	public List<FaixaEscalaVO> obtemFaixasEscalas(Integer idEscala) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"SELECT COD_ESCALA, NUM_SEQ_ESCALA_FX, NUM_INI_FX, NUM_FIM_FX, COD_UN_FX, NUM_REALZ_FX, COD_UN_REALZ_FX, " +
				"       FLG_PCT_100, NUM_LIMITE_FX, DT_INI_SIT_SRV, COD_USUARIO                                          " +
				"  FROM SRV_ESCALA_FAIXA  																				 " +
		 		" WHERE COD_ESCALA = ?	 																				 " +
		 		" ORDER BY NUM_SEQ_ESCALA_FX 																			 " );

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<FaixaEscalaVO> faixasEscalas = new ArrayList<FaixaEscalaVO>();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i=1;
			if (idEscala != null) {
				setInteger(stmt, i++, idEscala);
			}
			rs = stmt.executeQuery();
		         
			while (rs.next()) {
				
				FaixaEscalaVO faixaEscalaVO = new FaixaEscalaVO();
				faixaEscalaVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				faixaEscalaVO.setSequencial(getInteger(rs, "NUM_SEQ_ESCALA_FX"));
				faixaEscalaVO.setFaixaInicial(getDouble(rs, "NUM_INI_FX"));
				faixaEscalaVO.setFaixaFinal(getDouble(rs, "NUM_FIM_FX"));
				faixaEscalaVO.setIdUnidadeFaixa(getInteger(rs, "COD_UN_FX"));
				faixaEscalaVO.setRealizado(getDouble(rs, "NUM_REALZ_FX"));
				faixaEscalaVO.setIdUnidadeRealizado(getInteger(rs, "COD_UN_REALZ_FX"));
				faixaEscalaVO.setPercentual(getBoolean(rs, "FLG_PCT_100"));
				faixaEscalaVO.setLimite(getDouble(rs, "NUM_LIMITE_FX"));
				faixaEscalaVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				faixaEscalaVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				faixasEscalas.add(faixaEscalaVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de faixas de escalas ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		
		return faixasEscalas;
	}
	
	
	
    /**
     * Obtem uma faixa de escala
     * 
     * @param idEscala 
     * @param sequencial 
     * @return
     * @throws PersistenciaException
     */
	public FaixaEscalaVO obtemEscala(Integer idEscala, Integer sequencial) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"SELECT COD_ESCALA, NUM_SEQ_ESCALA_FX, NUM_INI_FX, NUM_FIM_FX, COD_UN_FX, NUM_REALZ_FX, COD_UN_REALZ_FX, " +
				"       FLG_PCT_100, NUM_LIMITE_FX, DT_INI_SIT_SRV, COD_USUARIO                                          " +
				"  FROM SRV_ESCALA_FAIXA  																				 " +
		 		" WHERE COD_ESCALA         = ?	 																		 " +
		 		"   AND NUM_SEQ_ESCALA_FX  = ?																			 "); 	
		 
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FaixaEscalaVO faixaEscalaVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i=1;
			setInteger(stmt, i++, idEscala);
			setInteger(stmt, i++, sequencial);
			rs = stmt.executeQuery();
		         
			if (rs.next()) {
				
				faixaEscalaVO = new FaixaEscalaVO();
				faixaEscalaVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				faixaEscalaVO.setSequencial(getInteger(rs, "NUM_SEQ_ESCALA_FX"));
				faixaEscalaVO.setFaixaInicial(getDouble(rs, "NUM_INI_FX"));
				faixaEscalaVO.setFaixaFinal(getDouble(rs, "NUM_FIM_FX"));
				faixaEscalaVO.setIdUnidadeFaixa(getInteger(rs, "COD_UN_FX"));
				faixaEscalaVO.setRealizado(getDouble(rs, "NUM_REALZ_FX"));
				faixaEscalaVO.setIdUnidadeRealizado(getInteger(rs, "COD_UN_REALZ_FX"));
				faixaEscalaVO.setPercentual(getBoolean(rs, "FLG_PCT_100"));
				faixaEscalaVO.setLimite(getDouble(rs, "NUM_LIMITE_FX"));
				faixaEscalaVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				faixaEscalaVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a faixa de escala " + idEscala + "/" + sequencial, e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		
		return faixaEscalaVO;
	}
	

	/**
	 * Incluir faixa de escala
	 * 
	 * @param faixaEscalaVO
	 * @return
	 * @throws PersistenciaException
	 */
	public void incluiFaixaEscala(FaixaEscalaVO faixaEscalaVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_ESCALA_FAIXA (                                       					 " +
                "        COD_ESCALA, NUM_SEQ_ESCALA_FX, NUM_INI_FX, NUM_FIM_FX, COD_UN_FX, NUM_REALZ_FX, " +
                "        COD_UN_REALZ_FX, FLG_PCT_100, NUM_LIMITE_FX, DT_INI_SIT_SRV, COD_USUARIO)       " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)              								 "); 
				 
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	faixaEscalaVO.getIdEscala())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	faixaEscalaVO.getSequencial())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	faixaEscalaVO.getFaixaInicial())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	faixaEscalaVO.getFaixaFinal())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	faixaEscalaVO.getIdUnidadeFaixa())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	faixaEscalaVO.getRealizado())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	faixaEscalaVO.getIdUnidadeRealizado())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_BOOLEAN, 	faixaEscalaVO.getPercentual())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	faixaEscalaVO.getLimite())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, 	faixaEscalaVO.getDataUltimaAlteracao())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	faixaEscalaVO.getIdUsuarioUltimaAlteracao())); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir faixa da escala.", e);
		} finally {
			closeStatement(stmt);
		}
	}	
	
	
	/**
	 * Excluir todas as faixas de uma escala
	 * 
	 * @param idEscala
	 * @return
	 * @throws PersistenciaException
	 */
	public void excluiFaixasEscala(Integer idEscala) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" DELETE FROM SRV_ESCALA_FAIXA  " +
				"  WHERE COD_ESCALA = ?         ");
				 
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
			throw new PersistenciaException(log, "Nao foi possível excluir as faixas da escala.", e);
		} finally {
			closeStatement(stmt);
		}
	}		
	
	
	/**
	 * Incluir histórico de faixa de escala
	 * 
	 * @param faixaEscalaVO
	 * @return
	 * @throws PersistenciaException
	 */
	public void incluiFaixaEscalaHistorico(FaixaEscalaVO faixaEscalaVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_ESCALA_FAIXA_HIST (                                       					 	" +
                "        COD_ESCALA, NUM_SEQ_ESCALA_FX, SEQ_FAIXA_ESCALA, NUM_INI_FX, NUM_FIM_FX, COD_UN_FX, 		" +
                "        NUM_REALZ_FX, COD_UN_REALZ_FX, FLG_PCT_100, NUM_LIMITE_FX, DT_INI_SIT_SRV, COD_USUARIO)    " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)                                         				"); 
				 
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	faixaEscalaVO.getIdEscala())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	faixaEscalaVO.getSequencial())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER,  new Integer(obtemProximaSequenciaHistorio(faixaEscalaVO.getIdEscala(), faixaEscalaVO.getSequencial())))); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	faixaEscalaVO.getFaixaInicial())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	faixaEscalaVO.getFaixaFinal())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	faixaEscalaVO.getIdUnidadeFaixa())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	faixaEscalaVO.getRealizado())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	faixaEscalaVO.getIdUnidadeRealizado())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_BOOLEAN, 	faixaEscalaVO.getPercentual())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	faixaEscalaVO.getLimite())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, 	faixaEscalaVO.getDataUltimaAlteracao())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	faixaEscalaVO.getIdUsuarioUltimaAlteracao())); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir historico de faixa de escala.", e);
		} finally {
			closeStatement(stmt);
		}
	}		


    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idEscala
     * @param sequencial
     * @return
     */
    private int obtemProximaSequenciaHistorio(Integer idEscala, Integer sequencial) throws PersistenciaException {

        String query =  " SELECT MAX(SEQ_FAIXA_ESCALA)   	" +
                        "   FROM SRV_ESCALA_FAIXA_HIST   	" +
						"  WHERE COD_ESCALA         = ?    	" +
						"    AND NUM_SEQ_ESCALA_FX  = ? 	";            
						
		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idEscala));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, sequencial));
						
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
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico da faixa de escala", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }

    /**
     * 
     * @param idEscala
     * @param realizado
     * @return
     * @throws PersistenciaException
     */
	public FaixaEscalaVO obtemFaixaEscala(Integer idEscala, Double realizado) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT FE.COD_ESCALA, " +
				"        FE.NUM_SEQ_ESCALA_FX, " +
				"        FE.NUM_INI_FX, " +
				"        FE.NUM_FIM_FX, " +
				"        FE.COD_UN_FX, " +
				"        FE.NUM_REALZ_FX, " +
				"        FE.COD_UN_REALZ_FX, " +
				"        FE.FLG_PCT_100, " +
				"        FE.NUM_LIMITE_FX, " +
				"        FE.DT_INI_SIT_SRV, " +
				"        FE.COD_USUARIO " +
				"   FROM SRV_ESCALA_FAIXA FE, SRV_ESCALA E " +
				"  WHERE FE.COD_ESCALA = E.COD_ESCALA " +
				"    AND FE.COD_ESCALA = ? " +
				"    AND ? BETWEEN FE.NUM_INI_FX AND FE.NUM_FIM_FX ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FaixaEscalaVO faixaEscalaVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i=1;
			if (idEscala != null) {
				setInteger(stmt, i++, idEscala);
				setDouble(stmt, i++, realizado);
			}
			rs = stmt.executeQuery();
		         
			while (rs.next()) {
				
				faixaEscalaVO = new FaixaEscalaVO();
				faixaEscalaVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				faixaEscalaVO.setSequencial(getInteger(rs, "NUM_SEQ_ESCALA_FX"));
				faixaEscalaVO.setFaixaInicial(getDouble(rs, "NUM_INI_FX"));
				faixaEscalaVO.setFaixaFinal(getDouble(rs, "NUM_FIM_FX"));
				faixaEscalaVO.setIdUnidadeFaixa(getInteger(rs, "COD_UN_FX"));
				faixaEscalaVO.setRealizado(getDouble(rs, "NUM_REALZ_FX"));
				faixaEscalaVO.setIdUnidadeRealizado(getInteger(rs, "COD_UN_REALZ_FX"));
				faixaEscalaVO.setPercentual(getBoolean(rs, "FLG_PCT_100"));
				faixaEscalaVO.setLimite(getDouble(rs, "NUM_LIMITE_FX"));
				faixaEscalaVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				faixaEscalaVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a faixa de escala: ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		
		return faixaEscalaVO;
	}

}