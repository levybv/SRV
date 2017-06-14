<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/CalendarioComercialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/UnidadeBusinessAjax.js'></script>
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
	
		dwr.engine.setAsync(false);

		$(document).bind('keydown', 'esc', function () {
			fecharModal();
		});
		
		$("#botaoCancelar").click(function(){
			fecharModal();
		});
		
		//Formatação dos campos
		$("#atingimentoC").keypress(function(){
			return mascara(this, moedaSemRS);
		});	
		$("#realizadoC").keypress(function(){
			return mascara(this, moedaSemRS);
		});	

		CalendarioComercialBusinessAjax.obtemCalendarioComercialListaMes( 
			function (listaMes) {
				if (listaMes != null) {
					var options = $('#idMesC').attr('options');
					for (var i=0; i<listaMes.length; i++) {
						var mesVO = listaMes[i];
						options[options.length] = new Option(mesVO.mesStr, mesVO.mes, false, false);
					}
				}
		});

		CalendarioComercialBusinessAjax.obtemCalendarioComercialListaAno( 
			function (listaAno) {
				if (listaAno != null) {
					var options = $('#idAnoC').attr('options');
					for (var i=0; i<listaAno.length; i++) {
						var anoVO = listaAno[i];
						options[options.length] = new Option(anoVO.ano, anoVO.ano, false, false);
					}
				}
		});

		IndicadorBusinessAjax.obtemListaIndicadoresPorTipo(1, 
			function (indicadores) {
				if (indicadores != null) {
					var options = $('#idIndicadorC').attr('options');
					for (var i=0; i<indicadores.length; i++) {
						var indicadorVO = indicadores[i];
						options[options.length] = new Option(indicadorVO.descricaoIndicador, indicadorVO.idIndicador, false, false);
					}
				}
		});
		
		UnidadeBusinessAjax.obtemUnidades(
			function (unidades) {
				if (unidades != null) {
					var options1 = $('#idUnidadeAtingimentoC').attr('options');
					var options2 = $('#idUnidadeRealizadoC').attr('options');
					for (var i=0; i<unidades.length; i++) {
						var unidadeVO = unidades[i];
						options1[options1.length] = new Option(unidadeVO.descricaoUnidade, unidadeVO.idUnidade, false, false);
						options2[options2.length] = new Option(unidadeVO.descricaoUnidade, unidadeVO.idUnidade, false, false);
					}
				}
		});				
		
		
		$("#detCargo").validate({
			rules: {
	    		idMesC:		      			{required:true},
	    		idAnoC:		      			{required:true},
	    		idIndicadorC:    			{required:true},
	    		idUnidadeAtingimentoC:    	{required:true},
	    		atingimentoC:    			{required:true}
	     	},
	     	messages: {
	     		idMesC:   	    			{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     		idAnoC:   	    			{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	idIndicadorC:     			{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	idUnidadeAtingimentoC:     	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	atingimentoC:     			{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	        },
	        submitHandler : function(form){
		    	$("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detCargo.action = "cargoBonus.do?operacao=processaBonus";   
	            document.detCargo.submit();
	        }
    	});
    	
    	$("#cargoDIV").html(idCargo + " - " + descricaoCargo);
    	$("#idCargoC").val(idCargo);
		$("#idCargoFiltro").val(idCargoF);
		$("#descricaoFiltro").val(descricaoF);
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 770px;">
	<form name="detCargo" id="detCargo" method="post"> 
			
		<input type='hidden' id="idCargoC"      	 name="idCargoC" 	   value="">
		<input type='hidden' id="idCargoFiltro"      name="idCargoF"       value=""/>
		<input type='hidden' id="descricaoFiltro"    name="descricaoF"     value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">Cargo:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" id="cargoDIV" >
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">M&ecirc;s:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idMesC" name="idMesC" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Ano:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idAnoC" name="idAnoC" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Indicador:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idIndicadorC" name="idIndicadorC" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Unidade Atingimento:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idUnidadeAtingimentoC" name="idUnidadeAtingimentoC" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Atingimento:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="atingimentoC" name="atingimentoC" value="" size="12" maxlength="14" />
					</td>	
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Unidade Realizado:</label> 
					</td>
					<td class="componente" >
						<select id="idUnidadeRealizadoC" name="idUnidadeRealizadoC" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Realizado:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="realizadoC" name="realizadoC" maxlength="22" size="15" style="width:125px;" />
					</td>	
				</tr>					
			</tbody>
		</table>
		<BR>
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