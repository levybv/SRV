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
import br.com.marisa.srv.meta.vo.MetaFuncionarioVO;


/**
 * Classe para tratar dos métodos de acesso a dados de Metas de Funcionários
 * 
 * @author Walter Fontes
 */
public class MetaFuncionarioDAO extends BasicDAO {


    //Log4J
    private static final Logger log = Logger.getLogger(MetaFuncionarioDAO.class);


	/**
	 * Obtém metas de funcionários
	 * 
	 * @param mes
	 * @param ano
	 * @param idFuncionario
	 * @param nomeFuncionario
	 * @param idIndicador
	 * @param descricaoIndicador
	 * @return
	 * @throws PersistenciaException
	 */
	public List obtemMetasFuncionarios(Integer mes, Integer ano, Long idFuncionario, String nomeFuncionario, Integer idIndicador, String descricaoIndicador) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_INDIC, A.COD_FUNC, A.NUM_ANO, A.NUM_MES, A.COD_UN_META, A.NUM_META, 		" +
				"        A.DT_INI_SIT_SRV, A.COD_USUARIO, B.DESCR_INDIC, C.NOME_FUNC, A.DESCR_META      " +
				"   FROM SRV_META_FUNC_BONUS A,  " +
				"        SRV_INDICADOR   	 B,  " +
				"        SRV_FUNCIONARIO 	 C   " +
				"  WHERE A.COD_INDIC = B.COD_INDIC  " +
				"    AND A.COD_FUNC  = C.COD_FUNC   ");
				
		List parametros = new ArrayList();
	    if (mes != null) {
	    	query.append(" AND A.NUM_MES = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, mes)); 
	    }
	    if (ano != null) {
	    	query.append(" AND A.NUM_ANO = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano)); 
	    }
	    if (idFuncionario != null) {
	    	query.append(" AND A.COD_FUNC = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario)); 
	    }
	    if (ObjectHelper.isNotEmpty(nomeFuncionario)) {
	    	query.append(" AND TRIM(UPPER(C.NOME_FUNC)) LIKE '%'||?||'%' ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, nomeFuncionario.toUpperCase().trim())); 
	    }
	    if (idIndicador != null) {
	    	query.append(" AND A.COD_INDIC = ? ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idIndicador)); 
	    }
	    if (ObjectHelper.isNotEmpty(descricaoIndicador)) {
	    	query.append(" AND TRIM(UPPER(B.DESCR_INDIC)) LIKE '%'||?||'%' ");
	    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, descricaoIndicador.toUpperCase().trim())); 
	    }
	    query.append(" ORDER BY A.NUM_ANO, A.NUM_MES, A.COD_INDIC, A.COD_FUNC ");	
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List metasFuncionarios = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
			while (rs.next()) {
				MetaFuncionarioVO metaFuncionarioVO = new MetaFuncionarioVO();
				metaFuncionarioVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				metaFuncionarioVO.setIdFuncionario(getLong(rs, "COD_FUNC"));
				metaFuncionarioVO.setAno(getInteger(rs, "NUM_ANO"));
				metaFuncionarioVO.setMes(getInteger(rs, "NUM_MES"));
				metaFuncionarioVO.setIdUnidadeMeta(getInteger(rs, "COD_UN_META"));
				metaFuncionarioVO.setValorMeta(getDouble(rs, "NUM_META"));
				metaFuncionarioVO.setDescricaoMeta(getString(rs, "DESCR_META"));
				metaFuncionarioVO.setDataAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				metaFuncionarioVO.setIdUsuario(getInteger(rs, "COD_USUARIO"));
				metaFuncionarioVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				metaFuncionarioVO.setNomeFuncionario(getString(rs, "NOME_FUNC"));
				metasFuncionarios.add(metaFuncionarioVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter lista de metas de funcionarios ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return metasFuncionarios;
	}
	
	
	/**
	 * Obtém meta de funcionario
	 * 
	 * @param mes
	 * @param ano
	 * @param idFuncionario
	 * @param idIndicador
	 * @return
	 * @throws PersistenciaException
	 */
	public MetaFuncionarioVO obtemMetaFuncionario(Integer mes, Integer ano, Long idFuncionario, Integer idIndicador) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_INDIC, A.COD_FUNC, A.NUM_ANO, A.NUM_MES, A.COD_UN_META, A.NUM_META,      " +
				"        A.DT_INI_SIT_SRV, A.COD_USUARIO, B.DESCR_INDIC, C.NOME_FUNC, A.DESCR_META 		" +
				"   FROM SRV_META_FUNC_BONUS A,  " +
				"        SRV_INDICADOR   	 B,  " +
				"        SRV_FUNCIONARIO  	 C   " +
				"  WHERE A.COD_INDIC = B.COD_INDIC  " +
				"    AND A.COD_FUNC  = C.COD_FUNC   " +
				"    AND A.COD_INDIC = ?    		" +
				"    AND A.COD_FUNC  = ?    		" +
				"    AND A.NUM_ANO   = ?    		" +
				"    AND A.NUM_MES   = ?    		");
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idIndicador)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, mes)); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		MetaFuncionarioVO metaFuncionarioVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
			if (rs.next()) {
				metaFuncionarioVO = new MetaFuncionarioVO();
				metaFuncionarioVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				metaFuncionarioVO.setIdFuncionario(getLong(rs, "COD_FUNC"));
				metaFuncionarioVO.setAno(getInteger(rs, "NUM_ANO"));
				metaFuncionarioVO.setMes(getInteger(rs, "NUM_MES"));
				metaFuncionarioVO.setIdUnidadeMeta(getInteger(rs, "COD_UN_META"));
				metaFuncionarioVO.setValorMeta(getDouble(rs, "NUM_META"));
				metaFuncionarioVO.setDescricaoMeta(getString(rs, "DESCR_META"));
				metaFuncionarioVO.setDataAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				metaFuncionarioVO.setIdUsuario(getInteger(rs, "COD_USUARIO"));
				metaFuncionarioVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				metaFuncionarioVO.setNomeFuncionario(getString(rs, "NOME_FUNC"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter meta de funcionario.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return metaFuncionarioVO;
	}	


	/**
	 * Incluir meta de funcionario
	 * 
	 * @param metaFuncionarioVO
	 * @return
	 * @throws PersistenciaException
	 */
	public void incluiMetaFuncionario(MetaFuncionarioVO metaFuncionarioVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_META_FUNC_BONUS (                             " +
                "        COD_INDIC, COD_FUNC, NUM_ANO, NUM_MES, COD_UN_META,  	" +
				"        NUM_META, DESCR_META, DT_INI_SIT_SRV, COD_USUARIO)     " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)                         	"); 
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFuncionarioVO.getIdIndicador())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_LONG,    metaFuncionarioVO.getIdFuncionario())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFuncionarioVO.getAno())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFuncionarioVO.getMes())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFuncionarioVO.getIdUnidadeMeta())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE,  metaFuncionarioVO.getValorMeta())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING,  metaFuncionarioVO.getDescricaoMeta()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE,    metaFuncionarioVO.getDataAlteracao())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFuncionarioVO.getIdUsuario())); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir meta de funcionario.", e);
		} finally {
			closeStatement(stmt);
		}
	}	
	
	
	/**
	 * Excluir meta de funcionario
	 * 
	 * @param mes
	 * @param ano
	 * @param idFuncionario
	 * @param idIndicador
	 * @return
	 * @throws PersistenciaException
	 */
	public void excluiMetaFuncionario(Integer mes, Integer ano, Long idFuncionario, Integer idIndicador) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" DELETE FROM SRV_META_FUNC_BONUS   " +
				"  WHERE COD_INDIC  = ?    			" +
				"    AND COD_FUNC   = ?    			" +
				"    AND NUM_ANO    = ?    			" +
				"    AND NUM_MES    = ?    			");
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idIndicador)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_LONG,    idFuncionario)); 
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
			throw new PersistenciaException(log, "Nao foi possível excluir meta de funcionario.", e);
		} finally {
			closeStatement(stmt);
		}
	}		
	
	
	/**
	 * Incluir meta de funcionario
	 * 
	 * @param metaFuncionarioVO
	 * @return
	 * @throws PersistenciaException
	 */
	public void incluiMetaFuncionarioHistorico(MetaFuncionarioVO metaFuncionarioVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_META_FUNC_BONUS_HIST (                        " +
                "        COD_INDIC, COD_FUNC, NUM_ANO, NUM_MES, SEQ_META, 		" +
                "        COD_UN_META, NUM_META, DESCR_META, DT_INI_SIT_SRV, 	" +
                "        COD_USUARIO) 											" +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)                        	"); 
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFuncionarioVO.getIdIndicador())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_LONG,    metaFuncionarioVO.getIdFuncionario())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFuncionarioVO.getAno())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFuncionarioVO.getMes())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(obtemProximaSequenciaHistorio(metaFuncionarioVO.getMes(), metaFuncionarioVO.getAno(), metaFuncionarioVO.getIdFuncionario(), metaFuncionarioVO.getIdIndicador())))); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFuncionarioVO.getIdUnidadeMeta())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE,  metaFuncionarioVO.getValorMeta())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING,  metaFuncionarioVO.getDescricaoMeta()));
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE,    metaFuncionarioVO.getDataAlteracao())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, metaFuncionarioVO.getIdUsuario())); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir historico de meta de funcionario.", e);
		} finally {
			closeStatement(stmt);
		}
	}		


    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param mes
     * @param ano
     * @param idFuncionario
     * @param idIndicador
     * @return
     */
    private int obtemProximaSequenciaHistorio(Integer mes, Integer ano, Long idFuncionario, Integer idIndicador) throws PersistenciaException {

        String query =  " SELECT MAX(SEQ_META)          	" +
                        "   FROM SRV_META_FUNC_BONUS_HIST   " +
						"  WHERE COD_INDIC = ?    			" +
						"    AND COD_FUNC  = ?    			" +
						"    AND NUM_ANO   = ?    			" +
						"    AND NUM_MES   = ?    			";            
						
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idIndicador)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_LONG,    idFuncionario)); 
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
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico da meta do funcionario", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }
}