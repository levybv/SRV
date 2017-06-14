package br.com.marisa.srv.filial.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.marisa.srv.geral.helper.CNPJCPFHelper;

public class FilialVO implements Comparable<FilialVO>, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4970551943172072941L;

	private Integer codEmpresa;
	private Integer codFilial;
	private Integer codTpFilial;
	private String 	descricao;
	private String  descricaoTpFil;
	private String 	cnpj;
	private String 	uf;
	private Boolean flagAtivo;
	private String 	flagAtivoStr;
	private String 	cnpjStr;
	private Boolean flagMeta100;
	private Date	dataUltimaAlteracao;
	private Date	dataInauguracao;
	private String	dataStrInauguracao;
	private String  dataStringUltimaAlteracao;
	private Integer	idUsuarioUltimaAlteracao;

	public FilialVO() {
		super();
	}

	public FilialVO(Integer codFilial) {
		super();
		this.codFilial = codFilial;
	}

	//Obtém meta formatada
	public String getFlagMeta100Formatada() {
		return flagMeta100 == Boolean.TRUE?"Sim":"Não";
	}
	
	public String getDescricaoTpFil() {
		return descricaoTpFil;
	}
	public void setDescricaoTpFil(String descricaoTpFil) {
		this.descricaoTpFil = descricaoTpFil;
	}
	public String getDataStringUltimaAlteracao() {
		return dataStringUltimaAlteracao;
	}
	public void setDataStringUltimaAlteracao(String dataStringUltimaAlteracao) {
		this.dataStringUltimaAlteracao = dataStringUltimaAlteracao;
	}
	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}
	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}
	public Integer getIdUsuarioUltimaAlteracao() {
		return idUsuarioUltimaAlteracao;
	}
	public void setIdUsuarioUltimaAlteracao(Integer idUsuarioUltimaAlteracao) {
		this.idUsuarioUltimaAlteracao = idUsuarioUltimaAlteracao;
	}
	public Boolean getFlagMeta100() {
		return flagMeta100;
	}
	public void setFlagMeta100(Boolean flagMeta100) {
		this.flagMeta100 = flagMeta100;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
		cnpjStr = CNPJCPFHelper.formataCnpj(cnpj);
	}
	public Integer getCodEmpresa() {
		return codEmpresa;
	}
	public void setCodEmpresa(Integer codEmpresa) {
		this.codEmpresa = codEmpresa;
	}
	public Integer getCodFilial() {
		return codFilial;
	}
	public void setCodFilial(Integer codFilial) {
		this.codFilial = codFilial;
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
		this.flagAtivoStr = flagAtivo == Boolean.TRUE?"Sim":"Não";
	}
	public String getUf() {
		return uf;
	}
	public void setUf(String uf) {
		this.uf = uf;
	}
	public String getCnpjStr() {
		return cnpjStr;
	}
	public void setCnpjStr(String cnpjStr) {
		this.cnpjStr = cnpjStr;
	}
	public String getFlagAtivoStr() {
		return flagAtivoStr;
	}
	public void setFlagAtivoStr(String flagAtivoStr) {
		this.flagAtivoStr = flagAtivoStr;
	}

	public Integer getCodTpFilial() {
		return codTpFilial;
	}

	public void setCodTpFilial(Integer codTpFilial) {
		this.codTpFilial = codTpFilial;
	}

	
	public Date getDataInauguracao() {
	
		return dataInauguracao;
	}

	
	public void setDataInauguracao(Date dataInauguracao) {
	
		this.dataInauguracao = dataInauguracao;
	}

	
	public String getDataStrInauguracao() {
		if (dataInauguracao != null) {
			dataStrInauguracao = new SimpleDateFormat("dd/MM/yyyy").format(dataInauguracao);
		}
		return dataStrInauguracao;
	}

	
	public void setDataStrInauguracao(String dataStrInauguracao) {
		this.dataStrInauguracao = dataStrInauguracao;
	}

	@Override
	public int compareTo(FilialVO o) {
		return getCodFilial().compareTo(o.codFilial);
	}

}
