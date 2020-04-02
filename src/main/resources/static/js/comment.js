window.addEventListener('DOMContentLoaded', function(){
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
	  
	  $('#uploadFile').on('change', function (e) {
		    var reader = new FileReader();
		    reader.onload = function (e) {
		        $("#preview").attr('src', e.target.result);
		    }
		    reader.readAsDataURL(e.target.files[0]);
		});
});

