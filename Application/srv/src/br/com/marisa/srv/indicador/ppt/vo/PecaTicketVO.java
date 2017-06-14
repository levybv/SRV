package br.com.marisa.srv.indicador.ppt.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.marisa.srv.geral.helper.NumeroHelper;

/**
 * 
 * @author levy.villar
 *
 */
public class PecaTicketVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7500336378519369482L;

	private Integer codFilial;
	private String descrFilial;
	private Integer codEmpresa;
	private Integer numAno;
	private Integer numMes;
	private Double numRealizado;
	private Integer codUniRealizado;
	private String realizadoFormatado;
	private Integer idUsuario;
	private Date dataAlteracao;

	public Integer getCodFilial() {
		return codFilial;
	}

	public void setCodFilial(Integer codFilial) {
		this.codFilial = codFilial;
	}

	public Integer getCodEmpresa() {
		return codEmpresa;
	}

	public void setCodEmpresa(Integer codEmpresa) {
		this.codEmpresa = codEmpresa;
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

	public Double getNumRealizado() {
		return numRealizado;
	}

	public void setNumRealizado(Double numRealizado) {
		this.numRealizado = numRealizado;
	}

	public Integer getCodUniRealizado() {
		return codUniRealizado;
	}

	public void setCodUniRealizado(Integer codUniRealizado) {
		this.codUniRealizado = codUniRealizado;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getRealizadoFormatado() {
		realizadoFormatado = NumeroHelper.formataNumero(numRealizado, codUniRealizado);
		return realizadoFormatado;
	}

	public String getDescrFilial() {
		return descrFilial;
	}

	public void setDescrFilial(String descrFilial) {
		this.descrFilial = descrFilial;
	}

}