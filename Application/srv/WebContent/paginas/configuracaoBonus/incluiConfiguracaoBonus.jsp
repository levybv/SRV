<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/CalendarioComercialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/ConfiguracaoBonusBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/CalendarioBonusBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/FuncionarioBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/EscalaBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/DataBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>

<script type="text/javascript" src="js/lib/jquery-1.4.2.js" charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.alphanumeric.js" charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.hotkeys-0.7.9.js" charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskedinput-1.2.2.js" charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.maskMoney.js" charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.validate.js" charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.tipsy.js" charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.limit-1.2.source.js" charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery.metadata.js"></script>
<script type="text/javascript" src="js/lib/jquery.hoverIntent.js"></script>

<script type="text/javascript" src="js/ns/neuro.1.js" charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.validate.1.js" charset="UTF8" ></script>
<script type="text/javascript" src="js/ns/neuro.modal-1.0.js" charset="UTF8" ></script>

<script type="text/javascript">

	$(document).ready(function() {

		dwr.engine.setAsync(false);

		$(document).bind('keydown', 'esc', function () {
			fecharModal();
		});

		$("#botaoCancelar").click(function(){
			fecharModal();
		});

		$('#codIndicIniI').numeric();
		$('#codIndicFimI').numeric();
		$("#fundingI").maskMoney({decimal:",", thousands:".", allowNegative:false});
		$('#dataLimiteAceiteI').mask('99/99/9999');
		$('#textoConsentimentoI').limit('500','#charsLeftConsentimento');

		$("#formIncluiConfiguracaoBonus").validate({
			rules: {
				anoBonusI: {required:true,
							isPermiteGravarAno:true},
				flgEncerradoI: {required:true},
				idFuncCorpI: {required:true},
				codIndicIniI: {	required:true,
								//isCodigoIndicadorJaUtilizado: true,
								isRangeIndicadoresJaExiste:true},
				codIndicFimI: {	required:true,
								isMaiorQueInicio: true,
								isRangeIndicadoresJaExiste: true},
				flgFundingI: {required:true},
				fundingI: {required:true},
				flgContratoMetaI: {required:true},
				dataLimiteAceiteI: {required:true,
									isDataInvalida:true},
				textoConsentimentoI: {required:true},
				escalasSelecionadasI: {validaEscalasSelecionadas:true}
			},
	     	messages: {
				anoBonusI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
							isPermiteGravarAno: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Ano j&aacute; possui par&acirc;metro cadastrado.</span>'},
				flgEncerradoI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
				idFuncCorpI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
				codIndicIniI: {	required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
								//isCodigoIndicadorJaUtilizado: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> C&oacute;digo de indicador j&aacute; est&aacute; sendo utilizado.</span>',
								isRangeIndicadoresJaExiste: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Range de indicadores já está sendo utilizado.</span>'},
				codIndicFimI: {	required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
								isMaiorQueInicio: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> C&oacute;digo final deve ser maior que c&oacute;digo in&iacute;cio.</span>',
								isRangeIndicadoresJaExiste: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Range de indicadores já está sendo utilizado.</span>'},
				flgFundingI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
				fundingI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
				flgContratoMetaI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
				dataLimiteAceiteI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
									isDataInvalida: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Data inv&aacute;lida.</span>'},
				textoConsentimentoI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
				escalasSelecionadasI: {validaEscalasSelecionadas: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	     	},
	        submitHandler : function(form){
	        	$("#div-funcao-barra").hide();
	           	$("#div-load-barra").show();
	         	for (i = 0; i < document.getElementById('escalasSelecionadasI').options.length ; i++){
					document.getElementById('escalasSelecionadasI').options[i].selected = true;
				}
		       	document.formIncluiConfiguracaoBonus.action = "configuracaoBonus.do?operacao=incluiConfiguracaoBonus";
	            document.formIncluiConfiguracaoBonus.submit();
	        }
    	});

		/* funcoes de carga */
		CalendarioBonusBusinessAjax.obtemAnosCalendarioBonus(
			function (listaAnos) {
				if (listaAnos != null) {
					var options = $('#anoBonusI').attr('options');
					for (var i=0; i<listaAnos.length; i++) {
						var numAnoBonus = listaAnos[i];
						options[options.length] = new Option(numAnoBonus, numAnoBonus, false, false);
					}
				}
		});

		EscalaBusinessAjax.obtemListaEscalaBonus(
			function (listaEscala) {
				if (listaEscala != null) {
					var options = $('#listaEscalasI').attr('options');
					for (var i=0; i<listaEscala.length; i++) {
						var itemEscala = listaEscala[i];
						var numEscala = itemEscala.numEscala!=null?' (NUM: ' + itemEscala.numEscala + ')':"";
						var descrEscala = itemEscala.idEscala + ' - ' + itemEscala.descricaoEscala + numEscala;
						options[options.length] = new Option(descrEscala, itemEscala.idEscala, false, false);
					}
				}
		});

        CalendarioComercialBusinessAjax.obtemListaPeriodoMesAno( 
   			function (periodo) {
   				if (periodo != null) {
   					var options = $('#periodoI').attr('options');
   					for (var i=0; i<periodo.length; i++) {
   						var periodoVO = periodo[i];
   						options[options.length] = new Option(periodoVO.periodoMesAno, periodoVO.mesAno, false, false);
   						
   					}
   				}
   		});

		/* funcoes de validacao */
		jQuery.validator.addMethod('isPermiteGravarAno', function(value, element, params) {
			var validacao = false;
			ConfiguracaoBonusBusinessAjax.isJaExisteConfiguracaoBonus(value, function (retorno) {
				validacao = retorno;
			});
			return !validacao;
		});

		jQuery.validator.addMethod('isCodigoIndicadorJaUtilizado', function(value, element, params) {
			var validacao = false;
			IndicadorBusinessAjax.isCodigoIndicadorJaUtilizado(value, function (retorno) {
				validacao = retorno;
			});
			return !validacao;
		});

		jQuery.validator.addMethod('isRangeIndicadoresJaExiste', function(value, element, params) {
			var validacao = false;
			ConfiguracaoBonusBusinessAjax.isRangeIndicadoresJaExiste(value, null, function (retorno) {
				validacao = retorno;
			});
			return !validacao;
		});

		jQuery.validator.addMethod('isMaiorQueInicio', function(value, element, params) {
			var validacao = false;
			if ($('#codIndicIniI').val() != "") {
				validacao = $('#codIndicIniI').val() >= $('#codIndicFimI').val();
			}
			return !validacao;
		});

		jQuery.validator.addMethod('validaEscalasSelecionadas', function(value, element, params) {
			var validacao = (document.getElementById('escalasSelecionadasI').options.length > 0);
			return validacao;
		});

		jQuery.validator.addMethod('isDataInvalida', function(value, element, params) {
			var validacao = false;
			DataBusinessAjax.isDataInvalida(value, function (retorno) {
				validacao = retorno;
			});
			return !validacao;
		});

		$('#idFuncCorpI').blur(function () {
			FuncionarioBusinessAjax.obtemFuncionario($('#idFuncCorpI').val(), function (retorno) {
				if (retorno != null) {
					$('#idNomeFuncDIV').html(retorno.nomeFuncionario);
				} else {
					$('#idNomeFuncDIV').html("");
				}
			});
		});

		$('#flgFundingI').change(function(value, element, params) {
			if ($('#flgFundingI').val()=='N') {
				$('#fundingI').val("");
				$('#fundingI').attr("disabled", true);
				$('#reqFunding').hide();
			} else {
				$('#fundingI').val("");
				$('#fundingI').attr("disabled", false);
				$('#reqFunding').show();
			}
		});

		$('#flgContratoMetaI').change(function(value, element, params) {
			if ($('#flgContratoMetaI').val()=='N') {
				$('#dataLimiteAceiteI').val("");
				$('#dataLimiteAceiteI').attr("disabled", true);
				$('#textoConsentimentoI').val("");
				$('#textoConsentimentoI').attr("disabled", true);
				$('#reqDataLimiteAceite').hide();
				$('#reqTextoConsentimento').hide();
			} else {
				$('#dataLimiteAceiteI').val("");
				$('#dataLimiteAceiteI').attr("disabled", false);
				$('#textoConsentimentoI').val("");
				$('#textoConsentimentoI').attr("disabled", false);
				$('#reqDataLimiteAceite').show();
				$('#reqTextoConsentimento').show();
			}
		});

    });

	/* Funcoes auxiliares */
	function adicionaEscala(escalaSelecionada){
		if(escalaSelecionada != ''){
			var descricao = obtemEscalaSelecionada();
			var posicao = obtemPosicaoEscalaSelecionada(document.getElementById("listaEscalasI"));
			document.getElementById("escalasSelecionadasI").options[document.getElementById("escalasSelecionadasI").options.length] = new Option(descricao,document.getElementById("listaEscalasI").value);
			document.getElementById("listaEscalasI").options[posicao] = null;
		}
	}

	function obtemEscalaSelecionada(){
		return document.getElementById("listaEscalasI").options[obtemPosicaoEscalaSelecionada(document.getElementById("listaEscalasI"))].text;
	}

	function obtemPosicaoEscalaSelecionada(selectBox){
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
		refreshListaEscala();
	}

	function refreshListaEscala() {
		document.getElementById("listaEscalasI").options.length = 0;
		document.getElementById("listaEscalasI").options[document.getElementById("listaEscalasI").options.length] = new Option('[SELECIONE]', '');
		var listEscalasJaSelecionadas = obtemEscalasJaSelecionadas(document.getElementById("escalasSelecionadasI"));
		EscalaBusinessAjax.obtemListaEscalaBonus(function(listaEscalaRefresh) {
			for (i = 0; i < listaEscalaRefresh.length; i++) {
				var itemEscala = listaEscalaRefresh[i];
				var numEscala = itemEscala.numEscala != null ? ' (NUM: ' + itemEscala.numEscala + ')' : "";
				var descrEscala = itemEscala.idEscala + ' - ' + itemEscala.descricaoEscala + numEscala;
				if (!isJaSelecionou(listEscalasJaSelecionadas, descrEscala)) {
					document.getElementById("listaEscalasI").options[document.getElementById("listaEscalasI").options.length] = new Option(descrEscala, itemEscala.idEscala);
				}
			}
		});
	}

	function obtemEscalasJaSelecionadas(obj) {
		var listEscalas = '';
		for (i = 0; i < obj.length; i++) {
			listEscalas += obj.options[i].text + ';';
		}
		return listEscalas;
	}

	function isJaSelecionou(lista, paramEscala) {
		if (lista.length > 0) {
			var listaTmp = lista.split(';');
			for(j=0;j<listaTmp.length;j++){
				var item = listaTmp[j];
				if(paramEscala == item){
					return true;
				}
			}
		}
		return false;
	}
</script>

<div id="corpoModalIncluiConfiguracaoBonus" style="float:left;margin-left: 10px; width: 625px">

	<form name="formIncluiConfiguracaoBonus" id="formIncluiConfiguracaoBonus" method="post"> 
		</br>
		<table class="tabelaComponente">
			<tbody>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Ano (B&ocirc;nus):<span class="requerido">*</span></label>
					</td>
					<td class="componente"  width="70%">
						<select id="anoBonusI" name="anoBonusI" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Per&iacute;odo Encerrado:<span class="requerido">*</span></label>
					</td>
					<td class="componente" width="70%">
						<select id="flgEncerradoI" name="flgEncerradoI" class="campo">
							<option value="" selected>Selecione...</option>
							<option value="S">Sim</option>
							<option value="N">N&atilde;o</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">ID Presidente:<span class="requerido">*</span></label>
					</td>
					<td class="componente" width="70%">
						<input type="text" class="campo2" id="idFuncCorpI" name="idFuncCorpI" value="" size="14" maxlength="8" /> <label id="idNomeFuncDIV" class="label"></label>
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Range de Indicadores:<span class="requerido">*</span></label>
					</td>
					<td class="componente"  width="70%">
						<input type="text" class="campo2" id="codIndicIniI" name="codIndicIniI" value="" size="7" maxlength="5" />
						&nbsp;at&eacute;&nbsp;
						<input type="text" class="campo2" id="codIndicFimI" name="codIndicFimI" value="" size="7" maxlength="5" />
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Utiliza Funding:<span class="requerido">*</span></label>
					</td>
					<td class="componente" width="70%">
						<select id="flgFundingI" name="flgFundingI" class="campo">
							<option value="" selected>Selecione...</option>
							<option value="S">Sim</option>
							<option value="N">N&atilde;o</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Fator Multiplicador (Funding):<span class="requerido" id="reqFunding">*</span></label>
					</td>
					<td class="componente" width="70%">
						<input type="text" class="campo2" id="fundingI" name="fundingI" value="" size="8" maxlength="6" />
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Per&iacute;odo Dispon&iacute;vel:</label>
					</td>
					<td class="componente" width="70%">
						<select id="periodoI" name="periodoI" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" colspan="2">
						</br>
						<font color='blue'><u><b>Contrato de Metas<b></u></font>
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Usa Contrato Metas:<span class="requerido">*</span></label>
					</td>
					<td class="componente" width="70%">
						<select id="flgContratoMetaI" name="flgContratoMetaI" class="campo">
							<option value="" selected>Selecione...</option>
							<option value="S">Sim</option>
							<option value="N">N&atilde;o</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Data Limite Aceite:<span class="requerido" id="reqDataLimiteAceite">*</span></label>
					</td>
					<td class="componente"  width="70%">
						<input type="text" class="campo2" id="dataLimiteAceiteI" name="dataLimiteAceiteI" value="" size="13" maxlength="10" />&nbsp;(dd/mm/aaaa)
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Texto de Consentimento:<span class="requerido" id="reqTextoConsentimento">*</span></label>
					</td>
					<td class="componente"  width="70%">
						<textarea class="campo2" rows="4" cols="80" id="textoConsentimentoI" name="textoConsentimentoI"></textarea>
						Restam <span id="charsLeftConsentimento"></span> carateres.
					</td>
				</tr>
				<tr>
					<td class="componente" colspan="2">
						</br>
						<font color='blue'><u><b>Escalas Ativas no Per&iacute;odo<b></u></font>
					</td>
				</tr>
			</tbody>
		</table>
		<table>
			<tbody>
				<tr>
					<td class="componente">
						<label class="label" >Lista de Escala (B&ocirc;nus):</label><br>
						<select id="listaEscalasI" name="listaEscalasI" class="campo2">
                           	<option value="" selected>[SELECIONE]</option>
                        </select>
					</td>
				</tr>
				<tr>
					<td class="componente">
						<label class="label">Escalas para o Per&iacute;odo:</label><span class="requerido">*</span><br>
						<select id="escalasSelecionadasI" name="escalasSelecionadasI" size="8" style="width:500px" class="campo2" multiple="multiple">
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente">
						<div id="botao-geral" style="width:100%;">
							<div class="div-botao" style="text-align:left;float:left;width:18%;">
								<input id="botaoAdicionar" type="button" class="button" value="+" onclick="adicionaEscala(document.getElementById('listaEscalasI').value);" />
							</div>
							<div class="div-botao" style="text-align:left;float:left;width:18%;">
								<input id="botaoRemover" type="button" class="button" value="-" onclick="removeSelecionados(document.getElementById('escalasSelecionadasI'));"/>
							</div>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;">
				<div id="div-funcao-barra" class="div-botao-modal" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Salvar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />
				</div>
				<div id="div-load-barra" style="display: none;">
					<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
				</div>
			</div>
		</div>
	</form>
</div>