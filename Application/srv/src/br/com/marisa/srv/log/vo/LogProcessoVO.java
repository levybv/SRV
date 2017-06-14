package br.com.marisa.srv.log.vo;

import java.io.Serializable;
import java.util.Date;

public class LogProcessoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -601269155868953940L;

	private Integer codProc;
	private String descrProc;
	private String flgAtivProc;
	private Integer ordemProc;
	private Integer anoProc;
	private Integer mesProc;
	private Date dtInicio;
	private Date dtFim;
	private Integer statProc;
	private int statProcInt;
	private String statProcStr;
	private String obsExec;
	private Integer filLog;
	private Integer funcLog;
	private Integer indicLog;
	private String erroLog;

	public LogProcessoVO() {
	}

	public Integer getAnoProc() {
		return anoProc;
	}

	public void setAnoProc(Integer anoProc) {
		this.anoProc = anoProc;
	}

	public Integer getCodProc() {
		return codProc;
	}

	public void setCodProc(Integer codProc) {
		this.codProc = codProc;
	}

	public String getDescrProc() {
		return descrProc;
	}

	public void setDescrProc(String descrProc) {
		this.descrProc = descrProc;
	}

	public Date getDtFim() {
		return dtFim;
	}

	public void setDtFim(Date dtFim) {
		this.dtFim = dtFim;
	}

	public Date getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
	}

	public String getErroLog() {
		return erroLog;
	}

	public void setErroLog(String erroLog) {
		this.erroLog = erroLog;
	}

	public Integer getFilLog() {
		return filLog;
	}

	public void setFilLog(Integer filLog) {
		this.filLog = filLog;
	}

	public String getFlgAtivProc() {
		return flgAtivProc;
	}

	public void setFlgAtivProc(String flgAtivProc) {
		this.flgAtivProc = flgAtivProc;
	}

	public Integer getFuncLog() {
		return funcLog;
	}

	public void setFuncLog(Integer funcLog) {
		this.funcLog = funcLog;
	}

	public Integer getIndicLog() {
		return indicLog;
	}

	public void setIndicLog(Integer indicLog) {
		this.indicLog = indicLog;
	}

	public Integer getMesProc() {
		return mesProc;
	}

	public void setMesProc(Integer mesProc) {
		this.mesProc = mesProc;
	}

	public String getObsExec() {
		return obsExec;
	}

	public void setObsExec(String obsExec) {
		this.obsExec = obsExec;
	}

	public Integer getOrdemProc() {
		return ordemProc;
	}

	public void setOrdemProc(Integer ordemProc) {
		this.ordemProc = ordemProc;
	}

	public Integer getStatProc() {
		return statProc;
	}

	public void setStatProc(Integer statProc) {
		this.statProc = statProc;
	}

	public int getStatProcInt() {
		return statProcInt;
	}

	public void setStatProcInt(int statProcInt) {
		this.statProcInt = statProcInt;
	}

	public String getStatProcStr() {
		return statProcStr;
	}

	public void setStatProcStr(String statProcStr) {
		this.statProcStr = statProcStr;
	}
}
