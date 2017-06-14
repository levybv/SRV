<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.alphanumeric.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.hotkeys-0.7.9.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskedinput-1.2.2.js" charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskMoney.js" charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.validate.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.tipsy.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="js/lib/jquery.hoverIntent.js"></script>

<script type='text/javascript' src='srvdwr/interface/TelemarketingBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/DataBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>

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

		$("#detMeta").validate({
	    	 rules: {	periodoI: {required:true},
	    		 		elegiveisSelecionadosI: {validaElegiveisSelecionados:true}},
		     messages: {	periodoI: {required:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 			elegiveisSelecionadosI: {validaElegiveisSelecionados:'   <span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}},
	         submitHandler : function(form){
	        	var validado = true;
				$("input[id*='indicMeta_']").each(function() {
					if ($(this).val() == '') {
						alert('Os valores de "Meta" devem ser informados para todos indicadores!');
						validado = false;
						$(this).focus();
						return false;
					}
					
				});
				if (validado) {
					$("input[id*='indicRealz_']").each(function() {
						if ($(this).val() == '') {
							alert('Os valores de "Realizado" devem ser informados para todos indicadores!');
							validado = false;
							$(this).focus();
							return false;
						}
					});
				}
				if (validado) {
		         	for (i = 0; i < document.getElementById('elegiveisSelecionadosI').options.length ; i++){
						document.getElementById('elegiveisSelecionadosI').options[i].selected = true;
					}
			        $("#div-funcao-barra").hide();
		           	$("#div-load-barra").show();
			       	document.detMeta.action = "telemarketing.do?operacao=incluiTelemarketing";
		            document.detMeta.submit();
				}
	         }
    	});

		DataBusinessAjax.listaUltimosPeriodosMesAno(-6,
			function (periodoReturn) {
				if (periodoReturn != null) {
					var options = $('#periodoI').attr('options');
					for (var i=0; i<periodoReturn.length; i++) {
						var periodoItem = periodoReturn[i];
						options[options.length] = new Option(periodoItem.periodoDesc, periodoItem.periodo, false, false);
					}
				}
		});

		TelemarketingBusinessAjax.obtemListaFuncionariosDisponiveis(
			function (retorno) {
				if (retorno != null) {
					var options = $('#listaElegivelI').attr('options');
					for (var i=0; i<retorno.length; i++) {
						var item = retorno[i];
						options[options.length] = new Option(item.idFuncionario + ' - ' + item.nomeFuncionario, item.idFuncionario, false, false);
					}
				}
		});

		TelemarketingBusinessAjax.obtemListaIndicadoresTlmkt(function (listaIndicTlmkt) {
			for (var i=0; i<listaIndicTlmkt.length; i++) {
				var item = listaIndicTlmkt[i];
				$('#tableTlmkt tr:last').after('<tr><td class="componente" width="30%"><label class="label">'+ item.idIndicador + ' - ' + item.descricaoIndicador +':</label></td><td class="componente"><b>Meta:&nbsp;<input type="text" class="campo2" id="indicMeta_'+item.idIndicador+'" name="indicMeta_'+item.idIndicador+'" value="" size="13" maxlength="10" />&nbsp;&nbsp;&nbsp;Realizado:&nbsp;<input type="text" class="campo2" id="indicRealz_'+item.idIndicador+'" name="indicRealz_'+item.idIndicador+'" value="" size="13" maxlength="10" /></b></td></tr>');
			}
			$("input[id*='indicMeta_']").maskMoney({decimal:",", thousands:".", allowNegative:false});
			$("input[id*='indicMeta_']").bind('cut paste copy', function(){return false;});
			$("input[id*='indicRealz_']").maskMoney({decimal:",", thousands:".", allowNegative:false});
			$("input[id*='indicRealz_']").bind('cut paste copy', function(){return false;});
		});

		jQuery.validator.addMethod('validaElegiveisSelecionados', function(value, element, params) {
			var validacao = (document.getElementById('elegiveisSelecionadosI').options.length > 0);
			return validacao;
		});

		$("#periodoI").focus();

    });

	/* Funcoes auxiliares */
	function adicionaElegivel(elegivelSelecionado){
		if(elegivelSelecionado != ''){
			var descricao = obtemElegivelSelecionado();
			var posicao = obtemPosicaoElegivelSelecionado(document.getElementById("listaElegivelI"));
			document.getElementById("elegiveisSelecionadosI").options[document.getElementById("elegiveisSelecionadosI").options.length] = new Option(descricao,document.getElementById("listaElegivelI").value);
			document.getElementById("listaElegivelI").options[posicao] = null;
		}
	}

	function obtemElegivelSelecionado(){
		return document.getElementById("listaElegivelI").options[obtemPosicaoElegivelSelecionado(document.getElementById("listaElegivelI"))].text;
	}

	function obtemPosicaoElegivelSelecionado(selectBox){
		for(i=0; i<selectBox.length; i++) {
			if(selectBox.options[i].selected)  {
				return i;
			}
		}
	}

	function removeSelecionados(listBox) {
		var tam = listBox.length;
		for (i = 0; i < tam; i++) {
			for (j = 0; j < listBox.length; j++) {
				if (listBox.options[j].selected) {
					listBox.options[j] = null;
					break;
				}
			}
		}
		refreshListaElegivel();
	}

	function refreshListaElegivel() {
		document.getElementById("listaElegivelI").options.length = 0;
		document.getElementById("listaElegivelI").options[document.getElementById("listaElegivelI").options.length] = new Option('[SELECIONE]', '');
		var listJaSelecionados = obtemElegiveisJaSelecionados(document.getElementById("elegiveisSelecionadosI"));
		TelemarketingBusinessAjax.obtemListaFuncionariosDisponiveis(function (retorno) {
			if (retorno != null) {
				var options = $('#listaElegivelI').attr('options');
				for (var i=0; i<retorno.length; i++) {
					var item = retorno[i];
					var descrItem = item.idFuncionario + ' - ' + item.nomeFuncionario;
					if (!isJaSelecionou(listJaSelecionados, descrItem)) {
						document.getElementById("listaElegivelI").options[document.getElementById("listaElegivelI").options.length] = new Option(descrItem, item.idFuncionario);
					}
				}
			}
		});

	}

	function obtemElegiveisJaSelecionados(obj) {
		var listElegiveis = '';
		for (i = 0; i < obj.length; i++) {
			listElegiveis += obj.options[i].text + ';';
		}
		return listElegiveis;
	}

	function isJaSelecionou(lista, param1) {
		if (lista.length > 0) {
			var listaTmp = lista.split(';');
			for(j=0;j<listaTmp.length;j++){
				var item = listaTmp[j];
				if(param1 == item){
					return true;
				}
			}
		}
		return false;
	}

</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 725px">
	<form name="detMeta" id="detMeta" method="post"> 

		<table class="tabelaComponente" id="tableTlmkt">
			<tbody> 
				<tr>
					<td class="componente" width="30%">
						<label class="label">Per&iacute;odo:</label><span class="requerido">*</span>
					</td>
					<td class="componente" >
						<select id="periodoI" name="periodoI" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>				
				<tr>
					<td class="componente" width="30%">
						<label class="label">Lista de Eleg&iacute;veis:</label>
					</td>
					<td class="componente" >
						<select id="listaElegivelI" name="listaElegivelI" class="campo">
							<option value="" selected>[SELECIONE]</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" colspan="2">
						<label class="label">Eleg&iacute;veis selecionados para o per&iacute;odo:</label><span class="requerido">*</span><br>
						<select id="elegiveisSelecionadosI" name="elegiveisSelecionadosI" size="8" style="width:500px" class="campo2" multiple="multiple">
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" colspan="2">
						<div id="botao-geral" style="width:100%;">
							<div class="div-botao" style="text-align:left;float:left;width:18%;">
								<input id="botaoAdicionar" type="button" class="button" value="+" onclick="adicionaElegivel(document.getElementById('listaElegivelI').value);" />
							</div>
							<div class="div-botao" style="text-align:left;float:left;width:18%;">
								<input id="botaoRemover" type="button" class="button" value="-" onclick="removeSelecionados(document.getElementById('elegiveisSelecionadosI'));"/>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<td class="componente" colspan="2">
						</br>
						<font color='blue'><u><b>Indicadores (Meta / Realizado)<b></u></font><span class="requerido">*</span>
					</td>
				</tr>
				<div id="indicadoresTlmkt"></div>
			</tbody>
		</table>
		<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div id="div-funcao-barra" class="div-botao-modal" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Incluir" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
				<div id="div-load-barra" style="display: none;">
					<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
				</div>
			</div>
		</div>
	</form>
</div>