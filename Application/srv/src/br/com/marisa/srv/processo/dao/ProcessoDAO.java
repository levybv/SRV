package br.com.marisa.srv.processo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.dao.ParametroSQL;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.processo.vo.ProcessoVO;


/**
 * Classe para tratar dos métodos de acesso a dados de Processo
 * 
 * @author Walter Fontes
 */
public class ProcessoDAO extends BasicDAO {

    //Log4J
    private static final Logger log = Logger.getLogger(ProcessoDAO.class);


	/**
	 * Obtém lista de processos ativos
	 * 
	 * @return
	 * @throws PersistenciaException
	 */
	public List obtemProcessosAtivos() throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT COD_PROC, ORDEM_PROC, NOME_PROC,  	 " +
				"        DESCR_PROC, FLG_ATIV_PROC, DT_PROC  " +
				"   FROM SRV_PROCESSO 						 " +
				"  WHERE FLG_ATIV_PROC 	= ?					 ");
		
		List parametros = new ArrayList();
		parametros.add(new ParametroSQL(PARAMTYPE_BOOLEAN, Boolean.TRUE));
				
		query.append(" ORDER BY COD_PROC ");
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List processos = new ArrayList(); 
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
			while (rs.next()) {
				ProcessoVO processoVO = new ProcessoVO();
				processoVO.setIdProcesso(getInteger(rs, "COD_PROC"));
				processoVO.setOrdem(getInteger(rs, "ORDEM_PROC"));
				processoVO.setNome(getString(rs, "NOME_PROC"));
				processoVO.setDescricao(getString(rs, "DESCR_PROC"));
				processoVO.setAtivo(getBoolean(rs, "FLG_ATIV_PROC"));
				processoVO.setDataCriacaoProcesso(getTimestamp(rs, "DT_PROC"));
				processos.add(processoVO);
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter a lista de processos.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return processos;
	}
	
	
	/**
	 * Obtém processo
	 * 
	 * @param idProcesso
	 * @return
	 * @throws PersistenciaException
	 */
	public ProcessoVO obtemProcesso(Integer idProcesso) throws PersistenciaException {
		
		StringBuffer query =  new StringBuffer(
				" SELECT COD_PROC, ORDEM_PROC, NOME_PROC,  	 " +
				"        DESCR_PROC, FLG_ATIV_PROC, DT_PROC  " +
				"   FROM SRV_PROCESSO 						 " +
				"  WHERE COD_PROC 	= ?					 	 ");
		
		List parametros = new ArrayList();
		parametros.add(new ParametroSQL(PARAMTYPE_INTEGER, idProcesso));
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		ProcessoVO processoVO = null;
		
		try {
			conn = getConn();
			stmt = conn.prepareStatement(query.toString());
			preencheParametros(stmt, parametros);
			rs = stmt.executeQuery();
			if (rs.next()) {
				processoVO = new ProcessoVO();
				processoVO.setIdProcesso(getInteger(rs, "COD_PROC"));
				processoVO.setOrdem(getInteger(rs, "ORDEM_PROC"));
				processoVO.setNome(getString(rs, "NOME_PROC"));
				processoVO.setDescricao(getString(rs, "DESCR_PROC"));
				processoVO.setAtivo(getBoolean(rs, "FLG_ATIV_PROC"));
				processoVO.setDataCriacaoProcesso(getTimestamp(rs, "DT_PROC"));
			}
		
		} catch (Exception e) {
			throw new PersistenciaException(log, "Nao foi possível obter o processo.", e);
		} finally {
			closeStatementAndResultSet(stmt, rs);
		}
		return processoVO;
	}	
}