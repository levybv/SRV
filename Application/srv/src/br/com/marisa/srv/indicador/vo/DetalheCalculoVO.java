package br.com.marisa.srv.indicador.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * VO para conter o detalhamento do cálculo do indicador da loja
 * 
 * @author Walter Fontes
 */
public class DetalheCalculoVO implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer mes;
	private String  mesExtenso;
	private Integer ano;
	private Integer idFilial;
	private String  descricaoFilial;
	private Integer idFuncionario;
	private String  nomeFuncionario;
	private Double  valorPremio;
	private Double  percentualRateio;
	private Double  meta;
	private Integer unidadeMeta;
	private Double  realizado;
	private Integer unidadeRealizado;
	private Double  realizadoXMeta;
	private Integer unidadeRealizadoXMeta;
	private Integer codigoEscala;
	private String  descricaoEscala;
	private Double  inicioFaixa;
	private Double  fimFaixa;
	private Integer unidadeFaixa;
	private Double  realizadoFaixa;
	private Integer unidadeRealizadoFaixa;
	private Integer codigoPonderacao;
	private Double  pesoPonderacao;
	private Integer unidadePesoPonderacao;
	private Double  realizadoPonderacao;
	private Integer unidadeRealizadoPonderacao;
	private Double  valorPremioCalculado;
	private Double  salarioMinimoClasseHay;
	private String mesesProporcional;
	private String observacao;
	private int statusCalculoRealizado;
	private Date dtIniSit;
	private Double  peso;
	private Integer unidadePeso;
	
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getMesesProporcional() {
		return mesesProporcional;
	}
	public void setMesesProporcional(String mesesProporcional) {
		this.mesesProporcional = mesesProporcional;
	}
	public Double getSalarioMinimoClasseHay() {
		return salarioMinimoClasseHay;
	}
	public void setSalarioMinimoClasseHay(Double salarioMinimoClasseHay) {
		this.salarioMinimoClasseHay = salarioMinimoClasseHay;
	}
	public Integer getUnidadeFaixa() {
		return unidadeFaixa;
	}
	public void setUnidadeFaixa(Integer unidadeFaixa) {
		this.unidadeFaixa = unidadeFaixa;
	}
	public Integer getUnidadePesoPonderacao() {
		return unidadePesoPonderacao;
	}
	public void setUnidadePesoPonderacao(Integer unidadePesoPonderacao) {
		this.unidadePesoPonderacao = unidadePesoPonderacao;
	}
	public Integer getUnidadeRealizadoFaixa() {
		return unidadeRealizadoFaixa;
	}
	public void setUnidadeRealizadoFaixa(Integer unidadeRealizadoFaixa) {
		this.unidadeRealizadoFaixa = unidadeRealizadoFaixa;
	}
	public Integer getUnidadeRealizadoPonderacao() {
		return unidadeRealizadoPonderacao;
	}
	public void setUnidadeRealizadoPonderacao(Integer unidadeRealizadoPonderacao) {
		this.unidadeRealizadoPonderacao = unidadeRealizadoPonderacao;
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
	public Integer getIdFuncionario() {
		return idFuncionario;
	}
	public void setIdFuncionario(Integer idFuncionario) {
		this.idFuncionario = idFuncionario;
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
	public Integer getCodigoPonderacao() {
		return codigoPonderacao;
	}
	public void setCodigoPonderacao(Integer codigoPonderacao) {
		this.codigoPonderacao = codigoPonderacao;
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
	public String getNomeFuncionario() {
		return nomeFuncionario;
	}
	public void setNomeFuncionario(String nome) {
		this.nomeFuncionario = nome;
	}
	public Double getPercentualRateio() {
		return percentualRateio;
	}
	public String getPercentualRateioFormatado() {
		if (percentualRateio == null) return "Não existente";
		return NumeroHelper.formataValor(percentualRateio) + " %";
	}
	public void setPercentualRateio(Double percentualRateio) {
		this.percentualRateio = percentualRateio;
	}
	public Double getPesoPonderacao() {
		return pesoPonderacao;
	}
	public String getPesoPonderacaoFormatado() {
		if (pesoPonderacao == null) return "";
		return NumeroHelper.formataNumero(pesoPonderacao, unidadePesoPonderacao);
	}	
	public void setPesoPonderacao(Double pesoPonderacao) {
		this.pesoPonderacao = pesoPonderacao;
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
	public Double getRealizadoPonderacao() {
		return realizadoPonderacao;
	}
	public String getRealizadoPonderacaoFormatado() {
		if (realizadoPonderacao == null) return "";
		return NumeroHelper.formataNumero(realizadoPonderacao, unidadeRealizadoPonderacao);
	}	
	public void setRealizadoPonderacao(Double realizadoPonderacao) {
		this.realizadoPonderacao = realizadoPonderacao;
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
	public Double getValorPremio() {
		return valorPremio;
	}
	public String getValorPremioFormatado() {
		if (valorPremio == null) return "Não existente";
		return "R$ " + NumeroHelper.formataValor(valorPremio);
	}
	public void setValorPremio(Double valorPremio) {
		this.valorPremio = valorPremio;
	}
	public Double getValorPremioCalculado() {
		return valorPremioCalculado;
	}
	public String getValorPremioCalculadoFormatado() {
		if (valorPremioCalculado == null) return "Não existente";
		return "R$ " + NumeroHelper.formataValor(valorPremioCalculado);
	}
	public String getValorPremioCalculadoBonusFormatado() {
		if (valorPremioCalculado == null) return "Não existente";
		return NumeroHelper.formataNumero(valorPremioCalculado, new Integer(Constantes.UNIDADE_VALOR_SEM_RS));
	}	
	public void setValorPremioCalculado(Double valorPremioCalculado) {
		this.valorPremioCalculado = valorPremioCalculado;
	}
	public String getFaixaUtilizadaFormatada() {
		if (inicioFaixa == null || fimFaixa == null) return "Não existente";
		return "de " + getInicioFaixaFormatado() + " até " + getFimFaixaFormatado() + " - Realizado Faixa: " + getRealizadoFaixaFormatado();		
	}
	public String getPonderacaoUtilizadaFormatada() {
		if (pesoPonderacao == null && realizadoPonderacao == null) 
			return "Não existente";
		if (pesoPonderacao != null && realizadoPonderacao != null) 
			return "Peso: " + getPesoPonderacaoFormatado() + " - Realizado Ponderação: " + getRealizadoPonderacaoFormatado();
		if (pesoPonderacao != null) {
			return "Peso: " + getPesoPonderacaoFormatado();
		} else {
			return "Realizado Ponderação: " + getRealizadoPonderacaoFormatado();
		}
	}
	public String getSalarioMinimoClasseHayFormatada() {
		if (salarioMinimoClasseHay == null) return "Não existente";
		return NumeroHelper.formataNumero(salarioMinimoClasseHay, new Integer(Constantes.UNIDADE_VALOR_SEM_RS));
	}
	
	public String getSalarioMinimoXPonderacaoFormatado() {
		if (salarioMinimoClasseHay == null || realizadoPonderacao == null) return "Não existente";
		double salarioMinimoXPonderacao = salarioMinimoClasseHay.doubleValue() * (realizadoPonderacao.doubleValue()/100);
		return NumeroHelper.formataNumero(new Double(salarioMinimoXPonderacao), new Integer(Constantes.UNIDADE_VALOR_SEM_RS));
	}
	public int getStatusCalculoRealizado() {
		return statusCalculoRealizado;
	}
	public void setStatusCalculoRealizado(int statusCalculoRealizado) {
		this.statusCalculoRealizado = statusCalculoRealizado;
	}
	public Date getDtIniSit() {
		return dtIniSit;
	}
	public void setDtIniSit(Date dtIniSit) {
		this.dtIniSit = dtIniSit;
	}
	public Double getPeso() {
		return peso;
	}

	public String getPesoFormatado() {
		return NumeroHelper.formataNumero(this.peso, this.unidadePeso);
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}
	public Integer getUnidadePeso() {
		return unidadePeso;
	}
	public void setUnidadePeso(Integer unidadePeso) {
		this.unidadePeso = unidadePeso;
	}		
	
	
}