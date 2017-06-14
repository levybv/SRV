package br.com.marisa.srv.acesso.vo;

import java.io.Serializable;

public class ModuloVO implements Serializable{
	
	private Integer codModulo;
	private String descricao;
	
	
	public Integer getCodModulo() {
		return codModulo;
	}
	public void setCodModulo(Integer codModulo) {
		this.codModulo = codModulo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
