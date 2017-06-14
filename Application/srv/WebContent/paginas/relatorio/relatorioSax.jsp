<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" >

</script> 

<script type="text/javascript" src="js/ms/srv.relatorioSax.js" charset="ISO-8859-1"></script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Relat&oacute;rio SAX</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
			
			<input type='hidden' id="idFilialFunc" name="idFilialFunc" value="<c:out value='${funcionario.idFilial}'/>"/>
				
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                    	    
                    	   <tr>
                    	     <td class="componente" width="10%">
 	                        	  <label class="label" for="filialDesc">Ano:</label>
 	                        	</td>
 	                        	<td class="componente" width="40%">                        		
                                   <input id="ano" name="ano" value="" class="campo2" maxlength="4" style="width: 60px;" type="text"/>
                              </td>
                    	   </tr> 
                    	   
                        	<tr>
 	                        	<td class="componente" width="10%">
 	                        		<label class="label" for="filialDesc">Mes:</label>
 	                        	</td>
 	                        	<td class="componente" width="40%">
									<select id="mes" name="mes" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                		<option value="1" <c:if test="${1 == mes}">selected</c:if> >Janeiro</option>
                                		<option value="2" <c:if test="${2 == mes}">selected</c:if> >Fevereiro</option>
										<option value="3" <c:if test="${3 == mes}">selected</c:if> >Mar&ccedil;o</option>
                                		<option value="4" <c:if test="${4 == mes}">selected</c:if> >Abril</option>
                                		<option value="5" <c:if test="${5 == mes}">selected</c:if> >Maio</option>
                                		<option value="6" <c:if test="${6 == mes}">selected</c:if> >Junho</option>                                		
                                		<option value="7" <c:if test="${7 == mes}">selected</c:if> >Julho</option>
                                		<option value="8" <c:if test="${8 == mes}">selected</c:if> >Agosto</option>
										<option value="9" <c:if test="${9 == mes}">selected</c:if> >Setembro</option>
                                		<option value="10" <c:if test="${10 == mes}">selected</c:if> >Outubro</option>
                                		<option value="11" <c:if test="${11 == mes}">selected</c:if> >Novembro</option>
                                		<option value="12" <c:if test="${12 == mes}">selected</c:if> >Dezembro</option>                                		
                                    </select>
                                </td>
                                
                            </tr>
                             
                        	<tr>
 	                        	<td class="componente" width="10%">
 	                        		<label class="label" for="filialDesc">Tipo Relat&oacute;rio:</label>
 	                        	</td>
 	                        	<td class="componente" width="40%">
                                	<select id="codigoRelatorio" name="codigoRelatorio" class="campo">
                                    	<option value="" selected>SELECIONE...</option>
										<c:forEach var="itemRelatorioSax" items="${listaRelatorioSax}">
											<option value="<c:out value='${itemRelatorioSax.codigo}'/>" ><c:out value='${itemRelatorioSax.codigo}' /> - <c:out value='${itemRelatorioSax.nome}' /></option>
										</c:forEach>
                                    </select>
                                </td>
 	                        	
                            </tr>
                        </tbody>
                    </table>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				
				<br>
				<div class="barTitle">
					<input id="importar" type="submit" class="buttonAtualizar" style="float:right" value="Relat&oacute;rio" />
					<input id="botaoLimpar" type="button" class="button" style="float:right"  value="Limpar" />
					<br/>
				</div>
				
				<BR>
			</form>
		</div>
	</div>
</div>