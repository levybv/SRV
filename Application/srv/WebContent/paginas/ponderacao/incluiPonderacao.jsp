<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/GrupoRemuneracaoVariavelBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/CargoBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/GrupoIndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/UnidadeBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/PonderacaoBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/FilialBusinessAjax.js'></script>

<script type='text/javascript' src='srvdwr/engine.js'></script>	
	
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.alphanumeric.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.hotkeys-0.7.9.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskedinput-1.2.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.validate.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.tipsy.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="js/lib/jquery.hoverIntent.js"></script>



<script type="text/javascript" src="js/ns/neuro.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.validate.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>

<script type="text/javascript">

	$(document).ready(function() {

		$(document).bind('keydown', 'esc', function () {
	       	document.detMeta.action = "ponderacao.do?operacao=inicio";   
            document.detMeta.submit();
		});
		
		$("#botaoCancelar").click(function(){
	       	document.detMeta.action = "ponderacao.do?operacao=inicio";   
            document.detMeta.submit();
		});

		$("#pesoP").keypress(function(){
			return mascara(this, moedaSemRS);
		});	
		$("#valorPremioP").keypress(function(){
			return mascara(this, moedaSemRS);
		});
		
		jQuery.validator.addMethod('pesoOuPremio', function(value, element, params) {
			if ($("#pesoP").val() == "" && $("#valorPremioP").val() == "") {
				return false;
			}
			return true;
		});		

		jQuery.validator.addMethod('unidadePeso', function(value, element, params) {
			if ($("#pesoP").val() != "" && $("#idUnidadePesoP").val() == "") {
				return false;
			}
			return true;
		});		
		
		$("#detMeta").validate({
			rules: {
	    		idGrupoRemVarP:   		{required:true},
	    		idCargoP:     			{required:true},
	    		idGrupoIndicadorP: 		{required:true},
	    		idIndicadorP: 			{required:true},
	    		idUnidadePesoP:		 	{unidadePeso:true},
	    		pesoP:		 			{pesoOuPremio:true},
	    		valorPremioP:			{pesoOuPremio:true}
			},
	     	messages: {
		     	idGrupoRemVarP:    		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	idCargoP:      			{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	idGrupoIndicadorP:  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	idIndicadorP:  			{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	idUnidadePesoP:  		{unidadePeso:  '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Informar unidade para o peso.</span>'},
		     	pesoP:  				{pesoOuPremio: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Informar peso ou prêmio.</span>'},
		     	valorPremioP:  			{pesoOuPremio: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Informar peso ou prêmio.</span>'}
	        },
	        submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "ponderacao.do?operacao=incluiPonderacao";   
	            document.detMeta.submit();
	         }
    	});
    	
		if ($("#filialS").val()!="") {
			$("#idFilialH").val($("#filialS").val());
		} else if (idDescrFiliaisF!="") {
			$("#idFilialH").val(idDescrFiliaisF);
		}
		if ($("#tipoFilialS").val()!="") {
			$("#idTipoFilialH").val($("#tipoFilialS").val());
		} else if (idDescrTipoFiliaisF!="") {
			$("#idTipoFilialH").val(idDescrTipoFiliaisF);
		}
		if ($("#grupoRemVarS").val()!="") {
			$("#idGrupoRemVarP").val($("#grupoRemVarS").val());
		} else if (idGrupoRemVarF!="") {
			$("#idGrupoRemVarP").val(idGrupoRemVarF);
		}
		if ($("#cargoS").val()!="") {
			$("#idCargoP").val($("#cargoS").val());
		} else if (idCargoF!="") {
			$("#idCargoP").val(idCargoF);
		}
		if ($("#grupoIndicadorS").val()!="") {
			$("#idGrupoIndicadorP").val($("#grupoIndicadorS").val());
		} else if (idGrupoIndicadorF!="") {
			$("#idGrupoIndicadorP").val(idGrupoIndicadorF);
		}
		if ($("#indicadorS").val()!="") {
			$("#idIndicadorP").val($("#indicadorS").val());
		} else if (idIndicadorF!="") {
			$("#idIndicadorP").val(idIndicadorF);
		}

		if($("#idFilialH").val()==""){
			$("#rdoFil2").attr("checked", false);
			$("#rdoFil1").attr("checked", true);
    		$("#idTipoFilialH").attr("disabled", false);
    		$("#idFilialH").attr("disabled", true);
		} else {
			$("#rdoFil2").attr("checked", true);
			$("#rdoFil1").attr("checked", false);
    		$("#idTipoFilialH").attr("disabled", true);
    		$("#idFilialH").attr("disabled", false);
		}
		if($("#idGrupoRemVarP").val()==""){
			$("#rdoRem2").attr("checked", true);
			$("#rdoRem1").attr("checked", false);
    		$("#idCargoP").attr("disabled", false);
    		$("#idGrupoRemVarP").attr("disabled", true);
		} else{
			$("#rdoRem2").attr("checked", false);
			$("#rdoRem1").attr("checked", true);
    		$("#idCargoP").attr("disabled", true);
    		$("#idGrupoRemVarP").attr("disabled", false);
		}
		if($("#idGrupoIndicadorP").val()==""){
			$("#rdoInd1").attr("checked", false);
			$("#rdoInd2").attr("checked", true);
    		$("#idIndicadorP").attr("disabled", false);
    		$("#idGrupoIndicadorP").attr("disabled", true);
		} else{
			$("#rdoInd1").attr("checked", true);
			$("#rdoInd2").attr("checked", false);
    		$("#idIndicadorP").attr("disabled", true);
    		$("#idGrupoIndicadorP").attr("disabled", false);
		}


		$("#rdoRem1").click(function(){
			$("#idCargoP").val("");
			$("#idGrupoRemVarP").attr("disabled", false); 
			$("#idCargoP").attr("disabled", true); 
			}); 

		$("#rdoRem2").click(function(){
			$("#idGrupoRemVarP").val("");
			$("#idGrupoRemVarP").attr("disabled", true); 
			$("#idCargoP").attr("disabled", false); 
			}); 

		$("#rdoInd1").click(function(){
			$("#idIndicadorP").val("");
			$("#idGrupoIndicadorP").attr("disabled", false); 
			$("#idIndicadorP").attr("disabled", true); 
			}); 
	    	
		$("#rdoInd2").click(function(){
			$("#idGrupoIndicadorP").val("");
			$("#idGrupoIndicadorP").attr("disabled", true); 
			$("#idIndicadorP").attr("disabled", false); 
			}); 
			
	    $("#rdoFil1").click(function(){
			$("#idFilialH").val("");
			$("#idFilialH").attr("disabled", true); 
			$("#idTipoFilialH").attr("disabled", false); 
			});

	    $("#rdoFil2").click(function(){
			$("#idTipoFilialH").val("");
			$("#idTipoFilialH").attr("disabled", true); 
			$("#idFilialH").attr("disabled", false); 
			});	

	});
	

</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 820px">
	<form name="detMeta" id="detMeta" method="post"> 
			
		<!--Filtro-->
		<input type='hidden' id="idGrupoRemVarFiltro"  	  name="idGrupoRemVarF"   	value=""/>
		<input type='hidden' id="idCargoFiltro"    		  name="idCargoF"     		value=""/>
		<input type='hidden' id="idIndicadorFiltro"    	  name="idGrupoIndicadorF"  value=""/>
		<input type='hidden' id="idGrupoIndicadorFiltro"  name="idIndicadorF"     	value=""/>

		<input type="hidden" name="grupoRemVarS" id="grupoRemVarS" value="<c:out value="${grupoRemVarS}"/>">
		<input type="hidden" name="cargoS" id="cargoS" value="<c:out value="${cargoS}"/>">
		<input type="hidden" name="grupoIndicadorS" id="grupoIndicadorS" value="<c:out value="${grupoIndicadorS}"/>">
		<input type="hidden" name="indicadorS" id="indicadorS" value="<c:out value="${indicadorS}"/>">
		<input type="hidden" name="filialS" id="filialS" value="<c:out value="${filialS}"/>">
		<input type="hidden" name="tipoFilialS" id="tipoFilialS" value="<c:out value="${tipoFilialS}"/>">

		<table class="tabelaComponente">
			<tbody> 
				<tr style='background-color: #f0f3f5;'>
					<td class="componente" >
						<input type="radio" id="rdoRem1" name="rdoRemuneracao" value="1" >
						<label class="label">Grupo de Remuneração:</label> 
					</td>
					<td class="componente" >
						<select id="idGrupoRemVarP" name="idGrupoRemVarP" class="campo">
							<option value="" selected>[SELECIONE]</option>
							<c:forEach var="itemGrupoRemVar" items="${listGrupoRemVar}">
								<option value='<c:out value="${itemGrupoRemVar.idGrupoRemuneracao}" />'><c:out value="${itemGrupoRemVar.descricaoOnline}" /></option>
							</c:forEach>
						</select>
					</td>
				</tr>				
				<tr style='background-color: #f0f3f5;'>
					<td class="componente" >
						<input type="radio" id="rdoRem2" name="rdoRemuneracao" value="2" checked>
						<label class="label">Cargo:</label> 
					</td>
					<td class="componente" >
						<select id="idCargoP" name="idCargoP" class="campo" >
							<option value="" selected>[SELECIONE]</option>
							<c:forEach var="itemCargo" items="${listCargos}">
								<option value='<c:out value="${itemCargo.idCargo}" />'><c:out value="${itemCargo.descricaoCargo}" /></option>
							</c:forEach>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<input type="radio" id="rdoInd1" name="rdoIndicador" value="1" >
						<label class="label">Grupo de Indicador:</label> 
					</td>
					<td class="componente" >
						<select id="idGrupoIndicadorP" name="idGrupoIndicadorP" class="campo">
							<option value="" selected>[SELECIONE]</option>
							<c:forEach var="itemGrupoIndicador" items="${listGrupoIndicadores}">
								<option value='<c:out value="${itemGrupoIndicador.idGrupoIndicador}" />'><c:out value="${itemGrupoIndicador.descricaoGrupoIndicador}" /></option>
							</c:forEach>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<input type="radio" id="rdoInd2" name="rdoIndicador" value="2" checked>
						<label class="label">Indicador:</label> 
					</td>
					<td class="componente" >
						<select id="idIndicadorP" name="idIndicadorP" class="campo" >
							<option value="" selected>[SELECIONE]</option>
							<c:forEach var="itemIndicador" items="${listIndicadores}">
								<option value='<c:out value="${itemIndicador.codIndicador}" />'><c:out value="${itemIndicador.codIndicador}" /> - <c:out value="${itemIndicador.descricaoIndicador}" /></option>
							</c:forEach>
						</select>
					</td>
				</tr>				
				
				
				<tr style='background-color: #f0f3f5;'>
					<td class="componente" >
						<input type="radio" id="rdoFil1" name="rdoFilial" value="1" checked>
						<label class="label">Tipo Filial:</label> 
					</td>
					<td class="componente" >
						<select id="idTipoFilialH" name="idTipoFilialH" class="campo">
							<option value="" selected>[SELECIONE]</option>
							<c:forEach var="itemTpFilial" items="${listTipoFiliais}">
								<option value='<c:out value="${itemTpFilial.codTpFilial}" />'><c:out value="${itemTpFilial.descricao}" /></option>
							</c:forEach>
						</select>
					</td>
				</tr>				
				<tr style='background-color: #f0f3f5;'>
					<td class="componente" >
						<input type="radio" id="rdoFil2" name="rdoFilial" value="2" >
						<label class="label">Filial:</label> 
					</td>
					<td class="componente" >
						<select id="idFilialH" name="idFilialH" class="campo" >
							<option value="" selected>[SELECIONE]</option>
							<c:forEach var="itemFilial" items="${listFiliais}">
								<option value='<c:out value="${itemFilial.codFilial}" />'><c:out value="${itemFilial.descricao}" /></option>
							</c:forEach>
						</select>
					</td>
				</tr>
				
								
				<tr>
					<td class="componente" >
						<label class="label">Unidade Peso:</label> 
					</td>
					<td class="componente" >
						<select id="idUnidadePesoP" name="idUnidadePesoP" class="campo">
							<option value="" selected>[SELECIONE]</option>
							<c:forEach var="itemUnidade" items="${listUnidades}">
								<option value='<c:out value="${itemUnidade.idUnidade}" />'><c:out value="${itemUnidade.descricaoUnidade}" /></option>
							</c:forEach>
						</select>
					</td>
				</tr>	
					
				<tr>
					<td class="componente" >
						<label class="label">Peso:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="pesoP" name="pesoP" value="" maxlength="14" size="15"  />
					</td>
				</tr>					
				<tr>
					<td class="componente" >
						<label class="label">Valor Prêmio:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="valorPremioP" name="valorPremioP" value="" maxlength="14" size="15"  />
					</td>
				</tr>
			</tbody>
		</table>
		<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Salvar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
	</form>
</div>