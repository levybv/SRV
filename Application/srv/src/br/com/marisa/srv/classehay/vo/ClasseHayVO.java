package br.com.marisa.srv.classehay.vo;

import java.util.Date;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * VO para manter os dados da Classe Hay
 * 
 * @author Walter Fontes
 */
public class ClasseHayVO {

	private Integer idClasseHay;
	private String	descricao;
	private Double  salarioMinimo;
	private Double	salarioMaximo;
	private Date    dataUltimaAlteracao;
	private Integer idUsuarioUltimaAlteracao;
	
	
	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}
	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getIdClasseHay() {
		return idClasseHay;
	}
	public void setIdClasseHay(Integer idClasseHay) {
		this.idClasseHay = idClasseHay;
	}
	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}
	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}
	public Double getSalarioMaximo() {
		return salarioMaximo;
	}
	public String getSalarioMaximoFormatado() {
		return NumeroHelper.formataNumero(salarioMaximo, Constantes.UNIDADE_VALOR_SEM_RS);
	}
	public String getSalarioMaximoFormatadoAlteracao() {
		return NumeroHelper.formataNumero(salarioMaximo, Constantes.UNIDADE_VALOR_SEM_RS);
	}	
	public void setSalarioMaximo(Double salarioMaximo) {
		this.salarioMaximo = salarioMaximo;
	}
	public Double getSalarioMinimo() {
		return salarioMinimo;
	}
	public String getSalarioMinimoFormatado() {
		return NumeroHelper.formataNumero(salarioMinimo, Constantes.UNIDADE_VALOR_SEM_RS);
	}
	public String getSalarioMinimoFormatadoAlteracao() {
		return NumeroHelper.formataNumero(salarioMinimo, Constantes.UNIDADE_VALOR_SEM_RS);
	}		
	public void setSalarioMinimo(Double salarioMinimo) {
		this.salarioMinimo = salarioMinimo;
	}
	
	
}
