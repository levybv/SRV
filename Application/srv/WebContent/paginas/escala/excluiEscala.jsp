<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

<script type='text/javascript' src='srvdwr/interface/EscalaBusinessAjax.js'></script>
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
		
		EscalaBusinessAjax.obtemEscala(idEscala,
			function (escalaVO) {
				if (escalaVO != null) {
					$("#idEscalaES").val(escalaVO.idEscala);
					$("#descricaoDIV").html(escalaVO.descricaoEscala);
					if (escalaVO.idIndicador != null) {
						$("#indicadorDIV").html(escalaVO.idIndicador + " - " + escalaVO.descricaoIndicador);
					} else {
						$("#indicadorDIV").html("n/a");
					}
					if (escalaVO.idGrupoIndicador != null) {
						$("#grupoIndicadorDIV").html(escalaVO.idGrupoIndicador + " - " + escalaVO.descricaoGrupoIndicador);
					} else {
						$("#grupoIndicadorDIV").html("n/a");
					}
					if (escalaVO.idGrupoRemVar != null) {
						$("#grupoRemVarDIV").html(escalaVO.idGrupoRemVar + " - " + escalaVO.descricaoGrupoRemVar);
					} else {
						$("#grupoRemVarDIV").html("n/a");
					}
					$("#percentualDIV").html(escalaVO.percentualFormatado);	
					$("#limiteDIV").html(escalaVO.limiteFormatado);					
				}
		});
		
		$("#detMeta").validate({
	    	 rules: {},
		     messages: {},
	         submitHandler : function(form){
			        $("#div-botao").hide();  
		           	$("#div-load").show(); 
			       	document.detMeta.action = "escala.do?operacao=excluiEscala";   
		            document.detMeta.submit();
	         }
    	});
    	
		$("#idEscalaFiltro").val(idEscalaF);
		$("#descricaoFiltro").val(descricaoF);
		
    });
    
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 620px;">
	<form name="detMeta" id="detMeta" method="post"> 
			
		<!--Filtro-->
		<input type='hidden' id="idEscalaFiltro"  	 name="idEscalaF"   value=""/>
		<input type='hidden' id="descricaoFiltro"    name="descricaoF"  value=""/>
		
		<!--Chave-->
		<input type='hidden' id="idEscalaES"   name="idEscalaES"   value=""/>
		
		<table class="tabelaComponente">
			<tbody> 
				<tr>
					<td class="componente" width="20%">
						<label class="label">Descrição:</label> 
					</td>
					<td class="componente" >
						<div id="descricaoDIV"></div>
					</td>
				</tr>			
				<tr>
					<td class="componente" >
						<label class="label">Indicador:</label> 
					</td>
					<td class="componente" >
						<div id="indicadorDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Grupo Indicador:</label> 
					</td>
					<td class="componente" >
						<div id="grupoIndicadorDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Grupo Rem. Var.:</label> 
					</td>
					<td class="componente" >
						<div id="grupoRemVarDIV"></div>
					</td>
				</tr>
				<tr>
					<td class="componente" >
						<label class="label">Percentual 100%:</label> 
					</td>
					<td class="componente" >
						<div id="percentualDIV"></div>
					</td>
				</tr>	
				<tr>
					<td class="componente" >
						<label class="label">Limite:</label> 
					</td>
					<td class="componente" >
						<div id="limiteDIV"></div>
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