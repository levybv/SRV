package br.com.marisa.srv.gerente.filial.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.gerente.filial.vo.GerenteVO;

/**
 * 
 * @author levy.villar
 *
 */
public class GerenteFilialDAO extends BasicDAO {

	/**
	 * 
	 */
    private static final Logger log = Logger.getLogger(GerenteFilialDAO.class);

    /**
     * 
     * @param codFuncionario
     * @param codFilial
     * @param codUsuario
     * @throws PersistenciaException
     */
	public void excluiGerenteFilial(Long codFuncionario, Integer codFilial, Integer codUsuario) throws PersistenciaException {

		StringBuffer query = new StringBuffer(" DELETE FROM SRV_GERENTE_2_LOJAS WHERE COD_FUNC = ? AND COD_FIL = ? ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			incluiGerenteFilialHistorico(codFuncionario, codFilial);

			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setLong(stmt, i++, codFuncionario);
			setInteger(stmt, i++, codFilial);

			stmt.executeUpdate();

		} catch(Exception ex) {
			throw new PersistenciaException(log, "Não foi possível excluir o gerente de loja: ", ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

	}

	/**
	 * 
	 * @param vo
	 * @throws PersistenciaException
	 */
	public void incluiGerenteFilial(GerenteVO vo) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" INSERT INTO SRV_GERENTE_2_LOJAS (COD_FUNC, COD_ATUACAO, COD_FIL_ORIG, COD_FIL, DT_INCL_FIL, DT_EXCL_FIL, COD_USUARIO, ST_CARGA) VALUES (?,?,?,?,SYSDATE,NULL,?,0) ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setLong(stmt, i++, vo.getIdFuncionario());
			setInteger(stmt, i++, vo.getCodAtuacao());
			setInteger(stmt, i++, vo.getFilialOrigem().getCodFilial());
			setInteger(stmt, i++, vo.getFilial().getCodFilial());
			setInteger(stmt, i++, vo.getIdUsuarioUltimaAlteracao());

			stmt.executeUpdate();

		} catch(Exception ex) {
			throw new PersistenciaException(log, "Não foi possível incluir o gerente de loja: ", ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

	}

	/**
	 * 
	 * @param codFuncionario
	 * @param codFilial
	 * @throws PersistenciaException
	 */
	public void incluiGerenteFilialHistorico(Long codFuncionario, Integer codFilial) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" INSERT INTO SRV_GERENTE_2_LOJAS_HIST " +
				"   (COD_FUNC, " +
				"    COD_ATUACAO, " +
				"    COD_FIL_ORIG, " +
				"    COD_FIL, " +
				"    DT_INCL_FIL, " +
				"    DT_EXCL_FIL, " +
				"    COD_USUARIO, " +
				"    ST_CARGA, " +
				"    DT_HIST) " +
				"   SELECT G.COD_FUNC, " +
				"          G.COD_ATUACAO, " +
				"          G.COD_FIL_ORIG, " +
				"          G.COD_FIL, " +
				"          G.DT_INCL_FIL, " +
				"          G.DT_EXCL_FIL, " +
				"          G.COD_USUARIO, " +
				"          G.ST_CARGA, " +
				"          SYSDATE " +
				"     FROM SRV_GERENTE_2_LOJAS G " +
				"    WHERE G.COD_FUNC = ? " +
				"      AND G.COD_FIL = ? ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setLong(stmt, i++, codFuncionario);
			setInteger(stmt, i++, codFilial);

			stmt.executeUpdate();

		} catch(Exception ex) {
			throw new PersistenciaException(log, "Não foi possível incluir o historico do gerente de loja: ", ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

	}

	/**
	 * 
	 * @param pesquisaVO
	 * @return
	 * @throws PersistenciaException
	 */
	public List<GerenteVO> obtemListaGerenteLoja(GerenteVO pesquisaVO) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT G.COD_FUNC     AS COD_FUNC, " +
				"        FU.NOME_FUNC   AS NOME_FUNC, " +
				"        G.COD_FIL_ORIG AS COD_FIL_ORIG, " +
				"        FO.DESCR_FIL   AS DESC_FIL_ORIG, " +
				"        G.COD_FIL      AS COD_FIL, " +
				"        G.COD_ATUACAO  AS COD_ATUACAO, " +
				"        G.DT_INCL_FIL  AS DT_INCL_FIL, " +
				"        G.DT_EXCL_FIL  AS DT_EXCL_FIL, " +
				"        G.ST_CARGA     AS ST_CARGA, " +
				"        G.COD_USUARIO  AS COD_USUARIO, " +
				"        FI.DESCR_FIL   AS DESCR_FIL " +
				"   FROM SRV_GERENTE_2_LOJAS G, " +
				"        SRV_FUNCIONARIO FU, " +
				"        SRV_FILIAL FI, " +
				"        SRV_FILIAL FO " +
				"  WHERE G.COD_FUNC = FU.COD_FUNC " +
				"    AND G.COD_FIL = FI.COD_FIL " +
				"    AND G.COD_FIL_ORIG = FO.COD_FIL ");

		if (ObjectHelper.isNotNull(pesquisaVO)) {
			if (ObjectHelper.isNotEmpty(pesquisaVO.getIdFuncionario())) {
				query.append(" AND G.COD_FUNC = ? ");
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getNomeFuncionario())) {
				query.append(" AND UPPER(FU.NOME_FUNC) LIKE ? ");
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getCodAtuacao())) {
				query.append(" AND G.COD_ATUACAO = ? ");
			}
			if (ObjectHelper.isNotEmpty(pesquisaVO.getCodUsuario())) {
				query.append(" AND G.COD_USUARIO = ? ");
			}
		}
		query.append(" ORDER BY G.COD_FUNC, G.COD_FIL ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<GerenteVO> lista = new ArrayList<GerenteVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			if (ObjectHelper.isNotNull(pesquisaVO)) {
				if (ObjectHelper.isNotEmpty(pesquisaVO.getIdFuncionario())) {
					setLong(stmt, i++, pesquisaVO.getIdFuncionario());
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getNomeFuncionario())) {
					setString(stmt, i++, "%"+pesquisaVO.getNomeFuncionario().toUpperCase()+"%");
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getCodAtuacao())) {
					setInteger(stmt, i++, pesquisaVO.getCodAtuacao());
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getCodUsuario())) {
					setInteger(stmt, i++, pesquisaVO.getCodUsuario());
				}
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				GerenteVO vo = new GerenteVO();
				vo.setIdFuncionario(getLong(rs, "COD_FUNC"));
				vo.setNomeFuncionario(getString(rs, "NOME_FUNC"));
				FilialVO filialOrigem = new FilialVO();
				filialOrigem.setCodFilial(getInteger(rs, "COD_FIL_ORIG"));
				filialOrigem.setDescricao(getString(rs, "DESC_FIL_ORIG"));
				vo.setFilialOrigem(filialOrigem);
				vo.setCodAtuacao(getInteger(rs, "COD_ATUACAO"));
				vo.setDataInclusaoFilial(getTimestamp(rs, "DT_INCL_FIL"));
				vo.setDataExclusaoFilial(getTimestamp(rs, "DT_EXCL_FIL"));
				vo.setStatusCarga(getInteger(rs, "ST_CARGA"));
				vo.setCodUsuario(getInteger(rs, "COD_USUARIO"));
				FilialVO filial = new FilialVO();
				filial.setCodFilial(getInteger(rs, "COD_FIL"));
				filial.setDescricao(getString(rs, "DESCR_FIL"));
				vo.setFilial(filial);
				lista.add(vo);
			}

		} catch(Exception ex) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de gerente de loja: ", ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;
	}

	/**
	 * 
	 * @param codFilial
	 * @return
	 * @throws PersistenciaException
	 */
	public Long lojaCadastrada(Integer codFilial) throws PersistenciaException {

		StringBuffer query = new StringBuffer(" SELECT COD_FUNC FROM SRV_GERENTE_2_LOJAS WHERE COD_FIL = ? ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Long codFuncionario = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setInteger(stmt, i++, codFilial);

			rs = stmt.executeQuery();

			if (rs.next()) {
				codFuncionario = getLong(rs, "COD_FUNC");
			}

		} catch(Exception ex) {
			throw new PersistenciaException(log, "Não foi possível obter a filial: ", ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return codFuncionario;
	}

	/**
	 * 
	 * @param codFuncionario
	 * @param codFilial
	 * @return
	 * @throws PersistenciaException
	 */
	public GerenteVO obtemGerenteLoja(Long codFuncionario, Integer codFilial) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT G.COD_FUNC AS COD_FUNC, " +
				"        FU.NOME_FUNC AS NOME_FUNC, " +
				"        G.COD_FIL_ORIG AS COD_FIL_ORIG, " +
				"        FO.DESCR_FIL AS DESC_FIL_ORIG, " +
				"        G.COD_FIL AS COD_FIL, " +
				"        G.COD_ATUACAO AS COD_ATUACAO, " +
				"        G.DT_INCL_FIL AS DT_INCL_FIL, " +
				"        G.DT_EXCL_FIL AS DT_EXCL_FIL, " +
				"        G.ST_CARGA AS ST_CARGA, " +
				"        FI.DESCR_FIL AS DESCR_FIL " +
				"   FROM SRV_GERENTE_2_LOJAS G, " +
				"        SRV_FUNCIONARIO     FU, " +
				"        SRV_FILIAL          FI, " +
				"        SRV_FILIAL          FO " +
				"  WHERE G.COD_FUNC = FU.COD_FUNC " +
				"    AND G.COD_FIL = FI.COD_FIL " +
				"    AND G.COD_FIL_ORIG = FO.COD_FIL " +
				"    AND G.COD_FUNC = ? " +
				"    AND G.COD_FIL = ? ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		GerenteVO vo = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setLong(stmt, i++, codFuncionario);
			setInteger(stmt, i++, codFilial);

			rs = stmt.executeQuery();

			while (rs.next()) {
				vo = new GerenteVO();
				vo.setIdFuncionario(getLong(rs, "COD_FUNC"));
				vo.setNomeFuncionario(getString(rs, "NOME_FUNC"));
				FilialVO filialOrigem = new FilialVO();
				filialOrigem.setCodFilial(getInteger(rs, "COD_FIL_ORIG"));
				filialOrigem.setDescricao(getString(rs, "DESC_FIL_ORIG"));
				vo.setFilialOrigem(filialOrigem);
				FilialVO filial = new FilialVO();
				filial.setCodFilial(getInteger(rs, "COD_FIL"));
				filial.setDescricao(getString(rs, "DESCR_FIL"));
				vo.setFilial(filial);
				vo.setCodAtuacao(getInteger(rs, "COD_ATUACAO"));
				vo.setDataInclusaoFilial(getTimestamp(rs, "DT_INCL_FIL"));
				vo.setDataExclusaoFilial(getTimestamp(rs, "DT_EXCL_FIL"));
				vo.setStatusCarga(getInteger(rs, "ST_CARGA"));
			}

		} catch(Exception ex) {
			throw new PersistenciaException(log, "Não foi possível obter o de gerente de loja: ", ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return vo;
	}

	public List<GerenteVO> obtemFuncionariosPorFilial(Integer codFilialOrigem) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT F.COD_FUNC, F.NOME_FUNC, F.COD_FIL FROM SRV_FUNCIONARIO F, SRV_CARGO C WHERE F.COD_CARGO = C.COD_CARGO AND C.COD_CARGO IN (1162, 1163, 1164) ");

		if (ObjectHelper.isNotNull(codFilialOrigem)) {
			query.append(" AND F.COD_FIL = ? ");
		}
		query.append(" ORDER BY F.NOME_FUNC ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		List<GerenteVO> lista = new ArrayList<GerenteVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			if (ObjectHelper.isNotNull(codFilialOrigem)) {
				setInteger(stmt, i++, codFilialOrigem);
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				GerenteVO vo = new GerenteVO();
				vo.setIdFuncionario(getLong(rs, "COD_FUNC"));
				vo.setNomeFuncionario(getString(rs, "NOME_FUNC"));
				lista.add(vo);
			}

		} catch(Exception ex) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de funcionarios por filial: ", ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;
	}

	/**
	 * 
	 * @param idGerente
	 * @return
	 * @throws PersistenciaException
	 */
	public Integer obtemLojaCadastro(Long idGerente) throws PersistenciaException {

		StringBuffer query = new StringBuffer(" SELECT F.COD_FIL FROM SRV_FUNCIONARIO F WHERE F.COD_FUNC = ? ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer filialCadastro = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 1;
			setLong(stmt, i++, idGerente);

			rs = stmt.executeQuery();

			if (rs.next()) {
				filialCadastro = getInteger(rs, "COD_FIL");
			}

		} catch(Exception ex) {
			throw new PersistenciaException(log, "Não foi possível obter a filial de origem: ", ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return filialCadastro;
	}

}