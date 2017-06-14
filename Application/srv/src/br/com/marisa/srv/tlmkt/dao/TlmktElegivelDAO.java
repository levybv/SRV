package br.com.marisa.srv.tlmkt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.tlmkt.vo.TelemarketingVO;


/**
 * 
 * @author Levy Villar
 */
public class TlmktElegivelDAO extends BasicDAO {

    private static final Logger log = Logger.getLogger(TlmktElegivelDAO.class);

    /**
     * 
     * @param ano
     * @param mes
     * @return
     * @throws PersistenciaException
     */
	public TelemarketingVO obtemTlmktElegivel(Integer ano, Integer mes, Long codFuncionario) throws PersistenciaException {

		StringBuffer query = new StringBuffer(" SELECT NUM_ANO, NUM_MES, COD_FUNC, DT_INI_SIT_SRV, COD_USUARIO FROM SRV_FUNC_ELEG_TLMKT ");

		if (ObjectHelper.isNotEmpty(ano)) {
			query.append(getWhereAnd(query)).append(" NUM_ANO = ? ");
		}
		if (ObjectHelper.isNotEmpty(mes)) {
			query.append(getWhereAnd(query)).append(" NUM_MES = ? ");
		}
		if (ObjectHelper.isNotEmpty(codFuncionario)) {
			query.append(getWhereAnd(query)).append(" COD_FUNC = ? ");
		}

		query.append(" ORDER BY NUM_ANO, NUM_MES, COD_FUNC");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		TelemarketingVO tlmktElegivelVO = new TelemarketingVO();
		tlmktElegivelVO.setAno(ano);
		tlmktElegivelVO.setMes(mes);

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 0;

			if (ObjectHelper.isNotEmpty(ano)) {
				setInteger(stmt, ++i, ano);
			}
			if (ObjectHelper.isNotEmpty(mes)) {
				setInteger(stmt, ++i, mes);
			}
			if (ObjectHelper.isNotEmpty(codFuncionario)) {
				setLong(stmt, ++i, codFuncionario);
			}
			rs = stmt.executeQuery();

			List<FuncionarioVO> listaFuncionario = new ArrayList<FuncionarioVO>();
			while (rs.next()) {
				listaFuncionario.add(new FuncionarioVO(getLong(rs, "COD_FUNC")));
			}
			tlmktElegivelVO.setListaFuncionarioVO(listaFuncionario);

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter a lista de elegiveis telemarketing: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return tlmktElegivelVO;
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param codFunc
	 * @param codUsuario
	 * @throws PersistenciaException
	 */
	public void incluiTlmktElegivel(Integer ano, Integer mes, Long codFunc, Integer codUsuario) throws PersistenciaException {

		String query = " INSERT INTO SRV_FUNC_ELEG_TLMKT (NUM_ANO,NUM_MES,COD_FUNC,DT_INI_SIT_SRV,COD_USUARIO) VALUES (?, ?, ?, SYSDATE, ?) ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int i = 0;

			setInteger(stmt, ++i, ano);
			setInteger(stmt, ++i, mes);
			setLong(stmt, ++i, codFunc);
			setInteger(stmt, ++i, codUsuario);

			stmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel incluir o elegivel telemarketing: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param codFuncionario
	 * @throws PersistenciaException
	 */
	public void excluiTlmktElegivel(Integer ano,Integer mes, Long codFuncionario) throws PersistenciaException {

		String query =  " DELETE FROM SRV_FUNC_ELEG_TLMKT WHERE NUM_ANO = ? AND NUM_MES = ? AND COD_FUNC = ? ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int i = 0;
			setInteger(stmt, ++i, ano);
			setInteger(stmt, ++i, mes);
			setLong(stmt, ++i, codFuncionario);
			stmt.execute();
		} catch(Exception ex) {
			throw new PersistenciaException(log, "Nao foi possivel excluir o elegivel telemarketing: " + ex.getMessage(), ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param codFuncionario
	 * @throws PersistenciaException
	 */
	public void incluiTlmktElegivelHist(Integer ano,Integer mes, Long codFuncionario) throws PersistenciaException {

		String query = " INSERT INTO SRV_FUNC_ELEG_TLMKT_HIST (NUM_ANO,NUM_MES,NUM_SEQ,COD_FUNC,DT_INI_SIT_SRV,COD_USUARIO) " +
						" SELECT NUM_ANO,NUM_MES,?,COD_FUNC,DT_INI_SIT_SRV,COD_USUARIO FROM SRV_FUNC_ELEG_TLMKT WHERE NUM_ANO = ? AND NUM_MES = ? AND COD_FUNC = ? ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			conn = getConn();
			stmt = conn.prepareStatement(query);
			int i = 0;
			setInteger(stmt, ++i, this.obtemProximaSequenciaHistorio(ano, mes, codFuncionario));
			setInteger(stmt, ++i, ano);
			setInteger(stmt, ++i, mes);
			setLong(stmt, ++i, codFuncionario);
			stmt.executeUpdate();

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel incluir o historico elegivel telemarketing: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	/**
	 * 
	 * @param ano
	 * @param mes
	 * @param codFuncionario
	 * @return
	 * @throws PersistenciaException
	 */
	private int obtemProximaSequenciaHistorio(Integer ano, Integer mes, Long codFuncionario) throws PersistenciaException {

		String query = " SELECT MAX(NUM_SEQ) FROM SRV_FUNC_ELEG_TLMKT_HIST WHERE NUM_ANO = ? AND NUM_MES = ? AND COD_FUNC = ? ";

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int proximaSequencia = 1;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			int i = 0;
			setInteger(stmt, ++i, ano);
			setInteger(stmt, ++i, mes);
			setLong(stmt, ++i, codFuncionario);
			rs = stmt.executeQuery();

			if (rs.next()) {
				proximaSequencia = getInt(rs, 1) + 1;
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter a proxima sequencia de elegivel telemarketing historico: " + e.getMessage(), e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return proximaSequencia;
	}

}