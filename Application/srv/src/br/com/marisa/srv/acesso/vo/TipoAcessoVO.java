package br.com.marisa.srv.acesso.vo;

import java.io.Serializable;

public class TipoAcessoVO implements Serializable{
	
	private Integer codFuncionalidade;
	private Integer codTipoAcesso;
	private String descricao;
	
	
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getCodFuncionalidade() {
		return codFuncionalidade;
	}
	public void setCodFuncionalidade(Integer codFuncionalidade) {
		this.codFuncionalidade = codFuncionalidade;
	}
	public Integer getCodTipoAcesso() {
		return codTipoAcesso;
	}
	public void setCodTipoAcesso(Integer codTipoAcesso) {
		this.codTipoAcesso = codTipoAcesso;
	}
	
	
	

}
