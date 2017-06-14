package br.com.marisa.srv.meta.action;

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

import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.FileHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.geral.vo.UsuarioBean;
import br.com.marisa.srv.meta.business.MetaFuncionarioBusiness;
import br.com.marisa.srv.meta.vo.MetaFuncionarioVO;

/**
 * Action para tratar as requisições de Upload de Metas de Funcionários
 * 
 * @author Walter Fontes
 */
public class UploadMetaFuncionarioServlet extends HttpServlet {
	
	//Log4J
	private static final Logger log = Logger.getLogger(UploadMetaFuncionarioServlet.class);    

	
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
						System.out.println( "Field Name 	= " + fileItem.getFieldName()+
											", File Name 	= " + fileItem.getName()+
											", Content type = " + fileItem.getContentType()+
											", File Size 	= " + fileItem.getSize());
						
						BufferedReader br = new BufferedReader(new InputStreamReader(fileItem.getInputStream(),FileHelper.detectCharset(fileItem.getInputStream(),true,true)));
				        
				        int linha = 0;
				        List metas = new ArrayList();
				        for(String line=br.readLine(); line!=null; line=br.readLine()) {
				        	
				        	linha++;
				        	
				        	if (ObjectHelper.isNotEmpty(line)) { 
				        		log.debug("\nMeta Funcionario => Linha: " + line);
				        		MetaFuncionarioVO metaFuncionarioVO = null;

				        		try {
					                StringTokenizer st = new StringTokenizer(line, ";");
					                
					                metaFuncionarioVO = new MetaFuncionarioVO();
					                
					                //Nro da Linha
					                metaFuncionarioVO.setNroLinha(new Integer(linha));					                
					                
					                //Mes-Ano 
					                StringTokenizer mesAno = new StringTokenizer(st.nextToken(), "/");
					        		metaFuncionarioVO.setMes(new Integer(mesAno.nextToken()));
					        		metaFuncionarioVO.setAno(new Integer(mesAno.nextToken()));
					        		
					        		//Funcionário
					        		metaFuncionarioVO.setIdFuncionario(new Long(st.nextToken()));
					        		
					        		//Indicador
					        		metaFuncionarioVO.setIdIndicador(new Integer(st.nextToken()));

					        		//Unidade Meta
					        		String unidadeMeta = st.nextToken();
					        		if (unidadeMeta.matches("^[0-9]*$")) {
					        			metaFuncionarioVO.setIdUnidadeMeta(new Integer(unidadeMeta));
					        		}
	
					        		//Valor Meta
					        		String valorMeta = st.nextToken();
					        		if (valorMeta.matches("^[0-9]*[.]{0,1}[0-9]*$")) {
					        			metaFuncionarioVO.setValorMeta(new Double(valorMeta));
					        		}
					        		
					        		//Descrição Meta
					        		String descricaoMeta = st.nextToken();
					        		if (ObjectHelper.isNotEmpty(descricaoMeta)) {
					        			metaFuncionarioVO.setDescricaoMeta(descricaoMeta);
					        		}
	
					        		//Usuário e data de alteração
					        		metaFuncionarioVO.setDataAlteracao(new Date());
					        		metaFuncionarioVO.setIdUsuario(obtemUsuarioDaSessao(request).getUsuarioVO().getIdUsuario());
					        		metas.add(metaFuncionarioVO);
					        		
				        		} catch (Exception e) {
				        			log.error("Linha " + linha + ": Erro estrutural - Rever formato do arquivo => " + e.getMessage());
				        			erros.add("Linha " + linha + ": Erro estrutural - Rever formato do arquivo");
								}
				        	}
			            }
				        
				        List errosAlteracao = MetaFuncionarioBusiness.getInstance().alteraMetaFuncionario(metas);
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
			
			log.error("Ocorreu erro ao importar metas de funcionarios.", e);
		}
		
		String destino = "/srv/metaFuncionario.do?operacao=inicio";
		log.debug("destino => " + destino);
		response.sendRedirect(destino);
		return;
		//request.getRequestDispatcher(destino).forward(request, response);
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