package br.com.marisa.srv.demissao.business;

import org.apache.log4j.Logger;

import br.com.marisa.srv.demissao.dao.MotivoDemissaoDAO;
import br.com.marisa.srv.demissao.vo.MotivoDemissaoVO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;

public class MotivoDemissaoBusiness {

	private static final Logger log = Logger.getLogger(MotivoDemissaoBusiness.class);

	private static MotivoDemissaoBusiness instance = new MotivoDemissaoBusiness();

	public static final MotivoDemissaoBusiness getInstance() {
		return instance;
	}

	private MotivoDemissaoBusiness() {}

	public MotivoDemissaoVO obtemMotivoDemissao(Integer codDemissao) throws PersistenciaException {
		MotivoDemissaoDAO motivoDemissaoDAO = new MotivoDemissaoDAO();
		try {
			return motivoDemissaoDAO.obtemMotivoDemissao(codDemissao);	
		} finally {
			motivoDemissaoDAO.closeConnection();
		}
	}

}