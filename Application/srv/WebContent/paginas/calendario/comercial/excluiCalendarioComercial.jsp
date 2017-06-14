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

		CalendarioComercialBusinessAjax.obtemCalendarioComercial(idCalComerc, anoCalComerc,
			function (returnVO) {
				if (returnVO != null) {
					$("#idPeriodoDIV").html(returnVO.periodo);
					$("#idPeriodoE").val(returnVO.periodo);
					$("#anoDIV").html(returnVO.ano);
					$("#anoE").val(returnVO.ano);
					$("#mesDIV").html(returnVO.mesStr);
					$("#dataFinalDIV").html(returnVO.dataFinalStr);
					$("#dataInicialDIV").html(returnVO.dataInicialStr);
				}
		});

		$("#detMeta").validate({
	    	 rules: {},
		     messages: {},
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detMeta.action = "calendarioComercial.do?operacao=excluiCalendarioComercial";   
		            document.detMeta.submit();
	         }
    	});

		$("#anoSelecionadoFiltro").val(anoSelecionadoFiltro);
		$("#anoSelecionadoF").val(anoSelecionadoF);
    });

</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 575px;">
	<form name="detMeta" id="detMeta" method="post">
		<input type="hidden" id="anoSelecionadoFiltro" name="anoSelecionadoFiltro" value=""/>
		<input type="hidden" id="anoSelecionadoF" name="anoSelecionadoF" value=""/>
		<input type="hidden" id="idPeriodoE" name="idPeriodoE" value=""/>
		<input type="hidden" id="anoE" name="anoE" value=""/>
		<center>Confirmar a exclusão do calendário comercial?</center>
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente"  width="20%">
						<label class="label">Período:</label> 
					</td>
					<td class="componente" >
  						<div id="idPeriodoDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Ano:</label> 
					</td>
					<td class="componente" >
						<div id="anoDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Mês de Referência:</label> 
					</td>
					<td class="componente" >
						<div id="mesDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Data Inicial:</label> 
					</td>
					<td class="componente" >
						<div id="dataInicialDIV"></div>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Data Final:</label> 
					</td>
					<td class="componente" >
						<div id="dataFinalDIV"></div>
					</td>
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