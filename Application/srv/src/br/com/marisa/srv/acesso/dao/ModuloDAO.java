package br.com.marisa.srv.acesso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.acesso.vo.ModuloVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;

public class ModuloDAO extends BasicDAO{

    private static final Logger log = Logger.getLogger(ModuloDAO.class);

	public List<ModuloVO> obtemListaModulos(Integer codModulo) throws PersistenciaException {

		StringBuffer query =  new StringBuffer("SELECT COD_MODULO, DESCR_MODULO FROM SRV_MODULO ");

		if(codModulo != null) {
			query.append(" WHERE COD_MODULO = ? ");
		}

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ModuloVO> lista = new ArrayList<ModuloVO>();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			if(codModulo != null) {
				int ordemCampos = 1;
				setInteger(stmt, ordemCampos++, codModulo);
			}
			rs = stmt.executeQuery();
			ModuloVO moduloVO = null;
			while (rs.next()) {
				moduloVO = new ModuloVO();
				moduloVO.setCodModulo(getInteger(rs, "COD_MODULO"));
				moduloVO.setDescricao(getString(rs, "DESCR_MODULO"));
				lista.add(moduloVO);
			}
		
			return lista;
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de modulos", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

}
