<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/CalendarioComercialBusinessAjax.js'></script>
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

		$("#divMesAno").html($("#periodos option:selected").text());
		$("#divMatricula").html($("#idFuncionarioSelecionado").val());
		$("#divNome").html($("#nomeFuncionarioSelecionado").val());
		$("#divFilial").html($("#idFilialSelecionada option:selected").text());

		$("#periodoFiltroM").val($("#periodos").val());
		$("#idFuncionarioSelecionadoM").val($("#idFuncionarioSelecionado").val());
		$("#nomeFuncionarioSelecionadoM").val($("#nomeFuncionarioSelecionado").val());
		$("#cargoFuncionarioSelecionadoM").val($("#cargoFuncionarioSelecionado").val());
		$("#centroCustoFuncionarioSelecionadoM").val($("#centroCustoFuncionarioSelecionado").val());		
		$("#idFilialSelecionadaM").val($("#idFilialSelecionada").val());

		$("#detMeta").validate({
	    	rules: {
	    		novoStatus: {required:true}
	    	},
	     	messages: {
	     		novoStatus: {required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	     	},
	        submitHandler : function(form){
		        $("#div-botao").hide();
	           	$("#div-load").show();
		      	$("#botaoSalvar").attr("disabled", true);
	           	$("#botaoSalvar").val("Aguarde...");	           	
		      	$("#botaoCancelar").hide();
				$(document).unbind('keydown', 'esc');
		       	document.detMeta.action = "bonus.do?operacao=alteraStatusCalculoBonus";
	            document.detMeta.submit();
	        }
    	});

    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 550px;">
	<form name="detMeta" id="detMeta" method="post"> 
			
			<!--Filtro-->
		<input type='hidden' id="periodoFiltroM"  					 name="periodos"   							value=""/>
		<input type='hidden' id="idFuncionarioSelecionadoM"    		 name="idFuncionarioSelecionado"     		value=""/>
		<input type='hidden' id="nomeFuncionarioSelecionadoM"    	 name="nomeFuncionarioSelecionado"    		value=""/>
		<input type='hidden' id="cargoFuncionarioSelecionadoM"    	 name="cargoFuncionarioSelecionado"    		value=""/>
		<input type='hidden' id="centroCustoFuncionarioSelecionadoM" name="centroCustoFuncionarioSelecionado" 	value=""/>
		<input type='hidden' id="idFilialSelecionadaM"				 name="idFilialSelecionada"					value=""/>

			<table class="tabelaComponente">
				<tbody> 
					<tr>
						<td class="componente" width="20%">
							<label class="label">Mês/Ano:</label> 
						</td>
						<td class="componente" width="80%">
	  						<div id="divMesAno"></div>
						</td>
					</tr>
					<tr>
						<td class="componente" width="20%">
							<label class="label">Matr&iacute;cula:</label> 
						</td>
						<td class="componente" width="80%">
	  						<div id="divMatricula"></div>
						</td>
					</tr>
					<tr>
						<td class="componente" width="20%">
							<label class="label">Nome:</label> 
						</td>
						<td class="componente" width="80%">
	  						<div id="divNome"></div>
						</td>
					</tr>
					<tr>
						<td class="componente" width="20%">
							<label class="label">Filial:</label> 
						</td>
						<td class="componente" width="80%">
	  						<div id="divFilial"></div>
						</td>
					</tr>
					<tr>
						<td class="componente" width="20%">
							<label class="label">Novo Status:</label><span class="requerido">*</span>
						</td>
						<td class="componente" width="80%">
							<select id="novoStatus" name="novoStatus" class="campo">
                            	<option value="" selected>[SELECIONE]</option>
                            	<option value="1">Iniciado</option>
                            	<option value="2">Aceite</option>
                            	<option value="3">Andamento</option>
                            	<option value="9">Finalizado</option>
                            </select>
						</td>
					</tr>				
				</tbody>
			</table>
			<br/>
		<div id="botao-geral" style="width:98%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Confirmar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
		<br/>
</form>
</div>