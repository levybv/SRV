<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type='text/javascript' src='srvdwr/interface/MetaLiderBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	


<script type="text/javascript" src="js/ms/srv.metaLider.js" charset="ISO-8859-1"></script>
<script type="text/javascript" >
/*
	//chave
	var idIndicador		=-1;
	var idFuncionario	=-1;
	var ano				=-1;
	var mes				=-1;
	
	//Filtro
	
	var anoMesF					="";
	var anoF					="";
	var idFuncionarioF			="";
	var nomeFuncionarioF		="";
	var idIndicadorF			="";
	var descricaoIndicadorF		="";
*/	
	
</script> 

<div id="box" class="box">
	<div class="boxCenter">
		<div class="barTitleInternoCadastro">Metas de Lideres</div>
		<br/>
		<div class="panel">
			<form id="form" name="form" method="post" action="" accept-charset="ISO-8859-1">
				<div class="barTitle">Filtro<a href="#" id="arrow-link"><img class="arrow" alt="" src="images/arrow_up_48.png" id="arrow" /></a></div>
                <div class="painel-filtro">
                	<table class="tabelaComponente">
                    	<tbody>
                        	<tr>
 	                        	<td class="componente" width="12%">
 	                        		<label class="label" for="filialDesc">Mês/Ano:</label>
 	                        	</td>
 	                        	<td class="componente" width="38%">
									<select id="mesesF" name="mesesF" class="campo">
                                    	<option value="" selected>Selecione...</option>
                                    </select>
                                </td>
                            </tr> 
                        	<tr>
 	                        	<td class="componente">
 	                        		<label class="label" for="filialDesc">Líder:</label>
 	                        	</td>
 	                        	<td class="componente"> 	                        		
                                	<select id="liderF" name="liderF" class="campo">
                                    	<option value="" selected>Selecione...</option>
                                    	<c:forEach var="item" items="${listaLideres}">
                                    		<option value="<c:out value='${item.idFuncionario}' />" <c:if test="${item.idFuncionario == filtro.codFuncionario}">selected</c:if> ><c:out value='${item.nomeFuncionario}' /></option>
										</c:forEach>
                                    </select>
                                </td>
                                <td class="componente">
 	                        		<label class="label" for="filialDesc">Indicador:</label>
 	                        	</td>
 	                        	<td class="componente"  > 	                        		
                                	<select id="indicadorF" name="indicadorF" class="campo">
                                    	<option value="" selected>Selecione...</option>
                                    	<c:forEach var="item" items="${listaIndicador}">
                                    		<option value="<c:out value='${item.idIndicador}' />" <c:if test="${item.idIndicador == filtro.codIndicador}">selected</c:if> ><c:out value='${item.descricaoIndicador}' /></option>
										</c:forEach>
                                    </select>
                                </td>
 	                        	<td class="componente">
 	                        		<label class="label" for="filialDesc">Equipe:</label>
 	                        	</td>
 	                        	<td class="componente"  > 	                        		
                                	<select id="equipeF" name="equipeF" class="campo">
                                    	<option value="" selected>Selecione...</option>
                                    	<c:forEach var="item" items="${listaEquipes}">
                                    		<option value="<c:out value='${item}' />" <c:if test="${item == filtro.equipe}">selected</c:if> ><c:out value='${item}' /></option>
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
					Resultado da Pesquisa
					<input id="excluir" type="button" class="buttonAtualizar" style="float:right" value="Excluir" />
					<input id="atualizar" type="button" class="buttonAtualizar" style="float:right" value="Alterar" />
					<input id="incluir" type="button" class="buttonAtualizar" style="float:right" value="Incluir" />
					<br/>
				</div>
				
				<display:table pagesize='10' id="listaMetas" name="listaMetas" class="table" requestURI="">     
					<display:setProperty name="paging.banner.placement" value="bottom" />   												
					<display:column title="" style="width:5%; text-align:center; "><input type="radio" name="checkbox1" id="checkbox1" value="${listaMetas.codFuncionario};${listaMetas.codIndicador};${listaMetas.ano};${listaMetas.mes};${listaMetas.equipe};${listaMetas.metaS}"></display:column>
					<display:column property="nomeFuncionario" title="Nome" style="cursor: pointer; vertical-align: middle; width:35%; "/> 
					<display:column property="descricaoIndicador" title="Indicador" style="cursor: pointer; vertical-align: middle; width:30%; "/> 
					<display:column property="equipe" title="Equipe" style="cursor: pointer; vertical-align: middle; width:10%;" /> 
					<display:column property="meta" format="{0,number,###,##0.00}" title="Meta" style="width:10%; text-align:right; cursor: pointer; vertical-align: middle;"/>
					<display:column property="mesAno"   title="Data"   style="cursor: pointer; vertical-align: middle; width:10%;" /> 
				</display:table>
				<BR>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript" >
var mesSelecionado = "<c:out value='${filtro.mesAno}'/>";
var meses = new Array("Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro");
document.getElementById('mesesF').options.length = 0;
$('#mesesF').append($('<option>', { 
    value: "",
    text :'Selecione...'
}));

//var mesPermissaoAlteracao = "<c:out value='${mesPermissaoAlteracao}'/>";
for(var i=6;i>0;i--){
	var date = new Date();
	date.setMonth(date.getMonth()-i);
	 $('#mesesF').append($('<option>', { 
	        value: date.getYear()+""+date.getMonth(),
	        text :  meses[date.getMonth()]+"/"+date.getYear()
	    }));

	 if(mesSelecionado == date.getYear()+""+date.getMonth()){
		 $("#mesesF").val(date.getYear()+""+date.getMonth()).attr('selected',true);
	 }
}



for(var i=0;i<5;i++){
	var date = new Date();
	date.setMonth(date.getMonth()+i);
	 $('#mesesF').append($('<option>', { 
	        value: date.getYear()+""+date.getMonth(),
	        text : meses[date.getMonth()]+"/"+date.getYear()
	    }));
	 
//	 if(mesSelecionado == date.getYear()+""+date.getMonth()){
//		 $("#mesesF").val(date.getYear()+""+date.getMonth()).attr('selected',true);
//	 }
	
	 if(mesSelecionado != "" && mesSelecionado == date.getYear()+""+date.getMonth() ){
		 $("#mesesF").val(date.getYear()+""+date.getMonth()).attr('selected',true);
	 }
}
//if(mesSelecionado == ""){
	//date = new Date();
	//$("#mesesF").val(date.getYear()+""+date.getMonth()).attr('selected',true);
//}
</script>