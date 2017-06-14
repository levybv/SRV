package br.com.marisa.srv.escala.business;

import br.com.marisa.srv.escala.vo.FaixaEscalaVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os métodos de negócio do módulo de Faixas de Escala Ajax
 * 
 * @author Walter Fontes
 */
public class FaixaEscalaBusinessAjax {
    
	
    /**
     * Pesquisa Faixa da Escala
     * 
     * @param idEscala
     * @param sequencial
     * @return
     */
    public FaixaEscalaVO obtemFaixaEscala(Integer idEscala, Integer sequencial) throws SRVException {
        return FaixaEscalaBusiness.getInstance().obtemFaixaEscala(idEscala, sequencial);
    }    

}