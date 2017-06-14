package br.com.marisa.srv.ponderacao.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.ponderacao.vo.PonderacaoVO;


/**
 * Classe para tratar dos métodos de acesso a dados de Ponderação 
 * 
 * @author Walter Fontes
 */
public class PonderacaoDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(PonderacaoDAO.class);


	/**
	 * Obtém lista de ponderações conforme filtro
	 * 
	 * @param idIndicador
	 * @param idGrupoIndicador
	 * @param idGrupoRemVar
	 * @param idCargo
	 * @return
	 * @throws PersistenciaException
	 */
	public List obtemPonderacoes(Integer idIndicador, Integer idGrupoIndicador, Integer idGrupoRemVar, Integer idCargo) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_POND, A.COD_GRP_REM_VAR, A.COD_CARGO, A.COD_GRP_INDIC, A.COD_INDIC, A.NUM_PESO, A.COD_UN_PESO, A.VLR_PREMIO, " +
				"        A.DT_INI_SIT_SRV, A.COD_USUARIO, B.DESCR_GRP_REM_VAR_ONLINE, C.DESCR_GRP_INDIC, D.DESCR_INDIC, E.DESCR_CARGO		" +
				"   FROM SRV_PONDERACAO A, SRV_GRUPO_REM_VARIAVEL B, SRV_GRUPO_INDICADOR C, SRV_INDICADOR D, SRV_CARGO E 					" +
				"  WHERE A.COD_GRP_REM_VAR 	= B.COD_GRP_REM_VAR (+)	" +
				"    AND A.COD_GRP_INDIC 	= C.COD_GRP_INDIC 	(+)	" +
				"    AND A.COD_INDIC 		= D.COD_INDIC 		(+)	" +
				"    AND A.COD_CARGO 		= E.COD_CARGO 		(+) ");
		
		List parametros = new ArrayList();
		if (idIndicador != null) {
			query.append(" AND A.COD_INDIC = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idIndicador));
		}
		if (idGrupoIndicador != null) {
			query.append(" AND A.COD_GRP_INDIC = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idGrupoIndicador));
		}
		if (idGrupoRemVar != null) {
			query.append(" AND A.COD_GRP_REM_VAR = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idGrupoRemVar));
		}
		if (idCargo != null) {
			query.append(" AND A.COD_CARGO = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idCargo));
		}
		
		query.append(" ORDER BY A.COD_POND ");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List ponderacoes = new ArrayList(); 
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
			while (rs.next()) {
				PonderacaoVO ponderacaoVO = new PonderacaoVO();
				ponderacaoVO.setIdPonderacao(getInteger(rs, "COD_POND"));
				ponderacaoVO.setIdGrupoRemVar(getInteger(rs, "COD_GRP_REM_VAR"));
				ponderacaoVO.setDescricaoGrupoRemVar(getString(rs, "DESCR_GRP_REM_VAR_ONLINE"));
				ponderacaoVO.setIdCargo(getInteger(rs, "COD_CARGO"));
				ponderacaoVO.setDescricaoCargo(getString(rs, "DESCR_CARGO"));
				ponderacaoVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				ponderacaoVO.setDescricaoGrupoIndicador(getString(rs, "DESCR_GRP_INDIC"));
				ponderacaoVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				ponderacaoVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				ponderacaoVO.setPeso(getDouble(rs, "NUM_PESO"));
				ponderacaoVO.setUnidadePeso(getInteger(rs, "COD_UN_PESO"));
				ponderacaoVO.setValorPremio(getDouble(rs, "VLR_PREMIO"));
				ponderacaoVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				ponderacaoVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				ponderacoes.add(ponderacaoVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a lista de ponderações.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return ponderacoes;
	}
	
	
	/**
	 * Obtém ponderação
	 * 
	 * @param idPonderacao
	 * @return
	 * @throws PersistenciaException
	 */
	public PonderacaoVO obtemPonderacao(Integer idPonderacao) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_POND, A.COD_GRP_REM_VAR, A.COD_CARGO, A.COD_GRP_INDIC, A.COD_INDIC, A.NUM_PESO, A.COD_UN_PESO, A.VLR_PREMIO, " +
				"        A.DT_INI_SIT_SRV, A.COD_USUARIO, B.DESCR_GRP_REM_VAR_ONLINE, C.DESCR_GRP_INDIC, D.DESCR_INDIC, E.DESCR_CARGO		" +
				"   FROM SRV_PONDERACAO A, SRV_GRUPO_REM_VARIAVEL B, SRV_GRUPO_INDICADOR C, SRV_INDICADOR D, SRV_CARGO E 					" +
				"  WHERE A.COD_GRP_REM_VAR 	= B.COD_GRP_REM_VAR (+)	" +
				"    AND A.COD_GRP_INDIC 	= C.COD_GRP_INDIC 	(+)	" +
				"    AND A.COD_INDIC 		= D.COD_INDIC 		(+)	" +
				"    AND A.COD_CARGO 		= E.COD_CARGO 		(+) " +
				"    AND A.COD_POND         = ?                     ");
		
		List parametros = new ArrayList();
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idPonderacao));
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		PonderacaoVO ponderacaoVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ponderacaoVO = new PonderacaoVO();
				ponderacaoVO.setIdPonderacao(getInteger(rs, "COD_POND"));
				ponderacaoVO.setIdGrupoRemVar(getInteger(rs, "COD_GRP_REM_VAR"));
				ponderacaoVO.setDescricaoGrupoRemVar(getString(rs, "DESCR_GRP_REM_VAR_ONLINE"));
				ponderacaoVO.setIdCargo(getInteger(rs, "COD_CARGO"));
				ponderacaoVO.setDescricaoCargo(getString(rs, "DESCR_CARGO"));
				ponderacaoVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				ponderacaoVO.setDescricaoGrupoIndicador(getString(rs, "DESCR_GRP_INDIC"));
				ponderacaoVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				ponderacaoVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				ponderacaoVO.setPeso(getDouble(rs, "NUM_PESO"));
				ponderacaoVO.setUnidadePeso(getInteger(rs, "COD_UN_PESO"));
				ponderacaoVO.setValorPremio(getDouble(rs, "VLR_PREMIO"));
				ponderacaoVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				ponderacaoVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a ponderação.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return ponderacaoVO;
	}	
	
	

    /**
     * Altera a ponderação
     * 
     * @param ponderacaoVO
     * @return
     */
    public void alteraPonderacao(PonderacaoVO ponderacaoVO) throws PersistenciaException {

        String query =  "UPDATE SRV_PONDERACAO           " +
        				"   SET COD_GRP_REM_VAR		= ?, " +
        				"		COD_CARGO			= ?, " +
        				"		COD_GRP_INDIC		= ?, " +
        				"		COD_INDIC			= ?, " +
        				"		NUM_PESO 			= ?, " +
        				"		COD_UN_PESO 		= ?, " +
        				"		VLR_PREMIO 			= ?, " +
        				"		DT_INI_SIT_SRV 		= ?, " +
        				"       COD_USUARIO 		= ?, " +
        				"       COD_TIPO_FIL 		= ?, " +
        				"       COD_FIL		 		= ?  " +
        				" WHERE COD_POND       		= ?  ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getIdGrupoRemVar());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getIdCargo());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getIdGrupoIndicador());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getIdIndicador());
            setDouble 	(stmt, ordemCampos++, ponderacaoVO.getPeso());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getUnidadePeso());
            setDouble	(stmt, ordemCampos++, ponderacaoVO.getValorPremio());
            setTimestamp(stmt, ordemCampos++, ponderacaoVO.getDataUltimaAlteracao());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getIdUsuarioUltimaAlteracao());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getIdTipoFilial());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getIdFilial());
            setInteger  (stmt, ordemCampos++, ponderacaoVO.getIdPonderacao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível alterar a ponderacao.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }
    
    
    
    /**
     * Inclui uma ponderação
     * 
     * @param ponderacaoVO
     * @return
     */
    public void incluiPonderacao(PonderacaoVO ponderacaoVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_PONDERACAO    " +
        				"      (COD_POND          	 , " +
        				"       COD_GRP_REM_VAR 	 , " +
        				"       COD_CARGO 		  	 , " +
        				"       COD_GRP_INDIC 		 , " +
        				"       COD_INDIC 		  	 , " +
        				"		NUM_PESO 	  		 , " +
        				"		COD_UN_PESO 	  	 , " +
        				"		VLR_PREMIO 	  		 , " +
        				"		DT_INI_SIT_SRV 		 , " +
        				"       COD_TIPO_FIL 		 , " +
        				"       COD_FIL		 		 , " +
        				"		COD_USUARIO)  		   " +
                        " VALUES (SEQ_SRV_PONDERACAO.nextVal, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, ponderacaoVO.getIdGrupoRemVar());
            setInteger 	(stmt, ordemCampos++, ponderacaoVO.getIdCargo());
            setInteger 	(stmt, ordemCampos++, ponderacaoVO.getIdGrupoIndicador());
            setInteger 	(stmt, ordemCampos++, ponderacaoVO.getIdIndicador());
            setDouble 	(stmt, ordemCampos++, ponderacaoVO.getPeso());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getUnidadePeso());
            setDouble	(stmt, ordemCampos++, ponderacaoVO.getValorPremio());
            setTimestamp(stmt, ordemCampos++, ponderacaoVO.getDataUltimaAlteracao());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getIdTipoFilial());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getIdFilial());
            setInteger  (stmt, ordemCampos++, ponderacaoVO.getIdUsuarioUltimaAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir a ponderacao", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }   
    

    /**
     * Inclui historico de ponderacao 
     *  
     * @param ponderacaoVO
     * @return
     */
    public void incluiPonderacaoHistorico(PonderacaoVO ponderacaoVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_PONDERACAO_HIST  " +
						"      (COD_POND          	    , " +
						"       SEQ_POND          	    , " +
        				"       COD_GRP_REM_VAR 	 	, " +
        				"       COD_CARGO 		  	 	, " +
        				"       COD_GRP_INDIC 		 	, " +
						"       COD_INDIC 		  	    , " +
						"		NUM_PESO 	  		    , " +
						"		COD_UN_PESO 	  	    , " +
						"		VLR_PREMIO 	  		    , " +
						"		DT_INI_SIT_SRV 		    , " +
        				"       COD_TIPO_FIL 		 	, " +
        				"       COD_FIL		 		 	, " +
						"		COD_USUARIO)  		      " +
				        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, ponderacaoVO.getIdPonderacao());
            setInteger	(stmt, ordemCampos++, obtemProximaSequenciaHistorio(ponderacaoVO.getIdPonderacao().intValue()));
            setInteger 	(stmt, ordemCampos++, ponderacaoVO.getIdGrupoRemVar());
            setInteger 	(stmt, ordemCampos++, ponderacaoVO.getIdCargo());
            setInteger 	(stmt, ordemCampos++, ponderacaoVO.getIdGrupoIndicador());
            setInteger 	(stmt, ordemCampos++, ponderacaoVO.getIdIndicador());
            setDouble 	(stmt, ordemCampos++, ponderacaoVO.getPeso());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getUnidadePeso());
            setDouble	(stmt, ordemCampos++, ponderacaoVO.getValorPremio());
            setTimestamp(stmt, ordemCampos++, ponderacaoVO.getDataUltimaAlteracao());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getIdTipoFilial());
            setInteger	(stmt, ordemCampos++, ponderacaoVO.getIdFilial());
            setInteger  (stmt, ordemCampos++, ponderacaoVO.getIdUsuarioUltimaAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o historico da ponderacao ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }      

    
    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idPonderacao
     * @return
     */
    private int obtemProximaSequenciaHistorio(int idPonderacao) throws PersistenciaException {

        String query =  "SELECT MAX(SEQ_POND)        " +
                        "  FROM SRV_PONDERACAO_HIST  " +
                        " WHERE COD_POND =?          ";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, idPonderacao);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, 1) + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico da ponderacao " + idPonderacao, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }
    
    
	/**
	 * Excluir ponderação
	 * 
	 * @param idPonderacao
	 * @return
	 * @throws PersistenciaException
	 */
	public void excluiPonderacao(Integer idPonderacao) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" DELETE FROM SRV_PONDERACAO   " +	
				"  WHERE COD_POND = ?    	   ");
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idPonderacao)); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível excluir ponderação.", e);
		} finally {
			closeStatement(stmt);
		}
	}		    

	public List obtemPonderacoes(Integer idIndicador, Integer idGrupoIndicador, Integer idGrupoRemVar, Integer idCargo, Integer idTipoFilial, Integer idFilial) throws PersistenciaException {
		List ponderacoes = new ArrayList();

		StringBuffer query = new StringBuffer(
		" SELECT A.COD_POND                 ,A.COD_GRP_REM_VAR          ,A.COD_CARGO     " +
		"           ,A.COD_GRP_INDIC            ,A.COD_INDIC                ,A.NUM_PESO  " +
		"               ,A.COD_UN_PESO              ,A.VLR_PREMIO               ,A.DT_INI" +
		"_SIT_SRV           ,A.COD_USUARIO              ,B.DESCR_GRP_REM_VAR_ONLINE      " +
		"  ,C.DESCR_GRP_INDIC                 ,D.DESCR_INDIC                     ,E.DESCR" +
		"_CARGO                     ,F.COD_TIPO_FIL                    ,F.DESCR_TIPO_FIL " +
		"                 ,G.COD_FIL                         ,G.DESCR_FIL                " +
		"  FROM SRV_PONDERACAO A                  ,SRV_GRUPO_REM_VARIAVEL B          ,SRV" +
		"_GRUPO_INDICADOR C             ,SRV_INDICADOR D                   ,SRV_CARGO E  " +
		"                     ,SRV_TIPO_FILIAL F                 ,SRV_FILIAL G           " +
		"      WHERE A.COD_GRP_REM_VAR  = B.COD_GRP_REM_VAR     (+)    AND A.COD_GRP_INDI" +
		"C    = C.COD_GRP_INDIC       (+)    AND A.COD_INDIC        = D.COD_INDIC        " +
		"   (+)    AND A.COD_CARGO        = E.COD_CARGO           (+)    AND A.COD_TIPO_F" +
		"IL     = F.COD_TIPO_FIL        (+)    AND A.COD_FIL          = G.COD_FIL        " +
		"     (+) ");

		List parametros = new ArrayList();
		if(idIndicador != null) {
			query.append(" AND A.COD_INDIC = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idIndicador));
		}
		if(idGrupoIndicador != null){
			query.append(" AND A.COD_GRP_INDIC = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idGrupoIndicador));
		}
		if(idGrupoRemVar != null) {
			query.append(" AND A.COD_GRP_REM_VAR = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idGrupoRemVar));
		}
		if(idCargo != null) {
			query.append(" AND A.COD_CARGO = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idCargo));
		}
		if(idTipoFilial != null) {
			query.append(" AND A.COD_TIPO_FIL = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idTipoFilial));
		}
		if(idFilial != null) {
			query.append(" AND A.COD_FIL = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idFilial));
		}
		query.append(" ORDER BY A.COD_POND ");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			PonderacaoVO ponderacaoVO;
			for(rs = stmt.executeQuery(); rs.next(); ponderacoes.add(ponderacaoVO)) {
				ponderacaoVO = new PonderacaoVO();
				ponderacaoVO.setIdPonderacao(getInteger(rs, "COD_POND"));
				ponderacaoVO.setIdGrupoRemVar(getInteger(rs, "COD_GRP_REM_VAR"));
				ponderacaoVO.setDescricaoGrupoRemVar(getString(rs, "DESCR_GRP_REM_VAR_ONLINE"));
				ponderacaoVO.setIdCargo(getInteger(rs, "COD_CARGO"));
				ponderacaoVO.setDescricaoCargo(getString(rs, "DESCR_CARGO"));
				ponderacaoVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				ponderacaoVO.setDescricaoGrupoIndicador(getString(rs, "DESCR_GRP_INDIC"));
				ponderacaoVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				ponderacaoVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				ponderacaoVO.setPeso(getDouble(rs, "NUM_PESO"));
				ponderacaoVO.setUnidadePeso(getInteger(rs, "COD_UN_PESO"));
				ponderacaoVO.setValorPremio(getDouble(rs, "VLR_PREMIO"));
				ponderacaoVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				ponderacaoVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				ponderacaoVO.setDescrTipoFil(getString(rs, "DESCR_TIPO_FIL"));
				ponderacaoVO.setDescrFil(getString(rs, "DESCR_FIL"));
				ponderacaoVO.setIdFilial(getInteger(rs, "COD_FIL"));
				ponderacaoVO.setIdTipoFilial(getInteger(rs, "COD_TIPO_FIL"));
			}
		} catch(Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a lista de ponderações.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return ponderacoes;
	}

	public List obtemListaFiliais() throws PersistenciaException {

		List filiais;
		filiais = new ArrayList();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String query = "SELECT COD_FIL, DESCR_FIL FROM SRV_FILIAL";
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			FilialVO filial;
			for (rs = stmt.executeQuery(); rs.next(); filiais.add(filial)) {
				filial = new FilialVO();
				filial.setCodFilial(getInteger(rs, "COD_FIL"));
				filial.setDescricao(getString(rs, "DESCR_FIL"));
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a lista de filiais.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return filiais;
	}

}