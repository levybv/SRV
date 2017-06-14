package br.com.marisa.srv.escala.vo;

import java.util.Date;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * VO para manter os dados da Escala
 * 
 * @author Walter Fontes
 */
public class EscalaVO {

	private Integer idEscala;
	private String	descricaoEscala;
	private Integer idGrupoIndicador;
	private String	descricaoGrupoIndicador;
	private Integer idIndicador;
	private String	descricaoIndicador;
	private Integer idGrupoRemVar;
	private String	descricaoGrupoRemVar;
	private Boolean	percentual;
	private Double	limite;
	private Date    dataUltimaAlteracao;
	private Integer idUsuarioUltimaAlteracao;
	private String flgAplicaFxResultNoRealz;
	private Integer numEscala;
	
	
	
	public String getPercentualFormatado() {
		return percentual==null ? "" : percentual.booleanValue() ? "Sim": "Não";
	}	
	public String getLimiteFormatado() {
		return NumeroHelper.formataNumero(limite, new Integer(Constantes.UNIDADE_VALOR_SEM_RS));
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
	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}
	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	public String getDescricaoEscala() {
		return descricaoEscala;
	}
	public void setDescricaoEscala(String descricaoEscala) {
		this.descricaoEscala = descricaoEscala;
	}
	public String getDescricaoGrupoIndicador() {
		return descricaoGrupoIndicador;
	}
	public void setDescricaoGrupoIndicador(String descricaoGrupoIndicador) {
		this.descricaoGrupoIndicador = descricaoGrupoIndicador;
	}
	public String getDescricaoGrupoRemVar() {
		return descricaoGrupoRemVar;
	}
	public void setDescricaoGrupoRemVar(String descricaoGrupoRemVar) {
		this.descricaoGrupoRemVar = descricaoGrupoRemVar;
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
	public Integer getIdGrupoRemVar() {
		return idGrupoRemVar;
	}
	public void setIdGrupoRemVar(Integer idGrupoRemVar) {
		this.idGrupoRemVar = idGrupoRemVar;
	}
	public Integer getIdIndicador() {
		return idIndicador;
	}
	public void setIdIndicador(Integer idIndicador) {
		this.idIndicador = idIndicador;
	}
	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}
	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}
	public String getFlgAplicaFxResultNoRealz() {
		return flgAplicaFxResultNoRealz;
	}
	public void setFlgAplicaFxResultNoRealz(String flgAplicaFxResultNoRealz) {
		this.flgAplicaFxResultNoRealz = flgAplicaFxResultNoRealz;
	}
	public Integer getNumEscala() {
		return numEscala;
	}
	public void setNumEscala(Integer numEscala) {
		this.numEscala = numEscala;
	}
}