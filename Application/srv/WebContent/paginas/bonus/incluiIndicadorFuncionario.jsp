<%@ page contentType="text/html;charset=UTF-8" language="java" %>

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
<script type="text/javascript" src="js/lib/jquery.maskMoney.js"></script>


<script type="text/javascript" src="js/lib/jquery.limit-1.2.source.js"  charset="UTF8" ></script>

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

		$("#pesoID").maskMoney({decimal:",", thousands:".", allowNegative:false, precision:2});
		$("#metaID").maskMoney({decimal:",", thousands:".", allowNegative:false, precision:2});
		$("#realizadoID").maskMoney({decimal:",", thousands:".", allowNegative:false, precision:2});

		UnidadeBusinessAjax.obtemUnidades(
			function (unidades) {
				dwr.engine.setAsync(false);
				if (unidades != null) {
					var options1 = $('#idUnidadeMetaID').attr('options');
					var options2 = $('#idUnidadeRealizadoID').attr('options');
					for (var i=0; i<unidades.length; i++) {
						var unidadeVO = unidades[i];
						options1[options1.length] = new Option(unidadeVO.descricaoUnidade + ' (' + unidadeVO.simbolo + ')', unidadeVO.idUnidade, false, false);
						options2[options2.length] = new Option(unidadeVO.descricaoUnidade + ' (' + unidadeVO.simbolo + ')', unidadeVO.idUnidade, false, false);
					}
				}
		});

		$("#detIndicador").validate({
	    	  rules: {
	    		  idIndicadorID: {required:true},
	    		  pesoID: {required:true},
	    		  idUnidadeMetaID: {required:true},
	    		  metaID: {required:true},
	    		  idGrpIndicadorID: {required:true},
	    		  idUnidadeRealizadoID:{required: function(){return $('#realizadoID').val() != '';}},
	    		  realizadoID:{required: function(){return $('#idUnidadeRealizadoID').val() != '';}}
		     },
	     	 messages: {
	     		  idIndicadorID: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	    		  pesoID: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	    		  idUnidadeMetaID: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	    		  metaID: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	    		  idGrpIndicadorID: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	    		  idUnidadeRealizadoID: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio se \'Realizado\' preenchido.</span>'},
	    		  realizadoID: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio se \'Unidade Realizado\' preenchido.</span>'}
	         },
	         submitHandler : function(form){
	            $("#div-botao-tela").hide();  
           	    $("#div-load-tela").show(); 
		       	document.detIndicador.action = "bonus.do?operacao=incluiIndicadorFuncionario";   
	            document.detIndicador.submit();
	         }
    	});
    	
		//Dados da tela atual
		$("#periodoFiltroM").val($("#periodos").val());
		$("#idFuncionarioSelecionadoM").val($("#idFuncionarioSelecionado").val());
		$("#nomeFuncionarioSelecionadoM").val($("#nomeFuncionarioSelecionado").val());
		$("#cargoFuncionarioSelecionadoM").val($("#cargoFuncionarioSelecionado").val());
		$("#centroCustoFuncionarioSelecionadoM").val($("#centroCustoFuncionarioSelecionado").val());		
		$("#idFilialSelecionadaM").val($("#idFilialSelecionada").val());

		//Dados da tela anterior
		$("#idFuncionarioM").val($("#idFuncionario").val());
		$("#nomeFuncionarioM").val($("#nomeFuncionario").val());
		$("#crachaM").val($("#cracha").val());
		$("#cpfFuncionarioM").val($("#cpfFuncionario").val());		
		
		//Dados para efetivacao
		$("#anoID").val($("#periodos").val().substring(0,4));
		$("#mesID").val($("#periodos").val().substring(4,$("#periodos").val().length));
		
		$("#divMesAno").html($("#periodos option:selected").text());
		$("#idFuncionarioID").val($("#idFuncionarioSelecionado").val());
		$("#divFuncionario").html($("#idFuncionarioSelecionado").val() + " - " + $("#nomeFuncionarioSelecionado").val());

		$("#idGrpIndicadorID").change(function(){
			$('#idIndicadorID').empty();
			var options = $('#idIndicadorID').attr('options');
			options[0] = new Option('SELECIONE...', '', false, false);
			if ($("#idGrpIndicadorID").val() != "") {
				$("#idIndicadorID").attr('disabled','');
				IndicadorBusinessAjax.obtemListaIndicadoresAtivosPorPeriodo($("#periodos").val().substring(0,4), $("#idGrpIndicadorID").val(), 1,
					function (indicadores) {
						if (indicadores != null) {
							var options = $('#idIndicadorID').attr('options');
							for (var i=0; i<indicadores.length; i++) {
								var indicadorVO = indicadores[i];
								options[options.length] = new Option(indicadorVO.idIndicador + " - " + indicadorVO.descricaoIndicador, indicadorVO.idIndicador, false, false);
							}
						}
				});
			}
			$("#idIndicadorID").val("");
		});

    });
	
	function onPaste(){return false;}

</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 770px">
	<form name="detIndicador" id="detIndicador" method="post"> 
		
		<!--Dados da tela atual-->
		<input type='hidden' id="periodoFiltroM"  					 name="periodos"   							value=""/>
		<input type='hidden' id="idFuncionarioSelecionadoM"    		 name="idFuncionarioSelecionado"     		value=""/>
		<input type='hidden' id="nomeFuncionarioSelecionadoM"    	 name="nomeFuncionarioSelecionado"    		value=""/>
		<input type='hidden' id="cargoFuncionarioSelecionadoM"    	 name="cargoFuncionarioSelecionado"    		value=""/>
		<input type='hidden' id="centroCustoFuncionarioSelecionadoM" name="centroCustoFuncionarioSelecionado" 	value=""/>
		<input type='hidden' id="idFilialSelecionadaM"				 name="idFilialSelecionada"					value=""/>

		<!--Dados da tela anterior-->
		<input type='hidden' id="idFuncionarioM"    	name="idFuncionario"     		value=""/>
		<input type='hidden' id="nomeFuncionarioM"   	name="nomeFuncionario"    		value=""/>
		<input type='hidden' id="crachaM"    			name="cracha"    				value=""/>
		<input type='hidden' id="cpfFuncionarioM"  		name="cpfFuncionario" 			value=""/>
		
		<!--Dados para efetivacao-->		
		<input type='hidden' id="idFuncionarioID"       name="idFuncionarioID"     		value=""/>
		<input type='hidden' id="anoID"    				name="anoID"     				value=""/>
		<input type='hidden' id="mesID"   				name="mesID"     				value=""/>
					
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">Mês/Ano:</label> 
					</td>
					<td class="componente" >
  						<div id="divMesAno"></div>
					</td>	
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Funcionário:</label> 
					</td>
					<td class="componente" >
  						<div id="divFuncionario"></div>
					</td>	
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Grupo Indicador:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idGrpIndicadorID" name="idGrpIndicadorID" class="campo">
							<option value="">Selecione...</option>
							<option value="1" >CORPORATIVO</option>
							<option value="7" >INDIVIDUAL</option>
						</select>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Indicador:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idIndicadorID" name="idIndicadorID" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Peso:</label><span class="requerido">*</span>
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="pesoID" name="pesoID" value="" size="10" maxlength="6" onpaste="return onPaste();" /> %
					</td>	
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Unidade Meta:</label><span class="requerido">*</span>
					</td>
					<td class="componente" >
						<select id="idUnidadeMetaID" name="idUnidadeMetaID" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Meta:</label><span class="requerido">*</span>
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="metaID" name="metaID" value="" size="25" maxlength="20" onpaste="return onPaste();" /> 
					</td>	
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Unidade Realizado:</label> 
					</td>
					<td class="componente" >
						<select id="idUnidadeRealizadoID" name="idUnidadeRealizadoID" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label">Realizado:</label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="realizadoID" name="realizadoID" value="" size="25" maxlength="20" onpaste="return onPaste();" />
					</td>	
				</tr>				
			</tbody>
		</table>
		<br/>
		<div id="botao-geral">
			<div id="botao" class="barBottom"> 
				<div id="div-botao-tela">
					<input id="botaoSalvar" type="submit" class="button" value="Salvar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
				<div id="div-load-tela" style="display: none;">
					<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
				</div>
			</div>
		</div>
	</form>
</div>