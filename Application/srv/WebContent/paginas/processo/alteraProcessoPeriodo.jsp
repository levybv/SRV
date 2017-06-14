<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

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

		for ( var i=0 ; i < listaAtualizar.length ; i++) {

			var chave = listaAtualizar[i].value;
			ano = chave.substring(0, chave.indexOf(";"));
			chave = chave.substring(chave.indexOf(";")+1);
			mes = chave.substring(0, chave.indexOf(";"));
			chave = chave.substring(chave.indexOf(";")+1);
			status = chave.substring(0, chave.indexOf(";"));
			chave = chave.substring(chave.indexOf(";")+1);
			periodo = chave.substring(0, chave.indexOf(";"));
			chave = chave.substring(chave.indexOf(";")+1);
			idProcesso = chave.substring(0, chave.indexOf(";"));
			chave = chave.substring(chave.indexOf(";")+1);
			descricaoProcesso = chave;

			if ( listaAtualizar.length == 1 ) {
				$("#statusAlt").val(status);
			}

			$("#chavesAlt").val($("#chavesAlt").val() + idProcesso + "," + mes + "," + ano + ";");
			$("#divProcesso").html($("#divProcesso").html() + descricaoProcesso + " (" + periodo + ")<br/>");

		}

		$("#detMeta").validate({
	    	 rules: {
	    			statusAlt: {required:true}
	     	 },
	      	 messages: {
	      			statusAlt: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
	         },
	         submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "processoPeriodo.do?operacao=alteraProcessosPeriodo";   
	            document.detMeta.submit();
	         }
    	});

		$("#anoFiltro").val(anoFiltro);
		$("#mesFiltro").val(mesFiltro);
		$("#statusAlt").focus();

    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 575px;">
	<form name="detMeta" id="detMeta" method="post"> 
			
			<!--Filtro-->
			<input type='hidden' id="anoFiltro" name="anoFiltro" value=""/>
			<input type='hidden' id="mesFiltro" name="mesFiltro" value=""/>

			<!--Chave-->
			<input type='hidden' id="chavesAlt" name="chavesAlt" value=""/>

			<center><b>Deseja alterar o status para todos os processos selecionados ?</b></center><br/>
			<table class="tabelaComponente">
				<tbody>
					<tr>
						<td class="componente" width="20%">
							<label class="label">Processo(s):</label>
						</td>
						<td class="componente" >
							<div id="divProcesso"></div>
						</td>
					</tr>
					<tr>
						<td class="componente" >
							<label class="label">Status:</label>
						</td>
						<td class="componente" >
							<select id="statusAlt" name="statusAlt" class="campo">
								<option value="" selected>[Selecione]</option>
								<option value="1">Período Aberto</option>
								<option value="2">Período Fechado</option>
								<option value="3">Período Reaberto</option>
								<option value="4">Período para Fechar</option>
							</select>
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