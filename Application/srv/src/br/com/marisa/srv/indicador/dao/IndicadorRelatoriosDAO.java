package br.com.marisa.srv.indicador.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.DataHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.indicador.vo.DetalheCalculoLojaVO;
import br.com.marisa.srv.indicador.vo.DetalheCalculoVO;
import br.com.marisa.srv.indicador.vo.IndicadorFuncionarioVO;
import br.com.marisa.srv.indicador.vo.IndicadorLojaVO;
import br.com.marisa.srv.util.tools.Tools;


/**
 * Classe para tratar dos métodos de acesso a dados às calendário 
 * 
 * @author Walter Fontes
 */
public class IndicadorRelatoriosDAO extends BasicDAO {

    private static final Logger log = Logger.getLogger(IndicadorRelatoriosDAO.class);

	/**
	 * Obtém resultados por loja conforme o filtro selecionado
	 * 
	 * @param idLider
	 * @param idLoja
	 * @param ano
	 * @param mes
	 * @param codigoEmpresa
	 * @param codigoIndicador
	 * @param codigoGrupo
	 * @return
	 * @throws PersistenciaException
	 */
	public List obtemRelatorioLoja(Integer idLider, Integer idLoja, Integer ano, Integer mes, 
			                       Integer codigoEmpresa, Integer codigoIndicador, Integer codigoGrupo, List idsFiliais) throws PersistenciaException {
		
		List parametroSQL = new ArrayList();
		
		StringBuffer query =  new StringBuffer(
				" SELECT A.COD_INDIC,			  " +
				"        A.COD_EMP,			  	  " +
				"        A.COD_FIL,			  	  " +
				"        A.NUM_ANO,			  	  " +
				"        A.NUM_MES,			  	  " +
				"        A.NUM_META, 			  " +
				" 		 A.NUM_REALZ, 			  " +
				" 		 A.NUM_REALZ_X_META, 	  " +
				" 		 D.VLR_PREMIO_FIL, 		  " +
				" 		 A.VLR_PREMIO_FIL_CALC,   " +
				" 	     A.QTD_REALZ 			  " +
				
				" FROM   SRV_REALIZADO_FILIAL A,  " +
				"        SRV_INDICADOR B,         " +
				"        SRV_META_FILIAL D,       " +
				"        SRV_FILIAL E             ");
				
		if (idLider != null) {
			query.append(" , SRV_FUNC_BASE_REM_VAR C ");
		}
		
		query.append(
				" WHERE  A.COD_INDIC      = B.COD_INDIC " +
				"   AND  A.COD_EMP        = E.COD_EMP   " +
				"   AND  A.COD_FIL        = E.COD_FIL   " +
				"   AND  E.FLG_ATIV       = ?           " +
				"   AND  B.COD_GRP_INDIC  = ? " + 
				"   AND  B.COD_INDIC      = ? " +
				"   AND  A.NUM_ANO        = ? " +
				"   AND  A.NUM_MES        = ? " +
				"   AND  A.NUM_META       > 0 "); //TODO RETIRAR ISSO
		
		parametroSQL.add(new ParametroSQL(PARAMTYPE_BOOLEAN, Boolean.TRUE));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codigoGrupo));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codigoIndicador));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		
		if (codigoEmpresa != null) {
			query.append(" AND A.COD_EMP = ? "); //1
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codigoEmpresa));
		}
		if (idLoja != null) {
			query.append(" AND A.COD_FIL = ? ");  //10
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idLoja));
		}				
		if (idLider != null) {
			query.append(" AND A.COD_EMP  = C.COD_EMP  " +
					 	 " AND A.COD_FIL  = C.COD_FIL  " +
					 	 " AND A.NUM_ANO  = C.NUM_ANO  " +
					 	 " AND A.NUM_MES  = C.NUM_MES  " +
		             	 " AND C.COD_FUNC = ?          ");
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idLider));
		}
		if (ObjectHelper.isNotEmpty(idsFiliais)) {
			query.append(" AND A.COD_FIL IN (");
			boolean pv = true;
			Iterator itIdsFiliais = idsFiliais.iterator();
			while (itIdsFiliais.hasNext()) {
				Integer idFil = (Integer)itIdsFiliais.next();
				if (!pv) {
					query.append(",?");
				} else {
					pv = false;
					query.append("?");
				}
				parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idFil));
			}
			query.append(" )");
		}
		
		query.append(
				" AND D.COD_INDIC (+)=  A.COD_INDIC  " +
		        " AND D.COD_EMP   (+)=  A.COD_EMP    " +
		        " AND D.COD_FIL   (+)=  A.COD_FIL    " +
		        " AND D.NUM_ANO   (+)=  A.NUM_ANO    " +
		        " AND D.NUM_MES   (+)=  A.NUM_MES    ");
		
		query.append(" ORDER BY A.COD_FIL ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List lista = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			rs = stmt.executeQuery();
			while (rs.next()) {
				IndicadorLojaVO indicadorLojaVO = new IndicadorLojaVO();
				indicadorLojaVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				indicadorLojaVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				indicadorLojaVO.setIdFilial(getInteger(rs, "COD_FIL"));
				indicadorLojaVO.setAno(getInteger(rs, "NUM_ANO"));
				indicadorLojaVO.setMes(getInteger(rs, "NUM_MES"));
				indicadorLojaVO.setNumMeta(getInteger(rs, "NUM_META"));
				indicadorLojaVO.setNumRealizado(getInteger(rs, "NUM_REALZ"));
				indicadorLojaVO.setNumRealizadoMeta(getDouble(rs, "NUM_REALZ_X_META"));
				indicadorLojaVO.setVlrPremioFilial(getDouble(rs, "VLR_PREMIO_FIL"));
				indicadorLojaVO.setVlrPremioFilialCalculado(getDouble(rs, "VLR_PREMIO_FIL_CALC"));
				indicadorLojaVO.setQuantidadeRealizada(getDouble(rs, "QTD_REALZ"));
				lista.add(indicadorLojaVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatorio por loja ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}


	/**
	 * Obtém resultados de indicadores por funcionário 
	 * 
	 * @param idLider
	 * @param idLoja
	 * @param ano
	 * @param mes
	 * @param idEmpresa
	 * @param grupoRemuneracao
	 * @param codigoIndicador
	 * @param codigoGrupo
	 * @return
	 * @throws PersistenciaException
	 */
	public List obtemRelatorioOperacional(Integer idLider, Integer idLoja, 
			Integer ano, Integer mes, Integer idEmpresa, String grupoRemuneracao,
			Integer codigoIndicador, Integer codigoGrupo, List idsFiliais) throws PersistenciaException {

		List parametroSQL = new ArrayList();
		
		StringBuffer query = new StringBuffer(
					" SELECT A.COD_FUNC 					" +
					" 		,A.COD_INDIC 					" +
					" 		,A.COD_EMP 						" +
					" 	 	,A.COD_FIL 						" +
					" 		,A.NUM_ANO 						" +
					" 		,A.NUM_MES 						" +
					" 		,B.NOME_FUNC 					" +
					" 		,C.DESCR_CARGO 					" +
					" 		,B.DT_ADMISSAO 					" +  
					" 	 	,B.DT_DEMISSAO   				" +
					" 		,A.NUM_REALZ     				" +
					" 		,A.NUM_REALZ_X_META 			" +
					" 		,E.VLR_SAL_BASE_REM_VAR 		" +
					" 		,A.PCT_CALC_RATEIO  			" +
					" 		,A.VLR_PREMIO_FUNC_CALC 		" +
					" 		,A.VLR_PREMIO 					" +
					" 		,A.NUM_META 					" +
					" FROM  SRV_REALIZADO_FUNC_INDICADOR A  " +  
					" 	   ,SRV_FUNCIONARIO B  			    " +
					" 	   ,SRV_CARGO C  					" +
					" 	   ,SRV_INDICADOR D  				" +
					" 	   ,SRV_FUNC_BASE_REM_VAR E			" +
					
					" WHERE A.COD_FUNC  =  B.COD_FUNC   	" +
					"   AND B.COD_CARGO =  C.COD_CARGO  	" +
					
					"   AND C.COD_CARGO IN (SELECT F.COD_CARGO                           " + 
					" 						  FROM SRV_GRUPO_CARGO F,                    " + 
					" 						       SRV_GRUPO_REM_VARIAVEL G              " +
					" 						 WHERE F.COD_GRP_REM_VAR = G.COD_GRP_REM_VAR " + 
					" 						   AND G.DESCR_GRP_REM_VAR = ?)              " +
					
					"   AND A.COD_INDIC 	= D.COD_INDIC   " +
					"   AND D.COD_INDIC 	= ?             " +
					"   AND D.COD_GRP_INDIC = ?             " +
					"   AND A.NUM_ANO 		= ?             " +
					"   AND A.NUM_MES 		= ?             " +
					"   AND E.COD_EMP    (+)= A.COD_EMP  	" + 
					"   AND E.COD_FIL    (+)= A.COD_FIL 	" +
					"   AND E.NUM_ANO    (+)= A.NUM_ANO  	" +
					"   AND E.NUM_MES    (+)= A.NUM_MES  	" +
					"   AND E.COD_FUNC   (+)= A.COD_FUNC 	");		

		parametroSQL.add(new ParametroSQL(PARAMTYPE_STRING,  grupoRemuneracao));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codigoIndicador));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codigoGrupo));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
				
		if (idEmpresa != null) {
			query.append(" AND A.COD_EMP = ? "); //1
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idEmpresa));
		}
		if (idLoja != null) {
			query.append(" AND A.COD_FIL = ?  "); //10
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idLoja));
		}
		if (idLider != null) {
			query.append(
					" AND A.COD_FIL IN (SELECT H.COD_FIL 				" + 
					"                     FROM SRV_FUNC_BASE_REM_VAR H 	" +
					"                    WHERE H.COD_EMP  = A.COD_EMP 	" +
					"                      AND H.NUM_ANO  = A.NUM_ANO	" +
					"                      AND H.NUM_MES  = A.NUM_MES	" +
					"                      AND H.COD_FUNC = ?) 			");
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idLider));
		}
		if (ObjectHelper.isNotEmpty(idsFiliais)) {
			query.append(" AND A.COD_FIL IN ( ");
			boolean pv = true;
			Iterator itIdsFiliais = idsFiliais.iterator();
			while (itIdsFiliais.hasNext()) {
				Integer idFil = (Integer)itIdsFiliais.next();
				if (!pv) {
					query.append(",?");
				} else {
					pv = false;
					query.append("?");
				}
				parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idFil));
			}
			query.append(" )");
		}
		query.append(" ORDER BY A.COD_FIL, B.NOME_FUNC ");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List lista = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);

			rs = stmt.executeQuery();
			while (rs.next()) {
				IndicadorFuncionarioVO indicadorFuncionarioVO = new IndicadorFuncionarioVO();
				
				indicadorFuncionarioVO.setIdFuncionario(getInteger(rs, "COD_FUNC"));
				indicadorFuncionarioVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				indicadorFuncionarioVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				indicadorFuncionarioVO.setCodFilial(getInteger(rs, "COD_FIL"));
				indicadorFuncionarioVO.setAno(getInteger(rs, "NUM_ANO"));
				indicadorFuncionarioVO.setMes(getInteger(rs, "NUM_MES"));
				indicadorFuncionarioVO.setNomeUsuario(getString(rs, "NOME_FUNC"));
				indicadorFuncionarioVO.setCargoUsuario(getString(rs, "DESCR_CARGO"));
				indicadorFuncionarioVO.setDataAdimissao(getDate(rs, "DT_ADMISSAO"));
				indicadorFuncionarioVO.setDataDemissao(getDate(rs, "DT_DEMISSAO"));
				indicadorFuncionarioVO.setBasePremio(getDouble(rs, "VLR_SAL_BASE_REM_VAR"));
				indicadorFuncionarioVO.setRateio(getDouble(rs, "PCT_CALC_RATEIO"));
				indicadorFuncionarioVO.setPremio(getDouble(rs, "VLR_PREMIO_FUNC_CALC"));
				indicadorFuncionarioVO.setValorPremio(getDouble(rs, "VLR_PREMIO"));
				indicadorFuncionarioVO.setMeta(getDouble(rs, "NUM_META"));
				indicadorFuncionarioVO.setValorRealizado(getDouble(rs, "NUM_REALZ"));
				indicadorFuncionarioVO.setNumRealizadoMeta(getDouble(rs, "NUM_REALZ_X_META"));
				lista.add(indicadorFuncionarioVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter relatorio por operacional/lideres ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}
	
	
	/**
	 * Obtém resultados de indicadores por funcionário 
	 * 
	 * @param idFuncionario
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 */
	public List obtemRelatorioOperacionalPorFuncionario(Integer idFuncionario, Integer ano, Integer mes) throws PersistenciaException {

		List parametroSQL = new ArrayList();
		
		StringBuffer query = new StringBuffer(
					" SELECT A.COD_EMP 						" +
					" 		,A.COD_FIL 						" +
					" 		,A.COD_FUNC 					" +
					" 		,A.COD_INDIC 					" +
					" 		,B.DESCR_INDIC 					" +
					" 		,A.NUM_META 					" +
					" 		,A.NUM_REALZ     				" +
					" 		,A.NUM_REALZ_X_META 			" +
					" 		,A.VLR_PREMIO 					" +
					" 		,A.VLR_PREMIO_FUNC_CALC 		" +
					
					" FROM  SRV_REALIZADO_FUNC_INDICADOR A  " +
					" 	   ,SRV_INDICADOR B  				" +
					
					" WHERE A.COD_INDIC   	= B.COD_INDIC   " +
					"   AND A.COD_FUNC     	= ?             " +
					"   AND A.NUM_ANO 		= ?             " +
					"   AND A.NUM_MES 		= ?             ");		

		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idFuncionario));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		
		query.append(" ORDER BY B.DESCR_INDIC ");
		
/*		
		SELECT B.COD_GRP_INDIC, C.DESCR_GRP_INDIC, A.COD_INDIC, B.DESCR_INDIC, A.COD_UN_PESO, A.NUM_PESO, A.COD_UN_META, A.NUM_META, A.COD_UN_REALZ, A.NUM_REALZ,
	       A.COD_UN_REALZ_X_META, A.NUM_REALZ_X_META, A.COD_ESCALA, A.COD_UN_REALZ_FX, A.NUM_REALZ_FX, A.COD_UN_VLR_PREMIO_FUNC_CALC, A.VLR_PREMIO_FUNC_CALC
	  FROM SRV_REALIZADO_FUNC_INDICADOR A, SRV_INDICADOR B, SRV_GRUPO_INDICADOR C, SRV_TIPO_REM_VAR D
	 WHERE A.COD_INDIC = B.COD_INDIC
	   AND B.COD_GRP_INDIC = C.COD_GRP_INDIC
	   AND C.COD_TIPO_REM_VAR = D.COD_TIPO_REM_VAR
	   AND D.DESCR_TIPO_REM_VAR = 'CORPORATIVO'
	 ORDER BY C.DESCR_GRP_INDIC, B.DESCR_INDIC; 		
*/		
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List lista = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);

			rs = stmt.executeQuery();
			while (rs.next()) {
				IndicadorFuncionarioVO indicadorFuncionarioVO = new IndicadorFuncionarioVO();
				indicadorFuncionarioVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				indicadorFuncionarioVO.setCodFilial(getInteger(rs, "COD_FIL"));
				indicadorFuncionarioVO.setIdFuncionario(getInteger(rs, "COD_FUNC"));
				indicadorFuncionarioVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
				indicadorFuncionarioVO.setAno(ano);
				indicadorFuncionarioVO.setMes(mes);
				indicadorFuncionarioVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				indicadorFuncionarioVO.setMeta(getDouble(rs, "NUM_META"));
				indicadorFuncionarioVO.setValorRealizado(getDouble(rs, "NUM_REALZ"));
				indicadorFuncionarioVO.setNumRealizadoMeta(getDouble(rs, "NUM_REALZ_X_META"));
				indicadorFuncionarioVO.setValorPremio(getDouble(rs, "VLR_PREMIO"));
				indicadorFuncionarioVO.setPremio(getDouble(rs, "VLR_PREMIO_FUNC_CALC"));
				lista.add(indicadorFuncionarioVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter relatorio operacional por funcionário", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}
	
	
	/**
	 * Obtém resultados de indicadores de bonus por funcionário 
	 * 
	 * @param idFuncionario
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 */
//	public List<IndicadorFuncionarioRealizadoVO> obtemRelatorioBonusOperacionalPorFuncionario(Long idFuncionario, Integer ano, Integer mes, Integer idGrupoIndic, Integer idIndicPai) throws PersistenciaException {
//
//		List<ParametroSQL> parametroSQL = new ArrayList<ParametroSQL>();
//		
//		StringBuffer query = new StringBuffer(
//				
//					"SELECT A.COD_EMP									" +
//					"      ,A.COD_FIL									" +
//					"      ,B.COD_GRP_INDIC								" +
//					"      ,C.DESCR_GRP_INDIC							" +
//					"	   ,A.COD_INDIC 								" +
//					"      ,B.DESCR_INDIC								" +
//					"      ,A.COD_UN_PESO								" +
//					"      ,A.NUM_PESO									" +
//					"      ,A.COD_UN_REALZ_POND							" +
//					"      ,A.NUM_REALZ_POND							" +
//					"      ,A.COD_UN_META								" +
//					"      ,A.NUM_META									" +
//					"      ,A.COD_UN_REALZ								" +
//					"      ,A.NUM_REALZ									" +
//					"      ,A.COD_UN_REALZ_X_META						" +
//					"      ,A.NUM_REALZ_X_META							" +
//					"      ,A.COD_ESCALA								" +
//					"      ,A.COD_UN_REALZ_FX							" +
//					"      ,A.NUM_REALZ_FX								" +
//					"      ,A.COD_UN_VLR_PREMIO_FUNC_CALC				" +
//					"      ,A.STA_CALC_REALZ							" +
//					"      ,A.VLR_PREMIO_FUNC_CALC						" +
//					"      ,A.DT_INI_SIT_SRV							" +
//					"      ,A.COD_USUARIO								" +
//					"      ,A.DESCR_META								" +
//					"      ,B.FLG_PREENCH_ATING_IGUAL_REALZ				" +
//					"      ,B.FORMULA_INDIC								" +
//					"      ,E.NUM_ESCALA								" +
//					"      ,B.DESCR_FORMULA_INDIC						" +
//					"      ,B.DESCR_FONTE								" +
//					"      ,B.COD_INDIC_PAI								" +
//					"  FROM SRV_REALIZADO_FUNC_INDICADOR A 				" +
//					"      ,SRV_INDICADOR                B 				" +
//					"      ,SRV_GRUPO_INDICADOR 		 C 				" +
//					"      ,SRV_TIPO_REM_VAR 			 D				" +
//					"      ,SRV_ESCALA              	 E				" +
//					" WHERE A.COD_INDIC          = B.COD_INDIC			" +
//					"   AND B.COD_GRP_INDIC      = C.COD_GRP_INDIC		" +
//					"   AND C.COD_TIPO_REM_VAR   = D.COD_TIPO_REM_VAR	" +
//					"   AND A.COD_ESCALA 		 = E.COD_ESCALA(+)		" +
//					"   AND D.DESCR_TIPO_REM_VAR = ?             		" +
//					"   AND A.COD_FUNC     	     = ?             		" +
//					"   AND A.NUM_ANO 		     = ?             		" +
//					"   AND A.NUM_MES 		     = ?             		");
//		if (ObjectHelper.isNotEmpty(idGrupoIndic)) {
//			query.append("   AND B.COD_GRP_INDIC = ?            		");
//		}
//		if (ObjectHelper.isNotEmpty(idIndicPai)) {
//			query.append("   AND B.COD_INDIC_PAI = ?            		");
//			query.append(" ORDER BY C.DESCR_GRP_INDIC, B.DESCR_INDIC	");
//		} else {
//			query.append("   AND B.COD_INDIC_PAI IS NULL         		");
//			query.append(" ORDER BY A.NUM_PESO DESC	");
//		}
//
//		
//		parametroSQL.add(new ParametroSQL(PARAMTYPE_STRING,  "CORPORATIVO"));
//		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, 	 idFuncionario));
//		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
//		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
//		if (ObjectHelper.isNotEmpty(idGrupoIndic)) {
//			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idGrupoIndic));
//		}
//		if (ObjectHelper.isNotEmpty(idIndicPai)) {
//			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idIndicPai));
//		}
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//		List<IndicadorFuncionarioRealizadoVO> lista = new ArrayList<IndicadorFuncionarioRealizadoVO>();
//		
//		try {
//			conn = getConn();
//			stmt = conn.prepareStatement(query.toString());
//			preencheParametros(stmt, parametroSQL);
//
//			rs = stmt.executeQuery();
//			while (rs.next()) {
//				IndicadorFuncionarioRealizadoVO indicadorFuncionarioRealizadoVO = new IndicadorFuncionarioRealizadoVO();
//				indicadorFuncionarioRealizadoVO.setIdFuncionario(idFuncionario);
//				indicadorFuncionarioRealizadoVO.setAno(ano);
//				indicadorFuncionarioRealizadoVO.setMes(mes);
//				indicadorFuncionarioRealizadoVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
//				indicadorFuncionarioRealizadoVO.setIdFilial(getInteger(rs, "COD_FIL"));
//				indicadorFuncionarioRealizadoVO.setIdGrupoIndicador(getInteger(rs, "COD_GRP_INDIC"));
//				indicadorFuncionarioRealizadoVO.setDescricaoGrupoIndicador(getString(rs, "DESCR_GRP_INDIC"));
//				indicadorFuncionarioRealizadoVO.setIdIndicador(getInteger(rs, "COD_INDIC"));
//				if (ObjectHelper.isNotEmpty(idIndicPai)) {
//					indicadorFuncionarioRealizadoVO.setDescricaoIndicador(" - " + getString(rs, "DESCR_INDIC"));
//				} else {
//					indicadorFuncionarioRealizadoVO.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
//				}
//				indicadorFuncionarioRealizadoVO.setUnidadePeso(getInteger(rs, "COD_UN_PESO"));
//				indicadorFuncionarioRealizadoVO.setPeso(getDouble(rs, "NUM_PESO"));
//				indicadorFuncionarioRealizadoVO.setUnidadeRealizadoPonderacao(getInteger(rs, "COD_UN_REALZ_POND"));
//				indicadorFuncionarioRealizadoVO.setRealizadoPonderacao(getDouble(rs, "NUM_REALZ_POND"));
//				indicadorFuncionarioRealizadoVO.setUnidadeMeta(getInteger(rs, "COD_UN_META"));
//				indicadorFuncionarioRealizadoVO.setMeta(getDouble(rs, "NUM_META"));
//				indicadorFuncionarioRealizadoVO.setUnidadeRealizado(getInteger(rs, "COD_UN_REALZ"));
//				indicadorFuncionarioRealizadoVO.setRealizado(getDouble(rs, "NUM_REALZ"));
//				indicadorFuncionarioRealizadoVO.setUnidadeRealizadoXMeta(getInteger(rs, "COD_UN_REALZ_X_META"));
//				indicadorFuncionarioRealizadoVO.setRealizadoXMeta(getDouble(rs, "NUM_REALZ_X_META"));
//				indicadorFuncionarioRealizadoVO.setIdEscala(getInteger(rs, "COD_ESCALA"));
//				indicadorFuncionarioRealizadoVO.setNumEscala(getInteger(rs, "COD_ESCALA"));//getInteger(rs, "NUM_ESCALA"));
//				indicadorFuncionarioRealizadoVO.setUnidadeRealizadoFaixa(getInteger(rs, "COD_UN_REALZ_FX"));
//				indicadorFuncionarioRealizadoVO.setRealizadoFaixa(getDouble(rs, "NUM_REALZ_FX"));
//				indicadorFuncionarioRealizadoVO.setUnidadeValorPremioCalculado(getInteger(rs, "COD_UN_VLR_PREMIO_FUNC_CALC"));
//				indicadorFuncionarioRealizadoVO.setValorPremio(getDouble(rs, "VLR_PREMIO_FUNC_CALC"));
//				indicadorFuncionarioRealizadoVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
//				indicadorFuncionarioRealizadoVO.setIdUsuarioAlteracao(getInteger(rs, "COD_USUARIO"));
//				indicadorFuncionarioRealizadoVO.setDescricaoMeta(getString(rs, "DESCR_META"));
//				indicadorFuncionarioRealizadoVO.setFlgPrrenchAtingIgualRealiz(getString(rs, "FLG_PREENCH_ATING_IGUAL_REALZ"));
//				indicadorFuncionarioRealizadoVO.setFormulaIndicador(getString(rs, "FORMULA_INDIC"));
//				indicadorFuncionarioRealizadoVO.setDescFormulaIndicador(getString(rs, "DESCR_FORMULA_INDIC"));
//				indicadorFuncionarioRealizadoVO.setDescFonte(getString(rs, "DESCR_FONTE"));
//				indicadorFuncionarioRealizadoVO.setIdIndicadorPai(getInteger(rs, "COD_INDIC_PAI"));
//				indicadorFuncionarioRealizadoVO.setIdStatusCalcRelz(getInteger(rs, "STA_CALC_REALZ"));
//				lista.add(indicadorFuncionarioRealizadoVO);
//			}
//		
//		} catch (Exception e) {
//			throw new PersistenciaException(log, "Nao foi possível obter relatorio operacional de bonus por funcionário", e);
//		} finally {
//			closeStatementAndResultSet(stmt, rs);
//		}
//		return lista;
//	}	


	/**
	 * Obtém os detalhes do calculo do indicador de um funcionario
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
	public DetalheCalculoVO obtemDetalheCalculo(int idFuncionario, int idIndicador, int idEmpresa, int idFilial, int ano, int mes) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT A.NUM_MES 				" +
				"       ,A.NUM_ANO				" +
				"       ,A.COD_FIL				" +
				"       ,A.COD_FUNC				" +
				"       ,D.NOME_FUNC			" +
				"       ,A.VLR_PREMIO			" +
				"       ,A.PCT_CALC_RATEIO		" +
				"       ,A.NUM_META				" +
				"       ,A.COD_UN_META          " +
				"       ,A.NUM_REALZ			" +
				"       ,A.COD_UN_REALZ         " +
				"       ,A.NUM_REALZ_X_META		" +
				"       ,A.COD_UN_REALZ_X_META  " +
				"       ,B.COD_ESCALA			" +
				"       ,B.DESCR_ESCALA			" +
				"       ,C.COD_UN_FX			" +
				"       ,C.NUM_INI_FX			" +
				"       ,C.NUM_FIM_FX			" +
				"       ,A.COD_UN_REALZ_FX		" +
				"       ,A.NUM_REALZ_FX			" +
				"       ,A.COD_POND				" +
				"       ,A.COD_UN_PESO			" +
				"       ,A.NUM_PESO				" +
				"       ,A.COD_UN_REALZ_POND	" +
				"       ,A.NUM_REALZ_POND		" +
				"       ,A.VLR_PREMIO_FUNC_CALC	" +
				"       ,A.VLR_PREMIO_FUNC_CALC_PROP	" +
				"       ,H.DESCR_SIT_CALC_REALZ_FUNC  	" +
				"       ,A.QTD_MESES_PROP		" +
				"       ,E.DESCR_FIL   			" +
				"       ,G.QTD_SALARIO_MIN      " +
				
				"  FROM SRV_REALIZADO_FUNC_INDICADOR A, " +
				"       SRV_ESCALA B, 					" +
				"       SRV_ESCALA_FAIXA C, 			" +
				"       SRV_FUNCIONARIO D,				" +
				"       SRV_FILIAL E,					" +
				"       SRV_CARGO F,					" +
				"       SRV_CLASSE_HAY G, 			    " +
				" 		SRV_SIT_CALC_REALZ_FUNC H       " +
				
				" WHERE B.COD_ESCALA 		(+) = A.COD_ESCALA			" +
				"   AND C.COD_ESCALA        (+) = A.COD_ESCALA			" +
				"   AND C.NUM_SEQ_ESCALA_FX (+) = A.NUM_SEQ_ESCALA_FX	" +
				"   AND D.COD_FUNC              = A.COD_FUNC			" +
				"   AND E.COD_EMP               = A.COD_EMP  			" +
				"   AND E.COD_FIL               = A.COD_FIL			    " +
				"   AND D.COD_CARGO             = F.COD_CARGO	        " +
				"   AND F.COD_CLAS_HAY          = G.COD_CLAS_HAY	    " +
				"   AND H.COD_SIT_CALC_REALZ_FUNC (+)  = A.COD_SIT_CALC_REALZ_FUNC  " +
				"   AND A.COD_FUNC 				= ? " +
				"   AND A.COD_INDIC 			= ? " +
				"   AND A.NUM_ANO 				= ? " +
				"   AND A.NUM_MES 				= ? ");
			
			if (idEmpresa > 0 && idFilial > 0) {
				query.append(
					"   AND A.COD_EMP 			= ? " +
					"   AND A.COD_FIL 			= ? ");
			}
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DetalheCalculoVO detalheCalculoVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			
			int nroCampo = 1;
			setInteger(stmt, nroCampo++, idFuncionario);
			setInteger(stmt, nroCampo++, idIndicador);
			setInteger(stmt, nroCampo++, ano);
			setInteger(stmt, nroCampo++, mes);
			if (idEmpresa > 0 && idFilial > 0) {
				setInteger(stmt, nroCampo++, idEmpresa);
				setInteger(stmt, nroCampo++, idFilial);
			}
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				detalheCalculoVO = new DetalheCalculoVO();
				detalheCalculoVO.setMes(getInteger(rs, "NUM_MES"));
				detalheCalculoVO.setMesExtenso(DataHelper.obtemMesExtenso(getInteger(rs, "NUM_MES").intValue(), Constantes.CAPTALIZADO_CASE));
				detalheCalculoVO.setAno(getInteger(rs, "NUM_ANO"));
				detalheCalculoVO.setIdFilial(getInteger(rs, "COD_FIL"));
				detalheCalculoVO.setDescricaoFilial(getString(rs, "DESCR_FIL"));
				detalheCalculoVO.setIdFuncionario(getInteger(rs, "COD_FUNC"));
				detalheCalculoVO.setNomeFuncionario(getString(rs, "NOME_FUNC"));
				detalheCalculoVO.setValorPremio(getDouble(rs, "VLR_PREMIO"));
				detalheCalculoVO.setPercentualRateio(getDouble(rs, "PCT_CALC_RATEIO"));
				detalheCalculoVO.setMeta(getDouble(rs, "NUM_META"));
				detalheCalculoVO.setUnidadeMeta(getInteger(rs, "COD_UN_META"));
				detalheCalculoVO.setRealizado(getDouble(rs, "NUM_REALZ"));
				detalheCalculoVO.setUnidadeRealizado(getInteger(rs, "COD_UN_REALZ"));
				detalheCalculoVO.setRealizadoXMeta(getDouble(rs, "NUM_REALZ_X_META"));
				detalheCalculoVO.setUnidadeRealizadoXMeta(getInteger(rs, "COD_UN_REALZ_X_META"));
				detalheCalculoVO.setCodigoEscala(getInteger(rs, "COD_ESCALA"));
				detalheCalculoVO.setDescricaoEscala(getString(rs, "VLR_PREMIO_FUNC_CALC"));
				detalheCalculoVO.setInicioFaixa(getDouble(rs, "NUM_INI_FX"));
				detalheCalculoVO.setFimFaixa(getDouble(rs, "NUM_FIM_FX"));
				detalheCalculoVO.setUnidadeFaixa(getInteger(rs, "COD_UN_FX"));
				detalheCalculoVO.setRealizadoFaixa(getDouble(rs, "NUM_REALZ_FX"));
				detalheCalculoVO.setUnidadeRealizadoFaixa(getInteger(rs, "COD_UN_REALZ_FX"));
				detalheCalculoVO.setCodigoPonderacao(getInteger(rs, "COD_POND"));
				detalheCalculoVO.setPesoPonderacao(getDouble(rs, "NUM_PESO"));
				detalheCalculoVO.setUnidadePesoPonderacao(getInteger(rs, "COD_UN_PESO"));
				detalheCalculoVO.setRealizadoPonderacao(getDouble(rs, "NUM_REALZ_POND"));
				detalheCalculoVO.setUnidadeRealizadoPonderacao(getInteger(rs, "COD_UN_REALZ_POND"));
				if(getDouble(rs, "VLR_PREMIO_FUNC_CALC_PROP")==null){
					detalheCalculoVO.setValorPremioCalculado(Double.valueOf("0"));
				}else{
					detalheCalculoVO.setValorPremioCalculado(getDouble(rs, "VLR_PREMIO_FUNC_CALC_PROP"));
				}
				
				detalheCalculoVO.setSalarioMinimoClasseHay(getDouble(rs, "QTD_SALARIO_MIN"));
				detalheCalculoVO.setMesesProporcional(getString(rs, "QTD_MESES_PROP"));
				detalheCalculoVO.setObservacao(getString(rs, "DESCR_SIT_CALC_REALZ_FUNC"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter os detalhes do cálculo", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return detalheCalculoVO;
	}
	
	
	/**
	 * Obtém os detalhes do calculo do indicador de uma loja
	 * 
	 * @param idIndicador
	 * @param idEmpresa
	 * @param idFilial
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 */
	public DetalheCalculoLojaVO obtemDetalheCalculoLoja(int idIndicador, int idEmpresa, int idFilial, int ano, int mes) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT A.NUM_ANO, 				" +
				"        A.NUM_MES, 				" +
				"        A.COD_EMP, 				" +
				"		 A.COD_FIL, 				" +
				"		 B.DESCR_FIL, 				" +
				"		 A.COD_UN_META, 			" +
				"		 A.NUM_META, 				" +
				"		 A.COD_UN_REALZ, 			" +
				"		 A.NUM_REALZ,				" +
				"        A.QTD_REALZ, 				" +
				"        A.COD_UN_REALZ_X_META, 	" +
				"        A.NUM_REALZ_X_META, 		" +
				"        A.COD_ESCALA, 				" +
				"        C.DESCR_ESCALA, 			" +
				"        D.COD_UN_FX, 				" +
				"        D.NUM_INI_FX, 				" +
				"        D.NUM_FIM_FX,				" +
				"        A.COD_UN_REALZ_FX, 		" +
				"        A.NUM_REALZ_FX, 			" +
				"        A.COD_UN_PREMIO_FIL_CALC, 	" +
				"        A.VLR_PREMIO_FIL_CALC      " +
				"  FROM SRV_REALIZADO_FILIAL A, 	" +
				"       SRV_FILIAL B, 				" +
				"       SRV_ESCALA C, 				" +
				"       SRV_ESCALA_FAIXA D		    " +
				
				" WHERE A.COD_EMP               = B.COD_EMP  			" +
				"   AND A.COD_FIL               = B.COD_FIL			    " +
				"   AND C.COD_ESCALA 		(+) = A.COD_ESCALA			" +
				"   AND D.COD_ESCALA        (+) = A.COD_ESCALA			" +
				"   AND D.NUM_SEQ_ESCALA_FX (+) = A.NUM_SEQ_ESCALA_FX	" +
				"   AND A.COD_INDIC 			= ? " +
				"   AND A.COD_EMP 				= ? " +
				"   AND A.COD_FIL 				= ? " +
				"   AND A.NUM_ANO 				= ? " +
				"   AND A.NUM_MES 				= ? ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		DetalheCalculoLojaVO detalheCalculoLojaVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			
			int nroCampo = 1;
			setInteger(stmt, nroCampo++, idIndicador);
			setInteger(stmt, nroCampo++, idEmpresa);
			setInteger(stmt, nroCampo++, idFilial);
			setInteger(stmt, nroCampo++, ano);
			setInteger(stmt, nroCampo++, mes);
			
			rs = stmt.executeQuery();
			if (rs.next()) {
				detalheCalculoLojaVO = new DetalheCalculoLojaVO();
				detalheCalculoLojaVO.setMes(getInteger(rs, "NUM_MES"));
				detalheCalculoLojaVO.setMesExtenso(DataHelper.obtemMesExtenso(getInteger(rs, "NUM_MES").intValue(), Constantes.CAPTALIZADO_CASE));
				detalheCalculoLojaVO.setAno(getInteger(rs, "NUM_ANO"));
				detalheCalculoLojaVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				detalheCalculoLojaVO.setIdFilial(getInteger(rs, "COD_FIL"));
				detalheCalculoLojaVO.setDescricaoFilial(getString(rs, "DESCR_FIL"));
				detalheCalculoLojaVO.setMeta(getDouble(rs, "NUM_META"));
				detalheCalculoLojaVO.setUnidadeMeta(getInteger(rs, "COD_UN_META"));
				detalheCalculoLojaVO.setRealizado(getDouble(rs, "NUM_REALZ"));
				detalheCalculoLojaVO.setUnidadeRealizado(getInteger(rs, "COD_UN_REALZ"));
				detalheCalculoLojaVO.setQtdeRealizado(getInteger(rs, "QTD_REALZ"));
				detalheCalculoLojaVO.setRealizadoXMeta(getDouble(rs, "NUM_REALZ_X_META"));
				detalheCalculoLojaVO.setUnidadeRealizadoXMeta(getInteger(rs, "COD_UN_REALZ_X_META"));
				detalheCalculoLojaVO.setCodigoEscala(getInteger(rs, "COD_ESCALA"));
				detalheCalculoLojaVO.setDescricaoEscala(getString(rs, "DESCR_ESCALA"));
				detalheCalculoLojaVO.setInicioFaixa(getDouble(rs, "NUM_INI_FX"));
				detalheCalculoLojaVO.setFimFaixa(getDouble(rs, "NUM_FIM_FX"));
				detalheCalculoLojaVO.setUnidadeFaixa(getInteger(rs, "COD_UN_FX"));
				detalheCalculoLojaVO.setRealizadoFaixa(getDouble(rs, "NUM_REALZ_FX"));
				detalheCalculoLojaVO.setUnidadeRealizadoFaixa(getInteger(rs, "COD_UN_REALZ_FX"));
				detalheCalculoLojaVO.setUnidadePremioCalculado(getInteger(rs, "COD_UN_PREMIO_FIL_CALC"));
				detalheCalculoLojaVO.setValorPremioCalculado(getDouble(rs, "VLR_PREMIO_FIL_CALC"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter os detalhes do cálculo de loja", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return detalheCalculoLojaVO;
	}	

	public List obtemRelatorioExcelBonusOperacionalPorFuncionario(Long idFuncionario, Integer ano, Integer mes) throws PersistenciaException {
		List parametroSQL = new ArrayList();
		StringBuffer query = new StringBuffer(
				"SELECT R.FORMULA, " +
				"       R.INDICADOR, " +
				"       R.PESO, " +
				"       R.DESCRICAO_META, " +
				"       R.REALIZADO, " +
				"       R.ATINGIMENTO_META, " +
				"       R.ESCALA, " +
				"       R.RESULTADO_CONFORME_ESCALA, " +
				"       R.PESO_X_RESULTADO, " +
				"       R.ORDEM " +
				"  FROM ( " +
				"        SELECT B.FORMULA_INDIC    AS FORMULA, " +
				"                B.DESCR_INDIC      AS INDICADOR, " +
				"                A.NUM_PESO         AS PESO, " +
				"                A.DESCR_META       AS DESCRICAO_META, " +
				"                A.NUM_REALZ        AS REALIZADO, " +
				"                A.NUM_REALZ_X_META AS ATINGIMENTO_META, " +
				"                E.NUM_ESCALA       AS ESCALA, " +
				"                A.NUM_REALZ_FX     AS RESULTADO_CONFORME_ESCALA, " +
				"                A.NUM_REALZ_POND   AS PESO_X_RESULTADO, " +
				"                1                  AS ORDEM " +
				"          FROM SRV_REALIZADO_FUNC_INDICADOR A, " +
				"                SRV_INDICADOR                B, " +
				"                SRV_GRUPO_INDICADOR          C, " +
				"                SRV_TIPO_REM_VAR             D, " +
				"                SRV_ESCALA                   E " +
				"         WHERE A.COD_INDIC = B.COD_INDIC " +
				"           AND B.COD_GRP_INDIC = C.COD_GRP_INDIC " +
				"           AND C.COD_TIPO_REM_VAR = D.COD_TIPO_REM_VAR " +
				"           AND A.COD_ESCALA = E.COD_ESCALA(+) " +
				"           AND D.DESCR_TIPO_REM_VAR = 'CORPORATIVO' " +
				"           AND C.DESCR_GRP_INDIC = 'CORPORATIVO' " +
				"           AND A.COD_FUNC = ? " +
				"           AND A.NUM_ANO = ? " +
				"           AND A.NUM_MES = ? " +
				"           AND B.COD_INDIC_SIS IS NULL " +
				"        UNION ALL " +
				"        SELECT NULL AS FORMULA, " +
				"               'TOTAL' AS INDICADOR, " +
				"               SUM(A.NUM_PESO) AS PESO, " +
				"               NULL AS DESCRICAO_META, " +
				"               NULL AS REALIZADO, " +
				"               NULL AS ATINGIMENTO_META, " +
				"               NULL AS ESCALA, " +
				"               NULL AS RESULTADO_CONFORME_ESCALA, " +
				"               NULL AS PESO_X_RESULTADO, " +
				"               2 AS ORDEM " +
				"          FROM SRV_REALIZADO_FUNC_INDICADOR A, " +
				"               SRV_INDICADOR                B, " +
				"               SRV_GRUPO_INDICADOR          C, " +
				"               SRV_TIPO_REM_VAR             D, " +
				"               SRV_ESCALA                   E " +
				"         WHERE A.COD_INDIC = B.COD_INDIC " +
				"           AND B.COD_GRP_INDIC = C.COD_GRP_INDIC " +
				"           AND C.COD_TIPO_REM_VAR = D.COD_TIPO_REM_VAR " +
				"           AND A.COD_ESCALA = E.COD_ESCALA(+) " +
				"           AND D.DESCR_TIPO_REM_VAR = 'CORPORATIVO' " +
				"           AND C.DESCR_GRP_INDIC = 'CORPORATIVO' " +
				"           AND A.COD_FUNC = ? " +
				"           AND A.NUM_ANO = ? " +
				"           AND A.NUM_MES = ? " +
				"           AND B.COD_INDIC_SIS IS NULL " +
				"        UNION ALL " +
				"        SELECT B.FORMULA_INDIC    AS FORMULA, " +
				"               B.DESCR_INDIC      AS INDICADOR, " +
				"               A.NUM_PESO         AS PESO, " +
				"               A.DESCR_META       AS DESCRICAO_META, " +
				"               A.NUM_REALZ        AS REALIZADO, " +
				"               A.NUM_REALZ_X_META AS ATINGIMENTO_META, " +
				"               E.NUM_ESCALA       AS ESCALA, " +
				"               A.NUM_REALZ_FX     AS RESULTADO_CONFORME_ESCALA, " +
				"               A.NUM_REALZ_POND   AS PESO_X_RESULTADO, " +
				"               3                  AS ORDEM " +
				"          FROM SRV_REALIZADO_FUNC_INDICADOR A, " +
				"               SRV_INDICADOR                B, " +
				"               SRV_GRUPO_INDICADOR          C, " +
				"               SRV_TIPO_REM_VAR             D, " +
				"               SRV_ESCALA                   E " +
				"         WHERE A.COD_INDIC = B.COD_INDIC " +
				"           AND B.COD_GRP_INDIC = C.COD_GRP_INDIC " +
				"           AND C.COD_TIPO_REM_VAR = D.COD_TIPO_REM_VAR " +
				"           AND A.COD_ESCALA = E.COD_ESCALA(+) " +
				"           AND D.DESCR_TIPO_REM_VAR = 'CORPORATIVO' " +
				"           AND C.DESCR_GRP_INDIC = 'INDIVIDUAL' " +
				"           AND A.COD_FUNC = ? AND A.NUM_ANO = ? " +
				"           AND A.NUM_MES = ? " +
				"           AND B.COD_INDIC_SIS IS NULL " +
				"        UNION ALL " +
				"        SELECT NULL AS FORMULA, " +
				"               'TOTAL' AS INDICADOR, " +
				"               SUM(A.NUM_PESO) AS PESO, " +
				"               NULL AS DESCRICAO_META, " +
				"               NULL AS REALIZADO, " +
				"               NULL AS ATINGIMENTO_META, " +
				"               NULL AS ESCALA, " +
				"               NULL AS RESULTADO_CONFORME_ESCALA, " +
				"               NULL AS PESO_X_RESULTADO, " +
				"               4 AS ORDEM " +
				"          FROM SRV_REALIZADO_FUNC_INDICADOR A, " +
				"               SRV_INDICADOR                B, " +
				"               SRV_GRUPO_INDICADOR          C, " +
				"               SRV_TIPO_REM_VAR             D, " +
				"               SRV_ESCALA                   E " +
				"         WHERE A.COD_INDIC = B.COD_INDIC " +
				"           AND B.COD_GRP_INDIC = C.COD_GRP_INDIC " +
				"           AND C.COD_TIPO_REM_VAR = D.COD_TIPO_REM_VAR " +
				"           AND A.COD_ESCALA = E.COD_ESCALA(+) " +
				"           AND D.DESCR_TIPO_REM_VAR = 'CORPORATIVO' " +
				"           AND C.DESCR_GRP_INDIC = 'INDIVIDUAL' " +
				"           AND A.COD_FUNC = ? " +
				"           AND A.NUM_ANO = ? " +
				"           AND A.NUM_MES = ? " +
				"           AND B.COD_INDIC_SIS IS NULL " +
				"        UNION ALL " +
				"        SELECT NULL AS FORMULA, " +
				"               'RESULTADO' AS INDICADOR, " +
				"               A.NUM_PESO AS PESO, " +
				"               NULL AS DESCRICAO_META, " +
				"               NULL AS REALIZADO, " +
				"               NULL AS ATINGIMENTO_META, " +
				"               NULL AS ESCALA, " +
				"               NULL AS RESULTADO_CONFORME_ESCALA, " +
				"               A.NUM_REALZ_POND AS PESO_X_RESULTADO, " +
				"               5 AS ORDEM " +
				"          FROM SRV_REALIZADO_FUNC_INDICADOR A, " +
				"               SRV_INDICADOR                B, " +
				"               SRV_GRUPO_INDICADOR          C, " +
				"               SRV_TIPO_REM_VAR             D " +
				"         WHERE A.COD_INDIC = B.COD_INDIC " +
				"           AND B.COD_GRP_INDIC = C.COD_GRP_INDIC " +
				"           AND C.COD_TIPO_REM_VAR = D.COD_TIPO_REM_VAR " +
				"           AND D.DESCR_TIPO_REM_VAR = 'CORPORATIVO' " +
				"           AND A.COD_FUNC = ? " +
				"           AND A.NUM_ANO = ? " +
				"           AND A.NUM_MES = ? " +
				"           AND B.COD_INDIC_SIS IS NOT NULL) R " +
				" ORDER BY R.ORDEM ");

		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List list;
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			rs = stmt.executeQuery();
			list = Tools.montaRelatorio(rs, true, true);
		} catch(Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter relatorio operacional de bonus por funcionário", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return list;
	}

}