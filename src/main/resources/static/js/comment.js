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
		    $('#preview').removeClass("d-none");
		    var reader = new FileReader();
		    reader.onload = function (e) {
		        $("#preview").attr('src', e.target.result);
		    }
		    reader.readAsDataURL(e.target.files[0]);
		});
	  
	  //コメント投稿フォームを開く
	    $('.open-button').on('click', function(){
	    $('.post-open').addClass("d-none");
	    $('.comment-form').removeClass("d-none");
	  })
	  
	  //トップへスクロール
	  function Scroll(){
	        $(window).scrollTop(0);
	      }
	    
	 //htmlの上書き   
	    function buildHTML(data){
	    	var html = `<div class="comment-contents" style="border-bottom:1px solid #DDE4E6; padding:10px 0;">
					<span>` + data.commentId +`.</span> <span
						>` + data.username +`</span> <span
						>` + data.createdAt +`</span>
					<div>
						<a
						   href="/boards/`+ data.boardId +`/comments/`+ data.commentId +`"
							class="my-2">タイトル: `+ data.title +`</a>
					</div>
					<div>`+ data.text +`</div>
					<div
						class="content-footer d-flex justify-content-end flex-row bd-highlight">
						<div
						style="line-height: 33px;">`+ data.imageText +`</div>
						<a 
							class="btn btn-info edit-btn"
							href="/boards/`+ data.boardId +`/comments/`+ data.commentId +`/edit">編集</a>
						<form 
							action="/boards/`+ data.boardId +`/comments/`+ data.commentId +`"
							method="POST">
							<button class="btn btn-info edit-btn">削除</button>
						</form>
					</div>
				</div>`;
	        return html;
	      }	   
	    //htmlの上書き2/コメント投稿数の更新
	    function buildHTML2(data){
	    	var html = `<div>`+ data.boardName +`(`+ data.commentsSize +`件)</div>`
	        return html;
	    }

	    //コメントの送信	      
	$('#data_upload_form').on('submit', function(e){
    e.preventDefault();
    var formData = new FormData(this);
//    formData.append(".title", $('.text').val());
//    formData.append(".text", $('.title').val());
//    formData.append(".image", $('.image').val());
    var url = $(this).attr('action');
    $.ajax({
      url: url,
      type: "POST",
      data: formData,
      dataType: 'json',
      processData: false,
      contentType: false
    })
    .done(function(data){
        var html = buildHTML(data);
        var html2 = buildHTML2(data);   
        $('.comment-index').prepend(html);
        $('.comment-size').html(html2);
	    $('.post-open').removeClass("d-none");
	    $('.comment-form').addClass("d-none");
	    $('#data_upload_form')[0].reset();
	    $('#preview').addClass("d-none");
        Scroll();
    })
    .fail(function(res) {
      alert('コメントの投稿に失敗しました');
    });
  })
});

