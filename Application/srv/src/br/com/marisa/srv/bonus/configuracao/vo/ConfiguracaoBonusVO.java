package br.com.marisa.srv.bonus.configuracao.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.marisa.srv.escala.vo.EscalaVO;

public class ConfiguracaoBonusVO implements Serializable {

	private static final long serialVersionUID = -1143018241485153771L;

	private Integer ano;
	private Integer codIndicIni;
	private Integer codIndicFim;
	private Boolean isFunding;
	private Double funding;
	private Boolean isContratoMeta;
	private Date dataLimiteAceite;
	private String periodoDisponivel;
	private String textoConsentimento;
	private Boolean isEncerrado;
	private String flgEncerrado;
	private Long idFuncionarioCorporativo;
	private String nomeFuncionarioCorporativo;
	private Date dtIniSitSrv;
	private Integer codUsuario;
	private List<EscalaVO> listaEscala;

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getCodIndicIni() {
		return codIndicIni;
	}

	public void setCodIndicIni(Integer codIndicIni) {
		this.codIndicIni = codIndicIni;
	}

	public Integer getCodIndicFim() {
		return codIndicFim;
	}

	public void setCodIndicFim(Integer codIndicFim) {
		this.codIndicFim = codIndicFim;
	}

	public Double getFunding() {
		return funding;
	}

	public void setFunding(Double funding) {
		this.funding = funding;
	}

	public Date getDataLimiteAceite() {
		return dataLimiteAceite;
	}

	public void setDataLimiteAceite(Date dataLimiteAceite) {
		this.dataLimiteAceite = dataLimiteAceite;
	}

	public String getTextoConsentimento() {
		return textoConsentimento;
	}

	public void setTextoConsentimento(String textoConsentimento) {
		this.textoConsentimento = textoConsentimento;
	}

	public List<EscalaVO> getListaEscala() {
		return listaEscala;
	}

	public void setListaEscala(List<EscalaVO> listaEscala) {
		this.listaEscala = listaEscala;
	}

	public Boolean getIsFunding() {
		return isFunding;
	}

	public void setIsFunding(Boolean isFunding) {
		this.isFunding = isFunding;
	}

	public Boolean getIsContratoMeta() {
		return isContratoMeta;
	}

	public void setIsContratoMeta(Boolean isContratoMeta) {
		this.isContratoMeta = isContratoMeta;
	}

	public Boolean getIsEncerrado() {
		return isEncerrado;
	}

	public void setIsEncerrado(Boolean isEncerrado) {
		this.isEncerrado = isEncerrado;
	}

	public String getPeriodoDisponivel() {
		return periodoDisponivel;
	}

	public void setPeriodoDisponivel(String periodoDisponivel) {
		this.periodoDisponivel = periodoDisponivel;
	}

	public Date getDtIniSitSrv() {
		return dtIniSitSrv;
	}

	public void setDtIniSitSrv(Date dtIniSitSrv) {
		this.dtIniSitSrv = dtIniSitSrv;
	}

	public Integer getCodUsuario() {
		return codUsuario;
	}

	public void setCodUsuario(Integer codUsuario) {
		this.codUsuario = codUsuario;
	}

	public Long getIdFuncionarioCorporativo() {
		return idFuncionarioCorporativo;
	}

	public void setIdFuncionarioCorporativo(Long idFuncionarioCorporativo) {
		this.idFuncionarioCorporativo = idFuncionarioCorporativo;
	}

	public String getNomeFuncionarioCorporativo() {
		return nomeFuncionarioCorporativo;
	}

	public void setNomeFuncionarioCorporativo(String nomeFuncionarioCorporativo) {
		this.nomeFuncionarioCorporativo = nomeFuncionarioCorporativo;
	}

	public String getFlgEncerrado() {
		flgEncerrado = isEncerrado == null ? null : (isEncerrado.booleanValue()==true?"S":"N");
		return flgEncerrado;
	}

	public void setFlgEncerrado(String flgEncerrado) {
		this.flgEncerrado = flgEncerrado;
	}

}
