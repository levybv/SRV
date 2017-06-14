package br.com.marisa.srv.tlmkt.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.usuario.vo.UsuarioVO;

/**
 * 
 * @author Levy Villar
 *
 */
public class TelemarketingVO implements Serializable {

	private static final long serialVersionUID = 8737749450948680464L;

	private Integer ano;
	private Integer mes;
	private Date dtIniSitSrv;

	private List<FuncionarioVO> listaFuncionarioVO;
	private List<TelemarketingIndicadorVO> listaIndicadorTlmktVO;

	private UsuarioVO usuarioVO;

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

	public Date getDtIniSitSrv() {
		return dtIniSitSrv;
	}

	public void setDtIniSitSrv(Date dtIniSitSrv) {
		this.dtIniSitSrv = dtIniSitSrv;
	}

	public List<FuncionarioVO> getListaFuncionarioVO() {
		return listaFuncionarioVO;
	}

	public void setListaFuncionarioVO(List<FuncionarioVO> listaFuncionarioVO) {
		this.listaFuncionarioVO = listaFuncionarioVO;
	}

	public List<TelemarketingIndicadorVO> getListaIndicadorTlmktVO() {
		return listaIndicadorTlmktVO;
	}

	public void setListaIndicadorTlmktVO(List<TelemarketingIndicadorVO> listaIndicadorTlmktVO) {
		this.listaIndicadorTlmktVO = listaIndicadorTlmktVO;
	}

	public UsuarioVO getUsuarioVO() {
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

}
