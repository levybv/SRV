package br.com.marisa.srv.indicador.ppt.dao;

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
import br.com.marisa.srv.indicador.ppt.vo.PecaTicketVO;


public class PecaTicketDAO extends BasicDAO {

    private static final Logger log = Logger.getLogger(PecaTicketDAO.class);

    /**
     * 
     * @param pesquisaVO
     * @return
     * @throws PersistenciaException
     */
	public List<PecaTicketVO> obterListaRealizadoFilialPecaTicket(PecaTicketVO pesquisaVO) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT RF.COD_EMP, " +
				"        RF.COD_FIL, " +
				"        FI.DESCR_FIL, " +
				"        RF.NUM_ANO, " +
				"        RF.NUM_MES, " +
				"        RF.NUM_REALZ, " +
				"        RF.COD_UN_REALZ, " +
				"        RF.DT_INI_SIT_SRV, " +
				"        RF.COD_USUARIO " +
				"   FROM SRV_REALIZADO_FILIAL_INDIC_PPT RF, " +
				"        SRV_FILIAL FI " +
				"   WHERE FI.COD_FIL = RF.COD_FIL " +
				"     AND RF.NUM_ANO = ? " +
				"     AND RF.NUM_MES = ? ");

		if (ObjectHelper.isNotEmpty(pesquisaVO.getCodFilial())) {
			query.append("     AND RF.COD_FIL = ? ");
		}
		if (ObjectHelper.isNotEmpty(pesquisaVO.getCodEmpresa())) {
			query.append("     AND RF.COD_EMP = ? ");
		}
		query.append(" ORDER BY RF.COD_FIL, RF.NUM_ANO, RF.NUM_MES ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<PecaTicketVO> lista = new ArrayList<PecaTicketVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setInteger(stmt, i++, pesquisaVO.getNumAno());
			setInteger(stmt, i++, pesquisaVO.getNumMes());
			if (ObjectHelper.isNotEmpty(pesquisaVO.getCodFilial())) {
				setInteger(stmt, i++, pesquisaVO.getCodFilial());
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getCodEmpresa())) {
				setInteger(stmt, i++, pesquisaVO.getCodEmpresa());
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				PecaTicketVO vo = new PecaTicketVO();
				vo.setNumAno(getInteger(rs, "NUM_ANO"));
				vo.setNumMes(getInteger(rs, "NUM_MES"));
				vo.setCodEmpresa(getInteger(rs, "COD_EMP"));
				vo.setCodFilial(getInteger(rs, "COD_FIL"));
				vo.setDescrFilial(getString(rs, "DESCR_FIL"));
				vo.setNumRealizado(getDouble(rs, "NUM_REALZ"));
				vo.setCodUniRealizado(getInteger(rs, "COD_UN_REALZ"));
				vo.setIdUsuario(getInteger(rs, "COD_USUARIO"));
				vo.setDataAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				lista.add(vo);
			}

		} catch (Exception ex) {
			throw new PersistenciaException(log, "Não foi possível obter a lista realizado filial PPT.", ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;
	}

	/**
	 * 
	 * @param pesquisaVO
	 * @return
	 * @throws PersistenciaException
	 */
	public PecaTicketVO obterRealizadoFilialPecaTicket(PecaTicketVO pesquisaVO) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT RF.COD_EMP, " +
				"        RF.COD_FIL, " +
				"        FI.DESCR_FIL, " +
				"        RF.NUM_ANO, " +
				"        RF.NUM_MES, " +
				"        RF.NUM_REALZ, " +
				"        RF.COD_UN_REALZ, " +
				"        RF.DT_INI_SIT_SRV, " +
				"        RF.COD_USUARIO " +
				"   FROM SRV_REALIZADO_FILIAL_INDIC_PPT RF, " +
				"        SRV_FILIAL FI " +
				"   WHERE FI.COD_FIL = RF.COD_FIL " +
				"     AND RF.NUM_ANO = ? " +
				"     AND RF.NUM_MES = ? " +
				"     AND RF.COD_FIL = ? " +
				"     AND RF.COD_EMP = ? ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		PecaTicketVO item = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setInteger(stmt, i++, pesquisaVO.getNumAno());
			setInteger(stmt, i++, pesquisaVO.getNumMes());
			setInteger(stmt, i++, pesquisaVO.getCodFilial());
			setInteger(stmt, i++, pesquisaVO.getCodEmpresa());

			rs = stmt.executeQuery();

			if (rs.next()) {
				item = new PecaTicketVO();
				item.setNumAno(getInteger(rs, "NUM_ANO"));
				item.setNumMes(getInteger(rs, "NUM_MES"));
				item.setCodEmpresa(getInteger(rs, "COD_EMP"));
				item.setCodFilial(getInteger(rs, "COD_FIL"));
				item.setDescrFilial(getString(rs, "DESCR_FIL"));
				item.setNumRealizado(getDouble(rs, "NUM_REALZ"));
				item.setCodUniRealizado(getInteger(rs, "COD_UN_REALZ"));
				item.setIdUsuario(getInteger(rs, "COD_USUARIO"));
				item.setDataAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
			}

		} catch (Exception ex) {
			throw new PersistenciaException(log, "Não foi possível obter o realizado filial PPT.", ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return item;
	}

	/**
	 * 
	 * @param incluiVO
	 * @throws PersistenciaException
	 */
	public void incluiPecaTicket(PecaTicketVO incluiVO) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
			" INSERT INTO SRV_REALIZADO_FILIAL_INDIC_PPT (COD_EMP,COD_FIL,NUM_ANO,NUM_MES,NUM_REALZ,COD_UN_REALZ,DT_INI_SIT_SRV,COD_USUARIO) VALUES (?,?,?,?,?,?,SYSDATE,?) ");

		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getCodEmpresa()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getCodFilial()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getNumAno()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getNumMes()));
		parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, incluiVO.getNumRealizado()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getCodUniRealizado()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getIdUsuario()));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir realizado filial PPT.", e);
		} finally {
			closeStatement(stmt);
		}
	}

	/**
	 * 
	 * @param mes
	 * @param ano
	 * @param idEmpresa
	 * @param idFilial
	 * @throws PersistenciaException
	 */
	public void excluiMetaFilial(final PecaTicketVO excluiVO) throws PersistenciaException {

		StringBuffer query = new StringBuffer(" DELETE FROM SRV_REALIZADO_FILIAL_INDIC_PPT WHERE NUM_ANO = ? AND NUM_MES = ? AND COD_FIL = ? AND COD_EMP = ? ");

		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, excluiVO.getNumAno()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, excluiVO.getNumMes()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, excluiVO.getCodFilial()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, excluiVO.getCodEmpresa()));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível excluir realizado filial PPT.", e);
		} finally {
			closeStatement(stmt);
		}
	}

	/**
	 * 
	 * @param incluiVO
	 * @throws PersistenciaException
	 */
	public void incluiPecaTicketHist(final PecaTicketVO incluiVO) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
			" INSERT INTO SRV_REAL_FILIAL_INDIC_PPT_HIST (COD_EMP,COD_FIL,NUM_ANO,NUM_MES,SEQ_REALZ,NUM_REALZ,COD_UN_REALZ,DT_INI_SIT_SRV,COD_USUARIO,DT_INCLUSAO) VALUES (?,?,?,?,?,?,?,?,?,SYSDATE) ");

		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getCodEmpresa()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getCodFilial()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getNumAno()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getNumMes()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, obtemProximaSequenciaHistorio(incluiVO)));
		parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, incluiVO.getNumRealizado()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getCodUniRealizado()));
		parametros.add(new ParametroSQL(PARAMTYPE_DATE, incluiVO.getDataAlteracao()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getIdUsuario()));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir realizado filial PPT historico.", e);
		} finally {
			closeStatement(stmt);
		}
	}

	/**
	 * 
	 * @param mes
	 * @param ano
	 * @param idEmpresa
	 * @param idFilial
	 * @return
	 * @throws PersistenciaException
	 */
	private int obtemProximaSequenciaHistorio(final PecaTicketVO vo) throws PersistenciaException {

		String query = " SELECT MAX(SEQ_REALZ) FROM SRV_REAL_FILIAL_INDIC_PPT_HIST WHERE NUM_ANO = ? AND NUM_MES = ? AND COD_FIL = ? AND COD_EMP = ? ";

		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, vo.getNumAno()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, vo.getNumMes()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, vo.getCodFilial()));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, vo.getCodEmpresa()));

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
			throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico de realizado filial PPT.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return proximaSequencia;
	}

}