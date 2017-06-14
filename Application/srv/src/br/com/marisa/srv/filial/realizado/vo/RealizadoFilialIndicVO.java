package br.com.marisa.srv.filial.realizado.vo;

import java.io.Serializable;
import java.util.Date;

import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.indicador.vo.IndicadorVO;

/**
 * 
 * @author Levy Villar
 *
 */
public class RealizadoFilialIndicVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4314847301018352927L;

	private IndicadorVO indicadorVO;
	private FilialVO filialVO;
	private Integer ano;
	private Integer mes;
	private Double numRealizado;
	private Integer codUsuario;
	private Date dataIniSitSrv;

	public Integer getAno() {
		return ano;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Double getNumRealizado() {
		return numRealizado;
	}

	public void setNumRealizado(Double numRealizado) {
		this.numRealizado = numRealizado;
	}

	public Integer getCodUsuario() {
		return codUsuario;
	}

	public void setCodUsuario(Integer codUsuario) {
		this.codUsuario = codUsuario;
	}

	public Date getDataIniSitSrv() {
		return dataIniSitSrv;
	}

	public void setDataIniSitSrv(Date dataIniSitSrv) {
		this.dataIniSitSrv = dataIniSitSrv;
	}

	public IndicadorVO getIndicadorVO() {
		return indicadorVO;
	}

	public void setIndicadorVO(IndicadorVO indicadorVO) {
		this.indicadorVO = indicadorVO;
	}

	public FilialVO getFilialVO() {
		return filialVO;
	}

	public void setFilialVO(FilialVO filialVO) {
		this.filialVO = filialVO;
	}

}
