<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type='text/javascript' src='srvdwr/interface/GrupoRemuneracaoVariavelBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/CargoBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/GrupoIndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/UnidadeBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/FilialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	

<script type="text/javascript" >

	//chave
	var idPonderacao=-1;
	
	//Filtro
	var idGrupoRemVarF			= "<c:out value="${idGrupoRemVarF}"/>";
	var idCargoF				= "<c:out value="${idCargoF}"/>";
	var idGrupoIndicadorF		= "<c:out value="${idGrupoIndicadorF}"/>";
	var idIndicadorF			= "<c:out value="${idIndicadorF}"/>";
	var idDescrFiliaisF			= "<c:out value="${idDescrFiliaisF}"/>";
	var idDescrTipoFiliaisF		= "<c:out value="${idDescrTipoFiliaisF}"/>";
	var rdoRemuneracaoF			= "<c:out value="${rdoRemuneracaoF}"/>";
	var rdoIndicadorF			= "<c:out value="${rdoIndicadorF}"/>";
	var rdoFilialF				= "<c:out value="${rdoFilialF}"/>";
	var exibeInclusao			= "<c:out value="${exibeInclusao}"/>";
	<c:remove var="exibeInclusao"/>

</script> 

<script type="text/javascript" src="js/ms/srv.ponderacao.js" charset="ISO-8859-1"></script>

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Ponderação</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="55%">
 	                        		<input type="radio" name="rdoRemuneracao" id="rdoRemuneracao2" value="2" checked="true">
 	                        		<label class="label" for="filialDesc">Cargo:</label>
									<select id="idCargoF" name="idCargoF" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
										<c:forEach var="itemCargo" items="${listCargos}">
											<c:choose>
												<c:when test="${itemCargo.idCargo == idCargoF}">
													<option value='<c:out value="${itemCargo.idCargo}"/>' selected ><c:out value="${itemCargo.descricaoCargo}" /></option>
												</c:when>
												<c:otherwise>
													<option value='<c:out value="${itemCargo.idCargo}" />'><c:out value="${itemCargo.descricaoCargo}" /></option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
                                    </select>
                                </td>                                
 	                        	<td class="componente">
 	                        		<input type="radio" name="rdoRemuneracao" id="rdoRemuneracao1" value="1">
 	                        		<label class="label" for="filialDesc">Grupo Remuneração:</label>
									<select id="idGrupoRemVarF" name="idGrupoRemVarF" class="campo" disabled>
                                    	<option value="" selected>[SELECIONE]</option>
										<c:forEach var="itemGrupoRemVar" items="${listGrupoRemVar}">
											<c:choose>
												<c:when test="${itemGrupoRemVar.idGrupoRemuneracao == idGrupoRemVarF}">
													<option value='<c:out value="${itemGrupoRemVar.idGrupoRemuneracao}" />' selected ><c:out value="${itemGrupoRemVar.descricaoOnline}" /></option>
												</c:when>
												<c:otherwise>
													<option value='<c:out value="${itemGrupoRemVar.idGrupoRemuneracao}" />'><c:out value="${itemGrupoRemVar.descricaoOnline}" /></option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
                                    </select>
                                </td>
                            </tr> 
                        	<tr>
 	                        	<td class="componente">
 	                        		<input type="radio" name="rdoIndicador" id="rdoIndicador2" value="2" checked="true">
 	                        		<label class="label" for="filialDesc">Indicador:</label>
									<select id="idIndicadorF" name="idIndicadorF" class="campo">
                                    	<option value="" selected>[SELECIONE]</option>
										<c:forEach var="itemIndicador" items="${listIndicadores}">
											<c:choose>
												<c:when test="${itemIndicador.codIndicador == idIndicadorF}">
													<option value='<c:out value="${itemIndicador.codIndicador}" />' selected ><c:out value="${itemIndicador.codIndicador}" /> - <c:out value="${itemIndicador.descricaoIndicador}" /></option>
												</c:when>
												<c:otherwise>
													<option value='<c:out value="${itemIndicador.codIndicador}" />'><c:out value="${itemIndicador.codIndicador}" /> - <c:out value="${itemIndicador.descricaoIndicador}" /></option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
                                    </select>
                                </td>
 	                        	<td class="componente">
 	                        		<input type="radio" name="rdoIndicador" id="rdoIndicador1" value="1">
 	                        		<label class="label" for="filialDesc">Grupo Indicador:</label>
									<select id="idGrupoIndicadorF" name="idGrupoIndicadorF" class="campo" disabled>
                                    	<option value="" selected>[SELECIONE]</option>
										<c:forEach var="itemGrupoIndicador" items="${listGrupoIndicadores}">
											<c:choose>
												<c:when test="${itemGrupoIndicador.idGrupoIndicador == idGrupoIndicadorF}">
													<option value='<c:out value="${itemGrupoIndicador.idGrupoIndicador}" />' selected ><c:out value="${itemGrupoIndicador.descricaoGrupoIndicador}" /></option>
												</c:when>
												<c:otherwise>
													<option value='<c:out value="${itemGrupoIndicador.idGrupoIndicador}" />'><c:out value="${itemGrupoIndicador.descricaoGrupoIndicador}" /></option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
                                    </select>
                                </td>
                            </tr> 
	                         <tr>
	                             <td class="componente">
 	                        		<input type="radio" name="rdoFilial" id="rdoFilial2" value="2" checked="true">
 	                        		<label class="label" for="filialDesc">Filial:</label>
									<select id="idDescrFiliaisF" name="idDescrFiliaisF" class="campo">
										<option value="" selected>[SELECIONE]</option>
										<c:forEach var="itemFilial" items="${listFiliais}">
											<c:choose>
												<c:when test="${itemFilial.codFilial == idDescrFiliaisF}">
													<option value='<c:out value="${itemFilial.codFilial}" />' selected ><c:out value="${itemFilial.descricao}" /></option>
												</c:when>
												<c:otherwise>
													<option value='<c:out value="${itemFilial.codFilial}" />'><c:out value="${itemFilial.descricao}" /></option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
		                            </select>
		                        </td>
	                             <td class="componente">
 	                        		<input type="radio" name="rdoFilial" id="rdoFilial1" value="1">
 	                        		<label class="label" for="filialDesc">Tipo Filial:</label>
									<select id="idDescrTipoFiliaisF" name="idDescrTipoFiliaisF" class="campo" disabled>
										<option value="" selected>[SELECIONE]</option>
										<c:forEach var="itemTpFilial" items="${listTipoFiliais}">
											<c:choose>
												<c:when test="${itemTpFilial.codTpFilial == idDescrTipoFiliaisF}">
													<option value='<c:out value="${itemTpFilial.codTpFilial}" />' selected ><c:out value="${itemTpFilial.descricao}" /></option>
												</c:when>
												<c:otherwise>
													<option value='<c:out value="${itemTpFilial.codTpFilial}" />'><c:out value="${itemTpFilial.descricao}" /></option>
												</c:otherwise>
											</c:choose>
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
							<input id="botaoLimpar" type="button" class="button" value="Limpar" />
							<input id="botaoConsultar" type="button" class="button" value="Filtrar" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">
					Ponderação
					<input id="excluir" 	type="button" class="buttonAtualizar" style="float:right" value="Excluir" />
					<input id="atualizar" 	type="button" class="buttonAtualizar" style="float:right" value="Alterar" />
					<input id="incluir" 	type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					<br/>
				</div>
				
				<display:table pagesize='10' id="ponderacoes" name="ponderacoes" class="table" requestURI="">
					<display:setProperty name="paging.banner.placement" value="bottom" />
					<display:column title="" style="width:5%; text-align:center; "><input type="radio" name="checkbox" id="checkbox" value="${ponderacoes.idPonderacao}@${ponderacoes.descricaoGrupoRemVar}@${ponderacoes.descricaoCargo}@${ponderacoes.descricaoGrupoIndicador}@${ponderacoes.descricaoIndicador}@${ponderacoes.pesoFormatado}@${ponderacoes.valorPremioFormatado}@${ponderacoes.descrTipoFil}@${ponderacoes.descrFil}@${ponderacoes.idFilial}@${ponderacoes.idTipoFilial}"></display:column>
					<display:column property="idPonderacao" 			title="Código" 				style="width:05%; text-align:center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="descricaoGrupoRemVar" 	title="Grupo Remuneração" 	style="width:8%;  text-align:left;   cursor: pointer; vertical-align: middle;"/>
					<display:column property="descricaoCargo" 			title="Cargo" 				style="width:12%; text-align:left;   cursor: pointer; vertical-align: middle;"/>
					<display:column property="descricaoGrupoIndicador" 	title="Grupo Indicador" 	style="width:12%; text-align:left;   cursor: pointer; vertical-align: middle;"/>
					<display:column property="descricaoIndicador"  		title="Indicador" 			style="width:12%; text-align:left;   cursor: pointer; vertical-align: middle;"/>
					<display:column property="pesoFormatado"  			title="Peso" 				style="width:04%; text-align:right;  cursor: pointer; vertical-align: middle;"/>
					<display:column property="valorPremioFormatado"  	title="Valor Prêmio" 		style="width:07%; text-align:right;  cursor: pointer; vertical-align: middle;"/>
					<display:column property="descrTipoFil"  	        title="Tipo Filial" 		style="width:05%; text-align:right;  cursor: pointer; vertical-align: middle;"/>
					<display:column property="descrFil"  	            title="Filial" 		        style="width:09%; text-align:right;  cursor: pointer; vertical-align: middle;"/>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>