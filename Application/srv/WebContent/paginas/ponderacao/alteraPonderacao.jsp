<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/GrupoRemuneracaoVariavelBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/CargoBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/GrupoIndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/UnidadeBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/PonderacaoBusinessAjax.js'></script>
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
			fecharModal();
		});
		
		$("#botaoCancelar").click(function(){
			fecharModal();
		});
		
		
		
		PonderacaoBusinessAjax.obtemPonderacao(idPonderacao,
			function (ponderacaoVO)	{
			
				$("#idPonderacaoP").val(ponderacaoVO.idPonderacao);
				$("#pesoP").val(ponderacaoVO.pesoFormatado);
				$("#valorPremioP").val(ponderacaoVO.valorPremioFormatado);
				$("#idGrupoRemVarP").val("");				
				$("#idCargoP").val("");				
				$("#idGrupoIndicadorP").val("");				
				$("#idIndicadorP").val("");	
				$("#idUnidadePesoP").val("");							
				
				GrupoRemuneracaoVariavelBusinessAjax.obtemGruposRemuneracao( 
					function (grupoRemuneracao) {
						if (grupoRemuneracao != null) {
							var options = $('#idGrupoRemVarP').attr('options');
							for (var i=0; i<grupoRemuneracao.length; i++) {
								var grupoRemuneracaoVO = grupoRemuneracao[i];
								options[options.length] = new Option(grupoRemuneracaoVO.descricaoOnline, grupoRemuneracaoVO.idGrupoRemuneracao, false, false);
							}
							if (ponderacaoVO.idGrupoRemVar != null) {
								$("input:radio[name=grupo1][value=1]").attr('checked', true);
								$("#idGrupoRemVarP").val(ponderacaoVO.idGrupoRemVar);
								$("#idGrupoRemVarP").attr("disabled", false); 
							}
						}
				});
				
				CargoBusinessAjax.obtemCargos( 
					function (cargos) {
						if (cargos != null) {
							var options = $('#idCargoP').attr('options');
							for (var i=0; i<cargos.length; i++) {
								var cargoVO = cargos[i];
								options[options.length] = new Option(cargoVO.descricaoCargo, cargoVO.idCargo, false, false);
							}
							if (ponderacaoVO.idCargo != null) {
								$("input:radio[name=grupo1][value=2]").attr('checked', true);
								$("#idCargoP").val(ponderacaoVO.idCargo);
								$("#idCargoP").attr("disabled", false); 
							}
						}
				});	
				
				GrupoIndicadorBusinessAjax.obtemGruposIndicadores( 
					function (grupoIndicadores) {
						if (grupoIndicadores != null) {
							var options = $('#idGrupoIndicadorP').attr('options');
							for (var i=0; i<grupoIndicadores.length; i++) {
								var grupoIndicadorVO = grupoIndicadores[i];
								options[options.length] = new Option(grupoIndicadorVO.descricaoGrupoIndicador, grupoIndicadorVO.idGrupoIndicador, false, false);
							}
							if (ponderacaoVO.idGrupoIndicador != null) {
								$("input:radio[name=grupo2][value=1]").attr('checked', true);
								$("#idGrupoIndicadorP").val(ponderacaoVO.idGrupoIndicador);
								$("#idGrupoIndicadorP").attr("disabled", false); 
							}
						}
				});			
		
				IndicadorBusinessAjax.obtemListaIndicadores( 
					function (indicadores) {
						if (indicadores != null) {
							var options = $('#idIndicadorP').attr('options');
							for (var i=0; i<indicadores.length; i++) {
								var indicadorVO = indicadores[i];
								options[options.length] = new Option(indicadorVO.descricaoIndicador, indicadorVO.codIndicador, false, false);
							}
							if (ponderacaoVO.idIndicador != null) {
								$("input:radio[name=grupo2][value=2]").attr('checked', true);
								$("#idIndicadorP").val(ponderacaoVO.idIndicador);
								$("#idIndicadorP").attr("disabled", false); 
							}
						}
				});
				
				UnidadeBusinessAjax.obtemUnidades(
					function (unidades) {
						if (unidades != null) {
							var options = $('#idUnidadePesoP').attr('options');
							for (var i=0; i<unidades.length; i++) {
								var unidadeVO = unidades[i];
								options[options.length] = new Option(unidadeVO.descricaoUnidade, unidadeVO.idUnidade, false, false);
							}
							if (ponderacaoVO.unidadePeso != null) {
								$("#idUnidadePesoP").val(ponderacaoVO.unidadePeso);
							}
						}
				});					
				
				
		FilialBusinessAjax.obterListaTipoFiliais(
		function (tipoFiliais) {
			if (tipoFiliais != null) {
					var options1 = document.getElementById('idTipoFilialH');
					for (var i=0; i<tipoFiliais.length; i++) {
						var tpFilialVO = tipoFiliais[i];
						options1[options1.length] = new Option(tpFilialVO.descricao,tpFilialVO.codTpFilial,false, false);
					}
					
					if (tpFilialVO.codTpFilial != null) {
						$("input:radio[name=grupo3][value=1]").attr('checked', true);
						$("input:radio[name=grupo3][value=2]").attr('checked', false);
						$("#idTipoFilialH").val(idTipoFilial);
						$("#idTipoFilialH").attr("disabled", false); 
					}
					
							
				}
	    });
	    
	    
	    FilialBusinessAjax.obterTodasFiliais(
		function (filiais) {
			if (filiais != null) {
					var options1 = document.getElementById('idFilialH');
					for (var i=0; i<filiais.length; i++) {
						var filialVO = filiais[i];
						options1[options1.length] = new Option(filialVO.descricao,filialVO.codFilial,false, false);
					}
					
					if (filialVO.codFilial != null) {
						//$("input:radio[name=grupo3][value=1]").attr('checked', false);
						$("input:radio[name=grupo3][value=2]").attr('checked', true);
						$("#idFilialH").val(idFilial);
						$("#idFilialH").attr("disabled", false); 
					}
					
							
				}
	    });
				
								
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
		     	pesoP:  				{pesoOuPremio: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Informar peso ou pr�mio.</span>'},
		     	valorPremioP:  			{pesoOuPremio: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Informar peso ou pr�mio.</span>'}
	        },
	        submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "ponderacao.do?operacao=alteraPonderacao";   
	            document.detMeta.submit();
	         }
    	});
    	
    	
    	$("#idPonderacaoK").val(idPonderacao);
		$("#idGrupoRemVarFiltro").val(idGrupoRemVarF);
		$("#idCargoFiltro").val(idCargoF);
		$("#idGrupoIndicadorFiltro").val(idGrupoIndicadorF);
		$("#idIndicadorFiltro").val(idIndicadorF);
		
    });
    
    function alteraGrupo1() {
    	var itemSelecionado = $("#detMeta input[name='grupo1']:checked").val();
    	
    	if (itemSelecionado == 1) {
    		$("#idCargoP").val("");
    		$("#idGrupoRemVarP").attr("disabled", false); 
    		$("#idCargoP").attr("disabled", true); 
    	
    	} else if (itemSelecionado == 2) {
    		$("#idGrupoRemVarP").val("");
    		$("#idGrupoRemVarP").attr("disabled", true); 
    		$("#idCargoP").attr("disabled", false); 
    	}	
	}	
	
	
    function alteraGrupo2() {
    	var itemSelecionado = $("#detMeta input[name='grupo2']:checked").val();
    	
    	if (itemSelecionado == 1) {
    		$("#idIndicadorP").val("");
    		$("#idGrupoIndicadorP").attr("disabled", false); 
    		$("#idIndicadorP").attr("disabled", true); 
    	
    	} else if (itemSelecionado == 2) {
    		$("#idGrupoIndicadorP").val("");
    		$("#idGrupoIndicadorP").attr("disabled", true); 
    		$("#idIndicadorP").attr("disabled", false); 
    	}	
	}	    
	
	
	function alteraGrupo3() {
    	var itemSelecionado = $("#detMeta input[name='grupo3']:checked").val();
    	
    	if (itemSelecionado == 1) {
    		$("#idFilialH").val("");
    		$("#idTipoFilialH").attr("disabled", false); 
    		$("#idFilialH").attr("disabled", true); 
    	
    	} else if (itemSelecionado == 2) {
    		$("#idTipoFilialH").val("");
    		$("#idTipoFilialH").attr("disabled", true); 
    		$("#idFilialH").attr("disabled", false); 
    	}	
	}	    
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 820px">
	<form name="detMeta" id="detMeta" method="post"> 
			
		<!--Filtro-->
		<input type='hidden' id="idGrupoRemVarFiltro"  	  name="idGrupoRemVarF"   	value=""/>
		<input type='hidden' id="idCargoFiltro"    		  name="idCargoF"     		value=""/>
		<input type='hidden' id="idIndicadorFiltro"    	  name="idGrupoIndicadorF"  value=""/>
		<input type='hidden' id="idGrupoIndicadorFiltro"  name="idIndicadorF"     	value=""/>
		
		<!--Chave-->
		<input type='hidden' id="idPonderacaoK"  		  name="idPonderacaoK"   	value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" >
						<input type="radio" id="grupo1" name="grupo1" value="1" onClick="alteraGrupo1()">
						<label class="label">Grupo de Remuneração:</label> 
					</td>
					<td class="componente" >
						<select id="idGrupoRemVarP" name="idGrupoRemVarP" class="campo" disabled>
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<input type="radio" id="grupo1" name="grupo1" value="2" onClick="alteraGrupo1()">
						<label class="label">Cargo:</label> 
					</td>
					<td class="componente" >
						<select id="idCargoP" name="idCargoP" class="campo" disabled>
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<input type="radio" id="grupo2" name="grupo2" value="1" onClick="alteraGrupo2()">
						<label class="label">Grupo de Indicador:</label> 
					</td>
					<td class="componente" >
						<select id="idGrupoIndicadorP" name="idGrupoIndicadorP" class="campo" disabled>
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<input type="radio" id="grupo2" name="grupo2" value="2" onClick="alteraGrupo2()">
						<label class="label">Indicador:</label> 
					</td>
					<td class="componente" >
						<select id="idIndicadorP" name="idIndicadorP" class="campo" disabled>
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				
				<tr>
					<td class="componente" >
						<input type="radio" id="grupo3" name="grupo3" value="1" onClick="alteraGrupo3()" checked>
						<label class="label">Tipo Filial:</label> 
					</td>
					<td class="componente" >
						<select id="idTipoFilialH" name="idTipoFilialH" class="campo" disabled>
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<input type="radio" id="grupo3" name="grupo3" value="2" onClick="alteraGrupo3()" checked>
						<label class="label">Filial:</label> 
					</td>
					<td class="componente" >
						<select id="idFilialH" name="idFilialH" class="campo" >
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
								
				<tr>
					<td class="componente" >
						<label class="label">Unidade Peso:</label> 
					</td>
					<td class="componente" >
						<select id="idUnidadePesoP" name="idUnidadePesoP" class="campo">
							<option value="" selected>Selecione...</option>
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