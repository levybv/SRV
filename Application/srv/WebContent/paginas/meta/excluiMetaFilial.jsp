<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/MetaFilialBusinessAjax.js'></script>
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
		
		MetaFilialBusinessAjax.obtemMetaFilial(mes, ano, idEmpresa, idFilial, idIndicador,
			function (metaFilialVO) {
				$("#idPeriodoMF").val(metaFilialVO.ano + "-" + metaFilialVO.mes);
				$("#idFilialMF").val(metaFilialVO.idFilial);
				$("#idIndicadorMF").val(metaFilialVO.idIndicador);
				$("#divPeriodo").html(metaFilialVO.periodoFormatado);
				$("#divIndicador").html(metaFilialVO.indicadorFormatado);
				$("#divFilial").html(metaFilialVO.filialFormatado);
				$("#divMeta").html(metaFilialVO.valorMetaFormatado);
				$("#divPremio").html(metaFilialVO.valorPremioFilialFormatado);
		}); 				
		
		$("#detMeta").validate({
	    	rules: {},
		    messages: {},
	        submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
		       	document.detMeta.action = "metaFilial.do?operacao=excluiMetaFilial";   
	            document.detMeta.submit();
	        }
    	});
    	
		$("#mesFiltro").val(mesF);
		$("#anoFiltro").val(anoF);
		$("#idIndicadorFiltro").val(idIndicadorF);
		$("#descricaoIndicadorFiltro").val(descricaoIndicadorF);
		$("#idFilialFiltro").val(idFilialF);
		
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 520px;">
	<form name="detMeta" id="detMeta" method="post"> 
			
			<!--Filtro-->
			<input type='hidden' id="mesFiltro"  				name="mesF"   					value=""/>
			<input type='hidden' id="anoFiltro"    				name="anoF"     				value=""/>
			<input type='hidden' id="idIndicadorFiltro"    		name="idIndicadorF"     		value=""/>
			<input type='hidden' id="descricaoIndicadorFiltro"  name="descricaoIndicadorF"     	value=""/>
			<input type='hidden' id="idFilialFiltro"    		name="idFilialF"     			value=""/>
			
			<!--Chave-->
			<input type='hidden' id="idPeriodoMF"  			name="idPeriodoMF"   				value=""/>
			<input type='hidden' id="idFilialMF"    		name="idFilialMF"     				value=""/>
			<input type='hidden' id="idIndicadorMF"    		name="idIndicadorMF"     			value=""/>
			
			<table class="tabelaComponente">
				<tbody> 
					<tr>
						<td class="componente" width="20%">
							<label class="label">Período:</label> 
						</td>
						<td class="componente" >
							<div id="divPeriodo"></div>
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
							<label class="label">Filial:</label> 
						</td>
						<td class="componente" >
							<div id="divFilial"></div>
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
					<tr>
						<td class="componente" >
							<label class="label">Prêmio:</label> 
						</td>
						<td class="componente" >
	  						<div id="divPremio"></div>
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