function injectAttributes(target, values) {
	for(var key in values) {
		if(values.hasOwnProperty(key) && typeof values[key] === "object") 
		{
			if(!target.hasOwnProperty(key)) {
				target[key] = {};
			}
			injectAttributes(target[key], values[key]);
		} else {
			target[key] = values[key];
		}
	}
	return target;
}

function fabricElement(type, options) {
	var el = document.createElement(type);
	
	if(options && typeof options === "object") {
		injectAttributes(el, options);
	}
	
	return el;
}

function getAlert(msg, cssClasses, dismiss)
{
	var classes = 'alert';
	
	if (dismiss)
	{
		classes += ' alert-dismissible';
	}
	
	var container = fabricElement("div", {
		className: classes
	});
	
	var closeButton = fabricElement("button", {
		className: "close",
		innerText: "x"
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
		container.appendChild(closeButton);
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
