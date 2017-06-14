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
				$("#valorPremioPrevio").html(detalheCalculo.valorPremioFormatado); 
				$("#rateio").html(detalheCalculo.percentualRateioFormatado); 
				$("#meta").html(detalheCalculo.metaFormatado); 
				$("#realizado").html(detalheCalculo.realizadoFormatado); 
				$("#realizadoXMeta").html(detalheCalculo.realizadoXMetaFormatado); 
				$("#escala").html(detalheCalculo.descricaoEscalaFormatada);
				$("#faixaUtilizada").html(detalheCalculo.faixaUtilizadaFormatada); 
				$("#ponderacaoUtilizada").html(detalheCalculo.ponderacaoUtilizadaFormatada); 
				$("#valorPremioCalculado").html(detalheCalculo.valorPremioCalculadoFormatado); 
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
						<label class="label">Valor Prêmio Prévio:</label> 
					</td>
					<td class="componente" >
  						<div id="valorPremioPrevio"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Rateio:</label> 
					</td>
					<td class="componente" >
  						<div id="rateio"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Meta:</label> 
					</td>
					<td class="componente" >
  						<div id="meta"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Realizado:</label> 
					</td>
					<td class="componente" >
  						<div id="realizado"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Realizado x Meta:</label> 
					</td>
					<td class="componente" >
  						<div id="realizadoXMeta"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Escala:</label> 
					</td>
					<td class="componente" >
  						<div id="escala"></div>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Faixa utilizada:</label> 
					</td>
					<td class="componente" >
  						<div id="faixaUtilizada"></div>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Ponderação Utilizada:</label> 
					</td>
					<td class="componente" >
  						<div id="ponderacaoUtilizada"></div>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Valor Prêmio Calculado:</label> 
					</td>
					<td class="componente" >
  						<div id="valorPremioCalculado"></div>
					</td>
				</tr>																																				
			</tbody>
		</table>
		<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoCancelar" type="button" class="button" value="Fechar" />					
				</div>
			</div>
		</div>
</form>
</div>