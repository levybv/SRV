package br.com.marisa.srv.escala.vo;

import java.util.Date;

import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * VO para manter os dados da Escala
 * 
 * @author Walter Fontes
 */
public class FaixaEscalaVO {

	private Integer idEscala;
	private Integer	sequencial;
	private Double  faixaInicial;
	private Double	faixaFinal;
	private Integer idUnidadeFaixa;
	private Double	realizado;
	private Integer idUnidadeRealizado;
	private Boolean	percentual;
	private Double	limite;
	private Date    dataUltimaAlteracao;
	private Integer idUsuarioUltimaAlteracao;
	
	//Formatações
	public String getFaixaInicialFormatada() {
		return NumeroHelper.formataValores(faixaInicial, idUnidadeFaixa);
	}	
	public String getFaixaFinalFormatada() {
		return NumeroHelper.formataValores(faixaFinal, idUnidadeFaixa);
	}	
	public String getRealizadoFormatado() {
		return NumeroHelper.formataValores(realizado, idUnidadeRealizado);
	}	
	public String getPercentualFormatado() {
		return percentual==null ? "" : percentual.booleanValue() ? "Sim": "Não";
	}	
	public String getLimiteFormatado() {
		return NumeroHelper.formataNumero(limite, idUnidadeRealizado);
	}
	
	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}
	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	public Double getFaixaFinal() {
		return faixaFinal;
	}
	public void setFaixaFinal(Double faixaFinal) {
		this.faixaFinal = faixaFinal;
	}
	public Double getFaixaInicial() {
		return faixaInicial;
	}
	public void setFaixaInicial(Double faixaInicial) {
		this.faixaInicial = faixaInicial;
	}
	public Integer getIdEscala() {
		return idEscala;
	}
	public void setIdEscala(Integer idEscala) {
		this.idEscala = idEscala;
	}
	public Integer getIdUnidadeFaixa() {
		return idUnidadeFaixa;
	}
	public void setIdUnidadeFaixa(Integer idUnidadeFaixa) {
		this.idUnidadeFaixa = idUnidadeFaixa;
	}
	public Integer getIdUnidadeRealizado() {
		return idUnidadeRealizado;
	}
	public void setIdUnidadeRealizado(Integer idUnidadeRealizado) {
		this.idUnidadeRealizado = idUnidadeRealizado;
	}
	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}
	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}
	public Double getLimite() {
		return limite;
	}
	public void setLimite(Double limite) {
		this.limite = limite;
	}
	public Boolean getPercentual() {
		return percentual;
	}
	public void setPercentual(Boolean percentual) {
		this.percentual = percentual;
	}
	public Double getRealizado() {
		return realizado;
	}
	public void setRealizado(Double realizado) {
		this.realizado = realizado;
	}
	public Integer getSequencial() {
		return sequencial;
	}
	public void setSequencial(Integer sequencial) {
		this.sequencial = sequencial;
	}
}