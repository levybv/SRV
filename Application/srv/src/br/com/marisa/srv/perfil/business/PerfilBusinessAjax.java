package br.com.marisa.srv.perfil.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.marisa.srv.acesso.business.AcessoBusiness;
import br.com.marisa.srv.acesso.business.FuncionalidadeBusiness;
import br.com.marisa.srv.acesso.business.ModuloBusiness;
import br.com.marisa.srv.acesso.business.TipoAcessoBusiness;
import br.com.marisa.srv.acesso.vo.AcessoVO;
import br.com.marisa.srv.acesso.vo.FuncionalidadeVO;
import br.com.marisa.srv.acesso.vo.ModuloVO;
import br.com.marisa.srv.acesso.vo.TipoAcessoVO;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.perfil.vo.PerfilVO;

public class PerfilBusinessAjax {

	public PerfilVO obtemPerfilByCod(String codPerfilSrt) throws NumberFormatException, SRVException {

		Integer codPerfil = new Integer(codPerfilSrt);
		return PerfilBusiness.getInstance().obtemPerfilByCod(codPerfil);
	}

	public List obtemListaModulos() throws NumberFormatException, SRVException {

		return ModuloBusiness.getInstance().obtemListaModulos(null);
	}

	public List obtemListaFuncionalidades(String funcionalidadeStr, String moduloStr) throws NumberFormatException, SRVException {

		Integer funcionalidade = funcionalidadeStr == null ? null : new Integer(funcionalidadeStr);
		Integer modulo = new Integer(moduloStr);
		return FuncionalidadeBusiness.getInstance().obtemListaFuncionalidades(funcionalidade, modulo);
	}

	public List obtemListaTipoAcesso(String funcionalidadeStr) throws NumberFormatException, SRVException {

		Integer funcionalidade = new Integer(funcionalidadeStr);
		return TipoAcessoBusiness.getInstance().obtemListaTiposAcesso(funcionalidade, null);
	}

	public List obtemListaAcessos(String codPerfilSrt) throws NumberFormatException, SRVException {

		Integer codPerfil = new Integer(codPerfilSrt);
		Map mapa = AcessoBusiness.getInstance().obtemAcessoPerfil(codPerfil.intValue());
		Iterator it = mapa.keySet().iterator();

		List listaModulo = ModuloBusiness.getInstance().obtemListaModulos(null);
		List listaFuncionalidades = FuncionalidadeBusiness.getInstance().obtemListaFuncionalidades(null, null);
		List listaTela = new ArrayList();
		while (it.hasNext()) {
			Integer idFuncionalidade = (Integer) it.next();
			List listaAcessosTemp = (List) mapa.get(idFuncionalidade);
			for (int i = 0; i < listaAcessosTemp.size(); i++) {
				Integer idAcesso = (Integer) listaAcessosTemp.get(i);
				FuncionalidadeVO funcionalidadeVO = obtemFuncionalidade(idFuncionalidade, listaFuncionalidades);
				ModuloVO moduloVO = obtemModulo(funcionalidadeVO.getCodModulo(), listaModulo);
				TipoAcessoVO tipoAcessoVO = (TipoAcessoVO) TipoAcessoBusiness.getInstance().obtemListaTiposAcesso(funcionalidadeVO.getCodFuncionalidade(), idAcesso).get(0);
				AcessoVO acessoVO = new AcessoVO();
				acessoVO.setFuncionalidadeVO(funcionalidadeVO);
				acessoVO.setModuloVO(moduloVO);
				acessoVO.setTipoAcessoVO(tipoAcessoVO);
				listaTela.add(acessoVO);
			}
		}
		return listaTela;
	}

	private ModuloVO obtemModulo(Integer codModulo, List listaModulo) {

		for (int i = 0; i < listaModulo.size(); i++) {
			ModuloVO moduloVO = (ModuloVO) listaModulo.get(i);
			if (moduloVO.getCodModulo().equals(codModulo)) {
				return moduloVO;
			}
		}
		return null;
	}

	private FuncionalidadeVO obtemFuncionalidade(Integer idFuncionalidade, List listaFuncionalidades) {

		for (int i = 0; i < listaFuncionalidades.size(); i++) {
			FuncionalidadeVO funcionalidadeVO = (FuncionalidadeVO) listaFuncionalidades.get(i);
			if (funcionalidadeVO.getCodFuncionalidade().equals(idFuncionalidade)) {
				return funcionalidadeVO;
			}
		}
		return null;
	}

	public List obtemAcessos(String moduloStr, String idFuncionalidade) throws SRVException {

		List lista = new ArrayList();
		Integer modulo = new Integer(moduloStr);
		Integer funcionalidade = ("T".equals(idFuncionalidade)) || (idFuncionalidade == null) ? null : new Integer(idFuncionalidade);
		List listaFuncionalidades = FuncionalidadeBusiness.getInstance().obtemListaFuncionalidades(funcionalidade, modulo);
		ModuloVO moduloVO = (ModuloVO) ModuloBusiness.getInstance().obtemListaModulos(modulo).get(0);
		for (int i = 0; i < listaFuncionalidades.size(); i++) {
			FuncionalidadeVO funcionalidadeVO = (FuncionalidadeVO) listaFuncionalidades.get(i);
			List listTiposAcesso = TipoAcessoBusiness.getInstance().obtemListaTiposAcesso(funcionalidadeVO.getCodFuncionalidade(), null);
			for (int j = 0; j < listTiposAcesso.size(); j++) {
				TipoAcessoVO tipoAcessoVO = (TipoAcessoVO) listTiposAcesso.get(j);
				AcessoVO acessoVO = new AcessoVO();
				acessoVO.setFuncionalidadeVO(funcionalidadeVO);
				acessoVO.setModuloVO(moduloVO);
				acessoVO.setTipoAcessoVO(tipoAcessoVO);
				lista.add(acessoVO);
			}
		}

		return lista;
	}

	/**
	 * 
	 * @return
	 * @throws SRVException
	 */
	public List<PerfilVO> obtemPerfis() throws SRVException {
		return PerfilBusiness.getInstance().obtemListaPerfisByDescricao(null);
	}
}