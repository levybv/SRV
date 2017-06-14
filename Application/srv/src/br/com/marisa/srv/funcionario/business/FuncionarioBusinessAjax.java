package br.com.marisa.srv.funcionario.business;

import java.util.List;

import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Obtém Classe para consultas Ajax de funcionários]
 * 
 * @author Walter Fontes
 */
public class FuncionarioBusinessAjax {
    
	
	/**
	 * Obtém lista de indicadores
	 * 
	 * @param idGrupo
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
    public List obtemLideres(String idGrupo) throws NumberFormatException, SRVException {
    	
    	List lideres = null;
    	if ("2".equalsIgnoreCase(idGrupo)) {
    		lideres = FuncionarioBusiness.getInstance().obtemLideres("LIDERANCA_LOJA");
    	} else if ("3".equalsIgnoreCase(idGrupo)) {
    		lideres = FuncionarioBusiness.getInstance().obtemLideres("LIDERANCA_LOJA");
    	} else if ("4".equalsIgnoreCase(idGrupo)) {
    		lideres = FuncionarioBusiness.getInstance().obtemLideres("LIDERANCA_SAX_EP");
    	} else if ("5".equalsIgnoreCase(idGrupo)) {
    		lideres = FuncionarioBusiness.getInstance().obtemLideres("LIDERANCA_SAX_SEGURO (EP)");
    	} else if ("6".equalsIgnoreCase(idGrupo)) {
    		lideres = FuncionarioBusiness.getInstance().obtemLideres("LIDERANCA_CALL_CENTER");
    	}
    	return lideres;
    }
    
    
	/**
	 * Verifica se determinado funcionario existe na base
	 * 
	 * @param idFuncionario
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
    public Boolean funcionarioExiste(Long idFuncionario) throws NumberFormatException, SRVException {
    	return new Boolean(FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionario) != null);
    }    
    
    
	/**
	 * Obtém funcionario
	 * 
	 * @param idFuncionario
	 * @return
	 * @throws NumberFormatException
	 * @throws SRVException
	 */
    public FuncionarioVO obtemFuncionario(Long idFuncionario) throws NumberFormatException, SRVException {
    	return FuncionarioBusiness.getInstance().obtemFuncionario(idFuncionario);
    }
}