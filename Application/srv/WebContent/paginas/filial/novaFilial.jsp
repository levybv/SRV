<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<%@page import="br.com.marisa.srv.filial.vo.FilialVO"%>
<%@page import="java.util.List"%>

<script type="text/javascript" src="js/ms/srv.filial.js" charset="ISO-8859-1"></script>

<script type="text/javascript">

	$(document).ready(function() {
		
		if ("<c:out value='${apenasAtivas}'/>" == "on") {
			$("#apenasAtivas").attr("checked", "checked");
		}
	
	    
	});
	

</script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Filiais</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="filial.do?operacao=inicio" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" style="width:25%">
 	                        		<label class="label" for="filialDesc">C&oacute;digo Filial:</label>
                                	<input id="codFilial" name="codFilial" value="<c:out value='${codFilial}'/>" class="campo2" maxlength="5" style="width: 25px;" value="" type="text"/>
                                </td>
                                
                                          
								<td class="componente">
                                    <label class="label" for="filialDesc">Descri&ccedil;&atilde;o Filial:</label>
									<select id="idDescrTipoFil" name="idDescrTipoFil" class="campo">
									   <option value="" selected>Selecione...</option>
									   <c:forEach var="filial" items="${listaTipoFil}" varStatus="id">
                                    	  <option value="${filial.codFilial}"><c:out value="${filial.descricao}"/></option>
                                       </c:forEach>
                                    </select>
                                </td> 
					 
                                
 	                        	<td class="componente">
 	                        		<label class="label">
 	                        			<input type="checkbox" id="apenasAtivas" name="apenasAtivas">
 	                        			Apenas Ativas
 	                        		</label>
                                </td>                                
                            </tr> 
                        </tbody>
                    </table>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
						    <input id="botaoIncluir"   type="button" class="button" value="Incluir" />
							<input id="botaoLimpar"    type="button" class="button" value="Limpar" />
							<input id="botaoConsultar" type="submit" class="button" value="Filtrar" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">Filiais <input id="atualizar" name="atualizar" type="button" class="buttonAtualizar" value="Alterar" /><br/></div>
				
				<display:table pagesize='10' id="listaFiliais" name="listaFiliais" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title=""  style="text-align:center;"><input type="radio" name="checkbox" id="checkbox" value="${listaFiliais.codFilial}@${listaFiliais.codEmpresa}@${listaFiliais.descricao}@${listaFiliais.flagAtivo}@${listaFiliais.cnpjStr}@${listaFiliais.descricaoTpFil}@${listaFiliais.flagMeta100}"></display:column> 					
					<display:column property="listaFiliais.codFilial"       title="C&oacute;digo"           style="text-align:center; cursor: pointer; width: 20%; vertical-align: middle;" />  
					<display:column property="listaFiliais.descricao"       title="Descri&ccedil;&atilde;o" style="cursor: pointer; width: 30%; vertical-align: middle;"/>             
					<display:column property="listaFiliais.flagAtivoStr"    title="Ativa"                   style="text-align:center; cursor: pointer; width: 15%; vertical-align: middle;"/>             					
					<display:column property="listaFiliais.cnpjStr"         title="CNPJ"                    style="text-align:center; cursor: pointer; width: 25%; vertical-align: middle;"/>
					<display:column property="listaFiliais.descricaoTpFil"  title="Tipo Filial"             style="text-align:center; cursor: pointer; width: 30%; vertical-align: middle;"/>
				</display:table>
				<BR>

				
			</form>
		</div>
	</div>
</div>

