package br.com.marisa.srv.relatorio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.relatorio.vo.RelatorioTipoVO;
import br.com.marisa.srv.relatorio.vo.RelatorioVO;
import br.com.marisa.srv.util.tools.RelatorioDinamicoTools;
import br.com.marisa.srv.util.tools.vo.LinhaVO;

/**
 * 
 * @author levy.villar
 *
 */
public class RelatorioDAO extends BasicDAO {

	/**
	 * 
	 */
	private static final Logger log = Logger.getLogger(RelatorioDAO.class);

	/**
	 * 
	 * @param pesquisaVO
	 * @return
	 * @throws PersistenciaException
	 */
	public List<RelatorioVO> obtemRelatorio(RelatorioVO pesquisaVO) throws PersistenciaException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<RelatorioVO> lista = new ArrayList<RelatorioVO>();

		StringBuffer query = new StringBuffer(
				" SELECT COD_RELATORIO, " +
				"        COD_TP_RELATORIO, " +
				"        NM_RELATORIO, " +
				"        DESCR_RELATORIO, " +
				"        DESCR_TITULO, " +
				"        NM_ARQUIVO, " +
				"        DESCR_COLUNAS, " +
				"        NM_TABELA, " +
				"        CMD_RELATORIO, " +
				"        QTD_REP_PARAM_TELA, " +
				"        FLG_ATIVO, " +
				"        FLG_PERIODO " +
				"   FROM SRV_RELATORIO ");

		try {

			if (ObjectHelper.isNotNull(pesquisaVO)) {
				if (ObjectHelper.isNotEmpty(pesquisaVO.getCodigo())) {
					query.append(getWhereAnd(query)).append(" COD_RELATORIO = ? ");
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getRelatorioTipoVO())) {
					if (ObjectHelper.isNotEmpty(pesquisaVO.getRelatorioTipoVO().getCodigo())) {
						query.append(getWhereAnd(query)).append(" COD_TP_RELATORIO = ? ");
					}
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getNome())) {
					query.append(getWhereAnd(query)).append(" UPPER(NM_RELATORIO) LIKE UPPER(?) ");
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricao())) {
					query.append(getWhereAnd(query)).append(" UPPER(DESCR_RELATORIO) LIKE UPPER(?) ");
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getIsAtivo())) {
					query.append(getWhereAnd(query)).append(" FLG_ATIVO = ? ");
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getIsPeriodo())) {
					query.append(getWhereAnd(query)).append(" FLG_PERIODO = ? ");
				}
			}
			query.append(" ORDER BY NM_RELATORIO ");

			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			int i = 0;
			if (ObjectHelper.isNotNull(pesquisaVO)) {
				if (ObjectHelper.isNotEmpty(pesquisaVO.getCodigo())) {
					setInteger(stmt, ++i, pesquisaVO.getCodigo());
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getRelatorioTipoVO())) {
					if (ObjectHelper.isNotEmpty(pesquisaVO.getRelatorioTipoVO().getCodigo())) {
						setInteger(stmt, ++i, pesquisaVO.getRelatorioTipoVO().getCodigo());
					}
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getNome())) {
					setString(stmt, ++i, "%"+pesquisaVO.getNome()+"%");
				}
				if (ObjectHelper.isNotEmpty(pesquisaVO.getDescricao())) {
					setString(stmt, ++i, "%"+pesquisaVO.getDescricao()+"%");
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getIsAtivo())) {
					setBoolean(stmt, ++i, pesquisaVO.getIsAtivo());
				}
				if (ObjectHelper.isNotNull(pesquisaVO.getIsPeriodo())) {
					setBoolean(stmt, ++i, pesquisaVO.getIsPeriodo());
				}
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				RelatorioVO vo = new RelatorioVO();
				vo.setCodigo(getInteger(rs, "COD_RELATORIO"));
				vo.setNome(getString(rs, "NM_RELATORIO"));
				vo.setDescricao(getString(rs, "DESCR_RELATORIO"));
				vo.setTitulo(getString(rs, "DESCR_TITULO"));
				vo.setNomeArquivo(getString(rs, "NM_ARQUIVO"));
				vo.setDescricaoColunas(getString(rs, "DESCR_COLUNAS"));
				vo.setNomeTabela(getString(rs, "NM_TABELA"));
				vo.setComandoSQL(getString(rs, "CMD_RELATORIO"));
				vo.setIsAtivo(getBoolean(rs, "FLG_ATIVO"));
				vo.setIsPeriodo(getBoolean(rs, "FLG_PERIODO"));
				vo.setQtdRepetirParametro(getInteger(rs, "QTD_REP_PARAM_TELA"));
				RelatorioTipoVO relatorioTipoVO = new RelatorioTipoVO();
				relatorioTipoVO.setCodigo(getInteger(rs, "COD_TP_RELATORIO"));
				vo.setRelatorioTipoVO(relatorioTipoVO);
				lista.add(vo);
			}

		} catch (SQLException ex) {
			throw new PersistenciaException(log, "Nao foi possivel obter o relatorio: ", ex);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return lista;
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 * @throws PersistenciaException
	 */
	public RelatorioVO obtemRelatorioPorCodigo(Integer codigo) throws PersistenciaException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		RelatorioVO vo = null;

		StringBuffer query = new StringBuffer(
				" SELECT COD_RELATORIO, " +
				"        COD_TP_RELATORIO, " +
				"        NM_RELATORIO, " +
				"        DESCR_RELATORIO, " +
				"        DESCR_TITULO, " +
				"        NM_ARQUIVO, " +
				"        DESCR_COLUNAS, " +
				"        NM_TABELA, " +
				"        CMD_RELATORIO, " +
				"        FLG_ATIVO, " +
				"        QTD_REP_PARAM_TELA, " +
				"        FLG_PERIODO " +
				"   FROM SRV_RELATORIO " +
				"  WHERE COD_RELATORIO = ? ");

		try {

			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 0;
			setInteger(stmt, ++i, codigo);
			rs = stmt.executeQuery();

			while (rs.next()) {
				vo = new RelatorioVO();
				vo.setCodigo(getInteger(rs, "COD_RELATORIO"));
				vo.setNome(getString(rs, "NM_RELATORIO"));
				vo.setDescricao(getString(rs, "DESCR_RELATORIO"));
				vo.setTitulo(getString(rs, "DESCR_TITULO"));
				vo.setNomeArquivo(getString(rs, "NM_ARQUIVO"));
				vo.setDescricaoColunas(getString(rs, "DESCR_COLUNAS"));
				vo.setNomeTabela(getString(rs, "NM_TABELA"));
				vo.setComandoSQL(getString(rs, "CMD_RELATORIO"));
				vo.setIsAtivo(getBoolean(rs, "FLG_ATIVO"));
				vo.setIsPeriodo(getBoolean(rs, "FLG_PERIODO"));
				vo.setQtdRepetirParametro(getInteger(rs, "QTD_REP_PARAM_TELA"));
				RelatorioTipoVO relatorioTipoVO = new RelatorioTipoVO();
				relatorioTipoVO.setCodigo(getInteger(rs, "COD_TP_RELATORIO"));
				vo.setRelatorioTipoVO(relatorioTipoVO);
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possivel obter o relatorio pelo codigo: " + codigo, e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return vo;
	}

	/**
	 * 
	 * @param nomeTabela
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 */
	public List<LinhaVO> montaRelatorioPorTabela(String nomeTabela, Integer ano, Integer mes) throws PersistenciaException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<LinhaVO> linhas = null;

		StringBuffer query = new StringBuffer(" SELECT * FROM " + nomeTabela);

		if (ObjectHelper.isNotEmpty(ano) && ObjectHelper.isNotEmpty(mes)) {
			query.append(" WHERE NUM_ANO = ? AND NUM_MES = ? ");
		}

		try {

			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			int i = 0;
			if (ObjectHelper.isNotEmpty(ano) && ObjectHelper.isNotEmpty(mes)) {
				setInteger(stmt, ++i, ano);
				setInteger(stmt, ++i, mes);
			}

			rs = stmt.executeQuery();
			linhas = RelatorioDinamicoTools.montaRelatorioDinamico(rs);

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível montar o relatorio por tabela.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return linhas;
	}

	/**
	 * 
	 * @param comandoSQL
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 */
	public List<LinhaVO> montaRelatorioPorComando(String comandoSQL, Integer ano, Integer mes, Integer qtdRepParam) throws PersistenciaException {

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<LinhaVO> linhas = null;

		StringBuffer query = new StringBuffer(comandoSQL);

		try {

			conn = getConn();
			stmt = conn.prepareStatement(query.toString());

			int i = 0;
			for (int j = 0; j < qtdRepParam; j++) {
				setInteger(stmt, ++i, ano);
				setInteger(stmt, ++i, mes);
			}

			rs = stmt.executeQuery();
			linhas = RelatorioDinamicoTools.montaRelatorioDinamico(rs);

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível montar o relatorio por comando SQL.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return linhas;
	}

	/**
	 * 
	 * @param incluiVO
	 * @throws PersistenciaException
	 */
	public void incluiRelatorio(RelatorioVO incluiVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_RELATORIO " +
				"   (COD_RELATORIO, " +
				"    COD_TP_RELATORIO, " +
				"    NM_RELATORIO, " +
				"    DESCR_RELATORIO, " +
				"    DESCR_TITULO, " +
				"    NM_ARQUIVO, " +
				"    DESCR_COLUNAS, " +
				"    NM_TABELA, " +
				"    CMD_RELATORIO, " +
				"    FLG_ATIVO, " +
				"    FLG_PERIODO, " +
				"    QTD_REP_PARAM_TELA, " +
				"    DT_INI_SIT_SRV, " +
				"    COD_USUARIO) " +
				" VALUES " +
				"   (SEQ_SRV_RELATORIO.NEXTVAL, " +
				"    ?, " +
				"    ?, " +
				"    ?, " +
				"    ?, " +
				"    ?, " +
				"    ?, " +
				"    ?, " +
				"    ?, " +
				"    ?, " +
				"    ?, " +
				"    ?, " +
				"    SYSDATE, " +
				"    ?) ");

		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getRelatorioTipoVO().getCodigo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, incluiVO.getNome()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, incluiVO.getDescricao()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, incluiVO.getTitulo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, incluiVO.getNomeArquivo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, incluiVO.getDescricaoColunas()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, incluiVO.getNomeTabela()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, incluiVO.getComandoSQL()));
    	parametros.add(new ParametroSQL(PARAMTYPE_BOOLEAN, incluiVO.getIsAtivo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_BOOLEAN, incluiVO.getIsPeriodo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getQtdRepetirParametro()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, incluiVO.getIdUsuario()));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir o relatorio.", e);
		} finally {
			closeStatement(stmt);
		}
	}	

	/**
	 * 
	 * @param alteraVO
	 * @throws PersistenciaException
	 */
	public void alteraRelatorio(RelatorioVO alteraVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" UPDATE SRV_RELATORIO SET " +
				"    COD_TP_RELATORIO = ?, " +
				"    NM_RELATORIO = ?, " +
				"    DESCR_RELATORIO = ?, " +
				"    DESCR_TITULO = ?, " +
				"    NM_ARQUIVO = ?, " +
				"    DESCR_COLUNAS = ?, " +
				"    NM_TABELA = ?, " +
				"    CMD_RELATORIO = ?, " +
				"    FLG_ATIVO = ?, " +
				"    FLG_PERIODO = ?, " +
				"    QTD_REP_PARAM_TELA = ?, " +
				"    DT_INI_SIT_SRV = SYSDATE, " +
				"    COD_USUARIO = ? " +
				" WHERE COD_RELATORIO = ? ");

		List<ParametroSQL> parametros = new ArrayList<ParametroSQL>();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, alteraVO.getRelatorioTipoVO().getCodigo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, alteraVO.getNome()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, alteraVO.getDescricao()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, alteraVO.getTitulo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, alteraVO.getNomeArquivo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, alteraVO.getDescricaoColunas()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, alteraVO.getNomeTabela()));
    	parametros.add(new ParametroSQL(PARAMTYPE_STRING, alteraVO.getComandoSQL()));
    	parametros.add(new ParametroSQL(PARAMTYPE_BOOLEAN, alteraVO.getIsAtivo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_BOOLEAN, alteraVO.getIsPeriodo()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, alteraVO.getQtdRepetirParametro()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, alteraVO.getIdUsuario()));
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, alteraVO.getCodigo()));

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível alterar o relatorio.", e);
		} finally {
			closeStatement(stmt);
		}
	}	

	/**
	 * 
	 * @param excluiVO
	 * @throws PersistenciaException
	 */
	public void excluiRelatorio(RelatorioVO excluiVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(" DELETE FROM SRV_RELATORIO WHERE COD_RELATORIO = ? ");

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			setInteger(stmt, 1, excluiVO.getCodigo());
			stmt.executeUpdate();
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível excluir o relatorio.", e);
		} finally {
			closeStatement(stmt);
		}
	}	

}