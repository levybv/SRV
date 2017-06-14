<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/CalendarioComercialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/FilialBusinessAjax.js'></script>
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
		$(document).bind('keydown', 'esc', function () {
			fecharModal();
		});
		
		$("#botaoCancelar").click(function(){
			fecharModal();
		});

		CalendarioComercialBusinessAjax.obtemCalendarioComercialListaMes( 
			function (meses) {
				if (meses != null) {
					var options = $('#idMesMF').attr('options');
					for (var i=0; i<meses.length; i++) {
						var periodoVO = meses[i];
						options[options.length] = new Option(periodoVO.mesStr, periodoVO.mes, false, false);
					}
				}
		});

		CalendarioComercialBusinessAjax.obtemCalendarioComercialListaAno( 
			function (anos) {
				if (anos != null) {
					var options = $('#idAnoMF').attr('options');
					for (var i=0; i<anos.length; i++) {
						var periodoVO = anos[i];
						options[options.length] = new Option(periodoVO.ano, periodoVO.ano, false, false);
					}
				}
		});

		IndicadorBusinessAjax.obtemListaIndicadores( 
			function (indicadores) {
				if (indicadores != null) {
					var options = $('#idIndicadorMF').attr('options');
					for (var i=0; i<indicadores.length; i++) {
						var indicadorVO = indicadores[i];
						options[options.length] = new Option(indicadorVO.descricaoIndicador, indicadorVO.codIndicador, false, false);
					}
				}
		});
		
		FilialBusinessAjax.obtemTodasFiliaisAtivas( 
			function (filiais) {
				if (filiais != null) {
					var options = $('#idFilialMF').attr('options');
					for (var i=0; i<filiais.length; i++) {
						var filialVO = filiais[i];
						options[options.length] = new Option(filialVO.descricao, filialVO.codFilial, false, false);
					}
				}
		});		
		
		UnidadeBusinessAjax.obtemUnidades(
			function (unidades) {
				if (unidades != null) {
					var options = $('#idUnidadeMetaMF').attr('options');
					for (var i=0; i<unidades.length; i++) {
						var unidadeVO = unidades[i];
						options[options.length] = new Option(unidadeVO.descricaoUnidade, unidadeVO.idUnidade, false, false);
					}
				}
		});		
		
		$("#valorMetaMF").keypress(function(){
			return mascara(this, moedaSemRS);
		});	
		$("#valorPremioFilialMF").keypress(function(){
			return mascara(this, moedaSemRS);
		});			
		
		$("#detMeta").validate({
	    	rules: {
	    			idMesMF:	   			{required:true},
	    			idAnoMF:	   			{required:true},
	    			idIndicadorMF:     		{required:true},
	    			idFilialMF: 			{required:true},
	    			idUnidadeMetaMF: 		{required:true},
	    			valorMetaMF: 			{required:true},
	    			valorPremioFilialMF: 	{required:true}
		     	},
		     	messages: {
			     	idMesMF:	    		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idAnoMF:	    		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idIndicadorMF:      	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idFilialMF:  			{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idUnidadeMetaMF:  		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	valorMetaMF:  			{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	valorPremioFilialMF:  	{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
		         },
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "metaFilial.do?operacao=incluiMetaFilial";   
	            document.detMeta.submit();
	         }
    	});
    	
		$("#mesFiltro").val(mesF);
		$("#anoFiltro").val(anoF);
		$("#idIndicadorFiltro").val(idIndicadorF);
		$("#descricaoIndicadorFiltro").val(descricaoIndicadorF);
		$("#idFilialFiltro").val(idFilialF);
		    	
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 770px">
	<form name="detMeta" id="detMeta" method="post"> 
			
		<!--Filtro-->
		<input type='hidden' id="mesFiltro"  				name="mesF"   					value=""/>
		<input type='hidden' id="anoFiltro"    				name="anoF"     				value=""/>
		<input type='hidden' id="idIndicadorFiltro"    		name="idIndicadorF"     		value=""/>
		<input type='hidden' id="descricaoIndicadorFiltro"  name="descricaoIndicadorF"     	value=""/>
		<input type='hidden' id="idFilialFiltro"    		name="idFilialF"     			value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" >
						<label class="label">M&ecirc;s:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idMesMF" name="idMesMF" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Ano:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idAnoMF" name="idAnoMF" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Indicador:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idIndicadorMF" name="idIndicadorMF" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Filial:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idFilialMF" name="idFilialMF" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Unidade Meta:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idUnidadeMetaMF" name="idUnidadeMetaMF" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Meta:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="valorMetaMF" name="valorMetaMF" value="" maxlength="14" size="15"  />
					</td>
				</tr>					
				<tr>
					<td class="componente" >
						<label class="label">Prêmio:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="valorPremioFilialMF" name="valorPremioFilialMF" value="" maxlength="14" size="15"  />
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