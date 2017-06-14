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

		$("#formRepPeriodo").validate({
	    	 rules: {},
	      	 messages: {},
	         submitHandler : function(form){
		        fecharModal();
		        alert("Reprocessamento chamado com sucesso.");
		       	document.form.action = "processoPeriodo.do?operacao=reprocessaPeriodo";
	            document.form.submit();
	         }
    	});

    });
	
</script>

<div id="corpoModalReprocessaAgendamento" style="float:left;margin-left: 10px; width: 575px;">
	<form name="formRepPeriodo" id="formRepPeriodo" method="post"> 
			<br>
			<center><b>Deseja reprocessar o período cadastrado at&eacute; o momento ?</b></center><br/>
			<table class="tabelaComponente">
				<tbody>
					<tr>
					</tr>
				</tbody>
			</table>
			<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Sim" />
					<input id="botaoCancelar" type="button" class="button" value="Não" />					
				</div>
			</div>
		</div>
</form>
</div>