package br.com.marisa.srv.acesso.vo;

public class FuncionalidadeVO {
	
	private Integer codFuncionalidade;
	private String descricao;
	private String url;
	private Integer codModulo;
	private Boolean flagAtivo;
	private Integer codFuncionalidadePai;
	private boolean isFuncionalidadePai;

	public Integer getCodFuncionalidade() {
		return codFuncionalidade;
	}

	public void setCodFuncionalidade(Integer codFuncionalidade) {
		this.codFuncionalidade = codFuncionalidade;
	}

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

	public Boolean getFlagAtivo() {
		return flagAtivo;
	}

	public void setFlagAtivo(Boolean flagAtivo) {
		this.flagAtivo = flagAtivo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getCodFuncionalidadePai() {
		return codFuncionalidadePai;
	}

	public void setCodFuncionalidadePai(Integer codFuncionalidadePai) {
		this.codFuncionalidadePai = codFuncionalidadePai;
	}

	public boolean isFuncionalidadePai() {
		return isFuncionalidadePai;
	}

	public void setFuncionalidadePai(boolean isFuncionalidadePai) {
		this.isFuncionalidadePai = isFuncionalidadePai;
	}

}
