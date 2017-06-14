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
<script type="text/javascript" src="js/lib/jquery_menu_20.js"></script>
<script type="text/javascript" src="js/lib/menu_20.js"></script>

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
					var options = $('#filialOri').attr('options');
					for (var i=0; i<filiais.length; i++) {
						var filialVO = filiais[i];
						options[options.length] = new Option(filialVO.descricao, filialVO.codFilial, false, false);
					}
				}
		});

		GerenteFilialBusinessAjax.obtemFuncionariosPorFilial(null,
			function (gerentes) {
				if (gerentes != null) {
					var options = $('#matriculaI').attr('options');
					for (var i=0; i<gerentes.length; i++) {
						var gerenteVO = gerentes[i];
						options[options.length] = new Option(gerenteVO.nomeFuncionario, gerenteVO.idFuncionario, false, false);
					}
				}
		});

		FilialBusinessAjax.obtemTodasFiliaisAtivas(
			function (filiais) {
				if (filiais != null) {
					var options = $('#filialI').attr('options');
					for (var i=0; i<filiais.length; i++) {
						var filialVO = filiais[i];
						options[options.length] = new Option(filialVO.descricao, filialVO.codFilial, false, false);
					}
				}
		});

		jQuery.validator.addMethod('lojaCadastrada', function(value, element, params) {
			dwr.engine.setAsync(false);
			var filialCadastrada = false;
			GerenteFilialBusinessAjax.lojaCadastrada($("#filialI").val(),function (codFunc) {
				if (codFunc != null){
					filialCadastrada = false;
				} else {
					filialCadastrada = true;
				}
			});
			return filialCadastrada;
		});

		$("#filialOri").change(function(){
			GerenteFilialBusinessAjax.obtemFuncionariosPorFilial($("#filialOri").val(),
				function (gerentes) {
					if (gerentes != null) {
						$('#matriculaI').find('option').remove();
						var options = $('#matriculaI').attr('options');
						options[0] = new Option("Selecione...", "", false, false);
						for (var i=0; i<gerentes.length; i++) {
							var gerenteVO = gerentes[i];
							options[options.length] = new Option(gerenteVO.nomeFuncionario, gerenteVO.idFuncionario, false, false);
						}
					}
			});
		});

		$("#detMeta").validate({
	    	 rules: {
	    		 	matriculaI: {required:true},
	    		 	filialI: {required:true,
	    		 				lojaCadastrada:true},
	    		 	codAtuacaoI: {required:true}
		     },
		     messages: {
		    	 	matriculaI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>'},
					filialI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>',
								lojaCadastrada: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Loja já possui gerente cadastrado!</span>'},
					codAtuacaoI: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Selecione na lista.</span>'}
		     },
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show();
			       	document.detMeta.action = "gerenteFilial.do?operacao=incluiGerenteFilial";
		            document.detMeta.submit();
	         }
    	});

		$("#filialOri").focus();

    });

</script>

<div id="corpoModalIncluirGerenteFilial" style="float:left;margin-left: 10px; width: 675px">
	<form name="detMeta" id="detMeta" method="post"> 

		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="25%">
						<label class="label">Filial de Origem:</label> 
					</td>
					<td class="componente" >
						<select id="filialOri" name="filialOri" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" width="25%">
						<label class="label">Funcionário:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="matriculaI" name="matriculaI" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>			
				<tr>
					<td class="componente" width="25%">
						<label class="label">Incluir Filial:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="filialI" name="filialI" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" width="25%">
						<label class="label">Atuação:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="codAtuacaoI" name="codAtuacaoI" class="campo">
							<option value="" selected>Selecione...</option>
							<option value="1">Definitiva</option>
							<option value="2">Provisória</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" width="25%">
					</td>
					<td class="componente" >
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