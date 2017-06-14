package br.com.marisa.srv.ponderacao.business;

import java.util.List;

import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.ponderacao.vo.PonderacaoVO;

public class PonderacaoBusinessAjax {

	public PonderacaoVO obtemPonderacao(Integer idPonderacao) throws SRVException {

		return PonderacaoBusiness.getInstance().obtemPonderacao(idPonderacao);
	}

	public List obtemListaFiliais() throws PersistenciaException {

		return PonderacaoBusiness.getInstance().obtemListaFiliais();
	}
}