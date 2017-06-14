package br.com.marisa.srv.gerente.filial.vo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.marisa.srv.filial.vo.FilialVO;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;

/**
 * 
 * @author levy.villar
 * 
 */
public class GerenteVO extends FuncionarioVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1000698245271110069L;

	private FilialVO filialOrigem;
	private FilialVO filial;
	private Date dataInclusaoFilial;
	private String dataInclusaoFilialFmt;
	private Date dataExclusaoFilial;
	private Integer codUsuario;
	private Integer statusCarga;
	private Integer codAtuacao;

	public FilialVO getFilialOrigem() {
		return filialOrigem;
	}

	public void setFilialOrigem(FilialVO filialOrigem) {
		this.filialOrigem = filialOrigem;
	}

	public Date getDataInclusaoFilial() {
		return dataInclusaoFilial;
	}

	public void setDataInclusaoFilial(Date dataInclusaoFilial) {
		this.dataInclusaoFilial = dataInclusaoFilial;
	}

	public Date getDataExclusaoFilial() {
		return dataExclusaoFilial;
	}

	public void setDataExclusaoFilial(Date dataExclusaoFilial) {
		this.dataExclusaoFilial = dataExclusaoFilial;
	}

	public Integer getCodUsuario() {
		return codUsuario;
	}

	public void setCodUsuario(Integer codUsuario) {
		this.codUsuario = codUsuario;
	}

	public Integer getStatusCarga() {
		return statusCarga;
	}

	public void setStatusCarga(Integer statusCarga) {
		this.statusCarga = statusCarga;
	}

	public Integer getCodAtuacao() {
		return codAtuacao;
	}

	public void setCodAtuacao(Integer codAtuacao) {
		this.codAtuacao = codAtuacao;
	}

	public FilialVO getFilial() {
		return filial;
	}

	public void setFilial(FilialVO filial) {
		this.filial = filial;
	}

	public String getDataInclusaoFilialFmt() {
		dataInclusaoFilialFmt = "";
		if (getDataInclusaoFilial()!=null) {
			dataInclusaoFilialFmt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(getDataInclusaoFilial());
		}
		return dataInclusaoFilialFmt;
	}

	public void setDataInclusaoFilialFmt(String dataInclusaoFilialFmt) {
		this.dataInclusaoFilialFmt = dataInclusaoFilialFmt;
	}

}