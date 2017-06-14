package br.com.marisa.srv.util.vo;

import java.io.Serializable;

/**
 * 
 * @author levy.villar
 *
 */
public class MesVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1371431710415866258L;

	private int numMes;
	private String descMes;

	public MesVO() {
	}

	public MesVO(int numMes, String descMes) {
		this.numMes = numMes;
		this.descMes = descMes;
	}

	public int getNumMes() {
		return numMes;
	}

	public void setNumMes(int numMes) {
		this.numMes = numMes;
	}

	public String getDescMes() {
		return descMes;
	}

	public void setDescMes(String descMes) {
		this.descMes = descMes;
	}

}
