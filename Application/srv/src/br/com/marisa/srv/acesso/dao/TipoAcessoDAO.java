package br.com.marisa.srv.acesso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.acesso.vo.TipoAcessoVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;

public class TipoAcessoDAO extends BasicDAO{

	//	Log4J
    private static final Logger log = Logger.getLogger(TipoAcessoDAO.class);
    
    public List obtemListaTipoAcesso(Integer codFuncionalidade, Integer codTipoAcesso/*, String acessosJaConcedidos*/) throws PersistenciaException {
	
    	List parametroSQL = new ArrayList();
	
		StringBuffer query =  new StringBuffer("SELECT COD_FUNCIONALIDADE," +
				" COD_TIPO_ACESSO, " +
				" DESCR_TIPO_ACESSO " +
        "  FROM SRV_FUNCIONALIDADE_TIPO_ACESSO  " +
        "  WHERE 1=1   ") ;
		if(codFuncionalidade != null) {
			query.append(" AND COD_FUNCIONALIDADE  =?  ");
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codFuncionalidade));
		}
		if(codTipoAcesso != null) {
			query.append(" AND COD_TIPO_ACESSO  = ?  ");
			parametroSQL.add(new ParametroSQL(PARAMTYPE_INTEGER, codTipoAcesso));
		}
//		removeAcessosJaConcedidos(query,parametroSQL, acessosJaConcedidos);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List lista = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametroSQL);
			rs = stmt.executeQuery();
			TipoAcessoVO tipoAcessoVO = null;
			while (rs.next()) {
				tipoAcessoVO = new TipoAcessoVO();
				tipoAcessoVO.setCodFuncionalidade(getInteger(rs, "COD_FUNCIONALIDADE"));
				tipoAcessoVO.setCodTipoAcesso(getInteger(rs, "COD_TIPO_ACESSO"));
				tipoAcessoVO.setDescricao(getString(rs,"DESCR_TIPO_ACESSO"));
				lista.add(tipoAcessoVO);
			}
		
			return lista;
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de tipos de acesso", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

    /*
	private void removeAcessosJaConcedidos(StringBuffer query, List parametroSQL, String acessosJaConcedidos) {
		if (acessosJaConcedidos != null && !acessosJaConcedidos.equals("")) {
			String linhas[] = acessosJaConcedidos.split(";");
			for(int i=0;i<linhas.length;i++) {
				
				query.append( " AND  ")
			}
		}
	}
	*/
	
}
