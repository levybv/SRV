<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/CalendarioBonusBusinessAjax.js'></script>
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

		CalendarioBonusBusinessAjax.obtemCalendarioBonus(mesBonus, anoBonus,
			function (returnVO) {
				if (returnVO != null) {
					$("#mesDIV").html(returnVO.mesStr);
					$("#anoDIV").html(returnVO.ano);
					$("#anoE").val(returnVO.ano);
					$("#mesE").val(returnVO.mes);
					$("#mesDescrDIV").html(returnVO.descricaoMes);
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
			       	document.detMeta.action = "calendarioBonus.do?operacao=excluiCalendarioBonus";   
		            document.detMeta.submit();
	         }
    	});

		$("#anoFiltro").val(anoFiltro);
		$("#mesFiltro").val(mesFiltro);
		$("#mesDescrFiltro").val(mesDescrFiltro);
		$("#dataInicialFiltro").val(dataInicialFiltro);
		$("#dataFinalFiltro").val(dataFinalFiltro);

	});

</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 575px;">
	<form name="detMeta" id="detMeta" method="post">

		<input type="hidden" id="anoFiltro" name="anoFiltro" value=""/>
		<input type="hidden" id="mesFiltro" name="mesFiltro" value=""/>
		<input type="hidden" id="mesDescrFiltro" name="mesDescrFiltro" value=""/>
		<input type="hidden" id="dataInicialFiltro" name="dataInicialFiltro" value=""/>
		<input type="hidden" id="dataFinalFiltro" name="dataFinalFiltro" value=""/>

		<input type="hidden" id="anoE" name="anoE" value=""/>
		<input type="hidden" id="mesE" name="mesE" value=""/>

		<center>Confirmar a exclusão do calendário de bônus?</center>
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente"  width="20%">
						<label class="label">Ano:</label> 
					</td>
					<td class="componente" >
						<div id="anoDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente"  width="20%">
						<label class="label">Mês:</label> 
					</td>
					<td class="componente" >
						<div id="mesDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Descrição Mês:</label> 
					</td>
					<td class="componente" >
						<div id="mesDescrDIV"></div>
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