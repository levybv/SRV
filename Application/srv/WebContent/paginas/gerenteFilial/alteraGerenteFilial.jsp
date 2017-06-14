<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/util.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	
<script type='text/javascript' src='srvdwr/interface/FuncionarioBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/GerenteFilialBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/FilialBusinessAjax.js'></script>

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

		FilialBusinessAjax.obtemTodasFiliaisAtivas(
			function (filiais) {
				if (filiais != null) {
					$('#filialA').find('option').remove();
					var options1 = $('#filialA').attr('options');
					options1[0] = new Option("Selecione...", "", false, false);
					for (var i=0; i<filiais.length; i++) {
						var filialVO = filiais[i];
						options1[options1.length] = new Option(filialVO.codFilial+" - "+filialVO.descricao, filialVO.codFilial, false, false);
					}
					GerenteFilialBusinessAjax.obtemGerenteLoja(idFuncA, idFilialA,
						function (gerenteVO) {
							if (gerenteVO != null) {
								$("#idFuncA").val(idFuncA);
								$("#idFilialA").val(idFilialA);
								$("#idFilialOriA").val(gerenteVO.filialOrigem.codFilial);
								$("#divFilialOriA").html(gerenteVO.filialOrigem.codFilial + " - " + gerenteVO.filialOrigem.descricao);
								$("#divFuncA").html(gerenteVO.nomeFuncionario);
								$('#filialA').val(gerenteVO.filial.codFilial);
								$('#codAtuacaoA').val(gerenteVO.codAtuacao);
								$('#filialA').focus();
							}
					});
				}
		});

		jQuery.validator.addMethod('lojaCadastrada', function(value, element, params) {
			dwr.engine.setAsync(false);
			var filialCadastrada = false;
			GerenteFilialBusinessAjax.lojaCadastrada(value,function (codFunc) {
				if (codFunc != null){
					filialCadastrada = false;
				} else {
					filialCadastrada = true;
				}
			});
			return filialCadastrada;
		});

		$("#detMeta").validate({
	    	 rules: {
	    		 	filialA: {required:true,
	    		 			   lojaCadastrada:true},
	    		 	codAtuacaoA: {required:true}
		     },
		     messages: {
		    	 	filialA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>',
							   lojaCadastrada: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Loja já possui gerente cadastrado!</span>'},
					codAtuacaoA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>'}
		     },
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show();
		       	document.detMeta.action = "gerenteFilial.do?operacao=alteraGerenteFilial";
	            document.detMeta.submit();
	         }
    	});

    });

</script>

<div id="corpoModalIncluirGerenteFilial" style="float:left;margin-left: 10px; width: 675px">
	<form name="detMeta" id="detMeta" method="post"> 
		<input type="hidden" id="idFuncA" name="idFuncA" value=""/>
		<input type="hidden" id="idFilialA" name="idFilialA" value=""/>
		<input type="hidden" id="idFilialOriA" name="idFilialOriA" value=""/>
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">Filial de Origem:</label> 
					</td>
					<td class="componente" >
						<div id="divFilialOriA"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Funcionário:</label> 
					</td>
					<td class="componente" >
						<div id="divFuncA"></div>
					</td>
				</tr>			
				<tr>
					<td class="componente" width="20%">
						<label class="label">Filial:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="filialA" name="filialA" class="campo">
							<option value="" selected>Aguarde...</option>
						</select>
						&nbsp;
						<label class="label">Atuação:<span class="requerido">*</span></label>
						<select id="codAtuacaoA" name="codAtuacaoA" class="campo">
							<option value="" selected>Selecione...</option>
							<option value="1">Definitiva</option>
							<option value="2">Provisória</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
					</td>
					<td class="componente" >
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
					</td>
					<td class="componente" >
						<span class="requerido">* Campo de preenchimento obrigat&oacute;rio.</span>
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