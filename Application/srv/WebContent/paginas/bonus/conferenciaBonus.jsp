<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.bonusExtratoFuncionario.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
	var idFuncionario=-1;
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Extrato do Funcionário - Desempenho do Colaborador</div>
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
				<input type="hidden" id="situacaoColaborador" name="situacaoColaborador" value="<c:out value='${situacaoColaborador}'/>"/>
				
				
				
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
 	                        	<td class="componente" width="30%" colspan="2">
 	                        		<label class="label" for="filialDesc">Período:</label>
									<select id="periodos" name="periodos" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
                                    	<option value="20110012" <c:if test="${'20110012' == periodoSelecionado}">selected</c:if>>DEZEMBRO / 2011</option>
                                    	<option value="20120012" <c:if test="${'20120012' == periodoSelecionado}">selected</c:if>>DEZEMBRO / 2012</option>
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
                            </tr>   
                        	<tr>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label" for="filialDesc">Situação do Colaborador:</label>
 	                        		<label class="label" for="filialDesc"><c:out value='${situacaoColaborador}'/></label>
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
					<input id="excluir" type="button" class="buttonAtualizar140" style="float:right" value="Remover Indicador" />
					<input id="incluir" type="button" class="buttonAtualizar140" style="float:right" value="Adicionar Indicador" />
					<input id="reprocessar" type="button" class="buttonAtualizar120" style="float:right" value="Reprocessar" />
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
					<display:column title="Fórmula" style="width:5%; text-align:center; "><div id="a" title="Formula" style="width:100%;cursor: hand;">F</div></display:column>
					<display:column title="Indicador" style="cursor: pointer; width: 15%; vertical-align: middle;text-align: left;">
						<c:if test="${lista.idIndicador == null}">
							<b><c:out value="${lista.descricaoIndicador}"/></b>
						</c:if>
						<c:if test="${lista.idIndicador != null}">
							<c:out value="${lista.descricaoIndicador}"/>
						</c:if>
					</display:column>
					<display:column property="pesoFormatado"					title="Peso"  			 			style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;"/>
					<display:column property="descricaoMetaFormatada"			title="Descrição Meta"   			style="cursor: pointer; width: 05%; vertical-align: middle;text-align: left;"/>
					<display:column property="realizadoFormatado"   			title="Realizado" 		 			style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;"/>
					<display:column property="realizadoXMetaFormatado" 			title="Atingimento da Meta" 		style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;"/>
					<display:column property="idEscala"  						title="Escala" 			 			style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;"/>
					<display:column property="realizadoFaixaFormatado"  		title="Resultado Conforme Escala" 	style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;"/>
					<display:column property="realizadoPonderacaoFormatado"  	title="Peso X Resultado" 			style="cursor: pointer; width: 05%; vertical-align: middle;text-align: right;"/>
					
					<display:footer>
					  	<tr>
						  	<td style="font-weight:bold;text-align: left;">&nbsp;</td>
			  				<td style="font-weight:bold;text-align: left;">
			  					<c:if test="${exibeBonus}">
				  					<a href="javascript:consultaDetalheCalculo(${total.idFuncionario}, ${total.idIndicador}, -1, -1, ${total.ano}, ${total.mes})"><c:out value="${total.descricaoIndicador}"/></a>
				  				</c:if>
			  					<c:if test="${!exibeBonus}">
				  					<c:out value="${total.descricaoIndicador}"/>
				  				</c:if>
			  				</td>
			  				<td style="font-weight:bold;text-align: right;">
			  					<c:if test="${total.pesoFormatado != '100,00%'}">
			  						<font color="red"><c:out value="${total.pesoFormatado}"/></font>
									<script>
										alert("O peso total desse funcionário não está 100%. Favor Verificar !");
									</script>
			  					</c:if>
			  					<c:if test="${total.pesoFormatado == '100,00%'}">
			  						<c:out value="${total.pesoFormatado}"/>
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