package br.com.marisa.srv.filial.realizado.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import br.com.marisa.srv.filial.realizado.business.RealizadoFilialIndicBusiness;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.FileHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.geral.vo.UsuarioBean;

/**
 * 
 * @author levy.villar
 *
 */
public class UploadRealizFilialIndic extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6020823809969571676L;

	private static final Logger log = Logger.getLogger(UploadRealizFilialIndic.class);

	/**
	 * 
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<String> erros = new ArrayList<String>();
		
		try {
		
			String contentType = request.getContentType();
			if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
				
				ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
				List<FileItem> fileItemsList = servletFileUpload.parseRequest(request);
				
				Iterator<FileItem> it = fileItemsList.iterator();
				while (it.hasNext()) {
					FileItem fileItem = (FileItem)it.next();
					
					if (!fileItem.isFormField()){

						System.out.println( "Field Name 	= " + fileItem.getFieldName()+
											", File Name 	= " + fileItem.getName()+
											", Content type = " + fileItem.getContentType()+
											", File Size 	= " + fileItem.getSize());
						
				        List<String> linhasArquivo = new ArrayList<String>();

				        BufferedReader br = new BufferedReader(new InputStreamReader(fileItem.getInputStream(),FileHelper.detectCharset(fileItem.getInputStream(),true,true)));
				        for(String line=br.readLine(); line!=null; line=br.readLine()) {
			        		log.debug("\nRealizado Indicador Filial => Linha: " + line);
			        		linhasArquivo.add(line);
			            }

				        List<String> errosImportacao = RealizadoFilialIndicBusiness.getInstance().importaRealizadoFilialIndic(linhasArquivo, obtemUsuarioDaSessao(request).getUsuarioVO().getIdUsuario());
				        if (ObjectHelper.isNotEmpty(errosImportacao)) {
				        	erros.addAll(errosImportacao);
				        }
					}
				}
			}
			
			if (erros != null && erros.size() > 0) {
				request.getSession().setAttribute("erros", erros);
			} else { 
				request.getSession().setAttribute("mensagem", "Importa��o realizada com sucesso");
			}
			
		} catch (Exception e) {
			erros.add("Ocorreu erro n�o esperado no processo de importa��o: " + e.getMessage());
			request.getSession().setAttribute("erros", erros);
			log.error("Ocorreu erro ao importar realizado indicador filial.", e);
		}
		
		String destino = "/srv/realizFilialIndic.do?operacao=inicio";
		response.sendRedirect(destino);
		return;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	public UsuarioBean obtemUsuarioDaSessao(HttpServletRequest req) {
		HttpSession session = req.getSession();
		if (session==null) {
			return null;
		}
		return (UsuarioBean) session.getAttribute(ConstantesRequestSession.SESSION_USUARIO_BEAN);
	}

}