package br.com.marisa.srv.unidade.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.unidade.vo.UnidadeVO;


/**
 * Classe para tratar dos métodos de acesso a dados de Unidade 
 * 
 * @author Walter Fontes
 */
public class UnidadeDAO extends BasicDAO {

	
    //Log4J
    private static final Logger log = Logger.getLogger(UnidadeDAO.class);


	/**
	 * Obtém unidades
	 * 
	 * @return
	 * @throws PersistenciaException
	 */
	public List<UnidadeVO> obtemUnidades() throws PersistenciaException {

		StringBuffer query = new StringBuffer(" SELECT COD_UN, DESCR_UN, SIMBOLO FROM SRV_UNIDADE ORDER BY DESCR_UN ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<UnidadeVO> unidades = new ArrayList<UnidadeVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			rs = stmt.executeQuery();
			while (rs.next()) {
				UnidadeVO unidadeVO = new UnidadeVO();
				unidadeVO.setIdUnidade(getInteger(rs, "COD_UN"));
				unidadeVO.setDescricaoUnidade(getString(rs, "DESCR_UN"));
				unidadeVO.setSimbolo(getString(rs, "SIMBOLO"));
				unidades.add(unidadeVO);
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter as unidades.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return unidades;
	}

	/**
	 * 
	 * @param simboloUnidadeParam
	 * @return
	 * @throws PersistenciaException
	 */
	public UnidadeVO obtemUnidadePorSimbolo(String simboloUnidadeParam) throws PersistenciaException {

		StringBuffer query = new StringBuffer(" SELECT COD_UN, DESCR_UN, SIMBOLO FROM SRV_UNIDADE WHERE UPPER(SIMBOLO) = UPPER(?) ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UnidadeVO unidadeVO = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 0;
			stmt.setString(++i, simboloUnidadeParam);
			rs = stmt.executeQuery();
			if (rs.next()) {
				unidadeVO = new UnidadeVO();
				unidadeVO.setIdUnidade(getInteger(rs, "COD_UN"));
				unidadeVO.setDescricaoUnidade(getString(rs, "DESCR_UN"));
				unidadeVO.setSimbolo(getString(rs, "SIMBOLO"));
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a unidade pelo simbolo '" + simboloUnidadeParam + "'" , e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return unidadeVO;
	}

	/**
	 * 
	 * @param descricaoUnidadeParam
	 * @return
	 * @throws PersistenciaException
	 */
	public UnidadeVO obtemUnidadePorDescricao(String descricaoUnidadeParam) throws PersistenciaException {

		StringBuffer query = new StringBuffer(" SELECT COD_UN, DESCR_UN, SIMBOLO FROM SRV_UNIDADE WHERE UPPER(DESCR_UN) = UPPER(?) ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		UnidadeVO unidadeVO = null;

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int i = 0;
			stmt.setString(++i, descricaoUnidadeParam);
			rs = stmt.executeQuery();
			if (rs.next()) {
				unidadeVO = new UnidadeVO();
				unidadeVO.setIdUnidade(getInteger(rs, "COD_UN"));
				unidadeVO.setDescricaoUnidade(getString(rs, "DESCR_UN"));
				unidadeVO.setSimbolo(getString(rs, "SIMBOLO"));
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a unidade pela descrição '" + descricaoUnidadeParam + "'" , e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return unidadeVO;
	}

}