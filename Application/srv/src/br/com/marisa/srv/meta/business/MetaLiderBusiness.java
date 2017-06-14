package br.com.marisa.srv.meta.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import br.com.marisa.srv.filtro.vo.FiltroMetaLiderVO;
import br.com.marisa.srv.funcionario.business.FuncionarioBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.helper.LoggerHelper;
import br.com.marisa.srv.indicador.business.IndicadorBusiness;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.meta.dao.MetaLiderDAO;
import br.com.marisa.srv.meta.vo.MetaLiderVO;
import br.com.marisa.srv.parametros.business.ParametroBusiness;
import br.com.marisa.srv.parametros.dao.ParametroDAO;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

public class MetaLiderBusiness {
	private static java.util.logging.Logger log = LoggerHelper.getInstance().getLogger(MetaLiderBusiness.class.getName());
	
	 //Instancia do Singleton
    private static MetaLiderBusiness instance = new MetaLiderBusiness();
    
    
    /**
     * Obtem uma instancia do objeto MetaFilialBusiness
     * @return O objeto MetaFilialBusiness
     */
    public static final MetaLiderBusiness getInstance() {
        return instance;
    }
    

    /**
     * Construtor default. Foi escondido como private por cargo do singleton. Utilizar o método getInstance();
     */
    private MetaLiderBusiness() {
    }


	public List<MetaLiderVO> obtemListaMetasLider(FiltroMetaLiderVO filtroMetaLiderVO) throws PersistenciaException {
		MetaLiderDAO metaLiferDAO = new MetaLiderDAO();
		List<MetaLiderVO> lista = metaLiferDAO.obtemListaMetasLider(filtroMetaLiderVO);
		for(MetaLiderVO metaLiderVO:lista){
			metaLiderVO.setMesAno(Constantes.MESES[metaLiderVO.getMes()-1]+"/"+metaLiderVO.getAno());
		}
		return lista;
	}


	public List<FuncionarioVO> obtemListaLideres() throws SRVException {
		Map<String,String> mapa =  ParametroBusiness.getInstance().obtemParametro("PRC_META_LIDERES_CARGOS", "PRM_META_LIDERES");
		String[] cargos = (mapa.get("COD_CARGO_LIDERES")).split(",");
		String[] statusNotIn = (mapa.get("STATUS_LIDERES_DIFERENTE_DE")).split(",");
		List<FuncionarioVO> lista = new ArrayList<FuncionarioVO>();
		for (int i=0;i<cargos.length;i++){
			lista.addAll(FuncionarioBusiness.getInstance().obtemListaFuncionario(null, null, null, null, new Integer(cargos[i]), null, null));	
		}
		// remove colaboradores com statusNotin igual ao parametrizado
		Iterator<FuncionarioVO> it = lista.iterator();
		while (it.hasNext()) {
			FuncionarioVO funcionarioVO = it.next();
			for(int i=0;i<statusNotIn.length;i++){
				if(statusNotIn[i].equals(funcionarioVO.getIdSituacaoRH().toString())){
					it.remove();
					break;
				}
			}
		}
		Collections.sort(lista);
		return lista;
	}


	public List<DadosIndicadorVO> obtemListaIndicadores() throws SRVException {
//		IndicadorBusiness.getInstance().obtemListaIndicadoresByTipo("CALL_CENTER")
		DadosIndicadorVO dadosPesquisa = new DadosIndicadorVO();
		List<Integer> listaGrupo = new ArrayList<Integer>();
		listaGrupo.add(Constantes.ID_TIPO_REM_VAR_CALL_CENTER);
		return IndicadorBusiness.getInstance().obtemListaIndicadores(dadosPesquisa,listaGrupo,Boolean.TRUE,Boolean.TRUE);
	}


	public List<String> obtemListaEquipes() throws SRVException {
		Map<String,String> mapa =  ParametroBusiness.getInstance().obtemParametro("PRC_META_LIDERES_CARGOS", "PRM_META_LIDERES");
		String[] equipes = (mapa.get("EQUIPES")).split(",");
		return new ArrayList<String>(Arrays.asList(equipes));
	}


	public boolean podeEditarMesAnterior() throws SRVException {
		Map<String,String> mapa =  ParametroBusiness.getInstance().obtemParametro("PRC_META_LIDERES_CARGOS", "PRM_META_LIDERES");
		Calendar c = new GregorianCalendar();
		return c.get(Calendar.DAY_OF_MONTH)<=Integer.parseInt(mapa.get("MAX_DIA_MES_PERMITE_ALTERACAO"));
	}


	public void incluiMetaLider(MetaLiderVO metaLiderVO, UsuarioVO usuarioVO) throws PersistenciaException {
		MetaLiderDAO metaLiderDAO = new MetaLiderDAO();
		metaLiderDAO.incluiMetaLider(metaLiderVO,usuarioVO);
	}


	/**
	 * verifica se para a mesma data/equipe/lider existe uma equipe cadastrada
	 * essa não poderá diferir de equipe se ja cadastrado para um idicador
	 * @param metaLiderVO
	 * @return se estiver vazia ou contiver o nome está valido
	 * @throws PersistenciaException 
	 */
	public Boolean validaMetaLider(MetaLiderVO metaLiderVO) throws PersistenciaException {
		MetaLiderDAO metaLiderDAO = new MetaLiderDAO();
		List<String> lista = metaLiderDAO.validaMetaLider(metaLiderVO);
		return lista.isEmpty() || lista.contains(metaLiderVO.getEquipe());
	}


	public Boolean validaDuplicidade(MetaLiderVO metaLiderVO) throws PersistenciaException {
		MetaLiderDAO metaLiderDAO = new MetaLiderDAO();
		return metaLiderDAO.validaDuplicidade(metaLiderVO);
	}


	public Boolean validaEdicao(MetaLiderVO metaLiderVO) throws PersistenciaException {
		ParametroDAO parametroDAO = new ParametroDAO();
		try {
			Map<String, String> mapa = parametroDAO.obtemParametro("PRC_META_LIDERES_CARGOS", "PRM_META_LIDERES");
			int diaMaximoParaEdicao = Integer.parseInt(mapa.get("MAX_DIA_MES_PERMITE_ALTERACAO"));
			Calendar c = new GregorianCalendar ();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			if(c.get(Calendar.DAY_OF_MONTH)<= diaMaximoParaEdicao){
				c.add(Calendar.MONTH, -1);
			}
			c.set(Calendar.DAY_OF_MONTH, 1);
			
			
			Calendar cParametro = new GregorianCalendar ();
			cParametro.set(Calendar.YEAR, metaLiderVO.getAno());
			cParametro.set(Calendar.MONTH, metaLiderVO.getMes()-1);
			cParametro.set(Calendar.DAY_OF_MONTH, 1);
			cParametro.set(Calendar.HOUR_OF_DAY, 0);
			cParametro.set(Calendar.MINUTE, 0);
			cParametro.set(Calendar.SECOND, 0);
			cParametro.set(Calendar.MILLISECOND, 0);
			return cParametro.getTimeInMillis()>=c.getTimeInMillis();
		} finally {
			parametroDAO.closeConnection();
		}
	}


	public void atualizaMetaLider(MetaLiderVO metaLiderVO, UsuarioVO usuarioVO,
			String dadosAnteriores) throws PersistenciaException {
		//${listaMetas.codFuncionario};${listaMetas.codIndicador};${listaMetas.ano};${listaMetas.mes}
		String[] valoresAnteriores = dadosAnteriores.split(";");
		MetaLiderDAO metaLiderDAO = new MetaLiderDAO();
		MetaLiderVO metaLiderVOAnterior = new MetaLiderVO();
		metaLiderVOAnterior.setCodFuncionario(new Integer(valoresAnteriores[0]));
		metaLiderVOAnterior.setCodIndicador(new Integer(valoresAnteriores[1]));
		metaLiderVOAnterior.setAno(new Integer(valoresAnteriores[2]));
		metaLiderVOAnterior.setMes(new Integer(valoresAnteriores[3]));
		try{
			metaLiderDAO.beginTrans();
			removeMetaLider(metaLiderVOAnterior);
			incluiMetaLider(metaLiderVO, usuarioVO);
			metaLiderDAO.commitTrans();
		}catch(Exception e){
			metaLiderDAO.rollbackTrans();
			throw new PersistenciaException( "Não foi possível atualizar metas lideres"  , e,log,Level.SEVERE,this.getClass(),"List<MetaLiderVO> obtemListaMetasLider(FiltroMetaLiderVO filtroMetaLiderVO)  ");
		}finally{
			metaLiderDAO.closeConnection();
		}
		
		
	}


	public void removeMetaLider(MetaLiderVO metaLiderVO) throws PersistenciaException {
		MetaLiderDAO metaLiderDAO = new MetaLiderDAO();
		metaLiderDAO.removeMetaLider(metaLiderVO);
	}

}
