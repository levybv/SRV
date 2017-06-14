<%@ page contentType="text/html" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
 
<script type="text/javascript" >

$(document).ready(function() {
	$("#botaoVoltar").click(function() {
		$("#idFuncionario").val("");
		$("#nomeFuncionario").val("");
	   	document.form.action = "ajusteFuncionario.do?operacao=inicio";
	    document.form.submit();
	});		
	$("#botaoLimpar").click(function() {
		$("#ano").val("");
		$("#mes").val("");
		$("#mes").focus();
	});
    $("#botaoConsultar").click(function(){
    	if ($('#mes').val() == "") {
    		modal(450,120,null,'Marisa - Consulta Ajuste Funcionário',true,'Informe o m&ecirc;s.',false); 
    		return;
    	}
    	if ($('#ano').val() == "") {
    		modal(450,120,null,'Marisa - Consulta Ajuste Funcionário',true,'Informe o ano.',false); 
    		return;
    	}
	   	document.form.action = "ajusteFuncionario.do?operacao=pesquisaPeriodoAjuste";
	    document.form.submit();
	});
	$("#excluir").click(function() {
	   	var lista = $('input[type=radio]:checked');
		if(lista.length > 1) {
			modal(450,120,null,'Marisa - Ajuste Funcionário',true,'Selecione apenas um registro.',false);
		} else if(lista.length < 1) {
			modal(450,120,null,'Marisa - Ajuste Funcionário',true,'Selecione um registro.',false);
    	} else {
    		$("#idIndicadorS").val(lista[0].value.split("@")[0]);
    		$("#idEmpresaS").val(lista[0].value.split("@")[1]);
    		$("#idFilialS").val(lista[0].value.split("@")[2]);
    		$("#idAnoS").val(lista[0].value.split("@")[3]);
    		$("#idMesS").val(lista[0].value.split("@")[4]);
    	   	document.form.action = "ajusteFuncionario.do?operacao=excluiAjuste";
    	    document.form.submit();
    	}
	});
	$("#atualizar").click(function() {
	   	var lista = $('input[type=radio]:checked');
		if(lista.length > 1) {
			modal(450,120,null,'Marisa - Ajuste Funcionário',true,'Selecione apenas um registro.',false);
		} else if(lista.length < 1) {
			modal(450,120,null,'Marisa - Ajuste Funcionário',true,'Selecione um registro.',false);
    	} else {
    		idIndicadorO = lista[0].value.split("@")[0];
    		idEmpresaO 	 = lista[0].value.split("@")[1];
    		idFilialO 	 = lista[0].value.split("@")[2];
    		idAnoO 		 = lista[0].value.split("@")[3];
    		idMesO 		 = lista[0].value.split("@")[4];
    		valorO 		 = lista[0].value.split("@")[5];
    		modal(550,220,'paginas/ajuste/alteraAjuste.jsp','Marisa - Incluir Ajuste Funcionário',true,'',true);
    	}
	});
	$("#incluir").click(function() {
		anoF = $("#ano").val();
		mesF = $("#mes").val();
		modal(550,220,'paginas/ajuste/incluiAjuste.jsp','Marisa - Incluir Ajuste Funcionário',true,'',true);
	});
});

</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Ajuste de Funcionário</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<input type="hidden" name="idIndicadorS" id="idIndicadorS" value=""/>
				<input type="hidden" name="idEmpresaS" id="idEmpresaS" value=""/>
				<input type="hidden" name="idFilialS" id="idFilialS" value=""/>
				<input type="hidden" name="idAnoS" id="idAnoS" value=""/>
				<input type="hidden" name="idMesS" id="idMesS" value=""/>
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label">Matrícula:</label>
 	                        		<label class="label"><c:out value='${idFuncionario}'/></label>
 	                        		<input type="hidden" name="idFuncionario" id="idFuncionario" value="<c:out value='${idFuncionario}'/>"/>
                                </td>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label">Nome:</label>
 	                        		<label class="label"><c:out value='${nomeFuncionario}'/></label>
 	                        		<input type="hidden" name="nomeFuncionario" id="nomeFuncionario" value="<c:out value='${nomeFuncionario}'/>"/>
                                </td>
 	                        	<td class="componente" width="30%">
 	                        		<label class="label">M&ecirc;s:</label>
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
 	                        		<label class="label">Cargo:</label>
 	                        		<label class="label"><c:out value='${cargoFuncionario}'/></label>
 	                        		<input type="hidden" name="cargoFuncionario" id="cargoFuncionario" value="<c:out value='${cargoFuncionario}'/>"/>
                                </td>
 	                        	<td class="componente" width="35%">
 	                        		<label class="label">Área:</label>
 	                        		<label class="label"><c:out value='${centroCustoFuncionario}'/></label>
 	                        		<input type="hidden" name="centroCustoFuncionario" id="centroCustoFuncionario" value="<c:out value='${centroCustoFuncionario}'/>"/>
                                </td>
 	                        	<td class="componente" width="30%">
 	                        		<label class="label">Ano:</label>
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
							<input id="botaoConsultar" type="button" class="button" value="Filtrar" />
						</div>
						<div id="div-load" style="display: none;">
							<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
						</div>
					</div>
				</div>
				<br>
				<div class="barTitle">
					Indicadores de Ajuste 
					<input id="excluir" 	type="button" class="buttonAtualizar" style="float:right" value="Excluir" />
					<input id="atualizar" 	type="button" class="buttonAtualizar" style="float:right" value="Alterar" />
					<input id="incluir" 	type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					<br/>
				</div>
				
				<display:table pagesize='15' id="listIndicadores" name="listIndicadores" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   
					<display:column title="" style="width:5%; text-align:center; ">
						<input type="radio" name="idIndS" id="idIndS" value="${listIndicadores.idIndicador}@${listIndicadores.idEmpresa}@${listIndicadores.idFilial}@${listIndicadores.ano}@${listIndicadores.mes}@${listIndicadores.valorPremioCalculadoStr}">
					</display:column>
					<display:column property="idFuncionario"			title="Matrícula"	style="text-align: center; cursor: pointer; vertical-align: middle;" /> 
					<display:column property="idFilial"					title="Loja" 		style="text-align: center; cursor: pointer; vertical-align: middle;"/>
					<display:column property="descricaoIndicador"		title="Indicador"  	style="text-align: left; cursor: pointer; vertical-align: middle;"/>
					<display:column property="valorPremioCalculado"		title="Valor"		format="{0,number,#,##0.00}" style="text-align: center; cursor: pointer; vertical-align: middle;"/>
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>