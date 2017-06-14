<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">  

<head>
<meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
<title>Erro - Sac Mailing</title> 
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
	
</head>
<body>
	<div id="header">
		<div id="logo">
			<h1><img class="png_bg" src="images/credi21.png" /></h1>
			<h2><img class="png_bg" src="images/novo-logo.png" /></h2>
			<h3>Erro - Sac Mailing</h3>
		</div>
	</div> 
	<div id="menu" >
        
	</div>
	
	<div id="content">
		<div class="boxIndexError"> 
			<div class="boxCenterError">	
				<div class="panelErro">
					<div> 
	                    <div id="" class="barTitle"><c:out value="${titulo}"></c:out></div>
	                </div> 
	                	
					<div id="painel-interno" style="font-variant: small-caps;">
					<br/>
						<img src="images/error.png" class="imagem" /> <c:out value="${mensagem}"></c:out>  
						<br/><br/>
							<div class="barTitleInterno">Detalhamento do Erro</div><br />
							<div class="painel-interno" style="font-variant: small-caps; margin-left: 0.9cm">
								<c:out value="${mensagemDetalhamento}"></c:out>     
							</div>
						<br/> 
						<br/>
					</div> 
					
					 <div id="botao-geral">
		                   <div id="botao" class="barBottom"> 
		                     	 
		                   </div>
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