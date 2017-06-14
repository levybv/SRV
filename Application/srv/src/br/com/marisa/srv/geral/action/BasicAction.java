package br.com.marisa.srv.geral.action;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.excecoes.ApresentacaoException;
import br.com.marisa.srv.geral.excecoes.SRVException;
import br.com.marisa.srv.geral.excecoes.SegurancaException;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.geral.vo.ErroVO;
import br.com.marisa.srv.geral.vo.UsuarioBean;

/**
 * Action para ser herdado por todos os demais actions do sistema,
 * contendo os métodos default
 *  
 * @author Walter Fontes
 */
public class BasicAction extends DispatchAction {

	//Loj4J
	private final Logger log = Logger.getLogger(BasicAction.class);
	
	public static final String PAGINA_INICIAL = "paginaInicial";

	/**
	 * Metodo responsável por verificar se a alteração é passivel ou não de excluir token
	 * 
	 * 
	 * 
	 * @param request
	 * @return
	 */
	private boolean isExcluirtoken(HttpServletRequest request){
		
		String metodo = getStringParam(request, "method");
		//garante que o token só será excluido quando terminar as transações.
		return !(metodo != null && (
										metodo.equalsIgnoreCase("home") || 
										metodo.startsWith("prepara") 	||
										metodo.startsWith("obtemTreeViewJSON") ||
										metodo.startsWith("abrir") ||
										metodo.startsWith("pesquisa") ||
										metodo.startsWith("gera")
						));
	}
	
	/**
	 * Ação padrão executada toda vez que uma action é chamada, independente do método solicitado. Aqui colocamos os comportamenos 
	 * padrões para todos os métodos, e ao invocar super.execute(...) o método apropriado será executado.
	 * 
	 * @param mapping   Objeto Struts que representa a navegação das páginas
	 * @param form      Objeto Struts que representa o formulário de entrada de dados
	 * @param request   Requisição HTTP
	 * @param response  Resposta HTTP
	 * @throws Exception
	 * @return Caminho da página a ser chamada
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean excluirToken  = isExcluirtoken(request);
		
		// Executa o método apropriado, e caso receba uma CCException, apresenta mensagem de erro na tela apropriada
		try {
			
			if (!request.getRequestURI().endsWith("principal.do") &&
				!request.getRequestURI().endsWith("login.do") &&
				request.getSession().getAttribute(ConstantesRequestSession.SESSION_USUARIO_BEAN) == null) {
				log.debug("Senha expirada. Redirecionando para o login");
				setMensagem(request, "Sua sessão expirou. Favor realizar o login novamente.");
				return (mapping.findForward("login"));
			}
			
			preExecute(mapping, form, request, response);
			return super.execute(mapping, form, request, response);

		} catch (SegurancaException e) {
			log.debug("Erro do tipo SegurancaException", e);
			request.setAttribute( "erro", getErro( e ) );
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			
			String tokenName = "";
			Enumeration attributeNames = request.getSession().getAttributeNames();
			while (attributeNames.hasMoreElements()) {
				String names = (String) attributeNames.nextElement();
				
				if(names.startsWith(Constantes.PRE_TOKEN)){
					//System.out.println("SegurancaException");
					//System.out.println(names);
					tokenName=names;
				}
			}
			//System.out.println("exception o token será guardado");
			saveToken(tokenName,request);
			excluirToken = false;
			//System.out.println("o token foi salvo");
			
			
			return (mapping.findForward("erro"));

		} catch (ApresentacaoException e) {
			log.debug("Erro do tipo ApresentacaoException", e);
			request.setAttribute( "erro", getErro( e ) );
			request.setAttribute( "mensagemErro", e.getMessage() );
			//response.sendRedirect("../WEB-INF/JSP/geral/error.jsp");
			//return null;
			
			String tokenName = "";
			Enumeration attributeNames = request.getSession().getAttributeNames();
			while (attributeNames.hasMoreElements()) {
				String names = (String) attributeNames.nextElement();
				
				if(names.startsWith(Constantes.PRE_TOKEN)){
					//System.out.println("SegurancaException");
					//System.out.println(names);
					tokenName=names;
				}
			}
			//System.out.println("exception o token será guardado");
			saveToken(tokenName,request);
			excluirToken = false;
			//System.out.println("o token foi salvo");
			
			
			return (mapping.findForward("erro"));

		} catch (SRVException e) {
			log.debug("Erro do tipo SRVException", e);
			request.setAttribute( "erro", getErro( e ) );
			
			String tokenName = "";
			Enumeration attributeNames = request.getSession().getAttributeNames();
			while (attributeNames.hasMoreElements()) {
				String names = (String) attributeNames.nextElement();
				
				if(names.startsWith(Constantes.PRE_TOKEN)){
					//System.out.println("SegurancaException");
					//System.out.println(names);
					tokenName=names;
				}
			}
			//System.out.println("exception o token será guardado");
			saveToken(tokenName,request);
			excluirToken = false;
			//System.out.println("o token foi salvo");
			
				
			return (mapping.findForward("erro"));

		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Erro do tipo Exception", e);
			request.setAttribute( "erro", getErro( e ) );
			
			String tokenName = "";
			Enumeration attributeNames = request.getSession().getAttributeNames();
			while (attributeNames.hasMoreElements()) {
				String names = (String) attributeNames.nextElement();
				
				if(names.startsWith(Constantes.PRE_TOKEN)){
					//System.out.println("SegurancaException");
					//System.out.println(names);
					tokenName=names;
				}
			}
			//System.out.println("exception o token será guardado");
			saveToken(tokenName,request);
			excluirToken = false;
			//System.out.println("o token foi salvo");
			
				
			return (mapping.findForward("erro"));
		}finally{
			//Enumeration asdf = request.getSession().getAttributeNames();
			//System.out.println("finallll");
//			while (asdf.hasMoreElements()) {
//				String nome = (String) asdf.nextElement();
//				
//				if(nome.startsWith(Constantes.PRE_TOKEN)){
//			
//					System.out.println("Final nome dos atributos é "+nome);
//
//				}
//			}
			
			if(excluirToken){
				String nomeToken = "";
				Enumeration attributeNames = request.getSession().getAttributeNames();
				while (attributeNames.hasMoreElements()) {
					String nome = (String) attributeNames.nextElement();
					
					if(nome.startsWith(Constantes.PRE_TOKEN)){
				
						//System.out.println(nome);
						nomeToken = nome;

					}
				}
				//System.out.println("finalizando retirando o token da sessão");
				resetToken(nomeToken, request);
				//System.out.println("o token foi retirando da sessão");
			}/*else{
				System.out.println("o token não foi retirado da sessão");
			}*/
		}
	}

	/**
	 * Este método é executado toda vez que um request é enviado para a Action, antes de iniciar o processamento do método apropriado. 
	 * Aqui devem ser colocados comandos a serem executados independentemente do método, tais como validação de senha forte, aribuição 
	 * de dados no request etc.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void preExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Sem implementação, deve ser sobrescrito pelas classes herdeiras caso seja necessário
	}

	/**
	 * Retorna um objeto do tipo <code>ErroVO</code> de acordo com os dados da 
	 * <code>Exception</code> fornecidos como parâmetros
	 * 
	 * @param e
	 * @return O objeto do tipo <code>ErroVO</code>
	 */
	private ErroVO getErro( Exception e ){
		ErroVO erro = new ErroVO();

		erro.setTipoErro( e.getClass().toString() );

		if (e.getMessage()==null || e.getClass().toString().equals("Exception"))
			erro.setMensagemErro("Erro ao executar a operação requisitada.");
		else
			erro.setMensagemErro(e.getMessage());

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		pw.close();

		erro.setStackErro( sw.getBuffer().toString() );
		erro.setLocalErro( e.getLocalizedMessage() );
		erro.setExcecao( e );

		return erro;
	}

	/**
	 * Obtém um parametro do tipo String do Request. Se o parametro vier vazio ou não for informado, esta rotina retorna <code>null</code>.
	 * 
	 * @param req Objeto HttpServletRequest
	 * @param param Nome do campo do form a ser lido
	 * @return Retorna o valor lido. Caso nao tenha sido enviado nenhum valor, a rotina retorna <code>null</code>.
	 */
	public String getStringParam(HttpServletRequest req, String param) {
		String result = req.getParameter(param);
		if ( result == null || "".equals(result.trim())) {
			return null;
		}
		return result;
	}

	/**
	 * Obtém um parametro do tipo Integer do Request. Se o parametro vier vazio ou não for informado, esta rotina retorna <code>null</code>.
	 * 
	 * @param req Objeto HttpServletRequest
	 * @param param Nome do campo do form a ser lido
	 * @return Retorna o valor lido. Caso nao tenha sido enviado nenhum valor, a rotina retorna <code>null</code>
	 * @throws ApresentacaoException
	 */
	public Integer getIntegerParam(HttpServletRequest req, String param) throws ApresentacaoException {
		String valor = req.getParameter(param);
		if (valor != null) {
			valor = valor.trim();
		}

		if ( valor == null || "".equals(valor)) {
			return null;
		}
		try {
			return Integer.valueOf(valor);
		} catch (Exception e) {
			log.error("O valor \"" + valor + "\" não é um valor válido para o campo. É esperado um valor numérico.", e);
			throw new ApresentacaoException("O valor \"" + valor + "\" não é um valor válido para o campo. É esperado um valor numérico.", e);
		}
	}
	
	
	/**
	 * Obtém um parametro do tipo Integer do Request. Se o parametro vier vazio ou não for informado, esta rotina retorna <code>null</code>.
	 * 
	 * @param req Objeto HttpServletRequest
	 * @param param Nome do campo do form a ser lido
	 * @return Retorna o valor lido. Caso nao tenha sido enviado nenhum valor, a rotina retorna <code>null</code>
	 * @throws ApresentacaoException
	 */
	public int getIntParam(HttpServletRequest req, String param) throws ApresentacaoException {
		String valor = req.getParameter(param);
		if (valor != null) {
			valor = valor.trim();
		}

		if ( valor == null || "".equals(valor)) {
			return Constantes.TIPO_INT_NULO;
		}
		try {
			return Integer.parseInt(valor);
		} catch (Exception e) {
			log.error("O valor \"" + valor + "\" não é um valor válido para o campo. É esperado um valor numérico.", e);
			throw new ApresentacaoException("O valor \"" + valor + "\" não é um valor válido para o campo. É esperado um valor numérico.", e);
		}
	}	
	

	/**
	 * Obtém um parametro do tipo Integer do Request. Se o parametro vier vazio, não for informado ou vier 0 (zero), esta rotina retorna <code>null</code>.
	 * 
	 * @param req Objeto HttpServletRequest
	 * @param param Nome do campo do form a ser lido
	 * @return Retorna o valor lido. Caso nao tenha sido enviado nenhum valor, a rotina retorna <code>null</code>
	 * @throws ApresentacaoException
	 */
	public Integer getIntegerParamIgnoreZero(HttpServletRequest req, String param) throws ApresentacaoException {
		String valor = req.getParameter(param);
		if (valor != null) {
			valor = valor.trim();
		}

		if ( valor == null || valor.equals("") ) {
			return null;
		}

		if ( Integer.parseInt( valor ) == 0 ){
			return null;
		}
		try {
			return Integer.valueOf(valor);
		} catch (Exception e) {
			log.error("O valor \"" + valor + "\" não é um valor válido para o campo. É esperado um valor numérico.", e);
			throw new ApresentacaoException("O valor \"" + valor + "\" não é um valor válido para o campo. É esperado um valor numérico.", e);
		}
	}

	/**
	 * Obtém um parametro do tipo Date do Request. Se o parametro vier vazio ou não for informado, esta rotina retorna <code>null</code>.
	 * 
	 * @param req Objeto HttpServletRequest
	 * @param param Nome do campo do form a ser lido
	 * @throws ApresentacaoException
	 * @return Retorna o valor lido. Caso nao tenha sido enviado nenhum valor, a rotina retorna <code>null</code>.
	 */
	public Date getDateParam(HttpServletRequest req, String param) throws ApresentacaoException {
		String valor = req.getParameter(param);
		if (valor != null) {
			valor = valor.trim();
		}

		if ( valor == null || "".equals(valor)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		try {
			return sdf.parse(valor);
		} catch (ParseException e) {
			log.error("A data \"" + valor + "\" não é uma data válida. A data deve estar no formato dd/mm/aaaa.");
			throw new ApresentacaoException("A data \"" + valor + "\" não é uma data válida. A data deve estar no formato dd/mm/aaaa.");
		}
	}

	/**
	 * Obtém um parametro do tipo Date do Request. Se o parametro vier vazio ou não for informado, esta rotina retorna <code>null</code>.
	 * 
	 * @param req Objeto HttpServletRequest
	 * @param param Nome do campo do form a ser lido
	 * @throws ApresentacaoException
	 * @return Retorna o valor lido. Caso nao tenha sido enviado nenhum valor, a rotina retorna <code>null</code>.
	 */
	public Date getDayMonthParam(HttpServletRequest req, String param) throws ApresentacaoException {
		String valor = req.getParameter(param);
		if (valor != null) {
			valor = valor.trim();
		}

		if ( valor == null || "".equals(valor)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
		sdf.setLenient(false);
		try {
			return sdf.parse(valor);
		} catch (ParseException e) {
			log.error("A data \"" + valor + "\" não é uma data válida. A data deve estar no formato dd/mm.");
			throw new ApresentacaoException("A data \"" + valor + "\" não é uma data válida. A data deve estar no formato dd/mm.");
		}
	}


	/**
	 * Obtém um parametro do tipo Date do Request. Se o parametro vier vazio ou não for informado, esta rotina retorna <code>null</code>.
	 * 
	 * @param req Objeto HttpServletRequest
	 * @param param Nome do campo do form a ser lido
	 * @throws ApresentacaoException
	 * @return Retorna o valor lido. Caso nao tenha sido enviado nenhum valor, a rotina retorna <code>null</code>.
	 */
	public Date getMonthYearParam(HttpServletRequest req, String param) throws ApresentacaoException {
		String valor = req.getParameter(param);
		if (valor != null) {
			valor = valor.trim();
		}

		if ( valor == null || "".equals(valor)) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
		sdf.setLenient(false);
		try {
			return sdf.parse(valor);
		} catch (ParseException e) {
			log.error("A data \"" + valor + "\" não é uma data válida. A data deve estar no formato mm/yyyy.");
			throw new ApresentacaoException("A data \"" + valor + "\" não é uma data válida. A data deve estar no formato mm/yyyy.");
		}
	}      


	/**
	 * Obtém um parametro do tipo Date do Request. Se o parametro vier vazio ou não for informado, esta rotina retorna <code>null</code>.
	 * 
	 * @param req Objeto HttpServletRequest
	 * @param paramDate Nome do campo do form de data a ser lido
	 * @param paramHour Nome do campo do form de hora a ser lido
	 * @throws ApresentacaoException
	 * @return Retorna o valor lido. Caso nao tenha sido enviado nenhum valor, a rotina retorna <code>null</code>.
	 */
	public Date getDateParam(HttpServletRequest req, String paramDate, String paramHour) throws ApresentacaoException {
		String valorData = req.getParameter(paramDate);
		if (valorData != null) {
			valorData = valorData.trim();
		}
		if ( valorData == null || "".equals(valorData)) {
			return null;
		}
		String valorHora = req.getParameter(paramHour);
		if (valorHora != null) {
			valorHora = valorHora.trim();
		}
		if ( valorHora == null || "".equals(valorHora)) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyyHH:mm");
		sdf.setLenient(false);
		try {
			return sdf.parse(valorData + valorHora);
		} catch (ParseException e) {
			log.error("A data/hora \"" + valorData + " - " + valorHora + "\" não é válida. A data deve estar no formato dd/mm/aaaa hh:mm.");
			throw new ApresentacaoException("A data/hora \"" + valorData + " - " + valorHora + "\" não é válida. A data deve estar no formato dd/mm/aaaa hh:mm.");
		}
	}    

	/**
	 * Obtém um parametro do tipo Double do Request. Se o parametro vier vazio ou não for informado, esta rotina retorna <code>null</code>.
	 * 
	 * @param req Objeto HttpServletRequest
	 * @param param Nome do campo do form a ser lido
	 * @param formatado 
	 *         true  - Obtém parâmetros formatados.     Ex.: 12.345,67
	 *         false - Obtém parâmetros não formatados. Ex.: 12345.67
	 * 
	 * @throws ApresentacaoException
	 * @return Retorna o valor lido. Caso nao tenha sido enviado nenhum valor, a rotina retorna <code>null</code>.
	 */
	public Double getDoubleParam(HttpServletRequest req, String param, boolean formatado) throws ApresentacaoException {
		String valor = req.getParameter(param);
		if (valor != null) {
			valor = valor.trim();
		}

		if ( valor == null || "".equals(valor)) {
			return null;
		}

		//Se o valor estiver formatado, retira a formatacao do mesmo
		if (formatado) {
			
			//Retira o R$
			if (valor.startsWith("R$")) {
				valor = valor.substring(2);			
			}
			
			//Retira o %
			if (valor.endsWith("%")) {
				valor = valor.substring(0, valor.length()-1);			
			}			
			
			//Retira os pontos
			valor = valor.replaceAll("\\.", "");

			//Troca a vírgula (se houver) por ponto
			valor = valor.replaceAll(",", "\\.");
		}

		try {
			return Double.valueOf(valor.trim());
		} catch (Exception e) {
			log.error("O valor \"" + valor + "\" não é um valor válido para o campo. É esperado um valor numérico.", e);
			throw new ApresentacaoException("O valor \"" + valor + "\" não é um valor válido para o campo. É esperado um valor numérico.", e);
		}
	}

	/**
	 * Obtém um parametro do tipo Long do Request. Se o parametro vier vazio ou não for informado, esta rotina retorna <code>null</code>.
	 * 
	 * @param req Objeto HttpServletRequest
	 * @param param Nome do campo do form a ser lido
	 * @return Retorna o valor lido. Caso nao tenha sido enviado nenhum valor, a rotina retorna <code>null</code>
	 * @throws ApresentacaoException
	 */
	public Long getLongParam(HttpServletRequest req, String param) throws ApresentacaoException  {
		String valor = req.getParameter(param);
		if (valor != null) {
			valor = valor.trim();
		}

		if ( valor == null || "".equals(valor)) {
			return null;
		}
		try {
			return new Long(valor);
		} catch (Exception e) {
			log.error("O valor \"" + valor + "\" não é um valor válido para o campo. É esperado um valor numérico.");
			throw new ApresentacaoException("O valor \"" + valor + "\" não é um valor válido para o campo. É esperado um valor numérico.");
		}
	}

	/**
	 * Obtém um parametro do tipo boolean do Request. Se o parametro for vazio ou não for informado ou então vier com valor "N", "n", "0" ou " ", esta rotina retorna
	 * <code>false</code>. Caso contrário retorna <code>true</code>.
	 * 
	 * @param req Objeto HttpServletRequest
	 * @param param Nome do campo do form a ser lido
	 * @return Retorna o valor lido. Caso nao tenha sido enviado nenhum valor, a rotina retorna <code>null</code>.
	 */
	public Boolean getBooleanParam(HttpServletRequest req, String param) {
		String valor = req.getParameter(param);
		if (valor == null) {
			return Boolean.FALSE;
		}
		if (getBoolParam(req, param)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Obtém um parametro do tipo boolean do Request. Se o parametro for vazio ou não for informado ou então vier com valor "N", "n", "0" ou " ", esta rotina retorna
	 * <code>false</code>. Caso contrário retorna <code>true</code>.
	 * 
	 * @param req Objeto HttpServletRequest
	 * @param param Nome do campo do form a ser lido
	 * @return Retorna o valor lido. Caso nao tenha sido enviado nenhum valor, a rotina retorna <code>null</code>.
	 */
	public boolean getBoolParam(HttpServletRequest req, String param) {
		String valor = req.getParameter(param);

		// Se for null retorna false
		if (valor == null) {
			return false;
		}

		valor = valor.trim();

		// Se for vazio retorna false
		if ( valor == null || "".equals(valor)) {
			return false;
		}

		if ("Nn0 ".indexOf(valor.charAt(0)) != -1) {
			return false;
		}

		return true;
	}

	/**
	 * Obtem um Array com objetos do tipo Integer a partir de um parâmetro multivalorado.
	 * 
	 * @param req
	 * @param param
	 * @return <code>java.util.List</code>
	 */
	public Integer[] getIntegerParamArray(HttpServletRequest req, String param) throws ApresentacaoException {
		String[] valores = req.getParameterValues(param);
		if (valores==null) return null;

		Integer[] result = new Integer[valores.length];

		for (int i = 0; i < valores.length; i++) {
			try {
				if (valores[i] != null && !valores[i].trim().equals("")) {
					result[i] = new Integer(valores[i].trim());
				} else {
					result[i] = null;
				}
			} catch (Exception e) {
				log.error("O valor \"" + valores[i] + "\" não é um valor válido para o campo. É esperado um valor numérico.", e);
				throw new ApresentacaoException("O valor \"" + valores[i] + "\" não é um valor válido para o campo. É esperado um valor numérico.", e);
			}
		}
		return result;
	}

	/**
	 * Obtem um Array com objetos do tipo String a partir de um parâmetro multivalorado.
	 * 
	 * @param req
	 * @param param
	 * @return <code>java.util.List</code>
	 */
	public String[] getStringParamArray(HttpServletRequest req, String param) throws ApresentacaoException {
		String[] valores = req.getParameterValues(param);
		if (valores==null) return null;

		String[] result = new String[valores.length];

		for (int i = 0; i < valores.length; i++) {
			try {
				String valor = null;
				if (valores[i] != null && !valores[i].trim().equals("")) {
					valor = valores[i].trim();
				}
				result[i] = valor;

			} catch (Exception e) {
				log.error("O valor \"" + valores[i] + "\" não é um valor válido para o campo.", e);
				throw new ApresentacaoException("O valor \"" + valores[i] + "\" não é um valor válido para o campo.", e);
			}
		}
		return result;
	}    


	/**
	 * Obtem um Array com objetos do tipo Double a partir de um parâmetro multivalorado.
	 * 
	 * @param req
	 * @param param
	 * @param formatado
	 *         true  - Obtém parâmetros formatados.     Ex.: 12.345,67
	 *         false - Obtém parâmetros não formatados. Ex.: 12345.67
	 * 
	 * @return <code>java.util.List</code>
	 */
	public Double[] getDoubleParamArray(HttpServletRequest req, String param, boolean formatado) throws ApresentacaoException {
		String[] valores = req.getParameterValues(param);
		if (valores==null) return null;

		Double[] result = new Double[valores.length];

		for (int i = 0; i < valores.length; i++) {
			try {
				String valor = null;
				if (valores[i] != null && !valores[i].trim().equals("")) {
					valor = valores[i].trim();

					//Se o valor estiver formatado, retira a formatacao do mesmo
					if (formatado) {
						//Retira os pontos
						valor = valor.replaceAll("\\.", "");

						//Troca a vírgula (se houver) por ponto
						valor = valor.replaceAll(",", "\\.");
					}

					//Converte o valor
					result[i] = Double.valueOf(valor);
				} else {
					result[i] = null;
				}

			} catch (Exception e) {
				log.error("O valor \"" + valores[i] + "\" não é um valor válido para o campo. É esperado um valor numérico.", e);
				throw new ApresentacaoException("O valor \"" + valores[i] + "\" não é um valor válido para o campo. É esperado um valor numérico.", e);
			}
		}
		return result;
	}

	/**
	 * Obtem um Array com objetos do tipo Boolean a partir de um parâmetro multivalorado.
	 * 
	 * @param req
	 * @param param
	 * @return <code>java.util.List</code>
	 */
	public Boolean[] getBooleanParamArray(HttpServletRequest req, String param) throws ApresentacaoException {
		String[] valores = req.getParameterValues(param);
		if (valores==null) return null;

		Boolean[] result = new Boolean[valores.length];

		for (int i = 0; i < valores.length; i++) {
			try {
				Boolean valor = Boolean.TRUE;
				if (valores[i] == null || valores[i].trim().equals("")) {
					valor = Boolean.FALSE;

				} else if ("Nn0 ".indexOf(valores[i].trim().charAt(0)) != -1) {
					valor = Boolean.FALSE;

				}
				result[i] = valor;

			} catch (Exception e) {
				log.error("O valor \"" + valores[i] + "\" não é um valor válido para o campo. É esperado um valor boleano.", e);
				throw new ApresentacaoException("O valor \"" + valores[i] + "\" não é um valor válido para o campo. É esperado um valor boleano.", e);
			}
		}
		return result;
	}        

	/**
	 * Obtém o <code>UsuarioBean</code> da sessão. Caso não encontre o objeto, retornar <code>null</code>.
	 * 
	 * @param req
	 * @return O <code>UsuarioBean</code> da sessão
	 */
	public UsuarioBean obtemUsuarioDaSessao(HttpServletRequest req) {
		HttpSession session = req.getSession();
		if (session==null) {
			return null;
		}
		return (UsuarioBean) session.getAttribute(ConstantesRequestSession.SESSION_USUARIO_BEAN);
	}


	/**
	 * Verifica se o usuário corrente possui acesso a pelo menos um dos
	 * níveis passados por parâmetro. Caso o usuário não possua acesso a 
	 * nenhum desses níveis, o método lança a execeção <code>SegurancaException</code>
	 * 
	 * @param req
	 * @param niveis
	 * @throws SegurancaException
	 */
	public void verificaAcesso(HttpServletRequest req, Integer[] niveis) throws SegurancaException {
		UsuarioBean usuario = obtemUsuarioDaSessao(req);
		boolean temAcesso = false;
		for (int i=0; (i < niveis.length) || (!temAcesso); i++) {
			temAcesso = usuario.verificaPermissao(niveis[i], null);
		}
		if (!temAcesso) {
			log.warn("Voce não possui acesso a esse módulo");
			throw new SegurancaException("Voce não possui acesso a esse módulo");
		}
	}

	/**
	 * Obtém sessão do usuário
	 * 
	 * @param request
	 * @return
	 */
	protected HttpSession getSession(HttpServletRequest request) {
		return request.getSession();
	}

	/**
	 * Valoriza a mensagem a ser enviada para a tela
	 * 
	 * @param request
	 * @param valor
	 */
	protected void setMensagem(HttpServletRequest request, String valor) {
		getSession(request).setAttribute("mensagem", valor);
	}
	
	/**
	 * Valoriza a mensagem de erro a ser enviada para a tela
	 * 
	 * @param request
	 * @param valor
	 */
	protected void setMensagemErro(HttpServletRequest request, String valor) {
		getSession(request).setAttribute("mensagemErro", valor);
	}	

	/**
	 * Salva token com o id do processo
	 * 
	 * @param id
	 * @param request
	 */
	protected void saveToken(String id, HttpServletRequest request) {
		//facilita para achar o token na sessão
		id =id.startsWith(Constantes.PRE_TOKEN)?id : Constantes.PRE_TOKEN + id;
		//System.out.println("chama save token");
		HttpSession session = request.getSession();
		//System.out.println("session.setAttribute("+id+", "+session.getId()+");");
		session.setAttribute(id, session.getId());
	}

	/**
	 * Salva token com o id do processo
	 * 
	 * @param id
	 * @param request
	 */
	protected boolean isTokenValid(String id, HttpServletRequest request, boolean resetToken) {
		//facilita para achar o token na sessão
		id =id.startsWith(Constantes.PRE_TOKEN)?id : Constantes.PRE_TOKEN + id;
		String saveToken = id;
		//System.out.println("chama is token valid");
		HttpSession session = request.getSession();
		String valor = (String)session.getAttribute(id);
		if (ObjectHelper.isEmpty(valor)) {
			id = id + Constantes.SU_TOKEN;
			
			valor = (String)session.getAttribute(id);
		}else{
			saveToken = id + Constantes.SU_TOKEN;
		}
		
		//System.out.println(valor+" = (String)session.getAttribute("+id+")");
		//System.out.println("resetToken "+resetToken);
		if (resetToken) {
			
			resetToken(id, request);
			saveToken(saveToken, request);
			
			
		}
		//System.out.println("return "+(valor != null && valor.equals(session.getId())));
		return (valor != null && valor.equals(session.getId()));
	}

	/**
	 * Limpa o token da session
	 * 
	 * @param id
	 * @param request
	 */
	protected void resetToken(String id, HttpServletRequest request) {
		//facilita para achar o token na sessão
    	id =id.startsWith(Constantes.PRE_TOKEN)?id : Constantes.PRE_TOKEN + id;
		//System.out.println("reseta o token");
		HttpSession session = request.getSession();
		//System.out.println("session.removeAttribute("+id+");");
		session.removeAttribute(id);
	}

}
