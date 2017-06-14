package br.com.marisa.srv.indicador.vo;

import java.io.Serializable;

import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * VO para conter o detalhamento do cálculo do indicador do funcionário
 * 
 * @author Walter Fontes
 */
public class DetalheCalculoLojaVO implements Serializable{
	
	private Integer mes;
	private String  mesExtenso;
	private Integer ano;
	private Integer idEmpresa;
	private Integer idFilial;
	private String  descricaoFilial;
	private Integer idIndicador;
	private String  descricaoIndicador;
	private Double  meta;
	private Integer unidadeMeta;
	private Double  realizado;
	private Integer unidadeRealizado;
	private Integer qtdeRealizado;
	private Double  realizadoXMeta;
	private Integer unidadeRealizadoXMeta;
	private Integer codigoEscala;
	private String  descricaoEscala;
	private Double  inicioFaixa;
	private Double  fimFaixa;
	private Integer unidadeFaixa;
	private Double  realizadoFaixa;
	private Integer unidadeRealizadoFaixa;
	private Double  valorPremioCalculado;
	private Integer unidadePremioCalculado;
	
	public Integer getUnidadeFaixa() {
		return unidadeFaixa;
	}
	public void setUnidadeFaixa(Integer unidadeFaixa) {
		this.unidadeFaixa = unidadeFaixa;
	}
	public Integer getUnidadeRealizadoFaixa() {
		return unidadeRealizadoFaixa;
	}
	public void setUnidadeRealizadoFaixa(Integer unidadeRealizadoFaixa) {
		this.unidadeRealizadoFaixa = unidadeRealizadoFaixa;
	}
	public Integer getUnidadeRealizadoXMeta() {
		return unidadeRealizadoXMeta;
	}
	public void setUnidadeRealizadoXMeta(Integer unidadeRealizadoXMeta) {
		this.unidadeRealizadoXMeta = unidadeRealizadoXMeta;
	}
	public Integer getUnidadeMeta() {
		return unidadeMeta;
	}
	public void setUnidadeMeta(Integer unidadeMeta) {
		this.unidadeMeta = unidadeMeta;
	}
	public Integer getUnidadeRealizado() {
		return unidadeRealizado;
	}
	public void setUnidadeRealizado(Integer unidadeRealizado) {
		this.unidadeRealizado = unidadeRealizado;
	}
	public String getDescricaoFilial() {
		return descricaoFilial;
	}
	public void setDescricaoFilial(String descricaoFilial) {
		this.descricaoFilial = descricaoFilial;
	}
	public Integer getIdFilial() {
		return idFilial;
	}
	public void setIdFilial(Integer idFilial) {
		this.idFilial = idFilial;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Integer getCodigoEscala() {
		return codigoEscala;
	}
	public void setCodigoEscala(Integer codigoEscala) {
		this.codigoEscala = codigoEscala;
	}
	public String getDescricaoEscala() {
		return descricaoEscala;
	}
	public String getDescricaoEscalaFormatada() {
		if (descricaoEscala == null) return "Não existente";
		return descricaoEscala;
	}
	public void setDescricaoEscala(String descricaoEscala) {
		this.descricaoEscala = descricaoEscala;
	}
	public Double getFimFaixa() {
		return fimFaixa;
	}
	public String getFimFaixaFormatado() {
		if (fimFaixa == null) return "";
		return NumeroHelper.formataNumero(fimFaixa, unidadeFaixa);
	}
	public void setFimFaixa(Double fimFaixa) {
		this.fimFaixa = fimFaixa;
	}
	public Double getInicioFaixa() {
		return inicioFaixa;
	}
	public String getInicioFaixaFormatado() {
		if (inicioFaixa == null) return "";
		return NumeroHelper.formataNumero(inicioFaixa, unidadeFaixa);
	}
	public void setInicioFaixa(Double inicioFaixa) {
		this.inicioFaixa = inicioFaixa;
	}
	public Integer getMes() {
		return mes;
	}
	public void setMes(Integer mes) {
		this.mes = mes;
	}
	public String getMesExtenso() {
		return mesExtenso;
	}
	public void setMesExtenso(String mesExtenso) {
		this.mesExtenso = mesExtenso;
	}
	public Double getMeta() {
		return meta;
	}
	public String getMetaFormatado() {
		if (meta == null) return "Não existente";
		return NumeroHelper.formataNumero(meta, unidadeMeta);
	}	
	public void setMeta(Double meta) {
		this.meta = meta;
	}
	public Double getRealizado() {
		return realizado;
	}
	public String getRealizadoFormatado() {
		if (realizado == null) return "Não existente";
		return NumeroHelper.formataNumero(realizado, unidadeRealizado);
	}
	public void setRealizado(Double realizado) {
		this.realizado = realizado;
	}
	public Double getRealizadoFaixa() {
		return realizadoFaixa;
	}
	public String getRealizadoFaixaFormatado() {
		if (realizadoFaixa == null) return "";
		return NumeroHelper.formataNumero(realizadoFaixa, unidadeRealizadoFaixa);
	}
	public void setRealizadoFaixa(Double realizadoFaixa) {
		this.realizadoFaixa = realizadoFaixa;
	}
	public Double getRealizadoXMeta() {
		return realizadoXMeta;
	}
	public String getRealizadoXMetaFormatado() {
		if (realizadoXMeta == null) return "";
		return NumeroHelper.formataNumero(realizadoXMeta, unidadeRealizadoXMeta);
	}	
	public void setRealizadoXMeta(Double realizadoXMeta) {
		this.realizadoXMeta = realizadoXMeta;
	}
	public Double getValorPremioCalculado() {
		return valorPremioCalculado;
	}
	public String getValorPremioCalculadoFormatado() {
		if (valorPremioCalculado == null) return "Não existente";
		return NumeroHelper.formataNumero(valorPremioCalculado, unidadePremioCalculado);
	}
	public void setValorPremioCalculado(Double valorPremioCalculado) {
		this.valorPremioCalculado = valorPremioCalculado;
	}
	public String getFaixaUtilizadaFormatada() {
		if (inicioFaixa == null || fimFaixa == null) return "Não existente";
		return "de " + getInicioFaixaFormatado() + " até " + getFimFaixaFormatado() + " - Realizado Faixa: " + getRealizadoFaixaFormatado();		
	}
	public String getDescricaoIndicador() {
		return descricaoIndicador;
	}
	public void setDescricaoIndicador(String descricaoIndicador) {
		this.descricaoIndicador = descricaoIndicador;
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
	public Integer getQtdeRealizado() {
		return qtdeRealizado;
	}
	public String getQtdeRealizadoFormatado() {
		if (qtdeRealizado == null) return "Não existente";
		return String.valueOf(qtdeRealizado);
	}
	public void setQtdeRealizado(Integer qtdeRealizado) {
		this.qtdeRealizado = qtdeRealizado;
	}
	public Integer getUnidadePremioCalculado() {
		return unidadePremioCalculado;
	}
	public void setUnidadePremioCalculado(Integer unidadePremioCalculado) {
		this.unidadePremioCalculado = unidadePremioCalculado;
	}
}