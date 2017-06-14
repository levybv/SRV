package br.com.marisa.srv.log.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.log.dao.LogProcessoDAO;
import br.com.marisa.srv.log.vo.LogProcessoVO;

public class LogProcessoBusiness {

	private static final Logger log = Logger.getLogger(LogProcessoBusiness.class);

	private static LogProcessoBusiness instance = new LogProcessoBusiness();

	private LogProcessoBusiness(){}

	public static final LogProcessoBusiness getInstance() {
		return instance;
	}

	public List<LogProcessoVO> obtemTodosLogsProcesso() throws SRVException {
		List<LogProcessoVO> listaLogProcessos = new ArrayList<LogProcessoVO>();
		LogProcessoDAO dao = new LogProcessoDAO();

		try {
			listaLogProcessos = dao.obtemTodosLogsProcesso();
		} catch(Exception e) {
			throw new SRVException("Nao foi possivel obter lista de log de processos.", e);
		} finally {
			dao.closeConnection();
		}

		return listaLogProcessos;
	}

	public List<LogProcessoVO> pesquisaLogProcesso(Date data, Integer codProcesso) throws SRVException {
		List<LogProcessoVO> listaLogProcessos = new ArrayList<LogProcessoVO>();
		LogProcessoDAO dao = new LogProcessoDAO();

		try {
			listaLogProcessos = dao.pesquisaLogProcesso(data, codProcesso);
		} catch(Exception e) {
			throw new SRVException("Nao foi possivel pesquisar log de processos.");
		} finally {
			dao.closeConnection();
		}

		return listaLogProcessos;
	}
}