<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   

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
		
		if (tipoFilialSimul == 2) {
			$("#label_Vendas").hide();
			$("#simuladoresPPR_Vendas1").hide();
			$("#simuladoresPPR_Vendas2").hide();
			$("#simuladoresPPR_Vendas3").hide();
			$("#simuladoresPPR_Vendas4").hide();
			$("#simuladoresPPR_Vendas5").hide();
			$("#label_Ling").show();
			$("#simuladoresPPR_Ling1").show();
			$("#simuladoresPPR_Ling2").show();
			$("#simuladoresPPR_Ling3").show();
			$("#label_PSF").hide();
			$("#simuladoresPPR_PSF1").hide();
			$("#simuladoresPPR_PSF2").hide();
			$("#simuladoresPPR_PSF3").hide();
		} else {
			$("#label_Vendas").show();
			$("#simuladoresPPR_Vendas1").show();
			$("#simuladoresPPR_Vendas2").show();
			$("#simuladoresPPR_Vendas3").show();
			$("#simuladoresPPR_Vendas4").show();
			$("#simuladoresPPR_Vendas5").show();
			$("#label_Ling").hide();
			$("#simuladoresPPR_Ling1").hide();
			$("#simuladoresPPR_Ling2").hide();
			$("#simuladoresPPR_Ling3").hide();
			$("#label_PSF").show();
			$("#simuladoresPPR_PSF1").show();
			$("#simuladoresPPR_PSF2").show();
			$("#simuladoresPPR_PSF3").show();
		}
    });
	
</script>

<div id="corpoModalAlterarConfigFilial" style="float:left;margin-left: 10px; width: 550px;">
	<form name="detMeta" id="detMeta" method="post" enctype="multipart/form-data"> 
			
			<br/>
			<b id="label_Ling">- Cargos Marisa Lingerie<br/><br/></b>
			<a id="simuladoresPPR_Ling1" href="paginas/resultadoLoja/simuladores/ling_gervendasjr.ods"><b>Gerentes de Vendas JR</b><br/></a>
			<a id="simuladoresPPR_Ling2" href="paginas/resultadoLoja/simuladores/ling_gervendaspl.ods"><b>Gerentes de Vendas PL</b><br/><br/></a>
			<a id="simuladoresPPR_Ling3" href="paginas/resultadoLoja/simuladores/ling_lideroperloja.ods"><b>Líder de Oper. Loja</b><br/><br/></a>
			<b id="label_Vendas">- Cargos de Vendas<br/><br/></b>
			<a id="simuladoresPPR_Vendas1" href="paginas/resultadoLoja/simuladores/vendas_gervendasjr.ods"><b>Gerentes de Vendas JR</b><br/></a>
			<a id="simuladoresPPR_Vendas2" href="paginas/resultadoLoja/simuladores/vendas_gervendaspl.ods"><b>Gerentes de Vendas PL</b><br/></a>
			<a id="simuladoresPPR_Vendas3" href="paginas/resultadoLoja/simuladores/vendas_gervendassr.ods"><b>Gerentes de Vendas SR</b><br/></a>
			<a id="simuladoresPPR_Vendas4" href="paginas/resultadoLoja/simuladores/vendas_supvendasadm.ods"><b>Sup. de Vendas e Administrativos</b><br/></a>
			<a id="simuladoresPPR_Vendas5" href="paginas/resultadoLoja/simuladores/vendas_assistadm.ods"><b>Assist. Administrativos JR PL e SR</b><br/></a>
			<br/>
			<b id="label_PSF">- Cargos de PSF<br/><br/></b>
			<a id="simuladoresPPR_PSF1" href="paginas/resultadoLoja/simuladores/psf_lidervendaspdv.ods"><b>Líder de Vendas em PDV</b><br/></a>
			<a id="simuladoresPPR_PSF2" href="paginas/resultadoLoja/simuladores/psf_supservcliente.ods"><b>Sup. de Serv ao Cliente</b><br/></a>
			<a id="simuladoresPPR_PSF3" href="paginas/resultadoLoja/simuladores/psf_supservclientesr.ods"><b>Sup. de Serv ao Cliente SR</b><br/><br/></a>
			<br/>
		<div id="botao-geral" style="width:100%;">
			<div id="botao" class="barBottom" style="width:100%;"> 			
				<div class="div-botao" style="float:right;width:100%;">
					<input id="botaoCancelar" type="button" class="button" value="Cancelar" />					
				</div>
			</div>
		</div>
</form>
</div>