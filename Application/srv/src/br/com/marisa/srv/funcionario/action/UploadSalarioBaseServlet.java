package br.com.marisa.srv.funcionario.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import br.com.marisa.srv.funcionario.business.SalarioBaseBusiness;
import br.com.marisa.srv.funcionario.vo.SalarioBaseVO;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.FileHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.geral.vo.UsuarioBean;

/**
 * Action para tratar as requisições de Upload de Salário Base
 * 
 * @author Walter Fontes
 */
public class UploadSalarioBaseServlet extends HttpServlet {
	
	//Log4J
	private static final Logger log = Logger.getLogger(UploadSalarioBaseServlet.class);    

	
	/**
	 * Método para o tratamento das chamadas ao servlet, para realização do Upload
	 * 
	 * @param request
	 * @param response
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List erros = new ArrayList();
		
		try {
		
			String contentType = request.getContentType();
			if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
				
				ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
				List fileItemsList = servletFileUpload.parseRequest(request);
				
				Iterator it = fileItemsList.iterator();
				while (it.hasNext()){
					FileItem fileItem = (FileItem)it.next();
					if (!fileItem.isFormField()){
						
						//Handle Uploaded files.
						System.out.println( "  Field Name 	= " + fileItem.getFieldName()+
											", File Name 	= " + fileItem.getName()+
											", Content type = " + fileItem.getContentType()+
											", File Size 	= " + fileItem.getSize());
						
						BufferedReader br = new BufferedReader(new InputStreamReader(fileItem.getInputStream(),FileHelper.detectCharset(fileItem.getInputStream(),true,true)));
				        
				        int linha = 0;
				        List salariosBase = new ArrayList();
				        for(String line=br.readLine(); line!=null; line=br.readLine()) {
				        	
				        	linha++;
				        	
				        	if (ObjectHelper.isNotEmpty(line)) { 
				        		log.debug("\nSalário Base => Linha: " + line);
				        		SalarioBaseVO salarioBaseVO = null;

				        		try {
					                StringTokenizer st = new StringTokenizer(line, ";");
					                
					                salarioBaseVO = new SalarioBaseVO();
					                
					                //Nro da Linha
					                salarioBaseVO.setNroLinha(new Integer(linha));
					                
					                //Mes-Ano 
					                StringTokenizer mesAno = new StringTokenizer(st.nextToken(), "/");
					        		salarioBaseVO.setMes(new Integer(mesAno.nextToken()));
					        		salarioBaseVO.setAno(new Integer(mesAno.nextToken()));
					        		
					        		//Funcionário
					        		salarioBaseVO.setIdFuncionario(new Long(st.nextToken()));
	
					        		//Salário base
					        		salarioBaseVO.setSalarioBase(new Double(st.nextToken()));
	
					        		//Usuário e data de alteração
					        		salarioBaseVO.setDataUltimaAlteracao(new Date());
					        		salarioBaseVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(request).getUsuarioVO().getIdUsuario());
				        			salariosBase.add(salarioBaseVO);
					        		
				        		} catch (Exception e) {
				        			log.error("Linha " + linha + ": Erro estrutural - Rever formato do arquivo => " + e.getMessage());
				        			erros.add("Linha " + linha + ": Erro estrutural - Rever formato do arquivo");
								}
				        	}
			            }
				        
				        List errosAlteracao = SalarioBaseBusiness.getInstance().alteraSalarioBase(salariosBase);
				        if (errosAlteracao != null) {
				        	erros.addAll(errosAlteracao);
				        }
					}
				}
			}
			
			if (erros != null && erros.size() > 0) {
				request.getSession().setAttribute("erros", erros);
			} else { 
				request.getSession().setAttribute("mensagem", "Importação realizada com sucesso.");
			}			
			
		} catch (Exception e) {
			erros.add("Ocorreu erro não esperado no processo de importação: " + e.getMessage());
			request.getSession().setAttribute("erros", erros);
			log.error("Ocorreu erro ao importar salários base.", e);			
		}
		
		String destino = "/srv/salarioBase.do?operacao=inicio";
		log.debug("destino => " + destino);
		response.sendRedirect(destino);
		return;
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
}