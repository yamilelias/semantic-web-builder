$().ready(function() {
    $("#btn_sml_tex").click(translate);
    $("#btn_translate").click(showMath);
});

function translate(ev)
{
	// Ejecutar
	var params = {};
	params.text = $("#sml_text").val();
	sendAjax("translate", params, translateOk, translateError);
}

function translateOk(data)
{
	if (data.error) alert(data.error);
	else $("#math_text").val(data.tex);
}

function translateError()
{
	alert("Error!!");
}

function showMath()
{
	var mathText = $("#math_text").val();
	var parts = mathText.split("\n");
	var result = "";
	for (var i = 0; i < parts.length; ++i)
	{
		result += "$$" + parts[i] + "$$\n";
	}
	$("#tex_text").html(result);
	MathJax.Hub.Queue(["Typeset",MathJax.Hub,"tex_text"]);
}

//----------------------
//Función de enviar ajax
//----------------------

function sendAjax(ajaxUrl, params, ajaxSuccessGeneric, ajaxErrorGeneric)
{
	ajaxQuery = $.ajax({
		timeout: 10000,
		type: "POST",
		contentType: "application/x-www-form-urlencoded; charset=utf-8",
		url: ajaxUrl,
		error: ajaxErrorGeneric,
		success: ajaxSuccessGeneric,
		data: params,
		cache: false,
		crossDomain: true
	});			
}