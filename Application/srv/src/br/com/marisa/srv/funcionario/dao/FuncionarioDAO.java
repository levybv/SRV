package br.com.marisa.srv.funcionario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.util.tools.Tools;


/**
 * Classe para tratar dos métodos de acesso a dados a funcionários
 * 
 * @author Walter Fontes
 */
public class FuncionarioDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(FuncionarioDAO.class);
    
    /**
     * Obtém todos os lideres de um grupo de remuneração
     * 
     * @return
     * @throws PersistenciaException
     */
	public List obtemLideres(String grupoRemuneracao) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"SELECT A.COD_FUNC, 				" +
				" 		A.NOME_FUNC, 				" +
				" 		D.DESCR_CARGO				" +
				" FROM  SRV_FUNCIONARIO A, 			" +
				" 		SRV_GRUPO_CARGO B, 			" +
				" 		SRV_GRUPO_REM_VARIAVEL C,	" +
				" 		SRV_CARGO D 				" +
				" WHERE A.COD_CARGO 		= B.COD_CARGO 		" +
				"   AND A.COD_CARGO 		= D.COD_CARGO 		" +
				"   AND B.COD_GRP_REM_VAR 	= C.COD_GRP_REM_VAR " +
				"   AND C.DESCR_GRP_REM_VAR = ? 				" +
				" ORDER BY D.DESCR_CARGO, A.NOME_FUNC " );

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List lista = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i=0;
			setString(stmt,++i, grupoRemuneracao);
			rs = stmt.executeQuery();
		         
			while (rs.next()) {
				FuncionarioVO funcionarioVO = new FuncionarioVO();
				funcionarioVO.setIdFuncionario(getLong(rs, "COD_FUNC"));
				funcionarioVO.setNomeFuncionario(getString(rs, "NOME_FUNC"));
				funcionarioVO.setDescricaoCargo(getString(rs, "DESCR_CARGO"));
				lista.add(funcionarioVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de lideres ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}
	
	/**
     * Obtém todos os lideres de um grupo de remuneração
     *  que tenham status diferente do parametrizado
     * @return
     * @throws PersistenciaException
     */
	public List<FuncionarioVO> obtemListaFuncionariosByIdGrupo(Integer idGrupo,
			Integer notSitRH) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"SELECT A.COD_FUNC, 				" +
				" 		A.NOME_FUNC, 				" +
				" 		D.DESCR_CARGO				" +
				" FROM  SRV_FUNCIONARIO A, 			" +
				" 		SRV_GRUPO_CARGO B, 			" +
				" 		SRV_CARGO D 				" +
				" WHERE A.COD_CARGO 		= B.COD_CARGO 		" +
				"   AND A.COD_CARGO 		= D.COD_CARGO 		" +
				"   AND B.COD_GRP_REM_VAR 	= ? " );
				

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<FuncionarioVO> lista = new ArrayList<FuncionarioVO> ();
		List <ParametroSQL>parametroSQL = new ArrayList<ParametroSQL>();
		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idGrupo));
		if (notSitRH != null) {
			query.append(" AND A.COD_SIT_RH <> ? ");
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, notSitRH));
		}
		query.append(" ORDER BY A.NOME_FUNC,D.DESCR_CARGO " );
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			rs = stmt.executeQuery();
		         
			while (rs.next()) {
				FuncionarioVO funcionarioVO = new FuncionarioVO();
				funcionarioVO.setIdFuncionario(getLong(rs, "COD_FUNC"));
				funcionarioVO.setNomeFuncionario(getString(rs, "NOME_FUNC"));
				lista.add(funcionarioVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de lideres ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs,conn);
		}
		return lista;
	}

	/**
	 * 
	 * @param idFuncionario
	 * @param nomeFuncionario
	 * @param cracha
	 * @param cpfFuncionario
	 * @param idCargo
	 * @param idsFuncs
	 * @param idsFiliais
	 * @param idsSituacaoRH
	 * @return
	 * @throws PersistenciaException
	 */
	public List<FuncionarioVO> obtemListaFuncionario(Long idFuncionario, String nomeFuncionario, String cracha, Long cpfFuncionario, Integer idCargo, List<Long> idsFuncs, List<Integer> idsFiliais, List<Integer> idsSituacaoRH) throws PersistenciaException {		
		StringBuffer query =  new StringBuffer(
				"SELECT A.COD_FUNC, 				" +
				" 		A.COD_EMP, 					" +
				" 		A.COD_FIL, 					" +
				" 		B.DESCR_FIL, 				" +
				" 		A.COD_FIL_RH,				" +
				"       A.DESCR_FIL_RH,  			" +
				" 		A.COD_CARGO, 				" +
				" 		C.DESCR_CARGO,				" +
				" 		A.COD_CRACHA, 				" +
				" 		A.NUM_CPF_FUNC,				" +
				" 		A.NOME_FUNC,				" +
				" 		A.COD_SIT_RH, 				" +
				" 		A.DESCR_SIT_RH,				" +
				" 	 	A.DT_INI_SIT_RH,			" +
				" 		A.COD_EMP_RH,				" +
				"       A.DESCR_EMP_RH,             " +
				" 		A.COD_CCST_FUNC, 			" +
				" 		A.DESCR_CCST_FUNC,			" +
				" 		A.COD_FUNC_SUPERIOR,		" +
				" 		A.COD_FUN_AVALIADOR, 		" +
				" 		A.DT_ADMISSAO,				" +
				" 		A.COD_SIT_RH_ANT,			" +
				" 		A.DESCR_SIT_RH_ANT,			" +
				" 		A.DT_INI_SIT_RH_ANT,		" +
				" 		A.QTD_DIAS_TRAB_PER,		" +
				" 		A.DT_DEMISSAO,				" +
				" 		A.DT_INI_SIT_SRV,			" +
				" 		A.COD_USUARIO 				" +
				" FROM  SRV_FUNCIONARIO A, 			" +
				" 		SRV_FILIAL 		B,			" +
				" 		SRV_CARGO 		C 			" +
				" WHERE A.COD_EMP 	= B.COD_EMP 	" +
				"   AND A.COD_FIL 	= B.COD_FIL		" +
				"   AND A.COD_CARGO = C.COD_CARGO	");
		
		List<ParametroSQL> parametroSQL = new ArrayList<ParametroSQL>();
		if (idFuncionario == null) {
			query.append(" AND A.DT_DEMISSAO IS NULL ");
		}
//		if (notSituacaoRH != null) {
//			query.append(" AND A.COD_SIT_RH <> ? ");
//			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, notSituacaoRH));
//		}
		if (idFuncionario != null) {
			query.append(" AND A.COD_FUNC = ? ");
			parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario));
		}
		if (ObjectHelper.isNotEmpty(nomeFuncionario)) {
			query.append(" AND UPPER(A.NOME_FUNC) LIKE '%'||?||'%' ");
			parametroSQL.add(new ParametroSQL(PARAMTYPE_STRING, nomeFuncionario.trim().toUpperCase()));
		}
		if (ObjectHelper.isNotEmpty(cracha)) {
			query.append(" AND UPPER(A.COD_CRACHA) LIKE '%'||?||'%' ");
			parametroSQL.add(new ParametroSQL(PARAMTYPE_STRING, cracha.trim().toUpperCase()));
		}
		if (cpfFuncionario != null) {
			query.append(" AND A.NUM_CPF_FUNC = ? ");
			parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, cpfFuncionario));
		}
		if (idCargo != null) {
			query.append(" AND A.COD_CARGO = ? ");
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idCargo));
		}
		if (ObjectHelper.isNotEmpty(idsFuncs)) {
			query.append(" AND A.COD_FUNC IN ( ");
			Iterator<Long> itIdsFuncs = idsFuncs.iterator();
			int i=0;
			while (itIdsFuncs.hasNext()) {
				if (i==0) {
					query.append("?");
				} else {
					query.append(",?");
				}
				i++;
				Long idFunc = (Long)itIdsFuncs.next();
				parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, idFunc));
			}
			query.append(" ) ");
		}
		if (ObjectHelper.isNotEmpty(idsFiliais)) {
			query.append(" AND A.COD_FIL IN (");
			boolean pv = true;
			Iterator<Integer> itIdsFiliais = idsFiliais.iterator();
			while (itIdsFiliais.hasNext()) {
				Integer idFil = itIdsFiliais.next();
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

		query.append(" ORDER BY A.NOME_FUNC ");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<FuncionarioVO> funcionarios = new ArrayList<FuncionarioVO>();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			rs = stmt.executeQuery();
		         
			while (rs.next()) {
				FuncionarioVO funcionarioVO = new FuncionarioVO();
				funcionarioVO.setIdFuncionario(getLong(rs, "COD_FUNC"));
				funcionarioVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				funcionarioVO.setIdFilial(getInteger(rs, "COD_FIL"));
				funcionarioVO.setDescricaoFilial(getString(rs, "DESCR_FIL"));
				funcionarioVO.setIdFilialRH(getString(rs, "COD_FIL_RH"));
				funcionarioVO.setDescricaoFilialRH(getString(rs, "DESCR_FIL_RH"));
				funcionarioVO.setIdCargo(getInteger(rs, "COD_CARGO"));
				funcionarioVO.setDescricaoCargo(getString(rs, "DESCR_CARGO"));
				funcionarioVO.setCracha(getString(rs, "COD_CRACHA"));
				funcionarioVO.setCpfFuncionario(getLong(rs, "NUM_CPF_FUNC"));
				funcionarioVO.setNomeFuncionario(getString(rs, "NOME_FUNC"));
				funcionarioVO.setIdSituacaoRH(getInteger(rs, "COD_SIT_RH"));
				funcionarioVO.setDescricaoSituacaoRH(getString(rs, "DESCR_SIT_RH"));
				funcionarioVO.setDataSituacaoRH(getDate(rs, "DT_INI_SIT_RH"));
				funcionarioVO.setIdEmpresaRH(getInteger(rs, "COD_EMP_RH"));
				funcionarioVO.setDescricaoFilialRH(getString(rs, "DESCR_EMP_RH"));
				funcionarioVO.setIdCentroCusto(getString(rs, "COD_CCST_FUNC"));
				funcionarioVO.setDescricaoCentroCusto(getString(rs, "DESCR_CCST_FUNC"));
				funcionarioVO.setIdFuncionarioSuperior(getLong(rs, "COD_FUNC_SUPERIOR"));
				funcionarioVO.setIdFuncionarioAvaliador(getLong(rs, "COD_FUN_AVALIADOR"));
				funcionarioVO.setDataAdmissao(getDate(rs, "DT_ADMISSAO"));
				funcionarioVO.setDataDemissao(getDate(rs, "DT_DEMISSAO"));
				funcionarioVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				funcionarioVO.setDataUltimaAlteracao(getDate(rs, "DT_INI_SIT_SRV"));
				funcionarioVO.setSituacaoColaborador(getString(rs, "DESCR_SIT_RH"));
				funcionarioVO.setIdSituacaoAnterior(getInteger(rs, "COD_SIT_RH_ANT"));
				funcionarioVO.setDescricaoSituacaoAnterior(getString(rs, "DESCR_SIT_RH_ANT"));
				funcionarioVO.setDataInicioSituacaoAnterior(getDate(rs, "DT_INI_SIT_RH_ANT"));
				funcionarioVO.setQtdDiasTrabalhadosMes(getInteger(rs, "QTD_DIAS_TRAB_PER"));
				funcionarios.add(funcionarioVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de funcionários ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return funcionarios;
	}
	
	
    /**
     * Obtém ids de funcionários subordinados
     * 
     * @param idFuncionario
     * @return
     * @throws PersistenciaException
     */
	public List obtemFuncionariosSubordinados(Long idFuncionario) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"SELECT COD_FUNC 				" +
				" FROM  SRV_FUNCIONARIO 		" +
				"  WHERE ESTRUTURA_GESTOR = " +
				"        (SELECT ESTRUTURA FROM SRV_FUNCIONARIO WHERE COD_FUNC = ?) " +
				" ORDER BY COD_FUNC	            ");
		
		List parametroSQL = new ArrayList();
		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario));
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List idsFuncionarios = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			rs = stmt.executeQuery();
		    
			while (rs.next()) {
				idsFuncionarios.add(getLong(rs, "COD_FUNC"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de funcionários ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return idsFuncionarios;
	}
	
	public List obtemFuncionariosSubordinadosElegiveis(Long idFuncionario) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT COD_FUNC " +
				"   FROM SRV_FUNCIONARIO " +
				"  WHERE ESTRUTURA_GESTOR = " +
				"        (SELECT ESTRUTURA FROM SRV_FUNCIONARIO WHERE COD_FUNC = ?) " +
				"    AND COD_CARGO IN (SELECT COD_CARGO FROM SRV_GRUPO_CARGO GC WHERE GC.COD_GRP_REM_VAR = 1) " +
				"  ORDER BY COD_FUNC ");

		List parametroSQL = new ArrayList();
		parametroSQL.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario));
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List idsFuncionarios = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			rs = stmt.executeQuery();
		    
			while (rs.next()) {
				idsFuncionarios.add(getLong(rs, "COD_FUNC"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de funcionários ", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return idsFuncionarios;
	}
	
    /**
     * Obtém funcionário conforme id
     * 
     * @param idFuncionario
     * @return
     * @throws PersistenciaException
     */
	public FuncionarioVO obtemFuncionario(Long idFuncionario) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"SELECT A.COD_FUNC, 				" +
				" 		A.COD_EMP, 					" +
				" 		A.COD_FIL, 					" +
				" 		B.DESCR_FIL, 				" +
				" 		A.COD_FIL_RH,				" +
				"       A.DESCR_FIL_RH,  			" +
				" 		A.COD_CARGO, 				" +
				" 		C.DESCR_CARGO,				" +
				" 		A.COD_CRACHA, 				" +
				" 		A.NUM_CPF_FUNC,				" +
				" 		A.NOME_FUNC,				" +
				" 		A.COD_SIT_RH, 				" +
				" 		A.DESCR_SIT_RH,				" +
				" 	 	A.DT_INI_SIT_RH,			" +
				" 		A.COD_EMP_RH,				" +
				"       A.DESCR_EMP_RH,             " +
				" 		A.COD_CCST_FUNC, 			" +
				" 		A.DESCR_CCST_FUNC,			" +
				" 		A.COD_FUNC_SUPERIOR,		" +
				" 		A.COD_FUN_AVALIADOR, 		" +
				" 		A.DT_ADMISSAO,				" +
				" 		A.DT_DEMISSAO,				" +
				" 		A.DT_INI_SIT_SRV,			" +
				" 		A.DT_PROMO_ELEG,			" +
				" 		A.COD_SIT_RH_ANT,			" +
				" 		A.DESCR_SIT_RH_ANT,			" +
				" 		A.DT_INI_SIT_RH_ANT,		" +
				" 		A.QTD_DIAS_TRAB_PER,		" +
				" 		A.COD_MOT_DEMISSAO,			" +
				" 		A.QTD_DIAS_AFASTAM,			" +
				" 		A.FLG_CCST_COMERCIAL,		" +
				" 		A.COD_USUARIO 				" +
//				" 		A.DESC_DIRETORIA			" +
				" FROM  SRV_FUNCIONARIO A, 			" +
				" 		SRV_FILIAL 		B,			" +
				" 		SRV_CARGO 		C 			" +
				" WHERE A.COD_EMP 	= B.COD_EMP 	" +
				"   AND A.COD_FIL 	= B.COD_FIL		" +
				"   AND A.COD_CARGO = C.COD_CARGO	" +
				"   AND A.COD_FUNC  = ? 			");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		FuncionarioVO funcionarioVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			setLong(stmt, 1, idFuncionario);
			rs = stmt.executeQuery();
		         
			if (rs.next()) {
				funcionarioVO = new FuncionarioVO();
				funcionarioVO.setIdFuncionario(getLong(rs, "COD_FUNC"));
				funcionarioVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				funcionarioVO.setIdFilial(getInteger(rs, "COD_FIL"));
				funcionarioVO.setDescricaoFilial(getString(rs, "DESCR_FIL"));
				funcionarioVO.setIdFilialRH(getString(rs, "COD_FIL_RH"));
				funcionarioVO.setDescricaoFilialRH(getString(rs, "DESCR_FIL_RH"));
				funcionarioVO.setIdCargo(getInteger(rs, "COD_CARGO"));
				funcionarioVO.setDescricaoCargo(getString(rs, "DESCR_CARGO"));
				funcionarioVO.setCracha(getString(rs, "COD_CRACHA"));
				funcionarioVO.setCpfFuncionario(getLong(rs, "NUM_CPF_FUNC"));
				funcionarioVO.setNomeFuncionario(getString(rs, "NOME_FUNC"));
				funcionarioVO.setIdSituacaoRH(getInteger(rs, "COD_SIT_RH"));
				funcionarioVO.setDescricaoSituacaoRH(getString(rs, "DESCR_SIT_RH"));
				funcionarioVO.setDataSituacaoRH(getDate(rs, "DT_INI_SIT_RH"));
				funcionarioVO.setIdEmpresaRH(getInteger(rs, "COD_EMP_RH"));
				funcionarioVO.setDescricaoEmpresaRH(getString(rs, "DESCR_EMP_RH"));
				funcionarioVO.setIdCentroCusto(getString(rs, "COD_CCST_FUNC"));
				funcionarioVO.setDescricaoCentroCusto(getString(rs, "DESCR_CCST_FUNC"));
				funcionarioVO.setIdFuncionarioSuperior(getLong(rs, "COD_FUNC_SUPERIOR"));
				funcionarioVO.setIdFuncionarioAvaliador(getLong(rs, "COD_FUN_AVALIADOR"));
				funcionarioVO.setDataAdmissao(getDate(rs, "DT_ADMISSAO"));
				funcionarioVO.setDataDemissao(getDate(rs, "DT_DEMISSAO"));
				funcionarioVO.setDataUltimaAlteracao(getDate(rs, "DT_INI_SIT_SRV"));
				funcionarioVO.setDataPromocaoElegivel(getDate(rs,"DT_PROMO_ELEG"));
				funcionarioVO.setCodigoMotivoDEmissao(getInteger(rs, "COD_MOT_DEMISSAO"));
				funcionarioVO.setQuantidadeDiasAfastamento(getInteger(rs, "QTD_DIAS_AFASTAM"));
				funcionarioVO.setFlagIndicadorCentroCusto(getString(rs, "FLG_CCST_COMERCIAL"));
				funcionarioVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				funcionarioVO.setIdSituacaoAnterior(getInteger(rs, "COD_SIT_RH_ANT"));
				funcionarioVO.setDescricaoSituacaoAnterior(getString(rs, "DESCR_SIT_RH_ANT"));
				funcionarioVO.setDataInicioSituacaoAnterior(getDate(rs, "DT_INI_SIT_RH_ANT"));
				funcionarioVO.setQtdDiasTrabalhadosMes(getInteger(rs, "QTD_DIAS_TRAB_PER"));
				funcionarioVO.setSituacaoColaborador(getString(rs, "DESCR_SIT_RH"));
				//funcionarioVO.setDescDiretoria(getString(rs, "DESC_DIRETORIA"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter o funcionario: " + idFuncionario, e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return funcionarioVO;
	}
	
	
    /**
     * Inclui funcionário
     * 
     * @param funcionarioVO
     * @return
     * @throws PersistenciaException
     */
	public void incluiFuncionario(FuncionarioVO funcionarioVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"INSERT INTO SRV_FUNCIONARIO ( 	" +
				"       COD_FUNC, 				" +
				" 		COD_EMP, 				" +
				" 		COD_FIL, 				" +
				" 		COD_FIL_RH,				" +
				"       DESCR_FIL_RH,  			" +
				" 		COD_CARGO, 				" +
				" 		COD_CRACHA, 			" +
				" 		NUM_CPF_FUNC,			" +
				" 		NOME_FUNC,				" +
				" 		COD_SIT_RH, 			" +
				" 		DESCR_SIT_RH,			" +
				" 	 	DT_INI_SIT_RH,			" +
				" 		COD_EMP_RH,				" +
				"       DESCR_EMP_RH,           " +
				" 		COD_CCST_FUNC, 			" +
				" 		DESCR_CCST_FUNC,		" +
				" 		COD_FUNC_SUPERIOR,		" +
				" 		COD_FUN_AVALIADOR, 		" +
				" 		DT_ADMISSAO,			" +
				" 		DT_DEMISSAO,			" +
				" 		DT_INI_SIT_SRV,			" +
				" 		COD_USUARIO,			" +
				" 		DT_PROMO_ELEG,			" +
				" 		COD_MOT_DEMISSAO,		" +
				" 		QTD_DIAS_AFASTAM,		" +
				" 		COD_SIT_RH_ANT,			" +
				" 		DESCR_SIT_RH_ANT,		" +
				" 		DT_INI_SIT_RH_ANT,		" +
				" 		QTD_DIAS_TRAB_PER,		" +
				" 		FLG_CCST_COMERCIAL)			" +

				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int nroCampo = 1;
			setLong  	(stmt, nroCampo++, funcionarioVO.getIdFuncionario());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdEmpresa());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdFilial());
			setString	(stmt, nroCampo++, funcionarioVO.getIdFilialRH());
			setString 	(stmt, nroCampo++, funcionarioVO.getDescricaoFilialRH());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdCargo());
			setString 	(stmt, nroCampo++, funcionarioVO.getCracha());
			setLong   	(stmt, nroCampo++, funcionarioVO.getCpfFuncionario());
			setString 	(stmt, nroCampo++, funcionarioVO.getNomeFuncionario());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdSituacaoRH());
			setString 	(stmt, nroCampo++, funcionarioVO.getDescricaoSituacaoRH());
			setDate   	(stmt, nroCampo++, funcionarioVO.getDataSituacaoRH());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdEmpresaRH());
			setString	(stmt, nroCampo++, funcionarioVO.getDescricaoEmpresaRH());
			setString 	(stmt, nroCampo++, funcionarioVO.getIdCentroCusto());
			setString 	(stmt, nroCampo++, funcionarioVO.getDescricaoCentroCusto());
			setLong		(stmt, nroCampo++, funcionarioVO.getIdFuncionarioSuperior());
			setLong		(stmt, nroCampo++, funcionarioVO.getIdFuncionarioAvaliador());
			setDate   	(stmt, nroCampo++, funcionarioVO.getDataAdmissao());
			setDate   	(stmt, nroCampo++, funcionarioVO.getDataDemissao());
			setTimestamp(stmt, nroCampo++, funcionarioVO.getDataUltimaAlteracao());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdUsuarioUltimaAlteracao());
			setDate   	(stmt, nroCampo++, funcionarioVO.getDataPromocaoElegivel());
			setInteger	(stmt, nroCampo++, funcionarioVO.getCodigoMotivoDEmissao());
			setInteger	(stmt, nroCampo++, funcionarioVO.getQuantidadeDiasAfastamento());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdSituacaoAnterior());
			setString	(stmt, nroCampo++, funcionarioVO.getDescricaoSituacaoAnterior());
			setDate		(stmt, nroCampo++, funcionarioVO.getDataInicioSituacaoAnterior());
			setInteger	(stmt, nroCampo++, funcionarioVO.getQtdDiasTrabalhadosMes());
			setString 	(stmt, nroCampo++, funcionarioVO.getFlagIndicadorCentroCusto());
			
			
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível incluir o funcionario.", e);
		} finally {
			closeStatement(stmt);
		}
	}	
	
	
    /**
     * Altera funcionário
     * 
     * @param idFuncionario
     * @return
     * @throws PersistenciaException
     */
	public void alteraFuncionario(FuncionarioVO funcionarioVO, boolean alterarAvaliador, boolean alterarSuperior) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"UPDATE SRV_FUNCIONARIO  				" +
				"   SET COD_EMP					= ?, 	" +
				" 		COD_FIL					= ?, 	" +
				" 		COD_FIL_RH				= ?,	" +
				"       DESCR_FIL_RH			= ?,  	" +
				" 		COD_CARGO				= ?, 	" +
				" 		COD_CRACHA				= ?, 	" +
				" 		NUM_CPF_FUNC			= ?,	" +
				" 		NOME_FUNC				= ?,	" +
				" 		COD_SIT_RH				= ?, 	" +
				" 		DESCR_SIT_RH			= ?,	" +
				" 	 	DT_INI_SIT_RH			= ?,	" +
				" 		COD_EMP_RH				= ?,	" +
				"       DESCR_EMP_RH 			= ?, 	" +
				" 		COD_CCST_FUNC			= ?, 	" +
				" 		DESCR_CCST_FUNC			= ?,	");
			if (alterarSuperior) {
				query.append(" COD_FUNC_SUPERIOR = ?, ");
			}
			if (alterarAvaliador) {
				query.append(" COD_FUN_AVALIADOR = ?, ");
			}
			query.append(
				" 		DT_ADMISSAO				= ?,	" +
				" 		DT_DEMISSAO				= ?,	" +
				" 		DT_INI_SIT_SRV			= ?,	" +
				" 		COD_USUARIO 			= ?, 	" +
				" 		DT_PROMO_ELEG 			= ?,	" +
				" 		COD_MOT_DEMISSAO		= ?,	" +
				" 		QTD_DIAS_AFASTAM		= ?,	" +
				" 		COD_SIT_RH_ANT			= ?,	" +
				" 		DESCR_SIT_RH_ANT		= ?,	" +
				" 		DT_INI_SIT_RH_ANT		= ?,	" +
				" 		QTD_DIAS_TRAB_PER		= ?,	" +
				" 		FLG_CCST_COMERCIAL		= ?		" +
				" WHERE COD_FUNC				= ?		");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int nroCampo = 1;
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdEmpresa());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdFilial());
			setString	(stmt, nroCampo++, funcionarioVO.getIdFilialRH());
			setString 	(stmt, nroCampo++, funcionarioVO.getDescricaoFilialRH());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdCargo());
			setString 	(stmt, nroCampo++, funcionarioVO.getCracha());
			setLong   	(stmt, nroCampo++, funcionarioVO.getCpfFuncionario());
			setString 	(stmt, nroCampo++, funcionarioVO.getNomeFuncionario());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdSituacaoRH());
			setString 	(stmt, nroCampo++, funcionarioVO.getDescricaoSituacaoRH());
			setDate   	(stmt, nroCampo++, funcionarioVO.getDataSituacaoRH());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdEmpresaRH());
			setString	(stmt, nroCampo++, funcionarioVO.getDescricaoEmpresaRH());
			setString 	(stmt, nroCampo++, funcionarioVO.getIdCentroCusto());
			setString 	(stmt, nroCampo++, funcionarioVO.getDescricaoCentroCusto());
			if (alterarSuperior) {
				setLong	(stmt, nroCampo++, funcionarioVO.getIdFuncionarioSuperior());
			}
			if (alterarAvaliador) {
				setLong	(stmt, nroCampo++, funcionarioVO.getIdFuncionarioAvaliador());
			}
			setDate   	(stmt, nroCampo++, funcionarioVO.getDataAdmissao());
			setDate   	(stmt, nroCampo++, funcionarioVO.getDataDemissao());
			setTimestamp(stmt, nroCampo++, funcionarioVO.getDataUltimaAlteracao());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdUsuarioUltimaAlteracao());
			setDate   	(stmt, nroCampo++, funcionarioVO.getDataPromocaoElegivel());
			setInteger	(stmt, nroCampo++, funcionarioVO.getCodigoMotivoDEmissao());
			setInteger	(stmt, nroCampo++, funcionarioVO.getQuantidadeDiasAfastamento());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdSituacaoAnterior());
			setString	(stmt, nroCampo++, funcionarioVO.getDescricaoSituacaoAnterior());
			setDate		(stmt, nroCampo++, funcionarioVO.getDataInicioSituacaoAnterior());
			setInteger	(stmt, nroCampo++, funcionarioVO.getQtdDiasTrabalhadosMes());
			setString 	(stmt, nroCampo++, funcionarioVO.getFlagIndicadorCentroCusto());
			setLong		(stmt, nroCampo++, funcionarioVO.getIdFuncionario());
			
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível incluir o funcionario.", e);
		} finally {
			closeStatement(stmt);
		}
	}	
	
	
    /**
     * Exclui funcionário
     * 
     * @param idFuncionario
     * @return
     * @throws PersistenciaException
     */
	public void excluiFuncionario(Long idFuncionario) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"DELETE FROM SRV_FUNCIONARIO  	" +
				" WHERE COD_FUNC = ?			");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int nroCampo = 1;
			setLong(stmt, nroCampo++, idFuncionario);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível excluir o funcionario.", e);
		} finally {
			closeStatement(stmt);
		}
	}		
	

    /**
     * Inclui histórico de funcionário
     * 
     * @param funcionarioVO
     * @return
     * @throws PersistenciaException
     */
	public void incluiFuncionarioHistorico(FuncionarioVO funcionarioVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"INSERT INTO SRV_FUNCIONARIO_HIST ( " +
				"       COD_FUNC, 					" +
				"       FUNC_SEQ, 					" +
				" 		COD_EMP, 					" +
				" 		COD_FIL, 					" +
				" 		COD_FIL_RH,					" +
				"       DESCR_FIL_RH,  				" +
				" 		COD_CARGO, 					" +
				" 		COD_CRACHA, 				" +
				" 		NUM_CPF_FUNC,				" +
				" 		NOME_FUNC,					" +
				" 		COD_SIT_RH, 				" +
				" 		DESCR_SIT_RH,				" +
				" 	 	DT_INI_SIT_RH,				" +
				" 		COD_EMP_RH,					" +
				" 		DESCR_EMP_RH,				" +
				" 		COD_CCST_FUNC, 				" +
				" 		DESCR_CCST_FUNC,			" +
				" 		COD_FUNC_SUPERIOR,			" +
				" 		COD_FUN_AVALIADOR, 			" +
				" 		DT_ADMISSAO,				" +
				" 		DT_DEMISSAO,				" +
				" 		DT_INI_SIT_SRV,				" +
				" 		DT_PROMO_ELEG,				" +
				" 		COD_MOT_DEMISSAO,			" +
				" 		QTD_DIAS_AFASTAM,			" +
				" 		FLG_CCST_COMERCIAL,			" +
				" 		COD_SIT_RH_ANT,				" +
				" 		DESCR_SIT_RH_ANT,			" +
				" 		DT_INI_SIT_RH_ANT,			" +
				" 		QTD_DIAS_TRAB_PER,			" +
				" 		COD_USUARIO)				" +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
		

		
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int nroCampo = 1;
			setLong		(stmt, nroCampo++, funcionarioVO.getIdFuncionario());
			setInteger	(stmt, nroCampo++, obtemProximaSequenciaHistorio(funcionarioVO.getIdFuncionario().longValue()));
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdEmpresa());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdFilial());
			setString	(stmt, nroCampo++, funcionarioVO.getIdFilialRH());
			setString 	(stmt, nroCampo++, funcionarioVO.getDescricaoFilialRH());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdCargo());
			setString 	(stmt, nroCampo++, funcionarioVO.getCracha());
			setLong   	(stmt, nroCampo++, funcionarioVO.getCpfFuncionario());
			setString 	(stmt, nroCampo++, funcionarioVO.getNomeFuncionario());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdSituacaoRH());
			setString 	(stmt, nroCampo++, funcionarioVO.getDescricaoSituacaoRH());
			setDate   	(stmt, nroCampo++, funcionarioVO.getDataSituacaoRH());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdEmpresaRH());
			setString	(stmt, nroCampo++, funcionarioVO.getDescricaoEmpresaRH());
			setString 	(stmt, nroCampo++, funcionarioVO.getIdCentroCusto());
			setString 	(stmt, nroCampo++, funcionarioVO.getDescricaoCentroCusto());
			setLong		(stmt, nroCampo++, funcionarioVO.getIdFuncionarioSuperior());
			setLong		(stmt, nroCampo++, funcionarioVO.getIdFuncionarioAvaliador());
			setDate   	(stmt, nroCampo++, funcionarioVO.getDataAdmissao());
			setDate   	(stmt, nroCampo++, funcionarioVO.getDataDemissao());
			setTimestamp(stmt, nroCampo++, funcionarioVO.getDataUltimaAlteracao());
			setDate   	(stmt, nroCampo++, funcionarioVO.getDataPromocaoElegivel());
			setInteger	(stmt, nroCampo++, funcionarioVO.getCodigoMotivoDEmissao());
			setInteger	(stmt, nroCampo++, funcionarioVO.getQuantidadeDiasAfastamento());
			setString 	(stmt, nroCampo++, funcionarioVO.getFlagIndicadorCentroCusto());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdSituacaoAnterior());
			setString	(stmt, nroCampo++, funcionarioVO.getDescricaoSituacaoAnterior());
			setDate		(stmt, nroCampo++, funcionarioVO.getDataInicioSituacaoAnterior());
			setInteger	(stmt, nroCampo++, funcionarioVO.getQtdDiasTrabalhadosMes());
			setInteger	(stmt, nroCampo++, funcionarioVO.getIdUsuarioUltimaAlteracao());
			
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível incluir o histórico funcionario.", e);
		} finally {
			closeStatement(stmt);
		}
	}
	
	
    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idFuncionario
     * @return
     */
    private int obtemProximaSequenciaHistorio(long idFuncionario) throws PersistenciaException {

        String query =  "SELECT MAX(FUNC_SEQ)     		" +
                        "  FROM SRV_FUNCIONARIO_HIST    " +
                        " WHERE COD_FUNC =?       		";
            
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int proximaSequencia = 1;
        
        try {
            conn = getConn();
            stmt = conn.prepareStatement(query);
            int ordemCampos = 1;
            setLong(stmt, ordemCampos++, idFuncionario);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	proximaSequencia = getInt(rs, 1) + 1;
            }

        } catch (Exception e) {
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico do funcionario " + idFuncionario, e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }

	public List geraRelatorioFuncionarios() throws PersistenciaException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List list = null;

		String query = 
		" select a.cod_fil                                   ,a.cod_func           as id " +
		"                 ,a.num_cpf_func       as cpf                 ,a.cod_cargo      " +
		"                           ,b.descr_cargo                               ,a.nome_" +
		"func                                 ,a.cod_sit_rh                              " +
		"  ,a.descr_sit_rh                              ,a.qtd_dias_trab_per             " +
		"            ,a.cod_sit_rh_ant     as cod_sit_ant         ,a.descr_sit_rh_ant   a" +
		"s sit_ant             ,a.dt_ini_sit_rh_ant  as dt_sit_ant          ,a.dt_admissa" +
		"o                               ,a.dt_demissao                          from srv" +
		"_funcionario a                            ,srv_cargo      b                     " +
		"  where a.cod_cargo = b.cod_cargo              order by b.descr_cargo           ";

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
			list = Tools.montaRelatorio(rs, true, true);
		} catch(Exception e) {
			throw new PersistenciaException(log, "Não foi possível gerar relatorio excel de funcionarios");
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return list;
	}

}