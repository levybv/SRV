package br.com.marisa.srv.indicador.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.enumeration.StatCalcRealzEnum;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioRealizadoVO;
import br.com.marisa.srv.indicador.vo.IndicadorVO;


/**
 * Classe para tratar dos métodos de acesso a dados de indicadores 
 * 
 * @author Walter Fontes
 */
public class IndicadorDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(IndicadorDAO.class);


	/**
	 * Obtém todos os indicadores de um grupo
	 * 
	 * @param codGrupo
	 * @return
	 * @throws PersistenciaException
	 */
	public List obtemListaIndicadores(Integer codGrupo) throws PersistenciaException {
		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_INDIC, A.DESCR_INDIC " +
				"   FROM SRV_INDICADOR A            " +
				"   WHERE FLG_INDIC_ATIV = 'S' ");
		if (codGrupo != null) {
			query.append("  AND A.COD_GRP_INDIC = ? ");
		}
		
		query.append("  ORDER BY A.DESCR_INDIC ");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List lista = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			if (codGrupo != null) {
				setInteger(stmt,1,codGrupo);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				IndicadorVO indicadorVO = new IndicadorVO();
				indicadorVO.setCodIndicador(getInteger(rs, "cod_indic"));
				indicadorVO.setDescricaoIndicador(getString(rs, "descr_indic"));
				lista.add(indicadorVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter lista de indicadores ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}
	
	public List obtemListaIndicadoresAjuste(Integer codGrupo) throws PersistenciaException {
		StringBuffer query =  new StringBuffer(
				" SELECT " +
				"		A.COD_INDIC, " +
				"		A.DESCR_INDIC " +
				" FROM " +
				"		SRV_INDICADOR A " +
				" WHERE " +
				"		A.FLG_INDIC_ATIV 	= 'S' " +
				"	AND	A.COD_INDIC_SIS 	= 'AJUS_DEB_CRED' ");
		if (codGrupo != null) {
			query.append("  AND A.COD_GRP_INDIC = ? ");
		}

		query.append("  ORDER BY A.DESCR_INDIC ");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List lista = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			if (codGrupo != null) {
				setInteger(stmt,1,codGrupo);
			}
			rs = stmt.executeQuery();
			while (rs.next()) {
				IndicadorVO indicadorVO = new IndicadorVO();
				indicadorVO.setCodIndicador(getInteger(rs, "COD_INDIC"));
				indicadorVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				lista.add(indicadorVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter lista de indicadores de ajuste ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}
	
	
	/**
	 * Obtém indicadores conforme filtro
	 * utilizar metodo q controla a conexao
	 * @param idIndicador
	 * @param descricaoIndicador
	 * @return
	 * @throws PersistenciaException
	 */
	@Deprecated
	public List<DadosIndicadorVO> obtemListaIndicadores(DadosIndicadorVO pesquisaVO, List<Integer> tiposGrupo, boolean isPesquisaSubIndicador) throws PersistenciaException {
		return obtemListaIndicadores(pesquisaVO, tiposGrupo, isPesquisaSubIndicador, Boolean.FALSE) ;
	}
	
	public List<DadosIndicadorVO> obtemListaIndicadores(DadosIndicadorVO pesquisaVO, List<Integer> tiposGrupo, boolean isPesquisaSubIndicador, Boolean fechaConexao) throws PersistenciaException {		

		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_INDIC, " +
				"        A.DESCR_INDIC, " +
				"        A.COD_GRP_INDIC, " +
				"        B.DESCR_GRP_INDIC, " +
				"        A.FLG_INDIC_ATIV, " +
				"        A.DESCR_FORMULA_INDIC, " +
				"        A.COD_VERBA_RH, " +
				"        A.DT_INI_SIT_SRV, " +
				"        A.COD_USUARIO, " +
				"        A.FORMULA_INDIC, " +
				"        A.FLG_PREENCH_ATING_IGUAL_REALZ, " +
				"        A.FLG_SENTIDO, " +
				"        A.DESCR_FONTE, " +
				"        A.DESCR_DIRETORIA " +
				"   FROM SRV_INDICADOR A, SRV_GRUPO_INDICADOR B " +
				"  WHERE A.COD_GRP_INDIC = B.COD_GRP_INDIC " +
				"    AND (A.COD_INDIC_SIS IS NULL OR A.COD_INDIC_SIS = 'SAX_PSF') ");

		List<ParametroSQL> parametroSQL = new ArrayList<ParametroSQL>();

		if (!isPesquisaSubIndicador) {
			query.append("  AND A.COD_INDIC_PAI IS NULL ");
		}
		if (ObjectHelper.isNotNull(pesquisaVO)) {
			if (pesquisaVO.isUtilizaRange()) {
				query.append("  AND A.COD_INDIC BETWEEN ? AND ? ");
				parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, pesquisaVO.getCodIndicInicio()));
				parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, pesquisaVO.getCodIndicFim()));
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getIdIndicador())) {
				query.append("  AND A.COD_INDIC = ? ");
				parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, pesquisaVO.getIdIndicador()));
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricaoIndicador())) {
				query.append("  AND UPPER(A.DESCR_INDIC) LIKE '%'||?||'%' ");
				parametroSQL.add(new ParametroSQL(PARAMTYPE_STRING, pesquisaVO.getDescricaoIndicador().toUpperCase().trim()));
			}
			if (ObjectHelper.isNotNull(pesquisaVO.getAtivo())) {
				query.append("  AND NVL(A.FLG_INDIC_ATIV, 'N') = ? ");
				parametroSQL.add(new ParametroSQL(PARAMTYPE_STRING, pesquisaVO.getAtivo()));
			} else {
				query.append("  AND NVL(A.FLG_INDIC_ATIV, 'N') = 'S' ");
			}
			if (ObjectHelper.isNotNull(pesquisaVO.getIdIndicadorPai())) {
				query.append("  AND A.COD_INDIC_PAI = ? ");
				parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, pesquisaVO.getIdIndicadorPai()));
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getIdGrupoIndicador())) {
				query.append("  AND A.COD_GRP_INDIC = ? ");
				parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, pesquisaVO.getIdGrupoIndicador()));
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricaoFonte())) {
				query.append("  AND UPPER(A.DESCR_FONTE) = ? ");
				parametroSQL.add(new ParametroSQL(PARAMTYPE_STRING, pesquisaVO.getDescricaoFonte().toUpperCase().trim()));
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricaoDiretoria())) {
				query.append("  AND UPPER(A.DESCR_DIRETORIA) = ? ");
				parametroSQL.add(new ParametroSQL(PARAMTYPE_STRING, pesquisaVO.getDescricaoDiretoria().toUpperCase().trim()));
			}
			if (tiposGrupo != null) {
				query.append("  AND B.COD_TIPO_REM_VAR IN (? ");
				for (int i=0; i<tiposGrupo.size(); i++) {
					parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, tiposGrupo.get(i)));
					if ((i+1)<tiposGrupo.size()) {
						query.append(",?");
					}
				}
				query.append(")");
			}		
		}

		query.append("  ORDER BY A.DESCR_INDIC ");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<DadosIndicadorVO> lista = new ArrayList<DadosIndicadorVO>();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				DadosIndicadorVO dadosIndicadorVO = new DadosIndicadorVO();
				dadosIndicadorVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				dadosIndicadorVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				dadosIndicadorVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				dadosIndicadorVO.setDescricaoGrupo(getString(rs, "DESCR_GRP_INDIC"));
				dadosIndicadorVO.setAtivo(getString(rs, "FLG_INDIC_ATIV"));
				dadosIndicadorVO.setFormulaConceito(getString(rs, "DESCR_FORMULA_INDIC"));
				dadosIndicadorVO.setVerbaRH(getInteger(rs, "COD_VERBA_RH"));
				dadosIndicadorVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				dadosIndicadorVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				dadosIndicadorVO.setFormulaIndicador(getString(rs, "FORMULA_INDIC"));
				dadosIndicadorVO.setFlgPreenchAtingIgualRealz(getString(rs, "FLG_PREENCH_ATING_IGUAL_REALZ"));
				dadosIndicadorVO.setDescricaoFonte(getString(rs, "DESCR_FONTE"));
				dadosIndicadorVO.setDescricaoDiretoria(getString(rs, "DESCR_DIRETORIA"));
				dadosIndicadorVO.setFlgSentido(getString(rs, "FLG_SENTIDO"));
				lista.add(dadosIndicadorVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter lista de indicadores (2) ", e);
		} finally {
			if(fechaConexao){
				closeStatementAndResultSet(stmt, rs,conn);
			}else{
				closeStatementAndResultSet(stmt, rs);	
			}
			
		}
		return lista;
	}	
	
	
	/**
	 * Obtém o indicador passado por parâmetro
	 * 
	 * @param idIndicador
	 * @return
	 * @throws PersistenciaException
	 */
	public DadosIndicadorVO obtemIndicador(int idIndicador) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_INDIC, A.DESCR_INDIC, A.COD_GRP_INDIC, B.DESCR_GRP_INDIC, A.FLG_INDIC_ATIV, A.DESCR_FORMULA_INDIC,  " +
				"        A.COD_VERBA_RH, A.COD_INDIC_SIS, A.DT_INI_SIT_SRV, A.COD_USUARIO, A.COD_ESCALA, A.FORMULA_INDIC, C.DESCR_ESCALA, A.FLG_PREENCH_ATING_IGUAL_REALZ, " +
				"        A.DESCR_FONTE, A.DESCR_DIRETORIA, A.FLG_SENTIDO " +
				"   FROM SRV_INDICADOR A, SRV_GRUPO_INDICADOR B, SRV_ESCALA C " +
				"  WHERE A.COD_GRP_INDIC    = B.COD_GRP_INDIC      " +
				"    AND A.COD_ESCALA       = C.COD_ESCALA (+)     " +
				"    AND A.COD_INDIC        = ?                    ");
				
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DadosIndicadorVO dadosIndicadorVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			setInteger(stmt, 1, idIndicador);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				dadosIndicadorVO = new DadosIndicadorVO();
				dadosIndicadorVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				dadosIndicadorVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				dadosIndicadorVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				dadosIndicadorVO.setDescricaoGrupo(getString(rs, "DESCR_GRP_INDIC"));
				dadosIndicadorVO.setAtivo(getString(rs, "FLG_INDIC_ATIV"));
				dadosIndicadorVO.setFormulaConceito(getString(rs, "DESCR_FORMULA_INDIC"));
				dadosIndicadorVO.setVerbaRH(getInteger(rs, "COD_VERBA_RH"));
				dadosIndicadorVO.setIdSistemico(getString(rs, "COD_INDIC_SIS"));
				dadosIndicadorVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				dadosIndicadorVO.setDescricaoEscala(getString(rs, "DESCR_ESCALA"));
				dadosIndicadorVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				dadosIndicadorVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				dadosIndicadorVO.setFormulaIndicador(getString(rs, "FORMULA_INDIC"));
				dadosIndicadorVO.setFlgPreenchAtingIgualRealz(getString(rs, "FLG_PREENCH_ATING_IGUAL_REALZ"));
				dadosIndicadorVO.setDescricaoFonte(getString(rs, "DESCR_FONTE"));
				dadosIndicadorVO.setDescricaoDiretoria(getString(rs, "DESCR_DIRETORIA"));
				dadosIndicadorVO.setFlgSentido(getString(rs, "FLG_SENTIDO"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter o indicador " + idIndicador, e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return dadosIndicadorVO;
	}
	
	
	/**
	 * Obtém o indicador passado por parâmetro
	 * 
	 * @param idSistemico
	 * @return
	 * @throws PersistenciaException
	 */
	public DadosIndicadorVO obtemIndicador(String idSistemico) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_INDIC, A.DESCR_INDIC, A.COD_GRP_INDIC, B.DESCR_GRP_INDIC, A.FLG_INDIC_ATIV, A.DESCR_FORMULA_INDIC, 	" +
				"        A.COD_VERBA_RH, A.COD_INDIC_SIS, A.DT_INI_SIT_SRV, A.COD_USUARIO, A.COD_ESCALA, A.FLG_PREENCH_ATING_IGUAL_REALZ, C.DESCR_ESCALA  			" +
				"   FROM SRV_INDICADOR A, SRV_GRUPO_INDICADOR B, SRV_ESCALA C " +
				"  WHERE A.COD_GRP_INDIC = B.COD_GRP_INDIC      " +
				"    AND A.COD_ESCALA    = C.COD_ESCALA         " +
				"    AND A.COD_INDIC_SIS = ?                    ");
				
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DadosIndicadorVO dadosIndicadorVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			setString(stmt, 1, idSistemico);
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				dadosIndicadorVO = new DadosIndicadorVO();
				dadosIndicadorVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				dadosIndicadorVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				dadosIndicadorVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				dadosIndicadorVO.setDescricaoGrupo(getString(rs, "DESCR_GRP_INDIC"));
				dadosIndicadorVO.setAtivo(getString(rs, "FLG_INDIC_ATIV"));
				dadosIndicadorVO.setFormulaConceito(getString(rs, "DESCR_FORMULA_INDIC"));
				dadosIndicadorVO.setVerbaRH(getInteger(rs, "COD_VERBA_RH"));
				dadosIndicadorVO.setIdSistemico(getString(rs, "COD_INDIC_SIS"));
				dadosIndicadorVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				dadosIndicadorVO.setDescricaoEscala(getString(rs, "DESCR_ESCALA"));
				dadosIndicadorVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				dadosIndicadorVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				dadosIndicadorVO.setFlgPreenchAtingIgualRealz(getString(rs, "FLG_PREENCH_ATING_IGUAL_REALZ"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter o indicador " + idSistemico, e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return dadosIndicadorVO;
	}	
	
	
    /**
     * Altera o indicador
     * 
     * @param dadosIndicadorVO
     * @return
     */
    public void alteraIndicador(DadosIndicadorVO dadosIndicadorVO) throws PersistenciaException {

        String query =  "UPDATE SRV_INDICADOR 						 " +
        				"   SET DESCR_INDIC 					= ?, " +
        				"       COD_GRP_INDIC       			= ?, " +
        				"		COD_VERBA_RH 					= ?, " +
        				"		FLG_INDIC_ATIV 					= ?, " +
        				"		DESCR_FORMULA_INDIC 			= ?, " +
        				"       COD_ESCALA              		= ?, " +
        				"		DT_INI_SIT_SRV 					= ?, " +
        				"       COD_USUARIO 					= ?, " +
        				"		FORMULA_INDIC					= ?, " +
        				"       FLG_PREENCH_ATING_IGUAL_REALZ 	= ?, " +
        				"       DESCR_FONTE					 	= ?, " +
        				"       FLG_SENTIDO					 	= ?, " +
        				"       DESCR_DIRETORIA				 	= ?  " +
        				" WHERE COD_INDIC     					= ?  ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getDescricaoIndicador());
            setInteger	(stmt, ordemCampos++, dadosIndicadorVO.getIdGrupoIndicador());
            setInteger	(stmt, ordemCampos++, dadosIndicadorVO.getVerbaRH());
            setString   (stmt, ordemCampos++, dadosIndicadorVO.getAtivo());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFormulaConceito());
            setInteger 	(stmt, ordemCampos++, dadosIndicadorVO.getIdEscala());
            setTimestamp(stmt, ordemCampos++, dadosIndicadorVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, dadosIndicadorVO.getIdUsuarioUltimaAlteracao());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFormulaIndicador());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFlgPreenchAtingIgualRealz());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getDescricaoFonte()==null?dadosIndicadorVO.getDescricaoFonte():dadosIndicadorVO.getDescricaoFonte().trim().toUpperCase());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFlgSentido());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getDescricaoDiretoria()==null?dadosIndicadorVO.getDescricaoDiretoria():dadosIndicadorVO.getDescricaoDiretoria().trim().toUpperCase());
            setInteger  (stmt, ordemCampos++, dadosIndicadorVO.getIdIndicador());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível alterar o indicador.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }
    
    
    
    /**
     * Inclui um indicador
     * 
     * @param dadosIndicadorVO
     * @return
     */
    public int incluiIndicador(DadosIndicadorVO dadosIndicadorVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_INDICADOR      " +
        				"      (COD_INDIC          	  , " +
        				"       COD_GRP_INDIC 		  , " +
        				"       DESCR_INDIC 		  , " +
        				"       DESCR_FORMULA_INDIC   , " +
        				"       FLG_INDIC_ATIV 		  , " +
        				"       COD_VERBA_RH 		  , " +
        				"       COD_ESCALA            , " +
        				"		FORMULA_INDIC		  , " +
        				"		DT_INI_SIT_SRV 		  , " +
        				"		COD_USUARIO  		  ,  " +
        				"		DESCR_FONTE			  ,  " +
        				"		FLG_SENTIDO			  ,  " +
        				"		DESCR_DIRETORIA		  ,  " +
        				"       FLG_PREENCH_ATING_IGUAL_REALZ )  " +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            int proximaSequencia = obtemProximaSequencia();
            setInteger 	(stmt, ordemCampos++, proximaSequencia);
            setInteger 	(stmt, ordemCampos++, dadosIndicadorVO.getIdGrupoIndicador());
            setString 	(stmt, ordemCampos++, dadosIndicadorVO.getDescricaoIndicador().toUpperCase());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFormulaConceito());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getAtivo());
            setInteger	(stmt, ordemCampos++, dadosIndicadorVO.getVerbaRH());
            setInteger 	(stmt, ordemCampos++, dadosIndicadorVO.getIdEscala());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFormulaIndicador());
            setTimestamp(stmt, ordemCampos++, dadosIndicadorVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, dadosIndicadorVO.getIdUsuarioUltimaAlteracao());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getDescricaoFonte());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFlgSentido());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getDescricaoDiretoria());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFlgPreenchAtingIgualRealz());

            stmt.executeUpdate();

            return proximaSequencia;
            
        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o indicador do bonus anual", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }   

    /**
     * 
     * @param dadosIndicadorVO
     * @return
     * @throws PersistenciaException
     */
    public void incluiIndicadorBonusImportacao(DadosIndicadorVO dadosIndicadorVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_INDICADOR      " +
        				"      (COD_INDIC          	  , " +
        				"       COD_GRP_INDIC 		  , " +
        				"       DESCR_INDIC 		  , " +
        				"       DESCR_FORMULA_INDIC   , " +
        				"       FLG_INDIC_ATIV 		  , " +
        				"       COD_VERBA_RH 		  , " +
        				"       COD_ESCALA            , " +
        				"		FORMULA_INDIC		  , " +
        				"		DT_INI_SIT_SRV 		  , " +
        				"		COD_USUARIO  		  , " +
        				"		DESCR_FONTE			  , " +
        				"		DESCR_DIRETORIA		  , " +
        				"		FLG_SENTIDO			  , " +
        				"       FLG_PREENCH_ATING_IGUAL_REALZ )  " +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?, ?, ?, ?, ?) ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, dadosIndicadorVO.getIdIndicador());
            setInteger 	(stmt, ordemCampos++, dadosIndicadorVO.getIdGrupoIndicador());
            setString 	(stmt, ordemCampos++, dadosIndicadorVO.getDescricaoIndicador().toUpperCase());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFormulaConceito());
            setString	(stmt, ordemCampos++, Constantes.CD_VERDADEIRO);
            setInteger	(stmt, ordemCampos++, dadosIndicadorVO.getVerbaRH());
            setInteger 	(stmt, ordemCampos++, dadosIndicadorVO.getIdEscala());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFormulaIndicador());
            setInteger  (stmt, ordemCampos++, dadosIndicadorVO.getIdUsuarioUltimaAlteracao());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getDescricaoFonte());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getDescricaoDiretoria());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFlgSentido());
            setString	(stmt, ordemCampos++, Constantes.CD_FALSO);

            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o indicador bonus anual", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }   
  
    /**
     * Obtém próxima sequencia
     * 
     * @return
     */
    private int obtemProximaSequencia() throws PersistenciaException {

        String query =  "SELECT MAX(COD_INDIC)  " +
                        "  FROM SRV_INDICADOR   ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, 1) + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia do indicador.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }     
    
    
    /**
     * Inclui historico de indicadores 
     *  
     * @param dadosIndicadorVO
     * @return
     */
    public void incluiIndicadorHistorico(DadosIndicadorVO dadosIndicadorVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_INDICADOR_HIST " +
						"      (COD_INDIC          	  , " +
						"       SEQ_INDIC          	  , " +
						"       COD_GRP_INDIC 		  , " +
						"       DESCR_INDIC 		  , " +
						"       DESCR_FORMULA_INDIC   , " +
						"       FLG_INDIC_ATIV 		  , " +
						"       COD_VERBA_RH 		  , " +
						"       COD_INDIC_SIS 		  , " +
						"       COD_ESCALA 		      , " +
						"		DT_INI_SIT_SRV 		  , " +
						"		FORMULA_INDIC		  , " +
						"		COD_USUARIO			  ,	" +
						" FLG_PREENCH_ATING_IGUAL_REALZ  " +
						")  		    " +
				        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setInteger 	(stmt, ordemCampos++, dadosIndicadorVO.getIdIndicador());
            setInteger	(stmt, ordemCampos++, obtemProximaSequenciaHistorio(dadosIndicadorVO.getIdIndicador().intValue()));
            setInteger 	(stmt, ordemCampos++, dadosIndicadorVO.getIdGrupoIndicador());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getDescricaoIndicador());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFormulaConceito());
            setBoolean	(stmt, ordemCampos++, dadosIndicadorVO.getAtivo());
            setInteger  (stmt, ordemCampos++, dadosIndicadorVO.getVerbaRH());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getIdSistemico());
            setInteger  (stmt, ordemCampos++, dadosIndicadorVO.getIdEscala());
            setTimestamp(stmt, ordemCampos++, dadosIndicadorVO.getDataUltimaAlteracao());
            setString	(stmt, ordemCampos++, dadosIndicadorVO.getFormulaIndicador());
            setInteger  (stmt, ordemCampos++, dadosIndicadorVO.getIdUsuarioUltimaAlteracao());
            setString   (stmt, ordemCampos++, dadosIndicadorVO.getFlgPreenchAtingIgualRealz());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o historico do indicador ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }      

    
    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idIndicador
     * @return
     */
    private int obtemProximaSequenciaHistorio(int idIndicador) throws PersistenciaException {

        String query =  "SELECT MAX(SEQ_INDIC)        " +
                        "  FROM SRV_INDICADOR_HIST    " +
                        " WHERE COD_INDIC =?          ";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, idIndicador);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, 1) + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico da indicador " + idIndicador, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }
    
    
    /**
     * Inclui historico de indicadores 
     *  
     * @param dadosIndicadorVO
     * @return
     */
    public void incluiRealizado(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_REALIZADO_FUNC_INDICADOR 		" +
        		        "      (COD_FUNC								" +
        		        "      ,COD_INDIC								" +
        		        "      ,COD_EMP									" +
        		        "      ,COD_FIL									" +
        		        "	   ,NUM_ANO									" +
        		        "      ,NUM_MES									" +
        		        "      ,COD_UN_REALZ							" +
        		        "      ,NUM_REALZ								" +
        		        "      ,COD_UN_META								" +
        		        "      ,NUM_META								" +
        		        "      ,COD_UN_PESO								" +
        		        "      ,NUM_PESO								" +
        		        "      ,COD_ESCALA								" +
        		        "      ,DT_INI_SIT_SRV							" +
        		        "      ,COD_USUARIO)							" +
        		        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE, ?) ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setLong 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFuncionario());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdIndicador());
            setInteger 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEmpresa());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFilial());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getAno());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMes());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizado());
            setDouble	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizado());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeMeta());
            setDouble	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMeta());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadePeso());
            setDouble	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getPeso());
            setDouble	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEscala());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdUsuarioAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o realizado do indicador ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }         

    /**
     * 
     * @param indicadorFuncionarioBonusVO
     * @throws PersistenciaException
     */
    public void incluiPesoMetaBonusAnual(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_REALIZADO_FUNC_INDICADOR 		" +
        		        "      (COD_FUNC								" +
        		        "      ,COD_INDIC								" +
        		        "      ,COD_EMP									" +
        		        "      ,COD_FIL									" +
        		        "	   ,NUM_ANO									" +
        		        "      ,NUM_MES									" +
        		        "      ,COD_UN_PESO								" +
        		        "      ,NUM_PESO								" +
        		        "      ,COD_UN_META								" +
        		        "      ,NUM_META								" +
        		        "      ,DT_INI_SIT_SRV							" +
        		        "      ,COD_USUARIO)							" +
        		        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)  	";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setLong 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFuncionario());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdIndicador());
            setInteger 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEmpresa());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFilial());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getAno());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMes());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadePeso());
            setDouble	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getPeso());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeMeta());
            setDouble   (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMeta());
            setTimestamp(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdUsuarioAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o Peso/Meta do indicador ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }         

    /**
     * 
     * @param indicadorFuncionarioBonusVO
     * @throws PersistenciaException
     */
    public void incluiRealizadoParametrizadoBonusAnual(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_REALIZADO_FUNC_INDICADOR 		" +
        		        "      (COD_FUNC								" +
        		        "      ,COD_INDIC								" +
        		        "      ,COD_EMP									" +
        		        "      ,COD_FIL									" +
        		        "      ,COD_ESCALA								" +
        		        "	   ,NUM_ANO									" +
        		        "      ,NUM_MES									" +
        		        "      ,COD_UN_META								" +
        		        "      ,NUM_META								" +
        		        "      ,COD_UN_PESO								" +
        		        "      ,NUM_PESO								" +
        		        "      ,DT_INI_SIT_SRV							" +
        		        "      ,COD_USUARIO)							" +
        		        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setLong 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFuncionario());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdIndicador());
            setInteger 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEmpresa());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFilial());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEscala());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getAno());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMes());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeMeta());
            setDouble   (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMeta());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadePeso());
            setDouble   (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getPeso());
            setTimestamp(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdUsuarioAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o realizado do indicador ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }         

    
    /**
     * Inclui historico de indicadores 
     * 
     * @param dadosIndicadorVO
     * @return
     */
    public void incluiHistoricoRealizado(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws PersistenciaException {

        String query =  "INSERT INTO SRV_REALZ_FUNC_INDIC_HIST 			" +
        		        "      (COD_FUNC								" +
        		        "      ,COD_INDIC								" +
        		        "      ,COD_EMP									" +
        		        "      ,COD_FIL									" +
        		        "	   ,NUM_ANO									" +
        		        "      ,NUM_MES									" +
        		        "      ,COD_ESCALA								" +
        		        "      ,NUM_SEQ_ESCALA_FX						" +
        		        "      ,NUM_REALZ_FX							" +
        		        "      ,COD_UN_REALZ_FX							" +
        		        "      ,COD_POND								" +
        		        "      ,NUM_PESO								" +
        		        "      ,COD_UN_PESO								" +
        		        "      ,NUM_REALZ_POND							" +
        		        "      ,COD_UN_REALZ_POND						" +
        		        "      ,NUM_META								" +
        		        "      ,COD_UN_META								" +
        		        "      ,NUM_REALZ								" +
        		        "      ,COD_UN_REALZ							" +
        		        "      ,NUM_REALZ_X_META						" +
        		        "      ,COD_UN_REALZ_X_META						" +
        		        "      ,VLR_PREMIO								" +
        		        "      ,VLR_PREMIO_FUNC_CALC					" +
        		        "      ,COD_UN_VLR_PREMIO_FUNC_CALC				" +
        		        "      ,PCT_CALC_RATEIO							" +
        		        "      ,DT_INI_SIT_SRV							" +
        		        "      ,COD_USUARIO)							" +
        		        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 		" +
        		        "         ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 		" +
        		        "         ?, ?, ?, ?, ?, ?, ?) 				    ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setLong 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFuncionario());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdIndicador());
            setInteger 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEmpresa());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFilial());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getAno());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMes());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEscala());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getSequencialEscala());
            setDouble   (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizadoFaixa());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizadoFaixa());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdPonderacao());
            setDouble   (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getPeso());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadePeso());
            setDouble   (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizadoPonderacao());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizadoPonderacao());
            setDouble   (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMeta());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeMeta());
            setDouble	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizado());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizado());
            setDouble   (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getRealizadoXMeta());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeRealizadoXMeta());
            setDouble   (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getValorPremio());
            setDouble   (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getValorPremioCalculado());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getUnidadeValorPremioCalculado());
            setDouble   (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getPercentualRateio());
            setTimestamp(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdUsuarioAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o realizado do indicador ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }
    
    
	/**
	 * 
	 * @param idFuncionario
	 * @param idIndicador
	 * @param idEmpresa
	 * @param idFilial
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 */
	public IndicadorFuncionarioRealizadoVO obtemIndicadorFuncionarioRealizado(long idFuncionario, int idIndicador, int idEmpresa, int idFilial, int ano, int mes) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
					"SELECT A.COD_EMP									" +
					"      ,A.COD_FIL									" +
					"      ,B.COD_GRP_INDIC								" +
					"      ,C.DESCR_GRP_INDIC							" +
					"	   ,A.COD_INDIC 								" +
					"      ,B.DESCR_INDIC								" +
					"      ,A.COD_ESCALA								" +
					"      ,A.NUM_SEQ_ESCALA_FX							" +
					"      ,A.COD_UN_REALZ_FX							" +
					"      ,A.NUM_REALZ_FX								" +
					"      ,A.COD_POND									" +
					"      ,A.COD_UN_PESO								" +
					"      ,A.NUM_PESO									" +
					"      ,A.NUM_REALZ_POND							" +
					"      ,A.COD_UN_REALZ_POND							" +
					"      ,A.COD_UN_META								" +
					"      ,A.NUM_META									" +
					"      ,A.COD_UN_REALZ								" +
					"      ,A.NUM_REALZ									" +
					"      ,A.COD_UN_REALZ_X_META						" +
					"      ,A.NUM_REALZ_X_META							" +
					"      ,A.VLR_PREMIO								" +
					"      ,A.COD_UN_VLR_PREMIO_FUNC_CALC				" +
					"      ,A.VLR_PREMIO_FUNC_CALC						" +
					"      ,A.PCT_CALC_RATEIO							" +	
					"      ,A.DT_INI_SIT_SRV							" +
					"      ,A.COD_USUARIO								" +
					"      ,B.FLG_PREENCH_ATING_IGUAL_REALZ				" +
					"      ,B.COD_INDIC_PAI								" +
					"  FROM SRV_REALIZADO_FUNC_INDICADOR A 				" +
					"      ,SRV_INDICADOR                B 				" +
					"      ,SRV_GRUPO_INDICADOR 		 C 				" +
					" WHERE A.COD_INDIC          = B.COD_INDIC			" +
					"   AND B.COD_GRP_INDIC      = C.COD_GRP_INDIC		" +
					"   AND A.COD_FUNC     	     = ?             		" +
					"   AND A.COD_INDIC     	 = ?             		" +
					"   AND A.COD_EMP     	     = ?             		" +
					"   AND A.COD_FIL	     	 = ?             		" +
					"   AND A.NUM_ANO 		     = ?             		" +
					"   AND A.NUM_MES 		     = ?             		");

		List parametroSQL = new ArrayList();
		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG,    new Long(idFuncionario)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idIndicador)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idEmpresa)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idFilial)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(ano)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(mes)));
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);

			rs = stmt.executeQuery();
			if (rs.next()) {
				indicadorFuncionarioBonusVO = new IndicadorFuncionarioRealizadoVO();
				indicadorFuncionarioBonusVO.setIdFuncionario(new Long(idFuncionario));
				indicadorFuncionarioBonusVO.setIdEmpresa(new Integer(idEmpresa));
				indicadorFuncionarioBonusVO.setIdFilial(new Integer(idFilial));				
				indicadorFuncionarioBonusVO.setAno(new Integer(ano));
				indicadorFuncionarioBonusVO.setMes(new Integer(mes));
				indicadorFuncionarioBonusVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				indicadorFuncionarioBonusVO.setDescricaoGrupoIndicador(getString(rs, "DESCR_GRP_INDIC"));
				indicadorFuncionarioBonusVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				indicadorFuncionarioBonusVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				indicadorFuncionarioBonusVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				indicadorFuncionarioBonusVO.setSequencialEscala(getInteger(rs, "NUM_SEQ_ESCALA_FX"));
				indicadorFuncionarioBonusVO.setUnidadeRealizadoFaixa(getInteger(rs, "COD_UN_REALZ_FX"));
				indicadorFuncionarioBonusVO.setRealizadoFaixa(getDouble(rs, "NUM_REALZ_FX"));
				indicadorFuncionarioBonusVO.setIdPonderacao(getInteger(rs, "COD_POND"));
				indicadorFuncionarioBonusVO.setUnidadePeso(getInteger(rs, "COD_UN_PESO"));
				indicadorFuncionarioBonusVO.setPeso(getDouble(rs, "NUM_PESO"));
				indicadorFuncionarioBonusVO.setUnidadeRealizadoPonderacao(getInteger(rs, "COD_UN_REALZ_POND"));
				indicadorFuncionarioBonusVO.setRealizadoPonderacao(getDouble(rs, "NUM_REALZ_POND"));
				indicadorFuncionarioBonusVO.setUnidadeMeta(getInteger(rs, "COD_UN_META"));
				indicadorFuncionarioBonusVO.setMeta(getDouble(rs, "NUM_META"));
				indicadorFuncionarioBonusVO.setUnidadeRealizado(getInteger(rs, "COD_UN_REALZ"));
				indicadorFuncionarioBonusVO.setRealizado(getDouble(rs, "NUM_REALZ"));
				indicadorFuncionarioBonusVO.setUnidadeRealizadoXMeta(getInteger(rs, "COD_UN_REALZ_X_META"));
				indicadorFuncionarioBonusVO.setRealizadoXMeta(getDouble(rs, "NUM_REALZ_X_META"));
				indicadorFuncionarioBonusVO.setValorPremio(getDouble(rs, "VLR_PREMIO"));
				indicadorFuncionarioBonusVO.setUnidadeValorPremioCalculado(getInteger(rs, "COD_UN_VLR_PREMIO_FUNC_CALC"));
				indicadorFuncionarioBonusVO.setValorPremioCalculado(getDouble(rs, "VLR_PREMIO_FUNC_CALC"));
				indicadorFuncionarioBonusVO.setPercentualRateio(getDouble(rs, "PCT_CALC_RATEIO"));
				indicadorFuncionarioBonusVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				indicadorFuncionarioBonusVO.setIdUsuarioAlteracao(getInteger(rs, "COD_USUARIO"));
				indicadorFuncionarioBonusVO.setFlgPrrenchAtingIgualRealiz(getString(rs, "FLG_PREENCH_ATING_IGUAL_REALZ"));
				indicadorFuncionarioBonusVO.setIdIndicadorPai(getInteger(rs, "COD_INDIC_PAI"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter os dados de indicador realizado por funcionário", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return indicadorFuncionarioBonusVO;
	}	    
	
	
	/**
	 * Obtém resultados de indicadores de bonus por funcionário 
	 * 
	 * @param idFuncionario
	 * @param idIndicador
	 * @param idEmpresa
	 * @param idFilial
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 */
	public void excluiIndicadorFuncionarioRealizado(long idFuncionario, int idIndicador, int idEmpresa, int idFilial, int ano, int mes) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
					" DELETE FROM SRV_REALIZADO_FUNC_INDICADOR A 	    " +
					" WHERE A.COD_FUNC     	     = ?             		" +
					"   AND A.COD_INDIC     	 = ?             		" +
					"   AND A.COD_EMP     	     = ?             		" +
					"   AND A.COD_FIL	     	 = ?             		" +
					"   AND A.NUM_ANO 		     = ?             		" +
					"   AND A.NUM_MES 		     = ?             		");

		List parametroSQL = new ArrayList();
		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG,    new Long(idFuncionario)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idIndicador)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idEmpresa)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idFilial)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(ano)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(mes)));
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);

			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível excluir o indicador realizado por funcionário", e);
		} finally {
			closeStatement(stmt);
		}
	}

	/**
	 * 
	 * @param idFuncionario
	 * @param idIndicador
	 * @param idEmpresa
	 * @param idFilial
	 * @param ano
	 * @param mes
	 * @throws PersistenciaException
	 */
	public void excluiIndicadorFuncionarioRealizadoPai(long idFuncionario, int idIndicador, int idEmpresa, int idFilial, int ano, int mes) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
					" DELETE FROM SRV_REALIZADO_FUNC_INDICADOR A 	    " +
					" WHERE A.COD_FUNC     	     = ?             		" +
					"   AND A.COD_INDIC	    	 = ?             		" +
					"   AND A.COD_EMP     	     = ?             		" +
					"   AND A.COD_FIL	     	 = ?             		" +
					"   AND A.NUM_ANO 		     = ?             		" +
					"   AND A.NUM_MES 		     = ?             		");

		List parametroSQL = new ArrayList();
		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG,    new Long(idFuncionario)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idIndicador)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idEmpresa)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(idFilial)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(ano)));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(mes)));
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);

			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível excluir o indicador realizado por funcionário", e);
		} finally {
			closeStatement(stmt);
		}
	}

	/**
	 * Obtém resultados de indicadores de bonus por funcionário 
	 * 
	 * @param ano
	 * @param mes
	 * @param idCargo
	 * @param idFuncionario
	 * @param idUsuario
	 * @return
	 * @throws PersistenciaException
	 */
	public void processaIndicadorAdministrativo(int ano, int mes, int idCargo, long idFuncionario, int idUsuario) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
					" { call PKG_SRV_CALC_REM_VAR.PRC_CALC_REALZ_FUNC_INDIC_ADM (?, ?, ?, ?, ?, ?, ?) } ");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
            CallableStatement cstmt = conn.prepareCall(query.toString());
            int nroCampo = 1;

            setInteger	(cstmt, nroCampo++, ano); 
            setInteger	(cstmt, nroCampo++, mes); 
            setInteger	(cstmt, nroCampo++, idCargo);
            setLong		(cstmt, nroCampo++, idFuncionario);
            setInteger	(cstmt, nroCampo++, idUsuario);

            cstmt.registerOutParameter(nroCampo++, Types.INTEGER);
            cstmt.registerOutParameter(nroCampo++, Types.VARCHAR);

            cstmt.execute();
            int idRetorno = cstmt.getInt(6);
            
            if (idRetorno != 0) {
            	throw new PersistenciaException("Ocorreu erro ao processar indicador corporativo: " + idRetorno + " - " + cstmt.getString(7));
            }
            
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível processar o indicador realizado por funcionário", e);
		} finally {
			closeStatement(stmt);
		}
	}

    public boolean isDoisIndicadoresRestantes(int codFunc, int mes, int ano) throws PersistenciaException {
            PreparedStatement stmt;
            ResultSet rs;
            int qtdRegistros;
            int index;
            StringBuffer query;
            Connection conn = null;
            stmt = null;
            rs = null;
            qtdRegistros = -1;
            index = 1;
            query = new StringBuffer(" SELECT COUNT(COD_FUNC)FROM SRV_REALIZADO_FUNC_INDICADOR " +
            							" WHERE COD_FUNC = ? AND NUM_MES  = ? AND NUM_ANO  = ? ");
            
            try{
                conn = getConn();
                stmt = conn.prepareStatement(query.toString());
                stmt.setInt(index++, codFunc);
                stmt.setInt(index++, mes);
                stmt.setInt(index++, ano);
                rs = stmt.executeQuery();
                if(rs.next())
                {
                    qtdRegistros = getInt(rs, "COUNT(COD_FUNC)");
                }
                if(qtdRegistros == 2) {
                    closeStatementAndResultSet(stmt, rs);
                    return true;
                } else {
                    closeStatementAndResultSet(stmt, rs);
                    return false;
                }
            }catch(Exception e){
                throw new PersistenciaException(log, "Nao foi possível obter quantidade de registros da tabela.", e);
            } finally {
                closeStatementAndResultSet(stmt, rs);
            }
        }

	public void finalizaIndicadorCorporativo(int ano, int mes, int idCargo, long idFuncionario, int idUsuario) throws PersistenciaException {
		PreparedStatement stmt = null;
		Connection conn = null;
		StringBuffer query = new StringBuffer(" { call pkg_srv_calc_rem_var.prc_calc_realz_Func_Indic_Corp (?, ?, ?, ?, ?, ?, ?,?) } ");
		try {
			conn = getConn();
			CallableStatement cstmt = conn.prepareCall(query.toString());
			int nroCampo = 1;
			setInteger(cstmt, nroCampo++, ano);
			setInteger(cstmt, nroCampo++, mes);
			if(idCargo > 0) {
				setInteger(cstmt, nroCampo++, idCargo);
			} else {
				setInteger(cstmt, nroCampo++, null);
			}
			if(idFuncionario > 0L) {
				setLong(cstmt, nroCampo++, idFuncionario);
			} else {
				setLong(cstmt, nroCampo++, null);
			}
			setInteger(cstmt, nroCampo++, StatCalcRealzEnum.FINALIZADO.getCodigo());
			setInteger(cstmt, nroCampo++, idUsuario);
			cstmt.registerOutParameter(nroCampo++, 4);
			cstmt.registerOutParameter(nroCampo++, 12);
			cstmt.execute();
			int idRetorno = cstmt.getInt(7);
			if(idRetorno != 0) {
				throw new PersistenciaException((new StringBuilder("Ocorreu erro ao processar indicador corporativo: ")).append(idRetorno).append(" - ").append(cstmt.getString(7)).toString());
			}
		} catch(Exception e) {
			throw new PersistenciaException(log, "Ocorreu erro ao finalizar indicador corporativo", e);
		} finally {
			closeStatement(stmt);
		}
	}

    public List obtemListaIndicadoresAjusteFuncionario(Long idFunc, Integer mes, Integer ano) throws PersistenciaException {

    	List list = new ArrayList();
    	String query = 
    		" SELECT " +
    		"		I.COD_INDIC, I.DESCR_INDIC, FI.COD_FIL, FI.NUM_ANO, FI.NUM_MES, FI.VLR_PREMIO_FUNC_CALC, FI.COD_FIL, FI.COD_EMP " +
    		" FROM " +
    		"		SRV_REALIZADO_FUNC_INDICADOR FI, " +
    		" 		SRV_INDICADOR I, " +
    		" 		SRV_FILIAL F " +
    		" WHERE " +
    		"		FI.COD_FUNC = ? " +
    		" 	AND FI.NUM_MES  = ? " +
    		" 	AND FI.NUM_ANO  = ? " +
    		" 	AND I.COD_INDIC = FI.COD_INDIC " +
    		" 	AND FI.COD_FIL  = F.COD_FIL " +
    		" 	AND FI.COD_INDIC IN " +
    		" 		(SELECT IND.COD_INDIC " +
    		" 			FROM SRV_INDICADOR IND " +
    		" 			WHERE IND.FLG_INDIC_ATIV = 'S' " +
    		" 			AND IND.COD_INDIC_SIS = 'AJUS_DEB_CRED')" +
			" ORDER BY I.DESCR_INDIC ";

        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = getConn().prepareStatement(query);
            int i = 1;
            setLong(stmt, i++, idFunc);
            setInteger(stmt, i++, mes);
            setInteger(stmt, i++, ano);

            rs = stmt.executeQuery();
            while(rs.next()) {
            	IndicadorFuncionarioRealizadoVO vo = new IndicadorFuncionarioRealizadoVO();
				vo.setIdFuncionario(idFunc);
				vo.setIdEmpresa(getInteger(rs, "COD_EMP"));
				vo.setIdFilial(getInteger(rs, "COD_FIL"));				
				vo.setAno(getInteger(rs, "NUM_ANO"));
				vo.setMes(getInteger(rs, "NUM_MES"));
				vo.setIdIndicador(getInteger(rs, "COD_INDIC"));
				vo.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				vo.setValorPremioCalculado(getDouble(rs, "VLR_PREMIO_FUNC_CALC"));
				list.add(vo);
           }
        } catch(Exception e) {
            throw new PersistenciaException(log, "Não foi possível encontrar o indicador de ajuste por funcionario.", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return list;
    }

    public void incluiRealizadoAjuste(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws PersistenciaException {

        String query =  " INSERT INTO SRV_REALIZADO_FUNC_INDICADOR 		" +
        		        "      (COD_FUNC								" +
        		        "      ,COD_INDIC								" +
        		        "      ,COD_EMP									" +
        		        "      ,COD_FIL									" +
        		        "	   ,NUM_ANO									" +
        		        "      ,NUM_MES									" +
        		        "      ,VLR_PREMIO_FUNC_CALC					" +
        		        "      ,DT_INI_SIT_SRV							" +
        		        "      ,COD_USUARIO)							" +
        		        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)  	";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setLong 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFuncionario());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdIndicador());
            setInteger 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEmpresa());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFilial());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getAno());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMes());
            setDouble	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getValorPremioCalculado());
            setTimestamp(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdUsuarioAlteracao());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o realizado do indicador ajuste ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }         

    public void alteraRealizadoAjuste(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws PersistenciaException {

        String query =  " UPDATE SRV_REALIZADO_FUNC_INDICADOR SET " +
        				"      VLR_PREMIO_FUNC_CALC	= ?, " +
        				"      DT_INI_SIT_SRV	= ?, " +
        				"      COD_USUARIO	= ? " +
        		        " WHERE COD_FUNC = ? " +
        		        "   AND COD_INDIC = ? " +
        		        "   AND COD_EMP	= ? " +
        		        "   AND COD_FIL = ? " +
        		        "	AND NUM_ANO = ? " +
        		        "   AND NUM_MES = ? ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;

            setDouble	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getValorPremioCalculado());
            setTimestamp(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getDataUltimaAlteracao());
            setInteger  (stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdUsuarioAlteracao());
            setLong 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFuncionario());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdIndicador());
            setInteger 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEmpresa());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFilial());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getAno());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMes());
            
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o realizado do indicador ajuste ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }         

    public boolean existeRealizadoAjuste(IndicadorFuncionarioRealizadoVO indicadorFuncionarioBonusVO) throws PersistenciaException {

        String query =  " SELECT * FROM SRV_REALIZADO_FUNC_INDICADOR " +
        		        " WHERE COD_FUNC = ? AND COD_INDIC	= ? AND COD_EMP = ? AND COD_FIL = ? AND NUM_ANO = ? AND NUM_MES = ? ";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean existe = false;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            
            setLong 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFuncionario());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdIndicador());
            setInteger 	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdEmpresa());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getIdFilial());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getAno());
            setInteger	(stmt, ordemCampos++, indicadorFuncionarioBonusVO.getMes());

            rs = stmt.executeQuery();
            if ( rs.next() ) {
            	existe = true;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível incluir o realizado do indicador ajuste ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return existe;
    }

    /**
     * 
     * @return
     * @throws PersistenciaException
     */
	public List<String> obtemListaFonte() throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(" SELECT DISTINCT(DESCR_FONTE) AS FONTE FROM SRV_INDICADOR WHERE DESCR_FONTE IS NOT NULL ORDER BY 1 ");
				
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> listaFonte = new ArrayList<String>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				listaFonte.add(getString(rs, "FONTE"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a lista de fonte dos indicadores.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return listaFonte;
	}

    /**
     * 
     * @return
     * @throws PersistenciaException
     */
	public List<String> obtemListaDiretoria() throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(" SELECT DISTINCT(DESCR_DIRETORIA) AS DIRETORIA FROM SRV_INDICADOR WHERE DESCR_DIRETORIA IS NOT NULL AND FLG_INDIC_ATIV = 'S' AND COD_INDIC_PAI IS NULL AND COD_GRP_INDIC <> 1 ORDER BY 1 ");
				
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<String> listaDiretoria = new ArrayList<String>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				listaDiretoria.add(getString(rs, "DIRETORIA"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a lista de diretoria dos indicadores.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return listaDiretoria;
	}

	public boolean existeIndicadorMesmoNome(String nome) throws PersistenciaException {

		StringBuffer query =  new StringBuffer(" SELECT 1 FROM SRV_INDICADOR WHERE UPPER(DESCR_INDIC) = ? ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		boolean retorno = false;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			setString(stmt, 1, nome.toUpperCase());
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				retorno = getBool(rs, 1);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível validar o nome do indicador: " + nome, e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return retorno;
	}	

/**********************************************************************************************************************/

	/**
	 * 
	 * @return
	 * @throws PersistenciaException
	 */
	public List<Integer> obtemListaFuncionarioRelatorio() throws PersistenciaException {

		StringBuffer query =  new StringBuffer(" SELECT DISTINCT COD_FUNC FROM SRV_REALIZADO_FUNC_INDICADOR F WHERE F.NUM_ANO = 2015 AND F.NUM_MES = 12 ORDER BY 1 ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Integer> lista = new ArrayList<Integer>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				lista.add(getInt(rs, 1));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a lista de funcionarios disponiveis: ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}	

	/**
	 * 
	 * @return
	 * @throws PersistenciaException
	 */
	public List<Integer> obtemListaLojaRelatorio() throws PersistenciaException {

		StringBuffer query =  new StringBuffer(" SELECT DISTINCT COD_FILIAL FROM SRV_REALIZADO_LOJA_INDICADOR F WHERE F.NUM_ANO = 2015 AND F.NUM_MES = 12 ORDER BY 1 ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Integer> lista = new ArrayList<Integer>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				lista.add(getInt(rs, 1));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a lista de lojas disponiveis: ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}	

	/**
	 * 
	 * @param idFuncionario
	 * @param numAno
	 * @param numMes
	 * @param idUsuario
	 * @throws PersistenciaException
	 */
    public void atualizaAceiteBonusAnual(long idFuncionario, int numAno, int numMes, int idUsuario) throws PersistenciaException {

        String query =  "UPDATE SRV_REALIZADO_FUNC_INDICADOR SET STA_CALC_REALZ = ?, COD_USUARIO = ?, DT_INI_SIT_SRV = SYSDATE "
        				+ " WHERE NUM_ANO = ? AND NUM_MES = ? AND COD_FUNC = ? AND COD_INDIC = 48 ";

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setInteger(stmt, ordemCampos++, 4);//Aceite usuario
            setLong(stmt, ordemCampos++, idUsuario);
            setInteger(stmt, ordemCampos++, numAno);
            setInteger(stmt, ordemCampos++, numMes);
            setLong(stmt, ordemCampos++, idFuncionario);
            stmt.executeUpdate();

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível atualizar aceite no bonus anual ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
    }         

    /**
     * 
     * @param pesquisaVO
     * @param tiposGrupo
     * @param isPesquisaSubIndicador
     * @return
     * @throws PersistenciaException
     */
	public List<DadosIndicadorVO> obtemListaIndicadoresAtivosPorPeriodo(DadosIndicadorVO pesquisaVO, int tipoGrupo, int codIndicInicio, int codIndicFim) throws PersistenciaException {		

		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_INDIC, " +
				"         A.DESCR_INDIC, " +
				"         A.COD_GRP_INDIC, " +
				"         B.DESCR_GRP_INDIC, " +
				"         A.COD_ESCALA, " +
				"         A.FLG_INDIC_ATIV, " +
				"         A.DESCR_FORMULA_INDIC, " +
				"         A.COD_VERBA_RH, " +
				"         A.DT_INI_SIT_SRV, " +
				"         A.COD_USUARIO, " +
				"         A.FORMULA_INDIC, " +
				"         A.FLG_PREENCH_ATING_IGUAL_REALZ, " +
				"         A.DESCR_FONTE, " +
				"         A.DESCR_DIRETORIA " +
				"    FROM SRV_INDICADOR A, SRV_GRUPO_INDICADOR B " +
				"   WHERE A.COD_GRP_INDIC = B.COD_GRP_INDIC " +
				"     AND (A.COD_INDIC_SIS IS NULL OR A.COD_INDIC_SIS = 'SAX_PSF') " +
				"     AND A.COD_INDIC_PAI IS NULL " +
				"     AND B.COD_TIPO_REM_VAR = ? " +
				"     AND NVL(A.FLG_INDIC_ATIV, 'N') = 'S' " +
				"     AND A.COD_GRP_INDIC = ? " +
				"     AND A.COD_INDIC BETWEEN ? AND ? " +
				"  ORDER BY A.DESCR_INDIC ");

		List<ParametroSQL> parametroSQL = new ArrayList<ParametroSQL>();
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, tipoGrupo));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, pesquisaVO.getIdGrupoIndicador()));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codIndicInicio));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codIndicFim));

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<DadosIndicadorVO> lista = new ArrayList<DadosIndicadorVO>();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			rs = stmt.executeQuery();
			
			while (rs.next()) {
				DadosIndicadorVO dadosIndicadorVO = new DadosIndicadorVO();
				dadosIndicadorVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				dadosIndicadorVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				dadosIndicadorVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
				dadosIndicadorVO.setDescricaoGrupo(getString(rs, "DESCR_GRP_INDIC"));
				dadosIndicadorVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				dadosIndicadorVO.setAtivo(getString(rs, "FLG_INDIC_ATIV"));
				dadosIndicadorVO.setFormulaConceito(getString(rs, "DESCR_FORMULA_INDIC"));
				dadosIndicadorVO.setVerbaRH(getInteger(rs, "COD_VERBA_RH"));
				dadosIndicadorVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				dadosIndicadorVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				dadosIndicadorVO.setFormulaIndicador(getString(rs, "FORMULA_INDIC"));
				dadosIndicadorVO.setFlgPreenchAtingIgualRealz(getString(rs, "FLG_PREENCH_ATING_IGUAL_REALZ"));
				dadosIndicadorVO.setDescricaoFonte(getString(rs, "DESCR_FONTE"));
				dadosIndicadorVO.setDescricaoDiretoria(getString(rs, "DESCR_DIRETORIA"));
				lista.add(dadosIndicadorVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter lista de obtemListaIndicadoresAtivosPorPeriodo() ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);	
		}
		return lista;
	}	

	/**
	 * 
	 * @param nomeIndicador
	 * @param codGrupoIndicador
	 * @param codIndicIni
	 * @param codIndicFim
	 * @return
	 * @throws PersistenciaException
	 */
	public DadosIndicadorVO obtemIndicadorPorNome(String nomeIndicador, int codGrupoIndicador, int codIndicIni, int codIndicFim) throws PersistenciaException {
		DadosIndicadorVO indicadorVO = null;

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = " SELECT COD_INDIC, DESCR_INDIC, COD_ESCALA FROM SRV_INDICADOR WHERE TRIM(UPPER(DESCR_INDIC)) = ? AND COD_GRP_INDIC = ? AND COD_INDIC BETWEEN ? AND ? ";
			stmt = getConn().prepareStatement(sql);
			int i = 0;
			setString(stmt, ++i, nomeIndicador.trim().toUpperCase());
			setInteger(stmt, ++i, codGrupoIndicador);
			setInteger(stmt, ++i, codIndicIni);
			setInteger(stmt, ++i, codIndicFim);
			rs = stmt.executeQuery();

			 if (rs.next()) {
				 indicadorVO = new DadosIndicadorVO();
				 indicadorVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				 indicadorVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				 indicadorVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
			}
		} catch (Exception e) {
			throw new PersistenciaException(log, "Erro ao executar IndicadorDAO.obtemIndicadorPorNome(String, int, int, int): " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return indicadorVO;
	}

	/**
	 * 
	 * @param codIndicIni
	 * @param codIndicFim
	 * @return
	 * @throws PersistenciaException
	 */
	public int obtemProximoCodIndicadorPorRange(int codIndicIni, int codIndicFim) throws PersistenciaException {

		PreparedStatement stmt = null;
		ResultSet rs = null;
		int maxIndicador = -1;

		try {
			String sql = " SELECT NVL(MAX(COD_INDIC),?)+1 AS MAX_INDIC FROM SRV_INDICADOR WHERE COD_INDIC BETWEEN ? AND ? ";
			stmt = getConn().prepareStatement(sql);
			int i = 0;
			setInteger(stmt, ++i, codIndicIni);
			setInteger(stmt, ++i, codIndicIni);
			setInteger(stmt, ++i, codIndicFim);
			rs = stmt.executeQuery();

			if (rs.next()) {
				maxIndicador = getInteger(rs, "MAX_INDIC");
			}
		} catch (PersistenciaException e) {
			throw e;
		} catch (Exception e) {
			throw new PersistenciaException("Erro ao executar IndicadorDAO.obtemMaxIndicador(int, int): " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return maxIndicador+1;
	}

	/**
	 * 
	 * @param codGrupoIndicador
	 * @param codIndicIni
	 * @param codIndicFim
	 * @return
	 * @throws PersistenciaException
	 */
	public List<DadosIndicadorVO> obtemListaIndicadorCorporativo(int codGrupoIndicador, int codIndicIni, int codIndicFim) throws PersistenciaException {
		
		List<DadosIndicadorVO> lista = new ArrayList<DadosIndicadorVO>();

		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = " SELECT COD_INDIC, COD_ESCALA FROM SRV_INDICADOR WHERE COD_GRP_INDIC = ? AND COD_INDIC BETWEEN ? AND ? ";
			stmt = getConn().prepareStatement(sql);
			int i = 0;
			setInteger(stmt, ++i, codGrupoIndicador);
			setInteger(stmt, ++i, codIndicIni);
			setInteger(stmt, ++i, codIndicFim);
			rs = stmt.executeQuery();

			while (rs.next()) {
				DadosIndicadorVO indicadorVO = new DadosIndicadorVO();
				 indicadorVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				 indicadorVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
				 lista.add(indicadorVO);
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Erro ao executar IndicadorDAO.obtemListaIndicadorCorporativo(int, int, int): " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param codigoPresidente
	 * @param codGrpIndic
	 * @return
	 * @throws PersistenciaException
	 */
	public List<Long> obtemListaFuncionarioBonusAnual(int ano, int mes, long codigoPresidente, int codGrpIndic) throws PersistenciaException {

		List<Long> lista = new ArrayList<Long>();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = " SELECT DISTINCT COD_FUNC " +
						 "   FROM SRV_REALIZADO_FUNC_INDICADOR FI, SRV_INDICADOR I " +
						 "  WHERE FI.COD_INDIC = I.COD_INDIC " +
						 "    AND FI.NUM_ANO = ? " +
						 "    AND FI.NUM_MES = ? " +
						 "    AND FI.COD_FUNC <> ? " +
						 "    AND I.COD_GRP_INDIC = ? ";

			stmt = getConn().prepareStatement(sql);
			int i = 0;
			setInteger(stmt, ++i, ano);
			setInteger(stmt, ++i, mes);
			setLong(stmt, ++i, codigoPresidente);
			setInteger(stmt, ++i, codGrpIndic);
			rs = stmt.executeQuery();
			while (rs.next()) {
				 lista.add(getLong(rs, "COD_FUNC"));
			}
		} catch (Exception e) {
			throw new PersistenciaException(log, "Erro ao executar IndicadorDAO.obtemListaFuncionarioBonusAnual(int, int, long, int): " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}

	/**
	 * 
	 * @return
	 * @throws PersistenciaException
	 */
	public Integer obtemMaiorCodIndicador() throws PersistenciaException {

		Integer codMaxIndic = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = " SELECT MAX(COD_INDIC) AS MAX_COD_INDIC FROM SRV_INDICADOR ";

			stmt = getConn().prepareStatement(sql);
			rs = stmt.executeQuery();
			if (rs.next()) {
				 codMaxIndic = getInteger(rs, "MAX_COD_INDIC");
			}
		} catch (Exception e) {
			throw new PersistenciaException(log, "Erro ao executar IndicadorDAO.obtemMaiorCodIndicador(): " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return codMaxIndic;
	}
	
}