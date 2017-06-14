package br.com.marisa.srv.funcionario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

public class HierarquiaDAO extends BasicDAO{
	
	
	//Log4J
    private static final Logger log = Logger.getLogger(HierarquiaDAO.class);
    
    
	public List<FuncionarioVO> obtemListaSubordinados(Long idLider, Integer grupo) throws PersistenciaException {
		StringBuilder query =  new StringBuilder(
				"SELECT af.COD_FUNC_subordinado , f.nome_func" +
				"  FROM SRV_AGRUPA_FUNCIONARIO af, srv_funcionario f " +
				" WHERE COD_GRP_REM_VAR = ? "
				+ " and af.cod_func_subordinado = f.cod_func");
				
				
		
		List <ParametroSQL>parametros = new ArrayList<ParametroSQL>();
		
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, grupo));
		if(idLider != null){
			parametros.add(new ParametroSQL(PARAMTYPE_LONG, idLider));
			query.append(" and COD_FUNC_lider = ? ");
		}
		query.append(" ORDER BY nome_func                    ");
					
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<FuncionarioVO> lista = new ArrayList<FuncionarioVO>();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
		    
			while (rs.next()) {
				FuncionarioVO funcionarioVO = new  FuncionarioVO();
				funcionarioVO.setIdFuncionario(getLong(rs, "COD_FUNC_subordinado"));
				funcionarioVO.setNomeFuncionario(getString(rs, "nome_func"));
				lista.add(funcionarioVO);
			}
			return lista;
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de funcionarios subordinados ao id - " + idLider, e);
		} finally {
			closeStatementAndResultSet(stmt, rs, conn);
		}	
	}

	/**
	 * String[] funcVinculados,
			String idLider, UsuarioVO usuarioVO, String grupo
	 * @param integer
	 * @param integer2
	 * @param usuarioVO
	 * @param integer3
	 * @throws SQLException 
	 * @throws PersistenciaException 
	 */
	public void vinculaSubordinadosLider(Integer idSubordinado, Integer idLider,
			UsuarioVO usuarioVO, Integer grupo) throws SQLException, PersistenciaException {
		StringBuilder query =  new StringBuilder(
				"insert into SRV_AGRUPA_FUNCIONARIO (COD_FUNC_lider ,COD_FUNC_subordinado, COD_GRP_REM_VAR "
				+ ",dt_ocorrencia,cod_usuario) values (?,?,?,sysdate,?) ");
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int columnIndex = 1;
			setInteger(stmt, columnIndex++, idLider);
			setInteger(stmt, columnIndex++, idSubordinado);
			setInteger(stmt, columnIndex++, grupo);
			setInteger(stmt, columnIndex++, usuarioVO.getIdUsuario());
			stmt.executeUpdate();
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível salvar vinculo do subordinado  - " +idSubordinado+" ao lider -"+ idLider, e);
		} finally {
			closeStatementAndResultSet(stmt, rs, conn);
		}	
		
		
	}

	public void removeALLSubordinados(Integer idLider, Integer grupo) throws PersistenciaException {
		StringBuilder query =  new StringBuilder(
				" delete from SRV_AGRUPA_FUNCIONARIO where COD_FUNC_lider=? and COD_GRP_REM_VAR = ? ");
				
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			int columnIndex = 1;
			setInteger(stmt, columnIndex++, idLider);
			setInteger(stmt, columnIndex++, grupo);
			stmt.executeUpdate();
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível remover subordinados do grupo  - " +grupo+" ao lider -"+ idLider, e);
		} finally {
			closeStatementAndResultSet(stmt, rs, conn);
		}
		
	}

}
