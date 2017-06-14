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


<script type="text/javascript" src="js/lib/jquery.limit-1.2.source.js"  charset="UTF8" ></script>

<script type="text/javascript" src="js/ns/neuro.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.validate.1.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>

<script type="text/javascript">

	$(document).ready(function() {
		$("#labelSubindicador").hide();
		$("#botaoSalvar").hide();
		$("#pesoID").hide();
		$("#labelPeso").hide();
		$("#idUnidadeMetaID").hide();
		$("#labelUnidadeMeta").hide();
		$("#metaID").hide();
		$("#labelMeta").hide();

		$(document).bind('keydown', 'esc', function () {
			fecharModal();
		});

		$("#botaoCancelar").click(function(){
			fecharModal();
		});

		//Formatação dos campos
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

		$("#detIndicador").validate({
	    	  rules: {
	    			//idUnidadePesoID: {required:true},
	    			pesoID: {required:true},
	    			idUnidadeMetaID: {required:true},
	    			metaID: {required:true}
		     },
	     	 messages: {
	     		//idUnidadePesoID: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     		pesoID: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     		idUnidadeMetaID: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     		metaID: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	         },
	         submitHandler : function(form){
	 			if (validaPeso() && validaMeta()) {
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detIndicador.action = "bonusAnualFuncionario.do?operacao=alteraIndicadorFuncionario";   
		            document.detIndicador.submit();
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
    	
		IndicadorBusinessAjax.obtemRealizadoIndicadorBonus($('input[type=radio]:checked').val(),
			function (indicadorRealizado) {
				if (indicadorRealizado != null) {
					$("#divIndicador").html(indicadorRealizado.idIndicador + " - " + indicadorRealizado.descricaoIndicador);
					$("#idIndicadorID").val(indicadorRealizado.idIndicador);
					//$("#idUnidadePesoID").val(indicadorRealizado.unidadePeso);
					$("#idEmpresaID").val(indicadorRealizado.idEmpresa);
					$("#idFilialID").val(indicadorRealizado.idFilial);
					if (indicadorRealizado.idIndicadorPai > 0) {
						$("#labelSubindicador").show();
						$("#botaoSalvar").hide();
						$("#pesoID").hide();
						$("#labelPeso").hide();
						$("#idUnidadeMetaID").hide();
						$("#labelUnidadeMeta").hide();
						$("#metaID").hide();
						$("#labelMeta").hide();
					} else {
						$("#labelSubindicador").hide();
						$("#botaoSalvar").show();
						$("#pesoID").show();
						$("#labelPeso").show();
						$("#idUnidadeMetaID").show();
						$("#labelUnidadeMeta").show();
						$("#metaID").show();
						$("#labelMeta").show();
						$("#pesoID").val(indicadorRealizado.pesoFormatado.replace('%','').replace('R$ ',''));
						$("#idUnidadeMetaID").val(indicadorRealizado.unidadeMeta);
						if (indicadorRealizado.unidadeMeta!=5) {
							$("#metaID").attr('disabled','');
							$("#metaID").val(indicadorRealizado.metaFormatada.replace('%','').replace('R$ ',''));
						} else {
							$("#metaID").attr('disabled','disabled');
						}
					}
				}
		});

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
		<input type='hidden' id="idIndicadorID"    		name="idIndicadorID"    		value=""/>
		<input type='hidden' id="idEmpresaID"    		name="idEmpresaID"    			value=""/>
		<input type='hidden' id="idFilialID"    		name="idFilialID"    			value=""/>
		
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
						<label class="label">Indicador:</label> 
					</td>
					<td class="componente" >
						<div id="divIndicador"></div>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label" id="labelPeso">Peso:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="pesoID" name="pesoID" value="" size="30" maxlength="24" onblur="verificaFlag();" onpaste="return onPaste();" />
					</td>	
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label" id="labelUnidadeMeta">Unidade Meta:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="idUnidadeMetaID" name="idUnidadeMetaID" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>									
				<tr>
					<td class="componente" >
						<label class="label" id="labelMeta">Meta:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="metaID" name="metaID" value="" size="22" maxlength="16" onpaste="return onPaste();" />
					</td>	
				</tr>
				<tr>
					<td class="componente" colspan="2">
						<label class="label" id="labelSubindicador"><span class="requerido">Subindicadores não possuem dados de peso/meta para serem alterados.</span></label> 
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