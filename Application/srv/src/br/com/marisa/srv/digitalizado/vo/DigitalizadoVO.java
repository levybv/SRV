package br.com.marisa.srv.digitalizado.vo;

public class DigitalizadoVO {

	private long cliCpf;
	private String codContrato;
	private int filCod;
	private long cpfVendedor;
	
	private String mes;
	private String ano;
	
	public long getCliCpf() {
	
		return cliCpf;
	}
	
	public void setCliCpf(long cliCpf) {
	
		this.cliCpf = cliCpf;
	}
	
	public String getCodContrato() {
	
		return codContrato;
	}
	
	public void setCodContrato(String codContrato) {
	
		this.codContrato = codContrato;
	}
	
	public int getFilCod() {
	
		return filCod;
	}
	
	public void setFilCod(int filCod) {
	
		this.filCod = filCod;
	}

	
	public String getMes() {
	
		return mes;
	}

	
	public void setMes(String mes) {
	
		this.mes = mes;
	}

	
	public String getAno() {
	
		return ano;
	}

	
	public void setAno(String ano) {
	
		this.ano = ano;
	}

	
	public long getCpfVendedor() {
	
		return cpfVendedor;
	}

	
	public void setCpfVendedor(long cpfVendedor) {
	
		this.cpfVendedor = cpfVendedor;
	}

}