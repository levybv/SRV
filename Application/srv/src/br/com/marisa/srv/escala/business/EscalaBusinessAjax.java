package br.com.marisa.srv.escala.business;

import java.util.List;

import br.com.marisa.srv.escala.vo.EscalaVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os métodos de negócio do módulo de Escala Ajax
 * 
 * @author Walter Fontes
 */
public class EscalaBusinessAjax {
    
	/**
	 * 
	 * @param idEscala
	 * @return
	 * @throws SRVException
	 */
    public EscalaVO obtemEscala(Integer idEscala) throws SRVException {
        return EscalaBusiness.getInstance().obtemEscala(idEscala);
    }

    /**
     * 
     * @return
     * @throws SRVException
     */
    public List<EscalaVO> obtemEscalas() throws SRVException {
        return EscalaBusiness.getInstance().obtemEscalas(null, null, null);
    }

    /**
     * 
     * @return
     * @throws SRVException
     */
    public List<EscalaVO> obtemListaEscalaBonus() throws SRVException {
        return EscalaBusiness.getInstance().obtemListaEscalaBonus();
    }

}