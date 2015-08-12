(function($){

	$(document).ready(function() {
		$.liquidiframe();
	});

	$.liquidiframe = function(el, options){
		var base = this;
		base.$el = $(el);
		base.el = el,
		base.timer;

		base.$el.data("liquidiframe", base);
		
		base.init = function(){

			$(window).on('resize', function() {
				$("iframe").each(function(index) {
					var $this = $(this).contents(),
						height = $('.countdown', $this).outerHeight();

					if (!height) {
						base.timer = window.setInterval(function() {
							$(window).trigger('resize');
						}, 100);
					} else {
						clearInterval(base.timer);
					}

					$(this).height(height + 'px');
				});
			}).trigger('resize');

		};
		
		base.init();
	};
	
	$.liquidiframe.defaultOptions = {};
	
	$.fn.liquidiframe = function(options){
		return this.each(function(){
			(new $.liquidiframe(this, options));
		});
	};
	
})(jQuery);