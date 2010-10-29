function log(msg) {
	$("#log").html(msg+"<br/>"+$("#log").html());
}

jQuery.fn.extend({
	findPos : function() {
	obj = jQuery(this).get(0);
	var curleft = obj.offsetLeft || 0;
	var curtop = obj.offsetTop || 0;
	while (obj = obj.offsetParent) {
		curleft += obj.offsetLeft
		curtop += obj.offsetTop
	}
	return {x:curleft,y:curtop};
}
});

jQuery.fn.liScrollClone = function(settings) {
	settings = jQuery.extend({
		travelocity: 0.07
	}, settings);
	return this.each(function(){
		var $strip = jQuery(this);
		$strip.addClass("newsticker")
		var stripWidth = 0;
		var direction = "left";

		var $mask = $strip.wrap("<div class='mask'></div>");
		var $tickercontainer = $strip.parent().wrap("<div class='tickercontainer'></div>");
		var containerWidth = $strip.parent().parent().width();	//a.k.a. 'mask' width
		$mask.css("left", 0)
		$strip.find("li").each(function(i){
			stripWidth += jQuery(this, i).width();
		});
		$strip.width(stripWidth);
		var defTiming = stripWidth/settings.travelocity;
		var totalTravel = stripWidth+containerWidth;
		function scrollnews(spazio, tempo){
			if(direction == 'right') {
				$strip.animate({left: '+='+ spazio}, tempo, "linear", function(){$strip.css("left", containerWidth); scrollnews(totalTravel, defTiming);});
			} else {
				$strip.animate({left: '-='+ spazio}, tempo, "linear", function(){$strip.css("left", containerWidth); scrollnews(totalTravel, defTiming);});
			}
		}
		scrollnews(totalTravel, defTiming);
		$tickercontainer.mousemove(function(event){
			if( window.event)
				event = window.event;
			var x = event.clientX;
			var y = event.clientY;
			pos = $tickercontainer.findPos();
			x = x - pos.x
			x = x - containerWidth / 2 + 10;

			max = containerWidth / 2 + 10;
			min = -max;
			var speed;
			var dirChange = false;

			/*if(x == 0){
				//log("x = 0 -> stop");
				//speed = 0
				//jQuery($strip).stop();
				//return;
			}*/

			if(x < 0 && direction == "left"){
				//log("change dir to right");
				direction = "right";
				dirChange = true;
			} else if(x > 0 && direction == "right"){
				//log("change dir to left");
				direction = "left";
				dirChange = true;
			}

			speed = Math.abs((x / max) / 2);

			if(Math.abs(speed - settings.travelocity) > 0.02 || dirChange == true){
				jQuery($strip).stop();
				var offset = jQuery($strip).offset();
				var residualSpace = offset.left + stripWidth;
				var residualTime = residualSpace/speed;
				//log("residual space : "+residualSpace);
				settings.travelocity = speed;
				scrollnews(residualSpace, residualTime);
			}

		});

		/*$strip.hover(function(){
			jQuery(this).stop();
		},
		function(){
			var offset = jQuery(this).offset();
			var residualSpace = offset.left + stripWidth;
			var residualTime = residualSpace/settings.travelocity;
			scrollnews(residualSpace, residualTime);
		});*/
	});
};