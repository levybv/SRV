<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script type='text/javascript' src='srvdwr/interface/IndicadorBusinessAjax.js'></script>
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
		
		$("#detIndicador").validate({
	    	 rules: {},
	     	 messages: {},
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detIndicador.action = "bonusAnualFuncionario.do?operacao=excluiIndicadorFuncionario";   
	            document.detIndicador.submit();
	         }
    	});		
    	
		//Dados da tela atual
		$("#periodoFiltroM").val($("#periodoAtual").val());
		$("#idFuncionarioSelecionadoM").val($("#idFuncionarioSelecionado").val());
		$("#nomeFuncionarioSelecionadoM").val($("#nomeFuncionarioSelecionado").val());
		$("#cargoFuncionarioSelecionadoM").val($("#cargoFuncionarioSelecionado").val());
		$("#centroCustoFuncionarioSelecionadoM").val($("#centroCustoFuncionarioSelecionado").val());		

		//Dados da tela anterior
		$("#idFuncionarioM").val($("#idFuncionario").val());
		$("#nomeFuncionarioM").val($("#nomeFuncionario").val());
		$("#crachaM").val($("#cracha").val());
		$("#cpfFuncionarioM").val($("#cpfFuncionario").val());		

		$("#divMesAno").html($("#periodoAtual").val());
		$("#idFuncionarioID").val($("#idFuncionarioSelecionado").val());
		$("#divFuncionario").html($("#idFuncionarioSelecionado").val() + " - " + $("#nomeFuncionarioSelecionado").val());    	    	
    	
		IndicadorBusinessAjax.obtemRealizadoIndicadorBonus($('input[type=radio]:checked').val(),
			function (indicadorRealizado) {
				if (indicadorRealizado != null) {
					$("#divIndicador").html(indicadorRealizado.idIndicador + " - " + indicadorRealizado.descricaoIndicador);
					$("#idIndicadorID").val(indicadorRealizado.idIndicador);
					$("#divPeso").html(indicadorRealizado.pesoFormatado);
					$("#divMeta").html(indicadorRealizado.metaFormatada);
					$("#idEmpresaID").val(indicadorRealizado.idEmpresa);
					$("#idFilialID").val(indicadorRealizado.idFilial);
				}
		});		
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 570px">
	<form name="detIndicador" id="detIndicador" method="post"> 
	
		<!--Dados da tela atual-->
		<input type='hidden' id="periodoFiltroM"  					 name="periodos"   							value=""/>
		<input type='hidden' id="idFuncionarioSelecionadoM"    		 name="idFuncionarioSelecionado"     		value=""/>
		<input type='hidden' id="nomeFuncionarioSelecionadoM"    	 name="nomeFuncionarioSelecionado"    		value=""/>
		<input type='hidden' id="cargoFuncionarioSelecionadoM"    	 name="cargoFuncionarioSelecionado"    		value=""/>
		<input type='hidden' id="centroCustoFuncionarioSelecionadoM" name="centroCustoFuncionarioSelecionado" 	value=""/>

		<!--Dados da tela anterior-->
		<input type='hidden' id="idFuncionarioM"    	name="idFuncionario"     		value=""/>
		<input type='hidden' id="nomeFuncionarioM"   	name="nomeFuncionario"    		value=""/>
		<input type='hidden' id="crachaM"    			name="cracha"    				value=""/>
		<input type='hidden' id="cpfFuncionarioM"  		name="cpfFuncionario" 			value=""/>
		
		<!--Dados para efetivacao-->		
		<input type='hidden' id="idFuncionarioID"       name="idFuncionarioID"     		value=""/>
		<input type='hidden' id="anoID"    				name="anoID"     				value=""/>
		<input type='hidden' id="mesID"   				name="mesID"     				value=""/>		
		<input type='hidden' id="idIndicadorID"    		name="idIndicadorID"    		value=""/>
		<input type='hidden' id="idEmpresaID"    		name="idEmpresaID"    			value=""/>
		<input type='hidden' id="idFilialID"    		name="idFilialID"    			value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">Mês/Ano:</label> 
					</td>
					<td class="componente" >
  						<div id="divMesAno"></div>
					</td>	
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Funcionário:</label> 
					</td>
					<td class="componente" >
  						<div id="divFuncionario"></div>
					</td>	
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Indicador:</label> 
					</td>
					<td class="componente" >
						<div id="divIndicador"></div>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Peso:</label> 
					</td>
					<td class="componente" >
  						<div id="divPeso"></div>
					</td>	
				</tr>				
				<tr>
					<td class="componente" >
						<label class="label">Meta:</label> 
					</td>
					<td class="componente" >
  						<div id="divMeta"></div>
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