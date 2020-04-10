$(function () {
	  // セレクトボックスが切り替わったら発動
	  $('.search').change(function()  {
		  var search_val = $('.search').val();
		  if(search_val == "キーワード") {
			  $('.wordsearch').css('display', 'block');
			  $('.titlesearch').css('display', 'none');
			  $('.textsearch').css('display', 'none');
		    } else if (search_val == "タイトル")  {
		    　$('.wordsearch').css('display', 'none');
			　$('.titlesearch').css('display', 'block');
			　$('.textsearch').css('display', 'none');
		    } else if (search_val == "本文") {
			    　$('.wordsearch').css('display', 'none');
				　$('.titlesearch').css('display', 'none');
				　$('.textsearch').css('display', 'block');
		    }
	  });
	  var input = document.querySelector('input');
	  var preview = document.querySelector('.preview');

	  input.style.opacity = 0;
	  input.addEventListener('change', updateFileDisplay);
	  function updateImageDisplay() {
		  while(preview.firstChild) {
		    preview.removeChild(preview.firstChild);
		  }		 
	  }
		  
});