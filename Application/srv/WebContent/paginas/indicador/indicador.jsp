<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript" src="js/ms/srv.indicadorcrud.js" charset="ISO-8859-1"></script> 
<script type="text/javascript" >
	var idIndicador=-1;
	var idIndicadorF="";
	var descricaoF="";
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Indicadores</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label" for="filialDesc">Código:</label>
                                	<input id="idIndicadorF" name="idIndicadorF" value="<c:out value='${idIndicadorF}'/>" class="campo2" maxlength="5" style="width: 50px;" value="" type="text"/>
                                </td>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label" for="filialDesc">Descrição:</label>
                                	<input id="descricaoF" name="descricaoF" value="<c:out value='${descricaoF}'/>" class="campo2" maxlength="100" style="width: 250px;" value="" type="text"/>
                                </td>
 	                        	<td class="componente" width="34%">
 	                        		<label class="label">Grupo Indicador:</label>
									<select id="grupoIndicadorF" name="grupoIndicadorF" class="campo">
										<option value="" selected>Selecione...</option>
										<c:forEach var="itemGrpIndic" items="${grupoIndicadores}">
											<option value="<c:out value='${itemGrpIndic.idGrupoIndicador}' />" <c:if test="${itemGrpIndic.idGrupoIndicador == grupoIndicadorF}">selected</c:if> ><c:out value='${itemGrpIndic.descricaoGrupoIndicador}' /></option>
										</c:forEach>
									</select>
                                </td>
                            </tr> 
                        	<tr>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label">Ativo?</label>
									<select id="ativoF" name="ativoF" class="campo">
										<option value="S" selected>Sim</option>
										<option value="N" <c:if test="${ativoF=='N'}">selected</c:if>>Não</option>
									</select>
                                </td>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label">Fonte:</label>
									<select id="fonteF" name="fonteF" class="campo" style="width:200px;">
										<option value="" selected>SELECIONE...</option>
										<c:forEach var="itemFonte" items="${listaFonte}">
											<option value="<c:out value='${itemFonte}' />" <c:if test="${itemFonte == fonteF}">selected</c:if> ><c:out value='${itemFonte}' /></option>
										</c:forEach>
									</select>
                                </td>
 	                        	<td class="componente" width="34%">
 	                        		<label class="label">Diretoria:</label>
									<select id="diretoriaF" name="diretoriaF" class="campo">
										<option value="" selected>SELECIONE...</option>
										<c:forEach var="itemDiretoria" items="${listaDiretoria}">
											<option value="<c:out value='${itemDiretoria}' />" <c:if test="${itemDiretoria == diretoriaF}">selected</c:if> ><c:out value='${itemDiretoria}' /></option>
										</c:forEach>
									</select>
                                </td>
                            </tr> 
                        	<tr>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label">Per&iacute;odo (B&ocirc;nus):</label>
									<select id="periodoF" name="periodoF" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
										<c:forEach var="itemPeriodo" items="${listaPeriodo}">
											<option value="<c:out value='${itemPeriodo}' />" <c:if test="${itemPeriodo == periodoF}">selected</c:if> ><c:out value='${itemPeriodo}' /></option>
										</c:forEach>
                                    </select>
                                </td>
 	                        	<td class="componente" width="33%">
 	                        		<label class="label"></label>
                                </td>
 	                        	<td class="componente" width="34%">
 	                        		<label class="label"></label>
                                </td>
                            </tr> 
                        </tbody>
                    </table>
                </div>
				<div id="botao-geral">
					<div id="botao" class="barBottom"> 
						<div id="div-botao">
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
					Indicadores
                    <c:if test="${'true' == stBtIncluir}">
						<input id="incluir" type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					</c:if>
                    <c:if test="${'true' == stBtAlterar}">
						<input id="atualizar" type="button" class="buttonAtualizar" value="Alterar" />
					</c:if>
					<br/>
				</div>
				
				<display:table pagesize='10' id="indicadores" name="indicadores" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />
					<display:column title="" style="width:5%; text-align:center; vertical-align: middle; "><input type="radio" name="checkbox" id="checkbox" value="${indicadores.idIndicador}"></display:column>
					<display:column property="idIndicador" title="Código" style="width:5%; cursor: pointer; vertical-align: middle; text-align:center; "/> 
					<display:column property="descricaoIndicador" title="Descrição" style="cursor: pointer; vertical-align: middle; width:30%;" /> 
					<display:column title="Sentido" style="cursor: pointer; vertical-align: bottom; width:5%; text-align:center;">
	                    <c:if test="${'C' == indicadores.flgSentido}">
							<img src="images/m_cima.png" alt="Melhor para cima" />
						</c:if>
	                    <c:if test="${'B' == indicadores.flgSentido}">
							<img src="images/m_baixo.png" alt="Melhor para baixo" />
						</c:if>
					</display:column> 
					<display:column property="descricaoGrupo" title="Grupo" style="width:8%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="ativoFormatado" title="Ativo" style="width:5%;  text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="formulaIndicador" title="Formula" style="width:20%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="descricaoFonte" title="Fonte" style="text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="descricaoDiretoria" title="Diretoria" style="text-align:center; cursor: pointer; vertical-align: middle;"/>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>