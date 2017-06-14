<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type='text/javascript' src='srvdwr/interface/CalendarioComercialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	

<script type="text/javascript" src="js/ms/srv.bonusExtratoFuncionario.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Extrato do Funcion&aacute;rio - Desempenho do Colaborador</div>
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
				<input type="hidden" id="cpfSelecionado"                    name="cpfSelecionado"                    value="<c:out value='${cpfFuncionario}'/>"/>
				<input type="hidden" id="crachaSelecionado"                 name="crachaSelecionado"                 value="<c:out value='${cracha}'/>"/>
				<input type="hidden" id="periodoSelecionado"                name="periodoSelecionado"                value="<c:out value='${periodoSelecionado}'/>"/>
				<input type="hidden" id="situacaoColaborador" 				name="situacaoColaborador" 					value="<c:out value='${situacaoColaborador}'/>"/>
				
				
				
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label" for="filialDesc">Matr&iacute;cula:</label>
 	                        		<label class="label" for="filialDesc"><c:out value='${idFuncionarioSelecionado}'/></label>
                                </td>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label" for="filialDesc">Nome:</label>
 	                        		<label class="label" for="filialDesc"><c:out value='${nomeFuncionarioSelecionado}'/></label>
                                </td>
 	                        	<td class="componente" width="30%" colspan="2">
 	                        		<label class="label" for="filialDesc">Per&iacute;odo:</label>
									<select id="periodos" name="periodos" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    </select>
                                </td>
                            </tr>
                        	<tr>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label" for="filialDesc">Cargo:</label>
 	                        		<label class="label" for="filialDesc"><c:out value='${cargoFuncionarioSelecionado}'/></label>
                                </td>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label" for="filialDesc">&Aacute;rea:</label>
 	                        		<label class="label" for="filialDesc"><c:out value='${centroCustoFuncionarioSelecionado}'/></label>
                                </td>
                                <td class="componente" width="35%">
 	                        		<label class="label" for="filialDesc">Filial:</label>
                                	<c:if test="${listaFilial != null}">
										<select id="idFilialSelecionada" name="idFilialSelecionada" class="campo">
	                                    	<option value="" selected>[SELECIONE]</option>
	                                    	<c:forEach  var="itemFilial" items="${listaFilial}">
	                                    		<option value="<c:out value='${itemFilial.codFilial}'/>" <c:if test="${idFilialSelecionada == itemFilial.codFilial}">selected</c:if> >
	                                    			<c:out value='${itemFilial.codFilial}'/> - <c:out value='${itemFilial.descricao}'/>
	                                    		</option>
	                                    	</c:forEach>
	                                    </select>
                                	</c:if>
			  				    </td>
                            </tr>                            
                        	<tr>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label" for="filialDesc">Situa&ccedil;&atilde;o do Colaborador:</label>
 	                        		<label class="label" for="filialDesc"><c:out value='${situacaoColaborador}'/></label>
                                </td>
 	                        	<td class="componente" width="35%">
									<label class="label" for="filialDesc">Aceite:</label>
 	                        		<label class="label" for="filialDesc" style="color:blue;"><c:out value='${statusAceite}'/></label>
                                </td>
 	                        	<td class="componente" width="35%">
                                   <label class="label" for="filialDesc">Status:</label>
			  				       <label class="label" for="filialDesc" style="color:red;"><c:out value='${statusRalz}'/></label>
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
					Extrato do Funcion&aacute;rio
					
                    <c:if test="${'true' == stBtImprimir}">
					    <input id="imprimir" type="button" class="buttonAtualizar140" style="float:right; width:125px;  margin-right:10px;" value="Contrato Meta" />
				    </c:if>
					
					<c:if test="${'true' == stBtRemover}">
					    <input id="excluir"        type="button" class="buttonAtualizar140" style="float:right; width:125px; margin-right:10px;" value="Remover Indicador" />
                    </c:if>
					
					<c:if test="${'true' == stBtAlterar}">
					    <input id="alterar"    type="button" class="buttonAtualizar140" style="float:right; width:135px;  margin-right:10px;" value="Alterar Indicador" />
                    </c:if>
					
					<c:if test="${'true' == stBtIncluir}">
					    <input id="incluir"        type="button" class="buttonAtualizar140" style="float:right; width:135px; margin-right:10px;" value="Adicionar Indicador" />
                    </c:if>
					
					<c:if test="${'true' == stBtStatus}">
					    <input id="status"      type="button" class="buttonAtualizar120" style="float:right; width:90px;  margin-right:10px;" value="Status" />
                    </c:if>

					<c:if test="${'true' == stBtCalcular}">
					    <input id="calcular"      type="button" class="buttonAtualizar120" style="float:right; width:90px;  margin-right:10px;" value="Calcular" />
                    </c:if>

					<br/>					
				</div>
				<fmt:setLocale value="pt_BR" scope="request"/>
				<display:table pagesize='15' id="lista" name="lista" class="table" requestURI="" decorator="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="" style="width:2%; text-align:center; ">
						<c:if test="${lista.idIndicador != null}">
							<input type="radio" name="checkbox" id="checkbox" value="${lista.chave}">
						</c:if>
					</display:column>
					<display:column title="Fórmula" style="width:5%; text-align:center; ">
						<div id="a" title="${lista.formulaIndicador}" style="width:100%; cursor:hand;">
						<c:if test="${lista.idIndicador != null}">
							<b style='color:blue'><u>F</u></b>
						</c:if>
						</div>
					</display:column>
					<display:column title="Indicador" style="cursor: pointer; width: 15%; vertical-align: middle;text-align: left;">
						<c:if test="${lista.idIndicador == null}">
							<b><c:out value="${lista.descricaoIndicador}"/></b>
						</c:if>
						<c:if test="${lista.idIndicador != null}">
							<c:out value="${lista.descricaoIndicador}"/>
						</c:if>
					</display:column>
					<display:column title="Sentido" style="width:2%; text-align:center; ">
						<c:if test="${lista.idIndicador != null}">
		                    <c:if test="${'C' == lista.flgSentido}">
								<img src="images/m_cima.png" alt="Melhor para cima" />
							</c:if>
		                    <c:if test="${'B' == lista.flgSentido}">
								<img src="images/m_baixo.png" alt="Melhor para baixo" />
							</c:if>
						</c:if>
					</display:column>
					<display:column property="pesoFormatado"					title="Peso"  			 			style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;"/>
					<display:column property="metaFormatada"					title="Meta"			   			style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;"/>
					<display:column property="realizadoFormatado"  			    title="Realizado" 		 			style="cursor: pointer; width: 08%; vertical-align: middle;text-align: right;"/>
					<display:column property="realizadoXMetaFormatado" 			title="Atingimento da Meta" 		style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;"/>
					<display:column property="numEscala"  						title="Escala" 			 			style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;"/>
					<display:column property="realizadoFaixaFormatado"  		title="Resultado Conforme Escala" 	style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;"/>
					<display:column property="realizadoPonderacaoFormatado"  	title="Desempenho"		 			style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;"/>

					<display:footer>
					  	<tr>
						  	<td style="font-weight:bold;text-align: left;">&nbsp;</td>
						  	<td style="font-weight:bold;text-align: left;">&nbsp;</td>
			  				<td style="font-weight:bold;text-align: left;">
			  					<!-- c:if test="${exibeBonus}" -->
				  					<!-- a href="javascript:consultaDetalheCalculo(${total.idFuncionario}, ${total.idIndicador}, -1, -1, ${total.ano}, ${total.mes})"><c:out value="${total.descricaoIndicador}"/></a -->
				  				<!-- /c:if -->
			  					<!-- c:if test="${!exibeBonus}" -->
				  					<c:out value="${total.descricaoIndicador}"/>
				  				<!-- /c:if -->
			  				</td>
			  				<td style="font-weight:bold;text-align: right;">&nbsp;</td>
			  				<td style="font-weight:bold;text-align: right;">
			  					<c:if test="${detalheCalculo.pesoFormatado != '100,00%'}">
			  						<font color="red"><c:out value="${detalheCalculo.pesoFormatado}"/></font>
									<script>
										alert("O peso total desse funcionário não está 100%. Favor Verificar !");
									</script>
			  					</c:if>
			  					<c:if test="${detalheCalculo.pesoFormatado == '100,00%'}">
			  						<c:out value="${detalheCalculo.pesoFormatado}"/>
			  					</c:if>
			  				</td>
			  				<td style="font-weight:bold;text-align: right;">&nbsp;</td>
			  				<td style="font-weight:bold;text-align: right;">&nbsp;</td>
			  				<td style="font-weight:bold;text-align: right;">&nbsp;</td>
			  				<td style="font-weight:bold;text-align: right;">&nbsp;</td>
			  				<td style="font-weight:bold;text-align: right;">&nbsp;</td>
			  				<td style="font-weight:bold;text-align: right;"><c:out value="${total.realizadoPonderacaoFormatado}"/></td>
			  			<tr>
				   </display:footer>					
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>