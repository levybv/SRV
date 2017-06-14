package br.com.marisa.srv.funcionario.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.marisa.srv.funcionario.dao.HierarquiaDAO;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

public class HierarquiaBusiness {
	
	//Log4J
    private static final Logger log = Logger.getLogger(HierarquiaBusiness.class);

	//Instancia do Singleton
    private static HierarquiaBusiness instance = new HierarquiaBusiness();
    
    
    /**
     * Obtem uma instancia do objeto SalarioBaseBusiness
     * @return O objeto SalarioBaseBusiness
     */
    public static final HierarquiaBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private HierarquiaBusiness() {
    }
    
    /**
     * traz todos os usuario exceto demitidos 
     * em meses anteriores
     * @param idGrupo
     * @return
     * @throws PersistenciaException
     */
    public List<FuncionarioVO> obtemListaFuncionariosByIdGrupo(Integer idGrupo) throws PersistenciaException{
    	return FuncionarioBusiness.getInstance().obtemListaFuncionariosByIdGrupo(idGrupo, Constantes.DEMITIDOS_EM_MESES_ANTERIORES);
    }


	public List<FuncionarioVO> obtemListaSubordinados(Long idFuncionario, Integer grupo) throws PersistenciaException {
		HierarquiaDAO hierarquiaDAO = new HierarquiaDAO();
		return hierarquiaDAO.obtemListaSubordinados(idFuncionario,grupo);
	}


	public List<FuncionarioVO> obtemListaColaboradoresDisponiveis(
			boolean podeTerMaisDeUmChefe, List<FuncionarioVO> listaSubordinados, Integer grupo) throws PersistenciaException {
		List<FuncionarioVO> listaALLFuncionariosGrupo = obtemListaFuncionariosByIdGrupo(grupo);
		Map<Long , FuncionarioVO> mapaAllFuncionarios = new HashMap<Long, FuncionarioVO>();
		List<FuncionarioVO> listaFuncDesconsiderados = new ArrayList<FuncionarioVO>();
		for(FuncionarioVO funcionarioVO :listaALLFuncionariosGrupo){
			mapaAllFuncionarios.put(funcionarioVO.getIdFuncionario(), funcionarioVO);
		}
		// desconsiderar todos os funcionarios já vinculados
		listaFuncDesconsiderados.addAll(listaSubordinados);
		if(!podeTerMaisDeUmChefe){
			//desconciderar todos os demais funcionarios ja vinculados
			HierarquiaDAO hierarquiaDAO = new  HierarquiaDAO();
			listaFuncDesconsiderados.addAll(hierarquiaDAO.obtemListaSubordinados(null, grupo));
		}
		for(FuncionarioVO funcionarioVO :listaFuncDesconsiderados){
			mapaAllFuncionarios.remove(funcionarioVO.getIdFuncionario());
		}
		List<FuncionarioVO> lista = new ArrayList<FuncionarioVO>(mapaAllFuncionarios.values());
		Collections.sort(lista);
		return lista;
	}


	public void vinculaSubordinadosLider(String[] funcVinculados,
			String idLiderS, UsuarioVO usuarioVO, String grupoS) throws PersistenciaException {
		HierarquiaDAO hierarquiaDAO = new HierarquiaDAO();
		try {
			hierarquiaDAO.beginTrans();
			Integer idLider = new Integer(idLiderS);
			Integer idGrupo = new Integer(grupoS);
			hierarquiaDAO.removeALLSubordinados(idLider,idGrupo);
			if(funcVinculados != null){
				for(int i=0; i<funcVinculados.length;i++){
					hierarquiaDAO.vinculaSubordinadosLider(new Integer(funcVinculados[i]), idLider,usuarioVO, idGrupo);
				}
			}
			hierarquiaDAO.commitTrans();
		} catch (Exception e) {
			hierarquiaDAO.rollbackTrans();
			throw new PersistenciaException(log, "Não foi possível atualizar vinculo de subordinados ao lider - " + idLiderS, e);
		}finally{
			hierarquiaDAO.closeConnection();
		}
		
	}

}
