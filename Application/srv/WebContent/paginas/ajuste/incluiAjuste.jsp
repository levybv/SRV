<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.alphanumeric.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.hotkeys-0.7.9.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskedinput-1.2.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskMoney.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.validate.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.tipsy.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="js/lib/jquery.hoverIntent.js"></script>



<script type="text/javascript" src="js/ns/neuro.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.validate.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>

<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/CalendarioComercialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	

<script type="text/javascript">

	$(document).ready(function() {
		$(document).bind('keydown', 'esc', function () {
			fecharModal();
		});

		$("#botaoCancelar").click(function(){
			fecharModal();
		});

		$("#botaoSalvar").click(function(){
			if ( $("#formIncluiAjuste").valid() ) {
				$("#idFuncionarioI").val($("#idFuncionario").val());
				$("#anoF").val(anoF);
				$("#mesF").val(mesF);
	           	document.formIncluiAjuste.action = "ajusteFuncionario.do?operacao=incluiAjuste";   
	            document.formIncluiAjuste.submit();
			}
		});

		IndicadorBusinessAjax.obtemListaIndicadoresAjuste( 
				function (indicadores) {
					if (indicadores != null) {
						var options = $('#indicadorI').attr('options');
						for (var i=0; i<indicadores.length; i++) {
							var indicadorVO = indicadores[i];
							options[options.length] = new Option(indicadorVO.descricaoIndicador, indicadorVO.codIndicador, false, false);
						}
					}
		});

		CalendarioComercialBusinessAjax.obtemCalendarioComercialListaMes( 
			function (listaMes) {
				if (listaMes != null) {
					var options = $('#mesI').attr('options');
					for (var i=0; i<listaMes.length; i++) {
						var mesVO = listaMes[i];
						options[options.length] = new Option(mesVO.mesStr, mesVO.mes, false, false);
					}
					$("#mesI").val(mesF);
				}
		});

		CalendarioComercialBusinessAjax.obtemCalendarioComercialListaAno( 
			function (listaAno) {
				if (listaAno != null) {
					var options = $('#anoI').attr('options');
					for (var i=0; i<listaAno.length; i++) {
						var anoVO = listaAno[i];
						options[options.length] = new Option(anoVO.ano, anoVO.ano, false, false);
					}
					$("#anoI").val(anoF);
				}
		});

		$("#formIncluiAjuste").validate({
	    	rules: {
		    		mesI:		{required:true},
		    		anoI:		{required:true},
		    		indicadorI: {required:true},
		    		valorI:   	{required:true}
			},
	     	messages: {
		     		mesI:	  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     		anoI:	  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     		indicadorI: {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     		valorI:  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
			},
	        submitHandler : function(form) {
	        	$("#div-botao").hide();
			}
		});

		$("#valorI").maskMoney({decimal:",", thousands:".", allowNegative:true});

	});

</script>

<div id="corpoModalIncluiAjusteFuncionario" style="float:left;margin-left: 10px; width: 530px">
	<form name="formIncluiAjuste" id="formIncluiAjuste" method="post"> 
		<input type="hidden" id="idFuncionarioI" name="idFuncionarioI" value=""/>
		<input type="hidden" id="anoF" name="anoF" value=""/>
		<input type="hidden" id="mesF" name="mesF" value=""/>
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">M&ecirc;s:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="mesI" name="mesI" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>			
				<tr>
					<td class="componente" width="20%">
						<label class="label">Ano:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="anoI" name="anoI" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>			
				<tr>
					<td class="componente" >
						<label class="label">Indicador:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="indicadorI" name="indicadorI" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>			
				<tr>
					<td class="componente" >
						<label class="label">Valor:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<input type="text" class="campo2" id="valorI" name="valorI" value="" maxlength="13" size="17"  />
					</td>
				</tr>				
			</tbody>
		</table>
		<br/><br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="button" class="button" value="Salvar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
	</form>
</div>