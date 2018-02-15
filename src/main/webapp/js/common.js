// Powered by Koshka!!!

function removeOverlay()
{
	$("#overlay").remove();
}

function makeOverlay()
{	
	$("body").prepend("<div id='overlay'></div>");

	$("#overlay").css({
		"background-color": "grey",
		"position": "absolute", 
		"width": $(document).width(), 
		"height": $(document).height(),
		"z-index": 99999
	}).fadeTo(0, 0.4);		
}

function getAlert(msg, cssClasses, dismiss)
{
	var classes = 'alert';
	
	if (dismiss)
	{
		classes += ' alert-dismissible';
	}
	
	var container = jQuery("<div/>", {
		class: classes
	});
	
	var closeButton = jQuery("<button/>", {
		class: "close",
		text: "x"
	});
	
	$(closeButton).attr('type', 'button');
	$(closeButton).attr('data-dismiss', 'alert');
	$(closeButton).attr('aria-label', 'Close');
	$(container).attr('role', 'alert');
	
	cssClasses.forEach((item) => {
		$(container).addClass(item);
	});
	
	if (dismiss)
	{
		$(container).append(closeButton);
	}
	
	$(container).append(msg);
	
	return container;
}

function getSuccess(msg, dismiss = true) 
{
	return getAlert(msg, ['alert-success'], dismiss);
}

function getFailed(msg, mark = false, dismiss = true) {
	var classes = ['alert-danger'];

	if (mark)
	{
		classes.push('spanMark');
	}
	
	return getAlert(msg, classes, dismiss);
}

function getAjaxError(data, textStatus)
{	
	var message = "";
	
	switch(data.code)
	{
		case 404: 
			message = "Page not found";
			break;
		default:
			if (data.responseJSON && data.responseJSON.message) {
				message = data.responseJSON.message;
			} else if (data.responseJSON && data.responseJSON.error) {
				message = data.responseJSON.error;				
			} else if (data.responseText) {
				message = data.responseText;
			} else if (data.status !== 200 && data.statusText) {
				message = data.statusText;
			} else {
				message = textStatus;
			}
	}
	
	var msg = getFailed(message);
	
	return msg;
}
