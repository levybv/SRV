package br.com.marisa.srv.funcionario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.funcionario.vo.SalarioBaseVO;
import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;


/**
 * Classe para tratar dos métodos de acesso a dados a salário base
 * 
 * @author Walter Fontes
 */
public class SalarioBaseDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(SalarioBaseDAO.class);
    
    /**
     * Obtém todos os salarios base do filtro informado
     * 
     * @param idFilial
     * @param idFuncionario
     * @param ano
     * @param mes
     * @return
     * @throws PersistenciaException
     */
	public List obtemSalariosBase(Integer idFilial, Long idFuncionario, String nomeFuncionario, Integer ano, Integer mes, Boolean salarioBase) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"SELECT A.COD_EMP, A.COD_FIL, A.COD_FUNC, A.NUM_ANO, A.NUM_MES, A.VLR_SAL_BASE_REM_VAR, " +
				"       A.DT_INI_SIT_SRV, A.COD_USUARIO, B.DESCR_FIL, C.NOME_FUNC 						" +
				"  FROM SRV_FUNC_BASE_REM_VAR A, SRV_FILIAL B, SRV_FUNCIONARIO C						" +
				" WHERE A.COD_EMP  = B.COD_EMP															" +
				"   AND A.COD_FIL  = B.COD_FIL															" +
				"   AND A.COD_FUNC = C.COD_FUNC 														" );
		
		List parametros = new ArrayList();
		if (idFilial != null) {
			query.append(" AND A.COD_FIL = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idFilial));
		}
		if (idFuncionario != null) {
			query.append(" AND A.COD_FUNC = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario));
		}
		if (nomeFuncionario != null) {
			query.append(" AND UPPER(TRIM(C.NOME_FUNC)) LIKE '%'||?||'%' ");
			parametros.add(new ParametroSQL(PARAMTYPE_STRING, nomeFuncionario.trim().toUpperCase()));
		}
		if (ano != null) {
			query.append(" AND A.NUM_ANO = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		}
		if (mes != null) {
			query.append(" AND A.NUM_MES = ? ");
			parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		}
		if (salarioBase != null) {
			if (salarioBase.booleanValue()) {
				query.append(" AND (A.VLR_SAL_BASE_REM_VAR is not null AND A.VLR_SAL_BASE_REM_VAR > 0) ");
			} else {
				query.append(" AND (A.VLR_SAL_BASE_REM_VAR is null OR A.VLR_SAL_BASE_REM_VAR = 0) ");
			}
		}
		
		query.append(" ORDER BY A.NUM_ANO, A.NUM_MES, C.NOME_FUNC, A.COD_FIL ");

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List lista = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
		    
			while (rs.next()) {
				SalarioBaseVO salarioBaseVO = new SalarioBaseVO();
				salarioBaseVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				salarioBaseVO.setIdFilial(getInteger(rs, "COD_FIL"));
				salarioBaseVO.setDescricaoFilial(getString(rs, "DESCR_FIL"));
				salarioBaseVO.setIdFuncionario(getLong(rs, "COD_FUNC"));
				salarioBaseVO.setNomeFuncionario(getString(rs, "NOME_FUNC"));
				salarioBaseVO.setAno(getInteger(rs, "NUM_ANO"));
				salarioBaseVO.setMes(getInteger(rs, "NUM_MES"));
				salarioBaseVO.setSalarioBase(getDouble(rs, "VLR_SAL_BASE_REM_VAR"));
				salarioBaseVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				salarioBaseVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
				lista.add(salarioBaseVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter a lista de salários base.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}
	
	
    /**
     * Obtém as filiais de um funcionário
     * 
     * @param idFuncionario
     * @return
     * @throws PersistenciaException
     */
	public List obtemIdsFiliais(Long idFuncionario) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"SELECT COD_FIL					      " +
				"  FROM SRV_FUNC_BASE_REM_VAR	      " +
				" WHERE COD_FUNC = ? 			      " +
				"   AND (VLR_SAL_BASE_REM_VAR is null " +
				"    OR  VLR_SAL_BASE_REM_VAR = 0)    " +
				" ORDER BY COD_FIL                    ");
		
		List parametros = new ArrayList();
		parametros.add(new ParametroSQL(PARAMTYPE_LONG, idFuncionario));

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List lista = new ArrayList();
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
		    
			while (rs.next()) {
				lista.add(getInteger(rs, "COD_FIL"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter as filiais do funcionario " + idFuncionario, e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return lista;
	}
		
	
	
    /**
     * Obtém o salário base da chave informada
     * 
     * @param idEmpresa
     * @param idFilial
     * @param idFuncionario
     * @param ano
     * @param mes 
     * @return
     * @throws PersistenciaException
     */
	public SalarioBaseVO obtemSalarioBase(Integer idEmpresa, Integer idFilial, Long idFuncionario, Integer ano, Integer mes) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				"SELECT A.COD_EMP, A.COD_FIL, A.COD_FUNC, A.NUM_ANO, A.NUM_MES, A.VLR_SAL_BASE_REM_VAR, " +
				"       A.DT_INI_SIT_SRV, A.COD_USUARIO, B.DESCR_FIL, C.NOME_FUNC 						" +
				"  FROM SRV_FUNC_BASE_REM_VAR A, SRV_FILIAL B, SRV_FUNCIONARIO C						" +
				" WHERE A.COD_EMP  	= B.COD_EMP															" +
				"   AND A.COD_FIL  	= B.COD_FIL															" +
				"   AND A.COD_FUNC 	= C.COD_FUNC 														" +
				"   AND A.COD_EMP 	= ? 																" +
				"   AND A.COD_FIL 	= ? 																" +
				" 	AND A.COD_FUNC 	= ? 																" +
				" 	AND A.NUM_ANO 	= ? 																" +
				" 	AND A.NUM_MES 	= ? 																" );

		List parametros = new ArrayList();
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idEmpresa));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idFilial));
		parametros.add(new ParametroSQL(PARAMTYPE_LONG,    idFuncionario));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano));
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, mes));
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		SalarioBaseVO salarioBaseVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
		    
			if (rs.next()) {
				salarioBaseVO = new SalarioBaseVO();
				salarioBaseVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				salarioBaseVO.setIdFilial(getInteger(rs, "COD_FIL"));
				salarioBaseVO.setDescricaoFilial(getString(rs, "DESCR_FIL"));
				salarioBaseVO.setIdFuncionario(getLong(rs, "COD_FUNC"));
				salarioBaseVO.setNomeFuncionario(getString(rs, "NOME_FUNC"));
				salarioBaseVO.setAno(getInteger(rs, "NUM_ANO"));
				salarioBaseVO.setMes(getInteger(rs, "NUM_MES"));
				salarioBaseVO.setSalarioBase(getDouble(rs, "VLR_SAL_BASE_REM_VAR"));
				salarioBaseVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				salarioBaseVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter um salário base.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return salarioBaseVO;
	}	


	/**
	 * Incluir salário base
	 * 
	 * @param salarioBaseVO
	 * @return
	 * @throws PersistenciaException
	 */
	public void incluiSalarioBase(SalarioBaseVO salarioBaseVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_FUNC_BASE_REM_VAR (                           " +
                "        COD_EMP, COD_FIL, COD_FUNC, NUM_ANO, NUM_MES, 			" +
                "        VLR_SAL_BASE_REM_VAR, DT_INI_SIT_SRV, COD_USUARIO)     " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?)                               "); 
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	salarioBaseVO.getIdEmpresa())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	salarioBaseVO.getIdFilial())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_LONG, 	salarioBaseVO.getIdFuncionario())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	salarioBaseVO.getAno())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	salarioBaseVO.getMes())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE, 	salarioBaseVO.getSalarioBase())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE, 	salarioBaseVO.getDataUltimaAlteracao())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, 	salarioBaseVO.getIdUsuarioUltimaAlteracao())); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
	    	System.out.println(">"+salarioBaseVO.getIdEmpresa()+">"+ 
	    	salarioBaseVO.getIdFilial()+">"+
	    	salarioBaseVO.getIdFuncionario()+">"+ 
	    	salarioBaseVO.getAno()+">"+
	    	salarioBaseVO.getMes()+ ">"+
	    	salarioBaseVO.getSalarioBase()+">"+ 
	    	salarioBaseVO.getDataUltimaAlteracao()+">"+ 
	    	salarioBaseVO.getIdUsuarioUltimaAlteracao()); 
			throw new PersistenciaException(log, "Nao foi possível incluir salario base.", e);
		} finally {
			closeStatement(stmt);
		}
	}	
	
	
	/**
	 * Excluir salário base
	 * 
	 * @param idEmpresa
	 * @param idFilial
	 * @param idFuncionario
	 * @param ano
	 * @param mes
	 * @return
	 * @throws PersistenciaException
	 */
	public void excluiSalarioBase(Integer idEmpresa, Integer idFilial, Long idFuncionario, Integer ano, Integer mes) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" DELETE FROM SRV_FUNC_BASE_REM_VAR " +
				"  WHERE COD_EMP   = ?    			" +
				"    AND COD_FIL   = ?    			" +
				"    AND COD_FUNC  = ?    			" +
				"    AND NUM_ANO   = ?    			" +
				"    AND NUM_MES   = ?    			");
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idEmpresa)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idFilial)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_LONG,    idFuncionario)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, mes)); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível excluir salario base.", e);
		} finally {
			closeStatement(stmt);
		}
	}		
	
	
	/**
	 * Incluir histórico de salario base
	 * 
	 * @param salarioBaseVO
	 * @return
	 * @throws PersistenciaException
	 */
	public void incluiSalarioBaseHistorico(SalarioBaseVO salarioBaseVO) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" INSERT INTO SRV_FUNC_BASE_REM_VAR_HIST (                           		" +
                "        COD_EMP, COD_FIL, COD_FUNC, NUM_ANO, NUM_MES, SALARIO_BASE_SEQ,	" +
                "        VLR_SAL_BASE_REM_VAR, DT_INI_SIT_SRV, COD_USUARIO)     			" +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)                               			"); 
				 
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, salarioBaseVO.getIdEmpresa())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, salarioBaseVO.getIdFilial())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_LONG,    salarioBaseVO.getIdFuncionario())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, salarioBaseVO.getAno())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, salarioBaseVO.getMes())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, new Integer(obtemProximaSequenciaHistorio(salarioBaseVO.getIdEmpresa(), salarioBaseVO.getIdFilial(), salarioBaseVO.getIdFuncionario(), salarioBaseVO.getAno(), salarioBaseVO.getMes())))); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DOUBLE,  salarioBaseVO.getSalarioBase())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_DATE,    salarioBaseVO.getDataUltimaAlteracao())); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, salarioBaseVO.getIdUsuarioUltimaAlteracao())); 
		
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			stmt.executeUpdate();
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível incluir historico de salario base.", e);
		} finally {
			closeStatement(stmt);
		}
	}		


    /**
     * Obtém próxima sequencia do histórico
     * 
     * @param idEmpresa
     * @param idFilial
     * @param idFuncionario
     * @param ano
     * @param mes
     * @return
     */
    private int obtemProximaSequenciaHistorio(Integer idEmpresa, Integer idFilial, Long idFuncionario, Integer ano, Integer mes) throws PersistenciaException {

        String query =  " SELECT MAX(SALARIO_BASE_SEQ)  		" +
                        "   FROM SRV_FUNC_BASE_REM_VAR_HIST   	" +
						"  WHERE COD_EMP   = ?    				" +
						"    AND COD_FIL   = ?    				" +
						"    AND COD_FUNC  = ?    				" +
						"    AND NUM_ANO   = ?    				" +
						"    AND NUM_MES   = ?    				";            
						
		List parametros = new ArrayList();
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idEmpresa)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idFilial)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_LONG,    idFuncionario)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, ano)); 
    	parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, mes)); 						
						
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
            throw new PersistenciaException(log, "Não foi possível obter a proxima sequencia de historico de salario base", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return proximaSequencia;
    }

    public Integer obterCodigoFilialFuncionario(Long idFuncionario) throws PersistenciaException {

    	String query = "SELECT COD_FIL FROM SRV_FUNCIONARIO WHERE COD_FUNC = ?";
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Integer idFilialFuncionario = null;

		try {
			con = getConn();
			stmt = con.prepareStatement(query);
			setLong(stmt, 1, idFuncionario);
			rs = stmt.executeQuery();

			if (rs.next()) {
				idFilialFuncionario = getInteger(rs, "COD_FIL");
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível pesquisar filial de funcionário.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}

		return idFilialFuncionario;

    }

	public SalarioBaseVO buscarSalarioBase(Long idFuncionario, Integer mes, Integer ano) throws PersistenciaException {
		SalarioBaseVO salarioVO = new SalarioBaseVO();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM SRV_FUNC_BASE_REM_VAR WHERE COD_FUNC = ? AND NUM_MES = ? AND NUM_ANO = ?";

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			
			setLong(stmt, 1, idFuncionario);
			setInteger(stmt, 2, mes);
			setInteger(stmt, 3, ano);
			
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				salarioVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				salarioVO.setIdFilial(getInteger(rs, "COD_FIL"));
				salarioVO.setIdFuncionario(getLong(rs, "COD_FUNC"));
				salarioVO.setAno(getInteger(rs, "NUM_ANO"));
				salarioVO.setMes(getInteger(rs, "NUM_MES"));
				salarioVO.setSalarioBase(getDouble(rs, "VLR_SAL_BASE_REM_VAR"));
				salarioVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				salarioVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
			}
		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter o salário base.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return salarioVO;
	}

	public void alterarSalarioBase(SalarioBaseVO salarioBaseVO) throws PersistenciaException {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		String query = "UPDATE SRV_FUNC_BASE_REM_VAR SET COD_FIL  = ? ,VLR_SAL_BASE_REM_VAR = ?, DT_INI_SIT_SRV = ? WHERE COD_FUNC = ? AND NUM_MES = ? AND NUM_ANO = ?";

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query);
			
			setInteger(stmt, 1, salarioBaseVO.getIdFilial());
			setDouble(stmt, 2, salarioBaseVO.getSalarioBase());
			setDate(stmt, 3, salarioBaseVO.getDataUltimaAlteracao());
			setLong(stmt, 4, salarioBaseVO.getIdFuncionario());
			setInteger(stmt, 5, salarioBaseVO.getMes());
			setInteger(stmt, 6, salarioBaseVO.getAno());
			
			stmt.executeUpdate();

		} catch (Exception e) {
	    	System.out.println(">"+salarioBaseVO.getIdFilial()+">"+salarioBaseVO.getSalarioBase()+">"+salarioBaseVO.getDataUltimaAlteracao()+">"+
	    	salarioBaseVO.getIdFuncionario()+">"+salarioBaseVO.getMes()+">"+salarioBaseVO.getAno()); 
			throw new PersistenciaException(log, "Nao foi possível alterar salário base.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
	}

	public SalarioBaseVO buscarSalarioBaseParaAgrupamentoFilial(Long idFuncionario, Integer mes, Integer ano, Integer idFilial) throws PersistenciaException {
		SalarioBaseVO salarioVO = new SalarioBaseVO();
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM SRV_FUNC_BASE_REM_VAR WHERE COD_FUNC = ? AND NUM_MES = ? AND NUM_ANO = ? AND COD_FIL = ?";

		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			
			setLong(stmt, 1, idFuncionario);
			setInteger(stmt, 2, mes);
			setInteger(stmt, 3, ano);
			setInteger(stmt, 4, idFilial);
			
			rs = stmt.executeQuery();
			
			if (rs.next()) {
				salarioVO.setIdEmpresa(getInteger(rs, "COD_EMP"));
				salarioVO.setIdFilial(getInteger(rs, "COD_FIL"));
				salarioVO.setIdFuncionario(getLong(rs, "COD_FUNC"));
				salarioVO.setAno(getInteger(rs, "NUM_ANO"));
				salarioVO.setMes(getInteger(rs, "NUM_MES"));
				salarioVO.setSalarioBase(getDouble(rs, "VLR_SAL_BASE_REM_VAR"));
				salarioVO.setDataUltimaAlteracao(getTimestamp(rs, "DT_INI_SIT_SRV"));
				salarioVO.setIdUsuarioUltimaAlteracao(getInteger(rs, "COD_USUARIO"));
			}

		} catch (Exception e) {
			throw new PersistenciaException(log, "Não foi possível obter o salário base.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return salarioVO;
	}

}