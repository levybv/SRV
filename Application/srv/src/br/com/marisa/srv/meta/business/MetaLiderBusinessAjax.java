package br.com.marisa.srv.meta.business;

import java.util.List;

import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.excecoes.PersistenciaException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.indicador.vo.DadosIndicadorVO;
import br.com.marisa.srv.meta.vo.MetaLiderVO;

public class MetaLiderBusinessAjax {
	public MetaLiderBusinessAjax(){}
	
	public List<FuncionarioVO> obtemLideres() throws SRVException{
		return MetaLiderBusiness.getInstance().obtemListaLideres();
	}
	public Boolean permiteAlteracaoMesAnterior() throws SRVException{
		return MetaLiderBusiness.getInstance().podeEditarMesAnterior();
	}
	public List<String> obtemListaEquipes() throws SRVException{
		return MetaLiderBusiness.getInstance().obtemListaEquipes();
	}
	public List<DadosIndicadorVO> obtemListaIndicador() throws SRVException{
		return MetaLiderBusiness.getInstance().obtemListaIndicadores();
	}
	/**
	 * verifica se a meta pode ser editada
	 * @param valorCheckBox
	 * @return
	 * @throws PersistenciaException 
	 */
	public Boolean validaEdicao(String valorCheckBox) throws PersistenciaException{
//		${listaMetas.codFuncionario};${listaMetas.codIndicador};${listaMetas.ano};${listaMetas.mes}
		String[] mesAno = valorCheckBox.split(";");
		MetaLiderVO metaLiderVO = new MetaLiderVO();
		metaLiderVO.setMes(Integer.parseInt(mesAno[3]));
		metaLiderVO.setAno(Integer.parseInt(mesAno[2]));
		return MetaLiderBusiness.getInstance().validaEdicao(metaLiderVO);
	}
	
	public Boolean validaMetaLider(String mesAno, String idLider, String idEquipe, String idIndicador) throws SRVException{
		if(isVariaveisPopuladas(mesAno, idLider, idEquipe, idIndicador)){
			MetaLiderVO metaLiderVO = new MetaLiderVO();
			metaLiderVO.setMes(Integer.parseInt(mesAno.substring(4))+1);
			metaLiderVO.setAno(Integer.parseInt(mesAno.substring(0,4)));
			metaLiderVO.setCodFuncionario(new Integer(idLider));
			metaLiderVO.setCodIndicador( new Integer(idIndicador));
			metaLiderVO.setEquipe(idEquipe);
			return MetaLiderBusiness.getInstance().validaMetaLider(metaLiderVO);
		}
		return true;
	}
	public Boolean validaDuplicidade(String mesAno, String idLider, String idEquipe, String idIndicador) throws SRVException{
		if(isVariaveisPopuladas(mesAno, idLider, idEquipe, idIndicador)){
			MetaLiderVO metaLiderVO = new MetaLiderVO();
			metaLiderVO.setMes(Integer.parseInt(mesAno.substring(4))+1);
			metaLiderVO.setAno(Integer.parseInt(mesAno.substring(0,4)));
			metaLiderVO.setCodFuncionario(new Integer(idLider));
			metaLiderVO.setCodIndicador( new Integer(idIndicador));
			metaLiderVO.setEquipe(idEquipe);
			return MetaLiderBusiness.getInstance().validaDuplicidade(metaLiderVO);
		}
		return true;
	}
	private boolean isVariaveisPopuladas  (String mesAno, String idLider, String idEquipe, String idIndicador){
		if(mesAno != null && idLider != null && idEquipe != null && idIndicador != null){
			return mesAno.length() >0 
					&& idLider.length() >0
					&& idEquipe.length() >0
					&& idIndicador.length() >0;
		}else{
			return false;
		}
	}
	
	
}
