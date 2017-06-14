<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type='text/javascript' src='srvdwr/interface/CalendarioComercialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	

<script type="text/javascript" src="js/ms/srv.bonusAnualIndicadorFuncionario.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Indicador B&ocirc;nus Anual - Funcion&aacute;rio</div>
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
				<input type="hidden" id="cpfSelecionado" name="cpfSelecionado" value="<c:out value='${cpfFuncionario}'/>"/>
				<input type="hidden" id="crachaSelecionado" name="crachaSelecionado" value="<c:out value='${cracha}'/>"/>
				<input type="hidden" id="situacaoColaborador" name="situacaoColaborador" value="<c:out value='${situacaoColaborador}'/>"/>
				<input type="hidden" id="periodoAtual" name="periodoAtual" value="<c:out value='${periodoAtual}'/>"/>

				<input type="hidden" id="acaoTela" name="acaoTela" value="<c:out value='${acaoTela}'/>"/>

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
 	                        		<label class="label"><c:out value='${periodoAtual}'/></label>
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
                                   <label class="label" for="filialDesc">Status:</label>
			  				       <label class="label" for="filialDesc" style="color:red;"><c:out value='${statusRalz}'/></label>
			  				    </td>
                            </tr>                            
                        	<tr>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label" for="filialDesc">Situa&ccedil;&atilde;o do Colaborador:</label>
 	                        		<label class="label" for="filialDesc"><c:out value='${situacaoColaborador}'/></label>
                                </td>
 	                        	<td class="componente" colspan="2">
 	                        		<label class="label" for="filialDesc">Aceite:</label>
 	                        		<label class="label" for="filialDesc" style="color:blue;"><c:out value='${statusAceite}'/></label>
                                </td>
							</tr>
                        	<tr>
 	                        	<td class="componente" colspan="3">
 	                        		<label class="label" for="filialDesc" style="color:red;">
 	                        			</br>
 	                        			** Para 2016 adotamos a inclus&atilde;o de novos crit&eacute;rios em exce&ccedil;&atilde;o:
 	                        			Desempenhos individuais acima de 80% ser&atilde;o reconhecidos.
 	                        			</br>
 	                        		</label>
                                </td>
							</tr>
							 
                        </tbody>
                    </table>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
							<input id="botaoVoltar" type="button" class="button" value="Voltar" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">
					Indicadores - Funcion&aacute;rio
                    <c:if test="${'true' == stBtImprimir}">
					    <input id="imprimir" type="button" class="buttonAtualizar140" style="float:right; width:125px;  margin-right:10px;" value="Contrato Meta" />
				    </c:if>
                    <c:if test="${'true' == stBtAceite}">
					    <input id="aceite" type="button" class="buttonAtualizar140" style="float:right; width:125px; margin-right:10px;" value="Aceitar" />
				    </c:if>
				    <input id="simular" type="button" class="buttonAtualizar140" style="float:right; width:90px;  margin-right:10px;" value="Simulador" />
					<br/>					
				</div>
				<fmt:setLocale value="pt_BR" scope="request"/>
				<display:table pagesize='20' id="lista" name="lista" class="table" requestURI="" decorator="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="Indicador" style="cursor: pointer; width: 15%; vertical-align: middle;text-align: left;">
						<c:if test="${lista.idIndicador == null}">
							<b><c:out value="${lista.descricaoIndicador}"/></b>
						</c:if>
						<c:if test="${lista.idIndicador != null}">
							<c:out value="${lista.descricaoIndicador}"/>
						</c:if>
					</display:column>
					<display:column title="Peso" style="cursor: pointer; width: 05%; vertical-align: middle;text-align: center;">
						<c:if test="${lista.idIndicador == null}">
							<b><c:out value="${lista.pesoFormatado}"/></b>
						</c:if>
						<c:if test="${lista.idIndicador != null}">
							<c:out value="${lista.pesoFormatado}"/>
						</c:if>
					</display:column>
					<display:column property="numEscala" title="Escala" style="cursor: pointer; width: 05%; vertical-align: middle;text-align: center;" />
					<!-- display:column property="descricaoMetaFormatada" title="Descrição Meta" style="cursor: pointer; width: 05%; vertical-align: middle;text-align: left;" / -->
					<c:if test="${'true' == isExibeMetaRealizado}">
						<display:column property="metaFormatada" title="Meta" style="cursor: pointer; width: 05%; vertical-align: middle;text-align: center;" />
					</c:if>
					<c:if test="${'true' == isExibeMetaRealizado}">
						<display:column property="realizadoFormatado" title="Realizado" style="cursor: pointer; width: 05%; vertical-align: middle;text-align: center;" />
					</c:if>
					<display:column property="realizadoXMetaFormatado" title="Atingimento da Meta" style="cursor: pointer; width: 05%; vertical-align: middle;text-align: center;" />
					<!-- display:column property="realizadoFaixaFormatado" title="Resultado Conforme Escala" style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;" / -->
					<display:column property="realizadoPonderacaoFormatado" title="Desempenho" style="cursor: pointer; width: 05%; vertical-align: middle;text-align: center;" />
					<!-- display:column title="Fonte" property="descFonte" style="cursor: pointer; width: 8%; vertical-align: middle;text-align: center;"/ -->
					<!-- display:column property="descFormulaIndicador" title="Conceito" style="cursor: pointer; width: 10%; vertical-align: middle;text-align: center;"/ -->
					<display:column property="formulaIndicador" title="F&oacute;rmula" style="cursor: pointer; width: 25%; vertical-align: middle;text-align: left;"/>

					<display:footer>
					  	<tr>
			  				<td style="font-weight:bold;text-align: left;">
			  					<font color="red"><c:out value="${total.descricaoIndicador}"/></font>
			  				</td>
			  				<td style="font-weight:bold;text-align: center;">
			  					<c:if test="${detalheCalculo.pesoFormatado != '100,00%'}">
			  						<font color="red"><c:out value="${total.pesoFormatado}"/></font>
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
							<c:if test="${'true' == isExibeMetaRealizado}">
				  				<td style="font-weight:bold;text-align: right;">&nbsp;</td>
				  				<td style="font-weight:bold;text-align: right;">&nbsp;</td>
							</c:if>
			  				<td style="font-weight:bold;text-align: center;">
			  					<c:if test="${total.realizadoPonderacao > 100}">
			  						100,00%
			  					</c:if>
			  					<c:if test="${total.realizadoPonderacao <= 100}">
									<c:out value="${total.realizadoPonderacaoFormatado}"/>
								</c:if>
			  				</td>
			  				<td style="font-weight:bold;text-align: right;">&nbsp;</td>
			  			<tr>
				   </display:footer>					
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>