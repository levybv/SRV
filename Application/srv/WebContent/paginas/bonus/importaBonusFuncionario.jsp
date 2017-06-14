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
		
		$("#detMeta").validate({
	    	rules: {
	    		periodoImport: 		{required:true},
	    		arquivoUpload: 		{required:true,
	    							 accept:"xls"}
	    	},
	     	messages: {
	     		periodoImport:  		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>'},
	     		arquivoUpload:  		{required:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Campo obrigat&oacute;rio.</span>',
	     								 accept:	   '<span class="erro-ini"><img src="images/ico_error.gif" class="imagem" /> Envie somente arquivos com extens&atilde;o XLS.</span>'}
	     	},
	        submitHandler : function(form){
		        $("#div-botao").hide();  
	           	$("#div-load").show(); 
	           	
		      	$("#botaoSalvar").attr("disabled", true);
	           	$("#botaoSalvar").val("Aguarde...");	           	

		      	$("#botaoCancelar").hide();
		      	
				$(document).unbind('keydown', 'esc');
	           	
		       	document.detMeta.action = "servlet/uploadBonusFuncionarioServlet";
	            document.detMeta.submit();
	        }
    	});

        CalendarioComercialBusinessAjax.obtemListaPeriodoMesAno( 
   			function (periodo) {
   				if (periodo != null) {
   					var options = $('#periodoImport').attr('options');
   					for (var i=0; i<periodo.length; i++) {
   						var periodoVO = periodo[i];
   						options[options.length] = new Option(periodoVO.periodoMesAno, periodoVO.mesAno, false, false);
   						
   					}
   				}
   				
   		});

		$("#mesFiltro").val(mesF);
		$("#anoFiltro").val(anoF);
		$("#idIndicadorFiltro").val(idIndicadorF);
		$("#descricaoIndicadorFiltro").val(descricaoIndicadorF);
		$("#idFilialFiltro").val(idFilialF);
		$("#acompMetaFilialFiltro").val($("#acompMetaFilial").val());
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 550px;">
	<form name="detMeta" id="detMeta" method="post" enctype="multipart/form-data"> 
			
			<!--Filtro-->
			<input type='hidden' id="mesFiltro"  				name="mesF"   					value=""/>
			<input type='hidden' id="anoFiltro"    				name="anoF"     				value=""/>
			<input type='hidden' id="idIndicadorFiltro"    		name="idIndicadorF"     		value=""/>
			<input type='hidden' id="descricaoIndicadorFiltro"  name="descricaoIndicadorF"     	value=""/>
			<input type='hidden' id="idFilialFiltro"    		name="idFilialF"     			value=""/>
			<input type='hidden' id="acompMetaFilialFiltro"    	name="acompMetaFilialF"     	value=""/>
			
			<table class="tabelaComponente">
				<tbody> 
					<tr>
						<td class="componente" width="100%">
							<label class="label">Per&iacute;odo:</label>
							<select id="periodoImport" name="periodoImport" class="campo">
                            	<option value="" selected>[SELECIONE]</option>
                            </select>
						</td>
					</tr>				
					<tr>
						<td class="componente" width="100%">
							<label class="label">Arquivo:</label>
							<input type="file" class="campo2" name="arquivoUpload" id="arquivoUpload" size="70" accept="application/vnd.ms-excel"/>
						</td>
					</tr>				
				</tbody>
			</table>
			<br/>
			<span class="requerido"><b>Layout do Arquivo</b></span><br/>
			<b>Tipo:</b> Arquivo XLS. Linhas 1, 2, 3 e 4 s&atilde;o ignoradas por serem consideradas cabe&ccedil;alho. O sistema importa apenas a primeira aba da planilha (Certifique-se de n&atilde;o haver planilhas ocultas, elas s&atilde;o consideradas).<br/>
			<br/>
			<b>Coluna D:</b> Matr&iacute;cula do colaborador<br/>
			<b>Coluna E:</b> Nome e unidade de medida do indicador<br/>
			<b>Coluna F:</b> Sentido do indicador<br/>
			<b>Coluna G:</b> Peso do indicador<br/>
			<b>Coluna H:</b> Escala do indicador<br/>
			<b>Coluna I:</b> F&oacute;rmula do indicador<br/>
			<b>Coluna N:</b> Meta ano do indicador<br/><br/>
			<b>Coluna AB:</b> Or&ccedil;amento JANEIRO do indicador (per&iacute;odo)<br/>
			<b>Coluna AC:</b> Or&ccedil;amento FEVEREIRO do indicador (per&iacute;odo)<br/>
			<b>Coluna AD:</b> Or&ccedil;amento MAR&Ccedil;O do indicador (per&iacute;odo)<br/>
			<b>Coluna AE:</b> Or&ccedil;amento ABRIL do indicador (per&iacute;odo)<br/>
			<b>Coluna AF:</b> Or&ccedil;amento MAIO do indicador (per&iacute;odo)<br/>
			<b>Coluna AG:</b> Or&ccedil;amento JUNHO do indicador (per&iacute;odo)<br/>
			<b>Coluna AH:</b> Or&ccedil;amento JULHO do indicador (per&iacute;odo)<br/>
			<b>Coluna AI:</b> Or&ccedil;amento AGOSTO do indicador (per&iacute;odo)<br/>
			<b>Coluna AJ:</b> Or&ccedil;amento SETEMBRO do indicador (per&iacute;odo)<br/>
			<b>Coluna AK:</b> Or&ccedil;amento OUTUBRO do indicador (per&iacute;odo)<br/>
			<b>Coluna AL:</b> Or&ccedil;amento NOVEMBRO do indicador (per&iacute;odo)<br/><br/>
			<b>Coluna BB:</b> Realizado JANEIRO do indicador (per&iacute;odo)<br/>
			<b>Coluna BC:</b> Realizado FEVEREIRO do indicador (per&iacute;odo)<br/>
			<b>Coluna BD:</b> Realizado MAR&Ccedil;O do indicador (per&iacute;odo)<br/>
			<b>Coluna BE:</b> Realizado ABRIL do indicador (per&iacute;odo)<br/>
			<b>Coluna BF:</b> Realizado MAIO do indicador (per&iacute;odo)<br/>
			<b>Coluna BG:</b> Realizado JUNHO do indicador (per&iacute;odo)<br/>
			<b>Coluna BH:</b> Realizado JULHO do indicador (per&iacute;odo)<br/>
			<b>Coluna BI:</b> Realizado AGOSTO do indicador (per&iacute;odo)<br/>
			<b>Coluna BJ:</b> Realizado SETEMBRO do indicador (per&iacute;odo)<br/>
			<b>Coluna BK:</b> Realizado OUTUBRO do indicador (per&iacute;odo)<br/>
			<b>Coluna BL:</b> Realizado NOVEMBRO do indicador (per&iacute;odo)<br/>
			<b>Coluna BM:</b> Realizado DEZEMBRO do indicador<br/><br/>
			<b>Coluna BO:</b> Marcar com "X" para ignorar linha<br/>
			<br/>
		<div id="botao-geral" style="width:98%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoSalvar" type="submit" class="button" value="Importar" />
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
		<br/>
</form>
</div>