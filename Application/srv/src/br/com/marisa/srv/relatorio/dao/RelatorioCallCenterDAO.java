package br.com.marisa.srv.relatorio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.ApresentacaoException;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.util.tools.Tools;

/**
 * 
 * @author levy.villar
 *
 */
@Deprecated
public class RelatorioCallCenterDAO extends BasicDAO {

	private static final Logger log = Logger.getLogger(RelatorioCallCenterDAO.class);

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 * @throws SQLException
	 */
	public List<Map<Object, Object>> obtemRelatorioPremiacaoCallCenterLideranca(String ano, String mes) throws PersistenciaException, SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		StringBuffer query = new StringBuffer(
						" SELECT A.COD_FUNC AS COD_FUNC, " +
						"        B.NUM_CPF_FUNC AS NUM_CPF_FUNC, " +
						"        B.NOME_FUNC AS NOME_FUNC, " +
						"        A.NUM_ANO AS NUM_ANO, " +
						"        A.NUM_MES AS NUM_MES, " +
						"        C.COD_CARGO AS COD_CARGO, " +
						"        C.DESCR_CARGO AS DESCR_CARGO, " +
						"        A.COD_INDIC AS COD_INDIC, " +
						"        D.DESCR_INDIC AS DESCR_INDIC, " +
						"        A.NUM_META AS NUM_META, " +
						"        A.NUM_REALZ AS NUM_REALZ, " +
						"        NUM_PESO AS NUM_PESO, " +
						"        NUM_REALZ_POND AS NUM_REALZ_POND, " +
						"        CASE " +
						"          WHEN A.COD_INDIC = 43 THEN " +
						"           A.VLR_PREMIO " +
						"        END AS VLR_PREMIO, " +
						"        A.VLR_PREMIO_FUNC_CALC AS VLR_PREMIO_FUNC_CALC " +
						"   FROM SRV_REALIZADO_FUNC_INDICADOR A " +
						"  INNER JOIN SRV_FUNCIONARIO B ON A.COD_FUNC = B.COD_FUNC " +
						"  INNER JOIN SRV_CARGO C ON B.COD_CARGO = C.COD_CARGO " +
						"  INNER JOIN SRV_INDICADOR D ON D.COD_INDIC = A.COD_INDIC " +
						"  WHERE A.NUM_ANO = ? " +
						"    AND A.NUM_MES = ? " +
						"    AND A.COD_INDIC IN (18, 25, 43) " +
						"    AND A.COD_CARGO NOT IN (943, 944, 945, 141, 949, 950, 951, 1002, 1009, 1022, 1071) " +
						"  ORDER BY A.COD_FUNC, A.COD_INDIC ");

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			setString(stmt, 1, ano);
			setString(stmt, 2, mes);

			rs = stmt.executeQuery();

			return Tools.montaRelatorio(rs, true, true);

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório Premiacao Call Center Lideranca.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 * @throws SQLException
	 */
	public List<Map<Object, Object>> obtemRelatorioPremiacaoCallCenterOperacional(String ano, String mes) throws PersistenciaException, SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		StringBuffer query = new StringBuffer(
							" SELECT A.COD_FUNC AS COD_FUNC, " +
							"        B.NUM_CPF_FUNC AS NUM_CPF_FUNC, " +
							"        B.NOME_FUNC AS NOME_FUNC, " +
							"        A.NUM_ANO AS NUM_ANO, " +
							"        A.NUM_MES AS NUM_MES, " +
							"        C.COD_CARGO AS COD_CARGO, " +
							"        C.DESCR_CARGO AS DESCR_CARGO, " +
							"        CASE " +
							"          WHEN B.COD_CARGO IN (943, 944, 945, 141) THEN " +
							"           UPPER('SAC RECEPTIVO') " +
							"          WHEN B.COD_CARGO IN (949, 950, 951, 1002, 1009, 1022, 1071) THEN " +
							"           UPPER('SAC ATIVO') " +
							"        END AS DESCR_DOMI_CARG, " +
							"        A.COD_INDIC AS COD_INDIC, " +
							"        D.DESCR_INDIC AS DESCR_INDIC, " +
							"        TRUNC(A.NUM_REALZ) AS NUM_REALZ, " +
							"        A.NUM_REALZ_POND AS VRL_PREMIO, " +
							"        NVL(A.VLR_PREMIO_FUNC_CALC, 0) AS VLR_PREMIO_CALCULADO " +
							"   FROM SRV_REALIZADO_FUNC_INDICADOR A " +
							"  INNER JOIN SRV_FUNCIONARIO B ON A.COD_FUNC = B.COD_FUNC " +
							"  INNER JOIN SRV_CARGO C ON B.COD_CARGO = C.COD_CARGO " +
							"  INNER JOIN SRV_INDICADOR D ON D.COD_INDIC = A.COD_INDIC " +
							"  WHERE A.NUM_ANO = ? " +
							"    AND A.NUM_MES = ? " +
							"    AND A.COD_INDIC IN (18, 25) " +
							"    AND C.COD_CARGO IN (943, 944, 945, 141, 949, 950, 951, 1002, 1009, 1022, 1071) " +
							"  ORDER BY DESCR_DOMI_CARG DESC, A.COD_FUNC, A.COD_INDIC ");

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setString(stmt, i++, ano);
			setString(stmt, i++, mes);

			rs = stmt.executeQuery();

			return Tools.montaRelatorio(rs, true, true);

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório Premiacao Call Center Operacional.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 * @throws SQLException
	 */
	public List<Map<Object, Object>> obtemRelatorioPremiacaoCallCenterTrimestral(String ano, String mes) throws PersistenciaException, SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		StringBuffer query = new StringBuffer(
						" SELECT COD_FUNC, " +
						"        NUM_CPF_FUNC, " +
						"        NOME_FUNC, " +
						"        COD_CARGO, " +
						"        CARGO_DESCR, " +
						"        DESCR_SIT_RH, " +
						"        DT_DEMISSAO, " +
						"        NUM_ANO, " +
						"        NUM_MES, " +
						"        DESCR_PERIODO_VENDA, " +
						"        DT_PRM_VENDA, " +
						"        QTD_DIAS_FERIAS_ABONADO, " +
						"        DESCR_PERIODO_FERIAS, " +
						"        QTD_DIAS_TRABALHADOS, " +
						"        META_PROPORCIONAL, " +
						"        NUM_REALZ, " +
						"        QTD_PONTOS, " +
						"        DT_INI_SIT_SRV " +
						"   FROM SRV_CALC_TRIMESTRAL_CCENTER " +
						"  WHERE NUM_ANO = ? " +
						"    AND NUM_MES = ? ");

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setString(stmt, i++, ano);
			setString(stmt, i++, mes);

			rs = stmt.executeQuery();

			return Tools.montaRelatorio(rs, true, true);

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter relatório Premiacao Call Center Trimestral.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	public List<Map<Object, Object>> obtemRelatorioCallCenterRealizado(String ano, String mes, Date inicio, Date fim) throws PersistenciaException, ApresentacaoException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String TABELA_ANO_MES = ano+mes;

		StringBuffer query = new StringBuffer(
				" SELECT F.NUM_CPF_FUNC CPF_VENDEDOR, " +
				"        F.COD_FIL_RH FILIAL_RH, " +
				"        F.COD_FUNC MATRICULA, " +
				"        F.NOME_FUNC NOME_VENDEDOR, " +
				"        C.DESCR_CARGO CARGO, " +
				"        F.DT_ADMISSAO, " +
				"        F.DT_DEMISSAO, " +
				"        F.DESCR_SIT_RH STATUS_FUNCIONARIO, " +
				"        CPF.DT_VENDA, " +
				"        A.COD_INDIC, " +
				"        A.DESCR_INDIC, " +
				"        NVL(A.QT_VENDAS, 0) QT_VENDAS " +
				"   FROM (SELECT CPF_VENDEDOR, TRUNC(DATA_CADASTRO) DT_VENDA " +
				"           FROM SRV_REALZFU_CT_AP_CC_" + TABELA_ANO_MES +
				"          GROUP BY CPF_VENDEDOR, TRUNC(DATA_CADASTRO) " +
				"          ORDER BY 1, 2 " +
				"         ) CPF, " +
				"        SRV_FUNCIONARIO F, " +
				"        SRV_CARGO C, " +
				"        (SELECT CPF_VENDEDOR, " +
				"                COD_INDIC, " +
				"                DESCR_INDIC, " +
				"                TRUNC(DATA_CADASTRO) DATA_CADASTRO, " +
				"                COUNT(1) QT_VENDAS " +
				"           FROM SRV_REALZFU_CT_AP_CC_" + TABELA_ANO_MES +
				"          GROUP BY CPF_VENDEDOR, COD_INDIC, DESCR_INDIC, TRUNC(DATA_CADASTRO)) A " +
				"  WHERE CPF.CPF_VENDEDOR = F.NUM_CPF_FUNC " +
				"    AND CPF.CPF_VENDEDOR = A.CPF_VENDEDOR(+) " +
				"    AND CPF.DT_VENDA = A.DATA_CADASTRO(+) " +
				"    AND F.COD_CARGO = C.COD_CARGO ");
		if (inicio != null && fim != null) {
			query.append("    AND CPF.DT_VENDA BETWEEN ? AND ? ");
		}
		query.append(" UNION " +
				" SELECT F.NUM_CPF_FUNC CPF_VENDEDOR, " +
				"        F.COD_FIL_RH FILIAL_RH, " +
				"        F.COD_FUNC MATRICULA, " +
				"        F.NOME_FUNC NOME_VENDEDOR, " +
				"        C.DESCR_CARGO CARGO, " +
				"        F.DT_ADMISSAO, " +
				"        F.DT_DEMISSAO, " +
				"        F.DESCR_SIT_RH STATUS_FUNCIONARIO, " +
				"        CPF.DT_VENDA, " +
				"        B.COD_INDIC, " +
				"        B.DESCR_INDIC, " +
				"        NVL(B.QT_SEGUROS, 0) QT_SEGUROS " +
				"   FROM (SELECT CPF_VENDEDOR, TRUNC(DATA_ADESAO_TITULAR) DT_VENDA " +
				"           FROM SRV_REALZFU_B_PROT_CC_" + TABELA_ANO_MES +
				"          GROUP BY CPF_VENDEDOR, TRUNC(DATA_ADESAO_TITULAR) " +
				"          ORDER BY 1, 2 " +
				"         ) CPF, " +
				"        SRV_FUNCIONARIO F, " +
				"        SRV_CARGO C, " +
				"        (SELECT CPF_VENDEDOR, " +
				"                COD_INDIC, " +
				"                DESCR_INDIC, " +
				"                TRUNC(DATA_ADESAO_TITULAR) DATA_ADESAO_TITULAR, " +
				"                COUNT(1) QT_SEGUROS " +
				"           FROM SRV_REALZFU_B_PROT_CC_" + TABELA_ANO_MES +
				"          GROUP BY CPF_VENDEDOR, COD_INDIC, DESCR_INDIC, TRUNC(DATA_ADESAO_TITULAR)) B " +
				"  WHERE CPF.CPF_VENDEDOR = F.NUM_CPF_FUNC " +
				"    AND CPF.CPF_VENDEDOR = B.CPF_VENDEDOR(+) " +
				"    AND CPF.DT_VENDA = B.DATA_ADESAO_TITULAR(+) " +
				"    AND F.COD_CARGO = C.COD_CARGO ");
		if (inicio != null && fim != null) {
			query.append("    AND CPF.DT_VENDA BETWEEN ? AND ? ");
		}

		query.append("  ORDER BY 1, 9 ");

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			if (inicio != null && fim != null) {
				setDate(stmt, i++, inicio);
				setDate(stmt, i++, fim);
				setDate(stmt, i++, inicio);
				setDate(stmt, i++, fim);
			}

			rs = stmt.executeQuery();

			return Tools.montaRelatorio(rs, true, true);

		} catch (Exception e) {
			if (e.getMessage().toUpperCase().indexOf("ORA-00942") > -1) {
				throw new ApresentacaoException(log, "Dados indisponíveis para o período selecionado.", e);
			} else {
				throw new PersistenciaException(log, "Não foi possível obter relatório Call Center (Realizado).", e);
			}
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

}