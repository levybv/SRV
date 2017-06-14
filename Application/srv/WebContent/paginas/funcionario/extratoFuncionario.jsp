<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.extratoFuncionario.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	var idFuncionario=-1;
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Extrato do Funcionário</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
			
				<input type="hidden" id="idFuncionario" name="idFuncionario" value="<c:out value='${idFuncionario}'/>"/>
				<input type="hidden" id="nomeFuncionario" name="nomeFuncionario" value="<c:out value='${nomeFuncionario}'/>"/>
				<input type="hidden" id="cracha" name="cracha" value="<c:out value='${cracha}'/>"/>
				<input type="hidden" id="cpfFuncionario" name="cpfFuncionario" value="<c:out value='${cpfFuncionario}'/>"/>

				<input type="hidden" id="idFuncionarioSelecionado" name="idFuncionarioSelecionado" value="<c:out value='${idFuncionarioSelecionado}'/>"/>
				<input type="hidden" id="nomeFuncionarioSelecionado" name="nomeFuncionarioSelecionado" value="<c:out value='${nomeFuncionarioSelecionado}'/>"/>
				<input type="hidden" id="cargoFuncionarioSelecionado" name="cargoFuncionarioSelecionado" value="<c:out value='${cargoFuncionarioSelecionado}'/>"/>
				<input type="hidden" id="centroCustoFuncionarioSelecionado" name="centroCustoFuncionarioSelecionado" value="<c:out value='${centroCustoFuncionarioSelecionado}'/>"/>
				
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label" for="filialDesc">Matrícula:</label>
 	                        		<label class="label" for="filialDesc"><c:out value='${idFuncionarioSelecionado}'/></label>
                                </td>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label" for="filialDesc">Nome:</label>
 	                        		<label class="label" for="filialDesc"><c:out value='${nomeFuncionarioSelecionado}'/></label>
                                </td>
 	                        	<td class="componente" width="30%">
 	                        		<label class="label" for="filialDesc">M&ecirc;s:</label>
									<select id="mes" name="mes" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<c:forEach var="itemMes" items="${listaMes}">
                                    		<option value="<c:out value='${itemMes.mes}' />" <c:if test="${itemMes.mes == mesSelecionado}">selected</c:if> ><c:out value='${itemMes.mesStr}' /></option>
										</c:forEach>
                                    </select>
                                </td>
                            </tr>
                        	<tr>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label" for="filialDesc">Cargo:</label>
 	                        		<label class="label" for="filialDesc"><c:out value='${cargoFuncionarioSelecionado}'/></label>
                                </td>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label" for="filialDesc">Área:</label>
 	                        		<label class="label" for="filialDesc"><c:out value='${centroCustoFuncionarioSelecionado}'/></label>
                                </td>
 	                        	<td class="componente" width="30%">
 	                        		<label class="label" for="filialDesc">Ano:</label>
									<select id="ano" name="ano" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<c:forEach var="itemAno" items="${listaAno}">
                                    		<option value="<c:out value='${itemAno.ano}' />" <c:if test="${itemAno.ano == anoSelecionado}">selected</c:if> ><c:out value='${itemAno.ano}' /></option>
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
							<input id="botaoVoltar" type="button" class="button" value="Voltar" />
							<input id="botaoLimpar" type="button" class="button" value="Limpar" />
							<input id="botaoConsultar" type="submit" class="button" value="Filtrar" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">
					Extrato do Funcionário
					<br/>
				</div>
				
				<fmt:setLocale value="pt_BR" scope="request"/>
				<display:table pagesize='15' id="lista" name="lista" class="table" requestURI="" decorator="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column property="descricaoIndicador"	title="Indicador"  		 style="cursor: pointer; width: 15%; vertical-align: middle;text-align: left;"/>
					<display:column property="codFilial"  			title="Filial" 			 style="cursor: pointer; width: 3%; vertical-align: middle;text-align: right;" />
					<display:column property="cargoUsuario"  		title="Cargo" 			 style="cursor: pointer; width: 7%; vertical-align: middle;text-align: right;" />
					<display:column property="meta"    				title="Meta"  			 style="cursor: pointer; width: 5%; vertical-align: middle;text-align: right;" format="{0,number,#,##0}"/>
					<display:column property="valorRealizado"   	title="Realizado" 		 style="cursor: pointer; width: 5%; vertical-align: middle;text-align: right;" format="{0,number,#,##0}"/>
					<display:column property="numRealizadoMeta" 	title="Realizado X Meta" style="cursor: pointer; width: 5%; vertical-align: middle;text-align: right;" format="{0,number,#,##0.00}"/>
					<display:column property="premio"  				title="Prêmio" 			 style="cursor: pointer; width: 5%; vertical-align: middle;text-align: right;" format="{0,number,#,##0.00}"/>
					<display:column title="" style="text-align: center;width: 1%"><a href="javascript:consultaDetalheCalculo(${lista.idFuncionario}, ${lista.idIndicador}, ${lista.idEmpresa}, ${lista.codFilial}, ${lista.ano}, ${lista.mes})"><img src="images/menu-consultas-ccm.png"/></a></display:column>
					
					<display:footer>
					  	<tr>
			  				<td style="font-weight:bold;text-align: left;" colspan="3">Total:</td>
			  				<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("totalMeta")%></td>
			  				<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("totalRealizado")%></td>
			  				<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("totalRealizadoXMeta")%></td>
			  				<td style="font-weight:bold;text-align: right;"><%=request.getAttribute("totalPremio")%></td>
			  				<td></td>
			  			<tr>
				   </display:footer>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>