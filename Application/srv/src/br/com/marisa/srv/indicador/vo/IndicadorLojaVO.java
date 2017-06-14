package br.com.marisa.srv.indicador.vo;

import java.io.Serializable;

public class IndicadorLojaVO implements Serializable{
	
	private Integer idIndicador;
	private Integer idEmpresa;
	private Integer idFilial;
	private Integer ano;
	private Integer mes;
	private Integer numMeta;
	private Integer numRealizado;
	private Double  numRealizadoMeta;
	private Double  vlrPremioFilial;
	private Double  vlrPremioFilialCalculado;
	private Double  quantidadeRealizada;
	
	
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Integer getIdEmpresa() {
		return idEmpresa;
	}
	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}
	public Integer getIdIndicador() {
		return idIndicador;
	}
	public void setIdIndicador(Integer idIndicador) {
		this.idIndicador = idIndicador;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public Integer getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Integer codFilial) {
		this.idFilial = codFilial;
	}
	public Integer getNumMeta() {
		return numMeta;
	}
	public void setNumMeta(Integer numMeta) {
		this.numMeta = numMeta;
	}
	public Integer getNumRealizado() {
		return numRealizado;
	}
	public void setNumRealizado(Integer numRealizado) {
		this.numRealizado = numRealizado;
	}
	public Double getNumRealizadoMeta() {
		return numRealizadoMeta;
	}
	public void setNumRealizadoMeta(Double numRealizadoMeta) {
		this.numRealizadoMeta = numRealizadoMeta;
	}
	public Double getVlrPremioFilial() {
		return vlrPremioFilial;
	}
	public void setVlrPremioFilial(Double vlrPremioFilial) {
		this.vlrPremioFilial = vlrPremioFilial;
	}
	public Double getVlrPremioFilialCalculado() {
		return vlrPremioFilialCalculado;
	}
	public void setVlrPremioFilialCalculado(Double vlrPremioFilialCalculado) {
		this.vlrPremioFilialCalculado = vlrPremioFilialCalculado;
	}
	public Double getQuantidadeRealizada() {
		return quantidadeRealizada;
	}
	public void setQuantidadeRealizada(Double quantidadeRealizada) {
		this.quantidadeRealizada = quantidadeRealizada;
	}
}
