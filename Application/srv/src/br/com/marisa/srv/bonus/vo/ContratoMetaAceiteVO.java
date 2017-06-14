package br.com.marisa.srv.bonus.vo;

import java.io.Serializable;
import java.util.Date;

public class ContratoMetaAceiteVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1509929494755459385L;

	private Long codFuncionario;
	private Integer numAno;
	private Integer numMes;
	private Integer idFilial;
	private Integer idEmpresa;
	private Date dataAceite;
	private Integer codUsuario;

	private Integer idStatusAceite;
	private String descStatusAceite;

	public Long getCodFuncionario() {
		return codFuncionario;
	}

	public void setCodFuncionario(Long codFuncionario) {
		this.codFuncionario = codFuncionario;
	}

	public Date getDataAceite() {
		return dataAceite;
	}

	public void setDataAceite(Date dataAceite) {
		this.dataAceite = dataAceite;
	}

	public Integer getCodUsuario() {
		return codUsuario;
	}

	public void setCodUsuario(Integer codUsuario) {
		this.codUsuario = codUsuario;
	}

	public Integer getNumAno() {
		return numAno;
	}

	public void setNumAno(Integer numAno) {
		this.numAno = numAno;
	}

	public Integer getNumMes() {
		return numMes;
	}

	public void setNumMes(Integer numMes) {
		this.numMes = numMes;
	}

	public Integer getIdFilial() {
		return idFilial;
	}

	public void setIdFilial(Integer idFilial) {
		this.idFilial = idFilial;
	}

	public Integer getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public Integer getIdStatusAceite() {
		return idStatusAceite;
	}

	public void setIdStatusAceite(Integer idStatusAceite) {
		this.idStatusAceite = idStatusAceite;
	}

	public String getDescStatusAceite() {
		return descStatusAceite;
	}

	public void setDescStatusAceite(String descStatusAceite) {
		this.descStatusAceite = descStatusAceite;
	}

}
