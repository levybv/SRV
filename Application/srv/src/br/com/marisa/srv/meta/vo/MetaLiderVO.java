package br.com.marisa.srv.meta.vo;

import java.io.Serializable;
import java.text.DecimalFormat;

import br.com.marisa.srv.geral.helper.StringHelper;

public class MetaLiderVO implements Serializable{
	
	private Integer codFuncionario;
	private Integer codIndicador;
	private Integer ano;
	private Integer mes;
	private String equipe;
	private Double meta;
	private String nomeFuncionario;
	private String descricaoIndicador;
	private String mesAno;
	private String metaS;
	
	
	
	public String getMetaS() {
		return metaS;
	}
	public void setMetaS(String metaS) {
		this.metaS = metaS;
	}
	public String getMesAno() {
		return mesAno;
	}
	public void setMesAno(String mesAno) {
		this.mesAno = mesAno;
	}
	public String getDescricaoIndicador() {
		return descricaoIndicador;
	}
	public void setDescricaoIndicador(String descricaoIndicador) {
		this.descricaoIndicador = descricaoIndicador;
	}
	public String getNomeFuncionario() {
		return nomeFuncionario;
	}
	public void setNomeFuncionario(String nomeFuncionario) {
		this.nomeFuncionario = nomeFuncionario;
	}
	public Integer getCodFuncionario() {
		return codFuncionario;
	}
	public void setCodFuncionario(Integer codFuncionario) {
		this.codFuncionario = codFuncionario;
	}
	
	public Integer getCodIndicador() {
		return codIndicador;
	}
	public void setCodIndicador(Integer codIndicador) {
		this.codIndicador = codIndicador;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public String getEquipe() {
		return equipe;
	}
	public void setEquipe(String equipe) {
		this.equipe = equipe;
	}
	public Double getMeta() {
		return meta;
	}
	public void setMeta(Double meta) {
		this.meta = meta;
		DecimalFormat df = new DecimalFormat("###,###.00");
		setMetaS(df.format(meta));
	}
	
	

}
