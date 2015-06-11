$(document).ready(function() {
	// syntax coloring
	$('pre > code').filter(function() {
			return !($(this).attr('class') === undefined);
		}
	).each(function() {
			$(this).attr('class', $(this).attr('class').replace('language','lang'));
			$(this).addClass('prettyprint')
//			$(this).addClass('linenums')
	})
	prettyPrint();
});