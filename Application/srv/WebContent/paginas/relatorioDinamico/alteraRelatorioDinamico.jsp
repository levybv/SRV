<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/RelatorioTipoBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/interface/RelatorioBusinessAjax.js'></script>
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

		$("#categoriaRelatorioDinamicoA").change(function(){
			if ($("#categoriaRelatorioDinamicoA").val() == "C") {
				$("#labelComando").show();
				$("#labelTabela").hide();
				$("#labelUtilizaPeriodo").hide();
				$("#labelDescUtilizaPeriodo").hide();
				$("#labelQtdParam").show();
				$("#comandoRelatorioDinamicoA").show();
				$("#tabelaRelatorioDinamicoA").hide();
				$("#periodoRelatorioDinamicoA").hide();
				$("#qtdRepParamRelatorioDinamicoA").show();
				$("#comandoRelatorioDinamicoA").val("");
				$("#periodoRelatorioDinamicoA").val("");
				$("#tabelaRelatorioDinamicoA").val("");
				$("#qtdRepParamRelatorioDinamicoA").val("");
				$("#comandoRelatorioDinamicoA").attr("disabled", false);
				$("#periodoRelatorioDinamicoA").attr("disabled", true);
				$("#tabelaRelatorioDinamicoA").attr("disabled", true);
				$("#qtdRepParamRelatorioDinamicoA").attr("disabled", false);
			} else if ($("#categoriaRelatorioDinamicoA").val() == "T") {
				$("#labelTabela").show();
				$("#labelComando").hide();
				$("#labelUtilizaPeriodo").show();
				$("#labelDescUtilizaPeriodo").show();
				$("#labelQtdParam").hide();
				$("#tabelaRelatorioDinamicoA").show();
				$("#comandoRelatorioDinamicoA").hide();
				$("#periodoRelatorioDinamicoA").show();
				$("#qtdRepParamRelatorioDinamicoA").hide();
				$("#comandoRelatorioDinamicoA").val("");
				$("#periodoRelatorioDinamicoA").val("");
				$("#tabelaRelatorioDinamicoA").val("");
				$("#qtdRepParamRelatorioDinamicoA").val("");
				$("#comandoRelatorioDinamicoA").attr("disabled", true);
				$("#periodoRelatorioDinamicoA").attr("disabled", false);
				$("#tabelaRelatorioDinamicoA").attr("disabled", false);
				$("#qtdRepParamRelatorioDinamicoA").attr("disabled", true);
			} else {
				$("#labelTabela").hide();
				$("#labelComando").hide();
				$("#labelUtilizaPeriodo").hide();
				$("#labelDescUtilizaPeriodo").hide();
				$("#labelQtdParam").hide();
				$("#tabelaRelatorioDinamicoA").hide();
				$("#comandoRelatorioDinamicoA").hide();
				$("#periodoRelatorioDinamicoA").hide();
				$("#qtdRepParamRelatorioDinamicoA").hide();
				$("#comandoRelatorioDinamicoA").val("");
				$("#periodoRelatorioDinamicoA").val("");
				$("#tabelaRelatorioDinamicoA").val("");
				$("#qtdRepParamRelatorioDinamicoA").val("");
				$("#comandoRelatorioDinamicoA").attr("disabled", true);
				$("#periodoRelatorioDinamicoA").attr("disabled", true);
				$("#tabelaRelatorioDinamicoA").attr("disabled", true);
				$("#qtdRepParamRelatorioDinamicoA").attr("disabled", true);
			}
		});

		RelatorioBusinessAjax.obtemRelatorio(idRelatorio,
			function (itemVO) {
				if (itemVO != null) {
					RelatorioTipoBusinessAjax.obtemListaRelatorioTipo(
						function (lista) {
							if (lista != null) {
								var options = $('#tipoRelatorioDinamicoA').attr('options');
								for (var i=0; i<lista.length; i++) {
									var itemTipoVO = lista[i];
									options[options.length] = new Option(itemTipoVO.nome, itemTipoVO.codigo, false, false);
								}
							}
							$("#tipoRelatorioDinamicoA").val(itemVO.relatorioTipoVO.codigo);
					});	
					$("#idRelatorioA").val(itemVO.codigo);
					$("#codigoDIV").html(itemVO.codigo);
					$("#nomeRelatorioDinamicoA").val(itemVO.nome);
					$("#descricaoRelatorioDinamicoA").val(itemVO.descricao);
					$("#ativoRelatorioDinamicoA").val(itemVO.isAtivo?"S":"N");
					$("#periodoRelatorioDinamicoA").val(itemVO.isPeriodo?"S":"N");
					$("#nomeArquivoRelatorioDinamicoA").val(itemVO.nomeArquivo);
					$("#tituloRelatorioDinamicoA").val(itemVO.titulo);
					$("#colunasRelatorioDinamicoA").val(itemVO.descricaoColunas==null?"":itemVO.descricaoColunas);
					if (itemVO.nomeTabela == null) {
						$("#labelComando").show();
						$("#labelTabela").hide();
						$("#labelUtilizaPeriodo").hide();
						$("#labelDescUtilizaPeriodo").hide();
						$("#labelQtdParam").show();
						$("#comandoRelatorioDinamicoA").show();
						$("#tabelaRelatorioDinamicoA").hide();
						$("#periodoRelatorioDinamicoA").hide();
						$("#qtdRepParamRelatorioDinamicoA").show();
						$("#comandoRelatorioDinamicoA").val("");
						$("#tabelaRelatorioDinamicoA").val("");
						$("#qtdRepParamRelatorioDinamicoA").val(itemVO.qtdRepetirParametro==null?"":itemVO.qtdRepetirParametro);
						$("#comandoRelatorioDinamicoA").attr("disabled", false);
						$("#periodoRelatorioDinamicoA").attr("disabled", true);
						$("#tabelaRelatorioDinamicoA").attr("disabled", true);
						$("#qtdRepParamRelatorioDinamicoA").attr("disabled", false);
						$("#categoriaRelatorioDinamicoA").val("C");
						$("#comandoRelatorioDinamicoA").val(itemVO.comandoSQL);
					} else {
						$("#labelTabela").show();
						$("#labelComando").hide();
						$("#labelUtilizaPeriodo").show();
						$("#labelDescUtilizaPeriodo").show();
						$("#labelQtdParam").hide();
						$("#tabelaRelatorioDinamicoA").show();
						$("#comandoRelatorioDinamicoA").hide();
						$("#periodoRelatorioDinamicoA").show();
						$("#qtdRepParamRelatorioDinamicoA").hide();
						$("#comandoRelatorioDinamicoA").val("");
						$("#tabelaRelatorioDinamicoA").val("");
						$("#comandoRelatorioDinamicoA").attr("disabled", true);
						$("#periodoRelatorioDinamicoA").attr("disabled", false);
						$("#tabelaRelatorioDinamicoA").attr("disabled", false);
						$("#qtdRepParamRelatorioDinamicoA").attr("disabled", true);
						$("#categoriaRelatorioDinamicoA").val("T");
						$("#tabelaRelatorioDinamicoA").val(itemVO.nomeTabela);
					}
				}
		});

		$("#detMeta").validate({
	    	 rules: {
	    		 nomeRelatorioDinamicoA: {required:true},
	    		 descricaoRelatorioDinamicoA: {required:true},
	    		 tipoRelatorioDinamicoA: {required:true},
	    		 ativoRelatorioDinamicoA: {required:true},
	    		 periodoRelatorioDinamicoA: {required:true},
	    		 tabelaRelatorioDinamicoA: {required:true},
	    		 comandoRelatorioDinamicoA: {required:true},
	    		 categoriaRelatorioDinamicoA: {required:true},
	    		 tituloRelatorioDinamicoA: {required:true},
	    		 qtdRepParamRelatorioDinamicoA: {required:true},
	    		 nomeArquivoRelatorioDinamicoA: {required:true}
		     },
		     messages: {
		    	 nomeRelatorioDinamicoA: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 descricaoRelatorioDinamicoA: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 tipoRelatorioDinamicoA: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 ativoRelatorioDinamicoA: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 periodoRelatorioDinamicoA: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 tabelaRelatorioDinamicoA: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 comandoRelatorioDinamicoA: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 categoriaRelatorioDinamicoA: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 tituloRelatorioDinamicoA: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 qtdRepParamRelatorioDinamicoA: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 nomeArquivoRelatorioDinamicoA: {required:'<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
		     },
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detMeta.action = "relatorioDinamico.do?operacao=alteraRelatorioDinamico";
		            document.detMeta.submit();
	         }
		});
		
		$("#nomeRelatorioDinamicoA").focus();

    });

</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 770px;">
	<form name="detMeta" id="detMeta" method="post"> 
		<input type='hidden' id="idRelatorioA" name="idRelatorioA" value=""/>
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">Código:</label> 
					</td>
					<td class="componente" >
						<div id="codigoDIV"></div>
					</td>
				</tr>			
				<tr>
					<td class="componente" width="20%">
						<label class="label">Nome:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="nomeRelatorioDinamicoA" name="nomeRelatorioDinamicoA" value="" maxlength="60" size="60" />
					</td>
				</tr>			
				<tr>
					<td class="componente" width="20%">
						<label class="label">Descrição:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="descricaoRelatorioDinamicoA" name="descricaoRelatorioDinamicoA" value="" maxlength="300" size="70"  />
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Tipo Relatório:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="tipoRelatorioDinamicoA" name="tipoRelatorioDinamicoA" class="campo">
							<option value="" selected>Selecione...</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Ativo?<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="ativoRelatorioDinamicoA" name="ativoRelatorioDinamicoA" class="campo">
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
  						<input type="text" class="campo2" id="nomeArquivoRelatorioDinamicoA" name="nomeArquivoRelatorioDinamicoA" value="" maxlength="50" size="80"  />
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Título do Arquivo:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="tituloRelatorioDinamicoA" name="tituloRelatorioDinamicoA" value="" maxlength="50" size="50"  />
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Nome das Colunas:</label> 
					</td>
					<td class="componente" >
						Nome das colunas (no arquivo) delimitados por ponto e vírgula <b>Ex.: campo1;campo2;campo3;</b><br>
  						<input type="text" class="campo2" id="colunasRelatorioDinamicoA" name="colunasRelatorioDinamicoA" value="" maxlength="500" size="80"  />
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label class="label">Categoria:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="categoriaRelatorioDinamicoA" name="categoriaRelatorioDinamicoA" class="campo">
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
						<input type="text" class="campo2" id="tabelaRelatorioDinamicoA" name="tabelaRelatorioDinamicoA" value="" maxlength="50" size="80"  />
						<textarea class="campo2" id="comandoRelatorioDinamicoA" name="comandoRelatorioDinamicoA" rows="13" cols="100"></textarea>
					</td>
				</tr>			
				<tr>
					<td class="componente" width="20%">
						<label class="label" id="labelQtdParam">Qtd. Repetir Parâmetros:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input type="text" class="campo2" id="qtdRepParamRelatorioDinamicoA" name="qtdRepParamRelatorioDinamicoA" value="" maxlength="5" size="10"  />
					</td>
				</tr>
				<tr>
					<td class="componente" width="20%">
						<label id="labelUtilizaPeriodo" class="label">Utiliza Período?<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="periodoRelatorioDinamicoA" name="periodoRelatorioDinamicoA" class="campo">
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