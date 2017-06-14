package br.com.marisa.srv.meta.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.meta.vo.MetaFilialVO;


/**
 * Classe para tratar dos métodos de acesso a dados de Metas de Filial
 * 
 * @author Walter Fontes
 */
public class MetaFilialDAO extends BasicDAO {


    //Log4J
    private static final Logger log = Logger.getLogger(MetaFilialDAO.class);


	/**
	 * Obtém metas de filiais
	 * 
	 * @param mes
	 * @param ano
	 * @param idFilial
	 * @param idIndicador
	 * @param descricaoIndicador
	 * @return
	 * @throws PersistenciaException
	 */
	public List obtemMetasFiliais(Integer mes, Integer ano, Integer idFilial, Integer idIndicador, String descricaoIndicador) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_INDIC, A.COD_EMP, A.COD_FIL, A.NUM_ANO, A.NUM_MES, A.COD_UN_META, A.PCT_CALC_META,  " +
				"        A.NUM_META, A.VLR_PREMIO_FIL, A.DT_INI_SIT_SRV, A.COD_USUARIO, B.DESCR_INDIC, C.DESCR_FIL " +
				"   FROM SRV_META_FILIAL A,  " +
				"        SRV_INDICADOR   B,  " +
				"        SRV_FILIAL      C   " +
				"  WHERE A.COD_INDIC = B.COD_INDIC  " +
				"    AND A.COD_EMP   = C.COD_EMP    " +
				"    AND A.COD_FIL   = C.COD_FIL    ");
				
		List parametros = new ArrayList();
	    if (mes != null) {
	    	query.append(" AND A.NUM_MES = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, mes)); 
	    }
	    if (ano != null) {
	    	query.append(" AND A.NUM_ANO = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano)); 
	    }
	    if (idFilial != null) {
	    	query.append(" AND A.COD_FIL = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idFilial)); 
	    }
	    if (idIndicador != null) {
	    	query.append(" AND A.COD_INDIC = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idIndicador)); 
	    }
	    if (ObjectHelper.isNotEmpty(descricaoIndicador)) {
	    	query.append(" AND TRIM(UPPER(B.DESCR_INDIC)) LIKE '%'||?||'%' ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, descricaoIndicador.toUpperCase().trim())); 
	    }
	    query.append(" ORDER BY A.NUM_ANO, A.NUM_MES, A.COD_INDIC, A.COD_EMP, A.COD_FIL ");	
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List metasFiliais = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
			while (rs.next()) {
				MetaFilialVO metaFilialVO = new MetaFilialVO();
				metaFilialVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				metaFilialVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				metaFilialVO.setIdFilial(getInteger(rs, "COD_FIL"));
				metaFilialVO.setAno(getInteger(rs, "NUM_ANO"));
				metaFilialVO.setMes(getInteger(rs, "NUM_MES"));
				metaFilialVO.setIdUnidadeMeta(getInteger(rs, "COD_UN_META"));
				metaFilialVO.setValorMeta(getDouble(rs, "NUM_META"));
				metaFilialVO.setPercentualMeta(getDouble(rs, "PCT_CALC_META"));
				metaFilialVO.setValorPremioFilial(getDouble(rs, "VLR_PREMIO_FIL"));
				metaFilialVO.setDataAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				metaFilialVO.setIdUsuario(getInteger(rs, "COD_USUARIO"));
				metaFilialVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				metaFilialVO.setDescricaoFilial(getString(rs, "DESCR_FIL"));
				metasFiliais.add(metaFilialVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter lista de metas de filiais ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return metasFiliais;
	}
	
	
	/**
	 * Obtém meta de filial
	 * 
	 * @param mes
	 * @param ano
	 * @param idEmpresa
	 * @param idFilial
	 * @param idIndicador
	 * @return
	 * @throws PersistenciaException
	 */
	public MetaFilialVO obtemMetaFilial(Integer mes, Integer ano, Integer idEmpresa, Integer idFilial, Integer idIndicador) throws PersistenciaException {

		StringBuffer query =  new StringBuffer(
				
				" SELECT A.COD_INDIC, A.COD_EMP, A.COD_FIL, A.NUM_ANO, A.NUM_MES, A.COD_UN_META, A.PCT_CALC_META,  " +
				"        A.NUM_META, A.VLR_PREMIO_FIL, A.DT_INI_SIT_SRV, A.COD_USUARIO, B.DESCR_INDIC, C.DESCR_FIL " +
				"   FROM SRV_META_FILIAL A,  " +
				"        SRV_INDICADOR   B,  " +
				"        SRV_FILIAL      C   " +
				"  WHERE A.COD_INDIC = B.COD_INDIC  " +
				"    AND A.COD_EMP   = C.COD_EMP    " +
				"    AND A.COD_FIL   = C.COD_FIL    " +			
				"    AND A.COD_INDIC = ?    		" +
				"    AND A.COD_EMP   = ?    		" +
				"    AND A.COD_FIL   = ?    		" +
				"    AND A.NUM_ANO   = ?    		" +
				"    AND A.NUM_MES   = ?    		");
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idIndicador)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idEmpresa)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idFilial)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, mes)); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		MetaFilialVO metaFilialVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
			if (rs.next()) {
				metaFilialVO = new MetaFilialVO();
				metaFilialVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				metaFilialVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				metaFilialVO.setIdFilial(getInteger(rs, "COD_FIL"));
				metaFilialVO.setAno(getInteger(rs, "NUM_ANO"));
				metaFilialVO.setMes(getInteger(rs, "NUM_MES"));
				metaFilialVO.setIdUnidadeMeta(getInteger(rs, "COD_UN_META"));
				metaFilialVO.setValorMeta(getDouble(rs, "NUM_META"));
				metaFilialVO.setPercentualMeta(getDouble(rs, "PCT_CALC_META"));
				metaFilialVO.setValorPremioFilial(getDouble(rs, "VLR_PREMIO_FIL"));
				metaFilialVO.setDataAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				metaFilialVO.setIdUsuario(getInteger(rs, "COD_USUARIO"));
				metaFilialVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				metaFilialVO.setDescricaoFilial(getString(rs, "DESCR_FIL"));
				
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter meta de filial.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return metaFilialVO;
	}	
	
	
	/**
	 * Incluir meta de filial
	 * 
	 * @param metaFilialVO
	 * @return
	 * @throws PersistenciaException
	 */
	public void incluiMetaFilial(MetaFilialVO metaFilialVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_META_FILIAL (                                       				" +
                "        COD_INDIC, COD_EMP, COD_FIL, NUM_ANO, NUM_MES, COD_UN_META,  				" +
				"        PCT_CALC_META, NUM_META, VLR_PREMIO_FIL, DT_INI_SIT_SRV, COD_USUARIO)      " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?)                       				"); 
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	metaFilialVO.getIdIndicador())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	metaFilialVO.getIdEmpresa())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	metaFilialVO.getIdFilial())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	metaFilialVO.getAno())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	metaFilialVO.getMes())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	metaFilialVO.getIdUnidadeMeta())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	metaFilialVO.getPercentualMeta()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	metaFilialVO.getValorMeta())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	metaFilialVO.getValorPremioFilial())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	metaFilialVO.getIdUsuario())); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir meta de filial.", e);
		} finally {
			closeStatement(stmt);
		}
	}	
	
	
	/**
	 * Excluir meta de filial
	 * 
	 * @param mes
	 * @param ano
	 * @param idEmpresa
	 * @param idFilial
	 * @param idIndicador
	 * @return
	 * @throws PersistenciaException
	 */
	public void excluiMetaFilial(Integer mes, Integer ano, Integer idEmpresa, Integer idFilial, Integer idIndicador) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" DELETE FROM SRV_META_FILIAL   " +
				"  WHERE COD_INDIC = ?    		" +
				"    AND COD_EMP   = ?    		" +
				"    AND COD_FIL   = ?    		" +
				"    AND NUM_ANO   = ?    		" +
				"    AND NUM_MES   = ?    		");
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idIndicador)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idEmpresa)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idFilial)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, mes)); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível excluir meta de filial.", e);
		} finally {
			closeStatement(stmt);
		}
	}		
	
	
	/**
	 * Incluir meta de filial
	 * 
	 * @param metaFilialVO
	 * @return
	 * @throws PersistenciaException
	 */
	public void incluiMetaFilialHistorico(MetaFilialVO metaFilialVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_META_FILIAL_HIST (                                          	 " +
                "        COD_INDIC, COD_EMP, COD_FIL, NUM_ANO, NUM_MES, SEQ_META, PCT_CALC_META, " +
                "        COD_UN_META, NUM_META, VLR_PREMIO_FIL, DT_INI_SIT_SRV, COD_USUARIO)  	 " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)                                    "); 
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFilialVO.getIdIndicador())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFilialVO.getIdEmpresa())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFilialVO.getIdFilial())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFilialVO.getAno())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFilialVO.getMes())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(obtemProximaSequenciaHistorio(metaFilialVO.getMes(), metaFilialVO.getAno(), metaFilialVO.getIdEmpresa(), metaFilialVO.getIdFilial(), metaFilialVO.getIdIndicador())))); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE,  metaFilialVO.getPercentualMeta()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFilialVO.getIdUnidadeMeta())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE,  metaFilialVO.getValorMeta())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE,  metaFilialVO.getValorPremioFilial())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE,    metaFilialVO.getDataAlteracao())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFilialVO.getIdUsuario())); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir historico de meta de filial.", e);
		} finally {
			closeStatement(stmt);
		}
	}		


    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param mes
     * @param ano
     * @param idEmpresa
     * @param idFilial
     * @param idIndicador
     * @return
     */
    private int obtemProximaSequenciaHistorio(Integer mes, Integer ano, Integer idEmpresa, Integer idFilial, Integer idIndicador) throws PersistenciaException {

        String query =  " SELECT MAX(SEQ_META)          " +
                        "   FROM SRV_META_FILIAL_HIST   " +
						"  WHERE COD_INDIC = ?    		" +
						"    AND COD_EMP   = ?    		" +
						"    AND COD_FIL   = ?    		" +
						"    AND NUM_ANO   = ?    		" +
						"    AND NUM_MES   = ?    		";            
						
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idIndicador)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idEmpresa)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idFilial)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, mes)); 						
						
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
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico da meta da filial", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }
}