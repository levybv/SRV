package br.com.marisa.srv.perfil.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.acesso.dao.AcessoDAO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.indicador.dao.IndicadorDAO;
import br.com.marisa.srv.perfil.dao.PerfilDAO;
import br.com.marisa.srv.perfil.vo.PerfilVO;

/**
 * Classe para conter os métodos de negócio do módulo de Usuários 
 * 
 * @author Walter Fontes
 */
public class PerfilBusiness {
    
    //Log4J
    private static final Logger log = Logger.getLogger(PerfilBusiness.class);    

    
    //Instancia do Singleton
    private static PerfilBusiness instance = new PerfilBusiness();
    
    
    /**
     * Obtem uma instancia do objeto UsuarioBusiness
     * @return O objeto UsuarioBusiness
     */
    public static final PerfilBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private PerfilBusiness() {
        // vazio
    }

    /**
     * 
     * @param pesquisaVO
     * @return
     * @throws SRVException
     */
	public List<PerfilVO> obtemListaPerfil(PerfilVO pesquisaVO) throws SRVException{
		PerfilDAO perfilDAO = new PerfilDAO();
		try {
			return perfilDAO.obtemListaPerfil(pesquisaVO);
		}
		finally {
			perfilDAO.closeConnection();
		}
	}

    /**
     * 
     * @param descricao
     * @return
     * @throws SRVException
     */
	public List<PerfilVO> obtemListaPerfisByDescricao(String descricao) throws SRVException{
		PerfilDAO perfilDAO = new PerfilDAO();
		try {
			return perfilDAO.obtemListaPerfisByDescricao(descricao);
		}
		finally {
			perfilDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param codPerFil
	 * @return
	 * @throws PersistenciaException
	 */
	public PerfilVO obtemPerfilByCod(Integer codPerFil) throws PersistenciaException {
		PerfilDAO perfilDAO = new PerfilDAO();
		try {
			return perfilDAO.obtemPerfilByCod(codPerFil);
		}finally {
			perfilDAO.closeConnection();
		}
	}

	/**
	 * 
	 * @param perfilVO
	 * @param idUsuario
	 * @param acessos
	 * @throws SRVException
	 */
	public void atualizarPerfil(PerfilVO perfilVO, Integer idUsuario, String[] acessos) throws SRVException {
		PerfilDAO perfilDAO = new PerfilDAO();
		Map mapaAcessos = obtemAcessosFromTela(perfilVO.getIdPerfil(),acessos);
		Map mapaAcessosRemovidos = obtemListaAcessosRemovidos(perfilVO.getIdPerfil(),mapaAcessos);
		try {
			perfilDAO.beginTrans();
			AcessoDAO acessoDAO = new AcessoDAO();
			acessoDAO.setConn(perfilDAO.getConn());
			if(!mapaAcessosRemovidos.isEmpty()) {
				acessoDAO.salvarHistorico(mapaAcessosRemovidos, perfilVO.getIdPerfil(),idUsuario);
			}
			acessoDAO.removeTodosAcessosPerfil(perfilVO.getIdPerfil());
			acessoDAO.salvarAcessos(mapaAcessos, perfilVO.getIdPerfil(),idUsuario);
			PerfilVO perfilAnterior = obtemPerfilByCod(perfilVO.getIdPerfil());
			perfilDAO.salvaPerfilHist(perfilAnterior,idUsuario);
			perfilDAO.atualizarPerfil(perfilVO);
			perfilDAO.commitTrans();
		}catch (Exception e) {
			perfilDAO.rollbackTrans();
			throw new SRVException(log, "Ocorreu erro na alteracao perfil", e);
		} finally {
			perfilDAO.closeConnection();
		}
		
	}

	/**
	 * 
	 * @param idPerfil
	 * @param mapaAcessosNew
	 * @return
	 * @throws SRVException
	 */
	private Map obtemListaAcessosRemovidos(Integer idPerfil, Map mapaAcessosNew) throws SRVException {
		Map mapaAcessosRemovidos = new HashMap();
		Map mapaAcessosOld = AcessoBusiness.getInstance().obtemAcessoPerfil(idPerfil.intValue());
		Iterator it = mapaAcessosOld.keySet().iterator();
		while (it.hasNext()) {
			Integer idFuncionalidade = (Integer)it.next();
			List listaAcessosOld = (List)mapaAcessosOld.get(idFuncionalidade);
			if(mapaAcessosNew.containsKey(idFuncionalidade)) {
				List listaNova = (List)mapaAcessosNew.get(idFuncionalidade);
				for (int i=0;i<listaAcessosOld.size();i++) {
					if(!listaNova.contains((Integer)listaAcessosOld.get(i))) {
						adicionaMapa(idFuncionalidade,mapaAcessosRemovidos, (Integer)listaAcessosOld.get(i));
					}
				}
			}else {
				mapaAcessosRemovidos.put(idFuncionalidade,listaAcessosOld);
			}
		}
		return mapaAcessosRemovidos;
	}

	/**
	 * 
	 * @param idFuncionalidade
	 * @param mapaAcessosRemovidos
	 * @param acesso
	 */
	private void adicionaMapa(Integer idFuncionalidade, Map mapaAcessosRemovidos, Integer acesso) {
		if (mapaAcessosRemovidos.containsKey(idFuncionalidade)) {
			List tiposAcessos = (List)mapaAcessosRemovidos.get(idFuncionalidade);
			tiposAcessos.add(acesso);
			mapaAcessosRemovidos.put(idFuncionalidade, tiposAcessos);
		} else {
			List tiposAcessos = new ArrayList();
			tiposAcessos.add(acesso);
			mapaAcessosRemovidos.put(idFuncionalidade, tiposAcessos);
		}
	}

	/**
	 * 
	 * @param idPerfil
	 * @param acessos
	 * @return
	 */
	private Map obtemAcessosFromTela(Integer idPerfil, String[] acessos) {
		Map mapaAcessos = new HashMap();
		if(acessos != null) {
			for(int i=0;i<acessos.length;i++) {
				String acesso[] = acessos[i].split("@");
				Integer idFuncionalidade = new Integer(acesso[0]);
				if (mapaAcessos.containsKey(idFuncionalidade)) {
	        		List tiposAcessos = (List)mapaAcessos.get(idFuncionalidade);
	        		tiposAcessos.add(new Integer(acesso[1]));
	        		mapaAcessos.put(idFuncionalidade, tiposAcessos);
	        	} else {
	        		List tiposAcessos = new ArrayList();
	        		tiposAcessos.add(new Integer(acesso[1]));
	        		mapaAcessos.put(idFuncionalidade, tiposAcessos);
	        	}
			}
		}
		return mapaAcessos;
	}

	/**
	 * 
	 * @param codPerfil
	 * @return
	 * @throws SRVException
	 */
    public boolean isUsuarioPodeReabrirBonus(Integer codPerfil) throws SRVException {
		PerfilDAO perfilDAO = new PerfilDAO();
		try {
			return perfilDAO.isUsuarioPodeReabrirBonus(codPerfil);
		} catch (Exception e) {
			throw new SRVException(log, "Ocorreu erro ao verificar se usuario possui permissão para reabrir bonus", e);
		} finally {
			perfilDAO.closeConnection();
		}
	}

}
