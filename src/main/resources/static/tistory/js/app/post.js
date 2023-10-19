/**
 * 	post.js
 */
$(document).ready(function(){
	
	var ACCESS_TOKEN = getCookie('access_token');

	function getCookie(key) {
		let result = null;
		let cookie = document.cookie.split(';');
		cookie.some(function(item) {
			item = item.replace(' ', '');
				
			let dic = item.split('=');
			if(key === dic[0]) {
				result = dic[1];
				return true;
			}
		});
		return result;
	}

	console.log(ACCESS_TOKEN);

	if(ACCESS_TOKEN != null) {
		console.log(ACCESS_TOKEN);
				
		var header = ACCESS_TOKEN.split('.')[0];
		var payload = ACCESS_TOKEN.split('.')[1];
		var signature = ACCESS_TOKEN.split('.')[2];
				
		console.log(header);
		console.log(payload);
		console.log(signature);
				
		console.log("복호화 : " + Base64.decode(payload));
				
		// 2023-02-12 -> 토큰에 있는 payload에 실어논 정보를 Base64로 디코드해서 가져와 세팅(base64.min.js 필요)
		var data = JSON.parse(Base64.decode(payload));

		console.log(data.username);

		$.ajax({
			type : "GET",
			url : "/api/categories",
			contentType : "application/json; charset=UTF-8",
			headers: {
				"Authorization" : "Bearer " + ACCESS_TOKEN
			},
			success : function(res) {
				console.log(res);	
				
				// 2023-10-13 : 여기까지
				for(var i = 0; i < res.data.categories.length; i++) {
					$(".drawer-menu").append(`<li><a class="drawer-menu-item" href="#">${res.data.categories[i].title}</a></li>`);
				}
				
				for(var i = 0; i < res.data.categories.length; i++) {
					if(res.data.categories[i].categoryId == 1) {
						$("#categoryList").append(`<option value=${res.data.categories[i].categoryId} selected='selected'>${res.data.categories[i].title}</option>`);
						
					} else {
						$("#categoryList").append(`<option value=${res.data.categories[i].categoryId}>${res.data.categories[i].title}</option>`);
						
					}
				}

			},
			error : function(res) {
				console.log(res);
						
				alert(res.responseJSON.message);
				
				// 2023-10-11 : 쿠키는 항상 도메인 주소가 루트("/")로 설정되어 있어야 모든 요청에서 사용 가능 -> 자바스크립트 단에서도 path룰 /로 재설정 해줘야함 
				deleteCookie('access_token');	
				location.href = "/login";
				return;
			}
		});
		
		$("#categoryList").change(function(){
			categoryId = $(this).val();
		
			const categoryList = document.getElementById('categoryList');
			
			const length = categoryList.options.length;
			
			for(let i=0; i < length; i++) {
				if(categoryList.options[i].value == categoryId) {
					console.log(categoryList.options[i].value);
					// 1-1. selected 옵션 전체 삭제
					$(`#categoryList option`).attr("selected", false);
					
					// 1-2. 클릭한 곳에 selected 옵션 활성화
					$(`#categoryList option:eq(${i})`).attr("selected", true);
					
				}
			}
		
		});
		
		/**
		 *  4-2. 나의 포스팅 정보 불러오기
		 **/
		let my_post_list = $(".my_post_list").val();
		
		// 4-3. 토큰 속에 들어가있는 id값을 Number 형으로 바꿔서 가져온다.
		let principalId = Number(data.id);

		if(my_post_list != null) {

			$.ajax({
				type : "GET",
				url : `/api/posts/${principalId}`,
				contentType : "application/json; charset=UTF-8",
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					console.log(res);	
					
					// 2023-10-17 -> 여기까지(포스팅 데이터를 들고와서 뿌려봄)
					
					var html = ``;
					
					if(!res.data.posts.content.length) {
						html = '<p> colspan="53">등록된 포스팅이 없습니다.</p>';
					} else {
						for(var i = 0; i < res.data.posts.content.length; i++) {
							html += `
								<div class="my_post_list_item">
									<div class="my_post_list_item_left">
							`;
							
							if(res.data.posts.content[i].thumnailImgFileName == null) {
								html += `
									<img id="p_thumnail" src="/tistory/images/dog.jpg" width="150" height="150">
										<!--	<div class="my_fakeimg"></div>  -->
									</div>
								`;
							} else {
								html += `
									<img id="p_thumnail" src="/thumnail/${res.data.posts.content[i].thumnailImgFileName}" width="150" height="150">
										<!--	<div class="my_fakeimg"></div>  -->
									</div>
								`;
							}
							
							html += `
										
									<div class="my_post_list_item_right my_ellipsis">
										<div id="p_title" class="my_text_title my_ellipsis">${res.data.posts.content[i].title}</div>
										<div id="p_content" class="my_text_body_sm">${res.data.posts.content[i].content}</div>
	                	
										<div class="my_mt_md_1">
	     
	                    					<a href="" class="my_success_btn">Read More</a> 
										</div>
									</div>
								</div>

							`;
									
						}

						document.getElementById('postList').innerHTML = html;
						
						
					}
				
						
				},
				error : function(res) {
					console.log(res);
							
					alert(res.responseJSON.message);
					
					// 2023-10-11 : 쿠키는 항상 도메인 주소가 루트("/")로 설정되어 있어야 모든 요청에서 사용 가능 -> 자바스크립트 단에서도 path룰 /로 재설정 해줘야함 
					deleteCookie('access_token');	
					location.href = "/login";
					return;
				}
			});
		}
			
	}
	/**
	 *  4-1. 포스팅 쓰기
	 */
	$("#writeBtn").on("click", function() {
	
		let categoryId = $("#categoryList option:selected").val();
		let title = document.getElementById("title").value; 
		let content = document.getElementsByClassName("ql-editor")[0];
		let thumnailFile = $("#thumnailFile")[0].files[0];
		
		var formData = new FormData();
		
		formData.append("categoryId", categoryId);
		
		if(title.length != 0){
			formData.append("title", title);
		} else {
			alert("제목을 입력해주세요.");
			$("#title").focus();
			return;
		}
		
		if(content.innerText.trim().length != 0) {
			formData.append("content", content.innerHTML);
		} else {
			alert("내용을 입력해주세요.");
			$(".ql-editor")[0].focus();
			return;
		}
		
		if(thumnailFile != null) {
			formData.append("thumnailFile", thumnailFile);
		}
		
		if(ACCESS_TOKEN != null) {
			$.ajax({
				type : "POST",
				url : `/api/posts/write`,
				data: formData,
				contentType: false,
				processData: false,
				enctype: 'multipart/form-data',  
				headers: {
					"Authorization" : "Bearer " + ACCESS_TOKEN
				},
				success : function(res) {
					console.log(res);	
					
					if(res.code == 1) {
						alert(res.data.id + "번 포스트 글쓰기 성공");
						location.href = "/post/write";
					}
					
				},
				error : function(res) {
					console.log(res);
					
					if(res.responseJSON.data == null) {
						alert(res.responseJSON.message);
					} else {
						/* 2023-10-03 : 일단 이렇게 해놓으면 예외처리는 작동하나 수정 요망 */
					
						if(res.responseJSON.data.title) {
							alert(res.responseJSON.data.title);
						}
						
						if(res.responseJSON.data.content) {
							alert(res.responseJSON.data.content);
						}
						
					}
				}
				
			});	
		}
	});
});

//2023-10-10 : 엑세스 토큰 만료시간시 쿠키 삭제해주는 함수 -> 여기서 key 값은 'access_token'
function deleteCookie(key) {
	document.cookie = key + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;path=/;';
}
