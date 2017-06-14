<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/util.js'></script>
<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
<script type='text/javascript' src='srvdwr/engine.js'></script>	
	
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
<script type="text/javascript" src="js/lib/jquery-1.4.2.js"  charset="UTF8" ></script>
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

		IndicadorBusinessAjax.obtemDetalheCalculo(idFuncionario, idIndicador, idEmpresa, idFilial, ano, mes, 
			function (detalheCalculo) {
				$("#mesReferencia").html(detalheCalculo.mesExtenso + " / " + detalheCalculo.ano);
				$("#filial").html(detalheCalculo.descricaoFilial);
				$("#funcionario").html(detalheCalculo.idFuncionario + " - " + detalheCalculo.nomeFuncionario);
				$("#pesoXResultadoTotal").html(detalheCalculo.realizadoPonderacaoFormatado);
				$("#target").html(detalheCalculo.salarioMinimoClasseHayFormatada + " Salários"); 
				$("#total").html(detalheCalculo.salarioMinimoXPonderacaoFormatado + " Salários"); 
				$("#percentualProgressaoReducao").html(detalheCalculo.realizadoFaixaFormatado); 
//				if (detalheCalculo.inicioFaixaFormatado != "") {
//					$("#escalaUtilizada").html(detalheCalculo.descricaoEscalaFormatada + " " + detalheCalculo.inicioFaixaFormatado + " à " + detalheCalculo.fimFaixaFormatado); 
//				} else {
					$("#escalaUtilizada").html(detalheCalculo.descricaoEscalaFormatada); 
//				}
					$("#mesesProporcional").html(detalheCalculo.mesesProporcional); 
					$("#observacao").html("<b> Obs.: "+detalheCalculo.observacao+"</b"); 
				$("#aReceber").html(detalheCalculo.valorPremioCalculadoBonusFormatado + " Salários");
	    });	
	    
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 440px;">
	<form name="detFilial" id="detFilial" method="post"> 
			
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" >
						<label class="label">Mês de Referência:</label> 
					</td>
					<td class="componente" >
  						<div id="mesReferencia"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Filial:</label> 
					</td>
					<td class="componente" >
  						<div id="filial"></div>
					</td>
				</tr>					
				<tr>
					<td class="componente" >
						<label class="label">Funcionário:</label> 
					</td>
					<td class="componente" >
  						<div id="funcionario"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Peso X Resultado Total:</label> 
					</td>
					<td class="componente" >
  						<div id="pesoXResultadoTotal"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Target:</label> 
					</td>
					<td class="componente" >
  						<div id="target"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Valor Antes do Gatilho:</label> 
					</td>
					<td class="componente" >
  						<div id="total"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Percentual de Progressão ou Redução:</label> 
					</td>
					<td class="componente" >
  						<div id="percentualProgressaoReducao"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Vlr Integral c/ Progressão ou Redução:</label> 
					</td>
					<td class="componente" >
  						<div id="escalaUtilizada"></div>
					</td>
				</tr>
				
				<tr>
					<td class="componente" >
						<label class="label">Qtde de Meses Proporcionalidade:</label> 
					</td>
					<td class="componente" >
  						<div id="mesesProporcional"></div>
					</td>
				</tr>
				
				<tr>
					<td class="componente" >
						<label class="label">À Receber:</label> 
					</td>
					<td class="componente">
  						<div id="aReceber"></div>
					</td>
				</tr>
			</tbody>
		</table>
		<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;">
				<div class="div-botao" style="float:right;width:100%;">
					<table border="0" width="100%" >
					<tr>
					<td class="componente">
  						<div id="observacao"></div>
					</td>
					<td style="float: right;" >
					<input id="botaoCancelar" type="button" class="button" value="Fechar" />
					</td>
					</tr>
					</table>
				</div>
			</div>
		</div>
	</form>
</div>