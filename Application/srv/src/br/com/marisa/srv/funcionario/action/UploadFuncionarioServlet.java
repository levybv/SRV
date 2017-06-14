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

import br.com.marisa.srv.funcionario.business.FuncionarioBusiness;
import br.com.marisa.srv.funcionario.vo.FuncionarioVO;
import br.com.marisa.srv.geral.constants.Constantes;
import br.com.marisa.srv.geral.constants.ConstantesRequestSession;
import br.com.marisa.srv.geral.helper.DataHelper;
import br.com.marisa.srv.geral.helper.FileHelper;
import br.com.marisa.srv.geral.helper.ObjectHelper;
import br.com.marisa.srv.geral.vo.UsuarioBean;

/**
 * Action para tratar as requisições de Upload de Funcionários
 * 
 * @author Walter Fontes
 */
public class UploadFuncionarioServlet extends HttpServlet {
	
	//Log4J
	private static final Logger log = Logger.getLogger(UploadFuncionarioServlet.class);    

	
	/**
	 * Método para o tratamento das chamadas ao servlet, para realização do Upload
	 * 
	 * @param request
	 * @param response
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List erros = new ArrayList();
		FuncionarioVO funcionarioVO = null; 
		
		String dataPromo;
		String codigoMotivo;
		String quantidadeDias;
		String flag;
		
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
				        List funcionarios = new ArrayList();

				        for(String line=br.readLine(); line!=null; line=br.readLine()) {
				        	
				        	linha++;
				        	
				        	if (ObjectHelper.isNotEmpty(line)) { 
				        		System.out.println("Linha: " + line);

				        		try {
					                StringTokenizer st = new StringTokenizer(line, ";");
					                
					                funcionarioVO = new FuncionarioVO();
					                
					                //Nro da Linha
					                funcionarioVO.setNroLinha(new Integer(linha));					                
					                
					                //Matrícula
					    			funcionarioVO.setIdFuncionario(new Long(st.nextToken()));
					    			funcionarioVO.setIdEmpresa(new Integer(Constantes.CODIGO_EMPRESA));
					    			funcionarioVO.setIdFilial(new Integer(st.nextToken()));
					    			funcionarioVO.setIdFilialRH(st.nextToken());
					    			funcionarioVO.setDescricaoFilialRH(st.nextToken());
					    			funcionarioVO.setIdCargo(new Integer(st.nextToken()));
					    			funcionarioVO.setCracha(st.nextToken());
					    			funcionarioVO.setCpfFuncionario(new Long(st.nextToken()));
					    			funcionarioVO.setNomeFuncionario(st.nextToken());
					    			
					    			String idSituacaoRHStr = st.nextToken();
					    			if (!ObjectHelper.isEmpty(idSituacaoRHStr)) {
						    			funcionarioVO.setIdSituacaoRH(new Integer(idSituacaoRHStr));
					    			}
					    			String descricaoSituacaoRHStr = st.nextToken();
					    			if (!ObjectHelper.isEmpty(descricaoSituacaoRHStr)) {
					    				funcionarioVO.setDescricaoSituacaoRH(descricaoSituacaoRHStr);
					    			}
					    			String dataSituacaoRHStr = st.nextToken();
					    			if (!ObjectHelper.isEmpty(dataSituacaoRHStr)) {
					    				funcionarioVO.setDataSituacaoRH(DataHelper.obtemData(dataSituacaoRHStr));
					    			}					    			
					    			
					    			funcionarioVO.setIdEmpresaRH(new Integer(st.nextToken()));
					    			funcionarioVO.setDescricaoEmpresaRH(st.nextToken());
					    			funcionarioVO.setIdCentroCusto(st.nextToken());
					    			funcionarioVO.setDescricaoCentroCusto(st.nextToken());
					    			
					    			String idFuncionarioAvaliadorStr = st.nextToken();
					    			if (!ObjectHelper.isEmpty(idFuncionarioAvaliadorStr)) {
					    				funcionarioVO.setIdFuncionarioAvaliador(new Long(idFuncionarioAvaliadorStr));
					    			}
					    			String idFuncionarioSuperiorStr = st.nextToken();
					    			if (!ObjectHelper.isEmpty(idFuncionarioSuperiorStr)) {
					    				funcionarioVO.setIdFuncionarioSuperior(new Long(idFuncionarioSuperiorStr));
					    			}					    			
					    			
					    			funcionarioVO.setDataAdmissao(DataHelper.obtemData(st.nextToken()));
					    			
					    			if (st.hasMoreTokens()) {
					    				String dataDemissao = st.nextToken();
					    				if (ObjectHelper.isNotEmpty(dataDemissao)) {
						    				funcionarioVO.setDataDemissao(DataHelper.obtemData(dataDemissao));
					    				}
					    			}
					    				
					    			funcionarioVO.setIdUsuarioUltimaAlteracao(obtemUsuarioDaSessao(request).getUsuarioVO().getIdUsuario());
					    			funcionarioVO.setDataUltimaAlteracao(new Date());
					    			
					    	if (st.hasMoreTokens()) {

					    			String idSituacaoAnterior = st.nextToken();
					    			String descricaoSituacaoAnterior = st.nextToken();
					    			String dataInicioSituacaoAnterior = st.nextToken();
					    			String qtdDiasTrabalhadosMes = st.nextToken();
					    			
									if(ObjectHelper.isEmpty(idSituacaoAnterior)){
										funcionarioVO.setIdSituacaoAnterior(null);
					    			}else{
										funcionarioVO.setIdSituacaoAnterior(new Integer(idSituacaoAnterior));
					    			}

									if(ObjectHelper.isEmpty(idSituacaoAnterior)){
										funcionarioVO.setDescricaoSituacaoAnterior(null);
					    			}else{
					    				funcionarioVO.setDescricaoSituacaoAnterior(descricaoSituacaoAnterior);
					    			}

									if(ObjectHelper.isEmpty(dataInicioSituacaoAnterior)){
										funcionarioVO.setDataInicioSituacaoAnterior(null);
					    			}else{
										funcionarioVO.setDataInicioSituacaoAnterior(DataHelper.obtemData(dataInicioSituacaoAnterior));
					    			}

									if(ObjectHelper.isEmpty(qtdDiasTrabalhadosMes)){
										funcionarioVO.setQtdDiasTrabalhadosMes(null);
					    			}else{
										funcionarioVO.setQtdDiasTrabalhadosMes(new Integer(qtdDiasTrabalhadosMes));
					    			}

					    			 dataPromo = 		st.nextToken();
					    			 codigoMotivo= 		st.nextToken();
					    			 quantidadeDias = 	st.nextToken();
					    			 flag = 			st.nextToken();
					    			
					    			if(dataPromo.trim().equals("")){
					    				funcionarioVO.setDataPromocaoElegivel(null);
					    			}else{
					    				funcionarioVO.setDataPromocaoElegivel(DataHelper.obtemData(dataPromo));
					    			}
					    			if(codigoMotivo.trim().equals("")){
					    				funcionarioVO.setCodigoMotivoDEmissao(null);
					    			}else{
					    				funcionarioVO.setCodigoMotivoDEmissao(Integer.valueOf(codigoMotivo));
					    			}
					    			if(quantidadeDias.trim().equals("")){
					    				funcionarioVO.setQuantidadeDiasAfastamento(null);
					    			}else{
					    				funcionarioVO.setQuantidadeDiasAfastamento(Integer.valueOf(quantidadeDias));
					    			}
					    			if(flag.trim().equals("") && (!flag.equals("S") && !flag.equals("N")) ){
					    				funcionarioVO.setFlagIndicadorCentroCusto(null);
					    			}else{
					    				funcionarioVO.setFlagIndicadorCentroCusto(flag);
					    			}
					    	}
					    			
					    			funcionarios.add(funcionarioVO);
					        		
				        		} catch (Exception e) {
				        			log.error("Linha " + linha + ": Erro estrutural - Rever formato do arquivo => " + e.getMessage());
				        			erros.add("Linha " + linha + ": Erro estrutural - Rever formato do arquivo");	
				        			e.printStackTrace();
								}
				        	}
			            }
				        
		        		System.out.println("Todas as linhas lidas...");

				        List errosAlteracao = FuncionarioBusiness.getInstance().importaFuncionarios(funcionarios);
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
			log.error("Ocorreu erro ao importar Funcionarios.", e);
			e.printStackTrace();
		}
		
		String destino = "/srv/funcionario.do?operacao=inicio";
		
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