package br.com.marisa.srv.filial.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import br.com.marisa.srv.cargo.dao.GrupoCargoDAO;
import br.com.marisa.srv.filial.dao.AgrupamentoFilialUsuarioDAO;
import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.funcionario.dao.FuncionarioDAO;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.parametros.dao.ParametroDAO;
import br.com.marisa.srv.usuario.vo.UsuarioVO;


	
	/**
	 * Classe para conter os métodos de negócio do módulo de Filiais 
	 * 
	 * @author Walter Fontes
	 */
	public class AgrupaFilialUsuarioBusiness {
	    
		
	    //Log4J
	    private static final Logger log = Logger.getLogger(AgrupaFilialUsuarioBusiness.class);    

	    private static AgrupaFilialUsuarioBusiness instance = new AgrupaFilialUsuarioBusiness();

	    /**
	     * Obtem uma instancia do objeto AgrupaFilialUsuarioBusiness
	     * @return O objeto AgrupaFilialUsuarioBusiness
	     */
	    public static final AgrupaFilialUsuarioBusiness getInstance() {
	        return instance;
	    }
	    

	    /**
	     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
	     */
	    private AgrupaFilialUsuarioBusiness() {
	    }


		public List<FuncionarioVO> obtemListaFuncionarioByArea(
				UsuarioVO usuarioVO, Map<String, String> mapa) throws PersistenciaException {
			int idGrupo = obtemIdGrupo(usuarioVO,mapa);
			if(idGrupo != -1){
				GrupoCargoDAO grupoCargoDAO = new  GrupoCargoDAO();
				List<Integer> listaCargos = grupoCargoDAO.obtemListaCargosByIdGrupo(idGrupo,Boolean.TRUE);
				List<FuncionarioVO> listaFuncionarioVOs = new ArrayList<FuncionarioVO>();
				FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
				for(Integer idCargo:listaCargos){
					listaFuncionarioVOs.addAll(funcionarioDAO.obtemListaFuncionario(null,null,null,null,idCargo,null,null, null));
				}
				Collections.sort(listaFuncionarioVOs);
				return listaFuncionarioVOs;
			}
			return null;
		}


		private int obtemIdGrupo(UsuarioVO usuarioVO, Map<String, String> mapa) {
			int DADOS_PSF = 1;
			int DADOS_VM = 0;
			int USUARIOS = 0;
			int GRUPO_REM_VAR =1;
			String valorParametro = mapa.get("SEPARADORES");
			String[] configuracaoGrupos = valorParametro.split("-");
			String[] grupoDadosPSF = configuracaoGrupos[DADOS_PSF].split(";");
			String[] grupoDadosVM = configuracaoGrupos[DADOS_VM].split(";");
			String[] usuariosPSF = grupoDadosPSF[USUARIOS].split(",");
			String[] usuariosVM = grupoDadosVM[USUARIOS].split(",");
			Integer grupoSPF = new Integer(grupoDadosPSF[GRUPO_REM_VAR]);
			Integer grupoVM = new Integer(grupoDadosVM[GRUPO_REM_VAR]);
			int idGrupo = -1;
			
			for(int i=0;i<usuariosPSF.length;i++){
				if(usuarioVO.getIdUsuario().intValue() == Integer.parseInt(usuariosPSF[i])){
					idGrupo = grupoSPF;
					break;
				}
			}
			if(idGrupo == -1){
				for(int i=0;i<usuariosVM.length;i++){
					if(usuarioVO.getIdUsuario().intValue() == Integer.parseInt(usuariosVM[i])){
						idGrupo = grupoVM;
						break;
					}
				}
			}
			return idGrupo;
		}


		public List<FilialVO> obtemListaFilialDisponivel(UsuarioVO usuarioVO, Map<String, String> mapa, Integer idFuncionario) throws PersistenciaException {
			AgrupamentoFilialUsuarioDAO agrupamentoFilialUsuarioDAO = new AgrupamentoFilialUsuarioDAO();
			int idGrupo = obtemIdGrupo(usuarioVO, mapa);
			List<FilialVO> listaJaUtilizada = null;
			List<FilialVO> listaTodasFiliais = FilialBusiness.getInstance().obtemTodasFiliais(Boolean.TRUE);
			Map<Integer, FilialVO> mapaTodasFiliaisInteiroDisponiveis= obtemMapaFilial(listaTodasFiliais);
			//if(idGrupo != Constantes.PSF){
				//listaJaUtilizada =  agrupamentoFilialUsuarioDAO.obtemListaFiliaisUtilizadasByIdGrupo(idGrupo,null);
			//}else{
			listaJaUtilizada =  agrupamentoFilialUsuarioDAO.obtemListaFiliaisUtilizadasByIdGrupo(idGrupo,idFuncionario);
			//}
			for(FilialVO filialVO:listaJaUtilizada){
				mapaTodasFiliaisInteiroDisponiveis.remove(filialVO.getCodFilial());
			}
			List<FilialVO> lista = new ArrayList<FilialVO>(mapaTodasFiliaisInteiroDisponiveis.values());
			Collections.sort(lista);
			return lista;
		}


		/**
		 * serve apenas para dar maior agilidade nas pesquisas retornando apenas inteiros
		 * @param listaTodasFiliais
		 * @return
		 */
		private Map<Integer, FilialVO> obtemMapaFilial(
				List<FilialVO> listaTodasFiliais) {
			Map<Integer,FilialVO>mapa = new HashMap<Integer,FilialVO>();
			for(FilialVO filialVO:listaTodasFiliais){
				mapa.put(filialVO.getCodFilial(),filialVO);
			}
			return mapa;
		}


		/**
		 * agrupa filiais a funcionarios responsaveis por elas
		 * @param filiais
		 * @param idFuncionario
		 * @param usuarioVO
		 * @throws SRVException
		 */
		public void incluiAgrupamentoFilial(String[] filiais, String idFuncionario, UsuarioVO usuarioVO) throws SRVException {
			AgrupamentoFilialUsuarioDAO agrupamentoFilialUsuarioDAO = new AgrupamentoFilialUsuarioDAO();
			ParametroDAO parametroDAO = new ParametroDAO();
			parametroDAO.setConn(agrupamentoFilialUsuarioDAO.getConn());
			Map<String, String> mapa = parametroDAO.obtemParametro("AGRUPAMENTO_FILIAIS_REM_VAR", "PRM_COD_FUNC_COD_GRP_REM_VAR");
			int idGrupo = obtemIdGrupo(usuarioVO,mapa);
			agrupamentoFilialUsuarioDAO.beginTrans();
			agrupamentoFilialUsuarioDAO.removeTodosAgrupamentos(Integer.parseInt(idFuncionario),idGrupo);
			try{
				if(filiais!= null){
					for(int i=0;i<filiais.length;i++){
						agrupamentoFilialUsuarioDAO.incluiAgrupamentoFilial(Integer.parseInt(filiais[i]),Integer.parseInt(idFuncionario),idGrupo,usuarioVO);
					}
				}
				agrupamentoFilialUsuarioDAO.commitTrans();
			}catch (Exception e) {
				agrupamentoFilialUsuarioDAO.rollbackTrans();
				throw new SRVException(log, "Ocorreu erro agrupar filiais ao funcionario", e);
			} finally {
				agrupamentoFilialUsuarioDAO.closeConnection();
			}
		}


		public List<FilialVO> obtemListaVinculadasColaborador(UsuarioVO usuarioVO, int idFuncionario, Map<String, String> mapa) throws SRVException {
			AgrupamentoFilialUsuarioDAO agrupamentoFilialUsuarioDAO = new AgrupamentoFilialUsuarioDAO();
			int idGrupo = obtemIdGrupo(usuarioVO,mapa);
			List<Integer> listaCodFilial = agrupamentoFilialUsuarioDAO.obtemListaVinculadasColaborador(idFuncionario,idGrupo);
			List<FilialVO> listaFiliais = new ArrayList<FilialVO>();
			for(Integer idFilial:listaCodFilial){
				listaFiliais.add(FilialBusiness.getInstance().obtemFilial(Constantes.CODIGO_EMPRESA, idFilial, null));
			}
			return listaFiliais;
		}

}
