package br.com.marisa.srv.log.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.dao.BasicDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.log.vo.LogProcessoVO;

public class LogProcessoDAO extends BasicDAO {

	private static final Logger log = Logger.getLogger(LogProcessoDAO.class);

	public LogProcessoDAO() {
		super();
	}

	public List<LogProcessoVO> obtemTodosLogsProcesso() throws PersistenciaException {

		List<LogProcessoVO> listaLogProcessos = new ArrayList<LogProcessoVO>();
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer query = new StringBuffer(
        		"select a.cod_proc, a.descr_proc " +
				"  from srv_processo a " +
				" where a.flg_ativ_proc = 'S' " +
				" order by a.ordem_proc ");

        try{
        	conn = getConn();
        	stmt = conn.prepareStatement(query.toString());
        	LogProcessoVO logProcessoVO;
        	for (rs = stmt.executeQuery(); rs.next(); listaLogProcessos.add(logProcessoVO)) {
        		logProcessoVO = new LogProcessoVO();
        		logProcessoVO.setCodProc(getInteger(rs, "cod_proc"));
        		logProcessoVO.setDescrProc(getString(rs, "descr_proc"));
        	}
        } catch(Exception e) {
        	throw new PersistenciaException(log, "Nao foi possivel obter lista de Logs de Processos ", e);
        } finally {
        	closeStatementAndResultSet(stmt, rs);
        }
        return listaLogProcessos;
    }

    public List<LogProcessoVO> pesquisaLogProcesso(Date data, Integer codProcesso) throws PersistenciaException {

    	List<LogProcessoVO> listaLogProcessos = new ArrayList<LogProcessoVO>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        StringBuffer query = new StringBuffer(
        		"select a.num_ano as ano_proc, " +
				"       a.num_mes as mes_proc, " +
				"       a.dt_proc_ini as dt_inicio, " +
				"       a.dt_proc_fim as dt_fim, " +
				"       decode(a.sta_proc_exec, 0, 'nao processado', 1, 'processado ok', 2, 'processado com erro', a.sta_proc_exec) as stat_proc, " +
				"       a.obs as obs_exec, " +
				"       b.cod_fil as fil_log, " +
				"       b.cod_func as func_log, " +
				"       b.cod_indic as indic_log, " +
				"       b.erro as erro_log " +
				"  from srv_processo_exec a, srv_processo_log_erro b " +
				" where a.cod_proc = b.cod_proc(+) " +
				"   and a.num_ano = b.num_ano(+) " +
				"   and a.num_mes = b.num_mes(+) " +
				"   and trunc(a.dt_proc_ini) = trunc(b.dt_log(+)) " +
				"   and trunc(a.dt_proc_ini) = ? " +
				"   and a.cod_proc = ? " +
				" order by a.num_ano, a.num_mes, a.dt_proc_ini, b.num_ano,  " +
				" b.num_mes, b.cod_fil, b.cod_func, b.cod_indic ");

        try {
            conn = getConn();
            stmt = conn.prepareStatement(query.toString());
            setDate(stmt, 1, data);
            setInteger(stmt, 2, codProcesso);
            LogProcessoVO logProcessoVO;
            for(rs = stmt.executeQuery(); rs.next(); listaLogProcessos.add(logProcessoVO)) {
                logProcessoVO = new LogProcessoVO();
                logProcessoVO.setAnoProc(getInteger(rs, "ano_proc"));
                logProcessoVO.setMesProc(getInteger(rs, "mes_proc"));
                logProcessoVO.setDtInicio(getTimestamp(rs, "dt_inicio"));
                logProcessoVO.setDtFim(getTimestamp(rs, "dt_fim"));
                logProcessoVO.setStatProcStr(getString(rs, "stat_proc"));
                logProcessoVO.setObsExec(getString(rs, "obs_exec"));
                logProcessoVO.setFilLog(getInteger(rs, "fil_log"));
                logProcessoVO.setFuncLog(getInteger(rs, "func_log"));
                logProcessoVO.setIndicLog(getInteger(rs, "indic_log"));
                logProcessoVO.setErroLog(getString(rs, "erro_log"));
            }

        } catch(Exception e) {
            throw new PersistenciaException(log, "Nao foi possivel obter lista de Logs de Processos ", e);
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return listaLogProcessos;
    }

}
