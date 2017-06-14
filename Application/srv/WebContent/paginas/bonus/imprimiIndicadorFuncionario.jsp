<%@ taglib prefix="tiles" uri="http://struts.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>   
<%@ taglib prefix='fn' uri='http://java.sun.com/jsp/jstl/functions' %>

<%  
response.setHeader("Cache-Control","no-cache, expire"); 
response.setHeader("max-age","0");  
response.setHeader("Pragma","no-cache");        
response.setDateHeader ("expires", 0); 
response.setHeader("Last-Modified","Sun, 06 Nov 2012 15:32:08 GMT"); 
%>  

<meta http-equiv="X-UA-Compatible" content="IE=8" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head> 
	<title>Marisa - Imprimir Indicador para Funcionário</title> 
	<link rel="stylesheet" type="text/css" href="../../css/default.css" />
	<link rel="stylesheet" type="text/css" href="../../css/displaytag.css" />
	<link rel="stylesheet" type="text/css" href="../../css/datePicker.css" /> 
	<link rel="stylesheet" type="text/css" href="../../css/menu.css" /> 
	
	<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href="../../css/default-ie6.css" />
		<script src="js/lib/DD_belatedPNG.js" type="text/javascript"></script>
		<script type="text/javascript">
		  DD_belatedPNG.fix('img');
		</script>
	<![endif]-->
	
	<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href="../../css/default-ie7.css" />
	<![endif]-->
	
	<!--[if IE 8]>
		<link rel="stylesheet" type="text/css" href="../../css/default-ie8.css" />
	<![endif]-->
	<link rel="stylesheet" type="text/css" href="../../css/abas.css" />
	<link rel="stylesheet" type="text/css" href="../../css/tipsy.css" />
	<link rel="stylesheet" type="text/css" href="../../css/picklist.css" /> 

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
	<!--[if IE 6]>
		<script type="text/javascript" src="js/ns/neuro.modal.ie6-1.0.js"  charset="UTF8" ></script>
		<script type="text/javascript" src="js/lib/jquery.bgiframe.js"  charset="UTF8" ></script>
		<script type="text/javascript">
			$(document).ready(function(){
				$('#modal').bgiframe({top: 0, left: 0, width: '100%', height: '100%',opacity:true});
				$('#modal-block').bgiframe({top: 0, left: 0, width: '100%', height: '100%',opacity:true});
				$('#critical-msg').bgiframe({top: 0, left: 0, width: '100%', height: '100%',opacity:true});
				$('#miniModal').bgiframe({top: 0, left: 0, width: '100%', height: '100%',opacity:true});
				$('#msg-block').bgiframe({top: 0, left: 0, width: '100%', height: '100%',opacity:true});
				$('.tipsy').bgiframe({top: 0, left: 0, width: '100%', height: '100%',opacity:true});
			});
		</script>
	<![endif]-->
	<!--[if gte IE 7]>
		<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>
	<![endif]-->
	<!--[if !IE]>--> 
		<script type="text/javascript" src="js/ns/neuro.modal-1.0.js"  charset="UTF8" ></script>
	<!--<![endif]-->
	<script type="text/javascript" src="js/lib/jquery.autotab-1.1b.js"  charset="UTF8" ></script>	
	<script type="text/javascript" src="js/lib/datePicker.date.js" charset="UTF8"></script>   
	<script type="text/javascript" src="js/lib/jquery.datePicker.js" charset="UTF8"></script>  
</head> 

<body>
	<div id="header">
		<div id="logo">
			<h1><img class="png_bg" src="../../images/novo-logo.png" /></h1>
			<h3>Titulo</h3>
		</div>
	</div>
</body>
</html>
<script>window.print();</script>