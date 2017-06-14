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
	
		IndicadorBusinessAjax.obtemDetalheCalculoLoja(idIndicador, idEmpresa, idFilial, ano, mes, 
			function (detalheCalculoLoja) {
				$("#mesReferencia").html(detalheCalculoLoja.mesExtenso + " / " + detalheCalculoLoja.ano);
				$("#filial").html(detalheCalculoLoja.descricaoFilial);
				$("#meta").html(detalheCalculoLoja.metaFormatado); 
				$("#qtdeRealizado").html(detalheCalculoLoja.qtdeRealizadoFormatado); 
				$("#realizado").html(detalheCalculoLoja.realizadoFormatado); 
				$("#realizadoXMeta").html(detalheCalculoLoja.realizadoXMetaFormatado); 
				$("#escala").html(detalheCalculoLoja.descricaoEscalaFormatada);
				$("#faixaUtilizada").html(detalheCalculoLoja.faixaUtilizadaFormatada); 
				$("#valorPremioCalculado").html(detalheCalculoLoja.valorPremioCalculadoFormatado); 
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
						<label class="label">Qtde Realizado:</label> 
					</td>
					<td class="componente" >
  						<div id="qtdeRealizado"></div>
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