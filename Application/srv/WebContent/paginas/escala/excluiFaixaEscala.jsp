<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/FaixaEscalaBusinessAjax.js'></script>
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
		
		$("#sequencialDIV").html(sequencial);
		$("#sequencial").val(sequencial);
		$("#faixaInicialDIV").html(faixaInicialFormatada);
		$("#faixaFinalDIV").html(faixaFinalFormatada);
		$("#realizadoDIV").html(realizadoFormatado);		
		
		$("#detMeta").validate({
	    	 rules: {},
		     messages: {},
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detMeta.action = "faixaEscala.do?operacao=excluiFaixaEscala";   
		            document.detMeta.submit();
	         }
    	});
    	
		$("#idEscalaFiltro").val(idEscalaF);
		    	
    });
    
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 450px">
	<form name="detMeta" id="detMeta" method="post"> 
			
		<!--Filtro-->
		<input type='hidden' id="idEscalaFiltro"  	 name="idEscalaF"    value=""/>
		<input type='hidden' id="sequencial"  	 	 name="sequencial"   value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="30%">
						<label class="label">Sequência:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<div id="sequencialDIV"></div>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Faixa Inicial:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<div id="faixaInicialDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Faixa Final:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<div id="faixaFinalDIV"></div>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Realizado:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<div id="realizadoDIV"></div>
					</td>
				</tr>								
			</tbody>
		</table>
		<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Excluir" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
	</form>
</div>