package br.com.marisa.srv.acesso.vo;

import java.io.Serializable;

public class AcessoVO implements Serializable{
	
	private FuncionalidadeVO funcionalidadeVO;
	private TipoAcessoVO tipoAcessoVO;
	private ModuloVO moduloVO;
	
	
	public FuncionalidadeVO getFuncionalidadeVO() {
		return funcionalidadeVO;
	}
	public void setFuncionalidadeVO(FuncionalidadeVO funcionalidadeVO) {
		this.funcionalidadeVO = funcionalidadeVO;
	}
	public ModuloVO getModuloVO() {
		return moduloVO;
	}
	public void setModuloVO(ModuloVO moduloVO) {
		this.moduloVO = moduloVO;
	}
	public TipoAcessoVO getTipoAcessoVO() {
		return tipoAcessoVO;
	}
	public void setTipoAcessoVO(TipoAcessoVO tipoAcessoVO) {
		this.tipoAcessoVO = tipoAcessoVO;
	}
	
	
	

}
