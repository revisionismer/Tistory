/**
 * 	board.js
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
		var header = ACCESS_TOKEN.split('.')[0];
		var payload = ACCESS_TOKEN.split('.')[1];
		var signature = ACCESS_TOKEN.split('.')[2];
		
		var data = JSON.parse(Base64.decode(payload));
		
		console.log(data.id);
		
		/**
		 *	5-1. 게시글 리스트
		 **/
		let boardList = $("#list").val();
		
		if(boardList != null) {
			
		}
		
		/**
		 *	5-2. 게시글 작성 폼 : 2023-11-24
		 **/
		let boardWriteForm = $("#boardWriteForm").val();
		
		if(boardWriteForm != null) {
			$("#writer").val(data.username);
			
			let boardId = $("#boardId").val();
	
			if(boardId.length !== 0) {
				$("#boardWriteBtn").hide();
			} else {
			
				$("#boardModifyBtn").hide();
			}
			
		}
		
	}
	
});

/**
 * 4-8. 상세보기 화면으로 값들고 가기. 
 **/
function writeBoard(principalId) {
	
	var form = document.createElement("form");
	form.setAttribute("action", "/post/detail");
	form.setAttribute("charset", "utf-8");
	form.setAttribute("method", "post");
	
	var principalId_field = document.createElement("input");
	principalId_field.setAttribute("type", "hidden");
	principalId_field.setAttribute("name", "principalId")
	principalId_field.setAttribute("value", principalId);
	form.appendChild(principalId_field);
	
	document.body.appendChild(form);
	form.submit();

}


//2023-10-10 : 엑세스 토큰 만료시간시 쿠키 삭제해주는 함수 -> 여기서 key 값은 'access_token'
function deleteCookie(key) {
	document.cookie = key + '=; expires=Thu, 01 Jan 1999 00:00:10 GMT;path=/;';
}

