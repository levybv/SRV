<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/AgendamentoBusinessAjax.js'></script>
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

		$("#anoA").numeric();
		$('#dataAgendamentoA').mask("99/99/9999");

		AgendamentoBusinessAjax.obtemAgendamento(codigoCarga,
			function (agendamentoVO) {
				if (agendamentoVO != null) {
					$("#codigoCargaA").val(agendamentoVO.codigoCarga);
					$("#codigoCargaDIV").html(agendamentoVO.codigoCarga);
					$("#descricaoDIV").html(agendamentoVO.descricaoCarga);
					$("#mesA").val(agendamentoVO.mes);
					$("#anoA").val(agendamentoVO.ano);
					$("#dataAgendamentoA").val(agendamentoVO.dataAgendamentoStr);
					$("#flagAtivaA").val(agendamentoVO.flagAtiva);
				}
		});

		$("#detMeta").validate({
	    	 rules: {
	    		anoA: {required:true},
	    		mesA: {required:true},
	    		dataAgendamentoA: {required:true},
	    		flagAtivaA: {required:true}
	    	 },
		     messages: {
		    	 anoA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 mesA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 dataAgendamentoA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
		    	 flagAtivaA: {required: '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'}
		     },
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detMeta.action = "agendamentoCarga.do?operacao=alteraAgendamento";   
		            document.detMeta.submit();
	         }
    	});

		$("#mesA").focus();

    });
</script>

<div id="corpoModalAlterarAgendamentoCarga" style="float:left;margin-left: 10px; width: 570px;">
	<form name="detMeta" id="detMeta" method="post"> 
		<input type="hidden" id="codigoCargaA" name="codigoCargaA" value=""/>
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">Código:</label> 
					</td>
					<td class="componente" >
  						<div id="codigoCargaDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente"  width="20%">
						<label class="label">Descrição:</label> 
					</td>
					<td class="componente" >
  						<div id="descricaoDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente"  width="20%">
						<label class="label">Mês:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="mesA" name="mesA" class="campo">
							<option value="" selected>[SELECIONE]</option>
							<option value="1">Janeiro</option>
							<option value="2">Fevereiro</option>
							<option value="3">Março</option>
							<option value="4">Abril</option>
							<option value="5">Maio</option>
							<option value="6">Junho</option>
							<option value="7">Julho</option>
							<option value="8">Agosto</option>
							<option value="9">Setembro</option>
							<option value="10">Outubro</option>
							<option value="11">Novembro</option>
							<option value="12">Dezembro</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="componente"  width="20%">
						<label class="label">Ano:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
  						<input id="anoA" name="anoA" value="" class="campo2" maxlength="4" style="width: 60px;" value="" type="text"/>
					</td>
				</tr>
				<tr>
					<td class="componente"  width="20%">
						<label class="label">Data Agendamento:<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
 						<input id="dataAgendamentoA" name="dataAgendamentoA" value="" class="campo2" maxlength="10" style="width: 70px;" type="text"/> (dd/mm/aaaa)
					</td>
				</tr>
				<tr>
					<td class="componente"  width="20%">
						<label class="label">Ativa?<span class="requerido">*</span></label> 
					</td>
					<td class="componente" >
						<select id="flagAtivaA" name="flagAtivaA" class="campo">
							<option value="" selected>[SELECIONE]</option>
							<option value="1">Sim</option>
							<option value="0">Não</option>
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