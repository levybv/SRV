<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/RelatorioTipoBusinessAjax.js'></script>
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

		$("#categoriaRelatorioDinamicoI").change(function(){
			if ($("#categoriaRelatorioDinamicoI").val() == "C") {
				$("#labelComando").show();
				$("#labelTabela").hide();
				$("#labelUtilizaPeriodo").hide();
				$("#labelDescUtilizaPeriodo").hide();
				$("#labelQtdParam").show();
				$("#comandoRelatorioDinamicoI").show();
				$("#tabelaRelatorioDinamicoI").hide();
				$("#periodoRelatorioDinamicoI").hide();
				$("#qtdRepParamRelatorioDinamicoI").show();
				$("#comandoRelatorioDinamicoI").val("");
				$("#periodoRelatorioDinamicoI").val("");
				$("#tabelaRelatorioDinamicoI").val("");
				$("#qtdRepParamRelatorioDinamicoI").val("");
				$("#comandoRelatorioDinamicoI").attr("disabled", false);
				$("#periodoRelatorioDinamicoI").attr("disabled", true);
				$("#tabelaRelatorioDinamicoI").attr("disabled", true);
				$("#qtdRepParamRelatorioDinamicoI").attr("disabled", false);
			} else if ($("#categoriaRelatorioDinamicoI").val() == "T") {
				$("#labelTabela").show();
				$("#labelComando").hide();
				$("#labelUtilizaPeriodo").show();
				$("#labelDescUtilizaPeriodo").show();
				$("#labelQtdParam").hide();
				$("#tabelaRelatorioDinamicoI").show();
				$("#comandoRelatorioDinamicoI").hide();
				$("#periodoRelatorioDinamicoI").show();
				$("#qtdRepParamRelatorioDinamicoI").hide();
				$("#comandoRelatorioDinamicoI").val("");
				$("#periodoRelatorioDinamicoI").val("");
				$("#tabelaRelatorioDinamicoI").val("");
				$("#qtdRepParamRelatorioDinamicoI").val("");
				$("#comandoRelatorioDinamicoI").attr("disabled", true);
				$("#periodoRelatorioDinamicoI").attr("disabled", false);
				$("#tabelaRelatorioDinamicoI").attr("disabled", false);
				$("#qtdRepParamRelatorioDinamicoI").attr("disabled", true);
			} else {
				$("#labelTabela").hide();
				$("#labelComando").hide();
				$("#labelUtilizaPeriodo").hide();
				$("#labelDescUtilizaPeriodo").hide();
				$("#labelQtdParam").hide();
				$("#tabelaRelatorioDinamicoI").hide();
				$("#comandoRelatorioDinamicoI").hide();
				$("#periodoRelatorioDinamicoI").hide();
				$("#qtdRepParamRelatorioDinamicoI").hide();
				$("#comandoRelatorioDinamicoI").val("");
				$("#periodoRelatorioDinamicoI").val("");
				$("#tabelaRelatorioDinamicoI").val("");
				$("#qtdRepParamRelatorioDinamicoI").val("");
				$("#comandoRelatorioDinamicoI").attr("disabled", true);
				$("#periodoRelatorioDinamicoI").attr("disabled", true);
				$("#tabelaRelatorioDinamicoI").attr("disabled", true);
				$("#qtdRepParamRelatorioDinamicoI").attr("disabled", true);
			}
		});	

		RelatorioTipoBusinessAjax.obtemListaRelatorioTipo(
			function (lista) {
				if (lista != null) {
					var options = $('#tipoRelatorioDinamicoI').attr('options');
					for (var i=0; i<lista.length; i++) {
						var itemVO = lista[i];
						options[options.length] = new Option(itemVO.nome, itemVO.codigo, false, false);
					}
				}
		});	

		$("#detMeta").validate({
	    	 rules: {
	    		 nomeRelatorioDinamicoI: {required:true},
	    		 descricaoRelatorioDinamicoI: {required:true},
	    		 tipoRelatorioDinamicoI: {required:true},
	    		 ativoRelatorioDinamicoI: {required:true},
	    		 periodoRelatorioDinamicoI: {required:true},
	    		 tabelaRelatorioDinamicoI: {required:true},
	    		 comandoRelatorioDinamicoI: {required:true},
	    		 categoriaRelatorioDinamicoI: {required:true},
	    		 tituloRelatorioDinamicoI: {required:true},
	    		 qtdRepParamRelatorioDinamicoI: {required:true},
	    		 nomeArquivoRelatorioDinamicoI: {required:true}
		     },
		     messages: {
		    	 nomeRelatorioDinamicoI: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 descricaoRelatorioDinamicoI: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 tipoRelatorioDinamicoI: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 ativoRelatorioDinamicoI: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 periodoRelatorioDinamicoI: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 tabelaRelatorioDinamicoI: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 comandoRelatorioDinamicoI: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 categoriaRelatorioDinamicoI: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 tituloRelatorioDinamicoI: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 qtdRepParamRelatorioDinamicoI: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 nomeArquivoRelatorioDinamicoI: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
		     },
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detMeta.action = "relatorioDinamico.do?operacao=incluiRelatorioDinamico";
		            document.detMeta.submit();
	         }
    	});

		$("#labelTabela").hide();
		$("#labelComando").hide();
		$("#labelUtilizaPeriodo").hide();
		$("#labelDescUtilizaPeriodo").hide();
		$("#labelQtdParam").hide();
		$("#tabelaRelatorioDinamicoI").hide();
		$("#comandoRelatorioDinamicoI").hide();
		$("#periodoRelatorioDinamicoI").hide();
		$("#qtdRepParamRelatorioDinamicoI").hide();
		$("#comandoRelatorioDinamicoI").val("");
		$("#periodoRelatorioDinamicoI").val("");
		$("#tabelaRelatorioDinamicoI").val("");
		$("#qtdRepParamRelatorioDinamicoI").val("");
		$("#comandoRelatorioDinamicoI").attr("disabled", true);
		$("#periodoRelatorioDinamicoI").attr("disabled", true);
		$("#tabelaRelatorioDinamicoI").attr("disabled", true);
		$("#qtdRepParamRelatorioDinamicoI").attr("disabled", true);
		$("#nomeRelatorioDinamicoI").focus();

    });

</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 770px">
	<form name="detMeta" id="detMeta" method="post"> 

		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">Nome:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="nomeRelatorioDinamicoI" name="nomeRelatorioDinamicoI" value="" maxlength="60" size="60" />
					</td>
				</tr>			
				<tr>
					<td class="componente" width="20%">
						<label class="label">Descrição:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="descricaoRelatorioDinamicoI" name="descricaoRelatorioDinamicoI" value="" maxlength="300" size="70"  />
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Tipo Relatório:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="tipoRelatorioDinamicoI" name="tipoRelatorioDinamicoI" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Ativo?<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="ativoRelatorioDinamicoI" name="ativoRelatorioDinamicoI" class="campo">
							<option value="" selected>Selecione...</option>
							<option value="S">Sim</option>
							<option value="N">Não</option>
						</select>
					</td>
				</tr>					
				<tr>
					<td class="componente" width="20%">
						<label class="label">Nome do Arquivo:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="nomeArquivoRelatorioDinamicoI" name="nomeArquivoRelatorioDinamicoI" value="" maxlength="50" size="80"  />
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Título do Arquivo:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="tituloRelatorioDinamicoI" name="tituloRelatorioDinamicoI" value="" maxlength="50" size="50"  />
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Nome das Colunas:</label> 
					</td>
					<td class="componente" >
						Nome das colunas (no arquivo) delimitados por ponto e vírgula <b>Ex.: campo1;campo2;campo3;</b><br>
  						<input type="text" class="campo2" id="colunasRelatorioDinamicoI" name="colunasRelatorioDinamicoI" value="" maxlength="500" size="80"  />
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Categoria:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="categoriaRelatorioDinamicoI" name="categoriaRelatorioDinamicoI" class="campo">
							<option value="" selected>Selecione...</option>
							<option value="T">VIA NOME DE TABELA</option>
							<option value="C">VIA COMANDO</option>
						</select>
					</td>
				</tr>					
				<tr>
					<td class="componente" width="20%">
						<label id="labelTabela" class="label">Nome da Tabela:<span class="requerido">*</span></label>
						<label id="labelComando" class="label">Comando:<span class="requerido">*</span></label>
					</td>
					<td class="componente" >
						<input type="text" class="campo2" id="tabelaRelatorioDinamicoI" name="tabelaRelatorioDinamicoI" value="" maxlength="50" size="80"  />
						<textarea class="campo2" id="comandoRelatorioDinamicoI" name="comandoRelatorioDinamicoI" rows="13" cols="100"></textarea>
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label" id="labelQtdParam">Qtd. Repetir Parâmetros:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="qtdRepParamRelatorioDinamicoI" name="qtdRepParamRelatorioDinamicoI" value="" maxlength="5" size="10"  />
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label id="labelUtilizaPeriodo" class="label">Utiliza Período?<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="periodoRelatorioDinamicoI" name="periodoRelatorioDinamicoI" class="campo">
							<option value="" selected>Selecione...</option>
							<option value="S">Sim</option>
							<option value="N">Não</option>
						</select>
						<label id="labelDescUtilizaPeriodo">Acrescenta ano e mês ao nome da tabela. <b>Ex.: nome_tabela_201402</b></label>
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