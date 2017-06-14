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

		$("#anoA").val(idConfigBonus);
		$("#anoDIV").html(idConfigBonus);

		$('#codIndicIniA').numeric();
		$('#codIndicFimA').numeric();
		$("#fundingA").maskMoney({decimal:",", thousands:".", allowNegative:false});
		$('#dataLimiteAceiteA').mask('99/99/9999');
		$('#textoConsentimentoA').limit('500','#charsLeftConsentimento');

		$("#formAlteraConfiguracaoBonus").validate({
			rules: {
				flgEncerradoA: {required:true},
				idFuncCorpA: {required:true},
				codIndicIniA: {	required:true,
								//isCodigoIndicadorJaUtilizado: true,
								isRangeIndicadoresJaExiste:true},
				codIndicFimA: {	required:true,
								isMaiorQueInicio: true,
								isRangeIndicadoresJaExiste: true},
				flgFundingA: {required:true},
				fundingA: {required:true},
				flgContratoMetaA: {required:true},
				dataLimiteAceiteA: {required:true,
									isDataInvalida:true},
				textoConsentimentoA: {required:true},
				escalasSelecionadasA: {validaEscalasSelecionadas:true}
			},
	     	messages: {
				flgEncerradoA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
				idFuncCorpA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
				codIndicIniA: {	required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
								//isCodigoIndicadorJaUtilizado: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> C&oacute;digo de indicador j&aacute; est&aacute; sendo utilizado.</span>',
								isRangeIndicadoresJaExiste: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Range de indicadores já está sendo utilizado.</span>'},
				codIndicFimA: {	required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
								isMaiorQueInicio: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> C&oacute;digo final deve ser maior que c&oacute;digo in&iacute;cio.</span>',
								isRangeIndicadoresJaExiste: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Range de indicadores já está sendo utilizado.</span>'},
				flgFundingA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
				fundingA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
				flgContratoMetaA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
				dataLimiteAceiteA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
									isDataInvalida: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Data inv&aacute;lida.</span>'},
				textoConsentimentoA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
				escalasSelecionadasA: {validaEscalasSelecionadas: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	     	},
	        submitHandler : function(form){
	        	$("#div-botao").hide();
	           	$("#div-load").show();
	         	for (i = 0; i < document.getElementById('escalasSelecionadasA').options.length ; i++){
					document.getElementById('escalasSelecionadasA').options[i].selected = true;
				}
		       	document.formAlteraConfiguracaoBonus.action = "configuracaoBonus.do?operacao=alteraConfiguracaoBonus";
	            document.formAlteraConfiguracaoBonus.submit();
	        }
    	});

		/* funcoes de carga */
		EscalaBusinessAjax.obtemListaEscalaBonus(
			function (listaEscala) {
				if (listaEscala != null) {
					var options = $('#listaEscalasA').attr('options');
					for (var i=0; i<listaEscala.length; i++) {
						var itemEscala = listaEscala[i];
						var numEscala = itemEscala.numEscala!=null?' (NUM: ' + itemEscala.numEscala + ')':"";
						var descrEscala = itemEscala.idEscala + ' - ' + itemEscala.descricaoEscala + numEscala;
						options[options.length] = new Option(descrEscala, itemEscala.idEscala, false, false);
					}
				}
		});

		/* funcoes de validacao */
		jQuery.validator.addMethod('isCodigoIndicadorJaUtilizado', function(value, element, params) {
			var validacao = false;
			IndicadorBusinessAjax.isCodigoIndicadorJaUtilizado(value, function (retorno) {
				validacao = retorno;
			});
			return !validacao;
		});

		jQuery.validator.addMethod('isRangeIndicadoresJaExiste', function(value, element, params) {
			var validacao = false;
			ConfiguracaoBonusBusinessAjax.isRangeIndicadoresJaExiste(value, $('#anoA').val(), function (retorno) {
				validacao = retorno;
			});
			return !validacao;
		});

		jQuery.validator.addMethod('isMaiorQueInicio', function(value, element, params) {
			var validacao = false;
			if ($('#codIndicIniA').val() != "") {
				validacao = $('#codIndicIniA').val() >= $('#codIndicFimA').val();
			}
			return !validacao;
		});

		jQuery.validator.addMethod('validaEscalasSelecionadas', function(value, element, params) {
			var validacao = (document.getElementById('escalasSelecionadasA').options.length > 0);
			return validacao;
		});

		jQuery.validator.addMethod('isDataInvalida', function(value, element, params) {
			var validacao = false;
			DataBusinessAjax.isDataInvalida(value, function (retorno) {
				validacao = retorno;
			});
			return !validacao;
		});

		$('#idFuncCorpA').blur(function () {
			FuncionarioBusinessAjax.obtemFuncionario($('#idFuncCorpA').val(), function (retorno) {
				if (retorno != null) {
					$('#idNomeFuncDIV').html(retorno.nomeFuncionario);
				} else {
					$('#idNomeFuncDIV').html("");
				}
			});
		});

		$('#flgFundingA').change(function(value, element, params) {
			if ($('#flgFundingA').val()=='N') {
				$('#fundingA').val("");
				$('#fundingA').attr("disabled", true);
				$('#reqFunding').hide();
			} else {
				$('#fundingA').val("");
				$('#fundingA').attr("disabled", false);
				$('#reqFunding').show();
			}
		});

		$('#flgContratoMetaA').change(function(value, element, params) {
			if ($('#flgContratoMetaA').val()=='N') {
				$('#dataLimiteAceiteA').val("");
				$('#dataLimiteAceiteA').attr("disabled", true);
				$('#textoConsentimentoA').val("");
				$('#textoConsentimentoA').attr("disabled", true);
				$('#reqDataLimiteAceite').hide();
				$('#reqTextoConsentimento').hide();
			} else {
				$('#dataLimiteAceiteA').val("");
				$('#dataLimiteAceiteA').attr("disabled", false);
				$('#textoConsentimentoA').val("");
				$('#textoConsentimentoA').attr("disabled", false);
				$('#reqDataLimiteAceite').show();
				$('#reqTextoConsentimento').show();
			}
		});

		ConfiguracaoBonusBusinessAjax.obtemConfiguracaoBonus($("#anoA").val(), function (configBonusVO) {
			$("#codIndicIniA").val(configBonusVO.codIndicIni);
			$("#codIndicFimA").val(configBonusVO.codIndicFim);
			$("#idFuncCorpA").val(configBonusVO.idFuncionarioCorporativo);
			$('#idFuncCorpA').blur();
			$("#flgEncerradoA").val(configBonusVO.isEncerrado?"S":"N");
			$("#flgFundingA").val(configBonusVO.isFunding?"S":"N");
			$("#flgFundingA").change();
			$("#fundingA").val(configBonusVO.funding!=null?configBonusVO.funding:"");
			$("#fundingA").val($("#fundingA").val().replace(".",","));
			CalendarioComercialBusinessAjax.obtemListaPeriodoMesAno(function (periodo) {
				if (periodo != null) {
					var options = $('#periodoA').attr('options');
					for (var i=0; i<periodo.length; i++) {
						var periodoVO = periodo[i];
						options[options.length] = new Option(periodoVO.periodoMesAno, periodoVO.mesAno, false, false);
					}
					if (configBonusVO.periodoDisponivel != null) {
						var perFormTemp = configBonusVO.periodoDisponivel.substring(3)+'00'+configBonusVO.periodoDisponivel.substring(0,2).replace("0","");
						$("#periodoA").val(perFormTemp);
					}
				}
			});
			$("#flgContratoMetaA").val(configBonusVO.isContratoMeta?"S":"N");
			$("#flgContratoMetaA").change();
			if (configBonusVO.dataLimiteAceite!=null) {
				var dtLimAce = configBonusVO.dataLimiteAceite;
				var year = dtLimAce.getFullYear();
				var month = (1 + dtLimAce.getMonth()).toString();
				month = month.length > 1 ? month : '0' + month;
				var day = dtLimAce.getDate().toString();
				day = day.length > 1 ? day : '0' + day;
				$("#dataLimiteAceiteA").val(day + '/' + month + '/' + year);
			}
			$("#textoConsentimentoA").val(configBonusVO.textoConsentimento!=null?configBonusVO.textoConsentimento:"");
			if (configBonusVO.listaEscala != null) {
				for (i = 0; i < configBonusVO.listaEscala.length; i++) {
					var itemEscala = configBonusVO.listaEscala[i];
					var numEscala = itemEscala.numEscala != null ? ' (NUM: ' + itemEscala.numEscala + ')' : "";
					var descrEscala = itemEscala.idEscala + ' - ' + itemEscala.descricaoEscala + numEscala;
					document.getElementById("escalasSelecionadasA").options[i] = new Option(descrEscala, itemEscala.idEscala);
					$("#listaEscalasA option[value='"+itemEscala.idEscala+"']").remove();
				}
			}
		});


    });

	/* Funcoes auxiliares */
	function adicionaEscala(escalaSelecionada){
		if(escalaSelecionada != ''){
			var descricao = obtemEscalaSelecionada();
			var posicao = obtemPosicaoEscalaSelecionada(document.getElementById("listaEscalasA"));
			document.getElementById("escalasSelecionadasA").options[document.getElementById("escalasSelecionadasA").options.length] = new Option(descricao,document.getElementById("listaEscalasA").value);
			document.getElementById("listaEscalasA").options[posicao] = null;
		}
	}

	function obtemEscalaSelecionada(){
		return document.getElementById("listaEscalasA").options[obtemPosicaoEscalaSelecionada(document.getElementById("listaEscalasA"))].text;
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
		document.getElementById("listaEscalasA").options.length = 0;
		document.getElementById("listaEscalasA").options[document.getElementById("listaEscalasA").options.length] = new Option('[SELECIONE]', '');
		var listEscalasJaSelecionadas = obtemEscalasJaSelecionadas(document.getElementById("escalasSelecionadasA"));
		EscalaBusinessAjax.obtemListaEscalaBonus(function(listaEscalaRefresh) {
			for (i = 0; i < listaEscalaRefresh.length; i++) {
				var itemEscala = listaEscalaRefresh[i];
				var numEscala = itemEscala.numEscala != null ? ' (NUM: ' + itemEscala.numEscala + ')' : "";
				var descrEscala = itemEscala.idEscala + ' - ' + itemEscala.descricaoEscala + numEscala;
				if (!isJaSelecionou(listEscalasJaSelecionadas, descrEscala)) {
					document.getElementById("listaEscalasA").options[document.getElementById("listaEscalasA").options.length] = new Option(descrEscala, itemEscala.idEscala);
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

<div id="corpoModalAlteraConfiguracaoBonus" style="float:left;margin-left: 10px; width: 625px">

	<form name="formAlteraConfiguracaoBonus" id="formAlteraConfiguracaoBonus" method="post"> 
		</br>
		<table class="tabelaComponente">
			<tbody>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Ano (B&ocirc;nus):</label>
					</td>
					<td class="componente"  width="70%">
						<div id="anoDIV"></div>
						<input type="hidden" id="anoA" name="anoA" value="" />
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Per&iacute;odo Encerrado:<span class="requerido">*</span></label>
					</td>
					<td class="componente" width="70%">
						<select id="flgEncerradoA" name="flgEncerradoA" class="campo">
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
						<input type="text" class="campo2" id="idFuncCorpA" name="idFuncCorpA" value="" size="14" maxlength="8" /> <label id="idNomeFuncDIV" class="label"></label>
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Range de Indicadores:<span class="requerido">*</span></label>
					</td>
					<td class="componente"  width="70%">
						<input type="text" class="campo2" id="codIndicIniA" name="codIndicIniA" value="" size="7" maxlength="5" />
						&nbsp;at&eacute;&nbsp;
						<input type="text" class="campo2" id="codIndicFimA" name="codIndicFimA" value="" size="7" maxlength="5" />
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Utiliza Funding:<span class="requerido">*</span></label>
					</td>
					<td class="componente" width="70%">
						<select id="flgFundingA" name="flgFundingA" class="campo">
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
						<input type="text" class="campo2" id="fundingA" name="fundingA" value="" size="8" maxlength="6" />
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Per&iacute;odo Dispon&iacute;vel:</label>
					</td>
					<td class="componente" width="70%">
						<select id="periodoA" name="periodoA" class="campo">
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
						<select id="flgContratoMetaA" name="flgContratoMetaA" class="campo">
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
						<input type="text" class="campo2" id="dataLimiteAceiteA" name="dataLimiteAceiteA" value="" size="13" maxlength="10" />&nbsp;(dd/mm/aaaa)
					</td>
				</tr>
				<tr>
					<td class="componente" width="30%">
						<label class="label">Texto de Consentimento:<span class="requerido" id="reqTextoConsentimento">*</span></label>
					</td>
					<td class="componente"  width="70%">
						<textarea class="campo2" rows="4" cols="80" id="textoConsentimentoA" name="textoConsentimentoA"></textarea>
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
						<select id="listaEscalasA" name="listaEscalasA" class="campo2">
                           	<option value="" selected>[SELECIONE]</option>
                        </select>
					</td>
				</tr>
				<tr>
					<td class="componente">
						<label class="label">Escalas para o Per&iacute;odo:</label><span class="requerido">*</span><br>
						<select id="escalasSelecionadasA" name="escalasSelecionadasA" size="8" style="width:500px" class="campo2" multiple="multiple">
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente">
						<div id="botao-geral" style="width:100%;">
							<div class="div-botao" style="text-align:left;float:left;width:18%;">
								<input id="botaoAdicionar" type="button" class="button" value="+" onclick="adicionaEscala(document.getElementById('listaEscalasA').value);" />
							</div>
							<div class="div-botao" style="text-align:left;float:left;width:18%;">
								<input id="botaoRemover" type="button" class="button" value="-" onclick="removeSelecionados(document.getElementById('escalasSelecionadasA'));"/>
							</div>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;">
				<div class="div-botao-modal" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Salvar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />
				</div>
				<div id="div-load" style="display: none;">
					<img id="aguardeCargo" alt="Aguarde..." src="images/ajax-loader.gif">
				</div>
			</div>
		</div>
	</form>
</div>