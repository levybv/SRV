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

		IndicadorBusinessAjax.obtemListaDiretoria(function (listaDiretoria) {
			if (listaDiretoria != null) {
				var options = $('#diretoriaID').attr('options');
				for (var i=0; i<listaDiretoria.length; i++) {
					options[options.length] = new Option(listaDiretoria[i], listaDiretoria[i], false, false);
				}
			}
    	});

		$("#diretoriaID").change(function(){
			if ($("#diretoriaID").val() == "") {
				$("#idIndicadorID").val("");
				$("#idIndicadorID").attr('disabled','disabled');
				$("#idSubIndicadorID").val("");
				$("#idSubIndicadorID").attr('disabled','disabled');
			} else {
				IndicadorBusinessAjax.obtemListaIndicadoresCorporativosBonusAnualDiretoria($("#diretoriaID").val(),
					function (indicadores) {
						$('#idIndicadorID').find('option').remove();
						var options = $('#idIndicadorID').attr('options');
						options[0] = new Option("Selecione...", "", false, false);
						if (indicadores != null) {
							for (var i=0; i<indicadores.length; i++) {
								var indicadorVO = indicadores[i];
								options[options.length] = new Option(indicadorVO.idIndicador + " - " + indicadorVO.descricaoIndicador, indicadorVO.idIndicador, false, false);
							}
						}
				});
				$("#idIndicadorID").attr('disabled','');
				$("#idIndicadorID").focus();
			}
		});	

		$("#idIndicadorID").change(function(){
			if ($("#idIndicadorID").val() == "") {
				$("#idSubIndicadorID").val("");
				$("#idSubIndicadorID").attr('disabled','disabled');
			} else {
				IndicadorBusinessAjax.obtemListaIndicadoresCorporativosBonusAnualSubindicador($("#idIndicadorID").val(),
					function (indicadores) {
						if (indicadores!=null && indicadores.length > 0) {
							$('#idSubIndicadorID').find('option').remove();
							var options = $('#idSubIndicadorID').attr('options');
							options[0] = new Option("Selecione...", "", false, false);
							if (indicadores != null) {
								for (var i=0; i<indicadores.length; i++) {
									var indicadorVO = indicadores[i];
									options[options.length] = new Option(indicadorVO.idIndicador + " - " + indicadorVO.descricaoIndicador, indicadorVO.idIndicador, false, false);
								}
								$("#idSubIndicadorID").attr('disabled','');
								$("#idSubIndicadorID").focus();
							}
						} else {
							$("#idSubIndicadorID").val("");
							$("#idSubIndicadorID").attr('disabled','disabled');
						}
				});
			}
		});	

		$("#idSubIndicadorID").change(function(){
			if ($("#idSubIndicadorID").val() != "") {
				$("#pesoID").val("");
				$("#idUnidadeMetaID").val("");
				$("#metaID").val("");
				$("#pesoID").attr('disabled','disabled');
				$("#idUnidadeMetaID").attr('disabled','disabled');
				$("#metaID").attr('disabled','disabled');
			} else {
				$("#pesoID").val("");
				$("#idUnidadeMetaID").val("");
				$("#metaID").val("");
				$("#pesoID").attr('disabled','');
				$("#idUnidadeMetaID").attr('disabled','');
				$("#metaID").attr('disabled','');
			}
		});	

		UnidadeBusinessAjax.obtemUnidades(
			function (unidades) {
				if (unidades != null) {
					//var options1 = $('#idUnidadePesoID').attr('options');
					var options2 = $('#idUnidadeMetaID').attr('options');
					for (var i=0; i<unidades.length; i++) {
						var unidadeVO = unidades[i];
						//options1[options1.length] = new Option(unidadeVO.descricaoUnidade, unidadeVO.idUnidade, false, false);
						options2[options2.length] = new Option(unidadeVO.descricaoUnidade, unidadeVO.idUnidade, false, false);
					}
				}
		});			

		$("#pesoID").keypress(function(event){
	 		var conteudoAceito = '-0123456789,';
			var tecla = String.fromCharCode(event.keyCode);
			if (conteudoAceito.indexOf(tecla) == -1) {
				return false;
			} else {
				return true;
			}
		});
		$("#metaID").keypress(function(event){
	 		var conteudoAceito = '-0123456789,';
			var tecla = String.fromCharCode(event.keyCode);
			if (conteudoAceito.indexOf(tecla) == -1) {
				return false;
			} else {
				return true;
			}
		});

		$("#detIndicador").validate({
	    	  rules: {
	    			idIndicadorID: {required:true},
	    			pesoID: {required:true},
	    			idUnidadeMetaID: {required:true},
	    			metaID: {required:true}
		     },
	     	 messages: {
		     	idIndicadorID: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	pesoID: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	idUnidadeMetaID: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		     	metaID: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	         },
	         submitHandler : function(form){
 				if ($("#idSubIndicadorID").val() != "") {
			        $("#div-botao").hide();
		           	$("#div-load").show();
			       	document.detIndicador.action = "bonusAnualFuncionario.do?operacao=incluiIndicadorFuncionario";
		            document.detIndicador.submit();
 				} else {
 		 			if (validaPeso() && validaMeta()) {
 				        $("#div-botao").hide();
 			           	$("#div-load").show();
 				       	document.detIndicador.action = "bonusAnualFuncionario.do?operacao=incluiIndicadorFuncionario";
 			            document.detIndicador.submit();
 		 			}
 				}
	         }
    	});

		//Dados da tela atual
		$("#periodoFiltroM").val($("#periodoAtual").val());
		$("#idFuncionarioSelecionadoM").val($("#idFuncionarioSelecionado").val());
		$("#nomeFuncionarioSelecionadoM").val($("#nomeFuncionarioSelecionado").val());
		$("#cargoFuncionarioSelecionadoM").val($("#cargoFuncionarioSelecionado").val());
		$("#centroCustoFuncionarioSelecionadoM").val($("#centroCustoFuncionarioSelecionado").val());		

		//Dados da tela anterior
		$("#idFuncionarioM").val($("#idFuncionario").val());
		$("#nomeFuncionarioM").val($("#nomeFuncionario").val());
		$("#crachaM").val($("#cracha").val());
		$("#cpfFuncionarioM").val($("#cpfFuncionario").val());		

		$("#divMesAno").html($("#periodoAtual").val());
		$("#idFuncionarioID").val($("#idFuncionarioSelecionado").val());
		$("#divFuncionario").html($("#idFuncionarioSelecionado").val() + " - " + $("#nomeFuncionarioSelecionado").val());

		$("#idUnidadeMetaID").change(function(){
			if ($("#idUnidadeMetaID").val()==5) {
				$("#metaID").val("");
				$("#metaID").attr('disabled','disabled');
			} else {
				$("#metaID").attr('disabled','');
			}
		});

    });

	function onPaste(){return false;}

	function validaPeso(){
		var validaDecRegExp = new RegExp("^([-]?[0-9]{1,13}?)([,][0-9]{1,4})?$");
		if (!validaDecRegExp.test($("#pesoID").val())) {
			alert("Valor 'PESO' inválido! Utilize apenas números e vírgula.\nCasas decimais são opcionais.\n\nEx: 12345,1234");
			return false;
		}
		return true;
	}

	function validaMeta(){
		if ($("#idUnidadeMetaID").val() == 5) {
			$("#metaID").val("");
			return true;
		} else {
			var validaDecRegExp = new RegExp("^([-]?[0-9]{1,10}?)([,][0-9]{1,4})?$");
			if (!validaDecRegExp.test($("#metaID").val())) {
				alert("Valor 'META' inválido! Utilize apenas números e vírgula.\nCasas decimais são opcionais.\n\nEx: 12345,1234");
				return false;
			}
			return true;
		}
	}

</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 870px">
	<form name="detIndicador" id="detIndicador" method="post"> 
		
		<!--Dados da tela atual-->
		<input type='hidden' id="periodoFiltroM"  					 name="periodos"   							value=""/>
		<input type='hidden' id="idFuncionarioSelecionadoM"    		 name="idFuncionarioSelecionado"     		value=""/>
		<input type='hidden' id="nomeFuncionarioSelecionadoM"    	 name="nomeFuncionarioSelecionado"    		value=""/>
		<input type='hidden' id="cargoFuncionarioSelecionadoM"    	 name="cargoFuncionarioSelecionado"    		value=""/>
		<input type='hidden' id="centroCustoFuncionarioSelecionadoM" name="centroCustoFuncionarioSelecionado" 	value=""/>

		<!--Dados da tela anterior-->
		<input type='hidden' id="idFuncionarioM"    	name="idFuncionario"     		value=""/>
		<input type='hidden' id="nomeFuncionarioM"   	name="nomeFuncionario"    		value=""/>
		<input type='hidden' id="crachaM"    			name="cracha"    				value=""/>
		<input type='hidden' id="cpfFuncionarioM"  		name="cpfFuncionario" 			value=""/>
		
		<!--Dados para efetivacao-->		
		<input type='hidden' id="idFuncionarioID"       name="idFuncionarioID"     		value=""/>
					
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
						<label class="label">Indicador por Diretoria:</label> 
					</td>
					<td class="componente" >
						<select id="diretoriaID" name="diretoriaID" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Indicador:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idIndicadorID" name="idIndicadorID" class="campo" disabled="disabled">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">SubIndicador:</label> 
					</td>
					<td class="componente" >
						<select id="idSubIndicadorID" name="idSubIndicadorID" class="campo" disabled="disabled">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Peso (%):<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="pesoID" name="pesoID" value="" size="30" maxlength="24" onpaste="return onPaste();" />
					</td>
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Unidade Meta:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idUnidadeMetaID" name="idUnidadeMetaID" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Meta:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="metaID" name="metaID" value="" size="30" maxlength="24" onpaste="return onPaste();" />
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