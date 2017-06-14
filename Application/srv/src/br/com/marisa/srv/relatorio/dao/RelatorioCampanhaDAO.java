package br.com.marisa.srv.relatorio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.marisa.srv.campanha.vo.CampanhaVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.util.tools.Tools;

/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioCampanhaDAO extends BasicDAO {

	private static final Logger log = Logger.getLogger(RelatorioCampanhaDAO.class);

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param dataInicio
	 * @param dataFim
	 * @param tabelaFuncionario
	 * @return
	 * @throws PersistenciaException
	 * @throws SQLException
	 */
	public List<Map<Object, Object>> obtemRelatorioCampanha(String ano, String mes, Date dataInicio, Date dataFim, String tabelaFuncionario) throws PersistenciaException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String TABELA_ANO_MES = tabelaFuncionario + "_" + ano + mes;

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
				"        B.COD_INDIC, " +
				"        B.DESCR_INDIC, " +
				"        NVL(B.QT_SEGUROS, 0) QT_SEGUROS, " +
				"        CARGO.NOME_FUNC COORDENADOR " +
				"   FROM (SELECT CPF_VENDEDOR, TRUNC(DATA_ADESAO_TITULAR) DT_VENDA " +
				"           FROM " + TABELA_ANO_MES +
				"          GROUP BY CPF_VENDEDOR, TRUNC(DATA_ADESAO_TITULAR) " +
				"          ORDER BY 1, 2) CPF, " +
				"        SRV_FUNCIONARIO F, " +
				"        SRV_CARGO C, " +
				"        (SELECT CPF_VENDEDOR, " +
				"                COD_INDIC, " +
				"                DESCR_INDIC, " +
				"                TRUNC(DATA_ADESAO_TITULAR) DATA_ADESAO_TITULAR, " +
				"                COUNT(1) QT_SEGUROS " +
				"           FROM " + TABELA_ANO_MES +
				"          GROUP BY CPF_VENDEDOR, " +
				"                   COD_INDIC, " +
				"                   DESCR_INDIC, " +
				"                   TRUNC(DATA_ADESAO_TITULAR)) B, " +
				"        (SELECT B.NOME_FUNC, C.DESCR_CARGO, A.COD_FIL " +
				"           FROM SRV_FUNC_BASE_REM_VAR A, " +
				"                SRV_FUNCIONARIO       B, " +
				"                SRV_CARGO             C " +
				"          WHERE A.COD_FUNC = B.COD_FUNC " +
				"            AND C.COD_CARGO = B.COD_CARGO " +
				"            AND A.NUM_ANO = ? " +
				"            AND A.NUM_MES = ? " +
				"            AND C.COD_CARGO IN (710, 714)) CARGO " +
				"  WHERE CPF.CPF_VENDEDOR = F.NUM_CPF_FUNC " +
				"    AND CPF.CPF_VENDEDOR = B.CPF_VENDEDOR(+) " +
				"    AND CPF.DT_VENDA = B.DATA_ADESAO_TITULAR(+) " +
				"    AND F.COD_CARGO = C.COD_CARGO " +
				"    AND F.COD_FIL = CARGO.COD_FIL ");
		if (dataInicio != null && dataFim != null) {
			query.append("    AND CPF.DT_VENDA BETWEEN ? AND ? ");
		}
		query.append("  ORDER BY 1, 9 ");

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 0;

			String paramAno = String.valueOf(((Integer.valueOf(mes).intValue() == 1) ? (Integer.valueOf(ano).intValue() - 1) : Integer.valueOf(ano).intValue()));
			String paramMes = String.valueOf(((Integer.valueOf(mes).intValue() == 1) ? 12 : (Integer.valueOf(mes).intValue() - 1)));
			paramMes = (paramMes.length() == 2 ? paramMes : "0"+ paramMes);

			setString(stmt, ++i, paramAno);
			setString(stmt, ++i, paramMes);

			if (dataInicio != null && dataFim != null) {
				setDate(stmt, ++i, dataInicio);
				setDate(stmt, ++i, dataFim);
			}

			rs = stmt.executeQuery();

			return Tools.montaRelatorio(rs, true, true);

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter o relatório de Campanha.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param tabelaFuncionario
	 * @return
	 * @throws PersistenciaException
	 */
	public boolean isTabelaExist(String ano, String mes, String tabelaFuncionario) throws PersistenciaException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String TABELA_ANO_MES = tabelaFuncionario + "_" + ano + mes;
		boolean existe = false;

		StringBuffer query = new StringBuffer(" SELECT COUNT(*) FROM " + TABELA_ANO_MES);

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			rs = stmt.executeQuery();
			existe = true;
		} catch (Exception e) {
			existe = false;
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return existe;
	}

	/**
	 * 
	 * @param idIndicador
	 * @return
	 * @throws PersistenciaException
	 */
	public List<CampanhaVO> obtemListaCampanha(Integer idIndicador) throws PersistenciaException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<CampanhaVO> listaCampanha = new ArrayList<CampanhaVO>();

		StringBuffer query = new StringBuffer(
				" SELECT IND.COD_INDIC, IND.DESCR_INDIC, RTT.NM_TAB_REALZ_FUNC " +
						"   FROM SRV_REALIZADO_TAB_TMP RTT, " +
						"        SRV_INDICADOR IND " +
						"  WHERE RTT.COD_INDIC = IND.COD_INDIC ");

		if (ObjectHelper.isNotEmpty(idIndicador)) {
			query.append(" AND IND.COD_INDIC = ? ");
		}

		query.append("  ORDER BY IND.DESCR_INDIC ");

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 0;

			if (ObjectHelper.isNotEmpty(idIndicador)) {
				setInteger(stmt, ++i, idIndicador);
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				CampanhaVO campanha = new CampanhaVO();
				campanha.setIdIndicador(getInteger(rs, "COD_INDIC"));
				campanha.setDescricaoIndicador(getString(rs, "DESCR_INDIC"));
				campanha.setTabelaFuncionario(getString(rs, "NM_TAB_REALZ_FUNC"));
				listaCampanha.add(campanha);
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível a lista de campanhas.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return listaCampanha;
	}

}