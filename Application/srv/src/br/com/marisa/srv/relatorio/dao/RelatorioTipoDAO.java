package br.com.marisa.srv.relatorio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.relatorio.vo.RelatorioTipoVO;

/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioTipoDAO extends BasicDAO {

	/**
	 * 
	 */
	private static final Logger log = Logger.getLogger(RelatorioTipoDAO.class);

	/**
	 * 
	 * @param codigo
	 * @return
	 * @throws PersistenciaException
	 */
	public RelatorioTipoVO obtemRelatorioTipoPorCodigo(Integer codigo) throws PersistenciaException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		RelatorioTipoVO vo = null;

		StringBuffer query = new StringBuffer(
				" SELECT COD_TP_RELATORIO, NM_TP_RELATORIO, DESCR_TP_RELATORIO " +
				"  FROM SRV_RELATORIO_TIPO " +
				" WHERE COD_TP_RELATORIO = ? ");

		try {

			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 0;
			setInteger(stmt, ++i, codigo);
			rs = stmt.executeQuery();

			if (rs.next()) {
				vo = new RelatorioTipoVO();
				vo.setCodigo(getInteger(rs, "COD_TP_RELATORIO"));
				vo.setNome(getString(rs, "NM_TP_RELATORIO"));
				vo.setDescricao(getString(rs, "DESCR_TP_RELATORIO"));
			}

		} catch (SQLException ex) {
			throw new PersistenciaException(log, "Nao foi possivel obter o tipo relatorio pelo codigo: " + codigo, ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return vo;
	}

	/**
	 * 
	 * @param pesquisaVO
	 * @return
	 * @throws PersistenciaException
	 */
	public List<RelatorioTipoVO> obtemRelatorioTipo(RelatorioTipoVO pesquisaVO) throws PersistenciaException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<RelatorioTipoVO> lista = new ArrayList<RelatorioTipoVO>();

		StringBuffer query = new StringBuffer(" SELECT COD_TP_RELATORIO, NM_TP_RELATORIO, DESCR_TP_RELATORIO  FROM SRV_RELATORIO_TIPO ");

		try {

			query.append(" ORDER BY NM_TP_RELATORIO ");
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			rs = stmt.executeQuery();

			while (rs.next()) {
				RelatorioTipoVO vo = new RelatorioTipoVO();
				vo.setCodigo(getInteger(rs, "COD_TP_RELATORIO"));
				vo.setNome(getString(rs, "NM_TP_RELATORIO"));
				vo.setDescricao(getString(rs, "DESCR_TP_RELATORIO"));
				lista.add(vo);
			}

		} catch (SQLException ex) {
			throw new PersistenciaException(log, "Nao foi possivel obter o tipo relatorio: ", ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;
	}

}