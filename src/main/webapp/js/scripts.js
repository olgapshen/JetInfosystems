/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function createContract()
{
	var url = "contract/";
	
	$.ajax({
        type: "POST",
        url: url,
		contentType: "application/json",
		error: (data, textStatus) =>
		{
			var msg = getAjaxError(data, textStatus);
			$("#messages").html(msg);
		},
		success: (data) =>
		{
			var msg = getSuccess(data.name + " created");
			$("#messages").html(msg);
		}
    });
}

function listContracts()
{
	var url = "contract/list";
	
	$.ajax({
        type: "GET",
        url: url,
		contentType: "application/json",
		error: (data, textStatus) =>
		{
			var msg = getAjaxError(data, textStatus);
			$("#messages").html(msg);
		},
		success: (data) =>
		{
			//var msg = getSuccess(data.name + " created");
			//$("#messages").html(msg);
		}
    });
}