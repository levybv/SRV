package br.com.marisa.srv.acesso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.acesso.vo.FuncionalidadeVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;

public class FuncionalidadeDAO extends BasicDAO{

	//	Log4J
    private static final Logger log = Logger.getLogger(FuncionalidadeDAO.class);
    
    public List<FuncionalidadeVO> obtemListaFuncionalidades(Integer codFuncionalidade, Integer codModulo) throws PersistenciaException {

		StringBuffer query =  new StringBuffer(
				"	SELECT COD_FUNCIONALIDADE," +
				"		DESCR_FUNCIONALIDADE, " +
				"		URL_FUNCIONALIDADE, " +
				"		COD_MODULO, " +
				"		FLG_ATIVO, " +
				"		COD_FUNCIONALIDADE_PAI " +
				"	FROM SRV_FUNCIONALIDADE " +
				"	WHERE 1=1 ") ;

    	List<ParametroSQL> parametroSQL = new ArrayList<ParametroSQL>();

		if(codFuncionalidade != null) {
			query.append(" AND COD_FUNCIONALIDADE = ? ");
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codFuncionalidade));
		}
		if(codModulo != null) {
			query.append(" AND COD_MODULO = ? ");
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codModulo));
		}
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<FuncionalidadeVO> lista = new ArrayList<FuncionalidadeVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			rs = stmt.executeQuery();
			FuncionalidadeVO funcionalidadeVO = null;
			while (rs.next()) {
				funcionalidadeVO = new FuncionalidadeVO();
				funcionalidadeVO.setCodFuncionalidade(getInteger(rs, "COD_FUNCIONALIDADE"));
				funcionalidadeVO.setCodModulo(getInteger(rs, "COD_MODULO"));
				funcionalidadeVO.setDescricao(getString(rs,"DESCR_FUNCIONALIDADE"));
				funcionalidadeVO.setFlagAtivo(getBoolean(rs,"FLG_ATIVO"));
				funcionalidadeVO.setUrl(getString(rs,"URL_FUNCIONALIDADE"));
				funcionalidadeVO.setCodFuncionalidadePai(getInteger(rs,"COD_FUNCIONALIDADE_PAI"));
				lista.add(funcionalidadeVO);
			}
		
			return lista;
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de funcionalidades", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	public List<FuncionalidadeVO> obtemListaFuncionalidadesPai(Integer codModulo) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT * FROM SRV_FUNCIONALIDADE F WHERE F.COD_FUNCIONALIDADE NOT IN (SELECT COD_FUNCIONALIDADE FROM SRV_FUNCIONALIDADE WHERE COD_FUNCIONALIDADE_PAI > 0) ");

		List<ParametroSQL> parametroSQL = new ArrayList<ParametroSQL>();

		query.append(" AND F.COD_MODULO = ? ");

		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codModulo));

		query.append(" ORDER BY F.DESCR_FUNCIONALIDADE ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<FuncionalidadeVO> lista = new ArrayList<FuncionalidadeVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			rs = stmt.executeQuery();
			while (rs.next()) {
				FuncionalidadeVO funcionalidadeVO = new FuncionalidadeVO();
				funcionalidadeVO.setCodFuncionalidade(getInteger(rs, "COD_FUNCIONALIDADE"));
				funcionalidadeVO.setCodModulo(getInteger(rs, "COD_MODULO"));
				funcionalidadeVO.setDescricao(getString(rs, "DESCR_FUNCIONALIDADE"));
				funcionalidadeVO.setFlagAtivo(getBoolean(rs, "FLG_ATIVO"));
				funcionalidadeVO.setUrl(getString(rs, "URL_FUNCIONALIDADE"));
				funcionalidadeVO.setCodFuncionalidadePai(getInteger(rs, "COD_FUNCIONALIDADE_PAI"));
				lista.add(funcionalidadeVO);
			}

			return lista;
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de funcionalidades", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	public List<FuncionalidadeVO> obtemFuncionalidadesFilha(Integer idFuncionalidade) throws PersistenciaException {

		StringBuffer query = new StringBuffer(
				" SELECT * FROM SRV_FUNCIONALIDADE F WHERE F.COD_FUNCIONALIDADE_PAI = ? ORDER BY F.DESCR_FUNCIONALIDADE ");

		List<ParametroSQL> parametroSQL = new ArrayList<ParametroSQL>();

		parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, idFuncionalidade));

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<FuncionalidadeVO> lista = new ArrayList<FuncionalidadeVO>();

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			rs = stmt.executeQuery();
			while (rs.next()) {
				FuncionalidadeVO funcionalidadeVO = new FuncionalidadeVO();
				funcionalidadeVO.setCodFuncionalidade(getInteger(rs, "COD_FUNCIONALIDADE"));
				funcionalidadeVO.setCodModulo(getInteger(rs, "COD_MODULO"));
				funcionalidadeVO.setDescricao(getString(rs, "DESCR_FUNCIONALIDADE"));
				funcionalidadeVO.setFlagAtivo(getBoolean(rs, "FLG_ATIVO"));
				funcionalidadeVO.setUrl(getString(rs, "URL_FUNCIONALIDADE"));
				funcionalidadeVO.setCodFuncionalidadePai(getInteger(rs, "COD_FUNCIONALIDADE_PAI"));
				lista.add(funcionalidadeVO);
			}

			return lista;
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de funcionalidades", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

}
