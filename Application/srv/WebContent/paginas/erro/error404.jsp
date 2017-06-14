<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">  

<head>
<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
<title>Erro 404</title> 
	<link rel="stylesheet" type="text/css" href="css/default.css" />
	<link rel="stylesheet" type="text/css" href="css/menu.css" /> 
	<!-- CSS -->
	<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href="css/default-ie6.css" />
	<![endif]-->
	
	<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href="css/default-ie7.css" />
	<![endif]-->
	
	<!--[if IE 8]>
		<link rel="stylesheet" type="text/css" href="css/default-ie8.css" />
	<![endif]-->

	<script type="text/javascript">
		$("#botao-avancar").ready(function() {
			$("#botao-avancar").focus();
		});
	</script>	
</head>

<body>
	<div id="header">
		<div id="logo">
			<h1><img class="png_bg" src="images/credi21.png" /></h1>
			<h2><img class="png_bg" src="images/novo-logo.png" /></h2>
			<h3>Erro no Servidor</h3>
		</div>
	</div> 
	<div id="menu">
        
	</div>
	<div class="boxIndexError">
		<div class="boxCenterError">	
			<div class="panelErro">
				<div> 
                    <div class="barTitle">Marisa - Erro</div>
                </div> 
                	
				<div id="painel-interno" style="font-variant: small-caps;">
				<br/>
					<img src="images/error.png" class="imagem" />Erro no Servidor 
					<br/><br/>
						<div class="barTitleInterno">Erro 404</div><br />
						<div class="painel-interno" style="font-variant: small-caps; margin-left: 0.9cm">
							Operação solicitada pelo usuário não existe.   
						</div>
					<br/> 
					<br/>
				</div> 
				
				 <div id="botao-geral">
	                   <div id="botao" class="barBottom"> 
	                     	<input style="text-decoration: none;" id="botao-avancar" type="button" class="button" value="Finalizar" onclick="window.location='paginas/layout/menuCCM.jsp';" /> 
	                   </div>
	              </div> 
	         </div> 	
        </div> 
	</div>        
</body>
</html>
<!--[if IE 6]>
		<script src="js/lib/DD_belatedPNG.js" type="text/javascript"></script>
		<script type="text/javascript">
		  DD_belatedPNG.fix('img');
		</script>
	<![endif]-->