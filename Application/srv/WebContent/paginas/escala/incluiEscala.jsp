<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/GrupoIndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/GrupoRemuneracaoVariavelBusinessAjax.js'></script>
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

		IndicadorBusinessAjax.obtemListaIndicadoresLoja( 
			function (indicadores) {
				if (indicadores != null) {
					var options = $('#idIndicadorES').attr('options');
					for (var i=0; i<indicadores.length; i++) {
						var indicadorVO = indicadores[i];
									options[options.length] = new Option(indicadorVO.descricaoIndicador, indicadorVO.idIndicador, false, false);
					}
								
								
				}
		});
	
		GrupoIndicadorBusinessAjax.obtemGruposIndicadores(
			function (grupos) {
				if (grupos != null) {
					var options = $('#idGrupoIndicadorES').attr('options');
					for (var i=0; i<grupos.length; i++) {
						var grupoVO = grupos[i];
						options[options.length] = new Option(grupoVO.descricaoGrupoIndicador, grupoVO.idGrupoIndicador, false, false);
					}
				}
		});	
		
		GrupoRemuneracaoVariavelBusinessAjax.obtemGruposRemuneracao(
			function (grupos) {
				if (grupos != null) {
					var options = $('#idGrupoRemVarES').attr('options');
					for (var i=0; i<grupos.length; i++) {
						var grupoVO = grupos[i];
						options[options.length] = new Option(grupoVO.descricaoOnline, grupoVO.idGrupoRemuneracao, false, false);
					}
				}
		});	
		
		$("#limiteES").keypress(function(){
			return mascara(this, moedaSemRS);
		});		
		
		$("#detMeta").validate({
	    	 rules: {
	    			descricaoES:   			{required:true},
	    			idIndicadorES: 			{required:true},
	    			idGrupoIndicadorES: 	{required:true},
	    			idGrupoRemVarES: 		{required:true}
		     },
		     messages: {
			     	descricaoES:    		{required:	   	'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
			     	idIndicadorES:  		{required:	    '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>'},
			     	idGrupoIndicadorES:  	{required:	    '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>'},
			     	idGrupoRemVarES:  		{required:	    '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>'}
		     },
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detMeta.action = "escala.do?operacao=incluiEscala";   
		            document.detMeta.submit();
	         }
    	});
    	
		$("#idEscalaFiltro").val(idEscalaF);
		$("#descricaoFiltro").val(descricaoF);
		    	
    });
    
    
    function selecionaAssociacao() {
    	var itemSelecionado = $("#detMeta input[type='radio']:checked").val();
    	
    	if (itemSelecionado == 1) {
    	
    		//$("#idIndicadorES").val("");
    		$("#idGrupoIndicadorES").val("");
    		$("#idGrupoRemVarES").val("");
    		
    		$("#idIndicadorES").attr("disabled", false); 
    		$("#idGrupoIndicadorES").attr("disabled", true); 
    		$("#idGrupoRemVarES").attr("disabled", true); 
    	
    	} else if (itemSelecionado == 2) {
    	
    		$("#idIndicadorES").val("");
    		//$("#idGrupoIndicadorES").val("");
    		$("#idGrupoRemVarES").val("");
    		
    		$("#idIndicadorES").attr("disabled", true); 
    		$("#idGrupoIndicadorES").attr("disabled", false); 
    		$("#idGrupoRemVarES").attr("disabled", true);     	
    	
    	} else if (itemSelecionado == 3) {
    	
    		$("#idIndicadorES").val("");
    		$("#idGrupoIndicadorES").val("");
    		//$("#idGrupoRemVarES").val("");
    		
    		$("#idIndicadorES").attr("disabled", true); 
    		$("#idGrupoIndicadorES").attr("disabled", true); 
    		$("#idGrupoRemVarES").attr("disabled", false);
    	}
    }
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 770px">
	<form name="detMeta" id="detMeta" method="post"> 
			
		<!--Filtro-->
		<input type='hidden' id="idEscalaFiltro"  	 name="idEscalaF"   value=""/>
		<input type='hidden' id="descricaoFiltro"    name="descricaoF"  value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">Descrição:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="descricaoES" name="descricaoES" value="" maxlength="100" size="60"  />
					</td>
				</tr>			
				<tr>
					<td class="componente" >
						<label class="label">Indicador:</label> 
					</td>
					<td class="componente" >
						<input type="radio" id="associacao" name="associacao" value="1" onClick="selecionaAssociacao()" checked/>
						<select id="idIndicadorES" name="idIndicadorES" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Grupo Indicador:</label> 
					</td>
					<td class="componente" >
						<input type="radio" id="associacao" name="associacao" value="2" onClick="selecionaAssociacao()"/>
						<select id="idGrupoIndicadorES" name="idGrupoIndicadorES" class="campo" disabled>
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Grupo Rem. Var.:</label> 
					</td>
					<td class="componente" >
						<input type="radio" id="associacao" name="associacao" value="3" onClick="selecionaAssociacao()"/>
						<select id="idGrupoRemVarES" name="idGrupoRemVarES" class="campo" disabled>
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Percentual 100%:</label> 
					</td>
					<td class="componente" >
						<input type="radio" id="percentualES" name="percentualES" value="S" />Sim
						<input type="radio" id="percentualES" name="percentualES" value="N" checked/>Não
					</td>
				</tr>					
				
				</tr>
									
				<tr>
					<td class="componente" >
						<label class="label">Limite:</label> 
					</td>
					<td class="componente" >
						<input type="text" class="campo2" id="limiteES" name="limiteES" value="" maxlength="8" size="10" />
					</td>
				</tr>
				
				<tr>
					<td class="componente" >
						<label class="label">Aplica Escala sobre Realizado:</label> 
					</td>
					<td class="componente" >
						<input type="radio" id="aplEscalaRelz" name="aplEscalaRelz" value="S"/>Sim
						<input type="radio" id="aplEscalaRelz" name="aplEscalaRelz" value="N" checked/>Não
					</td>
				</tr>
				
				<tr>
					<td class="componente" >
						<label class="label">Nº Escala:</label> 
					</td>
					<td class="componente" >
						<input type="text" class="campo2" id="nrEscalaES" name="nrEscalaES" value="" maxlength="6" size="10" />
					</td>
				
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