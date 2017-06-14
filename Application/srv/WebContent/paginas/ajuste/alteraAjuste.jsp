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
			if ( $("#formAlteraAjuste").valid() ) {
				$("#idFuncionarioA").val($("#idFuncionario").val());
	           	document.formAlteraAjuste.action = "ajusteFuncionario.do?operacao=alteraAjuste";   
	            document.formAlteraAjuste.submit();
			}
		});

		IndicadorBusinessAjax.obtemListaIndicadoresAjuste( 
				function (indicadores) {
					if (indicadores != null) {
						var options = $('#indicadorAT').attr('options');
						for (var i=0; i<indicadores.length; i++) {
							var indicadorVO = indicadores[i];
							options[options.length] = new Option(indicadorVO.descricaoIndicador, indicadorVO.codIndicador, false, false);
						}
						$("#indicadorA").val(idIndicadorO);
						$("#indicadorAT").val(idIndicadorO);
					}
		});

		CalendarioComercialBusinessAjax.obtemCalendarioComercialListaMes( 
			function (listaMes) {
				if (listaMes != null) {
					var options = $('#mesAT').attr('options');
					for (var i=0; i<listaMes.length; i++) {
						var mesVO = listaMes[i];
						options[options.length] = new Option(mesVO.mesStr, mesVO.mes, false, false);
					}
					$("#mesAT").val($("#idMesA").val());
				}
		});

		CalendarioComercialBusinessAjax.obtemCalendarioComercialListaAno( 
			function (listaAno) {
				if (listaAno != null) {
					var options = $('#anoAT').attr('options');
					for (var i=0; i<listaAno.length; i++) {
						var anoVO = listaAno[i];
						options[options.length] = new Option(anoVO.ano, anoVO.ano, false, false);
					}
					$("#anoAT").val($("#idAnoA").val());
				}
		});

		$("#formAlteraAjuste").validate({
	    	rules: {
		    		mesAT:	   	{required:true},
		    		anoAT:	   	{required:true},
		    		indicadorA:	{required:true},
		    		valorA:   	{required:true}
			},
	     	messages: {
	     			mesAT:  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     			anoAT:  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     		indicadorA: {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     		valorA:  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
			},
	        submitHandler : function(form) {
	        	$("#div-botao").hide();
			}
		});

		$("#valorA").val(valorO);
		$("#valorA").maskMoney({decimal:",", thousands:".", allowNegative:true});
		
		$("#idFilialA").val(idFilialO);
		$("#idEmpresaA").val(idEmpresaO);
		$("#idAnoA").val(idAnoO);
		$("#idMesA").val(idMesO);
		
		

	});

</script>

<div id="corpoModalAlteraAjusteFuncionario" style="float:left;margin-left: 10px; width: 530px">
	<form name="formAlteraAjuste" id="formAlteraAjuste" method="post"> 
		<input type="hidden" id="idFuncionarioA" name="idFuncionarioA" value=""/>
		<input type="hidden" id="periodoA" name="periodoA" value=""/>
		<input type="hidden" id="indicadorA" name="indicadorA" value=""/>
		<input type="hidden" id="idFilialA" name="idFilialA" value=""/>
		<input type="hidden" id="idEmpresaA" name="idEmpresaA" value=""/>
		<input type="hidden" id="idAnoA" name="idAnoA" value=""/>
		<input type="hidden" id="idMesA" name="idMesA" value=""/>
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">M&ecirc;s:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="mesAT" name="mesAT" class="campo" disabled>
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>			
				<tr>
					<td class="componente" width="20%">
						<label class="label">Ano:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="anoAT" name="anoAT" class="campo" disabled>
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>			
				<tr>
					<td class="componente" >
						<label class="label">Indicador:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="indicadorAT" name="indicadorAT" class="campo" disabled>
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>			
				<tr>
					<td class="componente" >
						<label class="label">Valor:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<input type="text" class="campo2" id="valorA" name="valorA" value="" maxlength="13" size="17"  />
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