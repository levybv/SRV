package br.com.marisa.srv.geral.enumeration;

/**
 * 
 * @author Levy Villar
 *
 */
public enum ExcelColunasEnum {

	/**
	 * 
	 */
	A(0,"A"), B(1,"B"), C(2,"C"), D(3,"D"), E(4,"E"), F(5,"F"), G(6,"G"), H(7,"H"), I(8,"I"), J(9,"J"), 
	K(10,"K"), L(11,"L"), M(12,"M"), N(13,"N"), O(14,"O"), P(15,"P"), Q(16,"Q"), R(17,"R"), S(18,"S"), T(19,"T"), 
	U(20,"U"), V(21,"V"), W(22,"W"), X(23,"X"), Y(24,"Y"), Z(25,"Z"), AA(26,"AA"), AB(27,"AB"), AC(28,"AC"), AD(29,"AD"), 
	AE(30,"AE"), AF(31,"AF"), AG(32,"AG"), AH(33,"AH"), AI(34,"AI"), AJ(35,"AJ"), AK(36,"AK"), AL(37,"AL"), AM(38,"AM"), AN(39,"AN"),
	AO(40,"AO"), AP(41,"AP"), AQ(42,"AQ"), AR(43,"AR"), AS(44,"AS"), AT(45,"AT"), AU(46,"AU"), AV(47,"AV"), AW(48,"AW"), AX(49,"AX"),
	AY(50,"AY"), AZ(51,"AZ"), BA(52,"BA"), BB(53,"BB"), BC(54,"BC"), BD(55,"BD"), BE(56,"BE"), BF(57,"BF"), BG(58,"BG"), BH(59,"BH"), 
	BI(60,"BI"), BJ(61,"BJ"), BK(62,"BK"), BL(63,"BL"), BM(64,"BM"), BN(65,"BN"), BO(66,"BO"), BP(67,"BP"), BQ(68,"BQ"), BR(69,"BR");

	/**
	 * 
	 */
	private int numeroColuna;
	private String descricaoColuna;

	/**
	 * 
	 * @param valor
	 * @param coluna
	 */
	ExcelColunasEnum(int valor, String coluna) {
		numeroColuna = valor;
		descricaoColuna = coluna;
	}

	/**
	 * 
	 * @return
	 */
	public int getNumeroColuna() {
		return numeroColuna;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescricaoColuna() {
		return descricaoColuna;
	}

}
