package br.com.marisa.srv.resultado.loja.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.resultado.loja.vo.ResultadoVO;

/**
 * 
 * @author levy.villar
 *
 */
public class ResultadoLojaDAO extends BasicDAO {

	/**
	 * 
	 */
	private static final Logger log = Logger.getLogger(ResultadoLojaDAO.class);

	/**
	 * 
	 * @param numAno
	 * @param numMes
	 * @param idLoja
	 * @return
	 * @throws PersistenciaException
	 */
	public List<ResultadoVO> obterResultadoPorLoja(Integer numAno, Integer numMes, Integer idLoja) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT A.COD_INDIC, " +
				"        B.DESCR_INDIC, " +
				"        A.COD_FIL, " +
				"        A.NUM_ANO, " +
				"        A.NUM_MES, " +
				"        A.NUM_META, " +
				"        A.COD_UN_META, " +
				"        A.NUM_REALZ, " +
				"        A.COD_UN_REALZ, " +
				"        A.QTD_REALZ, " +
				"        CASE " +
				"          WHEN (A.COD_INDIC = 29 AND NVL(A.NUM_REALZ_X_META, 0) > 100) THEN " +
				"           100 " +
				"          WHEN (A.COD_INDIC <> 29 AND NVL(A.NUM_REALZ_X_META, 0) > 120) THEN " +
				"           120 " +
				"          ELSE " +
				"           A.NUM_REALZ_X_META " +
				"        END AS NUM_REALZ_X_META, " +
				"        A.COD_UN_REALZ_X_META, " +
				"        A.VLR_PREMIO_FIL_CALC, " +
				"        A.COD_UN_PREMIO_FIL_CALC, " +
				"        A.NUM_REALZ_FX, " +
				"        A.COD_UN_REALZ_FX " +
				"   FROM SRV_REALIZADO_FILIAL A " +
				"   LEFT JOIN SRV_INDICADOR B " +
				"     ON B.COD_INDIC = A.COD_INDIC " +
				"  WHERE A.COD_INDIC NOT IN " +
				"        (4, 5, 6, 7, 9, 12, 13, 15, 16, 17, 18, 25, 54, 528) " +
				"    AND A.NUM_ANO = ? " +
				"    AND A.NUM_MES = ? " +
				"    AND A.COD_FIL = ? " +
				"  ORDER BY A.COD_FIL, A.COD_INDIC ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<ResultadoVO> lista = new ArrayList<ResultadoVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setInteger(stmt, i++, numAno);
			setInteger(stmt, i++, numMes);
			setInteger(stmt, i++, idLoja);

			rs = stmt.executeQuery();

			while (rs.next()) {
				ResultadoVO vo = new ResultadoVO();
				vo.setNumAno(numAno);
				vo.setNumMes(numMes);
				vo.setIdFilial(idLoja);
				vo.setCodIndic(getInteger(rs, "COD_INDIC"));
				vo.setDescIndic(getString(rs, "DESCR_INDIC"));
				vo.setNumMeta(getDouble(rs, "NUM_META"));
				vo.setCodUniMeta(getInteger(rs, "COD_UN_META"));
				vo.setNumRealz(getDouble(rs, "NUM_REALZ"));
				vo.setCodUniRealz(getInteger(rs, "COD_UN_REALZ"));
				vo.setQtdRealz(getInteger(rs, "QTD_REALZ"));
				vo.setNumRealzXMeta(getDouble(rs, "NUM_REALZ_X_META"));
				vo.setCodUniRealzXMeta(getInteger(rs, "COD_UN_REALZ_X_META"));
				lista.add(vo);
			}

		} catch (Exception ex) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de premiacao por loja: " + idLoja, ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;
	}

	/**
	 * 
	 * @param numAno
	 * @param numMes
	 * @param idLoja
	 * @param idFuncionario
	 * @return
	 * @throws PersistenciaException
	 */
	public List<ResultadoVO> obterResultadoPorOperacional(Integer numAno, Integer numMes, Integer idLoja, Long idFuncionario) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT RFI.NUM_ANO AS NUM_ANO, " +
				"        RFI.NUM_MES AS NUM_MES, " +
				"        RFI.COD_FIL AS COD_FIL, " +
				"        RFI.COD_FUNC AS COD_FUNC, " +
				"        F.NOME_FUNC AS NOME_FUNC, " +
				"        C.COD_CARGO AS COD_CARGO, " +
				"        C.DESCR_CARGO AS DESCR_CARGO, " +
				"        RFI.COD_INDIC AS COD_INDIC, " +
				"        I.DESCR_INDIC AS DESCR_INDIC, " +
				"        RFI.NUM_META AS NUM_META, " +
				"        RFI.COD_UN_META AS COD_UN_META, " +
				"        RFI.NUM_REALZ AS NUM_REALZ_FUNC, " +
				"        RFI.COD_UN_REALZ AS COD_UN_REALZ_FUNC, " +
				"        RF.NUM_REALZ AS NUM_REALZ_FIL, " +
				"        RF.COD_UN_REALZ AS COD_UN_REALZ_FIL, " +
				"        CASE " +
				"          WHEN (RFI.COD_INDIC = 29 AND NVL(RFI.NUM_REALZ_X_META, 0) > 100) THEN " +
				"           100 " +
				"          WHEN (RFI.COD_INDIC <> 29 AND NVL(RFI.NUM_REALZ_X_META, 0) > 120) THEN " +
				"           120 " +
				"          ELSE " +
				"           RFI.NUM_REALZ_X_META " +
				"        END AS NUM_REALZ_X_META, " +
				"        RFI.COD_UN_REALZ_X_META AS COD_UN_REALZ_X_META " +
				"   FROM SRV_REALIZADO_FUNC_INDICADOR RFI " +
				"  INNER JOIN SRV_INDICADOR I " +
				"     ON I.COD_INDIC = RFI.COD_INDIC " +
				"  INNER JOIN SRV_GRUPO_INDICADOR GI " +
				"     ON GI.COD_GRP_INDIC = I.COD_GRP_INDIC " +
				"  INNER JOIN SRV_GRP_INDIC_GRP_REM_VAR GIGRV " +
				"     ON GIGRV.COD_GRP_INDIC = GI.COD_GRP_INDIC " +
				"  INNER JOIN SRV_GRUPO_REM_VARIAVEL GRV " +
				"     ON GRV.COD_GRP_REM_VAR = GIGRV.COD_GRP_REM_VAR " +
				"  INNER JOIN SRV_TIPO_REM_VAR TRV " +
				"     ON TRV.COD_TIPO_REM_VAR = GI.COD_TIPO_REM_VAR " +
				"  INNER JOIN SRV_FUNCIONARIO F " +
				"     ON F.COD_FUNC = RFI.COD_FUNC " +
				"  INNER JOIN SRV_CARGO C " +
				"     ON C.COD_CARGO = F.COD_CARGO " +
				"  INNER JOIN SRV_GRUPO_CARGO GC " +
				"     ON GC.COD_CARGO = RFI.COD_CARGO " +
				"    AND GC.COD_GRP_REM_VAR = GRV.COD_GRP_REM_VAR " +
				"   LEFT JOIN SRV_REALIZADO_FILIAL RF " +
				"     ON RF.NUM_ANO = RFI.NUM_ANO " +
				"    AND RF.NUM_MES = RFI.NUM_MES " +
				"    AND RF.COD_INDIC = RFI.COD_INDIC " +
				"    AND RF.COD_EMP = RFI.COD_EMP " +
				"    AND RF.COD_FIL = RFI.COD_FIL " +
				"  WHERE TRV.COD_TIPO_REM_VAR = ? " +
				"    AND GRV.COD_GRP_REM_VAR = ? " +
				"    AND RFI.COD_INDIC NOT IN (4, 5, 6, 7, 9, 12, 13, 15, 16, 17, 18, 25, 54, 528) " +
				"    AND RFI.NUM_ANO = ? " +
				"    AND RFI.NUM_MES = ? " +
				"    AND RFI.COD_FIL = ? ");
		if (ObjectHelper.isNotEmpty(idFuncionario)) {
			query.append("    AND RFI.COD_FUNC = ? ");
		}
		query.append("  ORDER BY RFI.COD_FIL, RFI.COD_FUNC, RFI.COD_INDIC ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<ResultadoVO> lista = new ArrayList<ResultadoVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setInteger(stmt, i++, Constantes.ID_TIPO_REM_VAR_REMUNERACAO_LOJA);
			setInteger(stmt, i++, Constantes.COD_GRP_REM_VAR_OPERACIONAL_LOJA);
			setInteger(stmt, i++, numAno);
			setInteger(stmt, i++, numMes);
			setInteger(stmt, i++, idLoja);
			if (ObjectHelper.isNotEmpty(idFuncionario)) {
				setLong(stmt, i++, idFuncionario);
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				ResultadoVO vo = new ResultadoVO();
				vo.setNumAno(getInteger(rs, "NUM_ANO"));
				vo.setNumMes(getInteger(rs, "NUM_MES"));
				vo.setIdFilial(getInteger(rs, "COD_FIL"));
				vo.setCodFunc(getLong(rs, "COD_FUNC"));
				vo.setNomeFunc(getString(rs, "NOME_FUNC"));
				vo.setCodCargo(getInteger(rs, "COD_CARGO"));
				vo.setDescrCargo(getString(rs, "DESCR_CARGO"));
				vo.setCodIndic(getInteger(rs, "COD_INDIC"));
				vo.setDescIndic(getString(rs, "DESCR_INDIC"));
				vo.setNumMeta(getDouble(rs, "NUM_META"));
				vo.setCodUniMeta(getInteger(rs, "COD_UN_META"));
				vo.setNumRealz(getDouble(rs, "NUM_REALZ_FUNC"));
				vo.setCodUniRealz(getInteger(rs, "COD_UN_REALZ_FUNC"));
				vo.setNumRealzFil(getDouble(rs, "NUM_REALZ_FIL"));
				vo.setCodUniRealzFil(getInteger(rs, "COD_UN_REALZ_FIL"));
				vo.setNumRealzXMeta(getDouble(rs, "NUM_REALZ_X_META"));
				vo.setCodUniRealzXMeta(getInteger(rs, "COD_UN_REALZ_X_META"));
				lista.add(vo);
			}

		} catch (Exception ex) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de premiacao por operacional: " + idLoja, ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;
	}

	/**
	 * 
	 * @param numAno
	 * @param numMes
	 * @param idLoja
	 * @param idFuncionario
	 * @return
	 * @throws PersistenciaException
	 */
	public List<ResultadoVO> obterResultadoPorLider(Integer numAno, Integer numMes, Integer idLoja, Long idFuncionario) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT RFI.COD_FIL, " +
				"        RFI.COD_FUNC, " +
				"        F.NOME_FUNC, " +
				"        C.COD_CARGO, " +
				"        C.DESCR_CARGO, " +
				"        RFI.COD_INDIC, " +
				"        I.DESCR_INDIC, " +
				"        RFI.NUM_META, " +
				"        RFI.COD_UN_META, " +
				"        RFI.NUM_REALZ, " +
				"        RFI.COD_UN_REALZ, " +
				"        RFI.NUM_REALZ_X_META, " +
				"        RFI.COD_UN_REALZ_X_META, " +
				"        RFI.NUM_PESO, " +
				"        RFI.NUM_REALZ_POND, " +
				"        RFI.COD_UN_REALZ_POND, " +
				"        RFI.VLR_PREMIO, " +
				"        RFI.VLR_PREMIO_FUNC_CALC, " +
				"        RFI.QTD_DESC_INDICACAO " +
				"   FROM SRV_REALIZADO_FUNC_INDICADOR RFI " +
				"  INNER JOIN SRV_INDICADOR I " +
				"     ON I.COD_INDIC = RFI.COD_INDIC " +
				"  INNER JOIN SRV_GRUPO_INDICADOR GI " +
				"     ON GI.COD_GRP_INDIC = I.COD_GRP_INDIC " +
				"  INNER JOIN SRV_TIPO_REM_VAR TRV " +
				"     ON TRV.COD_TIPO_REM_VAR = GI.COD_TIPO_REM_VAR " +
				"  INNER JOIN SRV_GRP_INDIC_GRP_REM_VAR GIGRV " +
				"     ON GIGRV.COD_GRP_INDIC = GI.COD_GRP_INDIC " +
				"  INNER JOIN SRV_GRUPO_REM_VARIAVEL GRV " +
				"     ON GRV.COD_GRP_REM_VAR = GIGRV.COD_GRP_REM_VAR " +
				"  INNER JOIN SRV_FUNCIONARIO F " +
				"     ON F.COD_FUNC = RFI.COD_FUNC " +
				"  INNER JOIN SRV_CARGO C " +
				"     ON C.COD_CARGO = F.COD_CARGO " +
				"  INNER JOIN SRV_GRUPO_CARGO GC " +
				"     ON GC.COD_CARGO = RFI.COD_CARGO " +
				"    AND GC.COD_GRP_REM_VAR = GRV.COD_GRP_REM_VAR " +
				"  WHERE (RFI.NUM_PESO > 0 OR RFI.COD_INDIC IN (1, 524, 525, 526, 527)) " +
				"    AND RFI.COD_INDIC NOT IN (39, 40, 528) " +
				"    AND NVL(C.FLG_AGRUPA_FIL_LIDER, 'N') = 'N' " +
				"    AND TRV.COD_TIPO_REM_VAR = ? " +
				"    AND GRV.COD_GRP_REM_VAR = ? " +
				"    AND RFI.NUM_ANO = ? " +
				"    AND RFI.NUM_MES = ? " +
				"    AND RFI.COD_FIL = ? ");
		if (ObjectHelper.isNotEmpty(idFuncionario)) {
			query.append("    AND RFI.COD_FUNC = ? ");
		}
		query.append("  ORDER BY RFI.COD_FIL, RFI.COD_FUNC, RFI.COD_INDIC ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<ResultadoVO> lista = new ArrayList<ResultadoVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setInteger(stmt, i++, Constantes.ID_TIPO_REM_VAR_REMUNERACAO_LOJA);
			setInteger(stmt, i++, Constantes.COD_GRP_REM_VAR_LIDERANCA_LOJA);
			setInteger(stmt, i++, numAno);
			setInteger(stmt, i++, numMes);
			setInteger(stmt, i++, idLoja);
			if (ObjectHelper.isNotEmpty(idFuncionario)) {
				setLong(stmt, i++, idFuncionario);
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				ResultadoVO vo = new ResultadoVO();
				vo.setNumAno(numAno);
				vo.setNumMes(numMes);
				vo.setIdFilial(idLoja);
				vo.setCodIndic(getInteger(rs, "COD_INDIC"));
				vo.setDescIndic(getString(rs, "DESCR_INDIC"));
				vo.setNumMeta(getDouble(rs, "NUM_META"));
				vo.setCodUniMeta(getInteger(rs, "COD_UN_META"));
				vo.setNumRealz(getDouble(rs, "NUM_REALZ"));
				vo.setCodUniRealz(getInteger(rs, "COD_UN_REALZ"));
				vo.setNumRealzXMeta(getDouble(rs, "NUM_REALZ_X_META"));
				vo.setCodUniRealzXMeta(getInteger(rs, "COD_UN_REALZ_X_META"));
				vo.setCodFunc(getLong(rs, "COD_FUNC"));
				vo.setNomeFunc(getString(rs, "NOME_FUNC"));
				vo.setCodCargo(getInteger(rs, "COD_CARGO"));
				vo.setDescrCargo(getString(rs, "DESCR_CARGO"));
				lista.add(vo);
			}

		} catch (Exception ex) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de premiacao por operacional: " + idLoja, ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;
	}

}