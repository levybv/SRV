package br.com.marisa.srv.gpremuneracaovariavel.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * VO para conter dados de indicador realizado por funcionário 
 * 
 * @author Walter Fontes
 */
public class IndicadorGpRemVarRealizadoVO implements Serializable{
	
	private Integer idGrupoRemuneracaoVariavel;
	private Integer ano;
	private Integer mes;
	private Integer idGrupoIndicador;
	private String  descricaoGrupoIndicador;
	private Integer idIndicador;
	private String  descricaoIndicador;
	private Integer idEscala;
	private Integer sequencialEscala;
	private Double  realizadoFaixa;
	private Integer unidadeRealizadoFaixa;
	private Integer idPonderacao;
	private Double  peso;
	private Integer unidadePeso;
	private Double  realizadoPonderacao;
	private Integer unidadeRealizadoPonderacao;
	private Double  meta;
	private Integer unidadeMeta;
	private Double  realizado;
	private Integer unidadeRealizado;
	private Double  realizadoXMeta;
	private Integer unidadeRealizadoXMeta;
	private Double  valorPremio;
	private Double  valorPremioCalculado;
	private Integer unidadeValorPremioCalculado;
	private Double  percentualRateio;
	private Date    dataUltimaAlteracao;
	private Integer idUsuarioAlteracao;
	
	
	public Integer getIdGrupoRemuneracaoVariavel() {
		return idGrupoRemuneracaoVariavel;
	}
	public void setIdGrupoRemuneracaoVariavel(Integer idGrupoRemuneracaoVariavel) {
		this.idGrupoRemuneracaoVariavel = idGrupoRemuneracaoVariavel;
	}
	public Integer getIdPonderacao() {
		return idPonderacao;
	}
	public void setIdPonderacao(Integer idPonderacao) {
		this.idPonderacao = idPonderacao;
	}
	public Double getPercentualRateio() {
		return percentualRateio;
	}
	public void setPercentualRateio(Double percentualRateiro) {
		this.percentualRateio = percentualRateiro;
	}
	public Double getRealizadoPonderacao() {
		return realizadoPonderacao;
	}
	public String getRealizadoPonderacaoFormatado() {
		return NumeroHelper.formataNumero(realizadoPonderacao, unidadeRealizadoPonderacao);
	}
	public void setRealizadoPonderacao(Double realizadoPonderacao) {
		this.realizadoPonderacao = realizadoPonderacao;
	}
	public Integer getSequencialEscala() {
		return sequencialEscala;
	}
	public void setSequencialEscala(Integer sequencialEscala) {
		this.sequencialEscala = sequencialEscala;
	}
	public Integer getUnidadeRealizadoPonderacao() {
		return unidadeRealizadoPonderacao;
	}
	public void setUnidadeRealizadoPonderacao(Integer unidadeRealizadoPonderacao) {
		this.unidadeRealizadoPonderacao = unidadeRealizadoPonderacao;
	}
	public Double getValorPremioCalculado() {
		return valorPremioCalculado;
	}
	public void setValorPremioCalculado(Double valorPremioCalculado) {
		this.valorPremioCalculado = valorPremioCalculado;
	}
	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}
	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	public Integer getIdUsuarioAlteracao() {
		return idUsuarioAlteracao;
	}
	public void setIdUsuarioAlteracao(Integer idUsuarioAlteracao) {
		this.idUsuarioAlteracao = idUsuarioAlteracao;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public String getDescricaoGrupoIndicador() {
		return descricaoGrupoIndicador;
	}
	public void setDescricaoGrupoIndicador(String descricaoGrupoIndicador) {
		this.descricaoGrupoIndicador = descricaoGrupoIndicador;
	}
	public String getDescricaoIndicador() {
		return descricaoIndicador;
	}
	public void setDescricaoIndicador(String descricaoIndicador) {
		this.descricaoIndicador = descricaoIndicador;
	}
	public Integer getIdEscala() {
		return idEscala;
	}
	public void setIdEscala(Integer idEscala) {
		this.idEscala = idEscala;
	}
	public Integer getIdGrupoIndicador() {
		return idGrupoIndicador;
	}
	public void setIdGrupoIndicador(Integer idGrupoIndicador) {
		this.idGrupoIndicador = idGrupoIndicador;
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
	public Double getMeta() {
		return meta;
	}
	public String getMetaFormatada() {
		return NumeroHelper.formataNumero(meta, unidadeMeta);
	}
	public void setMeta(Double meta) {
		this.meta = meta;
	}
	public Double getPeso() {
		return peso;
	}
	public String getPesoFormatado() {
		return NumeroHelper.formataNumero(peso, unidadePeso);
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	public Double getRealizado() {
		return realizado;
	}
	public String getRealizadoFormatado() {
		return NumeroHelper.formataNumero(realizado, unidadeRealizado);
	}	
	public void setRealizado(Double realizado) {
		this.realizado = realizado;
	}
	public Double getRealizadoFaixa() {
		return realizadoFaixa;
	}
	public String getRealizadoFaixaFormatado() {
		return NumeroHelper.formataNumero(realizadoFaixa, unidadeRealizadoFaixa);
	}	
	public void setRealizadoFaixa(Double realizadoFaixa) {
		this.realizadoFaixa = realizadoFaixa;
	}
	public Double getRealizadoXMeta() {
		return realizadoXMeta;
	}
	public String getRealizadoXMetaFormatado() {
		return NumeroHelper.formataNumero(realizadoXMeta, unidadeRealizadoXMeta);
	}
	public void setRealizadoXMeta(Double realizadoXMeta) {
		this.realizadoXMeta = realizadoXMeta;
	}
	public Integer getUnidadeMeta() {
		return unidadeMeta;
	}
	public void setUnidadeMeta(Integer unidadeMeta) {
		this.unidadeMeta = unidadeMeta;
	}
	public Integer getUnidadePeso() {
		return unidadePeso;
	}
	public void setUnidadePeso(Integer unidadePeso) {
		this.unidadePeso = unidadePeso;
	}
	public Integer getUnidadeRealizado() {
		return unidadeRealizado;
	}
	public void setUnidadeRealizado(Integer unidadeRealizado) {
		this.unidadeRealizado = unidadeRealizado;
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
	public Integer getUnidadeValorPremioCalculado() {
		return unidadeValorPremioCalculado;
	}
	public void setUnidadeValorPremioCalculado(Integer unidadeValorPremio) {
		this.unidadeValorPremioCalculado = unidadeValorPremio;
	}
	public Double getValorPremio() {
		return valorPremio;
	}
	public String getValorPremioCalculadoFormatado() {
		return NumeroHelper.formataNumero(valorPremioCalculado, unidadeValorPremioCalculado);
	}
	public void setValorPremio(Double valorPremio) {
		this.valorPremio = valorPremio;
	}
}