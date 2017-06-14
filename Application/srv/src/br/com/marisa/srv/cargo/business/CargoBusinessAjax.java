package br.com.marisa.srv.cargo.business;

import java.util.List;

import br.com.marisa.srv.cargo.vo.CargoVO;
import br.com.marisa.srv.geral.excecoes.SRVException;

/**
 * Classe para conter os métodos de negócio acessíveis por Ajax do módulo de Cargo
 * 
 * @author Walter Fontes
 */
public class CargoBusinessAjax {
	
	/**
	 * Obtém cargo
	 * 
	 * @param idCargoSrt
	 * @return
	 * @throws SRVException
	 */
	public CargoVO obtemCargo(String idCargoSrt) throws SRVException {
		Integer idCargo = new Integer(idCargoSrt);
		return CargoBusiness.getInstance().obtemCargo(idCargo.intValue());
	}
	
	/**
	 * Verifica se o codigo já existe
	 * 
	 * @param idCargoSrt
	 * @return
	 * @throws SRVException
	 */
	public boolean codigoJaExiste(String idCargoSrt) throws SRVException {
		Integer idCargo = new Integer(idCargoSrt);
		CargoVO cargoVO = CargoBusiness.getInstance().obtemCargo(idCargo.intValue());
		return (cargoVO != null);
	}
	
	
	/**
	 * Obtém ids dos grupos de remunerações de um cargo
	 * 
	 * @param idCargoSrt
	 * @return
	 * @throws SRVException
	 */
	public List obtemIdsGruposRemuneracao(String idCargoSrt) throws SRVException {
		Integer idCargo = new Integer(idCargoSrt);
		return CargoBusiness.getInstance().obtemIdsGruposRemuneracao(idCargo.intValue());
	}
	
	
	/**
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List<CargoVO> obtemCargos() throws SRVException {
		return CargoBusiness.getInstance().obtemListaCargo(null);
	}	
}